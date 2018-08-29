package com.tokopedia.cacheapi.data.source;

import com.tokopedia.cacheapi.data.source.db.CacheApiDatabaseSource;
import com.tokopedia.cacheapi.data.source.db.model.CacheApiWhitelist;
import com.tokopedia.cacheapi.domain.model.CacheApiWhiteListDomain;
import com.tokopedia.cacheapi.exception.WhiteListNotFoundException;
import com.tokopedia.cacheapi.util.CacheApiLoggingUtils;

import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import okhttp3.Response;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by normansyahputa on 8/9/17.
 */

public class CacheApiDataSource {

    private final CacheApiDatabaseSource cacheApiDatabaseSource;

    public CacheApiDataSource(CacheApiDatabaseSource cacheApiDataManager) {
        this.cacheApiDatabaseSource = cacheApiDataManager;
    }

    public Observable<Boolean> bulkInsert(final List<CacheApiWhiteListDomain> domainList, final String versionName) {
        return cacheApiDatabaseSource.isWhiteListVersionUpdated(versionName).flatMap(new Func1<Boolean, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Boolean aBoolean) {
                CacheApiLoggingUtils.dumper(String.format("Need to update white list: %b", aBoolean));
                if (!aBoolean) {
                    return Observable.just(false);
                }
                return Observable.zip(deleteAllCacheData(), cacheApiDatabaseSource.deleteAllWhiteListData(), new Func2<Boolean, Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean aBoolean, Boolean aBoolean2) {
                        CacheApiLoggingUtils.dumper(String.format("Delete white list and cache finished"));
                        return true;
                    }
                }).flatMap(new Func1<Boolean, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Boolean aBoolean) {
                        return cacheApiDatabaseSource.insertWhiteList(domainList).flatMap(new Func1<Boolean, Observable<Boolean>>() {
                            @Override
                            public Observable<Boolean> call(Boolean aBoolean) {
                                if (!aBoolean) {
                                    return Observable.just(false);
                                }
                                return cacheApiDatabaseSource.updateCacheWhiteListVersion(versionName);
                            }
                        });
                    }
                });
            }
        });
    }

    public Observable<Boolean> saveResponse(String host, String path, final Response response) {
        return getWhiteList(host, path).flatMap(new Func1<CacheApiWhitelist, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(CacheApiWhitelist cacheApiWhitelist) {
                if (cacheApiWhitelist == null) {
                    return Observable.error(new WhiteListNotFoundException());
                }
                return cacheApiDatabaseSource.updateResponse(response, cacheApiWhitelist);
            }
        });
    }

    public Observable<Boolean> isInWhiteList(String host, String path) {
        return getWhiteList(host, path).flatMap(new Func1<CacheApiWhitelist, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(CacheApiWhitelist cacheApiWhitelist) {
                return Observable.just(cacheApiWhitelist != null);
            }
        });
    }

    public Observable<String> getCachedResponse(String host, String path, String param) {
        return cacheApiDatabaseSource.getCachedResponse(host, path, param);
    }

    private Observable<CacheApiWhitelist> getWhiteList(final String host, final String path) {
        return cacheApiDatabaseSource.getWhiteList(host, path).flatMap(new Func1<CacheApiWhitelist, Observable<CacheApiWhitelist>>() {
            @Override
            public Observable<CacheApiWhitelist> call(CacheApiWhitelist cacheApiWhitelist) {
                if (cacheApiWhitelist != null) {
                    return Observable.just(cacheApiWhitelist);
                }
                // Find with dynamic link
                return cacheApiDatabaseSource.getDynamicLinkWhiteList(host).flatMap(new Func1<List<CacheApiWhitelist>, Observable<CacheApiWhitelist>>() {
                    @Override
                    public Observable<CacheApiWhitelist> call(List<CacheApiWhitelist> cacheApiWhitelists) {
                        for (CacheApiWhitelist cacheApiWhitelist: cacheApiWhitelists) {
                            if (Pattern.matches(cacheApiWhitelist.getPath(), path)) {
                                CacheApiLoggingUtils.dumper(String.format("Compare : %s : %s", path, cacheApiWhitelist.getPath()));
                                return Observable.just(cacheApiWhitelist);
                            }
                        }
                        return Observable.just(null);
                    }
                });
            }
        });
    }

    public Observable<Boolean> deleteCachedData(String host, String path) {
        return cacheApiDatabaseSource.getWhiteList(host, path).flatMap(new Func1<CacheApiWhitelist, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(CacheApiWhitelist cacheApiWhitelist) {
                if (cacheApiWhitelist == null) {
                    return Observable.error(new WhiteListNotFoundException());
                }
                return cacheApiDatabaseSource.deleteCachedData(cacheApiWhitelist.getId());
            }
        });
    }

    public Observable<Boolean> deleteExpiredCachedData() {
        return cacheApiDatabaseSource.deleteExpiredCachedData();
    }

    public Observable<Boolean> deleteAllCacheData() {
        return cacheApiDatabaseSource.deleteAllCacheData();
    }
}