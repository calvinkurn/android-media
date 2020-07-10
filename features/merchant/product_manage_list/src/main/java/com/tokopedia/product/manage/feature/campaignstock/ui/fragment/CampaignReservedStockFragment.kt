package com.tokopedia.product.manage.feature.campaignstock.ui.fragment

import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.product.manage.feature.campaignstock.ui.adapter.typefactory.CampaignStockAdapterTypeFactory
import com.tokopedia.product.manage.feature.campaignstock.ui.adapter.typefactory.CampaignStockTypeFactory
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.ReservedEventInfoUiModel

class CampaignReservedStockFragment: BaseListFragment<Visitable<CampaignStockTypeFactory>, CampaignStockAdapterTypeFactory>() {

    companion object {
        @JvmStatic
        fun createInstance(isVariant: Boolean,
                           reservedEventInfoUiList: ArrayList<ReservedEventInfoUiModel>): CampaignReservedStockFragment =
                CampaignReservedStockFragment().apply {
                    arguments = Bundle().apply {
                        putBoolean(EXTRA_IS_VARIANT, isVariant)
                        putParcelableArrayList(EXTRA_RESERVED_EVENT_INFO_LIST, reservedEventInfoUiList)
                    }
                }

        private const val EXTRA_IS_VARIANT = "extra_is_variant"
        private const val EXTRA_RESERVED_EVENT_INFO_LIST = "extra_reserved_event_info"
    }

    private val isVariant by lazy {
        arguments?.getBoolean(EXTRA_IS_VARIANT) ?: false
    }

    private val reservedEventInfoList by lazy {
        arguments?.getParcelableArrayList<ReservedEventInfoUiModel>(EXTRA_RESERVED_EVENT_INFO_LIST)?.toList().orEmpty()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun getAdapterTypeFactory(): CampaignStockAdapterTypeFactory = CampaignStockAdapterTypeFactory()

    override fun onItemClicked(t: Visitable<CampaignStockTypeFactory>?) {}

    override fun getScreenName(): String = ""

    override fun initInjector() {

    }

    override fun loadData(page: Int) {}

    private fun setupView() {
        if(reservedEventInfoList.isNotEmpty()) {
            setupAdapterModels(isVariant)
        }
    }

    private fun setupAdapterModels(isVariant: Boolean) {
        renderList(reservedEventInfoList)
    }
}