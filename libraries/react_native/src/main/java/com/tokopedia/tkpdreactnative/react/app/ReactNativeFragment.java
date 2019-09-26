package com.tokopedia.tkpdreactnative.react.app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.tkpdreactnative.react.ReactUtils;

import org.jetbrains.annotations.NotNull;

/**
 * @author ricoharisin .
 */

public abstract class ReactNativeFragment extends Fragment implements DefaultHardwareBackBtnHandler {

    public static final int OVERLAY_PERMISSION_REQ_CODE = 1080;

    protected ReactRootView reactRootView;
    protected ReactInstanceManager reactInstanceManager;

    public abstract String getModuleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPermissionReactNativeDev();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (reactRootView == null) {
            reactRootView = new ReactRootView(getActivity());
        }
        if (reactInstanceManager == null && getActivity() != null) {
            reactInstanceManager = ((ReactApplication) getActivity().getApplication()).getReactNativeHost().getReactInstanceManager();
        }
    }

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
        if (reactRootView != null) {
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    protected abstract Bundle getInitialBundle();

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        reactRootView.startReactApplication(reactInstanceManager, getModuleName(), getInitialBundle());
        return reactRootView;
    }

    private void initPermissionReactNativeDev() {
        if (GlobalConfig.isAllowDebuggingTools()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (getContext() == null)
                    return;

                if (!Settings.canDrawOverlays(getContext())) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getContext().getPackageName()));
                    startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
                }
            }
        }
    }
}
