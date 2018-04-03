package com.tokopedia.tkpdreactnative.react.app;

import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.facebook.react.ReactApplication;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.tkpdreactnative.R;

/**
 *
 * Abstract activity for react native fragment
 * Created by okasurya on 1/9/18.
 */

public abstract class ReactFragmentActivity<T extends ReactNativeFragment> extends BasePresenterActivity implements ReactNativeView {

    public static final String IS_DEEP_LINK_FLAG = "is_deep_link_flag";
    public static final String ANDROID_INTENT_EXTRA_REFERRER = "android.intent.extra.REFERRER";
    public static final String DEEP_LINK_URI = "deep_link_uri";

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
    public void actionSetToolbarTitle(String title) {
        if(!TextUtils.isEmpty(title)) {
            toolbar.setTitle(title);
        }
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
