package com.tokopedia.abstraction.base.view.activity;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;

/**
 * @author by nisie on 13/02/18.
 */

public abstract class BaseEmptyActivity extends BaseActivity {

    @LayoutRes
    protected abstract int getLayoutRes();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupLayout(savedInstanceState);
    }

    @CallSuper
    protected void setupLayout(Bundle savedInstanceState) {
        setContentView(getLayoutRes());
    }
}
