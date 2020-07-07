package com.tokopedia.product.manage.feature.campaignstock.ui.fragment

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.product.manage.feature.campaignstock.ui.adapter.typefactory.CampaignStockAdapterTypeFactory

class CampaignMainStockFragment: BaseListFragment<Visitable<CampaignMainStockFragment>, CampaignStockAdapterTypeFactory>() {

    companion object {
        @JvmStatic
        fun createInstance(): CampaignMainStockFragment {
            return CampaignMainStockFragment().apply {

            }
        }
    }

    override fun getAdapterTypeFactory(): CampaignStockAdapterTypeFactory = CampaignStockAdapterTypeFactory()

    override fun onItemClicked(t: Visitable<CampaignMainStockFragment>?) {}

    override fun getScreenName(): String = ""

    override fun initInjector() {

    }

    override fun loadData(page: Int) {}
}