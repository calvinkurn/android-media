package com.tokopedia.digital_deals.di;

import android.app.Application;

import com.tokopedia.abstraction.base.app.BaseMainApplication;


public class DealsComponentInstance {
    private static DealsComponent dealsComponent;

    public static DealsComponent getDealsComponent(Application application) {
        if (dealsComponent == null) {
            dealsComponent = DaggerDealsComponent.builder().baseAppComponent(
                    ((BaseMainApplication)application).getBaseAppComponent()).dealsModule(new DealsModule(application.getBaseContext())).build();
        }
        return dealsComponent;
    }
}
