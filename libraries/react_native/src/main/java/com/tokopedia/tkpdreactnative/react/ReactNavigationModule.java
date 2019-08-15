package com.tokopedia.tkpdreactnative.react;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.ApplinkRouter;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.tkpdreactnative.R;
import com.tokopedia.tkpdreactnative.react.app.ReactNativeView;
import com.tokopedia.tkpdreactnative.react.fingerprint.view.FingerPrintUIHelper;
import com.tokopedia.tkpdreactnative.react.fingerprint.view.FingerprintDialogConfirmation;
import com.tokopedia.tkpdreactnative.react.singleauthpayment.view.SingleAuthPaymentDialog;
import com.tokopedia.tkpdreactnative.router.ReactNativeRouter;
import com.tokopedia.track.TrackApp;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.HashMap;

import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;

/**
 * @author ricoharisin .
 */

public class ReactNavigationModule extends ReactContextBaseJavaModule implements FingerPrintUIHelper.Callback {

    private final Context appContext;

    private Context context;
    private ProgressDialog progressDialog;
    private Promise mNativeModulePromise;

    public ReactNavigationModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.context = reactContext;
        this.appContext = reactContext.getApplicationContext();

        reactContext.addActivityEventListener(mActivityEventListener);
    }

    @Override
    public String getName() {
        return "NavigationModule";
    }

    @ReactMethod
    public void navigate(String appLinks, String extra) {
        if (extra != null && !TextUtils.isEmpty(extra)) {
            ((ApplinkRouter) context.getApplicationContext())
                    .goToApplinkActivity(this.getCurrentActivity(), appLinks, ReactUtils.convertBundle(extra));
        } else {
            ((ApplinkRouter) context.getApplicationContext())
                    .goToApplinkActivity(this.getCurrentActivity(), appLinks);
        }
    }

    @ReactMethod
    public void navigateWithMobileUrl(String appLinks, String mobileUrl, String extra) {
        if (((ApplinkRouter) context.getApplicationContext()).isSupportApplink(appLinks)) {
            ((ApplinkRouter) context.getApplicationContext())
                    .goToApplinkActivity(this.getCurrentActivity(), appLinks, ReactUtils.convertBundle(extra));
        } else {
            RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, mobileUrl));
        }
    }

    @ReactMethod
    public void navigateAndFinish(String appLinks, String extra) {
        navigate(appLinks, extra);
        finish();
    }

    @ReactMethod
    public void navigateToLoginWithResult(Promise promise) {
        if (((ApplinkRouter) context.getApplicationContext()).isSupportApplink(ApplinkConst.LOGIN)) {
            ((ApplinkRouter) context.getApplicationContext())
                    .goToApplinkActivity(this.getCurrentActivity(), ApplinkConst.LOGIN);
        }
    }

    @ReactMethod
    public void loginWithresult(Promise promise) {
        Intent intent = RouteManager.getIntent(getCurrentActivity(), ApplinkConst.LOGIN);
        String UserID = getUserId(getCurrentActivity().getApplicationContext());
        if (UserID.equals("")){
            getCurrentActivity().startActivityForResult(intent, ReactConst.REACT_LOGIN_REQUEST_CODE);
        }
    }

    @ReactMethod
    public void getCurrentUserId(Promise promise) {
        promise.resolve(getUserId(context));
    }

    public static String getUserId(Context context){
        UserSessionInterface userSession = new UserSession(context);
        return userSession.getUserId();
    }


    @ReactMethod
    public void getCurrentDeviceId(Promise promise) {
        if (context.getApplicationContext() instanceof AbstractionRouter) {
            UserSessionInterface userSession = new UserSession(context);
            promise.resolve(userSession.getDeviceId());
        }
    }

    @ReactMethod
    public void setTitleToolbar(final String title, final Promise promise) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (getCurrentActivity() != null && getCurrentActivity() instanceof ReactNativeView) {
                    ((ReactNativeView) getCurrentActivity()).actionSetToolbarTitle(title);
                    promise.resolve("OK");
                } else {
                    promise.resolve("NOT OK");
                }
            }
        });
    }

    @ReactMethod
    public void goToRBAInfo() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Dialog dialog = new Dialog(getCurrentActivity(), Dialog.Type.RETORIC);
                dialog.setBtnOk(getCurrentActivity().getString(R.string.title_ok));
                dialog.setDesc(getCurrentActivity().getString(R.string.single_auth_label_desc_info));
                dialog.setTitle(getCurrentActivity().getString(R.string.single_auth_label_setting_credit_card));
                dialog.setOnOkClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    @ReactMethod
    public void goToRBAThanks() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SingleAuthPaymentDialog singleAuthPaymentDialog = new SingleAuthPaymentDialog(getCurrentActivity(), Dialog.Type.PROMINANCE, ReactNavigationModule.this);
                singleAuthPaymentDialog.show();
            }
        });
    }

    @ReactMethod
    public void goToFingerprintThanks(final String transactionId) {
        if (getCurrentActivity().getApplication() instanceof ReactNativeRouter) {
            if (((ReactNativeRouter) getCurrentActivity().getApplication()).getEnableFingerprintPayment()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        FingerprintDialogConfirmation dialogPreferenceHide = new FingerprintDialogConfirmation(getCurrentActivity(), Dialog.Type.PROMINANCE, transactionId, ReactNavigationModule.this);
                        dialogPreferenceHide.show();
                    }
                });
            }
        }
    }

    @ReactMethod
    public void getFlavor(Promise promise) {
        promise.resolve(GlobalConfig.FLAVOR);
    }

    @ReactMethod
    public void sendTrackingEvent(ReadableMap dataLayer) {
        HashMap<String, Object> maps = dataLayer.toHashMap();
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(maps);

    }

    @ReactMethod
    public void trackScreenName(String name) {
        TrackApp.getInstance().getGTM().sendScreenAuthenticated(name);
    }

    @ReactMethod
    public void getGraphQLRequestHeader(Promise promise) {
        promise.resolve(AuthUtil.getHeaderRequestReactNative(context));
    }

    @ReactMethod
    public void finish() {
        if (getCurrentActivity() != null) {
            getCurrentActivity().finish();
        }
    }

    @Override
    public void showProgressDialog() {
        progressDialog = new ProgressDialog(getCurrentActivity());
        progressDialog.setMessage(getCurrentActivity().getString(R.string.title_loading));
        progressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @ReactMethod
    public void getDebuggingMode(Promise promise){
        if (GlobalConfig.isAllowDebuggingTools()) {
            promise.resolve("debug");
        } else {
            promise.resolve("release");
        }
    }

    @ReactMethod
    public void navigateWithResult(String applink, int requestCode, Promise promise) {
        if(RouteManager.isSupportApplink(context, applink) && getCurrentActivity() != null) {
            Intent intent = RouteManager.getIntent(getCurrentActivity(), applink);
            getCurrentActivity().startActivityForResult(intent, requestCode);
            mNativeModulePromise = promise;
        }
    }

    private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {
        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
            super.onActivityResult(activity, requestCode, resultCode, data);

            if (requestCode == ReactConst.REACT_ADD_CREDIT_CARD_REQUEST_CODE) {
                if (resultCode == Activity.RESULT_OK) {
                    mNativeModulePromise.resolve("OK");
                } else {
                    mNativeModulePromise.reject("FAILED");
                }
            }
        }
    };
}
