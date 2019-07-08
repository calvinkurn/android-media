package com.tokopedia.officialstore.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactRootView;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.tokopedia.navigation_common.listener.AllNotificationListener;
import com.tokopedia.officialstore.R;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.ReactNativeHostFactory;
import com.tokopedia.tkpdreactnative.react.ReactUtils;
import com.tokopedia.tkpdreactnative.react.app.ReactNativeFragment;

import org.jetbrains.annotations.NotNull;

public class ReactNativeOfficialStoreFragment extends ReactNativeFragment
        implements AllNotificationListener {

    private static final String TOTAL_INBOX = "totalInbox";
    private static final String TOTAL_NOTIFICATION = "totalNotif";
    private static final String MP_OFFICIAL_STORE = "mp_official_store";
    private static final String REFRESH_NOTIFICATION = "refreshNotification";

    private ReactRootView mReactRootView;

    public static ReactNativeOfficialStoreFragment createInstance() {
        return new ReactNativeOfficialStoreFragment();
    }

    @Override
    public String getModuleName() {
        return ReactConst.Screen.OFFICIAL_STORE_HOME;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ReactUtils.startTracing(MP_OFFICIAL_STORE); // start trace when view created
//        super.onCreateView(inflater, container, savedInstanceState);
//
//        if (getActivity() != null)
//            reactRootView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));	            // set background color of react root view
//        reactRootView.
//        return reactRootView;

        mReactRootView = new ReactRootView(getContext());
        mReactRootView.startReactApplication(
                getReactNativeHost().getReactInstanceManager(),
                ReactConst.MAIN_MODULE,
                savedInstanceState
        );
        return mReactRootView;
    }

    protected ReactNativeHost getReactNativeHost() {
        return ((ReactApplication) getActivity().getApplication()).getReactNativeHost();
    }

    @Override
    protected Bundle getInitialBundle() {
        return getArguments() != null ? getArguments() : new Bundle();
    }

    @Override
    public void onNotificationChanged(int notificationCount, int inboxCount) {
        if (reactInstanceManager != null && reactInstanceManager.getCurrentReactContext() != null) {
            WritableMap param = Arguments.createMap();
            param.putInt(TOTAL_NOTIFICATION, notificationCount);
            param.putInt(TOTAL_INBOX, inboxCount);
            reactInstanceManager
                    .getCurrentReactContext()
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(REFRESH_NOTIFICATION, param);
        }
    }
}
