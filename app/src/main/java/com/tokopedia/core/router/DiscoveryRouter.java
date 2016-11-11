package com.tokopedia.core.router;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.util.RouterUtils;

/**
 * Created by ricoharisin on 11/9/16.
 */

public class DiscoveryRouter {

    private static final String CATALOG_DETAIL_ACTIVITY = "com.tokopedia.discovery.catalog.activity.CatalogDetailActivity";

    private static final String CATALOG_DETAIL_LIST_FRAGMENT = "com.tokopedia.discovery.catalog.fragment.CatalogDetailListFragment";

    public static final String EXTRA_CATALOG_ID = "EXTRA_CATALOG_ID";

    public static Intent getCatalogDetailActivity(Context context, String catalogId) {
        Intent intent = RouterUtils.getActivityIntent(context, CATALOG_DETAIL_ACTIVITY);
        intent.putExtra(EXTRA_CATALOG_ID, catalogId);
        return intent;
    }

    public static Fragment getCatalogDetailListFragment(Context context, String catalogId) {
        Fragment fragment = Fragment.instantiate(context, CATALOG_DETAIL_LIST_FRAGMENT);
        Bundle bundle = new Bundle();
        bundle.putString("catalog_id", catalogId);
        fragment.setArguments(bundle);
        return fragment;
    }
}