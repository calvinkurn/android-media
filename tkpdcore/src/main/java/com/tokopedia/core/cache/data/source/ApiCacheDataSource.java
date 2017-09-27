package com.tokopedia.core.cache.data.source;

import android.text.TextUtils;

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
import com.tokopedia.core.cache.util.CacheApiUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by normansyahputa on 8/9/17.
 */

public class ApiCacheDataSource {
    private static final long DIVIDE_FOR_SECONDS = 1000L;

    private static final Charset UTF8 = Charset.forName("UTF-8");

    public ApiCacheDataSource() {

    }

    public CacheApiWhitelist getWhiteList(String host, String path) {
        return new Select()
                .from(CacheApiWhitelist.class)
                .where(CacheApiWhitelist_Table.host.eq(host))
                .and(CacheApiWhitelist_Table.path.eq(path))
                .querySingle();
    }

    public boolean isInWhiteList(final String host, final String path) {
        CacheApiWhitelist cacheApiWhitelist = getWhiteList(host, path);
        return cacheApiWhitelist != null;
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

    public Observable<Boolean> clearTimeout() {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                long currentTime = System.currentTimeMillis() / DIVIDE_FOR_SECONDS;
                List<CacheApiData> cacheApiDatas = new Select()
                        .from(CacheApiData.class)
                        .where(CacheApiData_Table.expired_time.lessThan(currentTime))
                        .queryList();
                for (int i = 0; i < cacheApiDatas.size(); i++) {
                    cacheApiDatas.get(i).delete();
                }
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
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                CacheApiWhitelist cacheApiWhitelist = getWhiteList(cacheApiWhiteListDomain.getHost(), cacheApiWhiteListDomain.getPath());
                cacheApiWhitelist.delete();
                subscriber.onNext(true);
            }
        });
    }

    public void updateResponse(CacheApiData cacheApiData, CacheApiWhitelist cacheApiWhitelist, Response response) {
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