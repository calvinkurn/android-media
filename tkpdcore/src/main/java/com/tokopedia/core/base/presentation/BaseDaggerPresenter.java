package com.tokopedia.core.base.presentation;

import android.content.Context;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;

/**
 * @author kulomady on 11/20/16.
 */

public class BaseDaggerPresenter<T extends CustomerView> extends com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter<T> {
    public AppComponent getComponent(Context context) {
        return ((MainApplication) context).getAppComponent();
    }
}