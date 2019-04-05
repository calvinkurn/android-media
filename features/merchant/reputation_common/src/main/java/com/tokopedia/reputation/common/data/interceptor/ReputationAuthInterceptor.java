package com.tokopedia.reputation.common.data.interceptor;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;


public class ReputationAuthInterceptor extends TkpdAuthInterceptor {

    public ReputationAuthInterceptor(Context context,
                                     AbstractionRouter abstractionRouter) {
        super(context, abstractionRouter);
    }
}
