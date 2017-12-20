package com.tokopedia.core.router.logistic;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.core.router.OnActivityResultListener;

/**
 * Created by normansyahputa on 12/20/17.
 */

public interface LogisticRouter {
    void navigateToChooseAddressActivityRequest(Intent intent, int requestCode);
    void navigateToChooseAddressActivityRequest(Fragment fragment, Intent intent, int requestCode);
    void onActivityResultChooseAddress(int requestCode, Intent data, OnActivityResultListener onActivityResultListener);
}
