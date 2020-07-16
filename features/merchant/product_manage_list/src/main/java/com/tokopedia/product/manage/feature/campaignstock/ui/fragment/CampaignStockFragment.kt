package com.tokopedia.product.manage.feature.campaignstock.ui.fragment

import android.app.Activity
import android.content.Intent
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
import com.tokopedia.product.manage.feature.campaignstock.domain.model.response.GetStockAllocationData
import com.tokopedia.product.manage.feature.campaignstock.domain.model.response.GetStockAllocationSummary
import com.tokopedia.product.manage.feature.campaignstock.domain.model.response.OtherCampaignStockData
import com.tokopedia.product.manage.feature.campaignstock.ui.activity.CampaignStockActivity
import com.tokopedia.product.manage.feature.campaignstock.ui.adapter.CampaignStockAdapter
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.result.NonVariantStockAllocationResult
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.result.StockAllocationResult
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.result.VariantStockAllocationResult
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.ReservedEventInfoUiModel
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.SellableStockProductUIModel
import com.tokopedia.product.manage.feature.campaignstock.ui.util.CampaignStockMapper
import com.tokopedia.product.manage.feature.campaignstock.ui.viewmodel.CampaignStockViewModel
import com.tokopedia.product.manage.feature.list.constant.ProductManageListConstant
import com.tokopedia.product.manage.feature.quickedit.variant.presentation.data.GetVariantResult
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_campaign_stock.*
import kotlinx.android.synthetic.main.layout_campaign_stock_product_info.view.*
import javax.inject.Inject

class CampaignStockFragment: BaseDaggerFragment(), CampaignStockListener {

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

