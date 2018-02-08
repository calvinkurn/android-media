package com.tokopedia.posapp.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.posapp.deeplink.Constants;
import com.tokopedia.posapp.react.PosReactConst;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.app.ReactNativeActivity;

/**
 * Created by okasurya on 9/20/17.
 */

public class PaymentProcessingActivity extends ReactNativeActivity {

    public static final String PARAM_EXTRAS = "extras";
    public static final String PARAM_DATA_PROCESS = "data_process";

    @DeepLink(Constants.Applinks.PAYMENT_PROCESSING)
    public static Intent getIntentFromDeeplink(Context context, Bundle extras) {
        Uri uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon().build();

        return new Intent(context, PaymentProcessingActivity.class)
                .setData(uri)
                .putExtras(extras);
    }

    public static Intent newInstance(Context context, String data) {
        return new Intent(context, PaymentProcessingActivity.class)
                .putExtra(PARAM_EXTRAS, data);
    }

    @Override
    protected Bundle getPropsBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(ReactConst.KEY_SCREEN, PosReactConst.Screen.MAIN_POS_O2O);
        bundle.putString(PosReactConst.Screen.PARAM_POS_PAGE, PosReactConst.Page.PAYMENT_PROCESSING);
        bundle.putString(USER_ID,  SessionHandler.getLoginID(this));
        bundle.putString(PARAM_DATA_PROCESS, getIntent().getStringExtra(PARAM_EXTRAS));

        return bundle;
    }
}
