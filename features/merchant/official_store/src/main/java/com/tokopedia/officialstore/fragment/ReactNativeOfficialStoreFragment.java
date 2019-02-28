package com.tokopedia.officialstore.fragment;

import android.os.Bundle;

import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.app.ReactNativeFragment;

/**
 * Created by meta on 28/02/19.
 */
public class ReactNativeOfficialStoreFragment extends ReactNativeFragment {

    @Override
    public String getModuleName() {
        return ReactConst.Screen.OFFICIAL_STORE_HOME;
    }

    @Override
    protected Bundle getInitialBundle() {
        return getArguments() != null ? getArguments() : new Bundle();
    }

    public static ReactNativeOfficialStoreFragment createInstance(Bundle bundle){
        ReactNativeOfficialStoreFragment fragment = new ReactNativeOfficialStoreFragment();
        return fragment;
    }
}
