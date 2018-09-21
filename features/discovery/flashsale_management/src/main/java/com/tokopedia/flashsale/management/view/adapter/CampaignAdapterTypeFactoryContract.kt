package com.tokopedia.flashsale.management.view.adapter
import com.tokopedia.flashsale.management.view.viewmodel.CampaignViewModel

interface CampaignAdapterTypeFactoryContract {
    fun type(viewModel: CampaignViewModel): Int
}