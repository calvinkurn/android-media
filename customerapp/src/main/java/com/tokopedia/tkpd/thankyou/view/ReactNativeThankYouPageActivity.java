package com.tokopedia.tkpd.thankyou.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.KeyEvent;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.common_wallet.balance.data.CacheUtil;
import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.nps.presentation.view.dialog.AppFeedbackRatingBottomSheet;
import com.tokopedia.tkpd.BuildConfig;
import com.tokopedia.tkpd.home.fragment.ReactNativeThankYouPageFragment;
import com.tokopedia.tkpd.thankyou.domain.model.ThanksTrackerConst;
import com.tokopedia.tkpd.thankyou.view.viewmodel.ThanksTrackerData;
import com.tokopedia.tkpd.tkpdreputation.ReputationRouter;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.ReactUtils;
import com.tokopedia.tkpdreactnative.react.app.ReactFragmentActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;


public class ReactNativeThankYouPageActivity extends ReactFragmentActivity<ReactNativeThankYouPageFragment>
        implements ReputationRouter {
    public static final String EXTRA_TITLE = "EXTRA_TITLE";

    private static final String PLATFORM = "platform";
    private static final String DIGITAL = "digital";
    private static final String GL_THANK_YOU_PAGE = "gl_thank_you_page";
    private static final String PAGE_TITLE = "Thank You";

    private ReactInstanceManager reactInstanceManager;
    private static final String SAVED_VERSION = "SAVED_VERSION";
    private static final String REACT_NAVIGATION_MODULE = "REACT_NAVIGATION_MODULE";
    private static final String IS_SHOWING_APP_RATING = "isShowAppRating";

    @DeepLink("tokopedia://thankyou/{platform}/{template}")
    public static Intent getThankYouPageApplinkIntent(Context context, Bundle bundle) {
        ReactUtils.startTracing(GL_THANK_YOU_PAGE);
        return ReactNativeThankYouPageActivity.createReactNativeActivity(
                context, PAGE_TITLE
        ).putExtras(bundle);
    }

    public static Intent createReactNativeActivity(Context context, String pageTitle) {
        Intent intent = new Intent(context, ReactNativeThankYouPageActivity.class);
        Bundle extras = new Bundle();

        SharedPreferences sharedPreferences = context.getSharedPreferences(REACT_NAVIGATION_MODULE, Context.MODE_PRIVATE);

        extras.putString(ReactConst.KEY_SCREEN, ReactConst.Screen.THANK_YOU_PAGE);
        extras.putString(EXTRA_TITLE, pageTitle);
        extras.putBoolean(IS_SHOWING_APP_RATING, isShowAppRating(sharedPreferences, context));
        intent.putExtras(extras);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reactInstanceManager = ((ReactApplication) getApplication())
                .getReactNativeHost().getReactInstanceManager();
        PurchaseNotifier.notify(this, getIntent().getExtras());
        resetWalletCache();
    }

    @Override
    protected ReactNativeThankYouPageFragment getReactNativeFragment() {
        Bundle initialProps = getIntent().getExtras();
        if (initialProps != null) {
            initialProps.remove("android.intent.extra.REFERRER");
            initialProps.remove("is_deep_link_flag");
            initialProps.remove("deep_link_uri");
            sendAnalytics(initialProps);
        }
        return ReactNativeThankYouPageFragment.createInstance(initialProps);
    }

    @Override
    protected String getToolbarTitle() {
        return "Tokopedia";
    }

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        reactInstanceManager = ((ReactApplication) getApplication())
                .getReactNativeHost().getReactInstanceManager();
        if (keyCode == KeyEvent.KEYCODE_MENU && reactInstanceManager != null) {
            reactInstanceManager.showDevOptionsDialog();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    private void sendAnalytics(Bundle initialProps) {
        ThanksTrackerData data = new ThanksTrackerData();
        data.setPlatform(initialProps.getString(ThanksTrackerConst.Key.PLATFORM));
        data.setTemplate(initialProps.getString(ThanksTrackerConst.Key.TEMPLATE));
        data.setId(initialProps.getString(ThanksTrackerConst.Key.ID));
        if (initialProps.getString(ThanksTrackerConst.Key.SHOP_TYPES) != null &&
                !initialProps.getString(ThanksTrackerConst.Key.SHOP_TYPES).isEmpty()) {
            try {
                data.setShopTypes(Arrays.asList(URLDecoder.decode(initialProps.getString(ThanksTrackerConst.Key.SHOP_TYPES), "UTF-8").split(",")));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        ThanksTrackerService.start(this, data);
    }

    /* Check savedVersion in sharedpreferences
        If there is no savedVersion in sharedpreferences, then save the currentVersion
        If there is savedVersion in sharedpreferences, then check if currentVersion - savedVersion equals to 4
        If it is equals to 4 then show appRating */
    private static boolean isShowAppRating(SharedPreferences sharedPreferences, Context context) {
        String GlobalVersionName = BuildConfig.VERSION_NAME;
        sharedPreferences = context.getSharedPreferences(REACT_NAVIGATION_MODULE, Context.MODE_PRIVATE);
        String savedVersion = sharedPreferences.getString(SAVED_VERSION, "");
        if (savedVersion.isEmpty()) {
            sharedPreferences.edit().putString(SAVED_VERSION, GlobalVersionName).apply();
            return true;
        } else {
            String[] splittedCurrentVersionName = GlobalVersionName.split(".");
            String currentMinorVersion = splittedCurrentVersionName.length > 0 ? splittedCurrentVersionName[1] : "00" ;
            int currentMinorVersionInt = Integer.parseInt(currentMinorVersion);

            String[] splittedSavedVersionName = savedVersion.split(".");
            String savedMinorVersion = splittedSavedVersionName.length > 0 ? splittedSavedVersionName[1]: "00";
            int savedMinorVersionInt = Integer.parseInt(savedMinorVersion);

            if (currentMinorVersionInt - savedMinorVersionInt >= 4) {
                sharedPreferences.edit().putString(SAVED_VERSION, GlobalVersionName).apply();
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();

        if (isDigital() && manager != null) {
            AppFeedbackRatingBottomSheet rating = new AppFeedbackRatingBottomSheet();
            rating.setDialogDismissListener(this::closeThankyouPage);
            rating.showDialog(manager, this);
        } else {
            closeThankyouPage();
        }
    }

    private boolean isDigital() {
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            String platform = extra.getString(PLATFORM);
            return platform != null && platform.equals(DIGITAL);
        }
        return false;
    }

    private void resetWalletCache() {
        PersistentCacheManager.instance.delete(CacheUtil.KEY_TOKOCASH_BALANCE_CACHE);
    }

    private void closeThankyouPage() {
        RouteManager.route(this, ApplinkConst.HOME);
        finish();
    }

    @Override
    public Intent getInboxReputationIntent(Context context) {
        return null;
    }

    @Override
    public Fragment getReputationHistoryFragment() {
        return null;
    }

    @Override
    public Intent getLoginIntent(Context context) {
        return null;
    }

    @Override
    public Intent getShopPageIntent(Context context, String shopId) {
        return null;
    }

    @Override
    public Intent getShoProductListIntent(Context context, String shopId, String keyword, String etalaseId) {
        return null;
    }

    @Override
    public Intent getTopProfileIntent(Context context, String reviewUserId) {
        return null;
    }

    @Override
    public void showAppFeedbackRatingDialog(FragmentManager fragmentManager, Context context, BottomSheets.BottomSheetDismissListener listener) {

    }

    @Override
    public void showSimpleAppRatingDialog(Activity activity) {

    }
}