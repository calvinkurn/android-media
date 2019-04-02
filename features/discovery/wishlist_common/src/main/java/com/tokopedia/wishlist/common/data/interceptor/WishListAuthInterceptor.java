package com.tokopedia.wishlist.common.data.interceptor;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;


public class WishListAuthInterceptor extends MojitoInterceptor {

    public WishListAuthInterceptor(Context context,
                                   AbstractionRouter abstractionRouter) {
        super(context, abstractionRouter);
    }
}
