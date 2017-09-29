package com.tokopedia.core.cache.data.source;

import android.text.TextUtils;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.language.Where;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.cache.data.source.db.CacheApiData;
import com.tokopedia.core.cache.data.source.db.CacheApiData_Table;
import com.tokopedia.core.cache.data.source.db.CacheApiWhitelist;
import com.tokopedia.core.cache.data.source.db.CacheApiWhitelist_Table;
import com.tokopedia.core.cache.di.qualifier.ApiCacheQualifier;
import com.tokopedia.core.cache.di.qualifier.VersionNameQualifier;
import com.tokopedia.core.cache.domain.mapper.CacheApiWhiteListMapper;
import com.tokopedia.core.cache.domain.model.CacheApiDataDomain;
import com.tokopedia.core.cache.domain.model.CacheApiWhiteListDomain;
import com.tokopedia.core.cache.util.CacheApiUtils;
import com.tokopedia.core.var.TkpdCache;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Calendar;
import java.util.Collection;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by normansyahputa on 8/9/17.
 */

public class ApiCacheDataSource {
    private static final long DIVIDE_FOR_SECONDS = 1000L;

    private static final Charset UTF8 = Charset.forName("UTF-8");

    private LocalCacheHandler localCacheHandler;
    private String versionName;

    @Inject
    public ApiCacheDataSource(@ApiCacheQualifier LocalCacheHandler localCacheHandler, @VersionNameQualifier String versionName) {
        this.versionName = versionName;
        this.localCacheHandler = localCacheHandler;
    }

    private Observable<Boolean> isWhiteListVersionUpdated() {
        String storedVersionName = localCacheHandler.getString(TkpdCache.Key.WHITE_LIST_VERSION);
        CommonUtils.dumper(String.format("Current vs local version: %s - %s", storedVersionName, versionName));
        // Fresh install or different version
        boolean whiteListVersionUpdated = TextUtils.isEmpty(storedVersionName) || !storedVersionName.equals(versionName);
        return Observable.just(whiteListVersionUpdated);
    }

    public Observable<Boolean> bulkInsert(final Collection<CacheApiWhiteListDomain> cacheApiDatas) {
        return isWhiteListVersionUpdated().flatMap(new Func1<Boolean, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Boolean aBoolean) {
                CommonUtils.dumper(String.format("Need to update white list: %b", aBoolean));
                if (!aBoolean) {
                    return Observable.just(false);
                }
                return Observable.zip(deleteAllCacheData(), deleteAllWhiteListData(), new Func2<Boolean, Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean aBoolean, Boolean aBoolean2) {
                        CommonUtils.dumper(String.format("Delete white list and cache finished"));
                        return true;
                    }
                }).flatMap(new Func1<Boolean, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Boolean aBoolean) {
                        return insertWhiteList(cacheApiDatas).flatMap(new Func1<Boolean, Observable<Boolean>>() {
                            @Override
                            public Observable<Boolean> call(Boolean aBoolean) {
                                if (!aBoolean) {
                                    return Observable.just(false);
                                }
                                return updateLocalCacheWhiteListVersion();
                            }
                        });
                    }

                    private Observable<Boolean> updateLocalCacheWhiteListVersion() {
                        localCacheHandler.putString(TkpdCache.Key.WHITE_LIST_VERSION, versionName);
                        localCacheHandler.applyEditor();
                        return Observable.just(true);
                    }
                });
            }
        });
    }

    public Observable<CacheApiWhitelist> getWhiteList(final String host, final String path) {
        return Observable.create(new Observable.OnSubscribe<CacheApiWhitelist>() {
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
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                CommonUtils.dumper(String.format("Inserting White List"));
                for (CacheApiWhiteListDomain cacheApiWhiteListDomain: cacheApiDatas) {
                    CacheApiWhitelist whiteList = CacheApiWhiteListMapper.from(cacheApiWhiteListDomain);
                    CommonUtils.dumper(String.format("Insert white list: %s - %s", whiteList.getHost(), whiteList.getPath()));
                    whiteList.save();
                }
                subscriber.onNext(true);
            }
        });
    }

    public Observable<Boolean> deleteAllWhiteListData() {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                SQLite.delete(CacheApiWhitelist.class).execute();
                subscriber.onNext(true);
            }
        });
    }

    public Observable<CacheApiData> getCachedData(final String host, final String path, final String param) {
        return Observable.create(new Observable.OnSubscribe<CacheApiData>() {
            @Override
            public void call(Subscriber<? super CacheApiData> subscriber) {
                Where<CacheApiData> selection = new Select()
                        .from(CacheApiData.class)
                        .where(CacheApiData_Table.host.eq(host))
                        .and(CacheApiData_Table.path.eq(path))
                        .and(CacheApiData_Table.request_param.eq(param));
                CommonUtils.dumper("CachedData : " + selection.toString());
                subscriber.onNext(selection.querySingle());
            }
        });
    }

    public Observable<Boolean> deleteAllCacheData() {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                SQLite.delete(CacheApiData.class).execute();
                subscriber.onNext(true);
            }
        });
    }

    public Observable<Boolean> clearTimeout() {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
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

    public Observable<Boolean> deleteCachedData(final CacheApiDataDomain cacheApiDataDomain) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                new Delete()
                        .from(CacheApiData.class)
                        .where(CacheApiData_Table.host.eq(cacheApiDataDomain.getHost()))
                        .and(CacheApiData_Table.path.eq(cacheApiDataDomain.getPath()))
                        .execute();
                subscriber.onNext(true);
            }
        });
    }

    public Observable<Boolean> deleteWhiteList(final CacheApiWhiteListDomain cacheApiWhiteListDomain) {
        return getWhiteList(cacheApiWhiteListDomain.getHost(), cacheApiWhiteListDomain.getPath()).flatMap(new Func1<CacheApiWhitelist, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(CacheApiWhitelist cacheApiWhitelist) {
                cacheApiWhitelist.delete();
                return Observable.just(true);
            }
        });
    }

    public Observable<Boolean> updateResponse(final CacheApiData cacheApiData, final CacheApiWhitelist cacheApiWhitelist, final Response response) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                Calendar expiredCalendar = Calendar.getInstance();
                expiredCalendar.add(Calendar.SECOND, (int) cacheApiWhitelist.getExpiredTime());
                cacheApiData.setResponseTime(System.currentTimeMillis() / DIVIDE_FOR_SECONDS);
                cacheApiData.setExpiredTime(expiredCalendar.getTimeInMillis() / DIVIDE_FOR_SECONDS);
                try {
                    putResponseBody(cacheApiData, response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cacheApiData.save();
                subscriber.onNext(true);
            }
        });
    }

    private void putResponseBody(CacheApiData cacheApiData, Response response) throws IOException {
        ResponseBody responseBody = response.body();
        if (HttpHeaders.hasBody(response)) {
            BufferedSource source = CacheApiUtils.getNativeSource(response);
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.buffer();
            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(UTF8);
                } catch (UnsupportedCharsetException e) {
//                    update(transaction, transactionUri);
                }
            }
            if (CacheApiUtils.isPlaintext(buffer)) {
                cacheApiData.setResponseBody(CacheApiUtils.readFromBuffer(buffer.clone(), charset));
            }
        }
    }
}