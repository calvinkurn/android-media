package com.tokopedia.loyalty.di.component;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.loyalty.di.PromoFragmentScope;
import com.tokopedia.loyalty.di.module.PromoListFragmentModule;
import com.tokopedia.loyalty.view.fragment.PromoListFragment;

import dagger.Component;

/**
 * @author anggaprasetiyo on 03/01/18.
 */
@PromoFragmentScope
@Component(modules = PromoListFragmentModule.class, dependencies = BaseAppComponent.class)
public interface PromoListFragmentComponent {

    void inject(PromoListFragment promoListFragment);
}
