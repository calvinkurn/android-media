package com.tokopedia.officialstore.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.ReactUtils;
import com.tokopedia.tkpdreactnative.react.app.GeneralReactNativeFragment;
import com.tokopedia.tkpdreactnative.react.app.ReactFragmentActivity;

/**
 * Created by yogie putra on 29/03/18.
 */

public class ReactNativeBrandListOsActivity extends ReactFragmentActivity<GeneralReactNativeFragment> {

    public static final String EXTRA_TITLE = "EXTRA_TITLE";

    private static final String MP_BRAND_LIST = "mp_brand_list";
    
    @DeepLink({ApplinkConst.BRAND_LIST, ApplinkConst.BRAND_LIST_WITH_SLASH})
    public static Intent getBrandlistApplinkCallingIntent(Context context, Bundle bundle){
        ReactUtils.startTracing(MP_BRAND_LIST);
        return ReactNativeBrandListOsActivity.createApplinkCallingIntent(context, ReactConst.Screen.BRANDLIST_PAGE, "All Brands", bundle);
    }

    public static Intent createApplinkCallingIntent(Context context, String reactScreenName, String pageTitle, Bundle extras){
        Intent intent = new Intent(context, ReactNativeBrandListOsActivity.class);
        extras.putString(ReactConst.KEY_SCREEN, reactScreenName);
        extras.putString(EXTRA_TITLE, pageTitle);
        intent.putExtras(extras);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    public static Intent createCallingIntent(Context context, String reactScreenName, String pageTitle){
        Intent intent = new Intent(context, ReactNativeBrandListOsActivity.class);
        Bundle extras = new Bundle();
        extras.putString(ReactConst.KEY_SCREEN, reactScreenName);
        extras.putString(EXTRA_TITLE, pageTitle);
        intent.putExtras(extras);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    protected GeneralReactNativeFragment getReactNativeFragment() {
        return GeneralReactNativeFragment.createInstance(getReactNativeProps());
    }

    @Override
    protected String getToolbarTitle() {
        if (getIntent() !=  null && getIntent().getExtras() != null){
            return getIntent().getExtras().getString(EXTRA_TITLE);
        }
        return "";
    }
}
