package com.tokopedia.core.cache.interceptor;

import android.util.Log;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.cache.domain.interactor.ApiCacheInterceptorUseCase;
import com.tokopedia.core.cache.domain.interactor.CheckWhiteListUseCase;
import com.tokopedia.core.cache.domain.interactor.ClearTimeOutCacheData;
import com.tokopedia.core.cache.domain.interactor.GetCacheDatatUseCase;
import com.tokopedia.core.cache.domain.interactor.SaveToDbUseCase;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
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

        new ClearTimeOutCacheData().createObservableSync(RequestParams.EMPTY).toBlocking().first();

        ApiCacheInterceptorUseCase apiCacheInterceptorUseCase = new ApiCacheInterceptorUseCase();

        CheckWhiteListUseCase checkWhiteListUseCase = new CheckWhiteListUseCase(apiCacheInterceptorUseCase);
        GetCacheDatatUseCase getCacheDatatUseCase = new GetCacheDatatUseCase(apiCacheInterceptorUseCase);
        SaveToDbUseCase saveToDbUseCase = new SaveToDbUseCase(apiCacheInterceptorUseCase);

        RequestParams requestParams = RequestParams.create();
        requestParams.putString(ApiCacheInterceptorUseCase.FULL_URL, request.url().toString());
        requestParams.putString(ApiCacheInterceptorUseCase.METHOD, request.method());

        String cacheData = getCacheDatatUseCase.createObservableSync(requestParams).toBlocking().first();
        Boolean isInWhiteList = checkWhiteListUseCase.createObservableSync(requestParams).toBlocking().first();

        if (isInWhiteList) {

            if (cacheData == null || cacheData.equals("")) {
                Log.d(LOG_TAG, apiCacheInterceptorUseCase.isInWhiteListRaw() + " data is not here !!");
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
                Log.d(LOG_TAG, apiCacheInterceptorUseCase.isInWhiteListRaw() + " already in here !!");
                Response.Builder builder = new Response.Builder();
                builder.request(request);
                builder.protocol(Protocol.HTTP_1_1);
                builder.code(200);
                builder.message("");
                builder.body(ResponseBody.create(MediaType.parse("application/json"), cacheData));
                return builder.build();
            }
        }else{
            Log.d(LOG_TAG, "just hit another network !!");
            Response response;
            try {
                response = chain.proceed(request);
            } catch (Exception e) {
                throw e;
            }

            return response;
        }
    }


}
