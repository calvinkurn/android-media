package com.tokopedia.core.app;

import android.app.Service;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.module.ActivityModule;

/**
 * @author sebastianuskh on 4/20/17.
 */

public abstract class BaseService extends Service{


    protected AppComponent getApplicationComponent() {
        return ((MainApplication) getApplication())
                .getApplicationComponent();
    }

    protected ActivityModule getServiceModule() {
        return new ActivityModule(this);
    }
}
