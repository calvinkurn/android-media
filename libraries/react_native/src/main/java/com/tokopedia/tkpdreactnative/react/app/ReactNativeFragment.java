package com.tokopedia.tkpdreactnative.react.app;

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
import com.tokopedia.tkpdreactnative.react.ReactUtils;

import org.jetbrains.annotations.NotNull;

/**
 * @author ricoharisin .
 */

public abstract class ReactNativeFragment extends Fragment implements DefaultHardwareBackBtnHandler {

    protected ReactRootView reactRootView;
    protected ReactInstanceManager reactInstanceManager;

    public abstract String getModuleName();

    @Override
    public void onPause() {
        super.onPause();

        if (reactInstanceManager != null && getActivity() != null) {
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
    public void onDestroyView() {
        super.onDestroyView();

        ReactUtils.stopTracing();
        if(reactRootView != null) {
            reactRootView.unmountReactApplication();
            reactRootView = null;
        }
    }

    @Override
    public void invokeDefaultOnBackPressed() {
        if (getActivity() != null)
            getActivity().onBackPressed();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (reactRootView == null)
            reactRootView = new ReactRootView(context);
        if (reactInstanceManager == null && getActivity() != null)
            reactInstanceManager = ((ReactApplication) getActivity().getApplication()).getReactNativeHost().getReactInstanceManager();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        reactRootView.startReactApplication(reactInstanceManager, getModuleName(), getInitialBundle());
    }

    protected abstract Bundle getInitialBundle();

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return reactRootView;
    }
}
