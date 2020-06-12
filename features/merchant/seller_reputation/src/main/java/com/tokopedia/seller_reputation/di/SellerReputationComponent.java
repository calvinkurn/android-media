package com.tokopedia.seller_reputation.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller_reputation.view.fragment.SellerReputationFragment;

import dagger.Component;

/**
 * Created by normansyahputa on 2/13/18.
 */
@SellerReputationScope
@Component(modules = SellerReputationModule.class, dependencies = AppComponent.class)
public interface SellerReputationComponent {

    void inject(SellerReputationFragment sellerReputationFragment);

}