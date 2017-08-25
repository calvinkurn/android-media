package com.tokopedia.posapp.view.fragment;

import android.os.Bundle;

import com.tokopedia.core.app.ReactNativeFragmentV2;
import com.tokopedia.core.util.SessionHandler;

/**
 * Created by okasurya on 8/24/17.
 */

public class ProductListFragment extends ReactNativeFragmentV2 {

    public static final String SHOP_ID = "SHOP_ID";
    public static final String ETALASE_ID = "ETALASE_ID";

    public static ProductListFragment newInstance(String shopId, String etalaseId) {
        Bundle args = new Bundle();
        args.putString(SHOP_ID, shopId);
        args.putString(ETALASE_ID, etalaseId);
        ProductListFragment fragment = new ProductListFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public String getReactScreenName() {
        return "pos";
    }

    @Override
    public String getUserId() {
        return SessionHandler.getLoginID(getContext());
    }
}
