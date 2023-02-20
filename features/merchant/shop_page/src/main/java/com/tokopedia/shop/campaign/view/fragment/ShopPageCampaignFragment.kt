package com.tokopedia.shop.campaign.view.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.applink.RouteManager
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.analytic.ShopCampaignTabTracker
import com.tokopedia.shop.analytic.ShopPageTrackingConstant
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VALUE_MULTIPLE_BUNDLING
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VALUE_SINGLE_BUNDLING
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.campaign.view.adapter.ShopCampaignTabAdapter
import com.tokopedia.shop.campaign.view.adapter.ShopCampaignTabAdapterTypeFactory
import com.tokopedia.shop.campaign.view.adapter.viewholder.ShopCampaignProductBundleParentWidgetViewHolder
import com.tokopedia.shop.campaign.view.adapter.viewholder.WidgetConfigListener
import com.tokopedia.shop.common.data.model.ShopPageWidgetLayoutUiModel
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeBundleProductUiModel
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeProductBundleDetailUiModel
import com.tokopedia.shop.home.di.component.DaggerShopPageHomeComponent
import com.tokopedia.shop.home.di.module.ShopPageHomeModule
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.fragment.ShopPageHomeFragment
import com.tokopedia.shop.home.view.listener.ShopHomeListener
import com.tokopedia.shop.home.view.model.ShopHomeFlashSaleUiModel
import com.tokopedia.shop.home.view.model.ShopHomeNewProductLaunchCampaignUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductBundleListUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeVoucherUiModel
import com.tokopedia.shop.pageheader.presentation.fragment.NewShopPageFragment
import com.tokopedia.shop.product.view.adapter.scrolllistener.DataEndlessScrollListener
import com.tokopedia.shop_widget.thematicwidget.uimodel.ProductCardUiModel
import com.tokopedia.shop_widget.thematicwidget.uimodel.ThematicWidgetUiModel
import com.tokopedia.shop_widget.thematicwidget.viewholder.ThematicWidgetViewHolder
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

