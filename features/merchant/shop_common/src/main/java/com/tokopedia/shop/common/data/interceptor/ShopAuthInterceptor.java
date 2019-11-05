package com.tokopedia.shop.common.data.interceptor;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;


public class ShopAuthInterceptor extends TkpdAuthInterceptor {

    @Inject
    public ShopAuthInterceptor(@ApplicationContext Context context,
                               NetworkRouter networkRouter,
                               UserSessionInterface userSessionInterface) {
        super(context, networkRouter, userSessionInterface);
    }
}
