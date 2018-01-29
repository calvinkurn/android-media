package com.tokopedia.cacheapi.interceptor;

import android.text.TextUtils;

import com.tokopedia.cacheapi.constant.CacheApiConstant;
import com.tokopedia.cacheapi.domain.interactor.CacheApiCheckWhiteListUseCase;
import com.tokopedia.cacheapi.domain.interactor.CacheApiClearTimeOutCacheUseCase;
import com.tokopedia.cacheapi.domain.interactor.CacheApiGetCacheDataUseCase;
import com.tokopedia.cacheapi.domain.interactor.CacheApiSaveToDbUseCase;
import com.tokopedia.cacheapi.util.CacheApiResponseValidator;
import com.tokopedia.cacheapi.util.CacheApiUtils;
import com.tokopedia.cacheapi.util.CacheApiLoggingUtils;
import com.tokopedia.usecase.RequestParams;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by normansyahputa on 8/9/17.
 */

public class CacheApiInterceptor implements Interceptor {

    private CacheApiResponseValidator responseValidator;

    public void setResponseValidator(CacheApiResponseValidator responseValidator) {
        this.responseValidator = responseValidator;
    }

    public CacheApiInterceptor() {
        this(new CacheApiResponseValidator());
    }

    public CacheApiInterceptor(CacheApiResponseValidator responseValidator) {
        this.responseValidator = responseValidator;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        try {
            return getCacheResponse(chain);
        } catch (Throwable t) {
            return getDefaultResponse(chain);
        }
    }

    private Response getDefaultResponse(Chain chain) throws IOException {
        return chain.proceed(chain.request());
    }

    private Response getCacheResponse(Chain chain) throws Throwable {
        Request request = chain.request();

        new CacheApiClearTimeOutCacheUseCase().executeSync(RequestParams.EMPTY);

        CacheApiCheckWhiteListUseCase checkWhiteListUseCase = new CacheApiCheckWhiteListUseCase();
        CacheApiGetCacheDataUseCase getCacheDataUseCase = new CacheApiGetCacheDataUseCase();
        CacheApiSaveToDbUseCase saveToDbUseCase = new CacheApiSaveToDbUseCase();

        String host = request.url().host();
        String path = CacheApiUtils.getPath(request.url().toString());

        boolean inWhiteList = checkWhiteListUseCase.getData(CacheApiCheckWhiteListUseCase.createParams(host, path));

        if (!inWhiteList) {
            CacheApiLoggingUtils.dumper(String.format("Not registered in white list: %s", request.url().toString()));
            throw new Exception("Not registered in white list");
        }
        String requestParams = CacheApiUtils.getRequestParam(request);
        String cachedResponseData = getCacheDataUseCase.getData(CacheApiGetCacheDataUseCase.createParams(host, path, requestParams));
        if (TextUtils.isEmpty(cachedResponseData)) {
            CacheApiLoggingUtils.dumper(String.format("Data is not here, fetch and save: %s", request.url().toString()));
            Response originalResponse = getDefaultResponse(chain);
            if (responseValidator == null || responseValidator.isResponseValidToBeCached(originalResponse)) {
                saveToDbUseCase.executeSync(CacheApiSaveToDbUseCase.createParams(host, path, originalResponse));
            }
            return originalResponse;
        } else {
            Response originalResponse = getDefaultResponse(chain);
            CacheApiLoggingUtils.dumper(String.format("Data exist, return data from db: %s", request.url().toString()));
            Response.Builder builder = new Response.Builder();
            builder.request(request);
            builder.protocol(originalResponse.protocol());
            builder.code(originalResponse.code());
            builder.message("");
            builder.body(ResponseBody.create(MediaType.parse(CacheApiConstant.TYPE_APPLICATION_JSON), cachedResponseData));
            return builder.build();
        }
    }
}