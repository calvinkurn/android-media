package com.tokopedia.officialstore.fragment;

import android.os.Bundle;

import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.app.ReactNativeFragment;

public class ReactBrandListOsFragment extends ReactNativeFragment {
    @Override
    public String getModuleName() {
        return ReactConst.Screen.BRANDLIST_CATEGORY;
    }

    @Override
    protected Bundle getInitialBundle() {
        return getArguments() != null ? getArguments() : new Bundle();
    }

    public static ReactBrandListOsFragment createInstance(Bundle bundle) {
        ReactBrandListOsFragment fragment = new ReactBrandListOsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}