package com.tokopedia.officialstore.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.tokopedia.navigation_common.listener.AllNotificationListener;
import com.tokopedia.nps.presentation.view.dialog.AdvancedAppRatingDialog;
import com.tokopedia.nps.presentation.view.dialog.AppFeedbackRatingBottomSheet;
import com.tokopedia.officialstore.R;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.ReactUtils;
import com.tokopedia.tkpdreactnative.react.app.ReactNativeFragment;

import org.jetbrains.annotations.NotNull;

public class ReactNativeOfficialStoreFragment extends ReactNativeFragment
        implements AllNotificationListener {

    private static final String TOTAL_INBOX = "totalInbox";
    private static final String TOTAL_NOTIFICATION = "totalNotif";
    private static final String MP_OFFICIAL_STORE = "mp_official_store";
    private static final String REFRESH_NOTIFICATION = "refreshNotification";

    public static ReactNativeOfficialStoreFragment createInstance() {
        return new ReactNativeOfficialStoreFragment();
    }

    private void initView() {
        FragmentManager manager = getActivity().getSupportFragmentManager();

        if (manager != null) {
            AppFeedbackRatingBottomSheet rating = new AppFeedbackRatingBottomSheet();
            rating.show(manager, "AppFeedbackRatingBottomSheet");
        }

    }

    @Override
    public String getModuleName() {
        return ReactConst.Screen.OFFICIAL_STORE_HOME;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ReactUtils.startTracing(MP_OFFICIAL_STORE); // start trace when view created

        View view = super.onCreateView(inflater, container, savedInstanceState);

        if (getActivity() != null && view != null) {
            // set background color of react root view
            view.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            view.invalidate();
        }

        initView();
        return view;
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
