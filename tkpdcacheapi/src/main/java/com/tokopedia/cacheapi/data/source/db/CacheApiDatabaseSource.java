package com.tokopedia.cacheapi.data.source.db;

import android.text.TextUtils;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.language.Where;
import com.tokopedia.cacheapi.exception.VersionNameNotValidException;
import com.tokopedia.cacheapi.data.source.db.model.CacheApiData;
import com.tokopedia.cacheapi.data.source.db.model.CacheApiData_Table;
import com.tokopedia.cacheapi.data.source.db.model.CacheApiVersion;
import com.tokopedia.cacheapi.data.source.db.model.CacheApiWhitelist;
import com.tokopedia.cacheapi.data.source.db.model.CacheApiWhitelist_Table;
import com.tokopedia.cacheapi.domain.mapper.CacheApiWhiteListMapper;
import com.tokopedia.cacheapi.domain.model.CacheApiWhiteListDomain;
import com.tokopedia.cacheapi.util.CacheApiUtils;
import com.tokopedia.cacheapi.util.EncryptionUtils;
import com.tokopedia.cacheapi.util.CacheApiLoggingUtils;

import java.util.Collection;
import java.util.UUID;

import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by nathan on 9/30/17.
 */

public class CacheApiDatabaseSource {

    private static final long DIVIDE_FOR_SECONDS = 1000L;

    private static final String CACHE_API_KEY = "BU}~GV2(K)%z$1+H";

    public Observable<Boolean> isWhiteListVersionUpdated(final String versionName) {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (TextUtils.isEmpty(versionName)) {
                    subscriber.onError(new VersionNameNotValidException());
                    return;
                }
                String storedVersionName = "";
                CacheApiVersion cacheApiVersion = new Select()
                        .from(CacheApiVersion.class)
                        .querySingle();
                if (cacheApiVersion != null) {
                    storedVersionName = cacheApiVersion.getVersion();
                }
                CacheApiLoggingUtils.dumper(String.format("Stored vs current version: %s - %s", storedVersionName, versionName));
                // Fresh install or different version
                boolean needUpdate;
                if (TextUtils.isEmpty(storedVersionName)) {
                    needUpdate = true;
                } else {
                    needUpdate = !storedVersionName.equalsIgnoreCase(versionName);
                }
                subscriber.onNext(needUpdate);
            }
        });
    }

    public Observable<Boolean> updateCacheWhiteListVersion(final String versionName) {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (TextUtils.isEmpty(versionName)) {
                    subscriber.onError(new VersionNameNotValidException());
                    return;
                }
                new Delete().from(CacheApiVersion.class).execute();
                CacheApiVersion cacheApiVersion = new CacheApiVersion();
                cacheApiVersion.setVersion(versionName);
                cacheApiVersion.save();
                subscriber.onNext(true);
            }
        });
    }

    public Observable<CacheApiWhitelist> getWhiteList(final String host, final String path) {
        return Observable.unsafeCreate(new Observable.OnSubscribe<CacheApiWhitelist>() {
            @Override
            public void call(Subscriber<? super CacheApiWhitelist> subscriber) {
                subscriber.onNext(new Select()
                        .from(CacheApiWhitelist.class)
                        .where(CacheApiWhitelist_Table.host.eq(host))
                        .and(CacheApiWhitelist_Table.path.eq(path))
                        .querySingle());
            }
        });
    }

    public Observable<Boolean> isInWhiteList(final String host, final String path) {
        return getWhiteList(host, path).flatMap(new Func1<CacheApiWhitelist, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(CacheApiWhitelist cacheApiWhitelist) {
                return Observable.just(cacheApiWhitelist != null);
            }
        });
    }

    public Observable<Boolean> insertWhiteList(final Collection<CacheApiWhiteListDomain> cacheApiDatas) {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                CacheApiLoggingUtils.dumper(String.format("Inserting White List"));
                int i = 0;
                for (CacheApiWhiteListDomain cacheApiWhiteListDomain : cacheApiDatas) {
                    CacheApiWhitelist whiteList = CacheApiWhiteListMapper.from(cacheApiWhiteListDomain);
                    whiteList.setId(i++);
                    CacheApiLoggingUtils.dumper(String.format("Insert white list: %s - %s (id:%s)", whiteList.getHost(), whiteList.getPath(), whiteList.getId()));
                    whiteList.save();
                }
                subscriber.onNext(true);
            }
        });
    }

    public Observable<Boolean> deleteAllWhiteListData() {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                new Delete().from(CacheApiWhitelist.class).execute();
                subscriber.onNext(true);
            }
        });
    }

    public Observable<String> getCachedResponse(final String host, final String path, final String param) {
        return Observable.unsafeCreate(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                CacheApiLoggingUtils.dumper(String.format("Query cache: %s - %s - %s", host, path, param));
                Where<CacheApiData> selection = new Select()
                        .from(CacheApiData.class)
                        .where(CacheApiData_Table.host.eq(host))
                        .and(CacheApiData_Table.path.eq(path))
                        .and(CacheApiData_Table.request_param.eq(getEncrypted(param)));
                CacheApiData cacheApiData = selection.querySingle();
                String cachedResponseBody = null;
                if (cacheApiData != null) {
                    cachedResponseBody = cacheApiData.getResponseBody();
                    cachedResponseBody = getDecrypted(cachedResponseBody);
                }
                CacheApiLoggingUtils.dumper("CachedData : " + cachedResponseBody);
                subscriber.onNext(cachedResponseBody);
            }
        });
    }

    public Observable<Boolean> deleteAllCacheData() {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                new Delete().from(CacheApiData.class).execute();
                subscriber.onNext(true);
            }
        });
    }

    public Observable<Boolean> deleteExpiredCachedData() {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                long currentTime = System.currentTimeMillis() / DIVIDE_FOR_SECONDS;
                new Delete()
                        .from(CacheApiData.class)
                        .where(CacheApiData_Table.expired_time.lessThan(currentTime))
                        .execute();
                subscriber.onNext(true);
            }
        });
    }

    public Observable<Boolean> deleteCachedData(final String host, final String path) {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                new Delete()
                        .from(CacheApiData.class)
                        .where(CacheApiData_Table.host.eq(host))
                        .and(CacheApiData_Table.path.eq(path))
                        .execute();
                CacheApiLoggingUtils.dumper(String.format("Cache deleted: %s - %s", host, path));
                subscriber.onNext(true);
            }
        });
    }

    public Observable<Boolean> updateResponse(final Response response, final int expiredTime) {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                String responseBody = CacheApiUtils.getResponseBody(response);
                if (TextUtils.isEmpty(responseBody)) {
                    subscriber.onNext(false);
                    return;
                }
                long responseTime = System.currentTimeMillis() / DIVIDE_FOR_SECONDS;

                CacheApiData cacheApiData = new CacheApiData();
                cacheApiData.setMethod(response.request().method());
                cacheApiData.setHost(response.request().url().host());
                cacheApiData.setPath(CacheApiUtils.getPath(response.request().url().toString()));
                cacheApiData.setRequestParam(getEncrypted(CacheApiUtils.getRequestParam(response.request())));
                cacheApiData.setResponseBody(getEncrypted(responseBody));
                cacheApiData.setResponseTime(responseTime);
                cacheApiData.setExpiredTime(responseTime + expiredTime);
                cacheApiData.save();
                subscriber.onNext(true);
            }
        });
    }

    private String getEncrypted(String text) {
        return EncryptionUtils.Encrypt(text, CACHE_API_KEY);
    }

    private String getDecrypted(String text) {
        return EncryptionUtils.Decrypt(text, CACHE_API_KEY);
    }
}