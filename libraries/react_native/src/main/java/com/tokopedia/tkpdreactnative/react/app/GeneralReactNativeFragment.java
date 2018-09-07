package com.tokopedia.tkpdreactnative.react.app;

import android.os.Bundle;

import com.tokopedia.tkpdreactnative.react.ReactConst;

/**
 * Created by okasurya on 1/9/18.
 */

public class GeneralReactNativeFragment extends ReactNativeFragment {
    @Override
    public String getModuleName() {
        return ReactConst.MAIN_MODULE;
    }

    @Override
    protected Bundle getInitialBundle() {
        return getArguments() != null ? getArguments() : new Bundle();
    }

    public static GeneralReactNativeFragment createInstance(Bundle bundle){
        GeneralReactNativeFragment fragment = new GeneralReactNativeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
