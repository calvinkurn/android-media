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

    public static final String VALUES_DYNAMIC_FILTER_SEARCH_PRODUCT = "search_product";
    public static final String VALUES_DYNAMIC_FILTER_DIRECTORY = "directory";


    public static final String VALUES_DYNAMIC_FILTER_HOT_PRODUCT = "hot_product";

    public final static String VALUES_DEFAULT_DEPARTMENT_ID = "0";

    private static final String BROWSE_PRODUCT_ACTIVITY
            = "com.tokopedia.discovery.activity.BrowseProductActivity";
    private static final String CATEGORY_NAVIGATION_ACTIVITY
            = "com.tokopedia.discovery.categorynav.view.CategoryNavigationActivity";
    private static final String BROWSE_HOTLIST_ACTIVITY
            = "com.tokopedia.discovery.newdiscovery.hotlist.view.activity.HotlistActivity";
    private static final String BROWSE_SEARCH_ACTIVITY
            = "com.tokopedia.search.result.presentation.view.activity.SearchActivity";

    public enum GridType {
        GRID_1, GRID_2, GRID_3
    }


    public static Intent getDefaultBrowseIntent(Context context) {
        Intent intent = RouterUtils.getActivityIntent(context, BROWSE_PRODUCT_ACTIVITY);
        Bundle bundle = new Bundle();
        bundle.putString(BrowseProductRouter.DEPARTMENT_ID, VALUES_DEFAULT_DEPARTMENT_ID);
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

    public static Intent getSearchProductIntent(Context context) {
        return RouterUtils.getActivityIntent(context, BROWSE_SEARCH_ACTIVITY);
    }

    public static Intent getBrowseProductIntent(Context context, String alias) {
        Intent intent = RouterUtils.getActivityIntent(context, BROWSE_PRODUCT_ACTIVITY);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRAS_DISCOVERY_ALIAS, alias);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent getCategoryNavigationIntent(Context context) {
        Intent intent =  RouterUtils.getActivityIntent(context, CATEGORY_NAVIGATION_ACTIVITY);
        return intent;

    }

//    public static Fragment getCatalogDetailListFragment(Context context) {
//        Fragment fragment = Fragment.instantiate(context, CATALOG_DETAIL_LIST_FRAGMENT);
//        Bundle bundle = new Bundle();
//        bundle.putString("catalog_id", catalogId);
//        fragment.setArguments(bundle);
//        return fragment;
//    }

}