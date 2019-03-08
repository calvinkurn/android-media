package com.tokopedia.officialstore.fragment;

import android.os.Bundle;

import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.app.ReactNativeFragment;

/**
 * @author okasurya on 3/8/19.
 */
public class OldReactNativeOfficialStoreFragment extends ReactNativeFragment {
    public static OldReactNativeOfficialStoreFragment instance(Bundle extras) {
        Bundle bundle = new Bundle();
        OldReactNativeOfficialStoreFragment fragment = new OldReactNativeOfficialStoreFragment();
        bundle.putAll(extras);
        return fragment;
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
