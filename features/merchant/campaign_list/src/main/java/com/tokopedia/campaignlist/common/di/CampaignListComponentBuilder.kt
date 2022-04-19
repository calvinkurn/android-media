package com.tokopedia.campaignlist.common.di

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication

class CampaignListComponentBuilder {
    companion object {
        private var campaignListComponent: CampaignListComponent? = null

        fun getComponent(application: Application): CampaignListComponent {
            return campaignListComponent?.run { campaignListComponent }
                    ?: DaggerCampaignListComponent.builder().baseAppComponent((application as BaseMainApplication).baseAppComponent).build()
        }
    }
}