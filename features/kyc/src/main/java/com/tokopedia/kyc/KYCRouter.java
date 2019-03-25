package com.tokopedia.kyc;

import android.app.Activity;

import com.tokopedia.abstraction.Actions.interfaces.ActionCreator;
import com.tokopedia.abstraction.Actions.interfaces.ActionDataProvider;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;

import java.util.ArrayList;
import java.util.HashMap;

public interface KYCRouter {
    BaseDaggerFragment getKYCCameraFragment(
            ActionCreator<HashMap<String, Object>, Integer> actionCreator,
            ActionDataProvider<ArrayList<String>, Object> keysListProvider, int cameraType);
    void actionOpenGeneralWebView(Activity activity, String mobileUrl);
    String getUserId();
}
