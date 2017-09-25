package com.tokopedia.posapp.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.airbnb.deeplinkdispatch.DeepLink;

import com.tokopedia.core.session.presenter.Session;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.posapp.deeplink.Constants;
import com.tokopedia.posapp.react.PosReactConst;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.app.ReactNativeActivity;

/**
 * Created by okasurya on 9/12/17.
 */

public class PaymentActivity extends ReactNativeActivity {

    private static final String TOTAL_PAYMENT = "total_payment";

    @DeepLink(Constants.Applinks.PAYMENT_CHECKOUT)
    public static Intent getIntentFromDeeplink(Context context, Bundle extras) {
        Uri uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon().build();
        extras.putString(TOTAL_PAYMENT, uri.getQueryParameter(TOTAL_PAYMENT));


        return new Intent(context, PaymentActivity.class)
                .setData(uri)
                .putExtras(extras);
    }

    @Override
    protected Bundle getPropsBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(ReactConst.KEY_SCREEN, PosReactConst.Screen.MAIN_POS_O2O);
        bundle.putString(PosReactConst.Screen.PARAM_POS_PAGE,  PosReactConst.Page.PAYMENT);
        bundle.putString(USER_ID,  SessionHandler.getLoginID(this));
        bundle.putString(TOTAL_PAYMENT, getIntent().getExtras().getString(TOTAL_PAYMENT));

        return bundle;
    }

    @Override
    public void onBackPressed() {

    }
}
