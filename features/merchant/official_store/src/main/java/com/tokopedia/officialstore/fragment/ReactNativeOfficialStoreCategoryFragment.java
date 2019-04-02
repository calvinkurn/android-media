package com.tokopedia.officialstore.fragment;

import android.os.Bundle;

import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.app.ReactNativeFragment;

public class ReactNativeOfficialStoreCategoryFragment extends ReactNativeFragment {
    private static final String CATEGORY = "Category";
    private static final String KEY_CATEGORY = "key_category";

    @Override
    public String getModuleName() {
        return ReactConst.Screen.OFFICIAL_STORE_CATEGORY;
    }

    @Override
    protected Bundle getInitialBundle() {
        return getArguments() != null ? getArguments() : new Bundle();
    }



    public static ReactNativeOfficialStoreCategoryFragment createInstance(Bundle bundle){
        if (bundle == null)
            bundle = new Bundle();

        ReactNativeOfficialStoreCategoryFragment osFragment = new ReactNativeOfficialStoreCategoryFragment();
        bundle.putString(ReactConst.KEY_SCREEN, ReactConst.Screen.OFFICIAL_STORE);
        String category = bundle.getString(KEY_CATEGORY);
        if (category != null && !category.isEmpty()){
            bundle.putString(CATEGORY, bundle.getString(KEY_CATEGORY));
        }
        osFragment.setArguments(bundle);
        return osFragment;
    }

}