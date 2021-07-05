package com.tokopedia.product.manage.feature.campaignstock.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.config.GlobalConfig
import com.tokopedia.product.manage.ProductManageInstance
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccess
import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageTicker.*
import com.tokopedia.product.manage.common.feature.list.view.mapper.ProductManageTickerMapper.mapToTickerData
import com.tokopedia.product.manage.feature.campaignstock.di.DaggerCampaignStockComponent
import com.tokopedia.product.manage.feature.campaignstock.ui.adapter.typefactory.CampaignStockAdapterTypeFactory
import com.tokopedia.product.manage.feature.campaignstock.ui.adapter.typefactory.CampaignStockTypeFactory
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.CampaignStockTickerUiModel
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.ReservedEventInfoUiModel
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.ReservedStockRedirectionUiModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_campaign_stock_tab.*
import javax.inject.Inject

class CampaignReservedStockFragment: BaseListFragment<Visitable<CampaignStockTypeFactory>, CampaignStockAdapterTypeFactory>() {

    companion object {
        @JvmStatic
        fun createInstance(
            isVariant: Boolean,
            reservedEventInfoUiList: ArrayList<ReservedEventInfoUiModel>,
            access: ProductManageAccess
        ): CampaignReservedStockFragment =
            CampaignReservedStockFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(EXTRA_IS_VARIANT, isVariant)
                    putParcelableArrayList(EXTRA_RESERVED_EVENT_INFO_LIST, reservedEventInfoUiList)
                    putParcelable(EXTRA_PRODUCT_MANAGE_ACCESS, access)
                }
            }

        private const val EXTRA_IS_VARIANT = "extra_is_variant"
        private const val EXTRA_RESERVED_EVENT_INFO_LIST = "extra_reserved_event_info"
        private const val EXTRA_PRODUCT_MANAGE_ACCESS = "extra_product_manage_access"
    }

    @Inject
    lateinit var userSession: UserSessionInterface

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
        activity?.run {
            DaggerCampaignStockComponent.builder()
                .productManageComponent(ProductManageInstance.getComponent(application))
                .build()
                .inject(this@CampaignReservedStockFragment)
        }
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
            val tickerUiModel = createTickerUiModel()
            val eventSize = reservedEventInfoList.size
            val reservedStockList = mutableListOf<Visitable<CampaignStockTypeFactory>>().apply {
                add(tickerUiModel)
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

    private fun createTickerUiModel(): CampaignStockTickerUiModel {
        val tickerList = listOf(CampaignStockTicker)
        val tickerData = mapToTickerData(context, tickerList)
        return CampaignStockTickerUiModel(tickerData)
    }

    private fun onVariantAccordionStateChanged(position: Int) {
        activity?.runOnUiThread {
            adapter.notifyItemChanged(position)
            rv_campaign_stock?.smoothScrollToPosition(position)
        }
    }
}