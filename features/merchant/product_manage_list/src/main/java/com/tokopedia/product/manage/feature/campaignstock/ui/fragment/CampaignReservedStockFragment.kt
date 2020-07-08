package com.tokopedia.product.manage.feature.campaignstock.ui.fragment

import android.os.Bundle
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.product.manage.feature.campaignstock.ui.adapter.typefactory.CampaignStockAdapterTypeFactory
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.ReservedEventInfoModel

class CampaignReservedStockFragment: BaseListFragment<Visitable<CampaignMainStockFragment>, CampaignStockAdapterTypeFactory>() {

    companion object {
        @JvmStatic
        fun createInstance(isVariant: Boolean,
                           reservedEventInfoList: ArrayList<ReservedEventInfoModel>): CampaignReservedStockFragment =
                CampaignReservedStockFragment().apply {
                    arguments = Bundle().apply {
                        putBoolean(EXTRA_IS_VARIANT, isVariant)
                        putParcelableArrayList(EXTRA_RESERVED_EVENT_INFO_LIST, reservedEventInfoList)
                    }
                }

        private const val EXTRA_IS_VARIANT = "extra_is_variant"
        private const val EXTRA_RESERVED_EVENT_INFO_LIST = "extra_reserved_event_info"
    }

    override fun getAdapterTypeFactory(): CampaignStockAdapterTypeFactory = CampaignStockAdapterTypeFactory()

    override fun onItemClicked(t: Visitable<CampaignMainStockFragment>?) {}

    override fun getScreenName(): String = ""

    override fun initInjector() {

    }

    override fun loadData(page: Int) {}
}