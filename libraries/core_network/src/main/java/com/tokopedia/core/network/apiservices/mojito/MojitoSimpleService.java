package com.tokopedia.core.network.apiservices.mojito;

import com.tokopedia.core.network.core.OkHttpRetryPolicy;

/**
 * Created by HenryPri on 02/05/17.
 */

@Deprecated
public class MojitoSimpleService extends MojitoService {
    @Override
    protected OkHttpRetryPolicy getOkHttpRetryPolicy() {
        return new OkHttpRetryPolicy(1, 1, 1, 0);
    }
}
