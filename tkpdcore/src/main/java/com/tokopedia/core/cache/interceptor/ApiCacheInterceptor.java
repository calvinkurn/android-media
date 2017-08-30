package com.tokopedia.core.cache.interceptor;

import android.util.Log;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.cache.domain.interactor.ApiCacheInterceptorUseCase;

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

        ApiCacheInterceptorUseCase apiCacheInterceptorUseCase = new ApiCacheInterceptorUseCase(
                request.method(), request.url().toString()
        );
        apiCacheInterceptorUseCase.createObservableSync(RequestParams.EMPTY).toBlocking().first();

        if (apiCacheInterceptorUseCase.isInWhiteList()) {

            if (apiCacheInterceptorUseCase.isEmptyData()) {
                Log.d(LOG_TAG, apiCacheInterceptorUseCase.isInWhiteListRaw() + " data is not here !!");
                Response response;
                try {
                    response = chain.proceed(request);
                } catch (Exception e) {
                    throw e;
                }

                apiCacheInterceptorUseCase.updateResponse(response);

                return response;
            }

            if (apiCacheInterceptorUseCase.isExpiredData()) {
                // delete row
                Log.d(LOG_TAG, apiCacheInterceptorUseCase.isInWhiteListRaw() + " is expired time !!");

                Response response;
                try {
                    response = chain.proceed(request);
                } catch (Exception e) {
                    throw e;
                }

                apiCacheInterceptorUseCase.updateResponse(response);

                return response;
            } else {

                Log.d(LOG_TAG, apiCacheInterceptorUseCase.isInWhiteListRaw() + " already in here !!");
                Response.Builder builder = new Response.Builder();
                builder.request(request);
                builder.protocol(Protocol.HTTP_1_1);
                builder.code(200);
                builder.message("");
                builder.body(ResponseBody.create(MediaType.parse("application/json"), apiCacheInterceptorUseCase.getTempData().getResponseBody()));
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
