package com.tokopedia.tkpd.home.fragment;

import android.os.Bundle;

import com.tokopedia.tkpdreactnative.react.app.ReactNativeFragment;

/**
 * @author ricoharisin .
 */

public class FragmentReactNativeHotList extends ReactNativeFragment {

    @Override
    public String getModuleName() {
        return "HotList";
    }

    @Override
    protected Bundle getInitialBundle() {
        return null;
    }
}
