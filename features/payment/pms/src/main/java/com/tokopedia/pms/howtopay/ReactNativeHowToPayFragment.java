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

/**
 * Created by nakama on 10/07/18.
 */

public class ReactNativeHowToPayFragment extends Fragment  implements DefaultHardwareBackBtnHandler {
    protected ReactRootView reactRootView;
    protected ReactInstanceManager reactInstanceManager;

    public static Fragment createInstance(Bundle bundle) {
        ReactNativeHowToPayFragment fragment = new ReactNativeHowToPayFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

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
    public void onDestroyView() {
        super.onDestroyView();
        if(reactRootView != null) {
            reactRootView.unmountReactApplication();
        }
    }

    @Override
    public void invokeDefaultOnBackPressed() {
        getActivity().onBackPressed();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (reactRootView == null)
            reactRootView = new ReactRootView(context);
        if (reactInstanceManager == null)
            reactInstanceManager = ((ReactApplication) getActivity().getApplication()).getReactNativeHost().getReactInstanceManager();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (reactRootView == null)
            reactRootView = new ReactRootView(activity);
        if (reactInstanceManager == null)
            reactInstanceManager = ((ReactApplication) getActivity().getApplication()).getReactNativeHost().getReactInstanceManager();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        reactRootView.startReactApplication(reactInstanceManager, ReactConst.MAIN_MODULE, getArguments());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return reactRootView;
    }
}
