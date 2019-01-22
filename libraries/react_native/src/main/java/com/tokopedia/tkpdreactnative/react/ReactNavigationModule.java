package com.tokopedia.tkpdreactnative.react;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.tkpdreactnative.R;
import com.tokopedia.tkpdreactnative.react.app.ReactNativeView;
import com.tokopedia.tkpdreactnative.react.fingerprint.view.FingerPrintUIHelper;
import com.tokopedia.tkpdreactnative.react.fingerprint.view.FingerprintDialogConfirmation;
import com.tokopedia.tkpdreactnative.react.singleauthpayment.view.SingleAuthPaymentDialog;
import com.tokopedia.tkpdreactnative.router.ReactNativeRouter;
import com.tokopedia.core.util.GlobalConfig;

import java.util.HashMap;

import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;

/**
 * @author ricoharisin .
 */

public class ReactNavigationModule extends ReactContextBaseJavaModule implements FingerPrintUIHelper.Callback {
    private static final int LOGIN_REQUEST_CODE = 1005;
    private final Context appContext;

    private Context context;
    private ProgressDialog progressDialog;

    public ReactNavigationModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.context = reactContext;
        this.appContext = reactContext.getApplicationContext();
    }

    @Override
    public String getName() {
        return "NavigationModule";
    }

    @ReactMethod
    public void navigate(String appLinks, String extra) {
        if (!extra.isEmpty()) {
            ((TkpdCoreRouter) context.getApplicationContext())
                    .actionApplink(this.getCurrentActivity(), appLinks, extra);
        } else {
            ((TkpdCoreRouter) context.getApplicationContext())
                    .actionApplink(this.getCurrentActivity(), appLinks);
        }
    }

    @ReactMethod
    public void navigateWithMobileUrl(String appLinks, String mobileUrl, String extra) {
        if (((IDigitalModuleRouter) context.getApplicationContext()).isSupportedDelegateDeepLink(appLinks)) {
            ((TkpdCoreRouter) context.getApplicationContext())
                    .actionApplink(this.getCurrentActivity(), appLinks, extra);
        } else {
            ((TkpdCoreRouter) context.getApplicationContext())
                    .actionOpenGeneralWebView(this.getCurrentActivity(), mobileUrl);
        }
    }

    @ReactMethod
    public void navigateAndFinish(String appLinks, String extra) {
        navigate(appLinks, extra);

        finish();
    }

    @ReactMethod
    public void navigateToLoginWithResult(Promise promise) {
        if (((IDigitalModuleRouter) context.getApplicationContext()).isSupportedDelegateDeepLink(Constants.Applinks.LOGIN)) {
            ((TkpdCoreRouter) context.getApplicationContext())
                    .actionApplink(this.getCurrentActivity(), Constants.Applinks.LOGIN);
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
        return SessionHandler.getLoginID(context);
    }


    @ReactMethod
    public void getCurrentDeviceId(Promise promise) {
        if (context.getApplicationContext() instanceof AbstractionRouter) {
            promise.resolve(((AbstractionRouter) context.getApplicationContext()).getSession().getDeviceId());
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
        if (getCurrentActivity() != null && getCurrentActivity().getApplication() instanceof TkpdCoreRouter) {
            promise.resolve(((TkpdCoreRouter) getCurrentActivity().getApplication()).getFlavor());
        } else {
            promise.resolve("");
        }
    }

    @ReactMethod
    public void sendTrackingEvent(ReadableMap dataLayer) {
        HashMap<String, Object> maps = dataLayer.toHashMap();
        TrackingUtils.eventTrackingEnhancedEcommerce(appContext, maps);
    }

    @ReactMethod
    public void trackScreenName(String name) {
        if(context.getApplicationContext() instanceof AbstractionRouter) {
            AnalyticTracker analyticTracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
            analyticTracker.sendScreen(getCurrentActivity(), name);
        }
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
}
