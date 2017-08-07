package com.tokopedia.core.app;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;

/**
 * @author ricoharisin .
 */

public abstract class ReactNativeFragment extends TkpdBaseV4Fragment implements DefaultHardwareBackBtnHandler {

    protected ReactRootView reactRootView;
    protected ReactInstanceManager reactInstanceManager;

    public abstract String getModuleName();

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
        reactRootView.startReactApplication(reactInstanceManager, getModuleName(), null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        return reactRootView;
    }
}
