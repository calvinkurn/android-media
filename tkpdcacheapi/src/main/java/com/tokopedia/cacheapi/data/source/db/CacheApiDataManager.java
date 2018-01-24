package com.tokopedia.cacheapi.data.source.db;

import android.text.TextUtils;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.language.Where;
import com.tokopedia.cacheapi.domain.mapper.CacheApiWhiteListMapper;
import com.tokopedia.cacheapi.domain.model.CacheApiWhiteListDomain;
import com.tokopedia.cacheapi.util.CacheApiUtils;
import com.tokopedia.cacheapi.util.LoggingUtils;
import com.tokopedia.cacheapi.util.EncryptionUtils;

import java.util.Collection;

import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by nathan on 9/30/17.
 */

public class CacheApiDataManager {

    private static final long DIVIDE_FOR_SECONDS = 1000L;

    private static final String CACHE_API_KEY = "BU}~GV2(K)%z$1+H";

    public CacheApiDataManager() {

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
                LoggingUtils.dumper(String.format("Inserting White List"));
                for (CacheApiWhiteListDomain cacheApiWhiteListDomain : cacheApiDatas) {
                    CacheApiWhitelist whiteList = CacheApiWhiteListMapper.from(cacheApiWhiteListDomain);
                    LoggingUtils.dumper(String.format("Insert white list: %s - %s", whiteList.getHost(), whiteList.getPath()));
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
                Where<CacheApiData> selection = new Select()
                        .from(CacheApiData.class)
                        .where(CacheApiData_Table.host.eq(host))
                        .and(CacheApiData_Table.path.eq(path))
                        .and(CacheApiData_Table.request_param.eq(getEncrypted(param)));
                LoggingUtils.dumper("CachedData : " + selection.toString());
                CacheApiData cacheApiData = selection.querySingle();
                String cachedResponseBody = null;
                if (cacheApiData != null) {
                    cachedResponseBody = cacheApiData.getResponseBody();
                    cachedResponseBody = getDecrypted(cachedResponseBody);
                }
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
                subscriber.onNext(true);
            }
        });
    }

    public Observable<Boolean> deleteWhiteList(final String host, final String path) {
        return getWhiteList(host, path).flatMap(new Func1<CacheApiWhitelist, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(CacheApiWhitelist cacheApiWhitelist) {
                cacheApiWhitelist.delete();
                return Observable.just(true);
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