package com.tokopedia.abstraction.common.network.interceptor;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tokopedia.network.data.model.response.BaseResponseError;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.ResponseBody;
import timber.log.Timber;

/**
 * Created by hendry on 9/15/2017.
 */

public class ErrorResponseInterceptor implements Interceptor {
    private static final int BYTE_COUNT = 2048;
    private Class<? extends BaseResponseError> responseErrorClass;

    public ErrorResponseInterceptor(@NonNull Class<? extends BaseResponseError> responseErrorClass) {
        this.responseErrorClass = responseErrorClass;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());

        ResponseBody responseBody = null;
        String responseBodyString = "";
        if (mightContainCustomError(response)) {
            responseBody = response.peekBody(BYTE_COUNT);
            responseBodyString = responseBody.string();

            Gson gson = new Gson();
            BaseResponseError responseError = null;
            try {
                responseError = gson.fromJson(responseBodyString, responseErrorClass);
            } catch (JsonSyntaxException e) { // the json might not be TkpdResponseError instance, so just return it
                return response;
            }
            if (responseError == null) { // no error object
                return response;
            } else {
                if (responseError.hasBody()) {
                    Timber.d(response.headers().toString());
                    Timber.d(responseBodyString);
                    //noinspection ConstantConditions
                    response.body().close();
                    throw responseError.createException();
                } else {
                    return response;
                }
            }
        }
        return response;
    }

    protected boolean mightContainCustomError(Response response) {
        return response != null;
    }
}