    override fun onDestroyView() {
        removeObservers()
        super.onDestroyView()
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

    override fun onTotalStockChanged(totalStock: Int) {
        mViewModel.updateNonVariantStockCount(totalStock)
    }

    override fun onActiveStockChanged(isActive: Boolean) {
        mViewModel.updateNonVariantIsActive(isActive)
    }

    override fun onVariantStockChanged(productId: String, stock: Int) {
        mViewModel.updateVariantStockCount(productId, stock)
    }

    override fun onVariantStatusChanged(productId: String, status: ProductStatus) {
        mViewModel.updateVariantIsActive(productId, status)
    }

    private fun observeLiveData() {
        mViewModel.getStockAllocationData.observe(viewLifecycleOwner, Observer { result ->
            when(result) {
                is Success -> {
                    applyLayout(result.data)
                    showResult()
                }
                is Fail -> {
                    cancelActivity()
                }
            }
        })
        mViewModel.productUpdateResponseLiveData.observe(viewLifecycleOwner, Observer { result ->
            when(result) {
                is Success -> {
                    with(result.data) {
                        if (isSuccess) {
                            val resultIntent = Intent().apply {
                                putExtra(ProductManageListConstant.EXTRA_PRODUCT_ID, productId)
                                putExtra(ProductManageListConstant.EXTRA_PRODUCT_NAME, productName)
                                putExtra(ProductManageListConstant.EXTRA_UPDATED_STOCK, stock)
                                putExtra(ProductManageListConstant.EXTRA_UPDATED_STATUS, status.name)
                            }
                            activity?.run {
                                setResult(Activity.RESULT_OK, resultIntent)
                                finish()
                            }
                        } else {
                            cancelActivity()
                        }
                    }
                }
                is Fail -> {
                    cancelActivity()
                }
            }
        })
    }

    private fun setupView() {
        setupButtonOnClick()
        shopId?.let { shopId ->
            productIds?.let { productIds ->
                mViewModel.setShopId(shopId)
                mViewModel.getStockAllocation(productIds.toList())
            }
        }
    }

    private fun setupButtonOnClick() {
        btn_campaign_stock_save?.setOnClickListener {
            showButtonLoading()
            mViewModel.updateStockData()
        }
    }

    private fun applyLayout(stockAllocationResult: StockAllocationResult) {
        with(stockAllocationResult) {
            setupProductSummary(getStockAllocationData.summary, otherCampaignStockData.pictureList.firstOrNull()?.urlThumbnail.orEmpty())
            setupFragmentTabs(getStockAllocationData)
            when(this) {
                is VariantStockAllocationResult -> {
                    setupVariantFragmentViewPager(getVariantResult, getStockAllocationData, otherCampaignStockData)
                }
                is NonVariantStockAllocationResult -> {
                    setupNonVariantFragmentViewPager(getStockAllocationData, otherCampaignStockData)
                }
            }
        }
    }

    private fun setupProductSummary(summary: GetStockAllocationSummary?,
                                    productImageUrl: String) {
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

    private fun setupFragmentTabs(getStockAllocation: GetStockAllocationData) {
        with(getStockAllocation.summary) {
            tabs_campaign_stock?.run {
                addNewTab(String.format(context?.getString(R.string.product_manage_campaign_stock_main_stock).orEmpty(), sellableStock.toIntOrZero()))
                addNewTab(String.format(context?.getString(R.string.product_manage_campaign_stock_campaign_stock).orEmpty(), reserveStock.toIntOrZero()))

                getUnifyTabLayout().addOnTabSelectedListener(onTabSelectedListener)
            }
        }
    }

    private fun setupVariantFragmentViewPager(getVariantResult: GetVariantResult,
                                              getStockAllocation: GetStockAllocationData,
                                              otherCampaignStockData: OtherCampaignStockData) {
        vp2_campaign_stock?.run {
            adapter = activity?.let {
                CampaignStockAdapter(it, getFragmentList(
                        getStockAllocation.summary.isVariant,
                        otherCampaignStockData.isActive,
                        otherCampaignStockData.stock,
                        CampaignStockMapper.mapToParcellableSellableProduct(getStockAllocation.detail.sellable, getVariantResult.variants) as ArrayList<SellableStockProductUIModel>,
                        getStockAllocation.detail.reserve.map { reserved ->
                            CampaignStockMapper.mapToParcellableReserved(reserved) } as ArrayList<ReservedEventInfoUiModel>))
            }
            isUserInputEnabled = false
        }
    }

    private fun setupNonVariantFragmentViewPager(getStockAllocation: GetStockAllocationData,
                                                 otherCampaignStockData: OtherCampaignStockData) {
        with(getStockAllocation) {
            vp2_campaign_stock?.run {
                adapter = activity?.let {
                    CampaignStockAdapter(it, getFragmentList(
                            summary.isVariant,
                            otherCampaignStockData.isActive,
                            otherCampaignStockData.stock,
                            arrayListOf(),
                            detail.reserve.map { reserved ->
                                CampaignStockMapper.mapToParcellableReserved(reserved) } as ArrayList<ReservedEventInfoUiModel>))
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

    private fun showButtonLoading() {
        btn_campaign_stock_save?.isLoading = true
    }

    private fun changeViewPagerPage(position: Int) {
        vp2_campaign_stock?.currentItem = position
    }

    private fun cancelActivity() {
        activity?.run {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    private fun removeObservers() {
        mViewModel.getStockAllocationData.removeObservers(viewLifecycleOwner)
        mViewModel.productUpdateResponseLiveData.removeObservers(viewLifecycleOwner)
    }

    private fun getMainStockFragment(isVariant: Boolean,
                                     sellableProductUIList: ArrayList<SellableStockProductUIModel>,
                                     isActive: Boolean,
                                     stock: Int) =
            CampaignMainStockFragment.createInstance(isVariant, sellableProductUIList, isActive, stock, this)

    private fun getReservedStockFragment(isVariant: Boolean,
                                         reservedEventInfoUiList: ArrayList<ReservedEventInfoUiModel>) =
            CampaignReservedStockFragment.createInstance(isVariant, reservedEventInfoUiList)

    private fun getFragmentList(isVariant: Boolean,
                                isMainStockActive: Boolean,
                                stock: Int,
                                sellableProductUIList: ArrayList<SellableStockProductUIModel>,
                                reservedEventInfoUiList: ArrayList<ReservedEventInfoUiModel>): List<Fragment>{
        return listOf(
                getMainStockFragment(isVariant, sellableProductUIList, isMainStockActive, stock),
                getReservedStockFragment(isVariant, reservedEventInfoUiList)
        )
    }
}