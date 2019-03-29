package com.tokopedia.core.router.discovery;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.util.RouterUtils;

import static com.tokopedia.core.router.discovery.BrowseProductRouter.EXTRA_CATALOG_ID;

/**
 * @author Kulomady on 11/25/16.
 */

public class DetailProductRouter {

    public static final String EXTRA_ACTIVITY_PAUSED = "EXTRA_ACTIVITY_PAUSED";

    private static final String ARG_EXTRA_CATALOG_ID = "ARG_EXTRA_CATALOG_ID";

    private static final String CATALOG_DETAIL_ACTIVITY
            = "com.tokopedia.discovery.catalog.activity.CatalogDetailActivity";

    private static final String CATALOG_DETAIL_LIST_FRAGMENT
            = "com.tokopedia.discovery.catalog.fragment.CatalogDetailListFragment";

    private static final String CATALOG_DETAIL_FRAGMENT
            = "com.tokopedia.discovery.catalog.fragment.CatalogDetailFragment";

    public static Intent getCatalogDetailActivity(Context context, String catalogId) {
        return getCatalogDetailActivity(context, catalogId, false);
    }

    public static Intent getCatalogDetailActivity(Context context, String catalogId, boolean isActivityPaused) {
        Intent intent = RouterUtils.getActivityIntent(context, CATALOG_DETAIL_ACTIVITY);
        intent.putExtra(EXTRA_CATALOG_ID, catalogId);
        intent.putExtra(EXTRA_ACTIVITY_PAUSED, isActivityPaused);
        return intent;
    }

    public static Fragment getCatalogDetailListFragment(Context context, String catalogId) {
        Fragment fragment = Fragment.instantiate(context, CATALOG_DETAIL_LIST_FRAGMENT);
        Bundle bundle = new Bundle();
        bundle.putString("catalog_id", catalogId);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static Fragment getCatalogDetailFragment(Context context, String catalogId) {
        Fragment fragment = Fragment.instantiate(context, CATALOG_DETAIL_FRAGMENT);
        Bundle bundle = new Bundle();
        bundle.putString(ARG_EXTRA_CATALOG_ID, catalogId);
        fragment.setArguments(bundle);
        return fragment;
    }
}
