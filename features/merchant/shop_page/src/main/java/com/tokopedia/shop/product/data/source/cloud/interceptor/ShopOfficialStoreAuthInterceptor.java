package com.tokopedia.shop.product.data.source.cloud.interceptor;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.wishlist.common.data.interceptor.MojitoInterceptor;


public class ShopOfficialStoreAuthInterceptor extends MojitoInterceptor {

    public ShopOfficialStoreAuthInterceptor(Context context,
                                            AbstractionRouter abstractionRouter) {
        super(context, abstractionRouter);
    }
}
