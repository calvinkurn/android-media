package com.tokopedia.officialstore.fragment;

import android.os.Bundle;

import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.app.ReactNativeFragment;

/**
 * Created by meta on 28/02/19.
 */
public class ReactNativeOfficialStoreFragment extends ReactNativeFragment {

    private static final String CATEGORY = "Category";
    private static final String KEY_CATEGORY = "key_category";

    @Override
    public String getModuleName() {
        return ReactConst.MAIN_MODULE;
    }

    @Override
    protected Bundle getInitialBundle() {
        return getArguments() != null ? getArguments() : new Bundle();
    }

    public static ReactNativeOfficialStoreFragment createInstance(Bundle bundle){
        if (bundle == null)
            bundle = new Bundle();

        ReactNativeOfficialStoreFragment fragment = new ReactNativeOfficialStoreFragment();
        bundle.putString(ReactConst.KEY_SCREEN, ReactConst.Screen.OFFICIAL_STORE);
        String category = bundle.getString(KEY_CATEGORY);
        if (category != null && !category.isEmpty()){
            bundle.putString(CATEGORY, bundle.getString(KEY_CATEGORY));
        }
        fragment.setArguments(bundle);
        return fragment;
    }
}
