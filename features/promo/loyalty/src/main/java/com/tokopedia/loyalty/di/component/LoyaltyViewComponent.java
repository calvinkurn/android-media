package com.tokopedia.loyalty.di.component;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.loyalty.di.LoyaltyScope;
import com.tokopedia.loyalty.di.module.LoyaltyViewModule;
import com.tokopedia.loyalty.view.activity.LoyaltyActivity;

import dagger.Component;

/**
 * @author anggaprasetiyo on 30/11/17.
 */
@LoyaltyScope
@Component(modules = LoyaltyViewModule.class, dependencies = BaseAppComponent.class)
public interface LoyaltyViewComponent {

    void inject(LoyaltyActivity loyaltyActivity);
}
