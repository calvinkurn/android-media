package com.tokopedia.posapp.payment.invoice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.tokopedia.posapp.applink.PosAppLink;
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
    public static final String BACK_BUTTON_PRESSED = "backButtonPressed";

    private ReactInstanceManager reactInstanceManager;

    @DeepLink(PosAppLink.PAYMENT_INVOICE)
    public static Intent newApplinkIntent(Context context, Bundle bundle) {
        Intent intent = new Intent(context, InvoiceActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }

    public static Intent newErrorIntent(Context context, String errorTitle, String errorMessage) {
        Intent intent = new Intent(context, InvoiceActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(IS_ERROR, true);
        intent.putExtra(ERROR_TITLE, errorTitle);
        intent.putExtra(ERROR_MESSAGE, errorMessage);
        return intent;
    }

    @Override
    protected ReactNativeFragment getReactNativeFragment() {
        return ReactInvoiceFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected String getToolbarTitle() {
        return null;
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return false;
    }

    @Override
    public void onBackPressed() {
        backButtonPressed();
    }

    private void backButtonPressed() {
        if(getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().getBoolean(IS_ERROR)) {
            WritableMap params = Arguments.createMap();
            params.putBoolean(BACK_BUTTON_PRESSED, true);
            sendEmitter(reactInstanceManager.getCurrentReactContext(), BACK_BUTTON_PRESSED, params);
        }
    }

    private void sendEmitter(ReactContext reactContext,
                             String eventName,
                             @Nullable WritableMap params) {
        if(reactContext != null) {
            reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(eventName, params);
        }
    }
}
