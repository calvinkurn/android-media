package com.tokopedia.core.app;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.tokopedia.core.react.ReactConst;
import com.tokopedia.core.react.ReactUtils;

/**
 * Created by okasurya on 8/24/17.
 */

public abstract class ReactNativeFragmentV2 extends TkpdBaseV4Fragment
        implements DefaultHardwareBackBtnHandler {
    public static final String USER_ID = "User_ID";

    protected ReactRootView reactRootView;
    protected ReactInstanceManager reactInstanceManager;

    public abstract String getReactScreenName();

    public abstract String getUserId();

    @Override
    public void onPause() {
        super.onPause();

        if (reactInstanceManager != null) {
            reactInstanceManager.onHostPause(getActivity());
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (reactInstanceManager != null) {
            reactInstanceManager.onHostResume(getActivity(), this);
        }
    }

    @Override
    public void onDestroy() {
        ReactUtils.sendDestroyPageEmitter();
        super.onDestroy();

        if (reactInstanceManager != null) {
            reactInstanceManager.onHostDestroy(getActivity());
        }
    }

    @Override
    public void invokeDefaultOnBackPressed() {
        getActivity().onBackPressed();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        reactRootView = new ReactRootView(context);
        reactInstanceManager = MainApplication.getInstance().getReactNativeHost().getReactInstanceManager();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        reactRootView.startReactApplication(reactInstanceManager, ReactConst.MAIN_MODULE, getBundle());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        return reactRootView;
    }

    private Bundle getBundle() {
        Bundle bundle = new Bundle();
        bundle.putAll(getArguments());
        bundle.putString(ReactConst.KEY_SCREEN, getReactScreenName());
        bundle.putString(USER_ID, getUserId());
        return bundle;
    }
}
