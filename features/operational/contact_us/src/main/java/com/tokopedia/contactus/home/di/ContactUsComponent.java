package com.tokopedia.contactus.home.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.contactus.common.di.ContactUsModule;
import com.tokopedia.contactus.common.di.scope.ContactUsModuleScope;
import com.tokopedia.contactus.home.view.fragment.BuyerPurchaseFragment;
import com.tokopedia.contactus.home.view.fragment.ContactUsHomeFragment;
import com.tokopedia.contactus.home.view.fragment.SellerPurchaseFragment;

import dagger.Component;

/**
 * Created by sandeepgoyal on 15/12/17.
 */
@ContactUsModuleScope
@Component(modules = {ContactUsModule.class,ContactUsHomeModule.class}, dependencies = BaseAppComponent.class)
public interface ContactUsComponent {
    void inject(ContactUsHomeFragment contactUsHomeFragment);
    void inject(BuyerPurchaseFragment buyerPurchaseFragment);
    void inject(SellerPurchaseFragment buyerPurchaseFragment);
}