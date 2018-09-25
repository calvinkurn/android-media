package com.tokopedia.flashsale.management.view.fragment

class MyCampaignFragment : BaseCampaignFragment(){

    override fun loadData(page: Int) {
        super.loadData(page)
    }

    companion object {
        fun createInstance() = MyCampaignFragment()
    }
}