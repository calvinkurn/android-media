package com.tokopedia.flashsale.management.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flashsale.management.view.adapter.CampaignAdapterTypeFactory

class CampaignViewModel : Visitable<CampaignAdapterTypeFactory> {

    override fun type(typeFactory: CampaignAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}