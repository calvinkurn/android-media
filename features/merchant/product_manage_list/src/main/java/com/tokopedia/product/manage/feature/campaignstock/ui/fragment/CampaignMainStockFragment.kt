package com.tokopedia.product.manage.feature.campaignstock.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.manage.ProductManageInstance
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.campaignstock.di.DaggerCampaignStockComponent
import com.tokopedia.product.manage.feature.campaignstock.ui.adapter.typefactory.CampaignStockAdapterTypeFactory
import com.tokopedia.product.manage.feature.campaignstock.ui.adapter.typefactory.CampaignStockTypeFactory
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.ActiveProductSwitchUiModel
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.SellableStockProductUIModel
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.StockTickerInfoUiModel
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.TotalStockEditorUiModel
import com.tokopedia.product.manage.feature.campaignstock.ui.viewmodel.CampaignMainStockViewModel
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import javax.inject.Inject

class CampaignMainStockFragment: BaseListFragment<Visitable<CampaignStockTypeFactory>, CampaignStockAdapterTypeFactory>() {

    companion object {
        @JvmStatic
        fun createInstance(isVariant: Boolean,
                           sellableProductUIList: ArrayList<SellableStockProductUIModel>,
                           isActive: Boolean,
                           stock: Int,
                           campaignStockListener: CampaignStockListener): CampaignMainStockFragment {
            return CampaignMainStockFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(EXTRA_IS_VARIANT, isVariant)
                    putBoolean(EXTRA_IS_ACTIVE, isActive)
                    putInt(EXTRA_STOCK, stock)
                    putParcelableArrayList(EXTRA_SELLABLE_PRODUCT_LIST, sellableProductUIList)
                }
                this.campaignStockListener = campaignStockListener
            }
        }

        private const val EXTRA_STOCK = "extra_stock"
        private const val EXTRA_IS_VARIANT = "extra_is_variant"
        private const val EXTRA_IS_ACTIVE = "extra_is_active"
        private const val EXTRA_SELLABLE_PRODUCT_LIST = "extra_sellable"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val mViewModel: CampaignMainStockViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(CampaignMainStockViewModel::class.java)
    }

    private val isVariant by lazy {
        arguments?.getBoolean(EXTRA_IS_VARIANT) ?: false
    }

    private val isActive by lazy {
        arguments?.getBoolean(EXTRA_IS_ACTIVE) ?: false
    }

    private val stockCount by lazy {
        arguments?.getInt(EXTRA_STOCK)
    }

    private val sellableProductList by lazy {
        arguments?.getParcelableArrayList<SellableStockProductUIModel>(EXTRA_SELLABLE_PRODUCT_LIST)?.toList().orEmpty()
    }
    
    private val variantStockWarningTicker by lazy {
        StockTickerInfoUiModel(false, sellableProductList.any { it.stock.toIntOrZero() == 0 })
    }

    private var campaignStockListener: CampaignStockListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_campaign_stock_tab, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeVariantStock()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
    }

    override fun getAdapterTypeFactory(): CampaignStockAdapterTypeFactory = CampaignStockAdapterTypeFactory(
            onTotalStockChanged = ::onTotalStockChanged,
            onActiveStockChanged = ::onActiveStockChanged,
            onVariantStockChanged = ::onVariantStockChanged,
            onVariantStatusChanged = ::onVariantStatusChanged
    )

    override fun onItemClicked(t: Visitable<CampaignStockTypeFactory>?) {}

    override fun getScreenName(): String = ""

    override fun initInjector() {
        activity?.run {
            DaggerCampaignStockComponent.builder()
                    .productManageComponent(ProductManageInstance.getComponent(application))
                    .build()
                    .inject(this@CampaignMainStockFragment)
        }
    }

    override fun loadData(page: Int) {}

    override fun getRecyclerViewResourceId(): Int = R.id.rv_campaign_stock

    override fun isLoadMoreEnabledByDefault(): Boolean = false

    override fun onDestroyView() {
        mViewModel.shouldDisplayVariantStockWarningLiveData.removeObservers(viewLifecycleOwner)
        super.onDestroyView()
    }

    private fun setupView(view: View) {
        view.setBackgroundColor(Color.TRANSPARENT)
        setupAdapterModels(isVariant)
    }

    private fun setupAdapterModels(isVariant: Boolean) {
        if (isVariant) {
            val variantList = mutableListOf<Visitable<CampaignStockTypeFactory>>(variantStockWarningTicker).apply {
                addAll(sellableProductList)
            }
            renderList(variantList)
        } else {
            renderList(listOf(
                    ActiveProductSwitchUiModel(isActive),
                    TotalStockEditorUiModel(stockCount.orZero())
            ))
        }
    }

    private fun observeVariantStock() {
        mViewModel.shouldDisplayVariantStockWarningLiveData.observe(viewLifecycleOwner, Observer {
            showVariantWarningTickerWithCondition(it)
        })
    }

    private fun onTotalStockChanged(totalStock: Int) {
        campaignStockListener?.onTotalStockChanged(totalStock)
    }

    private fun onActiveStockChanged(isActive: Boolean) {
        campaignStockListener?.onActiveStockChanged(isActive)
    }

    private fun onVariantStockChanged(productId: String, stock: Int) {
        mViewModel.setVariantStock(productId, stock)
        campaignStockListener?.onVariantStockChanged(productId, stock)
    }

    private fun onVariantStatusChanged(productId: String, status: ProductStatus) {
        campaignStockListener?.onVariantStatusChanged(productId, status)
    }

    private fun showVariantWarningTickerWithCondition(shouldShowWarning: Boolean) {
        adapter.data.indexOf(variantStockWarningTicker).let { warningIndex ->
            (adapter.data.getOrNull(warningIndex) as? StockTickerInfoUiModel)?.run {
                if (hasEmptyVariantStock != shouldShowWarning) {
                    hasEmptyVariantStock = shouldShowWarning
                    adapter.notifyItemChanged(warningIndex)
                }
            }
        }
    }

}