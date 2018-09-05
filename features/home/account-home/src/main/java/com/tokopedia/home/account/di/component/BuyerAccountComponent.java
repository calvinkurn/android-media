package com.tokopedia.home.account.di.component;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.home.account.di.module.BuyerAccountModule;
import com.tokopedia.home.account.di.scope.BuyerAccountScope;
import com.tokopedia.home.account.presentation.fragment.BuyerAccountFragment;

import dagger.Component;

/**
 * @author okasurya on 7/17/18.
 */
@Component(modules = {BuyerAccountModule.class}, dependencies = {BaseAppComponent.class})
@BuyerAccountScope
public interface BuyerAccountComponent {
    void inject(BuyerAccountFragment fragment);
}
