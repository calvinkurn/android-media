package com.tokopedia.cacheapi.data.source.db;

import android.text.TextUtils;

import com.tokopedia.cacheapi.data.source.db.dao.CacheApiDataDao;
import com.tokopedia.cacheapi.data.source.db.dao.CacheApiVersionDao;
import com.tokopedia.cacheapi.data.source.db.dao.CacheApiWhitelistDao;
import com.tokopedia.cacheapi.data.source.db.model.CacheApiData;
import com.tokopedia.cacheapi.data.source.db.model.CacheApiVersion;
import com.tokopedia.cacheapi.data.source.db.model.CacheApiWhitelist;
import com.tokopedia.cacheapi.domain.model.CacheApiWhiteListDomain;
import com.tokopedia.cacheapi.exception.VersionNameNotValidException;
import com.tokopedia.cacheapi.util.CacheApiLoggingUtils;
import com.tokopedia.cacheapi.util.CacheApiUtils;
import com.tokopedia.cacheapi.util.EncryptionUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by nathan on 9/30/17.
 */

public class CacheApiDatabaseSource {

    private static final String CACHE_API_KEY = "BU}~GV2(K)%z$1+H";

    private CacheApiDataDao cacheApiDataDao;
    private CacheApiVersionDao cacheApiVersionDao;
    private CacheApiWhitelistDao cacheApiWhitelistDao;

    public CacheApiDatabaseSource(CacheApiVersionDao cacheApiVersionDao, CacheApiWhitelistDao cacheApiWhitelistDao, CacheApiDataDao cacheApiDataDao) {
        this.cacheApiVersionDao = cacheApiVersionDao;
        this.cacheApiWhitelistDao = cacheApiWhitelistDao;
        this.cacheApiDataDao = cacheApiDataDao;
    }

    public Observable<Boolean> isWhiteListVersionUpdated(final String versionName) {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (TextUtils.isEmpty(versionName)) {
                    subscriber.onError(new VersionNameNotValidException());
                    return;
                }
                String storedVersionName = "";
                CacheApiVersion cacheApiVersion = cacheApiVersionDao.getCurrentVersion();
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
                cacheApiVersionDao.deleteAll();
                CacheApiVersion cacheApiVersion = new CacheApiVersion(versionName);
                cacheApiVersionDao.insert(cacheApiVersion);
                subscriber.onNext(true);
            }
        });
    }

    public Observable<Boolean> insertWhiteList(final List<CacheApiWhiteListDomain> cacheApiDataList) {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                CacheApiLoggingUtils.dumper(String.format("Inserting White List"));
                int i = 0;
                for (CacheApiWhiteListDomain cacheApiWhiteListDomain : cacheApiDataList) {
                    CacheApiWhitelist cacheApiWhitelist = new CacheApiWhitelist(i++,
                            getEncrypted(cacheApiWhiteListDomain.getHost()),
                            getEncrypted(cacheApiWhiteListDomain.getPath()),
                            cacheApiWhiteListDomain.getExpireTime(),
                            cacheApiWhiteListDomain.isDynamicUrl());
                    CacheApiLoggingUtils.dumper(String.format("Insert white list: %s - %s (id:%s)", cacheApiWhitelist.getHost(), cacheApiWhitelist.getPath(), cacheApiWhitelist.getId()));
                    cacheApiWhitelistDao.insert(cacheApiWhitelist);
                }
                subscriber.onNext(true);
            }
        });
    }
    
    public Observable<CacheApiWhitelist> getWhiteList(final String host, final String path) {
        return Observable.unsafeCreate(new Observable.OnSubscribe<CacheApiWhitelist>() {
            @Override
            public void call(Subscriber<? super CacheApiWhitelist> subscriber) {
                CacheApiWhitelist cacheApiWhitelist = cacheApiWhitelistDao.getData(getEncrypted(host), getEncrypted(path));
                if (cacheApiWhitelist != null) {
                    cacheApiWhitelist.setHost(getDecrypted(cacheApiWhitelist.getHost()));
                    cacheApiWhitelist.setPath(getDecrypted(cacheApiWhitelist.getPath()));
                }
                subscriber.onNext(cacheApiWhitelist);
            }
        });
    }

    public Observable<List<CacheApiWhitelist>> getDynamicLinkWhiteList(final String host) {
        return Observable.unsafeCreate(new Observable.OnSubscribe<List<CacheApiWhitelist>>() {
            @Override
            public void call(Subscriber<? super List<CacheApiWhitelist>> subscriber) {
                List<CacheApiWhitelist> cacheApiWhitelistList = cacheApiWhitelistDao.getHostList(true, getEncrypted(host));

                for (CacheApiWhitelist cacheApiWhitelist : cacheApiWhitelistList) {
                    cacheApiWhitelist.setHost(getDecrypted(cacheApiWhitelist.getHost()));
                    cacheApiWhitelist.setPath(getDecrypted(cacheApiWhitelist.getPath()));
                }
                subscriber.onNext(cacheApiWhitelistList);
            }
        });
    }

    public Observable<String> getCachedResponse(final String host, final String path, final String param) {
        return Observable.unsafeCreate(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                CacheApiLoggingUtils.dumper(String.format("Query cache: %s - %s - %s", host, path, param));
                CacheApiData cacheApiData = cacheApiDataDao.getData(getEncrypted(host), getEncrypted(path), getEncrypted(param));
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

    public Observable<Boolean> updateResponse(final Response response, final CacheApiWhitelist cacheApiWhitelist) {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                String responseBody = CacheApiUtils.getResponseBody(response);
                if (TextUtils.isEmpty(responseBody)) {
                    subscriber.onNext(false);
                    return;
                }
                long responseTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());

                CacheApiData cacheApiData = new CacheApiData(
                        getEncrypted(response.request().url().host()),
                        getEncrypted(CacheApiUtils.getPath(response.request().url().toString())),
                        getEncrypted(CacheApiUtils.getRequestParam(response.request())),
                        response.request().method(),
                        getEncrypted(responseBody),
                        responseTime,
                        responseTime + cacheApiWhitelist.getExpiredTime(),
                        cacheApiWhitelist.getId()
                );
                cacheApiDataDao.insert(cacheApiData);
                subscriber.onNext(true);
            }
        });
    }

    public Observable<Boolean> deleteAllWhiteListData() {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                cacheApiWhitelistDao.deleteAll();
                subscriber.onNext(true);
            }
        });
    }

    public Observable<Boolean> deleteAllCacheData() {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                cacheApiDataDao.deleteAll();
                subscriber.onNext(true);
            }
        });
    }

    public Observable<Boolean> deleteExpiredCachedData() {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                long currentTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
                cacheApiDataDao.deleteOldData(currentTime);
                subscriber.onNext(true);
            }
        });
    }

    public Observable<Boolean> deleteCachedData(final long whiteListId) {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                cacheApiDataDao.deleteWhiteList(whiteListId);
                CacheApiLoggingUtils.dumper(String.format("Cache whiteListId deleted: %s", whiteListId));
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