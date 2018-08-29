package com.tokopedia.posapp.payment.checkout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.airbnb.deeplinkdispatch.DeepLink;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.posapp.applink.PosAppLink;
import com.tokopedia.posapp.react.PosReactConst;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.app.ReactFragmentActivity;
import com.tokopedia.tkpdreactnative.react.app.ReactNativeFragment;

/**
 * Created by okasurya on 9/12/17.
 */

public class CheckoutActivity extends ReactFragmentActivity {

    @DeepLink(PosAppLink.PAYMENT_CHECKOUT)
    public static Intent getIntentFromDeeplink(Context context, Bundle extras) {
        return new Intent(context, CheckoutActivity.class).putExtras(extras);
    }

    protected Bundle getPropsBundle() {
        Bundle bundle = getReactNativeProps();
        bundle.putString(ReactConst.KEY_SCREEN, PosReactConst.Screen.MAIN_POS_O2O);
        bundle.putString(PosReactConst.Screen.PARAM_POS_PAGE,  PosReactConst.Page.PAYMENT);
        bundle.putString(PosReactConst.USER_ID,  SessionHandler.getLoginID(this));

        return bundle;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected ReactNativeFragment getReactNativeFragment() {
        return ReactCheckoutFragment.newInstance(getPropsBundle());
    }

    @Override
    protected String getToolbarTitle() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideToolbar();
    }
}
