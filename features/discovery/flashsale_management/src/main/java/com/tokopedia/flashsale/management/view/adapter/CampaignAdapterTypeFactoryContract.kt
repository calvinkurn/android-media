package com.tokopedia.flashsale.management.view.adapter
import com.tokopedia.flashsale.management.view.viewmodel.CampaignStatusListViewModel
import com.tokopedia.flashsale.management.view.viewmodel.CampaignViewModel
import com.tokopedia.flashsale.management.view.viewmodel.EmptyMyCampaignViewModel

interface CampaignAdapterTypeFactoryContract {
    fun type(viewModel: CampaignViewModel): Int
    fun type(viewModel: EmptyMyCampaignViewModel): Int
}