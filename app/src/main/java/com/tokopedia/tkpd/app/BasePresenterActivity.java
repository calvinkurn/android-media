package com.tokopedia.tkpd.app;

import android.net.Uri;
import android.os.Bundle;

import butterknife.ButterKnife;

/**
 * Created by Angga.Prasetiyo on 09/11/2015.
 */
public abstract class BasePresenterActivity<P> extends TActivity {
    private static final String TAG = BasePresenterActivity.class.getSimpleName();

    protected P presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras() != null) {
            setupBundlePass(getIntent().getExtras());
        }
        if (getIntent().getData() != null) {
            setupURIPass(getIntent().getData());
        }
        initialPresenter();
        inflateView(getLayoutId());
        ButterKnife.bind(this);
        initView();
        initVar();
        setViewListener();
        setActionVar();
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
     * Layout id untuk si activity
     *
     * @return Res layout id
     */
    protected abstract int getLayoutId();

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

}
