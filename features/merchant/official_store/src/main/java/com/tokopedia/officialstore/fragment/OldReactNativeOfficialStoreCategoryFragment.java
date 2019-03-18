package com.tokopedia.officialstore.fragment;

import android.os.Bundle;

import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.app.ReactNativeFragment;

/**
 * @author okasurya on 3/8/19.
 */
public class OldReactNativeOfficialStoreCategoryFragment extends ReactNativeFragment {
    private static final String CATEGORY = "Category";
    private static final String KEY_CATEGORY = "key_category";

    public static OldReactNativeOfficialStoreCategoryFragment createInstance(Bundle bundle) {
        if (bundle == null)
            bundle = new Bundle();

        OldReactNativeOfficialStoreCategoryFragment osFragment = new OldReactNativeOfficialStoreCategoryFragment();
        bundle.putString(ReactConst.KEY_SCREEN, ReactConst.Screen.OFFICIAL_STORE_CATEGORY);
        String category = bundle.getString(KEY_CATEGORY);
        if (category != null && !category.isEmpty()){
            bundle.putString(CATEGORY, bundle.getString(KEY_CATEGORY));
        }
        osFragment.setArguments(bundle);
        return osFragment;
    }

    @Override
    public String getModuleName() {
        return ReactConst.MAIN_MODULE;
    }

    @Override
    protected Bundle getInitialBundle() {
        return getArguments() != null ? getArguments() : new Bundle();
    }
}
