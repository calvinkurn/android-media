package com.tokopedia.core.app;

import android.app.Service;

import com.tokopedia.core.base.di.component.AppComponent;

/**
 * @author sebastianuskh on 4/20/17.
 */

public abstract class BaseService extends Service{


    protected AppComponent getApplicationComponent() {
        return ((MainApplication) getApplication())
                .getApplicationComponent();
    }
}
