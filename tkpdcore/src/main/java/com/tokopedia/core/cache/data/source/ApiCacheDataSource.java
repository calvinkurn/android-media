package com.tokopedia.core.cache.data.source;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.language.Where;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.cache.data.source.db.CacheApiData;
import com.tokopedia.core.cache.data.source.db.CacheApiData_Table;
import com.tokopedia.core.cache.data.source.db.CacheApiWhitelist;
import com.tokopedia.core.cache.data.source.db.CacheApiWhitelist_Table;
import com.tokopedia.core.cache.domain.model.CacheApiDataDomain;
import com.tokopedia.core.cache.domain.model.CacheApiWhiteListDomain;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Calendar;
import java.util.List;

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

/**
 * Created by normansyahputa on 8/9/17.
 */

public class ApiCacheDataSource {
    private static final long DIVIDE_FOR_SECONDS = 1000L;
    private static final long DAFEAULT_MAX_CONTENT_LENGTH = 250000L;

    private static final Charset UTF8 = Charset.forName("UTF-8");
    private long maxContentLength = DAFEAULT_MAX_CONTENT_LENGTH;

    public ApiCacheDataSource() {

    }

    public Observable<Boolean> addWhiteListData(CacheApiWhitelist cacheApiWhitelist) {
        try {
            if (cacheApiWhitelist == null) {
                return Observable.just(false);
            }

            cacheApiWhitelist.save();

            return Observable.just(true);
        } catch (Exception e) {
            return Observable.just(false);
        }
    }

    public CacheApiWhitelist from(String host, String path, long expiredTime) {
        CacheApiWhitelist cacheApiWhitelist = new CacheApiWhitelist();
        cacheApiWhitelist.setHost(host);
        cacheApiWhitelist.setPath(path);
        cacheApiWhitelist.setExpiredTime(expiredTime);

        return cacheApiWhitelist;
    }

    public boolean queryFrom(String host, String path) {
        return queryFromRaw(host, path) != null;
    }

    public CacheApiWhitelist queryFromRaw(String host, String path) {
        return new Select()
                .from(CacheApiWhitelist.class)
                .where(CacheApiWhitelist_Table.host.eq(host))
                .and(CacheApiWhitelist_Table.path.eq(path))
                .querySingle();
    }

    public boolean isInWhiteList(final String host, final String path) {
        CacheApiWhitelist cacheApiWhitelist = queryFromRaw(host, path);
        return cacheApiWhitelist != null;
    }

    public CacheApiData queryDataFrom(String host, String path, String param) {
        Where<CacheApiData> and = new Select()
                .from(CacheApiData.class)
                .where(CacheApiData_Table.host.eq(host))
                .and(CacheApiData_Table.path.eq(path))
                .and(CacheApiData_Table.request_param.eq(param));
        CommonUtils.dumper("queryDataFrom : " + and.toString());
        return and.querySingle();
    }

    public List<CacheApiData> queryDataFrom(String host, String path) {
        Where<CacheApiData> and = new Select()
                .from(CacheApiData.class)
                .where(CacheApiData_Table.host.eq(host))
                .and(CacheApiData_Table.path.eq(path));
        CommonUtils.dumper("queryDataFrom : " + and.toString());
        return and.queryList();
    }

    public boolean delete(String host, String path) {
        new Delete()
                .from(CacheApiData.class)
                .where(CacheApiData_Table.host.eq(host))
                .and(CacheApiData_Table.path.eq(path))
                .execute();
        return true;
    }

    public void clearTimeout() {
        long currentTime = System.currentTimeMillis() / DIVIDE_FOR_SECONDS;
        List<CacheApiData> cacheApiDatas = new Select().from(CacheApiData.class).where(CacheApiData_Table.expired_time.lessThan(currentTime)).queryList();
        for (int i = 0; i < cacheApiDatas.size(); i++) {
            cacheApiDatas.get(i).delete();
        }
    }

    public void updateResponse(CacheApiData cacheApiData, CacheApiWhitelist cacheApiWhitelist, Response response) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND, (int) cacheApiWhitelist.getExpiredTime());
        cacheApiData.setResponseTime(System.currentTimeMillis() / DIVIDE_FOR_SECONDS);
        cacheApiData.setExpiredTime(instance.getTimeInMillis() / DIVIDE_FOR_SECONDS);

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
                CommonUtils.dumper("gzip encoded response was too long");
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

    public boolean singleDataDelete(CacheApiDataDomain cacheApiDataDomain) {
        if (cacheApiDataDomain == null) {
            return false;
        }

        return delete(cacheApiDataDomain.getHost(), cacheApiDataDomain.getPath());
    }

    public boolean singleDelete(CacheApiWhiteListDomain cacheApiWhiteListDomain) {
        CacheApiWhitelist cacheApiWhitelist = queryFromRaw(cacheApiWhiteListDomain.getHost(), cacheApiWhiteListDomain.getPath());
        cacheApiWhitelist.delete();
        return false;
    }
}