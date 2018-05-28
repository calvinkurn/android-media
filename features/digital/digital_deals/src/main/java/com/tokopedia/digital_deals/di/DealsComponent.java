package com.tokopedia.digital_deals.di;


import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.digital_deals.di.scope.DealsScope;
import com.tokopedia.digital_deals.view.activity.BrandDetailsActivity;
import com.tokopedia.digital_deals.view.activity.DealDetailsActivity;
import com.tokopedia.digital_deals.view.activity.DealsHomeActivity;
import com.tokopedia.digital_deals.view.activity.DealsLocationActivity;
import com.tokopedia.digital_deals.view.activity.DealsSearchActivity;
import com.tokopedia.digital_deals.view.fragment.AllBrandsFragment;
import com.tokopedia.digital_deals.view.fragment.CategoryDetailHomeFragment;
import com.tokopedia.digital_deals.view.fragment.CheckoutHomeFragment;
import com.tokopedia.digital_deals.view.fragment.DealDetailsAllRedeemLocationsFragment;
import com.tokopedia.digital_deals.view.fragment.DealDetailsFragment;
import com.tokopedia.digital_deals.view.fragment.SelectDealQuantityFragment;

import dagger.Component;

@DealsScope
@Component(modules = DealsModule.class, dependencies = BaseAppComponent.class)

public interface DealsComponent {

    void inject(DealsHomeActivity activity);

    void inject(DealsSearchActivity activity);

    void inject(BrandDetailsActivity activity);

    void inject(DealDetailsActivity activity);

    void inject(DealsLocationActivity activity);

    void inject(DealDetailsFragment fragment);

    void inject(DealDetailsAllRedeemLocationsFragment fragment);

    void inject(CategoryDetailHomeFragment fragment);

    void inject(AllBrandsFragment fragment);

    void inject(SelectDealQuantityFragment fragment);

    void inject(CheckoutHomeFragment fragment);
}
