package com.tokopedia.officialstore.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.officialstore.R;
import com.tokopedia.officialstore.fragment.ReactNativeOfficialStoreFragment;
import com.tokopedia.tkpdreactnative.react.app.ReactFragmentActivity;

public class ReactNativeOfficialStoreActivity extends ReactFragmentActivity<ReactNativeOfficialStoreFragment> {

    public static Intent createApplinkCallingIntent(Context context,
                                                    Bundle extras) {
        Intent intent = new Intent(context, ReactNativeOfficialStoreActivity.class);
        intent.putExtras(extras);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    protected ReactNativeOfficialStoreFragment getReactNativeFragment() {
        return ReactNativeOfficialStoreFragment.createInstance();
    }

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.react_native_banner_official_title);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // No super to avoid crash transactionTooLarge
    }
}
