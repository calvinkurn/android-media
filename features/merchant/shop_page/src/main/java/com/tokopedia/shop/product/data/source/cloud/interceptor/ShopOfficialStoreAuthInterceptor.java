package com.tokopedia.shop.product.data.source.cloud.interceptor;

import android.content.Context;

import com.tokopedia.network.NetworkRouter;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.wishlist.common.data.interceptor.MojitoInterceptor;


public class ShopOfficialStoreAuthInterceptor extends MojitoInterceptor {

    public ShopOfficialStoreAuthInterceptor(Context context,
                                            NetworkRouter networkRouter,
                                            UserSessionInterface userSessionInterface) {
        super(context, networkRouter, userSessionInterface);
    }
}
