package com.tokopedia.tkpd.home.fragment;

import android.os.Bundle;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.app.ReactNativeFragment;

/**
 * @author ricoharisin .
 */

public class ReactNativeThankYouPageFragment extends ReactNativeFragment {
    @Override
    public String getModuleName() {
        return ReactConst.MAIN_MODULE;
    }

    @Override
    protected Bundle getInitialBundle() {
        return getArguments() != null ? getArguments() : new Bundle();
    }

    public static ReactNativeThankYouPageFragment createInstance(Bundle bundle) {
        ReactNativeThankYouPageFragment fragment = new ReactNativeThankYouPageFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
