package com.tokopedia.loyalty.di.component;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.loyalty.di.LoyaltyScope;
import com.tokopedia.loyalty.di.module.PromoCodeViewModule;
import com.tokopedia.loyalty.view.fragment.PromoCodeFragment;

import dagger.Component;

/**
 * @author anggaprasetiyo on 27/11/17.
 */
@LoyaltyScope
@Component(modules = PromoCodeViewModule.class, dependencies = BaseAppComponent.class)
public interface PromoCodeComponent {
    void inject(PromoCodeFragment promoCodeFragment);
}
