package com.tokopedia.core.router;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.util.RouterUtils;
import com.tokopedia.core.var.RouterConstant;

/**
 * Created by ricoharisin on 11/9/16.
 */

public class Router {

    public static final String EXTRA_CATALOG_ID = "EXTRA_CATALOG_ID";

    public static Intent getCatalogDetailActivity(Context context, String catalogId) {
        Intent intent = RouterUtils.getActivityIntent(context, RouterConstant.CATALOG_DETAIL_ACTIVITY);
        intent.putExtra(EXTRA_CATALOG_ID, catalogId);
        return intent;
    }
}
