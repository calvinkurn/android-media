package com.tokopedia.pms.howtopay;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.app.GeneralReactNativeFragment;
import com.tokopedia.tkpdreactnative.react.app.ReactNativeFragment;

/**
 * Created by nakama on 10/07/18.
 */

public class ReactNativeHowToPayFragment extends ReactNativeFragment implements DefaultHardwareBackBtnHandler {

    public static Fragment createInstance(Bundle bundle) {
        ReactNativeHowToPayFragment fragment = new ReactNativeHowToPayFragment();
        fragment.setArguments(bundle);
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
