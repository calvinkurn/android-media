package com.tokopedia.tkpd.thankyou.view;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.fragment.ReactNativeThankYouPageFragment;
import com.tokopedia.tkpd.thankyou.domain.model.ThanksTrackerConst;
import com.tokopedia.tkpd.thankyou.view.viewmodel.ThanksTrackerData;
import com.tokopedia.tkpdreactnative.react.ReactConst;


public class ReactNativeThankYouPageActivity extends BasePresenterActivity {
    public static final String EXTRA_TITLE = "EXTRA_TITLE";

    private ReactInstanceManager reactInstanceManager;

    @DeepLink("tokopedia://thankyou/{platform}/{template}")
    public static Intent getThankYouPageApplinkIntent(Context context, Bundle bundle) {
        return ReactNativeThankYouPageActivity.createReactNativeActivity(
                context, ReactConst.Screen.THANK_YOU_PAGE,
                "Thank You"
        ).putExtras(bundle);
    }

    public static Intent createReactNativeActivity(Context context,
                                                   String reactScreenName,
                                                   String pageTitle) {
        Intent intent = new Intent(context, ReactNativeThankYouPageActivity.class);
        Bundle extras = new Bundle();
        extras.putString(ReactConst.KEY_SCREEN, reactScreenName);
        extras.putString(EXTRA_TITLE, pageTitle);
        intent.putExtras(extras);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reactInstanceManager = ((ReactApplication) getApplication())
                .getReactNativeHost().getReactInstanceManager();
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    protected void setupURIPass(Uri data) {
    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_react_native;
    }

    @Override
    protected void initView() {
        Bundle initialProps = getIntent().getExtras();
        initialProps.remove("android.intent.extra.REFERRER");
        initialProps.remove("is_deep_link_flag");
        initialProps.remove("deep_link_uri");
        Log.i("ReactNative", initialProps.toString());
        sendAnalytics(initialProps);
        ReactNativeThankYouPageFragment fragment = ReactNativeThankYouPageFragment.createInstance(initialProps);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if (getFragmentManager().findFragmentById(R.id.container) == null) {
            fragmentTransaction.add(R.id.container, fragment, fragment.getClass().getSimpleName());
        }
        fragmentTransaction.commit();
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_OFFICIAL_STORE_REACT;
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
        ThanksTrackerService.start(this, data);
    }
}
