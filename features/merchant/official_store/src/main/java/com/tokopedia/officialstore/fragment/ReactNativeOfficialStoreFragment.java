package com.tokopedia.officialstore.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.officialstore.R;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.ReactUtils;
import com.tokopedia.tkpdreactnative.react.app.ReactNativeFragment;

import org.jetbrains.annotations.NotNull;

public class ReactNativeOfficialStoreFragment extends ReactNativeFragment {

    private static final String MP_OFFICIAL_STORE = "mp_official_store";

    @Override
    public String getModuleName() {
        return ReactConst.Screen.OFFICIAL_STORE_HOME;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ReactUtils.startTracing(MP_OFFICIAL_STORE); // start trace when view created
        super.onCreateView(inflater, container, savedInstanceState);
        if (getActivity() != null) // set background color of react root view
            reactRootView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
        return reactRootView;
    }

    @Override
    protected Bundle getInitialBundle() {
        return getArguments() != null ? getArguments() : new Bundle();
    }

    public static ReactNativeOfficialStoreFragment createInstance(){
        return new ReactNativeOfficialStoreFragment();
    }
}
