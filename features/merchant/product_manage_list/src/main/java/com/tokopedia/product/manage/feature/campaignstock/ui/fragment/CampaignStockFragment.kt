package com.tokopedia.product.manage.feature.campaignstock.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.manage.ProductManageInstance
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.campaignstock.di.DaggerCampaignStockComponent
import com.tokopedia.product.manage.feature.campaignstock.domain.model.GetStockAllocationData
import com.tokopedia.product.manage.feature.campaignstock.domain.model.GetStockAllocationSummary
import com.tokopedia.product.manage.feature.campaignstock.ui.CampaignStockActivity
import com.tokopedia.product.manage.feature.campaignstock.ui.adapter.CampaignStockAdapter
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.ReservedEventInfoModel
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.SellableStockProductModel
import com.tokopedia.product.manage.feature.campaignstock.ui.util.CampaignStockMapper
import com.tokopedia.product.manage.feature.campaignstock.ui.viewmodel.CampaignStockViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_campaign_stock.*
import kotlinx.android.synthetic.main.layout_campaign_stock_product_info.view.*
import javax.inject.Inject

class CampaignStockFragment: BaseDaggerFragment() {

    companion object {
        @JvmStatic
        fun createInstance(): CampaignStockFragment = CampaignStockFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val mViewModel: CampaignStockViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(CampaignStockViewModel::class.java)
    }

    private val shopId by lazy {
        activity?.intent?.getStringExtra(CampaignStockActivity.SHOP_ID)
    }

    private val productIds by lazy {
        activity?.intent?.getStringArrayExtra(CampaignStockActivity.PRODUCT_ID)
    }

    private val onTabSelectedListener by lazy {
        object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab) {}

            override fun onTabUnselected(p0: TabLayout.Tab) {}

            override fun onTabSelected(tab: TabLayout.Tab) {
                changeViewPagerPage(tab.position)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_campaign_stock, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeLiveData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        activity?.run {
            DaggerCampaignStockComponent.builder()
                    .productManageComponent(ProductManageInstance.getComponent(application))
                    .build()
                    .inject(this@CampaignStockFragment)
        }
    }

    private fun observeLiveData() {
        mViewModel.getStockAllocationData.observe(viewLifecycleOwner, Observer { result ->
            when(result) {
                is Success -> {
                    result.data.let { productUrlAndStockDataPair ->
                        applyLayout(productUrlAndStockDataPair.first, productUrlAndStockDataPair.second)
                    }
                    showResult()
                }
                is Fail -> {
                    //Todo: finish activity with cancelled result
                }
            }
        })
    }

    private fun setupView() {
        shopId?.let { shopId ->
            productIds?.let { productIds ->
                mViewModel.getStockAllocation(productIds.toList(), shopId)
            }
        }
    }

    private fun applyLayout(productImageUrl: String, data: GetStockAllocationData) {
        with(data) {
            setupProductSummary(summary, productImageUrl)
            setupFragmentTabs(this)
        }
    }

    private fun setupProductSummary(summary: GetStockAllocationSummary?, productImageUrl: String) {
        summary?.run {
            layout_campaign_stock_product_info?.run {
                productImageUrl.let { url ->
                    img_campaign_stock_product?.loadImageRounded(url)
                }
                tv_campaign_stock_product_name?.text = productName
                tv_campaign_stock_product_total_stock_count?.text = totalStock
            }
        }
    }

    private fun setupFragmentTabs(data: GetStockAllocationData) {
        //Todo: set fragment data
        with(data) {
            tabs_campaign_stock?.run {
                addNewTab(String.format(context?.getString(R.string.product_manage_campaign_stock_main_stock).orEmpty(), summary.sellableStock.toIntOrZero()))
                addNewTab(String.format(context?.getString(R.string.product_manage_campaign_stock_campaign_stock).orEmpty(), summary.reserveStock.toIntOrZero()))

                getUnifyTabLayout().addOnTabSelectedListener(onTabSelectedListener)
            }
            vp2_campaign_stock?.run {
                adapter = activity?.let {
                    CampaignStockAdapter(it, getFragmentList(
                            summary.isVariant,
                            detail.sellable.map { sellable -> CampaignStockMapper.mapToParcellableSellableProduct(sellable) } as ArrayList<SellableStockProductModel>,
                            detail.reserve.map { reserved -> CampaignStockMapper.mapToParcellableReserved(reserved) } as ArrayList<ReservedEventInfoModel>))
                }
                isUserInputEnabled = false
            }
        }
    }

    private fun showResult() {
        layout_campaign_stock_product_info?.visible()
        vp2_campaign_stock?.visible()
        divider_campaign_stock?.visible()
        btn_campaign_stock_save?.visible()
        loader_campaign_stock?.gone()
    }

    private fun changeViewPagerPage(position: Int) {
        vp2_campaign_stock?.currentItem = position
    }

    private fun getMainStockFragment(isVariant: Boolean,
                                     sellableProductList: ArrayList<SellableStockProductModel>) =
            CampaignMainStockFragment.createInstance(isVariant, sellableProductList)

    private fun getReservedStockFragment(isVariant: Boolean,
                                         reservedEventInfoList: ArrayList<ReservedEventInfoModel>) =
            CampaignReservedStockFragment.createInstance(isVariant, reservedEventInfoList)

    private fun getFragmentList(isVariant: Boolean,
                                sellableProductList: ArrayList<SellableStockProductModel>,
                                reservedEventInfoList: ArrayList<ReservedEventInfoModel>): List<Fragment>{
        return listOf(
                getMainStockFragment(isVariant, sellableProductList),
                getReservedStockFragment(isVariant, reservedEventInfoList)
        )
    }
}