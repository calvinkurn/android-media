package com.tokopedia.logisticcommon.base;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.logisticcommon.R;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.component.HasComponent;

public abstract class BaseSimpleLogisticActivity extends BaseSimpleActivity implements HasComponent<BaseAppComponent> {

    protected boolean isAfterRotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        initInjector();
        super.onCreate(savedInstanceState);
        initView();
        initVar();
        setViewListener();
        setActionVar();
    }

    protected Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.parent_view);
    }

    protected abstract void initInjector();

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
     * initial wiew atau widget
     */
    protected abstract void initView();

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

    @Override
    public BaseAppComponent getComponent() {
        return ((BaseMainApplication) getApplication()).getBaseAppComponent();
    }
}