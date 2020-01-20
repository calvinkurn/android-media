package com.tokopedia.wishlist.common.data.interceptor;

import android.content.Context;

import com.tokopedia.network.NetworkRouter;
import com.tokopedia.user.session.UserSessionInterface;


public class WishListAuthInterceptor extends MojitoInterceptor {

    public WishListAuthInterceptor(Context context,
                                   NetworkRouter networkRouter,
                                   UserSessionInterface userSessionInterface) {
        super(context, networkRouter, userSessionInterface);
    }
}
