package com.tokopedia.shop.common.data.interceptor;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;

import javax.inject.Inject;


public class ShopAuthInterceptor extends TkpdAuthInterceptor {

    @Inject
    public ShopAuthInterceptor(@ApplicationContext Context context,
                               AbstractionRouter abstractionRouter) {
        super(context, abstractionRouter);
    }
}
