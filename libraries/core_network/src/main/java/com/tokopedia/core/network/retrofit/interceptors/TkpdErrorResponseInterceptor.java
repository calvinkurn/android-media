package com.tokopedia.core.network.retrofit.interceptors;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tokopedia.core.network.retrofit.response.BaseResponseError;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author Hendry on 28.02.2017.
 */
public class TkpdErrorResponseInterceptor implements Interceptor {
    private static final int BYTE_COUNT = 2048;
    Class<? extends BaseResponseError> responseErrorClass;
    BaseResponseError responseError;

    public TkpdErrorResponseInterceptor(@NonNull Class<? extends BaseResponseError> responseErrorClass) {
        this.responseErrorClass = responseErrorClass;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());

        ResponseBody responseBody = null;
        String responseBodyString = "";
        if (mightContainCustomError(response)) {
            responseBody = response.peekBody(BYTE_COUNT);
            responseBodyString = responseBody.string();

            Gson gson = new Gson();
            responseError = null;
            try {
                responseError = gson.fromJson(responseBodyString, responseErrorClass);
            } catch (JsonSyntaxException e) { // the json might not be TkpdResponseError instance, so just return it
                return response;
            }
            if (responseError == null) { // no error object
                return response;
            } else {
                if (responseError.hasBody()) {
                    throw responseError.createException();
                } else {
                    return response;
                }
            }
        }
        return response;
    }

    protected boolean mightContainCustomError(Response response) {
        return response != null && response.isSuccessful();
    }

}
