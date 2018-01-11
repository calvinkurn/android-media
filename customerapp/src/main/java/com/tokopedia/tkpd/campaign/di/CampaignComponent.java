package com.tokopedia.tkpd.campaign.di;


import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.tkpd.campaign.di.scope.CampaignModuleScope;
import com.tokopedia.tkpd.campaign.view.BarcodeCampaignActivity;

import javax.inject.Scope;

import dagger.Component;

/**
 * Created by sandeepgoyal on 15/12/17.
 */
@CampaignModuleScope
@Component(modules = CampaignModule.class, dependencies = AppComponent.class)
public interface CampaignComponent {
    public void inject(BarcodeCampaignActivity activity);
}
