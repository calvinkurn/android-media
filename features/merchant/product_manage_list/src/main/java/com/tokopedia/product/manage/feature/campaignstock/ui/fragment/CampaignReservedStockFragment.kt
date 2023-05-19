package com.tokopedia.product.manage.feature.campaignstock.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.product.manage.ProductManageInstance
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccess
import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageTicker.*
import com.tokopedia.product.manage.common.feature.list.view.mapper.ProductManageTickerMapper.mapToTickerData
import com.tokopedia.product.manage.common.util.ProductManageConfig
import com.tokopedia.product.manage.databinding.FragmentCampaignStockTabBinding
import com.tokopedia.product.manage.feature.campaignstock.di.DaggerCampaignStockComponent
import com.tokopedia.product.manage.feature.campaignstock.ui.adapter.typefactory.CampaignStockAdapterTypeFactory
import com.tokopedia.product.manage.feature.campaignstock.ui.adapter.typefactory.CampaignStockTypeFactory
import com.tokopedia.product.manage.feature.campaignstock.ui.bottomsheet.VariantReservedEventInfoBottomSheet
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.CampaignStockTickerUiModel
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.ReservedEventInfoUiModel
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.ReservedStockRedirectionUiModel
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.VariantReservedEventInfoUiModel
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class CampaignReservedStockFragment: BaseListFragment<Visitable<CampaignStockTypeFactory>, CampaignStockAdapterTypeFactory>() {

    companion object {
        @JvmStatic
        fun createNonVariantInstance(
            reservedEventInfoUiList: ArrayList<ReservedEventInfoUiModel>,
            access: ProductManageAccess
        ): CampaignReservedStockFragment =
            CampaignReservedStockFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(EXTRA_IS_VARIANT, false)
                    putParcelableArrayList(EXTRA_NONVARIANT_RESERVED_EVENT_INFO_LIST, reservedEventInfoUiList)
                    putParcelable(EXTRA_PRODUCT_MANAGE_ACCESS, access)
                }
            }

        @JvmStatic
        fun createVariantInstance(
            variantReservedInfoUiList: ArrayList<VariantReservedEventInfoUiModel>,
            access: ProductManageAccess
        ): CampaignReservedStockFragment =
            CampaignReservedStockFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(EXTRA_IS_VARIANT, true)
                    putParcelableArrayList(EXTRA_VARIANT_RESERVED_EVENT_INFO_LIST, variantReservedInfoUiList)
                    putParcelable(EXTRA_PRODUCT_MANAGE_ACCESS, access)
                }
            }

        private const val EXTRA_IS_VARIANT = "extra_is_variant"
        private const val EXTRA_NONVARIANT_RESERVED_EVENT_INFO_LIST = "extra_reserved_event_info"
        private const val EXTRA_VARIANT_RESERVED_EVENT_INFO_LIST = "extra_variant_reserved_event_info"
        private const val EXTRA_PRODUCT_MANAGE_ACCESS = "extra_product_manage_access"
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    private val isVariant by lazy {
        arguments?.getBoolean(EXTRA_IS_VARIANT) ?: false
    }

    private val reservedEventInfoList by lazy {
        arguments?.getParcelableArrayList<ReservedEventInfoUiModel>(EXTRA_NONVARIANT_RESERVED_EVENT_INFO_LIST)?.toList().orEmpty()
    }

    private val variantReservedEventInfoList by lazy {
        arguments?.getParcelableArrayList<ReservedEventInfoUiModel>(EXTRA_VARIANT_RESERVED_EVENT_INFO_LIST)?.toList().orEmpty()
    }
    private var binding by autoClearedNullable<FragmentCampaignStockTabBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCampaignStockTabBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
    }

    override fun getAdapterTypeFactory(): CampaignStockAdapterTypeFactory =
        CampaignStockAdapterTypeFactory(
            onVariantReservedEventInfoClicked = ::showVariantReservedEventInfoBottomSheet
        )

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
        setupAdapterModels(isVariant)
    }

    private fun setupAdapterModels(isVariant: Boolean) {
        if (ProductManageConfig.IS_SELLER_APP) {
            val tickerUiModel = createTickerUiModel()
            val adapterList = when {
                isVariant && variantReservedEventInfoList.isNotEmpty() -> {
                    mutableListOf<Visitable<CampaignStockTypeFactory>>().apply {
                        add(tickerUiModel)
                        addAll(variantReservedEventInfoList)
                    }
                }
                !isVariant && reservedEventInfoList.isNotEmpty() -> {
                    mutableListOf<Visitable<CampaignStockTypeFactory>>().apply {
                        add(tickerUiModel)
                        addAll(reservedEventInfoList)
                    }
                }
                else -> listOf()
            }
            renderList(adapterList)
        } else {
            renderList(listOf(ReservedStockRedirectionUiModel))
        }
    }

    private fun createTickerUiModel(): CampaignStockTickerUiModel {
        val tickerList = listOf(CampaignStockTicker)
        val tickerData = mapToTickerData(context, tickerList)
        return CampaignStockTickerUiModel(tickerData)
    }

    private fun showVariantReservedEventInfoBottomSheet(
        variantName: String,
        reservedEventInfos: MutableList<ReservedEventInfoUiModel>
    ) {
        if (!isAdded) return
        context?.let {
            VariantReservedEventInfoBottomSheet.createInstance(
                it,
                variantName,
                ArrayList(reservedEventInfos)
            ).show(childFragmentManager)
        }
    }

}
