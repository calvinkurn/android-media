package com.tokopedia.core.cache.data.repository;

import android.support.annotation.Nullable;
import android.util.Log;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.base.di.qualifier.ApiCacheQualifier;
import com.tokopedia.core.base.di.qualifier.VersionNameQualifier;
import com.tokopedia.core.cache.data.source.cache.CacheHelper;
import com.tokopedia.core.cache.data.source.db.CacheApiData;
import com.tokopedia.core.cache.data.source.db.CacheApiWhitelist;
import com.tokopedia.core.cache.domain.ApiCacheRepository;
import com.tokopedia.core.cache.domain.mapper.CacheApiWhiteListMapper;
import com.tokopedia.core.cache.domain.model.CacheApiDataDomain;
import com.tokopedia.core.cache.domain.model.CacheApiWhiteListDomain;
import com.tokopedia.core.var.TkpdCache;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Calendar;
import java.util.Collection;

import javax.inject.Inject;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;
import okio.Okio;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by normansyahputa on 8/14/17.
 */

public class ApiCacheRepositoryImpl implements ApiCacheRepository {

    private static final String TAG = "ApiCacheRepositoryImpl";
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private LocalCacheHandler localCacheHandler;
    private String versionName;
    private CacheHelper cacheHelper;
    private long maxContentLength = 250000L;

    @Inject
    public ApiCacheRepositoryImpl(@ApiCacheQualifier LocalCacheHandler localCacheHandler, @VersionNameQualifier String versionName, CacheHelper cacheHelper) {
        this.localCacheHandler = localCacheHandler;
        this.versionName = versionName;
        this.cacheHelper = cacheHelper;
    }

    /**
     * this is for older class.
     * removed if no longer used
     */
    public static void DeleteAllCache() {
        deleteAllCacheData();
        deleteAllWhiteLists();
    }

    protected static void deleteAllCacheData() {
        SQLite.delete(CacheApiData.class).execute();
    }

    protected static void deleteAllWhiteLists() {
        SQLite.delete(CacheApiWhitelist.class).execute();
    }

    @Override
    public Observable<Boolean> checkVersion() {
        if (localCacheHandler.getString(TkpdCache.Key.VERSION_NAME_IN_CACHE) == null) {// fresh install
            // update version name
            localCacheHandler.putString(TkpdCache.Key.VERSION_NAME_IN_CACHE, versionName);
            localCacheHandler.applyEditor();
            return Observable.just(false);
        } else if (localCacheHandler.getString(TkpdCache.Key.VERSION_NAME_IN_CACHE) != null && localCacheHandler.getString(TkpdCache.Key.VERSION_NAME_IN_CACHE).equals(versionName)) {
            // update version name
            localCacheHandler.putString(TkpdCache.Key.VERSION_NAME_IN_CACHE, versionName);
            localCacheHandler.applyEditor();
            return Observable.just(false);
        } else {
            return Observable.just(true);
        }
    }

