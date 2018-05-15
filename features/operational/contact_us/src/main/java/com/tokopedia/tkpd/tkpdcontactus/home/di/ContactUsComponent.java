package com.tokopedia.tkpd.tkpdcontactus.home.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.tkpd.tkpdcontactus.common.di.ContactUsModule;
import com.tokopedia.tkpd.tkpdcontactus.common.di.scope.ContactUsModuleScope;
import com.tokopedia.tkpd.tkpdcontactus.home.view.fragment.BuyerPurchaseFragment;
import com.tokopedia.tkpd.tkpdcontactus.home.view.fragment.ContactUsHomeFragment;
import com.tokopedia.tkpd.tkpdcontactus.home.view.fragment.SellerPurchaseFragment;
import com.tokopedia.tkpd.tkpdcontactus.home.view.presenter.SellerPurchasePresenter;

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