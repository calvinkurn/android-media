package com.tokopedia.abstraction.common.network.interceptor;

import androidx.annotation.NonNull;

import com.tokopedia.network.data.model.response.BaseResponseError;

import okhttp3.Response;

/**
 * If the header is success, do not process the error.
 */

public class HeaderErrorResponseInterceptor extends ErrorResponseInterceptor {

    public HeaderErrorResponseInterceptor(@NonNull Class<? extends BaseResponseError> responseErrorClass) {
        super(responseErrorClass);
    }

    @Override
    protected boolean mightContainCustomError(Response response) {
        return response != null && !response.isSuccessful();
    }
}
