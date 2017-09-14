package com.tokopedia.posapp.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.app.ReactNativeActivity;
import com.tokopedia.core.react.ReactConst;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.posapp.deeplink.Constants;
import com.tokopedia.posapp.react.PosReactConst;

/**
 * Created by okasurya on 9/13/17.
 */

public class PaymentProcessActivity extends ReactNativeActivity {
    @DeepLink(Constants.Applinks.PAYMENT_PROCESSING)
    public static Intent getIntentFromDeeplink(Context context, Bundle extras) {
        Uri uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon().build();

        extras.putString(ReactConst.KEY_SCREEN, PosReactConst.Screen.MAIN_POS_O2O);
        extras.putString(PosReactConst.Screen.PARAM_POS_PAGE, PosReactConst.Page.PAYMENT_PROCESSING);
        extras.putString(USER_ID, SessionHandler.getLoginID(context));

        return new Intent(context, PaymentActivity.class)
                .setData(uri)
                .putExtras(extras);
    }

    @Override
    protected Bundle getPropsBundle() {
        Bundle extras = getIntent().getExtras();
        Bundle bundle = new Bundle();
        if(extras != null) {
            bundle.putString(ReactConst.KEY_SCREEN, extras.getString(ReactConst.KEY_SCREEN));
            bundle.putString(PosReactConst.Screen.PARAM_POS_PAGE, extras.getString(PosReactConst.Screen.PARAM_POS_PAGE));
            bundle.putString(USER_ID, extras.getString(USER_ID));
        }

        return bundle;
    }
}
