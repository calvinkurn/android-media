package com.tokopedia.product.manage.feature.campaignstock.ui.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.product.manage.ProductManageInstance
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.common.feature.list.analytics.ProductManageTracking
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageCommonConstant
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageCommonConstant.EXTRA_SOURCE
import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccess
import com.tokopedia.product.manage.common.util.ProductManageConfig
import com.tokopedia.product.manage.databinding.FragmentCampaignStockBinding
import com.tokopedia.product.manage.feature.campaignstock.di.DaggerCampaignStockComponent
import com.tokopedia.product.manage.feature.campaignstock.domain.model.response.GetStockAllocationSummary
import com.tokopedia.product.manage.feature.campaignstock.domain.model.response.OtherCampaignStockData
import com.tokopedia.product.manage.feature.campaignstock.ui.activity.CampaignStockActivity
import com.tokopedia.product.manage.feature.campaignstock.ui.adapter.CampaignStockAdapter
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.result.NonVariantStockAllocationResult
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.result.StockAllocationResult
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.result.VariantStockAllocationResult
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.ReservedEventInfoUiModel
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.SellableStockProductUIModel
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.VariantReservedEventInfoUiModel
import com.tokopedia.product.manage.feature.campaignstock.ui.util.convertCheckMaximumStockLimit
import com.tokopedia.product.manage.feature.campaignstock.ui.viewmodel.CampaignStockViewModel
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class CampaignStockFragment : BaseDaggerFragment(), CampaignStockListener {

    companion object {
        private const val MAIN_TAB_POSITION = 0
        const val DEFAULT_SOURCE = "manage product"

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

    private val isBundling by lazy {
        activity?.intent?.getBooleanExtra(CampaignStockActivity.IS_BUNDLING, false) ?: false
    }

    private val source by lazy {
        activity?.intent?.getStringExtra(EXTRA_SOURCE) ?: DEFAULT_SOURCE
    }

    private var binding by autoClearedNullable<FragmentCampaignStockBinding>()

    private var isVariant: Boolean? = null

    private var tickerPagerAdapter: TickerPagerAdapter? = null

    private val onTabSelectedListener by lazy {
        object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab) {}

            override fun onTabUnselected(p0: TabLayout.Tab) {}

            override fun onTabSelected(tab: TabLayout.Tab) {
                val isMainStockTab = tab.position == MAIN_TAB_POSITION
                if (!ProductManageConfig.IS_SELLER_APP) {
                    toggleSaveButton()
                }
                isVariant?.run {
                    if (isMainStockTab) {
                        ProductManageTracking.eventClickAllocationMainStock(this)
                    } else {
                        ProductManageTracking.eventClickAllocationOnStockCampaign(this)
                    }
                }
                changeViewPagerPage(tab.position)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCampaignStockBinding.inflate(inflater, container, false)
        return binding?.root
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

    override fun setSaveButtonEnabled(isEnabled: Boolean) {
        binding?.btnCampaignStockSave?.isEnabled = isEnabled
    }

    private fun observeLiveData() {
        mViewModel.getStockAllocationData.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
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
            when (result) {
                is Success -> {
                    with(result.data) {
                        if (isSuccess) {
                            val resultIntent = Intent().apply {
                                putExtra(ProductManageCommonConstant.EXTRA_PRODUCT_ID, productId)
                                putExtra(
                                    ProductManageCommonConstant.EXTRA_PRODUCT_NAME,
                                    productName
                                )
                                putExtra(ProductManageCommonConstant.EXTRA_UPDATED_STOCK, stock)
                                putExtra(
                                    ProductManageCommonConstant.EXTRA_UPDATED_STATUS,
                                    status.name
                                )
                                putExtra(
                                    ProductManageCommonConstant.EXTRA_UPDATE_IS_STOCK_CHANGED,
                                    isStockChanged
                                )
                                putExtra(
                                    ProductManageCommonConstant.EXTRA_UPDATE_IS_STATUS_CHANGED,
                                    isStatusChanged
                                )
                                putExtra(
                                    ProductManageCommonConstant.EXTRA_UPDATE_VARIANTS_MAP,
                                    variantsMap
                                )
                            }
                            activity?.run {
                                setResult(Activity.RESULT_OK, resultIntent)
                                finish()
                            }
                        } else {
                            cancelActivity(result.data.message)
                        }
                    }
                }
                is Fail -> {
                    cancelActivity()
                }
            }
        })
        observe(mViewModel.showSaveBtn) {
            binding?.dividerCampaignStock?.showWithCondition(it)
            binding?.btnCampaignStockSave?.showWithCondition(it)
        }
        observe(mViewModel.tickerData) { tickerData ->
            var tickerPagerAdapter = tickerPagerAdapter
            if (tickerPagerAdapter == null) {
                tickerPagerAdapter = TickerPagerAdapter(context, tickerData)
                this.tickerPagerAdapter = tickerPagerAdapter.apply {
                    setPagerDescriptionClickEvent(object : TickerPagerCallback {
                        override fun onPageDescriptionViewClick(
                            linkUrl: CharSequence,
                            itemData: Any?
                        ) {
                            context?.let { RouteManager.route(it, linkUrl.toString()) }
                        }
                    })
                }
            }
            binding?.layoutTicker?.root?.let { tickerView ->
                tickerView.addPagerView(tickerPagerAdapter, tickerData)
                tickerView.tickerShape = Ticker.SHAPE_FULL
                tickerView.show()
            }
        }
    }

    private fun setupView() {
        setupButtonOnClick()
        setupHeader()
        shopId?.let { shopId ->
            productIds?.let { productIds ->
                mViewModel.setShopId(shopId)
                mViewModel.getStockAllocation(productIds.toList(), isBundling)
                mViewModel.getTickerData()

            }
        }

    }

    private fun setupButtonOnClick() {
        binding?.btnCampaignStockSave?.setOnClickListener {
            showButtonLoading()
            mViewModel.updateStockData()
            binding?.tabsCampaignStock?.getUnifyTabLayout()?.selectedTabPosition?.let { tabPosition ->
                val isMainStock = tabPosition == MAIN_TAB_POSITION
                isVariant?.run {
                    ProductManageTracking.eventClickAllocationSaveStock(
                        this, isMainStock, source, mViewModel.productId, mViewModel.shopId
                    )
                }
            }
        }
    }

    private fun setupHeader() {
        binding?.headerCampaignStock?.run {
            setBackgroundColor(Color.TRANSPARENT)
            setNavigationOnClickListener {
                isVariant?.run {
                    ProductManageTracking.eventClickCloseStockAllocation(this)
                }
                activity?.onBackPressed()
            }
        }
    }

    private fun applyLayout(stockAllocationResult: StockAllocationResult) {
        with(stockAllocationResult) {
            this@CampaignStockFragment.isVariant = getStockAllocationSummary.isVariant
            this@CampaignStockFragment.isVariant?.run {
                sendOpenScreenTracking(this)
            }
            setupProductSummary(getStockAllocationSummary, otherCampaignStockData.pictureList.firstOrNull()?.urlThumbnail.orEmpty())
            setupFragmentTabs(getStockAllocationSummary)
            when(this) {
                is VariantStockAllocationResult -> {
                    setupVariantFragmentViewPager(getStockAllocationSummary, sellableStockProductUiModels, variantReservedEventInfoUiModels, otherCampaignStockData, productManageAccess)
                }
                is NonVariantStockAllocationResult -> {
                    setupNonVariantFragmentViewPager(maxStock, getStockAllocationSummary, sellableStockProductUiModels, reservedEventInfoUiModels, otherCampaignStockData, productManageAccess)
                }
            }
        }
    }

    private fun setupProductSummary(
        summary: GetStockAllocationSummary?,
        productImageUrl: String
    ) {
        summary?.run {
            binding?.layoutCampaignStockProductInfo?.run {
                productImageUrl.let { url ->
                    ImageHandler.loadImageFitCenter(context, imgCampaignStockProduct, url)
                }
                tvCampaignStockProductName.text = productName
                tvCampaignStockProductTotalStockCount.text =
                    totalStock.convertCheckMaximumStockLimit(context)
            }
        }
    }

    private fun setupFragmentTabs(getStockAllocationSummary: GetStockAllocationSummary) {
        with(getStockAllocationSummary) {
            binding?.tabsCampaignStock?.run {
                addNewTab(String.format(context?.getString(R.string.product_manage_campaign_stock_main_stock).orEmpty(), sellableStock.convertCheckMaximumStockLimit(context)))
                addNewTab(String.format(context?.getString(R.string.product_manage_campaign_stock_campaign_stock).orEmpty(), reserveStock.convertCheckMaximumStockLimit(context)))

                getUnifyTabLayout().addOnTabSelectedListener(onTabSelectedListener)
            }
        }
    }

    private fun setupVariantFragmentViewPager(getStockAllocationSummary: GetStockAllocationSummary,
                                              sellableProduct: ArrayList<SellableStockProductUIModel>,
                                              reservedProducts: ArrayList<VariantReservedEventInfoUiModel>,
                                              otherCampaignStockData: OtherCampaignStockData,
                                              access: ProductManageAccess) {
        binding?.vp2CampaignStock?.run {
            adapter = activity?.let {
                CampaignStockAdapter(it, getVariantFragmentList(
                    otherCampaignStockData.getIsActive(),
                    getStockAllocationSummary.reserveStock.toIntOrZero(),
                    sellableProduct,
                    reservedProducts,
                    access
                ))
            }
            isUserInputEnabled = false
        }
    }

    private fun setupNonVariantFragmentViewPager(maxStock: Int?,
                                                 getStockAllocationSummary: GetStockAllocationSummary,
                                                 sellableProducts: ArrayList<SellableStockProductUIModel>,
                                                 reservedProducts: ArrayList<ReservedEventInfoUiModel>,
                                                 otherCampaignStockData: OtherCampaignStockData,
                                                 access: ProductManageAccess) {
        val nonVariantStock = getStockAllocationSummary.sellableStock.toIntOrZero()
        val nonVariantReservedStock = getStockAllocationSummary.reserveStock.toIntOrZero()

        mViewModel.updateNonVariantStockCount(nonVariantStock)
        mViewModel.updateNonVariantReservedStockCount(nonVariantReservedStock)

        binding?.vp2CampaignStock?.run {
            adapter = activity?.let {
                CampaignStockAdapter(it, getNonVariantFragmentList(
                    otherCampaignStockData.getIsActive(),
                    nonVariantStock,
                    sellableProducts,
                    reservedProducts,
                    access
                ))
            }
            isUserInputEnabled = false
        }
    }

    private fun showResult() {
        binding?.layoutCampaignStockProductInfo?.root?.visible()
        binding?.vp2CampaignStock?.visible()
        binding?.loaderCampaignStock?.gone()
        toggleSaveButton()
    }

    private fun toggleSaveButton() {
        binding?.tabsCampaignStock?.run {
            val selectedTabPosition = getUnifyTabLayout().selectedTabPosition
            val isMainStockTab = selectedTabPosition == MAIN_TAB_POSITION
            mViewModel.toggleSaveButton(isMainStockTab)
        }
    }

    private fun sendOpenScreenTracking(isVariant: Boolean) {
        ProductManageTracking.sendStockAllocationScreen(isVariant)
    }

    private fun showButtonLoading() {
        binding?.btnCampaignStockSave?.isLoading = true
    }

    private fun changeViewPagerPage(position: Int) {
        binding?.vp2CampaignStock?.currentItem = position
    }

    private fun cancelActivity(errorMessage: String? = null) {
        activity?.run {
            val intent = Intent().apply {
                errorMessage?.run {
                    putExtra(ProductManageCommonConstant.EXTRA_UPDATE_MESSAGE, this)
                }
                putExtra(
                    ProductManageCommonConstant.EXTRA_PRODUCT_ID,
                    productIds?.firstOrNull().orEmpty()
                )
            }
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        }
    }

    private fun removeObservers() {
        mViewModel.getStockAllocationData.removeObservers(viewLifecycleOwner)
        mViewModel.productUpdateResponseLiveData.removeObservers(viewLifecycleOwner)
    }

    private fun getMainStockFragment(
        isVariant: Boolean,
        sellableProductUIList: ArrayList<SellableStockProductUIModel>,
        isActive: Boolean,
        stock: Int,
        access: ProductManageAccess
    ) =
        CampaignMainStockFragment.createInstance(
            isVariant, sellableProductUIList, isActive,
            stock, access, source, this
        )

    private fun getNonVariantReservedStockFragment(reservedEventInfoUiList: ArrayList<ReservedEventInfoUiModel>,
                                                   access: ProductManageAccess) =
            CampaignReservedStockFragment.createNonVariantInstance(reservedEventInfoUiList, access)

    private fun getVariantReservedStockFragment(variantReservedEventInfoUiList: ArrayList<VariantReservedEventInfoUiModel>,
                                                access: ProductManageAccess) =
        CampaignReservedStockFragment.createVariantInstance(variantReservedEventInfoUiList, access)

    private fun getNonVariantFragmentList(isMainStockActive: Boolean,
                                          stock: Int,
                                          sellableProductUIList: ArrayList<SellableStockProductUIModel>,
                                          reservedEventInfoUiList: ArrayList<ReservedEventInfoUiModel>,
                                          access: ProductManageAccess): List<Fragment>{
        return listOf(
                getMainStockFragment(false, sellableProductUIList, isMainStockActive, stock, access),
            getNonVariantReservedStockFragment(reservedEventInfoUiList, access)
        )
    }

    private fun getVariantFragmentList(isMainStockActive: Boolean,
                                       stock: Int,
                                       sellableProductUIList: ArrayList<SellableStockProductUIModel>,
                                       variantReservedEventInfoUiList: ArrayList<VariantReservedEventInfoUiModel>,
                                       access: ProductManageAccess): List<Fragment>{
        return listOf(
            getMainStockFragment(true, sellableProductUIList, isMainStockActive, stock, access),
            getVariantReservedStockFragment(variantReservedEventInfoUiList, access)
        )
    }
}
