package com.tokopedia.campaign.shake.landing.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.campaign.shake.landing.di.scope.CampaignModuleScope;
import com.tokopedia.campaign.shake.landing.view.activity.ShakeDetectCampaignActivity;

import dagger.Component;

/**
 * Created by sandeepgoyal on 15/12/17.
 */
@CampaignModuleScope
@Component(modules = {CampaignModule.class}, dependencies = {BaseAppComponent.class})
public interface CampaignComponent {

    void inject(ShakeDetectCampaignActivity activity);
}