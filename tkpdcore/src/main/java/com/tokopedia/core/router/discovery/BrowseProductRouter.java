package com.tokopedia.core.router.discovery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.util.RouterUtils;

/**
 * Created by ricoharisin on 11/9/16.
 * Modified by mady on 11/24/16
 */

public class BrowseProductRouter {


    public static final String EXTRA_CATALOG_ID = "EXTRA_CATALOG_ID";
    public static final String EXTRAS_SEARCH_TERM = "EXTRAS_SEARCH_TERM";
    public static final String DEPARTMENT_ID = "DEPARTMENT_ID";
    public static final String EXTRA_CATEGORY_URL = "CATEGORY_URL";
    public static final String DEPARTMENT_NAME = "DEPARTMENT_NAME";
    public static final String FROM_NAVIGATION = "FROM_NAVIGATION";
    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String EXTRAS_DISCOVERY_ALIAS = "EXTRAS_DISCOVERY_ALIAS";
    public static final String EXTRAS_HOTLIST_URL = "HOTLIST_URL";

    public static final String VALUES_DYNAMIC_FILTER_DIRECTORY = "directory";


    public static final String VALUES_DYNAMIC_FILTER_HOT_PRODUCT = "hot_product";


    private static final String BROWSE_PRODUCT_ACTIVITY
            = "com.tokopedia.discovery.activity.BrowseProductActivity";
    private static final String BROWSE_HOTLIST_ACTIVITY
            = "com.tokopedia.discovery.newdiscovery.hotlist.view.activity.HotlistActivity";


    public enum GridType {
        GRID_1, GRID_2, GRID_3
    }

    public static Intent getBrowseProductIntent(Context context, String alias) {
        Intent intent = RouterUtils.getActivityIntent(context, BROWSE_PRODUCT_ACTIVITY);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRAS_DISCOVERY_ALIAS, alias);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent getHotlistIntent(Context context, String url) {
        Intent intent = RouterUtils.getActivityIntent(context, BROWSE_HOTLIST_ACTIVITY);
        Bundle bundle = new Bundle();
        bundle.putString(BrowseProductRouter.EXTRAS_HOTLIST_URL, url);
        intent.putExtras(bundle);
        return intent;
    }
}