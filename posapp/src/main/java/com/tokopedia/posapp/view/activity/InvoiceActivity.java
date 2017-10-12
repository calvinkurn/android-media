package com.tokopedia.posapp.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.posapp.react.PosReactConst;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.app.ReactNativeActivity;

/**
 * Created by okasurya on 10/11/17.
 */

public class InvoiceActivity extends ReactNativeActivity {

    public static final String DATA = "data";

    public static Intent newTopIntent(Context context, String data) {
        return new Intent(context, InvoiceActivity.class)
                .putExtra(DATA, data)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

    @Override
    protected Bundle getPropsBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(ReactConst.KEY_SCREEN, PosReactConst.Screen.MAIN_POS_O2O);
        bundle.putString(PosReactConst.Screen.PARAM_POS_PAGE, PosReactConst.Page.INVOICE);

        bundle.putString(DATA, getIntent().getStringExtra(DATA));

        return bundle;
    }

    @Override
    public void onBackPressed() {

    }
}
