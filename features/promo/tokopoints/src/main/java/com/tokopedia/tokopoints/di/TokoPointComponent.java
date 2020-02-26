package com.tokopedia.tokopoints.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.tokopoints.view.fragment.AddPointsFragment;
import com.tokopedia.tokopoints.view.cataloglisting.CatalogListItemFragment;
import com.tokopedia.tokopoints.view.cataloglisting.CatalogListingFragment;
import com.tokopedia.tokopoints.view.fragment.TokoPointsHomeFragmentNew;
import com.tokopedia.tokopoints.view.fragment.ValidateMerchantPinFragment;

import dagger.Component;

@TokoPointScope
@Component(dependencies = BaseAppComponent.class )
public interface TokoPointComponent {

    void inject(TokoPointsHomeFragmentNew fragment);












    void inject(ValidateMerchantPinFragment fragment);





    void inject(AddPointsFragment fragment);
}
