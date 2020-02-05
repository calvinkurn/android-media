package com.tokopedia.digital.product.additionalfeature.etoll.di;


import com.tokopedia.digital.common.di.DigitalComponent;
import com.tokopedia.digital.product.additionalfeature.etoll.view.activity.DigitalCheckETollBalanceNFCActivity;

import dagger.Component;

@DigitalETollScope
@Component(modules = DigitalETollModule.class, dependencies = DigitalComponent.class)
public interface DigitalETollComponent {

    void inject(DigitalCheckETollBalanceNFCActivity activity);
}
