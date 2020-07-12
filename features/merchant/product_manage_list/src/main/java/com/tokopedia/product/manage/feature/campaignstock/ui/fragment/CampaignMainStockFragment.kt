package com.tokopedia.product.manage.feature.campaignstock.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.manage.ProductManageInstance
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.campaignstock.di.DaggerCampaignStockComponent
import com.tokopedia.product.manage.feature.campaignstock.ui.adapter.typefactory.CampaignStockAdapterTypeFactory
import com.tokopedia.product.manage.feature.campaignstock.ui.adapter.typefactory.CampaignStockTypeFactory
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.ActiveProductSwitchUiModel
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.SellableStockProductUIModel
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.TotalStockEditorUiModel

class CampaignMainStockFragment: BaseListFragment<Visitable<CampaignStockTypeFactory>, CampaignStockAdapterTypeFactory>() {

    companion object {
        @JvmStatic
        fun createInstance(isVariant: Boolean,
                           sellableProductUIList: ArrayList<SellableStockProductUIModel>): CampaignMainStockFragment {
            return CampaignMainStockFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(EXTRA_IS_VARIANT, isVariant)
                    putParcelableArrayList(EXTRA_SELLABLE_PRODUCT_LIST, sellableProductUIList)
                }
            }
        }

        private const val EXTRA_IS_VARIANT = "extra_is_variant"
        private const val EXTRA_SELLABLE_PRODUCT_LIST = "extra_sellable"
    }

    private val isVariant by lazy {
        arguments?.getBoolean(EXTRA_IS_VARIANT) ?: false
    }

    private val sellableProductList by lazy {
        arguments?.getParcelableArrayList<SellableStockProductUIModel>(EXTRA_SELLABLE_PRODUCT_LIST)?.toList().orEmpty()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeLiveData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_campaign_stock_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
    }

    override fun getAdapterTypeFactory(): CampaignStockAdapterTypeFactory = CampaignStockAdapterTypeFactory()

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

    private fun observeLiveData() {

    }

    private fun setupView(view: View) {
        view.setBackgroundColor(Color.TRANSPARENT)
        if (sellableProductList.isNotEmpty()) {
            setupAdapterModels(isVariant)
        }
    }

    private fun setupAdapterModels(isVariant: Boolean) {
        if (isVariant) {
            renderList(sellableProductList)
        } else {
            renderList(listOf(
                    ActiveProductSwitchUiModel(false),
                    TotalStockEditorUiModel(sellableProductList.firstOrNull()?.stock.toIntOrZero())
            ))
        }

    }

}