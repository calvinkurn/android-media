package com.tokopedia.tkpdreactnative.react.app;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.router.reactnative.IReactNativeRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.tkpdreactnative.R;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.ReactNavigationModule;

/**
 *
 * Abstract activity for react native fragment
 * Created by okasurya on 1/9/18.
 */

public abstract class ReactFragmentActivity<T extends ReactNativeFragment> extends BasePresenterActivity implements ReactNativeView {

    public static final String IS_DEEP_LINK_FLAG = "is_deep_link_flag";
    public static final String ANDROID_INTENT_EXTRA_REFERRER = "android.intent.extra.REFERRER";
    public static final String DEEP_LINK_URI = "deep_link_uri";

    private ProgressBar loaderBootingReact;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_react_native;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (GlobalConfig.isAllowDebuggingTools()
                && keyCode == KeyEvent.KEYCODE_MENU
                && ((ReactApplication) getApplication()).getReactNativeHost().getReactInstanceManager() != null) {
            ((ReactApplication) getApplication()).getReactNativeHost().getReactInstanceManager().showDevOptionsDialog();
            return true;
        }

        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void initView() {
        actionSetToolbarTitle(getToolbarTitle());
        loaderBootingReact = (ProgressBar) findViewById(R.id.rn_progressbar);
        T fragment = getReactNativeFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if (getFragmentManager().findFragmentById(R.id.container) == null) {
            fragmentTransaction.add(R.id.container, fragment, fragment.getClass().getSimpleName());
        }
        fragmentTransaction.commit();
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
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ReactConst.REACT_LOGIN_REQUEST_CODE){
            if (resultCode == Activity.RESULT_OK){
                String UserID = ReactNavigationModule.getUserId(this);
                ((IReactNativeRouter) getApplication()).sendLoginEmitter(UserID);
            }
        } else if (requestCode == ReactConst.REACT_ADD_CREDIT_CARD_REQUEST_CODE) {
            ReactInstanceManager reactInstanceManager = ((ReactApplication) getApplication()).getReactNativeHost().getReactInstanceManager();
            reactInstanceManager.onActivityResult(this, requestCode, resultCode, data);
        }
    }

    @Override
    public void actionSetToolbarTitle(String title) {
        if(!TextUtils.isEmpty(title)) {
            toolbar.setTitle(title);
        }
    }

    @Override
    public void showLoaderReactPage() {
        loaderBootingReact.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoaderReactPage() {
        loaderBootingReact.setVisibility(View.GONE);
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    protected Bundle getReactNativeProps() {
        Bundle bundle = getIntent().getExtras();
        Bundle newBundle = new Bundle();

        // clear bundle from deeplinks default value
        for (String key : bundle.keySet()) {
            if (!key.equalsIgnoreCase(IS_DEEP_LINK_FLAG) &&
                    !key.equalsIgnoreCase(ANDROID_INTENT_EXTRA_REFERRER) &&
                    !key.equalsIgnoreCase(DEEP_LINK_URI)) {
                newBundle.putString(key, bundle.getString(key));
            }
        }
        return newBundle;
    }

    protected abstract T getReactNativeFragment();

    protected abstract String getToolbarTitle();
}
