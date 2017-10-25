package com.tokopedia.posapp.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.posapp.PosConstants;
import com.tokopedia.posapp.deeplink.Constants;
import com.tokopedia.posapp.react.PosReactConst;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.app.ReactNativeActivity;

/**
 * Created by okasurya on 10/25/17.
 */

public class PaymentErrorActivity extends ReactNativeActivity {
    @DeepLink(Constants.Applinks.PAYMENT_ERROR)
    public static Intent getAoolinkInstance(Context context, Bundle bundle) {
        Intent intent = new Intent(context, PaymentErrorActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    @Override
    protected Bundle getPropsBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(ReactConst.KEY_SCREEN, PosReactConst.Screen.MAIN_POS_O2O);
        bundle.putString(PosReactConst.Screen.PARAM_POS_PAGE, PosReactConst.Page.PAYMENT_ERROR);
        return bundle;
    }
}
