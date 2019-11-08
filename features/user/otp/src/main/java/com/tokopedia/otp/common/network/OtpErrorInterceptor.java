package com.tokopedia.otp.common.network;

import androidx.annotation.NonNull;

import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;

/**
 * @author by nisie on 4/26/18.
 */

public class OtpErrorInterceptor extends ErrorResponseInterceptor {

    public OtpErrorInterceptor(@NonNull Class<OtpErrorResponse> responseErrorClass) {
        super(responseErrorClass);
    }
}
