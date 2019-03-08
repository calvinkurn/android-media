package com.tokopedia.officialstore.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.officialstore.R;
import com.tokopedia.officialstore.fragment.OldReactNativeOfficialStoreFragment;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.app.ReactFragmentActivity;

/**
 * @author okasurya on 3/8/19.
 */
public class OldReactNativeOfficialStoreActivity extends ReactFragmentActivity<OldReactNativeOfficialStoreFragment> {
    public static final String EXTRA_TITLE = "EXTRA_TITLE";

    private static final String CATEGORY = "Category";

    public static Intent getIntent(Context context) {
        Bundle extras = new Bundle();
        Intent intent = new Intent(context, OldReactNativeOfficialStoreActivity.class);
        extras.putString(ReactConst.KEY_SCREEN, ReactConst.Screen.OFFICIAL_STORE);
        extras.putString(EXTRA_TITLE, context.getString(R.string.react_native_banner_official_title));

        intent.putExtras(extras);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    public static Intent getCategoryIntent(Context context, String category) {
        Bundle extras = new Bundle();
        Intent intent = new Intent(context, OldReactNativeOfficialStoreActivity.class);
        extras.putString(ReactConst.KEY_SCREEN, ReactConst.Screen.OFFICIAL_STORE);
        extras.putString(EXTRA_TITLE, context.getString(R.string.react_native_banner_official_title));
        extras.putString(CATEGORY, category);

        intent.putExtras(extras);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    protected OldReactNativeOfficialStoreFragment getReactNativeFragment() {
        return OldReactNativeOfficialStoreFragment.instance(getIntent().getExtras());
    }

    @Override
    protected String getToolbarTitle() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            return getIntent().getExtras().getString(EXTRA_TITLE);
        }

        return "";
    }
}
