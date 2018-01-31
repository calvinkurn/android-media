package com.tokopedia.tkpd.campaign.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.tkpd.campaign.di.scope.CampaignModuleScope;
import com.tokopedia.tkpd.qrscanner.QrScannerActivity;

import dagger.Component;

/**
 * Created by sandeepgoyal on 15/12/17.
 */
@CampaignModuleScope
@Component(modules = CampaignModule.class, dependencies = BaseAppComponent.class)
public interface CampaignComponent {
    void inject(QrScannerActivity activity);
}
