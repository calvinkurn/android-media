package com.tokopedia.digital_deals.di;


import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.digital_deals.di.scope.DealsScope;
import com.tokopedia.digital_deals.view.adapter.DealsCategoryAdapter;
import com.tokopedia.digital_deals.view.fragment.DealDetailsAllRedeemLocationsFragment;
import com.tokopedia.digital_deals.view.fragment.DealDetailsFragment;
import com.tokopedia.digital_deals.view.fragment.RevampCheckoutDealsFragment;
import com.tokopedia.digital_deals.view.fragment.RevampSelecDealsQuantityFragment;
import com.tokopedia.digital_deals.view.fragment.TncBottomSheetFragment;
import com.tokopedia.oms.di.OmsModule;

import dagger.Component;

@DealsScope
@Component(modules = {OmsModule.class, DealsModule.class, DealsVerifyViewModelModule.class},
        dependencies = BaseAppComponent.class)

public interface DealsComponent {

    void inject(DealDetailsFragment fragment);

    void inject(DealDetailsAllRedeemLocationsFragment fragment);

    void inject(RevampSelecDealsQuantityFragment fragment);

    void inject(RevampCheckoutDealsFragment fragment);

    void inject(DealsCategoryAdapter dealsCategoryAdapter);

    void inject(TncBottomSheetFragment fragment);
}
