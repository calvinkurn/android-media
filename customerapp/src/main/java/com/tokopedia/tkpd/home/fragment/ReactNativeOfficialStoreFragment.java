package com.tokopedia.tkpd.home.fragment;

import android.os.Bundle;

import com.tokopedia.core.app.ReactNativeFragment;
import com.tokopedia.core.react.ReactConst;

/**
 * Created by alvarisi on 8/31/17.
 */

public class ReactNativeOfficialStoreFragment extends ReactNativeFragment {

    @Override
    public String getModuleName() {
        return ReactConst.MAIN_MODULE;
    }

    @Override
    protected Bundle getInitialBundle() {
        return getArguments() != null ? getArguments() : new Bundle();
    }

    public static ReactNativeOfficialStoreFragment createInstance(Bundle bundle) {
        ReactNativeOfficialStoreFragment fragment = new ReactNativeOfficialStoreFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