    @Override
    public Observable<Boolean> bulkInsert(final Collection<CacheApiWhiteListDomain> cacheApiDatas) {
        return checkVersion().flatMap(new Func1<Boolean, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Boolean aBoolean) {
                if (!aBoolean) {// if version is upgdated

                    deleteAllCache();

                    Observable.from(cacheApiDatas)
                            .flatMap(new Func1<CacheApiWhiteListDomain, Observable<CacheApiWhitelist>>() {
                                @Override
                                public Observable<CacheApiWhitelist> call(CacheApiWhiteListDomain cacheApiWhiteListDomain) {
                                    CacheApiWhitelist from = CacheApiWhiteListMapper.from(cacheApiWhiteListDomain);
                                    from.save();
                                    return Observable.just(from);
                                }
                            }).toList().toBlocking().first();
                }
                return Observable.just(aBoolean);
            }
        });
    }

    @Override
    public Observable<Boolean> singleDelete(@Nullable CacheApiWhiteListDomain cacheApiWhiteListDomain) {
        return Observable.just(cacheApiWhiteListDomain).map(new Func1<CacheApiWhiteListDomain, Object>() {
            @Override
            public Object call(CacheApiWhiteListDomain cacheApiWhiteListDomain) {
                CacheApiWhitelist cacheApiWhitelist = cacheHelper.queryFromRaw(cacheApiWhiteListDomain.getHost(), cacheApiWhiteListDomain.getPath());
                cacheApiWhitelist.delete();
                return null;
            }
        }).map(new Func1<Object, Boolean>() {
            @Override
            public Boolean call(Object o) {
                return false;
            }
        });
    }

    @Override
    public Observable<Boolean> singleDataDelete(@Nullable final CacheApiDataDomain cacheApiDataDomain) {
        return Observable.just(true).flatMap(new Func1<Boolean, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Boolean aBoolean) {
                if (cacheApiDataDomain == null) {
                    return Observable.just(false);
                }

                CacheApiData cacheApiData = cacheHelper.queryDataFrom(cacheApiDataDomain.getHost(), cacheApiDataDomain.getPath(), cacheApiDataDomain.getParam());
                if (cacheApiData == null) {
                    return Observable.just(false);
                } else {
                    cacheApiData.delete();
                    return Observable.just(true);
                }
            }
        });
    }

    @Override
    public boolean isInWhiteList(final String host, final String path) {
        CacheApiWhitelist cacheApiWhitelist = cacheHelper.queryFromRaw(host, path);
        return cacheApiWhitelist != null;
    }

    @Override
    public CacheApiWhitelist isInWhiteListRaw(final String host, final String path) {
        CacheApiWhitelist cacheApiWhitelist = cacheHelper.queryFromRaw(host, path);
        return cacheApiWhitelist;
    }

    @Override
    public void deleteAllCache() {
        deleteAllCacheData();
    }

    @Override
    public void clearTimeout() {
        cacheHelper.clearTimeout();
    }

    @Override
    public CacheApiData queryDataFrom(String host, String path, String requestParam) {
        return cacheHelper.queryDataFrom(host, path, requestParam);
    }

    @Override
    public void updateResponse(CacheApiData cacheApiData, CacheApiWhitelist cacheApiWhitelist, Response response) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND, (int) cacheApiWhitelist.getExpiredTime());
        cacheApiData.setResponseDate(System.currentTimeMillis() / 1000L);
        cacheApiData.setExpiredDate(instance.getTimeInMillis() / 1000L);

        try {
            putResponseBody(cacheApiData, response);
        } catch (IOException e) {
            e.printStackTrace();
        }

        cacheApiData.save();
    }

    private void putResponseBody(CacheApiData cacheApiData, Response response) throws IOException {
        ResponseBody responseBody = response.body();
        if (HttpHeaders.hasBody(response)) {
            BufferedSource source = getNativeSource(response);
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
            if (isPlaintext(buffer)) {
                cacheApiData.setResponseBody(readFromBuffer(buffer.clone(), charset));
            }
//            else {
//                transaction.setResponseBodyIsPlainText(false);
//            }
//            transaction.setResponseContentLength(buffer.size());
        }
    }

    private String readFromBuffer(Buffer buffer, Charset charset) {
        long bufferSize = buffer.size();
        long maxBytes = Math.min(bufferSize, maxContentLength);
        String body = "";
        try {
            body = buffer.readString(maxBytes, charset);
        } catch (EOFException e) {
//            body += context.getString(com.readystatesoftware.chuck.R.string.chuck_body_unexpected_eof);
        }
        if (bufferSize > maxContentLength) {
//            body += context.getString(com.readystatesoftware.chuck.R.string.chuck_body_content_truncated);
        }
        return body;
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    private boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private boolean bodyGzipped(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return "gzip".equalsIgnoreCase(contentEncoding);
    }

    private BufferedSource getNativeSource(Response response) throws IOException {
        if (bodyGzipped(response.headers())) {
            BufferedSource source = response.peekBody(maxContentLength).source();
            if (source.buffer().size() < maxContentLength) {
                return getNativeSource(source, true);
            } else {
                Log.w(TAG, "gzip encoded response was too long");
            }
        }
        return response.body().source();
    }

    private BufferedSource getNativeSource(BufferedSource input, boolean isGzipped) {
        if (isGzipped) {
            GzipSource source = new GzipSource(input);
            return Okio.buffer(source);
        } else {
            return input;
        }
    }

}
