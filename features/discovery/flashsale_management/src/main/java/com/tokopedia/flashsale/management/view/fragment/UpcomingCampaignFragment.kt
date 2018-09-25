package com.tokopedia.flashsale.management.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.view.adapter.CampaignAdapterTypeFactory
import com.tokopedia.flashsale.management.view.viewmodel.CampaignViewModel

class UpcomingCampaignFragment : BaseCampaignFragment(){

    override fun loadData(page: Int) {
        super.loadData(page)
    }

    companion object {
        fun createInstance() = UpcomingCampaignFragment()
    }
}