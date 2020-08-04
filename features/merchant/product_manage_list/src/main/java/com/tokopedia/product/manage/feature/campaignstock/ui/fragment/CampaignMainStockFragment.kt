package com.tokopedia.product.manage.feature.campaignstock.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.campaignstock.ui.adapter.typefactory.CampaignStockAdapterTypeFactory
import com.tokopedia.product.manage.feature.campaignstock.ui.adapter.typefactory.CampaignStockTypeFactory
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.ActiveProductSwitchUiModel
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.SellableStockProductUIModel
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.StockTickerInfoUiModel
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.TotalStockEditorUiModel
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus

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

    private var campaignStockListener: CampaignStockListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_campaign_stock_tab, container, false)
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

    override fun initInjector() {}

    override fun loadData(page: Int) {}

    override fun getRecyclerViewResourceId(): Int = R.id.rv_campaign_stock

    private fun setupView(view: View) {
        view.setBackgroundColor(Color.TRANSPARENT)
        setupAdapterModels(isVariant)
    }

    private fun setupAdapterModels(isVariant: Boolean) {
        if (isVariant) {
            val variantList = mutableListOf<Visitable<CampaignStockTypeFactory>>(
                    StockTickerInfoUiModel(false)).apply {
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

    private fun onTotalStockChanged(totalStock: Int) {
        campaignStockListener?.onTotalStockChanged(totalStock)
    }

    private fun onActiveStockChanged(isActive: Boolean) {
        campaignStockListener?.onActiveStockChanged(isActive)
    }

    private fun onVariantStockChanged(productId: String, stock: Int) {
        campaignStockListener?.onVariantStockChanged(productId, stock)
    }

    private fun onVariantStatusChanged(productId: String, status: ProductStatus) {
        campaignStockListener?.onVariantStatusChanged(productId, status)
    }

}