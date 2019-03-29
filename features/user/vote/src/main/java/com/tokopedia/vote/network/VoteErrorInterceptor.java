package com.tokopedia.vote.network;

import android.support.annotation.NonNull;

import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;

/**
 * @author by nisie on 5/7/18.
 */
public class VoteErrorInterceptor extends ErrorResponseInterceptor {

    public VoteErrorInterceptor(@NonNull Class<VoteErrorResponse> responseErrorClass) {
        super(responseErrorClass);
    }
}
