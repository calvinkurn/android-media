package com.tokopedia.posapp.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.tokopedia.posapp.deeplink.Constants;
import com.tokopedia.posapp.view.fragment.ReactInvoiceFragment;
import com.tokopedia.tkpdreactnative.react.app.ReactFragmentActivity;
import com.tokopedia.tkpdreactnative.react.app.ReactNativeFragment;

/**
 * Created by okasurya on 10/11/17.
 */

public class InvoiceActivity extends ReactFragmentActivity {

    public static final String DATA = "data";
    private static final String IS_ERROR = "isError";
    public static final String ERROR_TITLE = "errorTitle";
    public static final String ERROR_MESSAGE = "errorMessage";

    private ReactInstanceManager reactInstanceManager;

    @DeepLink(Constants.Applinks.PAYMENT_INVOICE)
    public static Intent newApplinkIntent(Context context, Bundle bundle) {
        Intent intent = new Intent(context, InvoiceActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reactInstanceManager = ((ReactApplication) getApplication()).getReactNativeHost().getReactInstanceManager();
    }

    public static Intent newTopIntent(Context context, String data) {
        return new Intent(context, InvoiceActivity.class)
                .putExtra(DATA, data)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

    public static Intent newErrorIntent(Context context, String errorTitle, String errorMessage) {
        Intent intent = new Intent(context, InvoiceActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(IS_ERROR, true);
        intent.putExtra(ERROR_TITLE, errorTitle);
        intent.putExtra(ERROR_MESSAGE, errorMessage);
        return intent;
    }

    @Override
    protected ReactNativeFragment getReactNativeFragment() {
        return ReactInvoiceFragment.newInstance(getReactNativeProps());
    }

    @Override
    protected String getToolbarTitle() {
        return null;
    }

    @Override
    public void onBackPressed() {
        // no-op
    }
}
