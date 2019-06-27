package com.tokopedia.officialstore.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.ReactUtils;
import com.tokopedia.tkpdreactnative.react.app.ReactNativeFragment;

import org.jetbrains.annotations.NotNull;

public class ReactBrandListOsFragment extends ReactNativeFragment {
    private static final String MP_BRAND_LIST = "mp_brand_list";

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

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ReactUtils.startTracing(MP_BRAND_LIST);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}