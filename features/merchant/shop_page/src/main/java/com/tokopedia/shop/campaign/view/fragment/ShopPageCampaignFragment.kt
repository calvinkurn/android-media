package com.tokopedia.shop.campaign.view.fragment

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.applink.RouteManager
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.analytic.ShopCampaignTabTracker
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VALUE_MULTIPLE_BUNDLING
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VALUE_SINGLE_BUNDLING
import com.tokopedia.shop.campaign.util.mapper.ShopPageCampaignMapper
import com.tokopedia.shop.campaign.view.adapter.ShopCampaignTabAdapter
import com.tokopedia.shop.campaign.view.adapter.ShopCampaignTabAdapterTypeFactory
import com.tokopedia.shop.campaign.view.adapter.viewholder.ShopCampaignDisplaySliderBannerHighlightViewHolder
import com.tokopedia.shop.campaign.view.adapter.viewholder.ShopCampaignProductBundleParentWidgetViewHolder
import com.tokopedia.shop.campaign.view.adapter.viewholder.WidgetConfigListener
import com.tokopedia.shop.campaign.view.listener.ShopCampaignCarouselProductListener
import com.tokopedia.shop.campaign.view.listener.ShopCampaignInterface
import com.tokopedia.shop.campaign.view.model.ShopCampaignWidgetCarouselProductUiModel
import com.tokopedia.shop.campaign.view.model.ShopWidgetDisplaySliderBannerHighlightUiModel
import com.tokopedia.shop.campaign.view.viewmodel.ShopCampaignViewModel
import com.tokopedia.shop.common.constant.ShopPageLoggerConstant
import com.tokopedia.shop.common.data.model.ShopPageWidgetLayoutUiModel
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeBundleProductUiModel
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeProductBundleDetailUiModel
import com.tokopedia.shop.databinding.FragmentShopPageCampaignBinding
import com.tokopedia.shop.home.di.component.DaggerShopPageHomeComponent
import com.tokopedia.shop.home.di.module.ShopPageHomeModule
import com.tokopedia.shop.home.view.fragment.ShopPageHomeFragment
import com.tokopedia.shop.home.view.listener.ShopHomeListener
import com.tokopedia.shop.home.view.model.ShopHomeProductBundleListUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeVoucherUiModel
import com.tokopedia.shop.home.view.model.ShopPageHomeWidgetLayoutUiModel
import com.tokopedia.shop.home.view.viewmodel.ShopHomeViewModel
import com.tokopedia.shop.pageheader.presentation.fragment.ShopPageHeaderFragment
import com.tokopedia.shop.pageheader.util.ShopPageHeaderTabName
import com.tokopedia.shop.product.view.adapter.scrolllistener.DataEndlessScrollListener
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class ShopPageCampaignFragment :
    ShopPageHomeFragment(),
    WidgetConfigListener,
    ShopCampaignProductBundleParentWidgetViewHolder.Listener,
    ShopHomeListener,
    ShopCampaignInterface,
    ShopCampaignDisplaySliderBannerHighlightViewHolder.Listener,
    ShopCampaignCarouselProductListener {

    companion object {
        private const val KEY_SHOP_ID = "SHOP_ID"
        private const val LOAD_WIDGET_ITEM_PER_PAGE = 30
        private const val LIST_WIDGET_LAYOUT_START_INDEX = 0

        fun createInstance(shopId: String): ShopPageCampaignFragment {
            val bundle = Bundle()
            bundle.putString(KEY_SHOP_ID, shopId)
            return ShopPageCampaignFragment().apply {
                arguments = bundle
            }
        }
    }

    private val viewBindingCampaignTab: FragmentShopPageCampaignBinding? by viewBinding()
    private var campaignTabBackgroundColor: String = ""
    private var campaignTabBackgroundPatternImage: String = ""
    private var recyclerViewTopPadding = 0
    private var isClickToScrollToTop = false
    private var latestCompletelyVisibleItemIndex = -1
    private var textColor: String = ""
    private var staggeredGridLayoutManager: StaggeredGridLayoutManager? = null
    private var viewModelCampaign: ShopCampaignViewModel? = null

    private val shopCampaignTabAdapter: ShopCampaignTabAdapter
        get() = adapter as ShopCampaignTabAdapter
    private val shopCampaignTabAdapterTypeFactory by lazy {
        ShopCampaignTabAdapterTypeFactory(
            shopHomeDisplayWidgetListener = this,
            shopCampaignDisplayBannerTimerWidgetListener = this,
            shopCampaignCarouselProductListener = this,
            playWidgetCoordinator = playWidgetCoordinator,
            shopHomePlayWidgetListener = this,
            multipleProductBundleListener = this,
            singleProductBundleListener = this,
            bundlingParentListener = this,
            shopCampaignInterface = this,
            sliderBannerHighlightListener = this
        )
    }

    private var globalErrorShopPage: GlobalError? = null

    @Inject
    lateinit var shopCampaignTabTracker: ShopCampaignTabTracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelCampaign =
            ViewModelProviders.of(this, viewModelFactory).get(ShopCampaignViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shop_page_campaign, container, false)
    }

    override fun getRecyclerViewResourceId(): Int {
        return R.id.recycler_view
    }

    override fun callInitialLoadAutomatically(): Boolean {
        return false
    }

    override fun loadInitialData() {
        shopCampaignTabAdapter.showLoading()
        getRecyclerView(view)?.visible()
        recyclerViewTopPadding = getRecyclerView(view)?.paddingTop ?: 0
        globalErrorShopPage?.hide()
        shopPageHomeLayoutUiModel?.let {
            setShopWidgetLayoutData(it)
            setWidgetLayoutPlaceholder()
        }
    }

    override fun setWidgetLayoutPlaceholder() {
        val shopHomeWidgetContentData =
            ShopPageCampaignMapper.mapShopCampaignWidgetLayoutToListShopCampaignWidget(
                listWidgetLayout,
                shopId
            )
        shopCampaignTabAdapter.setCampaignLayoutData(shopHomeWidgetContentData)
    }

    override fun setShopWidgetLayoutData(dataWidgetLayoutUiModel: ShopPageHomeWidgetLayoutUiModel) {
        listWidgetLayout = dataWidgetLayoutUiModel.listWidgetLayout.toMutableList()
        val shopCampaignWidgetContentData =
            ShopPageCampaignMapper.mapShopCampaignWidgetLayoutToListShopCampaignWidget(
                dataWidgetLayoutUiModel.listWidgetLayout,
                shopId
            )
        shopCampaignTabAdapter.setCampaignLayoutData(shopCampaignWidgetContentData)
    }

    override fun initView() {
        globalErrorShopPage = viewBindingCampaignTab?.globalError
    }

    override fun observeShopHomeWidgetContentData() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModelCampaign?.shopCampaignWidgetContentData?.collect {
                when (it) {
                    is Success -> {
                        onSuccessGetShopHomeWidgetContentData(it.data)
                        renderCampaignTabBackgroundColor()
                    }

                    is Fail -> {
                        val throwable = it.throwable
                        val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
                        if (!ShopUtil.isExceptionIgnored(throwable)) {
                            ShopUtil.logShopPageP2BuyerFlowAlerting(
                                tag = ShopPageLoggerConstant.Tag.SHOP_PAGE_BUYER_FLOW_TAG,
                                functionName = this@ShopPageCampaignFragment::observeShopHomeWidgetContentData.name,
                                liveDataName = ShopHomeViewModel::shopHomeWidgetContentData.name,
                                userId = userId,
                                shopId = shopId,
                                shopName = shopName,
                                errorMessage = errorMessage,
                                stackTrace = Log.getStackTraceString(throwable),
                                errType = ShopPageLoggerConstant.Tag.SHOP_PAGE_HOME_TAB_BUYER_FLOW_TAG
                            )
                        }
                        showErrorToast(errorMessage)
                    }
                }
            }
            viewModelCampaign?.shopCampaignHomeWidgetContentDataError?.collect {
                shopCampaignTabAdapter.removeShopHomeWidget(it)
            }
        }
    }

    override fun onSuccessGetShopHomeWidgetContentData(mapWidgetContentData: Map<Pair<String, String>, Visitable<*>?>) {
        shopCampaignTabAdapter.updateShopCampaignWidgetContentData(mapWidgetContentData)
    }

    // region mvc widget listener
    override fun onVoucherImpression(model: ShopHomeVoucherUiModel, position: Int) {
        shopCampaignTabTracker.impressionShopBannerWidget(
            shopId,
            model.name,
            model.widgetId,
            ShopUtil.getActualPositionFromIndex(position),
            userId
        )
    }

    override fun onVoucherTokoMemberInformationImpression(
        model: ShopHomeVoucherUiModel,
        position: Int
    ) {
        shopCampaignTabTracker.impressionShopBannerWidget(
            shopId,
            model.name,
            model.widgetId,
            ShopUtil.getActualPositionFromIndex(position),
            userId
        )
        shopCampaignTabTracker.impressionSeeEntryPointMerchantVoucherCouponTokoMemberInformation(
            shopId
        )
    }

    override fun onVoucherReloaded() {
        getMvcWidgetData()
    }
    // endregion

    //region Bundling Widget
    override fun onImpressionBundlingWidget(
        model: ShopHomeProductBundleListUiModel,
        position: Int
    ) {
        shopCampaignTabTracker.impressionShopBannerWidget(
            shopId,
            model.name,
            model.widgetId,
            ShopUtil.getActualPositionFromIndex(position),
            userId
        )
    }

    override fun onMultipleBundleProductClicked(
        shopId: String,
        warehouseId: String,
        selectedProduct: ShopHomeBundleProductUiModel,
        selectedMultipleBundle: ShopHomeProductBundleDetailUiModel,
        bundleName: String,
        bundleType: String,
        bundlePosition: Int,
        widgetTitle: String,
        widgetName: String,
        productItemPosition: Int
    ) {
        shopCampaignTabTracker.clickCampaignTabProduct(
            selectedProduct.productId,
            selectedProduct.productName,
            selectedMultipleBundle.displayPriceRaw,
            widgetName,
            shopId,
            userId,
            widgetTitle,
            ShopUtil.getActualPositionFromIndex(productItemPosition),
            VALUE_MULTIPLE_BUNDLING,
            selectedMultipleBundle.bundleId
        )
        goToPDP(selectedProduct.productAppLink)
    }

    override fun onSingleBundleProductClicked(
        shopId: String,
        warehouseId: String,
        selectedProduct: ShopHomeBundleProductUiModel,
        selectedSingleBundle: ShopHomeProductBundleDetailUiModel,
        bundleName: String,
        bundlePosition: Int,
        widgetTitle: String,
        widgetName: String,
        productItemPosition: Int,
        bundleType: String
    ) {
        shopCampaignTabTracker.clickCampaignTabProduct(
            selectedProduct.productId,
            selectedProduct.productName,
            selectedSingleBundle.displayPriceRaw,
            widgetName,
            shopId,
            userId,
            widgetTitle,
            ShopUtil.getActualPositionFromIndex(productItemPosition),
            VALUE_SINGLE_BUNDLING,
            selectedSingleBundle.bundleId
        )
        goToPDP(selectedProduct.productAppLink)
    }

    override fun impressionProductItemBundleMultiple(
        selectedProduct: ShopHomeBundleProductUiModel,
        selectedMultipleBundle: ShopHomeProductBundleDetailUiModel,
        bundleName: String,
        bundlePosition: Int,
        widgetTitle: String,
        widgetName: String,
        productItemPosition: Int
    ) {
        shopCampaignTabTracker.impressionCampaignTabProduct(
            selectedProduct.productId,
            selectedProduct.productName,
            selectedMultipleBundle.displayPriceRaw,
            widgetName,
            shopId,
            userId,
            widgetTitle,
            ShopUtil.getActualPositionFromIndex(productItemPosition),
            VALUE_MULTIPLE_BUNDLING,
            selectedMultipleBundle.bundleId
        )
    }

    override fun impressionProductBundleSingle(
        shopId: String,
        warehouseId: String,
        selectedSingleBundle: ShopHomeProductBundleDetailUiModel,
        selectedProduct: ShopHomeBundleProductUiModel,
        bundleName: String,
        bundlePosition: Int,
        widgetTitle: String,
        widgetName: String,
        bundleType: String
    ) {
        shopCampaignTabTracker.impressionCampaignTabProduct(
            selectedProduct.productId,
            selectedProduct.productName,
            selectedSingleBundle.displayPriceRaw,
            widgetName,
            shopId,
            userId,
            widgetTitle,
            ShopUtil.getActualPositionFromIndex(bundlePosition),
            VALUE_SINGLE_BUNDLING,
            selectedSingleBundle.bundleId
        )
    }
    //endregion

    override fun getWidgetTextColor(): Int {
        return parseColor(textColor)
    }

    private fun renderCampaignTabBackgroundColor() {
//        if (listBackgroundColor.isNotEmpty()) {
//            topView?.show()
//            centerView?.show()
//            bottomView?.show()
//            val colors = IntArray(listBackgroundColor.size)
//            for (i in listBackgroundColor.indices) {
//                colors[i] = parseColor(listBackgroundColor.getOrNull(i).orEmpty())
//            }
//            val gradient = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors)
//            gradient.cornerRadius = 0f
//            topView?.setBackgroundColor(parseColor(listBackgroundColor.firstOrNull().orEmpty()))
//            centerView?.background = gradient
//            bottomView?.setBackgroundColor(parseColor(listBackgroundColor.lastOrNull().orEmpty()))
//        } else {
//            topView?.hide()
//            centerView?.hide()
//            bottomView?.hide()
//        }
    }

    private fun parseColor(colorHex: String): Int {
        return try {
            Color.parseColor(colorHex)
        } catch (e: Throwable) {
            0
        }
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, AdapterTypeFactory> {
        return ShopCampaignTabAdapter(shopCampaignTabAdapterTypeFactory)
    }

    override fun getAdapterTypeFactory(): AdapterTypeFactory {
        return shopCampaignTabAdapterTypeFactory
    }

    override fun onItemClicked(t: Visitable<*>?) {
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        activity?.run {
            DaggerShopPageHomeComponent
                .builder()
                .shopPageHomeModule(ShopPageHomeModule())
                .shopComponent(ShopComponentHelper().getComponent(application, this))
                .build()
                .inject(this@ShopPageCampaignFragment)
        }
    }

    override fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object :
            DataEndlessScrollListener(staggeredGridLayoutManager, shopCampaignTabAdapter) {

            override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(view, dx, dy)
                val layoutManager =
                    (getRecyclerView(view)?.layoutManager as? StaggeredGridLayoutManager)
                val firstCompletelyVisibleItemPosition =
                    layoutManager?.findFirstCompletelyVisibleItemPositions(null)?.getOrNull(0)
                        .orZero()
                if (firstCompletelyVisibleItemPosition == 0 && isClickToScrollToTop) {
                    isClickToScrollToTop = false
                    (parentFragment as? ShopPageHeaderFragment)?.expandHeader()
                }
                if (firstCompletelyVisibleItemPosition != latestCompletelyVisibleItemIndex) {
                    hideScrollToTopButton()
                }
                latestCompletelyVisibleItemIndex = firstCompletelyVisibleItemPosition
                val lastCompletelyVisibleItemPosition =
                    layoutManager?.findLastCompletelyVisibleItemPositions(
                        null
                    )?.getOrNull(0).orZero()
                val firstVisibleItemPosition = layoutManager?.findFirstVisibleItemPositions(
                    null
                )?.getOrNull(0).orZero()
                checkLoadNextShopCampaignWidgetContentData(
                    lastCompletelyVisibleItemPosition,
                    firstVisibleItemPosition
                )
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, state: Int) {
                super.onScrollStateChanged(recyclerView, state)
                if (state == SCROLL_STATE_IDLE) {
                    val firstCompletelyVisibleItemPosition =
                        (layoutManager as? StaggeredGridLayoutManager)?.findFirstCompletelyVisibleItemPositions(
                            null
                        )?.getOrNull(0).orZero()
                    if (firstCompletelyVisibleItemPosition > 0) {
                        showScrollToTopButton()
                    }
                    //TODO need to add changes from play on home tab after PR #32199 merged
                }
            }

            override fun onLoadMore(page: Int, totalItemsCount: Int) {}
        }
    }

    private fun checkLoadNextShopCampaignWidgetContentData(
        lastVisibleItemPosition: Int,
        firstVisibleItemPosition: Int
    ) {
        val shouldLoadLastVisibleItem =
            shopCampaignTabAdapter.isLoadNextCampaignWidgetData(lastVisibleItemPosition)
        val shouldLoadFirstVisibleItem =
            shopCampaignTabAdapter.isLoadNextCampaignWidgetData(firstVisibleItemPosition)
        if (shouldLoadLastVisibleItem || shouldLoadFirstVisibleItem) {
            val position = if (shouldLoadLastVisibleItem) {
                lastVisibleItemPosition
            } else {
                firstVisibleItemPosition
            }
            val listWidgetLayoutToLoad = getListWidgetLayoutToLoad(position)
            shopCampaignTabAdapter.updateShopCampaignWidgetStateToLoading(listWidgetLayoutToLoad)

            val widgetMvcLayout = listWidgetLayoutToLoad.firstOrNull { isWidgetMvc(it) }?.apply {
                listWidgetLayoutToLoad.remove(this)
            }
            getWidgetContentData(listWidgetLayoutToLoad)

            // get mvc widget content data
            widgetMvcLayout?.let {
                getMvcWidgetData()
            }
            listWidgetLayoutToLoad.clear()
        }
    }

    private fun getWidgetContentData(listWidgetLayoutToLoad: MutableList<ShopPageWidgetLayoutUiModel>) {
        if (listWidgetLayoutToLoad.isNotEmpty()) {
            val widgetUserAddressLocalData = ShopUtil.getShopPageWidgetUserAddressLocalData(context)
                ?: LocalCacheModel()
            viewModelCampaign?.getWidgetContentData(
                listWidgetLayoutToLoad.toList(),
                shopId,
                widgetUserAddressLocalData,
            )
        }
    }

    private fun getListWidgetLayoutToLoad(lastCompletelyVisibleItemPosition: Int): MutableList<ShopPageWidgetLayoutUiModel> {
        return if (listWidgetLayout.isNotEmpty()) {
            if (shopCampaignTabAdapter.isLoadFirstWidgetContentData()) {
                listWidgetLayout.subList(
                    LIST_WIDGET_LAYOUT_START_INDEX,
                    ShopUtil.getActualPositionFromIndex(lastCompletelyVisibleItemPosition)
                )
            } else {
                val toIndex = LOAD_WIDGET_ITEM_PER_PAGE.takeIf { it <= listWidgetLayout.size }
                    ?: listWidgetLayout.size
                listWidgetLayout.subList(LIST_WIDGET_LAYOUT_START_INDEX, toIndex)
            }
        } else {
            mutableListOf()
        }
    }

    override fun loadData(page: Int) {}

    override fun scrollToTop() {
        isClickToScrollToTop = true
        getRecyclerView(view)?.scrollToPosition(0)
    }

    override fun isShowScrollToTopButton(): Boolean {
        return latestCompletelyVisibleItemIndex > 0
    }

    private fun hideScrollToTopButton() {
        (parentFragment as? ShopPageHeaderFragment)?.hideScrollToTopButton()
    }

    private fun showScrollToTopButton() {
        (parentFragment as? ShopPageHeaderFragment)?.showScrollToTopButton()
    }

    fun setCampaignTabBackgroundColor(backgroundColor: String) {
        this.campaignTabBackgroundColor = backgroundColor
    }

    fun setCampaignTabBackgroundPatternImage(backgroundPatternImage: String) {
        this.campaignTabBackgroundPatternImage = backgroundPatternImage
    }

    override fun isCampaignTabDarkMode(): Boolean {
        return true
    }

    override fun resumeSliderBannerAutoScroll() {
        shopCampaignTabAdapter.resumeSliderBannerAutoScroll()
    }

    override fun pauseSliderBannerAutoScroll() {
        shopCampaignTabAdapter.pauseSliderBannerAutoScroll()
    }

    override fun onProductImageClicked(productData: ShopWidgetDisplaySliderBannerHighlightUiModel.ProductHighlightData) {
        RouteManager.route(context, productData.appLink)
    }

    override fun onProductImageImpression(productData: ShopWidgetDisplaySliderBannerHighlightUiModel.ProductHighlightData) {
    }

    override fun onWidgetSliderBannerHighlightImpression(
        uiModel: ShopWidgetDisplaySliderBannerHighlightUiModel,
        bindingAdapterPosition: Int
    ) {
    }

    override fun onWidgetSliderBannerHighlightCtaClicked(uiModel: ShopWidgetDisplaySliderBannerHighlightUiModel) {
        val appLink = uiModel.header.ctaLink
        if(Uri.parse(appLink).lastPathSegment.equals("home", true)) {
            (parentFragment as? ShopPageHeaderFragment)?.selectShopTab(ShopPageHeaderTabName.HOME)
        }
    }

    override fun onCampaignCarouselProductItemClicked(
        parentPosition: Int,
        itemPosition: Int,
        carouselProductWidgetUiModel: ShopCampaignWidgetCarouselProductUiModel?,
        productUiModel: ShopHomeProductUiModel?
    ) {
    }

    override fun onCampaignCarouselProductItemImpression(
        parentPosition: Int,
        itemPosition: Int,
        carouselProductWidgetUiModel: ShopCampaignWidgetCarouselProductUiModel?,
        productUiModel: ShopHomeProductUiModel?
    ) {
    }

    override fun onCtaClicked(carouselProductWidgetUiModel: ShopCampaignWidgetCarouselProductUiModel?) {
    }

    override fun onCampaignCarouselProductWidgetImpression(
        position: Int,
        carouselProductWidgetUiModel: ShopCampaignWidgetCarouselProductUiModel
    ) {
    }

}
