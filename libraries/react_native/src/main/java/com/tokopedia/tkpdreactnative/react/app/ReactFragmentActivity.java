package com.tokopedia.tkpdreactnative.react.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.tokopedia.abstraction.base.view.activity.BaseActivity;
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

public abstract class ReactFragmentActivity<T extends ReactNativeFragment> extends BaseActivity implements ReactNativeView {

    public static final String IS_DEEP_LINK_FLAG = "is_deep_link_flag";
    public static final String ANDROID_INTENT_EXTRA_REFERRER = "android.intent.extra.REFERRER";
    public static final String DEEP_LINK_URI = "deep_link_uri";

    private ProgressBar loaderBootingReact;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_react_native);
        initView();
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

    protected void initView() {
        setupToolbar();
        loaderBootingReact = findViewById(R.id.rn_progressbar);
        T fragment = getReactNativeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (getSupportFragmentManager().findFragmentById(R.id.container) == null) {
            fragmentTransaction.add(R.id.container, fragment, fragment.getClass().getSimpleName());
        }
        fragmentTransaction.commit();
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

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(this.getTitle());
        }
        actionSetToolbarTitle(getToolbarTitle());
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public void actionSetToolbarTitle(String title) {
        if(!TextUtils.isEmpty(title) && getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showLoaderReactPage() {
        loaderBootingReact.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoaderReactPage() {
        loaderBootingReact.setVisibility(View.GONE);
    }

    protected Bundle getReactNativeProps() {
        Bundle bundle = getIntent().getExtras();
        Bundle newBundle = new Bundle();

        // clear bundle from deeplinks default value
        assert bundle != null;
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
