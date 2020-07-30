package com.tokopedia.product.manage.feature.campaignstock.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.config.GlobalConfig
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.campaignstock.ui.adapter.typefactory.CampaignStockAdapterTypeFactory
import com.tokopedia.product.manage.feature.campaignstock.ui.adapter.typefactory.CampaignStockTypeFactory
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.ReservedEventInfoUiModel
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.ReservedStockRedirectionUiModel
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.StockTickerInfoUiModel
import kotlinx.android.synthetic.main.fragment_campaign_stock_tab.*

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_campaign_stock_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
    }

    override fun getAdapterTypeFactory(): CampaignStockAdapterTypeFactory = CampaignStockAdapterTypeFactory(::onVariantAccordionStateChanged)

    override fun onItemClicked(t: Visitable<CampaignStockTypeFactory>?) {}

    override fun getScreenName(): String = ""

    override fun getRecyclerViewResourceId(): Int = R.id.rv_campaign_stock

    override fun initInjector() {

    }

    override fun loadData(page: Int) {}

    private fun setupView(view: View) {
        view.setBackgroundColor(Color.TRANSPARENT)
        if(reservedEventInfoList.isNotEmpty()) {
            setupAdapterModels(isVariant)
        } else {
            renderList(listOf())
        }
    }

    private fun setupAdapterModels(isVariant: Boolean) {
        if (GlobalConfig.isSellerApp()) {
            val eventSize = reservedEventInfoList.size
            val reservedStockList = mutableListOf<Visitable<CampaignStockTypeFactory>>(
                    StockTickerInfoUiModel(true)
            ).apply {
                addAll(reservedEventInfoList
                        .mapIndexed { index, model ->
                            model.apply {
                                this.isVariant = isVariant
                                if (index == eventSize - 1) {
                                    this.isLastEvent = true
                                }
                            }
                        })
            }
            renderList(reservedStockList)
        } else {
            renderList(listOf(ReservedStockRedirectionUiModel))
        }
    }

    private fun onVariantAccordionStateChanged(position: Int) {
        activity?.runOnUiThread {
            adapter.notifyItemChanged(position)
            rv_campaign_stock?.smoothScrollToPosition(position)
        }
    }
}