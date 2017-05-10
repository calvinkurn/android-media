package com.tokopedia.core.network.apiservices.mojito;

import com.tokopedia.core.network.core.OkHttpRetryPolicy;

/**
 * Created by HenryPri on 03/05/17.
 */

public class MojitoNoRetryAuthService extends MojitoAuthService {
    @Override
    protected OkHttpRetryPolicy getOkHttpRetryPolicy() {
        return OkHttpRetryPolicy.createdOkHttpNoAutoRetryPolicy();
    }
}
