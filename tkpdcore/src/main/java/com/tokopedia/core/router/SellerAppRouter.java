package com.tokopedia.core.router;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.util.RouterUtils;

/**
 * Created by stevenfredian on 12/1/16.
 */
@Deprecated
public class SellerAppRouter {

    public static Intent getSellerHomeActivity(Context context) {
        return RouterUtils.getRouterFromContext(context).getHomeIntent(context);
    }
}