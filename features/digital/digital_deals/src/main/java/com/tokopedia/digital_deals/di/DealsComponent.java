package com.tokopedia.digital_deals.di;


import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.digital_deals.di.scope.DealsScope;
import com.tokopedia.digital_deals.view.activity.DealsSearchActivity;
import com.tokopedia.digital_deals.view.adapter.DealsCategoryAdapter;
import com.tokopedia.digital_deals.view.fragment.AllBrandsFragment;
import com.tokopedia.digital_deals.view.fragment.BrandDetailsFragment;
import com.tokopedia.digital_deals.view.fragment.CategoryDetailHomeFragment;
import com.tokopedia.digital_deals.view.fragment.CheckoutHomeFragment;
import com.tokopedia.digital_deals.view.fragment.DealDetailsAllRedeemLocationsFragment;
import com.tokopedia.digital_deals.view.fragment.DealDetailsFragment;
import com.tokopedia.digital_deals.view.fragment.DealsHomeFragment;
import com.tokopedia.digital_deals.view.fragment.SelectDealQuantityFragment;
import com.tokopedia.digital_deals.view.fragment.TncBottomSheetFragment;
import com.tokopedia.oms.di.OmsModule;
import com.tokopedia.oms.domain.postusecase.PostVerifyCartUseCase;

import dagger.Component;

@DealsScope
@Component(modules = OmsModule.class, dependencies = BaseAppComponent.class)

public interface DealsComponent {

    PostVerifyCartUseCase getPostVerifyCartUseCase();

    void inject(DealsSearchActivity activity);

    void inject(DealDetailsFragment fragment);

    void inject(DealDetailsAllRedeemLocationsFragment fragment);

    void inject(CategoryDetailHomeFragment fragment);

    void inject(AllBrandsFragment fragment);

    void inject(SelectDealQuantityFragment fragment);

    void inject(CheckoutHomeFragment fragment);

    void inject(DealsCategoryAdapter dealsCategoryAdapter);

    void inject(BrandDetailsFragment fragment);

    void inject(DealsHomeFragment dealsHomeFragment);

    void inject(TncBottomSheetFragment fragment);
}
