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
import com.tokopedia.product.manage.ProductManageInstance
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccess
import com.tokopedia.product.manage.common.feature.list.view.mapper.ProductManageTickerMapper.mapToTickerData
import com.tokopedia.product.manage.common.feature.list.view.mapper.ProductManageTickerMapper.mapToTickerList
import com.tokopedia.product.manage.feature.campaignstock.di.DaggerCampaignStockComponent
import com.tokopedia.product.manage.feature.campaignstock.ui.adapter.typefactory.CampaignStockAdapterTypeFactory
import com.tokopedia.product.manage.feature.campaignstock.ui.adapter.typefactory.CampaignStockTypeFactory
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.ActiveProductSwitchUiModel
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.CampaignStockTickerUiModel
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.SellableStockProductUIModel
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.TotalStockEditorUiModel
import com.tokopedia.product.manage.feature.campaignstock.ui.viewmodel.CampaignMainStockViewModel
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class CampaignMainStockFragment: BaseListFragment<Visitable<CampaignStockTypeFactory>, CampaignStockAdapterTypeFactory>() {

    companion object {
        @JvmStatic
        fun createInstance(isVariant: Boolean,
                           sellableProductUIList: ArrayList<SellableStockProductUIModel>,
                           isActive: Boolean,
                           stock: Int,
                           isCampaign: Boolean,
                           access: ProductManageAccess,
                           campaignStockListener: CampaignStockListener): CampaignMainStockFragment {
            return CampaignMainStockFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(EXTRA_IS_VARIANT, isVariant)
                    putBoolean(EXTRA_IS_ACTIVE, isActive)
                    putBoolean(EXTRA_IS_CAMPAIGN, isCampaign)
                    putInt(EXTRA_STOCK, stock)
                    putParcelableArrayList(EXTRA_SELLABLE_PRODUCT_LIST, sellableProductUIList)
                    putParcelable(EXTRA_PRODUCT_MANAGE_ACCESS, access)
                }
                this.campaignStockListener = campaignStockListener
            }
        }

        private const val EXTRA_STOCK = "extra_stock"
        private const val EXTRA_IS_VARIANT = "extra_is_variant"
        private const val EXTRA_IS_ACTIVE = "extra_is_active"
        private const val EXTRA_IS_CAMPAIGN = "extra_is_campaign"
        private const val EXTRA_SELLABLE_PRODUCT_LIST = "extra_sellable"
        private const val EXTRA_PRODUCT_MANAGE_ACCESS = "extra_product_manage_access"

        private const val ITEM_TICKER_POSITION = 0
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

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

    private val isCampaign by lazy {
        arguments?.getBoolean(EXTRA_IS_CAMPAIGN)
    }

    private val sellableProductList by lazy {
        arguments?.getParcelableArrayList<SellableStockProductUIModel>(EXTRA_SELLABLE_PRODUCT_LIST)?.toList().orEmpty()
    }

    private val access by lazy { arguments?.getParcelable<ProductManageAccess>(EXTRA_PRODUCT_MANAGE_ACCESS) }

    private var campaignStockListener: CampaignStockListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_campaign_stock_tab, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeVariantStock()
        observeStockInfo()
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
        setStockAvailability()
    }

    private fun setupAdapterModels(isVariant: Boolean) {
        val items = if (isVariant) {
            mutableListOf<Visitable<CampaignStockTypeFactory>>().apply {
                addAll(sellableProductList)
            }
        } else {
            mutableListOf<Visitable<CampaignStockTypeFactory>>().apply {
                addAll(listOf(
                    ActiveProductSwitchUiModel(isActive, access),
                    TotalStockEditorUiModel(stockCount.orZero(), isCampaign, access)
                ))
            }
        }
        renderList(items)
    }

    private fun setStockAvailability() {
        mViewModel.setStockAvailability(sellableProductList)
    }

    private fun createTickerUiModel(isAllStockEmpty: Boolean): CampaignStockTickerUiModel {
        val isMultiLocationShop = userSession.isMultiLocationShop
        val canEditStock = access?.editStock == true

        val tickerList = mapToTickerList(isMultiLocationShop, canEditStock, isAllStockEmpty)
        val tickerData = mapToTickerData(context, tickerList)

        return CampaignStockTickerUiModel(tickerData)
    }

    private fun observeVariantStock() {
        mViewModel.shouldDisplayVariantStockWarningLiveData.observe(viewLifecycleOwner, Observer { isAllStockEmpty ->
            val shouldShowWarning = isAllStockEmpty && isVariant
            showVariantWarningTickerWithCondition(shouldShowWarning)
        })
    }

    private fun observeStockInfo() {
        mViewModel.showStockInfo.observe(viewLifecycleOwner, Observer { showStockInfo ->
            showHideStockInfo(showStockInfo)
        })
    }

    private fun showHideStockInfo(showStockInfo: Boolean) {
        adapter.apply {
            data.filterIsInstance<SellableStockProductUIModel>().forEach {
                val index = data.indexOf(it)
                data[index] = if (showStockInfo) {
                    it.copy(isAllStockEmpty = false)
                } else {
                    it.copy(isAllStockEmpty = true)
                }
            }
            notifyDataSetChanged()
        }
    }

    private fun onTotalStockChanged(totalStock: Int) {
        updateStockEditorItem(totalStock)
        campaignStockListener?.onTotalStockChanged(totalStock)
    }

    private fun updateStockEditorItem(totalStock: Int) {
        adapter.apply {
            data.firstOrNull { it is TotalStockEditorUiModel }?.let {
                val item = TotalStockEditorUiModel(totalStock, isCampaign, access)
                val index = data.indexOf(it)
                data[index] = item
                notifyItemChanged(index)
            }
        }
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
        with(adapter) {
            val ticker = data.firstOrNull { it is CampaignStockTickerUiModel }
            val tickerUiModel = createTickerUiModel(shouldShowWarning)

            if(ticker == null) {
                data.add(ITEM_TICKER_POSITION, tickerUiModel)
                notifyItemInserted(ITEM_TICKER_POSITION)
            } else {
                val index = data.indexOf(ticker)
                data[index] = tickerUiModel
                notifyItemChanged(index)
            }
        }
    }

}