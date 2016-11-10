package com.tokopedia.core.router;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.util.RouterUtils;

/**
 * Created by ricoharisin on 11/10/16.
 */

public class SellerRouter {

    public static final String ACTIVITY_SELLING_TRANSACTION = "com.tokopedia.seller.selling.view.activity.ActivitySellingTransaction";

    public static Intent getActivitySellingTransaction(Context context) {
        return RouterUtils.getActivityIntent(context, ACTIVITY_SELLING_TRANSACTION);
    }
}
