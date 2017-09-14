package com.tokopedia.core.cache.interceptor;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.cache.data.repository.ApiCacheRepositoryImpl;
import com.tokopedia.core.cache.data.source.ApiCacheDataSource;
import com.tokopedia.core.cache.domain.ApiCacheRepository;
import com.tokopedia.core.cache.domain.interactor.BaseApiCacheInterceptorUseCase;
import com.tokopedia.core.cache.domain.interactor.CheckWhiteListUseCase;
import com.tokopedia.core.cache.domain.interactor.ClearTimeOutCache;
import com.tokopedia.core.cache.domain.interactor.GetCacheDataUseCaseUseCase;
import com.tokopedia.core.cache.domain.interactor.SaveToDbUseCase;
import com.tokopedia.core.var.TkpdCache;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by normansyahputa on 8/9/17.
 */

public class ApiCacheInterceptor implements Interceptor {
    private static final String LOG_TAG = "ApiCacheInterceptor";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        ThreadExecutor threadExecutor = new JobExecutor();
        PostExecutionThread postExecutionThread = new UIThread();
        String versionName = null;
        try {
            versionName = MainApplication.getAppContext().getPackageManager().getPackageInfo(MainApplication.getAppContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
        ApiCacheRepository apiCacheRepository = new ApiCacheRepositoryImpl(
                new LocalCacheHandler(MainApplication.getAppContext(), TkpdCache.CACHE_API),
                versionName,
                new ApiCacheDataSource()
        );

        new ClearTimeOutCache(threadExecutor, postExecutionThread, apiCacheRepository).createObservableSync(RequestParams.EMPTY).toBlocking().first();

        CheckWhiteListUseCase checkWhiteListUseCase = new CheckWhiteListUseCase(threadExecutor, postExecutionThread, apiCacheRepository);
        GetCacheDataUseCaseUseCase getCacheDataUseCase = new GetCacheDataUseCaseUseCase(threadExecutor, postExecutionThread, apiCacheRepository);
        SaveToDbUseCase saveToDbUseCase = new SaveToDbUseCase(threadExecutor, postExecutionThread, apiCacheRepository);

        RequestParams requestParams = RequestParams.create();

        requestParams.putString(BaseApiCacheInterceptorUseCase.FULL_URL, getFullRequestURL(request));
        requestParams.putString(BaseApiCacheInterceptorUseCase.METHOD, request.method());

        String cacheData = getCacheDataUseCase.createObservableSync(requestParams).defaultIfEmpty(null).toBlocking().firstOrDefault(null);
        Boolean isInWhiteList = checkWhiteListUseCase.createObservableSync(requestParams).defaultIfEmpty(false).toBlocking().firstOrDefault(null);

        if (isInWhiteList != null && isInWhiteList) {
            if (cacheData == null || cacheData.equals("")) {
                Log.d(LOG_TAG, request.url().toString() + " data is not here !!");
                Response response;
                try {
                    response = chain.proceed(request);
                } catch (Exception e) {
                    throw e;
                }

                requestParams.putObject(SaveToDbUseCase.RESPONSE, response);

                saveToDbUseCase.createObservableSync(requestParams).toBlocking().first();

                return response;
            } else {
                Log.d(LOG_TAG, request.url().toString() + " already in here !!");
                Response.Builder builder = new Response.Builder();
                builder.request(request);
                builder.protocol(Protocol.HTTP_1_1);
                builder.code(200);
                builder.message("");
                builder.body(ResponseBody.create(MediaType.parse("application/json"), cacheData));
                return builder.build();
            }
        } else {
            Log.d(LOG_TAG, String.format("%s just hit another network !!", request.url().toString()));
            Response response;
            try {
                response = chain.proceed(request);
            } catch (Exception e) {
                throw e;
            }

            return response;
        }
    }

    private String getFullRequestURL(Request request){
        String s = "";
        if (request.method().equals("POST")) {
            RequestBody requestBody = request.body();
            if (requestBody instanceof FormBody) {
                int size = ((FormBody) requestBody).size();
                for (int i = 0; i < size; i++) {
                    String key = ((FormBody) requestBody).encodedName(i);
                    if (key.equals("hash") || key.equals("device_time")) {
                        continue;
                    }

                    String value = ((FormBody) requestBody).encodedValue(i);
                    if (TextUtils.isEmpty(s)) {
                        s += "?";
                    } else {
                        s += "&";
                    }
                    s += key + "=" + value;
                }
            }
        }
        return request.url().toString() + s;
    }
}
