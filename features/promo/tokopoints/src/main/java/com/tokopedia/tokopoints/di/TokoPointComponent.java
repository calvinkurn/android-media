package com.tokopedia.tokopoints.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.tokopoints.view.addPoint.AddPointsFragment;
import com.tokopedia.tokopoints.view.fragment.ValidateMerchantPinFragment;

import dagger.Component;

@TokoPointScope
@Component(dependencies = BaseAppComponent.class )
public interface TokoPointComponent {














    void inject(ValidateMerchantPinFragment fragment);






}