class ShopPageCampaignFragment :
    ShopPageHomeFragment(),
    WidgetConfigListener,
    ShopCampaignProductBundleParentWidgetViewHolder.Listener,
    ShopHomeListener {

    companion object {
        private const val KEY_SHOP_ID = "SHOP_ID"
        private const val KEY_IS_OFFICIAL_STORE = "IS_OFFICIAL_STORE"
        private const val KEY_IS_GOLD_MERCHANT = "IS_GOLD_MERCHANT"
        private const val KEY_SHOP_NAME = "SHOP_NAME"
        private const val KEY_SHOP_ATTRIBUTION = "SHOP_ATTRIBUTION"
        private const val KEY_SHOP_REF = "SHOP_REF"
        private const val LOAD_WIDGET_ITEM_PER_PAGE = 3
        private const val LIST_WIDGET_LAYOUT_START_INDEX = 0
        private const val CONFETTI_URL = "https://assets.tokopedia.net/asts/android/shop_page/shop_campaign_tab_confetti.json"
        private const val KEY_ENABLE_SHOP_DIRECT_PURCHASE = "ENABLE_SHOP_DIRECT_PURCHASE"

        fun createInstance(
            shopId: String,
            isOfficialStore: Boolean,
            isGoldMerchant: Boolean,
            shopName: String,
            shopAttribution: String,
            shopRef: String,
            isEnableDirectPurchase: Boolean
        ): ShopPageCampaignFragment {
            val bundle = Bundle()
            bundle.putString(KEY_SHOP_ID, shopId)
            bundle.putBoolean(KEY_IS_OFFICIAL_STORE, isOfficialStore)
            bundle.putBoolean(KEY_IS_GOLD_MERCHANT, isGoldMerchant)
            bundle.putString(KEY_SHOP_NAME, shopName)
            bundle.putString(KEY_SHOP_ATTRIBUTION, shopAttribution)
            bundle.putString(KEY_SHOP_REF, shopRef)
            bundle.putBoolean(KEY_ENABLE_SHOP_DIRECT_PURCHASE, isEnableDirectPurchase)
            return ShopPageCampaignFragment().apply {
                arguments = bundle
            }
        }
    }

    private var recyclerViewTopPadding = 0
    private var isClickToScrollToTop = false
    private var latestCompletelyVisibleItemIndex = -1
    private var listBackgroundColor: List<String> = listOf()
    private var textColor: String = ""
    private var topView: View? = null
    private var centerView: View? = null
    private var bottomView: View? = null
    private var staggeredGridLayoutManager: StaggeredGridLayoutManager? = null
    private val shopCampaignTabAdapter: ShopCampaignTabAdapter
        get() = adapter as ShopCampaignTabAdapter
    private val shopCampaignTabAdapterTypeFactory by lazy {
        val userSession = UserSession(context)
        val _shopId = arguments?.getString(KEY_SHOP_ID, "") ?: ""
        val _isMyShop = ShopUtil.isMyShop(shopId = _shopId, userSessionShopId = userSession.shopId.orEmpty())
        ShopCampaignTabAdapterTypeFactory(
            listener = this,
            onMerchantVoucherListWidgetListener = this,
            shopHomeEndlessProductListener = this,
            shopHomeCarouselProductListener = this,
            shopProductEtalaseListViewHolderListener = this,
            shopHomeCampaignNplWidgetListener = this,
            shopHomeFlashSaleWidgetListener = this,
            shopProductChangeGridSectionListener = this,
            playWidgetCoordinator = playWidgetCoordinator,
            isShowTripleDot = !_isMyShop,
            shopHomeShowcaseListWidgetListener = this,
            shopHomePlayWidgetListener = this,
            shopHomeCardDonationListener = this,
            multipleProductBundleListener = this,
            singleProductBundleListener = this,
            thematicWidgetListener = thematicWidgetProductClickListenerImpl(),
            shopHomeProductListSellerEmptyListener = this,
            widgetConfigListener = this,
            bundlingParentListener = this,
            shopHomeListener = this
        )
    }

    private var globalErrorShopPage: GlobalError? = null

    @Inject
    lateinit var shopCampaignTabTracker: ShopCampaignTabTracker

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
        shopCampaignTabAdapter.isOwner = isOwner
        shopPageHomeLayoutUiModel?.let {
            setShopHomeWidgetLayoutData(it)
        }
        setWidgetLayoutPlaceholder()
    }

    override fun setWidgetLayoutPlaceholder() {
        if (listWidgetLayout.isNotEmpty()) {
            shopCampaignTabAdapter.hideLoading()
            val shopHomeWidgetContentData =
                ShopPageHomeMapper.mapShopHomeWidgetLayoutToListShopHomeWidget(
                    listWidgetLayout,
                    isOwner,
                    isLogin,
                    isThematicWidgetShown,
                    isEnableDirectPurchase,
                    shopId
                )
            shopCampaignTabAdapter.setCampaignLayoutData(shopHomeWidgetContentData)
        }
    }

    override fun initView() {
        super.initView()
        topView = viewBinding?.topView
        centerView = viewBinding?.centerView
        bottomView = viewBinding?.bottomView
    }

    override fun onSuccessGetShopHomeWidgetContentData(mapWidgetContentData: Map<Pair<String, String>, Visitable<*>?>) {
        if (shopCampaignTabAdapter.isAllWidgetLoading()) {
            setCampaignTabBackgroundGradient()
            checkShowCampaignTabConfetti()
        }
        super.onSuccessGetShopHomeWidgetContentData(mapWidgetContentData)
    }

    private fun checkShowCampaignTabConfetti() {
        if (isShowConfetti()) {
            setConfettiAlreadyShown()
            showConfetti()
        }
    }

    private fun showConfetti() {
        (parentFragment as? NewShopPageFragment)?.setupShopPageLottieAnimation(CONFETTI_URL)
    }

    private fun isShowConfetti(): Boolean {
        return (parentFragment as? NewShopPageFragment)?.isShowConfetti().orFalse()
    }

    private fun setConfettiAlreadyShown() {
        (parentFragment as? NewShopPageFragment)?.setConfettiAlreadyShown()
    }

    override fun observeShopProductFilterParameterSharedViewModel() {}

    override fun observeShopChangeProductGridSharedViewModel() {}

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

    // region flash sale toko widget
    override fun onFlashSaleWidgetImpressed(model: ShopHomeFlashSaleUiModel, position: Int) {
        shopCampaignTabTracker.impressionShopBannerWidget(
            shopId,
            model.name,
            model.widgetId,
            ShopUtil.getActualPositionFromIndex(position),
            userId
        )
    }

    override fun onFlashSaleProductClicked(model: ShopHomeProductUiModel, widgetModel: ShopHomeFlashSaleUiModel, position: Int) {
        shopCampaignTabTracker.clickCampaignTabProduct(
            model.id.orEmpty(),
            model.name.orEmpty(),
            model.displayedPrice.toLongOrZero(),
            widgetModel.name,
            shopId,
            userId,
            widgetModel.header.title,
            ShopUtil.getActualPositionFromIndex(position)
        )
        goToPDP(model.productUrl)
    }

    override fun onFlashSaleProductImpression(
        shopHomeProductUiModel: ShopHomeProductUiModel,
        flashSaleUiModel: ShopHomeFlashSaleUiModel?,
        position: Int
    ) {
        shopCampaignTabTracker.impressionCampaignTabProduct(
            shopHomeProductUiModel.id.orEmpty(),
            shopHomeProductUiModel.name.orEmpty(),
            shopHomeProductUiModel.displayedPrice.toLongOrZero(),
            flashSaleUiModel?.name.orEmpty(),
            shopId,
            userId,
            flashSaleUiModel?.header?.title.orEmpty(),
            ShopUtil.getActualPositionFromIndex(position)
        )
    }
    // endregion

    // region npl widget
    override fun onCampaignCarouselProductItemClicked(
        parentPosition: Int,
        itemPosition: Int,
        shopHomeNewProductLaunchCampaignUiModel: ShopHomeNewProductLaunchCampaignUiModel,
        shopHomeProductViewModel: ShopHomeProductUiModel?
    ) {
        sendShopHomeWidgetClickedTracker(
            ShopPageTrackingConstant.VALUE_SHOP_DECOR_CAMPAIGN,
            shopHomeNewProductLaunchCampaignUiModel.name,
            shopHomeNewProductLaunchCampaignUiModel.widgetId,
            ShopUtil.getActualPositionFromIndex(parentPosition)
        )
        shopHomeNewProductLaunchCampaignUiModel.data?.firstOrNull()?.let {
            shopCampaignTabTracker.clickCampaignTabProduct(
                shopHomeProductViewModel?.id.orEmpty(),
                shopHomeProductViewModel?.name.orEmpty(),
                shopHomeProductViewModel?.displayedPrice.toLongOrZero(),
                it.name,
                shopId,
                userId,
                shopHomeNewProductLaunchCampaignUiModel.header.title,
                ShopUtil.getActualPositionFromIndex(itemPosition)
            )
        }
        shopHomeProductViewModel?.let {
            goToPDP(it.productUrl)
        }
    }

    override fun onCampaignCarouselProductItemImpression(
        parentPosition: Int,
        itemPosition: Int,
        shopHomeNewProductLaunchCampaignUiModel: ShopHomeNewProductLaunchCampaignUiModel,
        shopHomeProductViewModel: ShopHomeProductUiModel?
    ) {
        shopHomeNewProductLaunchCampaignUiModel.data?.firstOrNull()?.let {
            shopCampaignTabTracker.impressionCampaignTabProduct(
                shopHomeProductViewModel?.id.orEmpty(),
                shopHomeProductViewModel?.name.orEmpty(),
                shopHomeProductViewModel?.displayedPrice.toLongOrZero(),
                shopHomeNewProductLaunchCampaignUiModel.name,
                shopId,
                userId,
                shopHomeNewProductLaunchCampaignUiModel.header.title,
                ShopUtil.getActualPositionFromIndex(itemPosition)
            )
        }
    }

    override fun onImpressionCampaignNplWidget(
        position: Int,
        shopHomeNewProductLaunchCampaignUiModel: ShopHomeNewProductLaunchCampaignUiModel
    ) {
        shopCampaignTabTracker.impressionShopBannerWidget(
            shopId,
            shopHomeNewProductLaunchCampaignUiModel.name,
            shopHomeNewProductLaunchCampaignUiModel.widgetId,
            ShopUtil.getActualPositionFromIndex(position),
            userId
        )
    }

    override fun onClickCampaignBannerAreaNplWidget(
        model: ShopHomeNewProductLaunchCampaignUiModel,
        widgetPosition: Int
    ) {
        shopCampaignTabTracker.clickShopBannerWidget(
            shopId,
            model.name,
            model.widgetId,
            ShopUtil.getActualPositionFromIndex(widgetPosition),
            userId
        )
        model.data?.firstOrNull()?.let {
            context?.let { context ->
                val appLink = model.header.ctaLink
                if (appLink.isNotEmpty()) {
                    RouteManager.route(context, appLink)
                }
            }
        }
    }
    // endregion

    // region Thematic Widget
    private fun thematicWidgetProductClickListenerImpl(): ThematicWidgetViewHolder.ThematicWidgetListener = object : ThematicWidgetViewHolder.ThematicWidgetListener {

        override fun onThematicWidgetImpressListener(model: ThematicWidgetUiModel, position: Int) {
            shopCampaignTabTracker.impressionShopBannerWidget(
                shopId,
                model.name,
                model.widgetId,
                ShopUtil.getActualPositionFromIndex(position),
                userId
            )
        }

        override fun onProductCardThematicWidgetImpressListener(
            products: List<ProductCardUiModel>,
            position: Int,
            campaignId: String,
            campaignName: String,
            campaignTitle: String
        ) {
            products.firstOrNull()?.let {
                shopCampaignTabTracker.impressionCampaignTabProduct(
                    it.id.orEmpty(),
                    it.name.orEmpty(),
                    it.displayedPrice.toLongOrZero(),
                    campaignName,
                    shopId,
                    userId,
                    campaignTitle,
                    ShopUtil.getActualPositionFromIndex(position)
                )
            }
        }

        override fun onProductCardThematicWidgetClickListener(
            product: ProductCardUiModel,
            campaignId: String,
            campaignName: String,
            position: Int,
            campaignTitle: String
        ) {
            shopCampaignTabTracker.clickCampaignTabProduct(
                product.id.orEmpty(),
                product.name.orEmpty(),
                product.displayedPrice.toLongOrZero(),
                campaignName,
                shopId,
                userId,
                campaignTitle,
                ShopUtil.getActualPositionFromIndex(position)
            )
            RouteManager.route(context, product.productUrl)
        }

        override fun onProductCardSeeAllThematicWidgetClickListener(appLink: String, campaignId: String, campaignName: String) {
            shopPageHomeTracking.clickProductCardSeeAllThematicWidgetCampaign(
                campaignId = campaignId,
                campaignName = campaignName,
                shopId = shopId,
                userId = userId
            )
            RouteManager.route(context, appLink)
        }

        override fun onSeeAllThematicWidgetClickListener(appLink: String, campaignId: String, campaignName: String) {
            shopPageHomeTracking.clickSeeAllThematicWidgetCampaign(
                campaignId = campaignId,
                campaignName = campaignName,
                shopId = shopId,
                userId = userId
            )
            RouteManager.route(context, appLink)
        }

        override fun onThematicWidgetTimerFinishListener(model: ThematicWidgetUiModel?) {
            model?.apply {
                shopCampaignTabAdapter.removeWidget(this)
            }
        }
    }
    // endregion

    override fun getWidgetTextColor(): Int {
        return parseColor(textColor)
    }

    private fun setCampaignTabBackgroundGradient() {
        if (listBackgroundColor.isNotEmpty()) {
            topView?.show()
            centerView?.show()
            bottomView?.show()
            val colors = IntArray(listBackgroundColor.size)
            for (i in listBackgroundColor.indices) {
                colors[i] = parseColor(listBackgroundColor.getOrNull(i).orEmpty())
            }
            val gradient = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors)
            gradient.cornerRadius = 0f
            topView?.setBackgroundColor(parseColor(listBackgroundColor.firstOrNull().orEmpty()))
            centerView?.background = gradient
            bottomView?.setBackgroundColor(parseColor(listBackgroundColor.lastOrNull().orEmpty()))
        } else {
            topView?.hide()
            centerView?.hide()
            bottomView?.hide()
        }
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
        return object : DataEndlessScrollListener(staggeredGridLayoutManager, shopCampaignTabAdapter) {

            override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(view, dx, dy)
                val layoutManager =
                    (getRecyclerView(view)?.layoutManager as? StaggeredGridLayoutManager)
                val firstCompletelyVisibleItemPosition =
                    layoutManager?.findFirstCompletelyVisibleItemPositions(null)?.getOrNull(0)
                        .orZero()
                if (firstCompletelyVisibleItemPosition == 0 && isClickToScrollToTop) {
                    isClickToScrollToTop = false
                    (parentFragment as? NewShopPageFragment)?.expandHeader()
                }
                if (firstCompletelyVisibleItemPosition != latestCompletelyVisibleItemIndex)
                    hideScrollToTopButton()
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
            shopCampaignTabAdapter.isLoadNextHomeWidgetData(lastVisibleItemPosition)
        val shouldLoadFirstVisibleItem =
            shopCampaignTabAdapter.isLoadNextHomeWidgetData(firstVisibleItemPosition)
        if (shouldLoadLastVisibleItem || shouldLoadFirstVisibleItem) {
            val position = if (shouldLoadLastVisibleItem)
                lastVisibleItemPosition
            else
                firstVisibleItemPosition
            val listWidgetLayoutToLoad = getListWidgetLayoutToLoad(position)
            shopCampaignTabAdapter.updateShopHomeWidgetStateToLoading(listWidgetLayoutToLoad)

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
            viewModel?.getWidgetContentData(
                listWidgetLayoutToLoad.toList(),
                shopId,
                widgetUserAddressLocalData,
                isThematicWidgetShown,
                isEnableDirectPurchase
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
        (parentFragment as? NewShopPageFragment)?.hideScrollToTopButton()
    }

    private fun showScrollToTopButton() {
        (parentFragment as? NewShopPageFragment)?.showScrollToTopButton()
    }

    fun setPageBackgroundColor(listBackgroundColor: List<String>) {
        this.listBackgroundColor = listBackgroundColor
        checkIfListBackgroundColorValueIsEmpty()
    }

    @SuppressLint("ResourceType")
    private fun checkIfListBackgroundColorValueIsEmpty() {
        if (listBackgroundColor.all { it.isEmpty() }) {
            this.listBackgroundColor = getDefaultListBackgroundColor()
        }
    }

    @SuppressLint("ResourceType")
    private fun getDefaultListBackgroundColor(): List<String> {
        return listOf(
            getString(R.color.clr_dms_shop_campaign_tab_first_color),
            getString(R.color.clr_dms_shop_campaign_tab_second_color)
        )
    }

    fun setPageTextColor(textColor: String) {
        this.textColor = textColor
    }
}
