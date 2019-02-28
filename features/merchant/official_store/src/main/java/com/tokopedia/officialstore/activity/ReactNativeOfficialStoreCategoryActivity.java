package com.tokopedia.officialstore.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.officialstore.R;
import com.tokopedia.officialstore.fragment.ReactNativeOfficialStoreCategoryFragment;
import com.tokopedia.tkpdreactnative.react.ReactUtils;
import com.tokopedia.tkpdreactnative.react.app.ReactFragmentActivity;

public class ReactNativeOfficialStoreCategoryActivity extends ReactFragmentActivity<ReactNativeOfficialStoreCategoryFragment> {
    private static final String MP_OFFICIAL_STORE_CATEGORY = "mp_official_store_category_activity";

    @DeepLink({ ApplinkConst.OFFICIAL_STORES_CATEGORY })
    public static Intent getOfficialStoreCategoryApplinkCallingIntent(Context context, Bundle bundle) {
        ReactUtils.startTracing(MP_OFFICIAL_STORE_CATEGORY);
        return ReactNativeOfficialStoreActivity.createApplinkCallingIntent(context, bundle);
    }

    public static Intent createApplinkCallingIntent(Context context, Bundle extras) {
        Intent intent = new Intent(context, ReactNativeOfficialStoreCategoryActivity.class);
        intent.putExtras(extras);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    protected ReactNativeOfficialStoreCategoryFragment getReactNativeFragment() {
        return ReactNativeOfficialStoreCategoryFragment.createInstance(getReactNativeProps());
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