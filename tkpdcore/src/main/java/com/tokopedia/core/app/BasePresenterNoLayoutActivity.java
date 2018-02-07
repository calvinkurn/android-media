package com.tokopedia.core.app;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import com.tokopedia.core.R;

import butterknife.ButterKnife;

/**
 * @author by alifa on 5/5/17.
 */

/**
 * Extends one of BaseActivity from tkpd abstraction eg:BaseSimpleActivity, BaseStepperActivity, BaseTabActivity, etc
 */
@Deprecated
public abstract class BasePresenterNoLayoutActivity<P> extends BaseActivity {
    protected P presenter;
    protected boolean isAfterRotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Tokopedia3);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.green_600));
        }

        setContentView(R.layout.drawer_activity);
        if (isAfterRotate(savedInstanceState)) {
            setupVar(savedInstanceState);
        } else {
            setupVar();
        }
        if (getIntent().getExtras() != null) {
            setupBundlePass(getIntent().getExtras());
        }
        if (getIntent().getData() != null) {
            setupURIPass(getIntent().getData());
        }
        initialPresenter();
        initVar();
        setViewListener();
        setActionVar();
    }

    protected void setupVar(Bundle savedInstanceState) { /*leave empty*/ }

    protected void setupVar() { /*leave empty*/ }

    protected boolean isAfterRotate(Bundle savedInstanceState) {
        return isAfterRotate = savedInstanceState != null;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * Kalau memang ada uri data dari intent, mau diapain?
     *
     * @param data uri data dari bundle intent
     */
    protected abstract void setupURIPass(Uri data);

    /**
     * Kalalu memang ada bundle data dari intent, mau diapain?
     *
     * @param extras bundle extras dari intent
     */
    protected abstract void setupBundlePass(Bundle extras);

    /**
     * Initial presenter, sesuai dengan Type param class
     */
    protected abstract void initialPresenter();

    /**
     * view / widgetnya mau diapain?
     */
    protected abstract void setViewListener();

    /**
     * initail variabel di activity
     */
    protected abstract void initVar();

    /**
     * variable nya mau diapain?
     */
    protected abstract void setActionVar();

}
