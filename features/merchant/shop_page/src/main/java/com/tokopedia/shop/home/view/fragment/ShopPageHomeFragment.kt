package com.tokopedia.shop.home.view.fragment

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalContent.PLAY_BROADCASTER_PERFORMANCE_DASHBOARD_APP_LINK
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.atc_common.domain.model.response.AddToCartBundleModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.content.common.analytic.entrypoint.PlayPerformanceDashboardEntryPointAnalytic
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.discovery.common.manager.ProductCardOptionsWishlistCallback
import com.tokopedia.discovery.common.manager.handleProductCardOptionsActivityResult
import com.tokopedia.discovery.common.manager.showProductCardOptions
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.encodeToUtf8
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.thousandFormatted
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.play.widget.analytic.global.model.PlayWidgetShopAnalyticModel
import com.tokopedia.play.widget.const.PlayWidgetConst
import com.tokopedia.play.widget.extension.stepScrollToPositionWithDelay
import com.tokopedia.play.widget.ui.PlayWidgetMediumView
import com.tokopedia.play.widget.ui.PlayWidgetView
import com.tokopedia.play.widget.ui.bottomsheet.PlayWidgetActionMenuBottomSheet
import com.tokopedia.play.widget.ui.coordinator.PlayWidgetCoordinator
import com.tokopedia.play.widget.ui.dialog.PlayWidgetDeleteDialogContainer
import com.tokopedia.play.widget.ui.dialog.PlayWidgetWatchDialogContainer
import com.tokopedia.play.widget.ui.listener.PlayWidgetListener
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.ext.hasSuccessfulTranscodedChannel
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.comparison.ComparisonListModel
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.analytic.ShopPageHomeTracking
import com.tokopedia.shop.analytic.ShopPageHomeTrackingMapper
import com.tokopedia.shop.analytic.ShopPageTrackingConstant
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.LABEL_GROUP_POSITION_FULFILLMENT
import com.tokopedia.shop.analytic.ShopPlayWidgetAnalyticListener
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageAttribution
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageProduct
import com.tokopedia.shop.analytic.model.ShopHomePersoProductComparisonWidgetImpressionTrackerModel
import com.tokopedia.shop.analytic.model.ShopHomeTerlarisWidgetTrackerDataModel
import com.tokopedia.shop.common.constant.DEFAULT_SORT_ID
import com.tokopedia.shop.common.constant.ShopCommonExtraConstant
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.common.constant.ShopPageConstant.VALUE_INT_ONE
import com.tokopedia.shop.common.constant.ShopPageLoggerConstant.Tag.SHOP_PAGE_BUYER_FLOW_TAG
import com.tokopedia.shop.common.constant.ShopPageLoggerConstant.Tag.SHOP_PAGE_HOME_TAB_BUYER_FLOW_TAG
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant
import com.tokopedia.shop.common.constant.ShopShowcaseParamConstant
import com.tokopedia.shop.common.constant.ShopShowcaseParamConstant.EXTRA_BUNDLE
import com.tokopedia.shop.common.data.mapper.ShopPageWidgetMapper
import com.tokopedia.shop.common.data.model.AffiliateAtcProductModel
import com.tokopedia.shop.common.data.model.HomeLayoutData
import com.tokopedia.shop.common.data.model.ShopPageAtcTracker
import com.tokopedia.shop.common.data.model.ShopPageProductDirectPurchaseWidgetAtcTracker
import com.tokopedia.shop.common.data.model.ShopPageWidgetUiModel
import com.tokopedia.shop.common.graphql.data.checkwishlist.CheckWishlistResult
import com.tokopedia.shop.common.util.ShopAsyncErrorException
import com.tokopedia.shop.common.util.ShopLogger
import com.tokopedia.shop.common.util.ShopPageExceptionHandler.ERROR_WHEN_GET_YOUTUBE_DATA
import com.tokopedia.shop.common.util.ShopPageExceptionHandler.logExceptionToCrashlytics
import com.tokopedia.shop.common.util.ShopPageRemoteConfigChecker
import com.tokopedia.shop.common.util.ShopProductViewGridType
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.common.util.ShopUtilExt.setAnchorViewToShopHeaderBottomViewContainer
import com.tokopedia.shop.common.util.getIndicatorCount
import com.tokopedia.shop.common.view.interfaces.InterfaceShopPageHeader
import com.tokopedia.shop.common.view.interfaces.ShopPageSharedListener
import com.tokopedia.shop.common.view.listener.InterfaceShopPageClickScrollToTop
import com.tokopedia.shop.common.view.listener.ShopProductChangeGridSectionListener
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.common.view.model.ShopProductFilterParameter
import com.tokopedia.shop.common.view.viewmodel.ShopBannerTimerSharedViewModel
import com.tokopedia.shop.common.view.viewmodel.ShopChangeProductGridSharedViewModel
import com.tokopedia.shop.common.view.viewmodel.ShopPageMiniCartSharedViewModel
import com.tokopedia.shop.common.view.viewmodel.ShopProductFilterParameterSharedViewModel
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeBundleProductUiModel
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeProductBundleDetailUiModel
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeProductBundleItemUiModel
import com.tokopedia.shop.common.widget.bundle.viewholder.MultipleProductBundleListener
import com.tokopedia.shop.common.widget.bundle.viewholder.SingleProductBundleListener
import com.tokopedia.shop.common.widget.model.ShopHomeWidgetLayout
import com.tokopedia.shop.databinding.FragmentShopPageHomeBinding
import com.tokopedia.shop.home.WidgetName
import com.tokopedia.shop.home.WidgetName.PERSO_PRODUCT_COMPARISON
import com.tokopedia.shop.home.WidgetName.PLAY_CAROUSEL_WIDGET
import com.tokopedia.shop.home.WidgetName.VIDEO
import com.tokopedia.shop.home.WidgetName.VOUCHER_STATIC
import com.tokopedia.shop.home.WidgetType.DYNAMIC
import com.tokopedia.shop.home.WidgetType.PERSONALIZATION
import com.tokopedia.shop.home.WidgetType.PROMO
import com.tokopedia.shop.home.di.component.DaggerShopPageHomeComponent
import com.tokopedia.shop.home.di.module.ShopPageHomeModule
import com.tokopedia.shop.home.util.CheckCampaignNplException
import com.tokopedia.shop.home.util.ShopBannerProductGroupWidgetTabDependencyProvider
import com.tokopedia.shop.home.util.ShopHomeShowcaseNavigationDependencyProvider
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapter
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeDisplayBannerProductHotspotViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomePersoProductComparisonViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeProductListSellerEmptyListener
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeV4TerlarisViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeVoucherViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.advance_carousel_banner.ShopHomeDisplayAdvanceCarouselBannerWidgetListener
import com.tokopedia.shop.home.view.adapter.viewholder.directpurchasebyetalase.ShopHomeDirectPurchaseByEtalaseWidgetListener
import com.tokopedia.shop.home.view.bottomsheet.ShopHomeFlashSaleTncBottomSheet
import com.tokopedia.shop.home.view.bottomsheet.ShopHomeNplCampaignTncBottomSheet
import com.tokopedia.shop.home.view.customview.directpurchase.ProductCardDirectPurchaseDataModel
import com.tokopedia.shop.home.view.listener.ShopBannerProductGroupListener
import com.tokopedia.shop.home.view.listener.ShopHomeCampaignNplWidgetListener
import com.tokopedia.shop.home.view.listener.ShopHomeCardDonationListener
import com.tokopedia.shop.home.view.listener.ShopHomeCarouselProductListener
import com.tokopedia.shop.home.view.listener.ShopHomeDisplayBannerTimerWidgetListener
import com.tokopedia.shop.home.view.listener.ShopHomeDisplayWidgetListener
import com.tokopedia.shop.home.view.listener.ShopHomeEndlessProductListener
import com.tokopedia.shop.home.view.listener.ShopHomeFlashSaleWidgetListener
import com.tokopedia.shop.home.view.listener.ShopHomeListener
import com.tokopedia.shop.home.view.listener.ShopHomePlayWidgetListener
import com.tokopedia.shop.home.view.listener.ShopHomeShowcaseListWidgetListener
import com.tokopedia.shop.home.view.listener.ShopHomeShowcaseNavigationListener
import com.tokopedia.shop.home.view.model.CarouselPlayWidgetUiModel
import com.tokopedia.shop.home.view.model.CheckCampaignNotifyMeUiModel
import com.tokopedia.shop.home.view.model.GetCampaignNotifyMeUiModel
import com.tokopedia.shop.home.view.model.NotifyMeAction
import com.tokopedia.shop.home.view.model.ShopHomeCardDonationUiModel
import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel
import com.tokopedia.shop.home.view.model.ShopHomeFlashSaleUiModel
import com.tokopedia.shop.home.view.model.ShopHomeNewProductLaunchCampaignUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeShowcaseListItemUiModel
import com.tokopedia.shop.home.view.model.ShopHomeShowcaseListSliderUiModel
import com.tokopedia.shop.home.view.model.ShopHomeVoucherUiModel
import com.tokopedia.shop.home.view.model.ShopPageLayoutUiModel
import com.tokopedia.shop.home.view.model.ShopWidgetDisplayBannerProductHotspotUiModel
import com.tokopedia.shop.home.view.model.ShopWidgetDisplayBannerTimerUiModel
import com.tokopedia.shop.home.view.model.StatusCampaign
import com.tokopedia.shop.home.view.model.banner_product_group.BannerProductGroupUiModel
import com.tokopedia.shop.home.view.model.banner_product_group.appearance.ProductItemType
import com.tokopedia.shop.home.view.model.banner_product_group.appearance.VerticalBannerItemType
import com.tokopedia.shop.home.view.model.showcase_navigation.Showcase
import com.tokopedia.shop.home.view.model.showcase_navigation.ShowcaseNavigationUiModel
import com.tokopedia.shop.home.view.model.showcase_navigation.appearance.ShopHomeShowcaseNavigationBannerWidgetAppearance
import com.tokopedia.shop.home.view.model.viewholder.ShopDirectPurchaseByEtalaseUiModel
import com.tokopedia.shop.home.view.viewmodel.ShopHomeViewModel
import com.tokopedia.shop.pageheader.presentation.activity.ShopPageHeaderActivity
import com.tokopedia.shop.pageheader.presentation.fragment.ShopPageHeaderFragment
import com.tokopedia.shop.pageheader.presentation.fragment.ShopPageHeaderFragmentV2
import com.tokopedia.shop.pageheader.presentation.listener.ShopPageHeaderPerformanceMonitoringListener
import com.tokopedia.shop.pageheader.util.ShopPageHeaderTabName
import com.tokopedia.shop.product.data.model.ShopProduct
import com.tokopedia.shop.product.util.StaggeredGridLayoutManagerWrapper
import com.tokopedia.shop.product.view.activity.ShopProductListResultActivity
import com.tokopedia.shop.product.view.adapter.scrolllistener.DataEndlessScrollListener
import com.tokopedia.shop.product.view.datamodel.ShopProductSortFilterUiModel
import com.tokopedia.shop.product.view.viewholder.ShopProductSortFilterViewHolder
import com.tokopedia.shop.product.view.widget.StickySingleHeaderView
import com.tokopedia.shop.sort.view.activity.ShopProductSortActivity
import com.tokopedia.shop_widget.thematicwidget.uimodel.ProductCardUiModel
import com.tokopedia.shop_widget.thematicwidget.uimodel.ThematicWidgetUiModel
import com.tokopedia.shop_widget.thematicwidget.viewholder.ThematicWidgetViewHolder
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.wishlistcommon.util.AddRemoveWishlistV2Handler
import com.tokopedia.youtube_common.data.model.YoutubeVideoDetailModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.math.min

open class ShopPageHomeFragment :
    BaseListFragment<Visitable<*>, AdapterTypeFactory>(),
    ShopHomeDisplayWidgetListener,
    ShopHomeVoucherViewHolder.ShopHomeVoucherViewHolderListener,
    ShopHomeEndlessProductListener,
    ShopProductSortFilterViewHolder.ShopProductSortFilterViewHolderListener,
    ShopHomeCarouselProductListener,
    ShopHomeCampaignNplWidgetListener,
    ShopHomeFlashSaleWidgetListener,
    ShopProductChangeGridSectionListener,
    SortFilterBottomSheet.Callback,
    PlayWidgetListener,
    ShopHomeShowcaseListWidgetListener,
    InterfaceShopPageClickScrollToTop,
    ShopHomePlayWidgetListener,
    ShopHomeCardDonationListener,
    MultipleProductBundleListener,
    SingleProductBundleListener,
    ShopHomeProductListSellerEmptyListener,
    ShopHomeListener,
    ShopHomePersoProductComparisonViewHolder.ShopHomePersoProductComparisonViewHolderListener,
    ShopHomeDisplayBannerTimerWidgetListener,
    ShopHomeDisplayBannerProductHotspotViewHolder.Listener,
    ShopHomeShowcaseNavigationListener,
    ShopHomeShowcaseNavigationDependencyProvider,
    ShopHomeV4TerlarisViewHolder.ShopHomeV4TerlarisViewHolderListener,
    ShopBannerProductGroupWidgetTabDependencyProvider,
    ShopBannerProductGroupListener,
    ShopHomeDisplayAdvanceCarouselBannerWidgetListener,
    ShopHomeDirectPurchaseByEtalaseWidgetListener {

    companion object {
        const val KEY_SHOP_ID = "SHOP_ID"
        const val KEY_IS_OFFICIAL_STORE = "IS_OFFICIAL_STORE"
        const val KEY_IS_GOLD_MERCHANT = "IS_GOLD_MERCHANT"
        const val KEY_SHOP_NAME = "SHOP_NAME"
        const val KEY_SHOP_ATTRIBUTION = "SHOP_ATTRIBUTION"
        const val KEY_SHOP_REF = "SHOP_REF"
        private const val KEY_ENABLE_SHOP_DIRECT_PURCHASE = "ENABLE_SHOP_DIRECT_PURCHASE"
        const val SAVED_SHOP_PRODUCT_FILTER_PARAMETER = "SAVED_SHOP_PRODUCT_FILTER_PARAMETER"
        private const val QUERY_PARAM_EXT_PARAM = "extParam"
        private const val QUERY_PARAM_TAB = "tab"
        private const val REQUEST_CODE_ETALASE = 206
        private const val REQUEST_CODE_SORT = 301
        private const val REQUEST_CODE_USER_LOGIN_PLAY_WIDGET_REMIND_ME = 256
        private const val REQUEST_CODE_USER_LOGIN = 101
        private const val REQUEST_CODE_DONATION_DETAIL = 123
        const val REGISTER_VALUE = "REGISTER"
        const val UNREGISTER_VALUE = "UNREGISTER"
        const val NPL_REMIND_ME_CAMPAIGN_ID = "NPL_REMIND_ME_CAMPAIGN_ID"
        const val FLASH_SALE_REMIND_ME_CAMPAIGN_ID = "FLASH_SALE_REMIND_ME_CAMPAIGN_ID"
        private const val CUSTOMER_APP_PACKAGE = "com.tokopedia.tkpd"
        const val PLAY_WIDGET_NEWLY_BROADCAST_SCROLL_DELAY = 40L
        private const val LOAD_WIDGET_ITEM_PER_PAGE = 3
        private const val LIST_WIDGET_LAYOUT_START_INDEX = 0
        private const val MIN_BUNDLE_SIZE = 1
        private const val DEVICE_WIDTH_540 = 540
        private const val DEVICE_WIDTH_1080 = 1080
        private const val BG_PATTERN_SIZE_MULTIPLIER_BELOW_540_WIDTH_DEVICE = 0.5f
        private const val BG_PATTERN_SIZE_MULTIPLIER_BELOW_1080_WIDTH_DEVICE = 0.75f
        private const val BG_PATTERN_SIZE_MULTIPLIER_DEFAULT_DEVICE = 1f
        private const val CAMPAIGN_TAB_APP_LINK_LAST_SEGMENT = "campaign"

        fun createInstance(
            shopId: String,
            isOfficialStore: Boolean,
            isGoldMerchant: Boolean,
            shopName: String,
            shopAttribution: String,
            shopRef: String,
            isEnableDirectPurchase: Boolean
        ): ShopPageHomeFragment {
            val bundle = Bundle()
            bundle.putString(KEY_SHOP_ID, shopId)
            bundle.putBoolean(KEY_IS_OFFICIAL_STORE, isOfficialStore)
            bundle.putBoolean(KEY_IS_GOLD_MERCHANT, isGoldMerchant)
            bundle.putString(KEY_SHOP_NAME, shopName)
            bundle.putString(KEY_SHOP_ATTRIBUTION, shopAttribution)
            bundle.putString(KEY_SHOP_REF, shopRef)
            bundle.putBoolean(KEY_ENABLE_SHOP_DIRECT_PURCHASE, isEnableDirectPurchase)
            return ShopPageHomeFragment().apply {
                arguments = bundle
            }
        }
    }

    private var threeDotsClickShopProductViewModel: ShopHomeProductUiModel? = null
    private var threeDotsClickShopCarouselProductUiModel: ShopHomeCarousellProductUiModel? = null

    @Inject
    lateinit var shopPageHomeTracking: ShopPageHomeTracking

    @Inject
    lateinit var shopPlayWidgetAnalytic: ShopPlayWidgetAnalyticListener

    @Inject
    lateinit var playPerformanceDashboardEntryPointAnalytic: PlayPerformanceDashboardEntryPointAnalytic

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var dispatcher: CoroutineDispatchers

    @Inject
    lateinit var trackingQueue: TrackingQueue

    var viewModel: ShopHomeViewModel? = null
    var extParam: String = ""
    var shopId: String = ""
    var isOfficialStore: Boolean = false
    var isGoldMerchant: Boolean = false
    var shopName: String = ""
    var shopAttribution: String = ""
    var shopRef: String = ""
    var isThematicWidgetShown: Boolean = false
    var isEnableDirectPurchase: Boolean = false
    private var productListName: String = ""
    private var sortId
        get() = shopProductFilterParameter?.getSortId().orEmpty()
        set(value) {
            shopProductFilterParameter?.setSortId(value)
        }
    private val sortName
        get() = viewModel?.getSortNameById(sortId).orEmpty()
    val userId: String
        get() = viewModel?.userId.orEmpty()
    private var recyclerViewTopPadding = 0
    private var shopProductFilterParameterSharedViewModel: ShopProductFilterParameterSharedViewModel? = null
    private var shopChangeProductGridSharedViewModel: ShopChangeProductGridSharedViewModel? = null
    private var shopPageMiniCartSharedViewModel: ShopPageMiniCartSharedViewModel? = null
    var shopReminderButtonStatusSharedVieModel: ShopBannerTimerSharedViewModel? = null
    private var remoteConfig: RemoteConfig? = null
    private var sortFilterBottomSheet: SortFilterBottomSheet? = null
    private var shopProductFilterParameter: ShopProductFilterParameter? = ShopProductFilterParameter()
    private var isClickToScrollToTop = false
    private var latestCompletelyVisibleItemIndex = -1

    val isLogin: Boolean
        get() = viewModel?.isLogin ?: false
    val isOwner: Boolean
        get() = ShopUtil.isMyShop(shopId, viewModel?.userSessionShopId ?: "")
    protected var shopPageHomeLayoutUiModel: ShopPageLayoutUiModel? = null
    private val customDimensionShopPage: CustomDimensionShopPage by lazy {
        CustomDimensionShopPage.create(shopId, isOfficialStore, isGoldMerchant)
    }
    private var staggeredGridLayoutManager: StaggeredGridLayoutManager? = null
    private val shopHomeAdapter: ShopHomeAdapter?
        get() = adapter as? ShopHomeAdapter
    private val shopHomeWidgetCarouselPositionSavedState = SparseIntArray()
    private var listHomeTabBackgroundColor: List<String> = listOf()
    private var homeTabBackgroundPatternImage: String = ""
    private var homeTabLottieUrl: String = ""
    private var topView: View? = null
    private var centerView: View? = null
    private var bottomView: View? = null
    private var stickyHeaderView: StickySingleHeaderView? = null
    private val imageBackgroundPattern: ImageUnify? by lazy {
        viewBinding?.imageBackgroundPattern
    }

    private val shopHomeAdapterTypeFactory by lazy {
        val userSession = UserSession(context)
        val _shopId = arguments?.getString(KEY_SHOP_ID, "") ?: ""
        val _isMyShop = ShopUtil.isMyShop(shopId = _shopId, userSessionShopId = userSession.shopId.orEmpty())
        ShopHomeAdapterTypeFactory(
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
            shopHomeListener = this,
            shopPersoProductComparisonListener = this,
            shopHomeDisplayBannerTimerWidgetListener = this,
            shopHomeDisplayBannerProductHotspotListener = this,
            shopHomeShowcaseNavigationListener = this,
            shopHomeShowcaseNavigationDependencyProvider = this,
            shopHomeV4TerlarisViewHolderListener = this,
            shopBannerProductGroupListener = this,
            shopBannerProductGroupWidgetTabDependencyProvider = this,
            shopHomeDisplayAdvanceCarouselBannerWidgetListener = this,
            shopHomeDirectPurchaseByEtalaseWidgetListener = this
        )
    }

    private val widgetDeleteDialogContainer by lazy {
        PlayWidgetDeleteDialogContainer(object : PlayWidgetDeleteDialogContainer.Listener {
            override fun onDeleteButtonClicked(channelId: String) {
                shopPlayWidgetAnalytic.onClickDialogDeleteChannel(channelId)
                deleteChannel(channelId)
            }
        })
    }

    private val widgetWatchDialogContainer by lazy { PlayWidgetWatchDialogContainer() }

    lateinit var playWidgetCoordinator: PlayWidgetCoordinator

    private val viewJob = SupervisorJob()

    protected val viewScope = object : CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = viewJob + dispatcher.main
    }
    var isLoadInitialData = false
    private var gridType: ShopProductViewGridType = ShopProductViewGridType.SMALL_GRID
    private var initialProductListData: ShopProduct.GetShopProduct? = null
    private var globalErrorShopPage: GlobalError? = null

    // TODO need to move this one to viewmodel in the future
    var listWidgetLayout = mutableListOf<ShopPageWidgetUiModel>()
    var initialLayoutData = mutableListOf<ShopPageWidgetUiModel>()
    val viewBinding: FragmentShopPageHomeBinding? by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        setDataFromAppLinkQueryParam()
        if (isShopHomeTabSelected()) {
            startMonitoringPltCustomMetric(ShopPagePerformanceConstant.PltConstant.SHOP_TRACE_HOME_V2_PREPARE)
        }
        getIntentData()
        setupPlayWidget()
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            shopProductFilterParameter = it.getParcelable(SAVED_SHOP_PRODUCT_FILTER_PARAMETER)
        }
        remoteConfig = FirebaseRemoteConfigImpl(context)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopHomeViewModel::class.java)
        shopProductFilterParameterSharedViewModel = ViewModelProviders.of(requireActivity())
            .get(ShopProductFilterParameterSharedViewModel::class.java)
        shopChangeProductGridSharedViewModel =
            ViewModelProvider(requireActivity()).get(ShopChangeProductGridSharedViewModel::class.java)
        shopPageMiniCartSharedViewModel = ViewModelProviders.of(requireActivity()).get(
            ShopPageMiniCartSharedViewModel::class.java
        )
        shopReminderButtonStatusSharedVieModel = ViewModelProviders.of(requireActivity()).get(
            ShopBannerTimerSharedViewModel::class.java
        )
        customDimensionShopPage.updateCustomDimensionData(shopId, isOfficialStore, isGoldMerchant)
        staggeredGridLayoutManager = StaggeredGridLayoutManagerWrapper(
            context?.resources?.getInteger(R.integer.span_count_small_grid).orZero(),
            StaggeredGridLayoutManager.VERTICAL
        )
        setupPlayWidgetAnalyticListener()
        isThematicWidgetShown = getRemoteConfigEnableThematicWidgetShop()
    }

    private fun isShopHomeTabSelected(): Boolean {
        return (getRealParentFragment() as? InterfaceShopPageHeader)?.isTabSelected(this::class.java)
            ?: false
    }

    private fun startMonitoringPltRenderPage() {
        (activity as? ShopPageHeaderPerformanceMonitoringListener)?.let { shopPageActivity ->
            shopPageActivity.getShopPageLoadTimePerformanceCallback()?.let {
                shopPageActivity.startMonitoringPltRenderPage(it)
            }
        }
    }

    private fun stopMonitoringPltRenderPage() {
        (activity as? ShopPageHeaderPerformanceMonitoringListener)?.let { shopPageActivity ->
            shopPageActivity.getShopPageLoadTimePerformanceCallback()?.let {
                shopPageActivity.stopMonitoringPltRenderPage(it)
            }
        }
    }

    private fun startMonitoringPltCustomMetric(tag: String) {
        (activity as? ShopPageHeaderPerformanceMonitoringListener)?.let { shopPageActivity ->
            shopPageActivity.getShopPageLoadTimePerformanceCallback()?.let {
                shopPageActivity.startCustomMetric(it, tag)
            }
        }
    }

    private fun stopMonitoringPltCustomMetric(tag: String) {
        (activity as? ShopPageHeaderPerformanceMonitoringListener)?.let { shopPageActivity ->
            shopPageActivity.getShopPageLoadTimePerformanceCallback()?.let {
                shopPageActivity.stopCustomMetric(it, tag)
            }
        }
    }

    private fun invalidateMonitoringPlt() {
        (activity as? ShopPageHeaderPerformanceMonitoringListener)?.let { shopPageActivity ->
            shopPageActivity.getShopPageLoadTimePerformanceCallback()?.let {
                shopPageActivity.invalidateMonitoringPlt(it)
            }
        }
    }

    private fun stopMonitoringPerformance() {
        (activity as? ShopPageHeaderActivity)?.stopShopHomeTabPerformanceMonitoring()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shop_page_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        configStickyHeaderView()
        getRecyclerView(view)?.let {
            it.clearOnScrollListeners()
            it.layoutManager = staggeredGridLayoutManager
            endlessRecyclerViewScrollListener = createEndlessRecyclerViewListener()
            it.addOnScrollListener(endlessRecyclerViewScrollListener)
            it.itemAnimator = null
        }
        observeShopProductFilterParameterSharedViewModel()
        observeShopChangeProductGridSharedViewModel()
        observeLiveData()
        observeShopHomeWidgetContentData()
        observeShopPageMiniCartSharedViewModel()
        observeShopReminderButtonStatusSharedVieModel()
        observeLatestShopHomeWidgetLayoutData()
        observeShowHomeTabConfetti()
        observeProductComparisonLiveData()
        observeBannerTimerRemindMeStatusData()
        observeCheckBannerTimerRemindMeStatusData()
        observeHomeWidgetListVisitable()
        observeUpdatedBannerTimerUiModelData()
        observeDirectPurchaseProductWidgetAtcResult()
        isLoadInitialData = true
    }

    private fun configStickyHeaderView() {
        if (isOverrideTheme()) {
            stickyHeaderView?.setShapeBackgroundColorIntValue(
                ShopUtil.parseColorFromHexString(getBodyBackgroundHexColor())
            )
        }
    }

    private fun getBodyBackgroundHexColor(): String {
        return (getRealParentFragment() as? InterfaceShopPageHeader)?.getBodyBackgroundHexColor().orEmpty()
    }

    private fun observeDirectPurchaseProductWidgetAtcResult() {
        viewModel?.directPurchaseProductWidgetAtcResult?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    showToastSuccess(
                        it.data.data.message.joinToString(separator = ", "),
                        getString(R.string.see_label)
                    ) {
                        goToCart()
                    }
                }
                is Fail -> {
                    showErrorToast(it.throwable.message.orEmpty())
                }
            }
        }
    }

    private fun observeUpdatedBannerTimerUiModelData() {
        viewModel?.updatedBannerTimerUiModelData?.observe(viewLifecycleOwner) { bannerTimerUiModel ->
            bannerTimerUiModel?.let {
                shopReminderButtonStatusSharedVieModel?.updateSharedBannerTimerData(it)
            }
        }
    }

    private fun observeShopReminderButtonStatusSharedVieModel() {
        shopReminderButtonStatusSharedVieModel?.sharedBannerTimerUiModel?.observe(viewLifecycleOwner) { bannerTimerUiModel ->
            if (shopHomeAdapter?.isLoading == false && getSelectedFragment() != this) {
                bannerTimerUiModel?.let {
                    viewModel?.updateBannerTimerWidgetUiModel(
                        shopHomeAdapter?.getNewVisitableItems().orEmpty().toMutableList(),
                        it
                    )
                }
            }
        }
    }

    private fun observeCheckBannerTimerRemindMeStatusData() {
        viewModel?.checkBannerTimerRemindMeStatusData?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    if (it.data.success) {
                        onSuccessCheckBannerTimerNotifyMe(it.data)
                    } else {
                        onFailCheckBannerTimerNotifyMe(it.data.errorMessage)
                    }
                }
                is Fail -> {
                    (it.throwable as? CheckCampaignNplException)?.let { checkCampaignException ->
                        val errorMessage =
                            ErrorHandler.getErrorMessage(context, checkCampaignException)
                        onFailCheckBannerTimerNotifyMe(
                            errorMessage
                        )
                    }
                }
            }
        }
    }

    protected open fun onFailCheckBannerTimerNotifyMe(errorMessage: String) {
        view?.let {
            Toaster.build(
                it,
                errorMessage,
                Toaster.LENGTH_LONG,
                Toaster.TYPE_ERROR,
                getString(R.string.shop_string_ok)
            ).setAnchorViewToShopHeaderBottomViewContainer(getShopHeaderBottomViewContainer()).show()
        }
        viewModel?.toggleBannerTimerRemindMe(
            shopHomeAdapter?.getNewVisitableItems().orEmpty().toMutableList(),
            isRemindMe = false,
            isClickRemindMe = false
        )
    }

    protected open fun onSuccessCheckBannerTimerNotifyMe(data: CheckCampaignNotifyMeUiModel) {
        val isRegisterCampaign = data.action.equals(
            NotifyMeAction.REGISTER.action,
            ignoreCase = true
        )
        viewModel?.toggleBannerTimerRemindMe(
            shopHomeAdapter?.getNewVisitableItems().orEmpty().toMutableList(),
            isRegisterCampaign,
            true
        )
        view?.let {
            Toaster.build(
                it,
                data.message,
                Snackbar.LENGTH_LONG,
                Toaster.TYPE_NORMAL,
                getString(R.string.shop_string_ok)
            ) {
                shopPageHomeTracking.toasterActivationClickOk(isOwner, customDimensionShopPage)
            }.setAnchorViewToShopHeaderBottomViewContainer(getShopHeaderBottomViewContainer()).show()
        }
    }

    private fun observeBannerTimerRemindMeStatusData() {
        viewModel?.bannerTimerRemindMeStatusData?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    onSuccessGetBannerTimerRemindMeStatusData(it.data)
                }
                else -> {
                    // no-op
                }
            }
        }
    }

    private fun observeHomeWidgetListVisitable() {
        viewModel?.homeWidgetListVisitable?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    shopHomeAdapter?.submitList(it.data)
                }
                is Fail -> {}
            }
        }
    }

    private fun observeLatestShopHomeWidgetLayoutData() {
        viewModel?.latestShopHomeWidgetLayoutData?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    getRecyclerView(view)?.visible()
                    setShopLayoutData(it.data)
                }
                is Fail -> {
                    onErrorGetLatestShopHomeWidgetLayoutData(it.throwable)
                }
            }
        }
    }

    private fun onErrorGetLatestShopHomeWidgetLayoutData(throwable: Throwable) {
        globalErrorShopPage?.visible()
        if (throwable is MessageErrorException) {
            globalErrorShopPage?.setType(GlobalError.SERVER_ERROR)
        } else {
            globalErrorShopPage?.setType(GlobalError.NO_CONNECTION)
        }
        globalErrorShopPage?.errorSecondaryAction?.show()
        globalErrorShopPage?.setOnClickListener {
            globalErrorShopPage?.errorSecondaryAction?.hide()
            getLatestShopHomeWidgetLayoutData()
        }
        getRecyclerView(view)?.hide()
    }

    private fun observeShowHomeTabConfetti() {
        viewModel?.isShowHomeTabConfettiLiveData?.observe(
            viewLifecycleOwner,
            Observer {
                if (it) {
                    setConfettiAlreadyShown()
                    showConfetti()
                }
            }
        )
    }

    private fun observeProductComparisonLiveData() {
        viewModel?.productComparisonLiveData?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    val listRecommendationItem = it.data.recommendationWidget?.recommendationItemList.orEmpty()
                    if (listRecommendationItem.isNotEmpty()) {
                        shopHomeAdapter?.setProductComparisonData(it.data)
                    } else {
                        shopHomeAdapter?.removeProductComparisonWidget()
                    }
                }
                is Fail -> {
                    shopHomeAdapter?.getPersoProductComparisonWidgetUiModel()?.let { uiModel ->
                        shopHomeAdapter?.setProductComparisonData(uiModel.copy(isError = true))
                    }
                }
            }
        }
    }

    open fun initView() {
        globalErrorShopPage = viewBinding?.globalErrorShopPage
        topView = viewBinding?.topView
        centerView = viewBinding?.centerView
        bottomView = viewBinding?.bottomView
        stickyHeaderView = viewBinding?.stickySingleHeaderView
    }

    protected open fun observeShopHomeWidgetContentData() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel?.shopHomeWidgetContentData?.collect {
                stopMonitoringPltCustomMetric(ShopPagePerformanceConstant.PltConstant.SHOP_TRACE_HOME_V2_MIDDLE)
                startMonitoringPltCustomMetric(ShopPagePerformanceConstant.PltConstant.SHOP_TRACE_HOME_V2_RENDER)
                startMonitoringPltRenderPage()
                when (it) {
                    is Success -> {
                        onSuccessGetShopHomeWidgetContentData(it.data)
                        checkShowConfetti()
                        setHomeTabBackgroundGradient()
                        setFestivityRvDecoration()
                        setHomeTabBackgroundPattern()
                    }
                    is Fail -> {
                        val throwable = it.throwable
                        val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
                        if (throwable is ShopAsyncErrorException) {
                            val actionName = when (throwable.asyncQueryType) {
                                ShopAsyncErrorException.AsyncQueryType.SHOP_PAGE_GET_LAYOUT_V2 -> {
                                    ShopLogger.SHOP_EMBRACE_BREADCRUMB_ACTION_FAIL_GET_SHOP_PAGE_GET_LAYOUT_V2
                                }
                                else -> {
                                    ""
                                }
                            }
                            sendEmbraceBreadCrumbLogger(
                                actionName,
                                shopId,
                                throwable.stackTraceToString()
                            )
                        }
                        if (!ShopUtil.isExceptionIgnored(throwable)) {
                            ShopUtil.logShopPageP2BuyerFlowAlerting(
                                tag = SHOP_PAGE_BUYER_FLOW_TAG,
                                functionName = this@ShopPageHomeFragment::observeShopHomeWidgetContentData.name,
                                liveDataName = ShopHomeViewModel::shopHomeWidgetContentData.name,
                                userId = userId,
                                shopId = shopId,
                                shopName = shopName,
                                errorMessage = errorMessage,
                                stackTrace = Log.getStackTraceString(throwable),
                                errType = SHOP_PAGE_HOME_TAB_BUYER_FLOW_TAG
                            )
                        }
                        showErrorToast(errorMessage)
                    }
                }
                getRecyclerView(view)?.viewTreeObserver?.addOnGlobalLayoutListener(object :
                        ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            stopMonitoringPltCustomMetric(ShopPagePerformanceConstant.PltConstant.SHOP_TRACE_HOME_V2_RENDER)
                            stopMonitoringPltRenderPage()
                            stopMonitoringPerformance()
                            view?.let { view ->
                                getRecyclerView(view)?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                            }
                        }
                    })
            }

            viewModel?.shopHomeWidgetContentDataError?.collect {
                shopHomeAdapter?.removeShopHomeWidget(it)
            }
        }
    }

    private fun setFestivityRvDecoration() {
        if (shopHomeAdapter?.anyFestivityOnShopHomeWidget() == true) {
            getRecyclerView(view)?.let {
                if (it.itemDecorationCount == Int.ZERO) {
                    context?.let { ctx ->
                        it.addItemDecoration(ShopFestivityRvItemDecoration(ctx))
                    }
                }
            }
        }
    }

    private fun setHomeTabBackgroundPattern() {
        imageBackgroundPattern?.apply {
            if (homeTabBackgroundPatternImage.isNotEmpty()) {
                show()
                loadBackgroundPatternImage()
            } else {
                hide()
            }
        }
    }

    private fun loadBackgroundPatternImage() {
        context?.let {
            Glide.with(this)
                .asBitmap()
                .load(homeTabBackgroundPatternImage)
                .into(object : CustomTarget<Bitmap?>() {
                    override fun onLoadCleared(placeholder: Drawable?) {}
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap?>?
                    ) {
                        val sizeMultiplier = getBgPatternSizeMultiplier()
                        val resizeBgPattern = getResizeBgPattern(sizeMultiplier, resource)
                        setBgPatternImage(resizeBgPattern)
                    }
                })
        }
    }

    private fun setBgPatternImage(resizeBgPattern: Bitmap?) {
        context?.let {
            val bitmapDrawable = BitmapDrawable(
                it.resources,
                resizeBgPattern
            )
            bitmapDrawable.setTileModeXY(
                Shader.TileMode.REPEAT,
                Shader.TileMode.REPEAT
            )
            imageBackgroundPattern?.setImageDrawable(bitmapDrawable)
        }
    }

    private fun getBgPatternSizeMultiplier(): Float {
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        return if (screenWidth <= DEVICE_WIDTH_540) {
            BG_PATTERN_SIZE_MULTIPLIER_BELOW_540_WIDTH_DEVICE
        } else if (screenWidth < DEVICE_WIDTH_1080) {
            BG_PATTERN_SIZE_MULTIPLIER_BELOW_1080_WIDTH_DEVICE
        } else {
            BG_PATTERN_SIZE_MULTIPLIER_DEFAULT_DEVICE
        }
    }

    private fun getResizeBgPattern(sizeMultiplier: Float, resource: Bitmap): Bitmap? {
        val resourceWidth = resource.width
        val resourceHeight = resource.height
        return Bitmap.createScaledBitmap(
            resource,
            (resourceWidth * sizeMultiplier).toInt(),
            (resourceHeight * sizeMultiplier).toInt(),
            true
        )
    }

    open fun onSuccessGetShopHomeWidgetContentData(mapWidgetContentData: Map<Pair<String, String>, Visitable<*>?>) {
        shopHomeAdapter?.updateShopHomeWidgetContentData(mapWidgetContentData)
        checkProductWidgetWishListStatus(mapWidgetContentData.values.toList())
        checkCampaignNplWidgetRemindMeStatus(mapWidgetContentData.values.toList())
        checkFlashSaleWidgetRemindMeStatus(mapWidgetContentData.values.toList())
        checkBannerTimerWidgetRemindMeStatus(mapWidgetContentData.values.toList())
    }

    private fun checkBannerTimerWidgetRemindMeStatus(listWidgetContentData: List<Visitable<*>?>) {
        if (isLogin) {
            listWidgetContentData
                .filterIsInstance<ShopWidgetDisplayBannerTimerUiModel>()
                .firstOrNull().let { bannerTimerWidget ->
                    if (bannerTimerWidget?.data?.status == StatusCampaign.UPCOMING) {
                        viewModel?.getBannerTimerRemindMeStatus(bannerTimerWidget.getCampaignId())
                    }
                }
        }
    }

    private fun observeShopChangeProductGridSharedViewModel() {
        shopChangeProductGridSharedViewModel?.sharedProductGridType?.observe(
            viewLifecycleOwner,
            Observer {
                if (shopHomeAdapter?.isLoading == false) {
                    gridType = it
                    changeProductListGridView(it)
                }
            }
        )
    }

    private fun observeShopPageMiniCartSharedViewModel() {
        shopPageMiniCartSharedViewModel?.miniCartSimplifiedData?.observe(viewLifecycleOwner) {
            viewModel?.setMiniCartData(it)
            val listWidgetData = shopHomeAdapter?.data.orEmpty().toMutableList()
            if (listWidgetData.isNotEmpty()) {
                viewModel?.getShopWidgetDataWithUpdatedQuantity(listWidgetData)
            }
        }
    }

    override fun getRecyclerViewResourceId(): Int {
        return R.id.recycler_view
    }

    override fun callInitialLoadAutomatically(): Boolean {
        return false
    }

    private fun observeShopProductFilterParameterSharedViewModel() {
        shopProductFilterParameterSharedViewModel?.sharedShopProductFilterParameter?.observe(
            viewLifecycleOwner,
            Observer {
                if (shopHomeAdapter?.isLoading == false && getSelectedFragment() != this) {
                    shopProductFilterParameter = it
                    changeSortData(sortId)
                }
            }
        )
    }

    protected fun getSelectedFragment(): Fragment? {
        return if (ShopUtil.isEnableShopPageReImagined()) {
            (getRealParentFragment() as? ShopPageHeaderFragmentV2)?.getSelectedFragmentInstance()
        } else {
            (getRealParentFragment() as? ShopPageHeaderFragment)?.getSelectedFragmentInstance()
        }
    }

    override fun onResume() {
        loadInitialDataAfterOnViewCreated()
        playWidgetOnVisibilityChanged(isViewResumed = true)
        super.onResume()
        resumeSliderBannerAutoScroll()
        checkShowScrollToTopButton()
    }

    protected open fun resumeSliderBannerAutoScroll() {
        shopHomeAdapter?.resumeSliderBannerAutoScroll()
    }

    private fun checkShowScrollToTopButton() {
        if (isShowScrollToTopButton()) {
            showScrollToTopButton()
        } else {
            hideScrollToTopButton()
        }
    }

    private fun loadInitialDataAfterOnViewCreated() {
        if (isLoadInitialData) {
            loadInitialData()
            isLoadInitialData = false
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        playWidgetOnVisibilityChanged(
            isUserVisibleHint = isVisibleToUser
        )
    }

    override fun onPause() {
        playWidgetOnVisibilityChanged(isViewResumed = false)
        super.onPause()
        shopPageHomeTracking.sendAllTrackingQueue()
        pauseSliderBannerAutoScroll()
        invalidateMonitoringPlt()
    }

    protected open fun pauseSliderBannerAutoScroll() {
        shopHomeAdapter?.pauseSliderBannerAutoScroll()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::playWidgetCoordinator.isInitialized) {
            playWidgetCoordinator.onDestroy()
        }
        viewJob.cancelChildren()
        Toaster.onCTAClick = View.OnClickListener { }
    }

    override fun onDestroy() {
        viewModel?.checkWishlistData?.removeObservers(this)
        viewModel?.bottomSheetFilterLiveData?.removeObservers(this)
        viewModel?.shopProductFilterCountLiveData?.removeObservers(this)
        viewModel?.flush()
        shopProductFilterParameterSharedViewModel?.sharedShopProductFilterParameter?.removeObservers(this)
        shopChangeProductGridSharedViewModel?.sharedProductGridType?.removeObservers(this)
        shopPageMiniCartSharedViewModel?.miniCartSimplifiedData?.removeObservers(this)
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(SAVED_SHOP_PRODUCT_FILTER_PARAMETER, shopProductFilterParameter)
    }

    override fun loadInitialData() {
        shopHomeAdapter?.showLoading()
        getRecyclerView(view)?.visible()
        recyclerViewTopPadding = getRecyclerView(view)?.paddingTop ?: 0
        globalErrorShopPage?.hide()
        shopHomeAdapter?.isOwner = isOwner
        stopMonitoringPltCustomMetric(ShopPagePerformanceConstant.PltConstant.SHOP_TRACE_HOME_V2_PREPARE)
        startMonitoringPltCustomMetric(ShopPagePerformanceConstant.PltConstant.SHOP_TRACE_HOME_V2_MIDDLE)
        checkShopLayout()
    }

    protected open fun checkShopLayout() {
        shopPageHomeLayoutUiModel.let {
            if (it != null) {
                shopPageHomeTracking.sendUserViewHomeTabWidgetTracker(
                    it.masterLayoutId,
                    shopId
                )
                shopHomeAdapter?.hideLoading()
                setShopLayoutData(it)
                setWidgetLayoutPlaceholder()
            } else {
                getLatestShopHomeWidgetLayoutData()
            }
        }
    }

    open fun setWidgetLayoutPlaceholder() {
        val shopHomeWidgetContentData = ShopPageHomeMapper.mapShopHomeWidgetLayoutToListShopHomeWidget(
            listWidgetLayout,
            isOwner,
            isLogin,
            isThematicWidgetShown,
            isEnableDirectPurchase,
            shopId,
            isOverrideTheme(),
            getShopPageColorSchema()
        )
        if (shopHomeWidgetContentData.isNotEmpty()) {
            shopHomeAdapter?.setHomeLayoutData(shopHomeWidgetContentData)
        } else {
            shopHomeAdapter?.addProductGridListPlaceHolder()
        }
    }

    private fun setDataFromAppLinkQueryParam() {
        activity?.intent?.data?.run {
            val uri = toString()
            val params = UriUtil.uriQueryParamsToMap(uri)
            if (params.isNotEmpty()) {
                extParam = params[QUERY_PARAM_EXT_PARAM].orEmpty().encodeToUtf8()
            }
        }
    }

    open fun getIntentData() {
        arguments?.let {
            shopId = it.getString(KEY_SHOP_ID, "")
            isOfficialStore = it.getBoolean(KEY_IS_OFFICIAL_STORE, false)
            isGoldMerchant = it.getBoolean(KEY_IS_GOLD_MERCHANT, false)
            shopName = it.getString(KEY_SHOP_NAME, "")
            shopAttribution = it.getString(KEY_SHOP_ATTRIBUTION, "")
            shopRef = it.getString(KEY_SHOP_REF, "")
            isEnableDirectPurchase = it.getBoolean(KEY_ENABLE_SHOP_DIRECT_PURCHASE, false)
        }
    }

    private fun observeLiveData() {
        viewModel?.productListData?.observe(
            viewLifecycleOwner,
            Observer {
                hideLoading()
                shopHomeAdapter?.removeProductGridListPlaceholder()
                when (it) {
                    is Success -> {
                        val productListData = it.data.listShopProductUiModel
                        val hasNextPage = it.data.hasNextPage
                        val totalProductOnShop = it.data.totalProductData
                        val currentPage = it.data.currentPage
                        addProductListHeader()
                        updateProductListData(hasNextPage, productListData, totalProductOnShop, currentPage)
                        productListName = productListData.joinToString(",") { product -> product.name.orEmpty() }
                    }
                    is Fail -> {
                        val throwable = it.throwable
                        val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
                        if (!ShopUtil.isExceptionIgnored(throwable)) {
                            ShopUtil.logShopPageP2BuyerFlowAlerting(
                                tag = SHOP_PAGE_BUYER_FLOW_TAG,
                                functionName = this::observeLiveData.name,
                                liveDataName = ShopHomeViewModel::productListData.name,
                                userId = userId,
                                shopId = shopId,
                                shopName = shopName,
                                errorMessage = errorMessage,
                                stackTrace = Log.getStackTraceString(throwable),
                                errType = SHOP_PAGE_HOME_TAB_BUYER_FLOW_TAG
                            )
                        }
                        if (shopHomeAdapter?.isProductGridListPlaceholderExists() == true) {
                            showErrorToast(errorMessage)
                        }
                    }
                }
            }
        )

        viewModel?.checkWishlistData?.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        onSuccessCheckWishlist(it.data)
                    }
                    else -> {
                        // no-op
                    }
                }
            }
        )

        viewModel?.videoYoutube?.observe(
            viewLifecycleOwner,
            Observer {
                val result = it.second
                when (result) {
                    is Success -> {
                        onSuccessGetYouTubeData(it.first, result.data)
                    }
                    is Fail -> {
                        onFailedGetYouTubeData(it.first, result.throwable)
                    }
                }
            }
        )

        viewModel?.campaignNplRemindMeStatusData?.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        onSuccessGetCampaignNplRemindMeStatusData(it.data)
                    }
                    else -> {
                        // no-op
                    }
                }
            }
        )

        viewModel?.campaignFlashSaleStatusData?.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        onSuccessGetCampaignFlashSaleRemindMeStatusData(it.data)
                    }
                    else -> {
                        // no-op
                    }
                }
            }
        )

        viewModel?.checkCampaignNplRemindMeStatusData?.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        if (it.data.success) {
                            onSuccessCheckCampaignNplNotifyMe(it.data)
                        } else {
                            onFailCheckCampaignNplNotifyMe(it.data.campaignId, it.data.errorMessage)
                        }
                    }
                    is Fail -> {
                        (it.throwable as? CheckCampaignNplException)?.let { checkCampaignException ->
                            val errorMessage =
                                ErrorHandler.getErrorMessage(context, checkCampaignException)
                            onFailCheckCampaignNplNotifyMe(
                                checkCampaignException.campaignId,
                                errorMessage
                            )
                        }
                    }
                }
            }
        )

        viewModel?.checkCampaignFlashSaleRemindMeStatusData?.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        if (it.data.success) {
                            onSuccessCheckCampaignFlashSaleNotifyMe(it.data)
                        } else {
                            onFailCheckCampaignFlashSaleNotifyMe(
                                it.data.campaignId,
                                it.data.errorMessage
                            )
                        }
                    }
                    is Fail -> {
                        (it.throwable as? CheckCampaignNplException)?.let { checkCampaignException ->
                            val errorMessage =
                                ErrorHandler.getErrorMessage(context, checkCampaignException)
                            onFailCheckCampaignFlashSaleNotifyMe(
                                checkCampaignException.campaignId,
                                errorMessage
                            )
                        }
                    }
                }
            }
        )

        viewModel?.bottomSheetFilterLiveData?.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        onSuccessGetBottomSheetFilterData(it.data)
                    }
                    else -> {
                        // no-op
                    }
                }
            }
        )

        viewModel?.shopProductFilterCountLiveData?.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        onSuccessGetShopProductFilterCount(count = it.data)
                    }
                    else -> {
                        // no-op
                    }
                }
            }
        )

        viewModel?.shopHomeMerchantVoucherLayoutData?.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        shopHomeAdapter?.setHomeMerchantVoucherData(it.data)
                    }
                    is Fail -> {
                        shopHomeAdapter?.getMvcWidgetUiModel()?.let { uiModel ->
                            shopHomeAdapter?.setHomeMerchantVoucherData(uiModel.copy(isError = true))
                        }
                    }
                }
            }
        )

        observePlayWidget()
        observePlayWidgetReminderEvent()
        observePlayWidgetReminder()
        observeAddCartLiveData()
        observeUpdateCartLiveData()
        observeDeleteCartLiveData()
        observeUpdatedShopHomeWidgetQuantityData()
        observeShopAtcTrackerLiveData()
        observeShopProductDirectPurchaseWidgetAtcLiveData()
        observeIsCreateAffiliateCookieAtcProduct()
    }

    private fun observeShopProductDirectPurchaseWidgetAtcLiveData() {
        viewModel?.shopPageProductDirectPurchaseWidgetAtcTracker?.observe(viewLifecycleOwner) {
            sendClickAddToCartProductDirectPurchaseWidgetTracker(it)
        }
    }

    private fun sendClickAddToCartProductDirectPurchaseWidgetTracker(
        atcTrackerModel: ShopPageProductDirectPurchaseWidgetAtcTracker?
    ) {
        atcTrackerModel?.let {
            shopPageHomeTracking.onAddToCartProductDirectPurchaseWidget(
                atcTrackerModel,
                shopId,
                userId,
                shopName,
                customDimensionShopPage.shopType.orEmpty()
            )
        }
    }

    private fun observeIsCreateAffiliateCookieAtcProduct() {
        viewModel?.createAffiliateCookieAtcProduct?.observe(viewLifecycleOwner) {
            it?.let {
                createAffiliateCookieAtcProduct(it)
            }
        }
    }

    private fun createAffiliateCookieAtcProduct(affiliateAtcProductModel: AffiliateAtcProductModel) {
        (activity as? ShopPageSharedListener)?.createAffiliateCookieAtcProduct(
            affiliateAtcProductModel.productId,
            affiliateAtcProductModel.isVariant,
            affiliateAtcProductModel.stockQty
        )
    }

    private fun observeShopAtcTrackerLiveData() {
        viewModel?.shopPageAtcTracker?.observe(viewLifecycleOwner) {
            when (it.atcType) {
                ShopPageAtcTracker.AtcType.ADD -> {
                    sendClickAddToCartTracker(it)
                }

                ShopPageAtcTracker.AtcType.UPDATE_ADD, ShopPageAtcTracker.AtcType.UPDATE_REMOVE -> {
                    sendUpdateCartProductQuantityTracker(it)
                }

                else -> {
                    sendRemoveCartProductTracker(it)
                }
            }
        }
    }

    private fun sendClickAddToCartTracker(atcTrackerModel: ShopPageAtcTracker) {
        shopPageHomeTracking.onClickProductAtcDirectPurchaseButton(
            atcTrackerModel,
            shopId,
            customDimensionShopPage.shopType.orEmpty(),
            shopName,
            userId
        )
    }

    private fun sendUpdateCartProductQuantityTracker(atcTrackerModel: ShopPageAtcTracker) {
        shopPageHomeTracking.onClickProductAtcQuantityButton(
            atcTrackerModel,
            shopId,
            userId
        )
    }

    private fun sendRemoveCartProductTracker(atcTrackerModel: ShopPageAtcTracker) {
        shopPageHomeTracking.onClickProductAtcTrashButton(
            atcTrackerModel,
            shopId,
            userId
        )
    }

    private fun observeUpdatedShopHomeWidgetQuantityData() {
        viewModel?.updatedShopHomeWidgetQuantityData?.observe(viewLifecycleOwner, {
            shopHomeAdapter?.submitList(it.toList())
        })
    }

    private fun sendEmbraceBreadCrumbLogger(
        actionName: String,
        shopId: String,
        stackTraceString: String
    ) {
        ShopLogger.logBreadCrumbShopPageHomeTabJourney(
            actionName,
            ShopLogger.mapToShopPageHomeTabJourneyEmbraceBreadCrumbJsonData(
                shopId,
                stackTraceString
            )
        )
    }

    private fun onSuccessGetShopProductFilterCount(count: Int = Int.ZERO, isFulfillmentFilterActive: Boolean = false) {
        val countText = if (isFulfillmentFilterActive) {
            getString(com.tokopedia.filter.R.string.bottom_sheet_filter_finish_button_no_count)
        } else {
            String.format(
                getString(com.tokopedia.filter.R.string.bottom_sheet_filter_finish_button_template_text),
                count.thousandFormatted()
            )
        }
        sortFilterBottomSheet?.setResultCountText(countText)
    }

    private fun onSuccessGetBottomSheetFilterData(model: DynamicFilterModel) {
        model.defaultSortValue = DEFAULT_SORT_ID
        sortFilterBottomSheet?.setDynamicFilterModel(model)
    }

    private fun addChangeProductGridSection(isProductListEmpty: Boolean, totalProductData: Int = 0) {
        shopHomeAdapter?.updateShopPageProductChangeGridSectionIcon(isProductListEmpty, totalProductData, gridType)
    }

    private fun onFailCheckCampaignNplNotifyMe(campaignId: String, errorMessage: String) {
        view?.let {
            Toaster.build(
                it,
                errorMessage,
                Toaster.LENGTH_LONG,
                Toaster.TYPE_ERROR,
                getString(R.string.shop_string_ok)
            ).setAnchorViewToShopHeaderBottomViewContainer(getShopHeaderBottomViewContainer()).show()
        }
        shopHomeAdapter?.updateRemindMeStatusCampaignNplWidgetData(campaignId)
    }

    private fun onSuccessCheckCampaignNplNotifyMe(data: CheckCampaignNotifyMeUiModel) {
        val isRegisterCampaign =
            data.action.toLowerCase() == NotifyMeAction.REGISTER.action.toLowerCase()
        shopHomeAdapter?.updateRemindMeStatusCampaignNplWidgetData(
            data.campaignId,
            isRegisterCampaign,
            true
        )
        if (shopHomeAdapter?.isCampaignFollower(data.campaignId) == true) {
            shopPageHomeTracking.clickNotifyMeNplFollowerButton(
                isOwner,
                data.action,
                viewModel?.userId.orEmpty(),
                customDimensionShopPage
            )
        } else {
            shopPageHomeTracking.clickNotifyMeButton(
                isOwner,
                data.action,
                customDimensionShopPage
            )
        }
        view?.let {
            Toaster.build(
                it,
                data.message,
                Snackbar.LENGTH_LONG,
                Toaster.TYPE_NORMAL,
                getString(R.string.shop_string_ok),
                View.OnClickListener {
                    shopPageHomeTracking.toasterActivationClickOk(isOwner, customDimensionShopPage)
                }
            ).setAnchorViewToShopHeaderBottomViewContainer(getShopHeaderBottomViewContainer()).show()
            shopPageHomeTracking.impressionToasterActivation(isOwner, customDimensionShopPage)
        }
    }

    private fun onSuccessGetCampaignNplRemindMeStatusData(data: GetCampaignNotifyMeUiModel) {
        shopHomeAdapter?.updateRemindMeStatusCampaignNplWidgetData(data.campaignId, data.isAvailable)
        if (getNplRemindMeClickedCampaignId() == data.campaignId && !data.isAvailable) {
            val nplCampaignModel = shopHomeAdapter?.getNplCampaignUiModel(data.campaignId)
            nplCampaignModel?.let {
                shopHomeAdapter?.showNplRemindMeLoading(data.campaignId)
                handleClickRemindMe(it)
                setNplRemindMeClickedCampaignId("")
            }
        }
    }

    private fun onFailCheckCampaignFlashSaleNotifyMe(campaignId: String, errorMessage: String) {
        view?.let {
            Toaster.build(
                it,
                errorMessage,
                Toaster.LENGTH_LONG,
                Toaster.TYPE_ERROR,
                getString(R.string.shop_page_label_oke)
            ).setAnchorViewToShopHeaderBottomViewContainer(getShopHeaderBottomViewContainer()).show()
        }
        shopHomeAdapter?.updateRemindMeStatusCampaignFlashSaleWidgetData(campaignId)
    }

    private fun onSuccessCheckCampaignFlashSaleNotifyMe(data: CheckCampaignNotifyMeUiModel) {
        val isRegisterCampaign =
            data.action.toLowerCase() == NotifyMeAction.REGISTER.action.toLowerCase()
        shopHomeAdapter?.updateRemindMeStatusCampaignFlashSaleWidgetData(
            data.campaignId,
            isRegisterCampaign,
            true
        )
        view?.let {
            Toaster.build(
                it,
                data.message,
                Snackbar.LENGTH_LONG,
                Toaster.TYPE_NORMAL,
                getString(R.string.shop_page_label_oke)
            ).setAnchorViewToShopHeaderBottomViewContainer(getShopHeaderBottomViewContainer()).show()
        }
    }

    private fun onSuccessGetCampaignFlashSaleRemindMeStatusData(data: GetCampaignNotifyMeUiModel) {
        shopHomeAdapter?.updateRemindMeStatusCampaignFlashSaleWidgetData(
            data.campaignId,
            data.isAvailable
        )
        if (getFlashSaleRemindMeClickedCampaignId() == data.campaignId && !data.isAvailable) {
            val flashSaleCampaignModel =
                shopHomeAdapter?.getFlashSaleCampaignUiModel(data.campaignId)
            flashSaleCampaignModel?.let {
                handleFlashSaleClickReminder(it)
                setFlashSaleRemindMeClickedCampaignId("")
            }
        }
    }

    protected open fun onSuccessGetBannerTimerRemindMeStatusData(data: GetCampaignNotifyMeUiModel) {
        viewModel?.toggleBannerTimerRemindMe(
            shopHomeAdapter?.getNewVisitableItems().orEmpty().toMutableList(),
            isRemindMe = data.isAvailable,
            isClickRemindMe = false
        )
    }

    private fun addProductListHeader() {
        shopHomeAdapter?.setEtalaseTitleData()
        val shopProductSortFilterUiModel = ShopProductSortFilterUiModel(
            selectedEtalaseId = "",
            selectedEtalaseName = "",
            selectedSortId = sortId,
            selectedSortName = sortName,
            filterIndicatorCounter = getIndicatorCount(
                shopProductFilterParameter?.getMapData()
            )
        )
        shopHomeAdapter?.setSortFilterData(shopProductSortFilterUiModel)
    }

    private fun onSuccessCheckWishlist(data: List<Pair<ShopHomeCarousellProductUiModel, List<CheckWishlistResult>>?>) {
        data.onEach { pairCheckWishlistData ->
            pairCheckWishlistData?.let {
                it.second.onEach { checkWishlistResult ->
                    val productData = it.first.productList.find { shopHomeProductViewModel ->
                        shopHomeProductViewModel.id == checkWishlistResult.productId
                    }
                    productData?.let { shopHomeProductViewModel ->
                        shopHomeProductViewModel.isWishList = checkWishlistResult.isWishlist
                    }
                }
                shopHomeAdapter?.updateProductWidgetData(it.first)
            }
        }
    }

    protected open fun onSuccessGetYouTubeData(widgetId: String, data: YoutubeVideoDetailModel) {
        shopHomeAdapter?.setHomeYouTubeData(widgetId, data)
    }

    protected open fun onFailedGetYouTubeData(widgetId: String, throwable: Throwable) {
        logExceptionToCrashlytics(ERROR_WHEN_GET_YOUTUBE_DATA, throwable)
        shopHomeAdapter?.setHomeYouTubeData(widgetId, YoutubeVideoDetailModel())
    }

    private fun onSuccessAddToCart(
        dataModelAtc: DataModel,
        shopHomeProductViewModel: ShopHomeProductUiModel?,
        parentPosition: Int,
        shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?,
        isPersonalizationWidget: Boolean = false,
        isOcc: Boolean = false
    ) {
        if (isPersonalizationWidget) {
            trackClickAddToCartPersonalization(
                dataModelAtc,
                shopHomeProductViewModel,
                shopHomeCarousellProductUiModel
            )
        } else {
            trackClickAddToCart(
                dataModelAtc,
                shopHomeProductViewModel,
                parentPosition,
                shopHomeCarousellProductUiModel
            )
        }

        if (isOcc) {
            context?.let {
                RouteManager.route(it, ApplinkConstInternalMarketplace.ONE_CLICK_CHECKOUT)
            }
        } else {
            view?.let { view ->
                NetworkErrorHelper.showGreenCloseSnackbar(view, dataModelAtc.message.first())
            }
        }
    }

    private fun trackClickAddToCart(
        dataModelAtc: DataModel?,
        shopHomeProductViewModel: ShopHomeProductUiModel?,
        parentPosition: Int,
        shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?
    ) {
        shopPageHomeTracking.addToCart(
            isOwner,
            dataModelAtc?.cartId ?: "",
            shopAttribution,
            isLogin,
            shopPageHomeLayoutUiModel?.masterLayoutId.toString(),
            shopHomeProductViewModel?.name ?: "",
            shopHomeProductViewModel?.id ?: "",
            shopHomeProductViewModel?.displayedPrice ?: "",
            dataModelAtc?.quantity ?: VALUE_INT_ONE,
            shopName,
            ShopUtil.getActualPositionFromIndex(parentPosition),
            shopHomeCarousellProductUiModel?.widgetId ?: "",
            shopHomeCarousellProductUiModel?.header?.title ?: "",
            shopHomeCarousellProductUiModel?.header?.isATC.orZero(),
            customDimensionShopPage
        )
    }

    private fun trackClickAddToCartPersonalization(
        dataModelAtc: DataModel?,
        shopHomeProductViewModel: ShopHomeProductUiModel?,
        shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?
    ) {
        shopPageHomeTracking.addToCartPersonalizationProduct(
            isOwner,
            shopHomeProductViewModel?.name ?: "",
            shopHomeProductViewModel?.id ?: "",
            shopHomeProductViewModel?.displayedPrice ?: "",
            dataModelAtc?.quantity ?: VALUE_INT_ONE,
            shopName,
            viewModel?.userId.orEmpty(),
            shopHomeCarousellProductUiModel?.header?.title ?: "",
            shopHomeCarousellProductUiModel?.name ?: "",
            customDimensionShopPage,
            shopHomeProductViewModel?.recommendationType ?: "",
            shopHomeProductViewModel?.categoryBreadcrumbs ?: ""
        )
    }

    private fun trackClickAddToCartPersonalizationReminder(
        dataModelAtc: DataModel?,
        shopHomeProductViewModel: ShopHomeProductUiModel?,
        shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?
    ) {
        shopPageHomeTracking.addToCartPersonalizationProductReminder(
            isOwner,
            isLogin,
            shopHomeProductViewModel?.name ?: "",
            shopHomeProductViewModel?.id ?: "",
            shopHomeProductViewModel?.displayedPrice ?: "",
            dataModelAtc?.quantity ?: VALUE_INT_ONE,
            shopName,
            viewModel?.userId.orEmpty(),
            shopHomeCarousellProductUiModel?.header?.title ?: "",
            dataModelAtc?.cartId ?: "",
            shopHomeProductViewModel?.recommendationType ?: "",
            customDimensionShopPage
        )
    }

    private fun onErrorAddToCart(exception: Throwable) {
        view?.let { view ->
            val errorMessage = if (exception is MessageErrorException) {
                exception.message
            } else {
                ErrorHandler.getErrorMessage(context, exception)
            }
            NetworkErrorHelper.showRedCloseSnackbar(view, errorMessage)
        }
    }

    private fun onErrorGetShopHomeLayoutData(throwable: Throwable) {
        if (throwable is MessageErrorException) {
            globalErrorShopPage?.setType(GlobalError.SERVER_ERROR)
        } else {
            globalErrorShopPage?.setType(GlobalError.NO_CONNECTION)
        }
        globalErrorShopPage?.visible()
        getRecyclerView(view)?.hide()

        globalErrorShopPage?.setOnClickListener {
            loadInitialData()
        }
    }

    private fun updateProductListData(
        hasNextPage: Boolean,
        productList: List<ShopHomeProductUiModel>,
        totalProductData: Int,
        currentPage: Int
    ) {
        val isProductListEmpty = (productList.size.isZero() && currentPage == Int.ONE) || totalProductData.isZero()
        if (isProductListEmpty) {
            shopHomeAdapter?.setProductListEmptyState(isOwner)
            addChangeProductGridSection(isProductListEmpty)
        } else {
            addChangeProductGridSection(isProductListEmpty, totalProductData)
            shopHomeAdapter?.setProductListData(productList, isOwner)
            updateScrollListenerState(hasNextPage)
        }
    }

    protected open fun setShopLayoutData(dataWidgetLayoutUiModel: ShopPageLayoutUiModel) {
        val data: ShopPageLayoutUiModel = filterNplWidgetLayoutDataIfDisabled(dataWidgetLayoutUiModel)
        initialLayoutData = data.listWidgetLayout.toMutableList()
        listWidgetLayout = initialLayoutData.toMutableList()
        shopPageHomeTracking.sendUserViewHomeTabWidgetTracker(
            data.masterLayoutId,
            shopId
        )
        listWidgetLayout = data.listWidgetLayout.toMutableList()
        val shopHomeWidgetContentData = ShopPageHomeMapper.mapShopHomeWidgetLayoutToListShopHomeWidget(
            data.listWidgetLayout,
            isOwner,
            isLogin,
            isThematicWidgetShown,
            isEnableDirectPurchase,
            shopId,
            isOverrideTheme(),
            getShopPageColorSchema()
        )
        if (shopHomeWidgetContentData.isNotEmpty()) {
            shopHomeAdapter?.setHomeLayoutData(shopHomeWidgetContentData)
        } else {
            shopHomeAdapter?.addProductGridListPlaceHolder()
        }
    }

    private fun filterNplWidgetLayoutDataIfDisabled(
        dataWidgetLayoutUiModel: ShopPageLayoutUiModel
    ): ShopPageLayoutUiModel {
        return if (!ShopPageRemoteConfigChecker.isEnableShopHomeNplWidget(context)) {
            dataWidgetLayoutUiModel.copy(
                listWidgetLayout = dataWidgetLayoutUiModel.listWidgetLayout.filter {
                    it.widgetName != WidgetName.NEW_PRODUCT_LAUNCH_CAMPAIGN
                }
            )
        } else {
            dataWidgetLayoutUiModel
        }
    }

    private fun checkCampaignNplWidgetRemindMeStatus(listWidgetContentData: List<Visitable<*>?>) {
        viewModel?.let {
            if (it.isLogin) {
                val listCampaignNplUiModel =
                    listWidgetContentData.filterIsInstance<ShopHomeNewProductLaunchCampaignUiModel>()
                listCampaignNplUiModel.forEach { nplCampaignUiModel ->
                    nplCampaignUiModel.data?.firstOrNull()?.let { nplCampaignItem ->
                        if (nplCampaignItem.statusCampaign.equals(
                                StatusCampaign.UPCOMING.statusCampaign,
                                ignoreCase = true
                            )
                        ) {
                            viewModel?.getCampaignNplRemindMeStatus(nplCampaignItem)
                        }
                    }
                }
            }
        }
    }

    private fun checkFlashSaleWidgetRemindMeStatus(listWidgetContentData: List<Visitable<*>?>) {
        viewModel?.let {
            if (it.isLogin) {
                val listCampaignFlashSaleUiModel =
                    listWidgetContentData.filterIsInstance<ShopHomeFlashSaleUiModel>()
                listCampaignFlashSaleUiModel.forEach { flashSaleCampaignUiModel ->
                    flashSaleCampaignUiModel.data?.firstOrNull()?.let { flashSaleItem ->
                        if (flashSaleItem.statusCampaign.equals(
                                StatusCampaign.UPCOMING.statusCampaign,
                                ignoreCase = true
                            )
                        ) {
                            viewModel?.getCampaignFlashSaleRemindMeStatus(flashSaleItem.campaignId)
                        }
                    }
                }
            }
        }
    }

    private fun checkProductWidgetWishListStatus(listWidgetContentData: List<Visitable<*>?>) {
        viewModel?.let {
            if (it.isLogin) {
                val listCarouselProductUiModel =
                    listWidgetContentData.filterIsInstance<ShopHomeCarousellProductUiModel>()
                if (listCarouselProductUiModel.isNotEmpty()) {
                    viewModel?.getWishlistStatus(listCarouselProductUiModel)
                }
            }
        }
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, AdapterTypeFactory> {
        return ShopHomeAdapter(shopHomeAdapterTypeFactory)
    }

    override fun getAdapterTypeFactory(): AdapterTypeFactory = shopHomeAdapterTypeFactory

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
                .inject(this@ShopPageHomeFragment)
        }
    }

    override fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : DataEndlessScrollListener(staggeredGridLayoutManager, shopHomeAdapter) {

            override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(view, dx, dy)
                val layoutManager =
                    (getRecyclerView(view)?.layoutManager as? StaggeredGridLayoutManager)
                val firstCompletelyVisibleItemPosition =
                    layoutManager?.findFirstCompletelyVisibleItemPositions(null)?.getOrNull(0)
                        .orZero()
                if (firstCompletelyVisibleItemPosition == 0 && isClickToScrollToTop) {
                    isClickToScrollToTop = false
                    if (ShopUtil.isEnableShopPageReImagined()) {
                        (getRealParentFragment() as? ShopPageHeaderFragmentV2)?.expandHeader()
                    } else {
                        (getRealParentFragment() as? ShopPageHeaderFragment)?.expandHeader()
                    }
                }
                if (firstCompletelyVisibleItemPosition != latestCompletelyVisibleItemIndex) {
                    hideScrollToTopButton()
                }
                latestCompletelyVisibleItemIndex = firstCompletelyVisibleItemPosition
                val lastCompletelyVisibleItemPosition =
                    layoutManager?.findLastCompletelyVisibleItemPositions(
                        null
                    )?.getOrNull(0).orZero()

                val lastVisibleItemPosition = layoutManager?.findLastVisibleItemPositions(
                    null
                )?.getOrNull(0).orZero()
                val firstVisibleItemPosition = layoutManager?.findFirstVisibleItemPositions(
                    null
                )?.getOrNull(0).orZero()
                checkLoadNextShopHomeWidgetContentData(
                    lastCompletelyVisibleItemPosition,
                    firstVisibleItemPosition
                )
                checkLoadProductGridListData(lastVisibleItemPosition)
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

            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                shopHomeAdapter?.showLoading()
                getProductList(page)
            }
        }
    }

    private fun checkLoadProductGridListData(lastCompletelyVisibleItemPosition: Int) {
        if (shopHomeAdapter?.isLoadProductGridListData(lastCompletelyVisibleItemPosition) == true) {
            shopHomeAdapter?.updateProductGridListPlaceholderStateToLoadingState()
            viewModel?.getProductGridListWidgetData(
                shopId,
                ShopUtil.getProductPerPage(context),
                shopProductFilterParameter ?: ShopProductFilterParameter(),
                initialProductListData,
                ShopUtil.getShopPageWidgetUserAddressLocalData(context)
                    ?: LocalCacheModel(),
                isEnableDirectPurchase
            )
        }
    }

    private fun checkLoadNextShopHomeWidgetContentData(
        lastVisibleItemPosition: Int,
        firstVisibleItemPosition: Int
    ) {
        val lastVisibleShopWidgetPosition = shopHomeAdapter?.getLastVisibleShopWidgetPosition(
            lastVisibleItemPosition
        ).orZero()
        val shouldLoadLastVisibleItem =
            shopHomeAdapter?.isLoadNextHomeWidgetData(lastVisibleShopWidgetPosition).orFalse()
        val shouldLoadFirstVisibleItem =
            shopHomeAdapter?.isLoadNextHomeWidgetData(firstVisibleItemPosition).orFalse()
        if (shouldLoadLastVisibleItem || shouldLoadFirstVisibleItem) {
            val position = if (shouldLoadLastVisibleItem) {
                lastVisibleShopWidgetPosition
            } else {
                firstVisibleItemPosition
            }
            val listWidgetLayoutToLoad = getListWidgetLayoutToLoad(position)
            shopHomeAdapter?.updateShopHomeWidgetStateToLoading(listWidgetLayoutToLoad)
            val widgetPlayLayout = listWidgetLayoutToLoad.firstOrNull { isWidgetPlay(it) }?.apply {
                listWidgetLayoutToLoad.remove(this)
            }
            val widgetMvcLayout = listWidgetLayoutToLoad.firstOrNull { isWidgetMvc(it) }?.apply {
                listWidgetLayoutToLoad.remove(this)
            }
            val widgetProductComparison = excludeWidgetComparison(listWidgetLayoutToLoad)
            excludeWidgetBundle(listWidgetLayoutToLoad)
            getWidgetContentData(listWidgetLayoutToLoad)
            widgetPlayLayout?.let {
                getPlayWidgetData()
            }
            widgetMvcLayout?.let {
                getMvcWidgetData()
            }
            widgetProductComparison?.let {
                getProductComparisonWidgetData()
            }
            listWidgetLayoutToLoad.clear()
        }
    }

    private fun excludeWidgetComparison(listWidgetLayoutToLoad: MutableList<ShopPageWidgetUiModel>): ShopPageWidgetUiModel? {
        return listWidgetLayoutToLoad.firstOrNull { isWidgetPersoComparison(it) }?.apply {
            listWidgetLayoutToLoad.remove(this)
        }
    }

    private fun getProductComparisonWidgetData() {
        shopHomeAdapter?.getPersoProductComparisonWidgetUiModel()?.let {
            viewModel?.getProductComparisonData(shopId, it)
        }
    }

    protected fun getMvcWidgetData() {
        shopHomeAdapter?.getMvcWidgetUiModel()?.let {
            viewModel?.getMerchantVoucherCoupon(shopId, context, it)
        }
    }

    private fun excludeWidgetBundle(listWidgetLayoutToLoad: MutableList<ShopPageWidgetUiModel>) {
        viewModel?.let { viewModel ->
            val iterator = listWidgetLayoutToLoad.iterator()
            while (iterator.hasNext()) {
                val element = iterator.next()
                if (viewModel.isWidgetBundle(element)) {
                    iterator.remove()
                }
            }
        }
    }

    protected open fun getPlayWidgetData() {
        shopHomeAdapter?.getPlayWidgetUiModel()?.let {
            val playWidgetType = if (isOwner) {
                ShopPageWidgetMapper.mapToPlayWidgetTypeSellerApp(shopId)
            } else {
                ShopPageWidgetMapper.mapToPlayWidgetTypeShopPage(shopId)
            }
            viewModel?.getPlayWidget(it, playWidgetType)
        }
    }

    private fun getWidgetContentData(listWidgetLayoutToLoad: MutableList<ShopPageWidgetUiModel>) {
        if (listWidgetLayoutToLoad.isNotEmpty()) {
            val widgetUserAddressLocalData = ShopUtil.getShopPageWidgetUserAddressLocalData(context)
                ?: LocalCacheModel()
            viewModel?.getWidgetContentData(
                listWidgetLayoutToLoad.toList(),
                shopId,
                widgetUserAddressLocalData,
                isThematicWidgetShown,
                isEnableDirectPurchase,
                isOverrideTheme(),
                getShopPageColorSchema()
            )
        }
    }

    private fun isWidgetPersoComparison(data: ShopPageWidgetUiModel): Boolean {
        return data.widgetType == PERSONALIZATION && data.widgetName == PERSO_PRODUCT_COMPARISON
    }

    private fun isWidgetMvc(data: ShopPageWidgetUiModel): Boolean {
        return data.widgetType == PROMO && data.widgetName == VOUCHER_STATIC
    }

    protected fun isWidgetPlay(data: ShopPageWidgetUiModel): Boolean {
        return data.widgetType == DYNAMIC && data.widgetName == PLAY_CAROUSEL_WIDGET
    }

    private fun getListWidgetLayoutToLoad(lastCompletelyVisibleItemPosition: Int): MutableList<ShopPageWidgetUiModel> {
        return if (listWidgetLayout.isNotEmpty()) {
            if (shopHomeAdapter?.isLoadFirstWidgetContentData() == true) {
                val toIndex = ShopUtil.getActualPositionFromIndex(lastCompletelyVisibleItemPosition)
                    .takeIf { it <= listWidgetLayout.size } ?: listWidgetLayout.size
                listWidgetLayout.subList(
                    LIST_WIDGET_LAYOUT_START_INDEX,
                    toIndex
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

    fun getProductList(page: Int) {
        if (shopId.isNotEmpty()) {
            viewModel?.getNewProductList(
                shopId,
                page,
                ShopUtil.getProductPerPage(context),
                shopProductFilterParameter ?: ShopProductFilterParameter(),
                ShopUtil.getShopPageWidgetUserAddressLocalData(context) ?: LocalCacheModel(),
                isEnableDirectPurchase
            )
        }
    }

    override fun loadData(page: Int) {}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_ETALASE -> if (resultCode == Activity.RESULT_OK && data != null) {
                if (shopHomeAdapter?.isLoading == true) {
                    return
                }

                val etalaseId = data.getStringExtra(ShopShowcaseParamConstant.EXTRA_ETALASE_ID)
                    ?: ""
                val etalaseName = data.getStringExtra(ShopShowcaseParamConstant.EXTRA_ETALASE_NAME)
                    ?: ""
                val isNeedToReloadData = data.getBooleanExtra(
                    ShopShowcaseParamConstant.EXTRA_IS_NEED_TO_RELOAD_DATA,
                    false
                )
                shopPageHomeTracking.clickMoreMenuChip(
                    isOwner,
                    etalaseName,
                    customDimensionShopPage
                )
                val intent = ShopProductListResultActivity.createIntent(
                    activity,
                    shopId,
                    "",
                    etalaseId,
                    "",
                    "",
                    shopRef
                )

                intent.putExtra(
                    ShopCommonExtraConstant.EXTRA_IS_NEED_TO_RELOAD_DATA,
                    isNeedToReloadData
                )
                startActivity(intent)
            }
            REQUEST_CODE_SORT -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (shopHomeAdapter?.isLoading == true) {
                        return
                    }
                    sortId = data?.getStringExtra(ShopProductSortActivity.SORT_VALUE) ?: ""
                    shopPageHomeTracking.sortProduct(sortName, isOwner, customDimensionShopPage)
                    changeShopProductFilterParameterSharedData()
                    changeSortData(sortId)
                    scrollToEtalaseTitlePosition()
                }
            }
            REQUEST_CODE_USER_LOGIN -> {
                if (resultCode == Activity.RESULT_OK) {
                    (getRealParentFragment() as? InterfaceShopPageHeader)?.refreshData()
                }
            }
            REQUEST_CODE_USER_LOGIN_PLAY_WIDGET_REMIND_ME -> if (resultCode == Activity.RESULT_OK) {
                val lastEvent = viewModel?.playWidgetReminderEvent?.value
                if (lastEvent != null) {
                    viewModel?.shouldUpdatePlayWidgetToggleReminder(
                        lastEvent.first,
                        lastEvent.second
                    )
                }
            }
            REQUEST_CODE_DONATION_DETAIL -> {
                shopPageHomeTracking.actionPressBackDonation(isOwner, shopId)
            }
            else -> {
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
        handleProductCardOptionsActivityResult(
            requestCode,
            resultCode,
            data,
            object : ProductCardOptionsWishlistCallback {
                override fun onReceiveWishlistResult(productCardOptionsModel: ProductCardOptionsModel) {
                    handleWishlistAction(productCardOptionsModel)
                }
            }
        )
        if (requestCode == PlayWidgetConst.KEY_PLAY_WIDGET_REQUEST_CODE && data != null) {
            notifyPlayWidgetTotalView(data)
            notifyPlayWidgetReminder(data)
        }
    }

    private fun scrollToEtalaseTitlePosition() {
        getRecyclerView(view)?.smoothScrollBy(0, recyclerViewTopPadding * 2)
        staggeredGridLayoutManager?.scrollToPositionWithOffset(
            shopHomeAdapter?.shopHomeEtalaseTitlePosition.orZero(),
            0
        )
    }

    override fun impressionProductBundleMultiple(
        shopId: String,
        warehouseId: String,
        selectedMultipleBundle: ShopHomeProductBundleDetailUiModel,
        bundleName: String,
        bundleType: String,
        bundlePosition: Int
    ) {
        shopPageHomeTracking.impressionMultipleBundleWidget(
            shopId = shopId,
            userId = userId,
            bundleId = selectedMultipleBundle.bundleId,
            bundleName = bundleName,
            bundlePriceCut = selectedMultipleBundle.discountPercentage.toString(),
            bundlePrice = selectedMultipleBundle.displayPrice,
            bundlePosition = bundlePosition,
            isFestivity = selectedMultipleBundle.isFestivity
        )
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
        sendShopHomeWidgetImpressionTracker(
            widgetTitle,
            widgetName,
            selectedMultipleBundle.widgetId,
            bundlePosition,
            selectedMultipleBundle.widgetMasterId,
            selectedMultipleBundle.isFestivity
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
        shopPageHomeTracking.clickOnMultipleBundleProduct(
            shopId = shopId,
            userId = userId,
            bundleId = selectedMultipleBundle.bundleId,
            bundleName = bundleName,
            bundlePriceCut = selectedMultipleBundle.discountPercentage.toString(),
            bundlePrice = selectedMultipleBundle.displayPrice,
            bundlePosition = bundlePosition,
            clickedProduct = selectedProduct,
            isFestivity = selectedMultipleBundle.isFestivity
        )
    }

    override fun onTrackSingleVariantChange(selectedProduct: ShopHomeBundleProductUiModel, selectedSingleBundle: ShopHomeProductBundleDetailUiModel, bundleName: String) {
        shopPageHomeTracking.onTrackSingleVariantChange(
            shopId = shopId,
            userId = userId,
            productId = selectedProduct.productId,
            bundleName = bundleName,
            bundleId = selectedSingleBundle.bundleId,
            bundlePriceCut = selectedSingleBundle.discountPercentage.toString(),
            selectedPackage = selectedSingleBundle.minOrderWording,
            isFestivity = selectedSingleBundle.isFestivity
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
        sendShopHomeWidgetImpressionTracker(
            widgetTitle,
            widgetName,
            selectedSingleBundle.widgetId,
            bundlePosition,
            selectedSingleBundle.widgetMasterId,
            selectedSingleBundle.isFestivity
        )
        shopPageHomeTracking.impressionSingleBundleWidget(
            shopId = shopId,
            userId = userId,
            productId = selectedProduct.productId,
            bundleId = selectedSingleBundle.bundleId,
            bundleName = bundleName,
            bundlePriceCut = selectedSingleBundle.discountPercentage.toString(),
            bundlePrice = selectedSingleBundle.displayPrice,
            bundlePosition = bundlePosition,
            isFestivity = selectedSingleBundle.isFestivity
        )
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
        shopPageHomeTracking.clickOnSingleBundleProduct(
            shopId = shopId,
            userId = userId,
            bundleId = selectedSingleBundle.bundleId,
            bundleName = bundleName,
            bundlePriceCut = selectedSingleBundle.discountPercentage.toString(),
            bundlePrice = selectedSingleBundle.displayPrice,
            bundlePosition = bundlePosition,
            clickedProduct = selectedProduct,
            selectedPackage = selectedSingleBundle.minOrderWording,
            isFestivity = selectedSingleBundle.isFestivity
        )
    }

    override fun addMultipleBundleToCart(
        shopId: String,
        warehouseId: String,
        selectedMultipleBundle: ShopHomeProductBundleDetailUiModel,
        bundleListSize: Int,
        productDetails: List<ShopHomeBundleProductUiModel>,
        bundleName: String,
        bundleType: String,
        bundlePosition: Int,
        widgetLayout: ShopHomeWidgetLayout,
        bundleGroupId: String
    ) {
        if (isOwner) {
            // disable owner add their own bundle to cart
            showErrorToast(getString(R.string.shop_page_product_bundle_failed_atc_text_for_shop_owner))
        } else {
            if (isLogin) {
                if (selectedMultipleBundle.isProductsHaveVariant) {
                    // go to bundling selection page
                    goToBundlingSelectionPage(selectedMultipleBundle.bundleId)
                } else {
                    // atc bundle directly from shop page home
                    val widgetLayoutParams = ShopPageWidgetUiModel(
                        widgetId = widgetLayout.widgetId,
                        widgetMasterId = widgetLayout.widgetMasterId,
                        widgetType = widgetLayout.widgetType,
                        widgetName = widgetLayout.widgetName
                    )
                    viewModel?.addBundleToCart(
                        shopId = shopId,
                        userId = userId,
                        bundleId = selectedMultipleBundle.bundleId,
                        productDetails = productDetails,
                        onFinishAddToCart = { handleOnFinishAtcBundle(it, bundleListSize, widgetLayoutParams, bundleName, ShopPageConstant.BundleType.MULTIPLE_BUNDLE, selectedMultipleBundle) },
                        onErrorAddBundleToCart = { handleOnErrorAtcBundle(it) },
                        productQuantity = selectedMultipleBundle.minOrder
                    )
                }
            } else {
                redirectToLoginPage()
            }
        }
    }

    override fun addSingleBundleToCart(
        shopId: String,
        warehouseId: String,
        selectedBundle: ShopHomeProductBundleDetailUiModel,
        bundleListSize: Int,
        bundleProducts: ShopHomeBundleProductUiModel,
        bundleName: String,
        bundleType: String,
        bundlePosition: Int,
        widgetLayout: ShopHomeWidgetLayout,
        bundleGroupId: String
    ) {
        if (isOwner) {
            // disable owner add their own bundle to cart
            showErrorToast(getString(R.string.shop_page_product_bundle_failed_atc_text_for_shop_owner))
        } else {
            if (isLogin) {
                if (selectedBundle.isProductsHaveVariant) {
                    // go to bundling selection page
                    goToBundlingSelectionPage(selectedBundle.bundleId)
                } else {
                    // atc bundle directly from shop page home
                    val widgetLayoutParams = ShopPageWidgetUiModel(
                        widgetId = widgetLayout.widgetId,
                        widgetMasterId = widgetLayout.widgetMasterId,
                        widgetType = widgetLayout.widgetType,
                        widgetName = widgetLayout.widgetName
                    )
                    viewModel?.addBundleToCart(
                        shopId = shopId,
                        userId = userId,
                        bundleId = selectedBundle.bundleId,
                        productDetails = listOf(bundleProducts),
                        onFinishAddToCart = { handleOnFinishAtcBundle(it, bundleListSize, widgetLayoutParams, bundleName, ShopPageConstant.BundleType.SINGLE_BUNDLE, selectedBundle) },
                        onErrorAddBundleToCart = { handleOnErrorAtcBundle(it) },
                        productQuantity = selectedBundle.minOrder
                    )
                }
            } else {
                redirectToLoginPage()
            }
        }
    }

    private fun handleOnFinishAtcBundle(
        atcBundleModel: AddToCartBundleModel,
        bundleListSize: Int,
        widgetLayout: ShopPageWidgetUiModel,
        bundleName: String,
        bundleType: String,
        shopHomeProductBundleDetailUiModel: ShopHomeProductBundleDetailUiModel
    ) {
        atcBundleModel.validateResponse(
            onSuccess = {
                // Send the tracker here because the tracker needs cartId. CartId will we get once we success ATC
                sendTrackerAtcBundle(atcBundleModel, bundleName, bundleType, shopHomeProductBundleDetailUiModel)

                showToastSuccess(
                    getString(R.string.shop_page_product_bundle_success_atc_text),
                    getString(R.string.see_label)
                ) {
                    goToCart()
                }
            },
            onFailedWithMessages = {
                val errorDialogCtaText: String
                val errorMessageDescription = if (bundleListSize > MIN_BUNDLE_SIZE) {
                    errorDialogCtaText = getString(R.string.shop_page_product_bundle_failed_oos_cta_text_with_alt)
                    getString(R.string.shop_page_product_bundle_failed_oos_dialog_desc_with_alt)
                } else {
                    errorDialogCtaText = getString(R.string.shop_page_product_bundle_failed_oos_cta_text)
                    getString(R.string.shop_page_product_bundle_failed_oos_dialog_desc_no_alt)
                }
                showErrorDialogAtcBundle(
                    errorTitle = getString(R.string.shop_page_product_bundle_failed_oos_dialog_title),
                    errorDescription = errorMessageDescription,
                    ctaText = errorDialogCtaText,
                    widgetLayoutParams = widgetLayout
                )
            },
            onFailedWithException = {
                showErrorToast(it.message.orEmpty())
            }
        )
    }

    private fun handleOnErrorAtcBundle(throwable: Throwable) {
        showErrorToast(throwable.message.orEmpty())
    }

    private fun sendTrackerAtcBundle(atcBundleModel: AddToCartBundleModel, bundleName: String, bundleType: String, shopHomeProductBundleDetailUiModel: ShopHomeProductBundleDetailUiModel) {
        if (bundleType == ShopPageConstant.BundleType.MULTIPLE_BUNDLE) {
            shopPageHomeTracking.clickAtcProductBundleMultiple(
                atcBundleModel = atcBundleModel,
                bundleName = bundleName,
                bundleId = shopHomeProductBundleDetailUiModel.bundleId,
                bundlePrice = shopHomeProductBundleDetailUiModel.displayPrice,
                quantity = shopHomeProductBundleDetailUiModel.minOrder.toString(),
                shopName = shopName,
                shopType = customDimensionShopPage.shopType.orEmpty(),
                bundlePriceCut = shopHomeProductBundleDetailUiModel.discountPercentage.toString(),
                shopId = shopId,
                userId = userId,
                isFestivity = shopHomeProductBundleDetailUiModel.isFestivity
            )
        } else if (bundleType == ShopPageConstant.BundleType.SINGLE_BUNDLE) {
            shopPageHomeTracking.clickAtcProductBundleSingle(
                atcBundleModel = atcBundleModel,
                bundleName = bundleName,
                bundleId = shopHomeProductBundleDetailUiModel.bundleId,
                bundlePrice = shopHomeProductBundleDetailUiModel.displayPrice,
                quantity = shopHomeProductBundleDetailUiModel.minOrder.toString(),
                shopName = shopName,
                shopType = customDimensionShopPage.shopType.orEmpty(),
                bundlePriceCut = shopHomeProductBundleDetailUiModel.discountPercentage.toString(),
                shopId = shopId,
                userId = userId,
                selectedPackage = shopHomeProductBundleDetailUiModel.minOrderWording,
                isFestivity = shopHomeProductBundleDetailUiModel.isFestivity
            )
        }
    }

    private fun goToBundlingSelectionPage(bundleId: String) {
        val bundlingSelectionPageAppLink = UriUtil.buildUri(
            ApplinkConstInternalMechant.MERCHANT_PRODUCT_BUNDLE,
            ShopHomeProductBundleItemUiModel.DEFAULT_BUNDLE_PRODUCT_PARENT_ID
        )
        val bundleAppLinkWithParams = Uri.parse(bundlingSelectionPageAppLink).buildUpon()
            .appendQueryParameter(ApplinkConstInternalMechant.QUERY_PARAM_BUNDLE_ID, bundleId)
            .appendQueryParameter(ApplinkConstInternalMechant.QUERY_PARAM_PAGE_SOURCE, ApplinkConstInternalMechant.SOURCE_SHOP_PAGE)
            .build()
            .toString()
        context?.let {
            val bspIntent = RouteManager.getIntent(it, bundleAppLinkWithParams)
            startActivity(bspIntent)
        }
    }

    private fun showErrorDialogAtcBundle(
        errorTitle: String,
        errorDescription: String,
        ctaText: String,
        widgetLayoutParams: ShopPageWidgetUiModel
    ) {
        context?.let {
            DialogUnify(it, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(errorTitle)
                setDescription(errorDescription)
                setPrimaryCTAText(ctaText)
                setPrimaryCTAClickListener {
                    dismiss()
                    getWidgetContentData(mutableListOf(widgetLayoutParams))
                }
            }.show()
        }
    }

    override fun onShowcaseListWidgetImpression(model: ShopHomeShowcaseListSliderUiModel, position: Int) {
        sendShopHomeWidgetImpressionTracker(
            ShopPageTrackingConstant.VALUE_SHOP_DECOR_SHOWCASE,
            model.name,
            model.widgetId,
            ShopUtil.getActualPositionFromIndex(position),
            model.widgetMasterId,
            model.isFestivity
        )
    }

    override fun onShowcaseListWidgetItemClicked(
        shopHomeShowcaseListSliderUiModel: ShopHomeShowcaseListSliderUiModel,
        showcaseItem: ShopHomeShowcaseListItemUiModel,
        position: Int,
        parentPosition: Int
    ) {
        sendShopHomeWidgetClickedTracker(
            ShopPageTrackingConstant.VALUE_SHOP_DECOR_SHOWCASE,
            shopHomeShowcaseListSliderUiModel.name,
            shopHomeShowcaseListSliderUiModel.widgetId,
            ShopUtil.getActualPositionFromIndex(parentPosition),
            shopHomeShowcaseListSliderUiModel.widgetMasterId,
            shopHomeShowcaseListSliderUiModel.isFestivity
        )
        shopPageHomeTracking.clickShowcaseListWidgetItem(
            showcaseItem,
            ShopUtil.getActualPositionFromIndex(position),
            customDimensionShopPage,
            userId
        )
        val intent = ShopProductListResultActivity.createIntent(
            activity,
            shopId,
            "",
            showcaseItem.id,
            "",
            "",
            shopRef
        )
        intent.putExtra(ShopCommonExtraConstant.EXTRA_IS_NEED_TO_RELOAD_DATA, true)
        startActivity(intent)
    }

    override fun onShowcaseListWidgetItemImpression(
        showcaseItem: ShopHomeShowcaseListItemUiModel,
        position: Int
    ) {
        shopPageHomeTracking.onImpressionShowcaseListWidgetItem(
            showcaseItem,
            ShopUtil.getActualPositionFromIndex(position),
            customDimensionShopPage,
            userId
        )
    }

    override fun onDisplayItemImpression(
        displayWidgetUiModel: ShopHomeDisplayWidgetUiModel?,
        displayWidgetItem: ShopHomeDisplayWidgetUiModel.DisplayWidgetItem,
        parentPosition: Int,
        adapterPosition: Int
    ) {
        val destinationLink: String
        val creativeUrl: String
        when (displayWidgetUiModel?.name ?: "") {
            VIDEO -> {
                destinationLink = displayWidgetItem.videoUrl
                creativeUrl = displayWidgetItem.videoUrl
            }
            else -> {
                destinationLink = displayWidgetItem.appLink
                creativeUrl = displayWidgetItem.imageUrl
            }
        }
        shopPageHomeTracking.impressionDisplayWidget(
            false,
            shopId,
            shopPageHomeLayoutUiModel?.masterLayoutId.toString(),
            displayWidgetUiModel?.name ?: "",
            displayWidgetUiModel?.widgetId ?: "",
            ShopUtil.getActualPositionFromIndex(parentPosition),
            displayWidgetUiModel?.header?.ratio ?: "",
            destinationLink,
            creativeUrl,
            ShopUtil.getActualPositionFromIndex(adapterPosition),
            customDimensionShopPage
        )
    }

    override fun onDisplayItemClicked(
        displayWidgetUiModel: ShopHomeDisplayWidgetUiModel?,
        displayWidgetItem: ShopHomeDisplayWidgetUiModel.DisplayWidgetItem,
        parentPosition: Int,
        adapterPosition: Int
    ) {
        val destinationLink: String
        val creativeUrl: String
        when (displayWidgetUiModel?.name ?: "") {
            VIDEO -> {
                destinationLink = displayWidgetItem.videoUrl
                creativeUrl = displayWidgetItem.videoUrl
            }
            else -> {
                destinationLink = displayWidgetItem.appLink
                creativeUrl = displayWidgetItem.imageUrl
            }
        }
        val segmentName = when (displayWidgetUiModel?.name.orEmpty()) {
            VIDEO -> {
                ShopPageTrackingConstant.VALUE_SHOP_DECOR_VIDEO
            }
            else -> {
                ShopPageTrackingConstant.VALUE_SHOP_DECOR_BANNER
            }
        }
        sendShopHomeWidgetClickedTracker(
            segmentName,
            displayWidgetUiModel?.name.orEmpty(),
            displayWidgetUiModel?.widgetId.orEmpty(),
            ShopUtil.getActualPositionFromIndex(parentPosition),
            displayWidgetUiModel?.widgetMasterId.orEmpty(),
            displayWidgetUiModel?.isFestivity.orFalse()
        )
        shopPageHomeTracking.clickDisplayWidget(
            false,
            shopId,
            shopPageHomeLayoutUiModel?.masterLayoutId.toString(),
            displayWidgetUiModel?.name ?: "",
            displayWidgetUiModel?.widgetId ?: "",
            ShopUtil.getActualPositionFromIndex(parentPosition),
            displayWidgetUiModel?.header?.ratio ?: "",
            destinationLink,
            creativeUrl,
            ShopUtil.getActualPositionFromIndex(adapterPosition),
            customDimensionShopPage
        )
        context?.let {
            if (displayWidgetItem.appLink.isNotEmpty()) {
                RouteManager.route(it, displayWidgetItem.appLink)
            }
        }
    }

    override fun loadYouTubeData(videoUrl: String, widgetId: String) {
        if (videoUrl.isNotEmpty()) {
            viewModel?.getVideoYoutube(videoUrl, widgetId)
        }
    }

    override fun onDisplayWidgetImpression(model: ShopHomeDisplayWidgetUiModel, position: Int) {
        val segmentName = when (model.name) {
            VIDEO -> {
                ShopPageTrackingConstant.VALUE_SHOP_DECOR_VIDEO
            }
            else -> {
                ShopPageTrackingConstant.VALUE_SHOP_DECOR_BANNER
            }
        }
        sendShopHomeWidgetImpressionTracker(
            segmentName,
            model.name,
            model.widgetId,
            ShopUtil.getActualPositionFromIndex(position),
            model.widgetMasterId,
            model.isFestivity
        )
    }

    private fun sendShopHomeWidgetImpressionTracker(
        segmentName: String,
        widgetName: String,
        widgetId: String,
        position: Int,
        widgetMasterId: String,
        isFestivity: Boolean
    ) {
        if (!isOwner) {
            shopPageHomeTracking.onImpressionShopHomeWidget(
                segmentName,
                widgetName,
                widgetId,
                position,
                shopId,
                userId,
                widgetMasterId,
                isFestivity
            )
        }
    }

    fun sendShopHomeWidgetClickedTracker(
        segmentName: String,
        widgetName: String,
        widgetId: String,
        position: Int,
        widgetMasterId: String,
        isFestivity: Boolean
    ) {
        if (!isOwner) {
            shopPageHomeTracking.onClickedShopHomeWidget(
                segmentName,
                widgetName,
                widgetId,
                position,
                shopId,
                userId,
                widgetMasterId,
                isFestivity
            )
        }
    }

    override fun onVoucherReloaded() {
        getMvcWidgetData()
    }

    override fun onVoucherImpression(model: ShopHomeVoucherUiModel, position: Int) {
        sendShopHomeWidgetImpressionTracker(
            ShopPageTrackingConstant.VALUE_SHOP_DECOR_VOUCHER,
            model.name,
            model.widgetId,
            position,
            model.widgetMasterId,
            model.isFestivity
        )
        shopPageHomeTracking.impressionSeeEntryPointMerchantVoucherCoupon(shopId, viewModel?.userId)
    }

    override fun onVoucherTokoMemberInformationImpression(
        model: ShopHomeVoucherUiModel,
        position: Int
    ) {
        sendShopHomeWidgetImpressionTracker(
            ShopPageTrackingConstant.VALUE_SHOP_DECOR_VOUCHER,
            model.name,
            model.widgetId,
            position,
            model.widgetMasterId,
            model.isFestivity
        )
        shopPageHomeTracking.impressionSeeEntryPointMerchantVoucherCouponTokoMemberInformation(
            shopId
        )
    }

    override fun onAllProductItemClicked(
        itemPosition: Int,
        shopHomeProductViewModel: ShopHomeProductUiModel?
    ) {
        val realItemPositonOnTheList = itemPosition - shopHomeAdapter?.getAllProductWidgetPosition().orZero()
        shopHomeProductViewModel?.let {
            if (!isOwner) {
                shopPageHomeTracking.clickProduct(
                    isLogin,
                    shopHomeProductViewModel.name ?: "",
                    shopHomeProductViewModel.id ?: "",
                    shopHomeProductViewModel.displayedPrice ?: "",
                    shopName,
                    ShopUtil.getActualPositionFromIndex(realItemPositonOnTheList),
                    CustomDimensionShopPageAttribution.create(
                        shopId,
                        isOfficialStore,
                        isGoldMerchant,
                        shopHomeProductViewModel.id,
                        shopAttribution,
                        shopRef,
                        shopHomeProductViewModel.labelGroupList.any { it.position == LABEL_GROUP_POSITION_FULFILLMENT },
                        shopHomeProductViewModel.isShowFreeOngkir
                    ),
                    shopProductFilterParameter?.getListFilterForTracking().orEmpty(),
                    shopId,
                    getSelectedTabName()
                )
            }
            goToPDP(it.productUrl)
        }
    }

    override fun onAllProductItemImpression(
        itemPosition: Int,
        shopHomeProductViewModel: ShopHomeProductUiModel?
    ) {
        val realItemPositonOnTheList = itemPosition - shopHomeAdapter?.getAllProductWidgetPosition().orZero()
        shopHomeProductViewModel?.let {
            if (!isOwner) {
                shopPageHomeTracking.impressionProduct(
                    isLogin,
                    shopHomeProductViewModel.name ?: "",
                    shopHomeProductViewModel.id ?: "",
                    shopHomeProductViewModel.displayedPrice ?: "",
                    shopName,
                    ShopUtil.getActualPositionFromIndex(realItemPositonOnTheList),
                    CustomDimensionShopPageAttribution.create(
                        shopId,
                        isOfficialStore,
                        isGoldMerchant,
                        shopHomeProductViewModel.id,
                        shopAttribution,
                        shopRef,
                        shopHomeProductViewModel.labelGroupList.any { it.position == LABEL_GROUP_POSITION_FULFILLMENT },
                        shopHomeProductViewModel.isShowFreeOngkir
                    ),
                    shopProductFilterParameter?.getListFilterForTracking().orEmpty(),
                    userId,
                    getSelectedTabName()
                )
            }
        }
    }

    override fun onThreeDotsAllProductClicked(shopHomeProductViewModel: ShopHomeProductUiModel) {
        threeDotsClickShopCarouselProductUiModel = null
        threeDotsClickShopProductViewModel = shopHomeProductViewModel
        showProductCardOptions(
            this,
            ProductCardOptionsModel(
                hasWishlist = true,
                isWishlisted = shopHomeProductViewModel.isWishList,
                productId = shopHomeProductViewModel.id ?: ""
            )
        )
    }

    override fun onProductAtcDefaultClick(
        shopHomeProductUiModel: ShopHomeProductUiModel,
        quantity: Int,
        componentName: String
    ) {
        if (isLogin) {
            if (isOwner) {
                val sellerViewAtcErrorMessage = getString(R.string.shop_page_seller_atc_error_message)
                showErrorToast(sellerViewAtcErrorMessage)
            } else {
                handleAtcFlow(quantity, shopId, componentName, shopHomeProductUiModel)
            }
        } else {
            redirectToLoginPage()
        }
    }

    override fun onImpressionProductAtc(
        shopHomeProductUiModel: ShopHomeProductUiModel,
        position: Int,
        name: String
    ) {
        if (isEnableDirectPurchase) {
            val productPosition = if (name == ShopPageConstant.ShopProductCardAtc.CARD_HOME) {
                position - shopHomeAdapter?.getAllProductWidgetPosition().orZero()
            } else {
                position
            }
            trackImpressionProductAtc(
                shopHomeProductUiModel,
                ShopUtil.getActualPositionFromIndex(productPosition),
                name
            )
        }
    }

    private fun trackImpressionProductAtc(
        shopHomeProductUiModel: ShopHomeProductUiModel,
        position: Int,
        widgetName: String
    ) {
        shopPageHomeTracking.onImpressionProductAtcDirectPurchaseButton(
            shopHomeProductUiModel,
            widgetName,
            position,
            shopId,
            userId
        )
    }

    override fun onProductAtcVariantClick(shopHomeProductViewModel: ShopHomeProductUiModel) {
        AtcVariantHelper.goToAtcVariant(
            context = requireContext(),
            productId = shopHomeProductViewModel.id.orEmpty(),
            pageSource = VariantPageSource.SHOP_PAGE_PAGESOURCE,
            shopId = shopId,
            startActivitResult = this::startActivityForResult,
            showQuantityEditor = true
        )
    }

    override fun onProductAtcNonVariantQuantityEditorChanged(
        shopHomeProductUiModel: ShopHomeProductUiModel,
        quantity: Int,
        componentName: String
    ) {
        if (isLogin) {
            handleAtcFlow(quantity, shopId, componentName, shopHomeProductUiModel)
        } else {
            redirectToLoginPage()
        }
    }

    private fun handleAtcFlow(
        quantity: Int,
        shopId: String,
        componentName: String,
        shopHomeProductUiModel: ShopHomeProductUiModel
    ) {
        viewModel?.handleAtcFlow(
            quantity,
            shopId,
            componentName,
            shopHomeProductUiModel
        )
    }

    override fun onPersonalizationCarouselProductItemClicked(
        parentPosition: Int,
        itemPosition: Int,
        shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?,
        shopHomeProductViewModel: ShopHomeProductUiModel?
    ) {
        shopHomeProductViewModel?.let {
            sendShopHomeWidgetClickedTracker(
                ShopPageTrackingConstant.VALUE_SHOP_DECOR_PRODUCT,
                shopHomeCarousellProductUiModel?.name.orEmpty(),
                shopHomeCarousellProductUiModel?.widgetId.orEmpty(),
                ShopUtil.getActualPositionFromIndex(parentPosition),
                shopHomeCarousellProductUiModel?.widgetMasterId.orEmpty(),
                shopHomeCarousellProductUiModel?.isFestivity.orFalse()
            )
            shopPageHomeTracking.clickProductPersonalization(
                isOwner,
                shopHomeProductViewModel.name ?: "",
                shopHomeProductViewModel.id ?: "",
                shopHomeProductViewModel.displayedPrice ?: "",
                shopHomeProductViewModel.recommendationType ?: "",
                shopHomeProductViewModel.categoryBreadcrumbs ?: "",
                shopName,
                viewModel?.userId.orEmpty(),
                ShopUtil.getActualPositionFromIndex(itemPosition),
                shopHomeCarousellProductUiModel?.header?.title ?: "",
                shopHomeCarousellProductUiModel?.name ?: "",
                customDimensionShopPage
            )
            goToPDP(it.productUrl)
        }
    }

    override fun onPersonalizationReminderCarouselProductItemClicked(
        parentPosition: Int,
        itemPosition: Int,
        shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?,
        shopHomeProductViewModel: ShopHomeProductUiModel?
    ) {
        sendShopHomeWidgetClickedTracker(
            ShopPageTrackingConstant.VALUE_SHOP_DECOR_PRODUCT,
            shopHomeCarousellProductUiModel?.name.orEmpty(),
            shopHomeCarousellProductUiModel?.widgetId.orEmpty(),
            ShopUtil.getActualPositionFromIndex(parentPosition),
            shopHomeCarousellProductUiModel?.widgetMasterId.orEmpty(),
            shopHomeCarousellProductUiModel?.isFestivity.orFalse()
        )
        shopHomeProductViewModel?.let {
            shopPageHomeTracking.clickProductPersonalizationReminder(
                isOwner,
                isLogin,
                shopHomeProductViewModel.name ?: "",
                shopHomeProductViewModel.id ?: "",
                shopHomeProductViewModel.displayedPrice ?: "",
                shopHomeProductViewModel.recommendationType ?: "",
                shopName,
                viewModel?.userId.orEmpty(),
                ShopUtil.getActualPositionFromIndex(itemPosition),
                shopHomeCarousellProductUiModel?.header?.title ?: "",
                customDimensionShopPage,
                shopHomeProductViewModel?.categoryBreadcrumbs ?: ""
            )
            goToPDP(it.productUrl)
        }
    }

    override fun onPersonalizationTrendingCarouselProductItemClicked(
        parentPosition: Int,
        itemPosition: Int,
        shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel,
        shopHomeProductViewModel: ShopHomeProductUiModel
    ) {
        sendShopHomeWidgetClickedTracker(
            ShopPageTrackingConstant.VALUE_SHOP_DECOR_PRODUCT,
            shopHomeCarousellProductUiModel.name,
            shopHomeCarousellProductUiModel.widgetId,
            ShopUtil.getActualPositionFromIndex(parentPosition),
            shopHomeCarousellProductUiModel.widgetMasterId,
            shopHomeCarousellProductUiModel.isFestivity
        )
        shopPageHomeTracking.clickProductPersonalizationTrendingWidget(
            itemPosition,
            shopHomeProductViewModel,
            shopId,
            userId
        )
        goToPDP(shopHomeProductViewModel.productUrl)
    }

    override fun onCarouselPersonalizationProductItemClickAddToCart(
        parentPosition: Int,
        itemPosition: Int,
        shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?,
        shopHomeProductViewModel: ShopHomeProductUiModel?,
        isOcc: Boolean
    ) {
        if (isLogin) {
            shopHomeProductViewModel?.let { product ->
                if (isOcc) {
                    viewModel?.addProductToCartOcc(
                        product,
                        shopId,
                        {
                            onSuccessAddToCart(
                                it,
                                shopHomeProductViewModel,
                                parentPosition,
                                shopHomeCarousellProductUiModel,
                                isPersonalizationWidget = true,
                                isOcc = isOcc
                            )
                        },
                        {
                            if (!ShopUtil.isExceptionIgnored(it)) {
                                ShopUtil.logShopPageP2BuyerFlowAlerting(
                                    tag = SHOP_PAGE_BUYER_FLOW_TAG,
                                    functionName = ShopHomeViewModel::addProductToCartOcc.name,
                                    liveDataName = "",
                                    userId = userId,
                                    shopId = shopId,
                                    shopName = shopName,
                                    errorMessage = ErrorHandler.getErrorMessage(context, it),
                                    stackTrace = Log.getStackTraceString(it),
                                    errType = SHOP_PAGE_HOME_TAB_BUYER_FLOW_TAG
                                )
                            }
                            onErrorAddToCart(it)
                        }
                    )
                } else {
                    viewModel?.addProductToCart(
                        product,
                        shopId,
                        {
                            onSuccessAddToCart(
                                it,
                                shopHomeProductViewModel,
                                parentPosition,
                                shopHomeCarousellProductUiModel,
                                isPersonalizationWidget = true
                            )
                        },
                        {
                            if (!ShopUtil.isExceptionIgnored(it)) {
                                ShopUtil.logShopPageP2BuyerFlowAlerting(
                                    tag = SHOP_PAGE_BUYER_FLOW_TAG,
                                    functionName = ShopHomeViewModel::addProductToCart.name,
                                    liveDataName = "",
                                    userId = userId,
                                    shopId = shopId,
                                    shopName = shopName,
                                    errorMessage = ErrorHandler.getErrorMessage(context, it),
                                    stackTrace = Log.getStackTraceString(it),
                                    errType = SHOP_PAGE_HOME_TAB_BUYER_FLOW_TAG
                                )
                            }
                            onErrorAddToCart(it)
                        }
                    )
                }
            }
        } else {
            redirectToLoginPage()
        }
    }

    override fun onCarouselPersonalizationReminderProductItemClickAddToCart(
        parentPosition: Int,
        itemPosition: Int,
        shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?,
        shopHomeProductViewModel: ShopHomeProductUiModel?
    ) {
        if (isLogin) {
            shopHomeProductViewModel?.let { product ->
                viewModel?.addProductToCart(
                    product,
                    shopId,
                    {
                        trackClickAddToCartPersonalizationReminder(
                            it,
                            shopHomeProductViewModel,
                            shopHomeCarousellProductUiModel
                        )
                        view?.let { view ->
                            NetworkErrorHelper.showGreenCloseSnackbar(view, it.message.first())
                        }
                    },
                    {
                        if (!ShopUtil.isExceptionIgnored(it)) {
                            ShopUtil.logShopPageP2BuyerFlowAlerting(
                                tag = SHOP_PAGE_BUYER_FLOW_TAG,
                                functionName = ShopHomeViewModel::addProductToCart.name,
                                liveDataName = "",
                                userId = userId,
                                shopId = shopId,
                                shopName = shopName,
                                errorMessage = ErrorHandler.getErrorMessage(context, it),
                                stackTrace = Log.getStackTraceString(it),
                                errType = SHOP_PAGE_HOME_TAB_BUYER_FLOW_TAG
                            )
                        }
                        onErrorAddToCart(it)
                    }
                )
            }
        } else {
            redirectToLoginPage()
        }
    }

    override fun onCarouselProductPersonalizationItemImpression(
        parentPosition: Int,
        itemPosition: Int,
        shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?,
        shopHomeProductViewModel: ShopHomeProductUiModel?
    ) {
        shopPageHomeTracking.impressionProductPersonalization(
            isOwner,
            shopHomeProductViewModel?.name ?: "",
            shopHomeProductViewModel?.id ?: "",
            shopHomeProductViewModel?.displayedPrice ?: "",
            shopHomeProductViewModel?.recommendationType ?: "",
            shopHomeProductViewModel?.categoryBreadcrumbs ?: "",
            viewModel?.userId.orEmpty(),
            shopName,
            ShopUtil.getActualPositionFromIndex(itemPosition),
            shopHomeCarousellProductUiModel?.header?.title ?: "",
            shopHomeCarousellProductUiModel?.name ?: "",
            customDimensionShopPage
        )
    }

    override fun onCarouselProductPersonalizationTrendingItemImpression(
        itemPosition: Int,
        shopHomeProductViewModel: ShopHomeProductUiModel
    ) {
        shopPageHomeTracking.impressionProductPersonalizationTrendingWidget(
            itemPosition,
            shopHomeProductViewModel,
            shopId,
            userId
        )
    }

    override fun onCarouselProductPersonalizationReminderItemImpression(
        parentPosition: Int,
        itemPosition: Int,
        shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?,
        shopHomeProductViewModel: ShopHomeProductUiModel?
    ) {
        if (!isOwner) { // Not sending impression tracker if seller is seeing the widget from his own shop
            shopPageHomeTracking.impressionProductPersonalizationReminder(
                isOwner,
                isLogin,
                shopHomeProductViewModel?.name ?: "",
                shopHomeProductViewModel?.id ?: "",
                shopHomeProductViewModel?.displayedPrice ?: "",
                shopHomeProductViewModel?.recommendationType ?: "",
                viewModel?.userId.orEmpty(),
                shopName,
                ShopUtil.getActualPositionFromIndex(itemPosition),
                shopHomeCarousellProductUiModel?.header?.title ?: "",
                customDimensionShopPage,
                shopHomeProductViewModel?.categoryBreadcrumbs ?: ""
            )
        }
    }

    override fun onCarouselProductItemClicked(
        parentPosition: Int,
        itemPosition: Int,
        shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?,
        shopHomeProductViewModel: ShopHomeProductUiModel?
    ) {
        shopHomeProductViewModel?.let {
            sendShopHomeWidgetClickedTracker(
                ShopPageTrackingConstant.VALUE_SHOP_DECOR_PRODUCT,
                shopHomeCarousellProductUiModel?.name.orEmpty(),
                shopHomeCarousellProductUiModel?.widgetId.orEmpty(),
                ShopUtil.getActualPositionFromIndex(parentPosition),
                shopHomeCarousellProductUiModel?.widgetMasterId.orEmpty(),
                shopHomeCarousellProductUiModel?.isFestivity.orFalse()
            )
            if (!isOwner) {
                shopPageHomeTracking.clickProductShopDecoration(
                    ShopPageHomeTrackingMapper.mapToProductShopDecorationTrackerDataModel(
                        shopId,
                        userId,
                        it.name,
                        it.id,
                        it.displayedPrice,
                        parentPosition,
                        itemPosition,
                        shopHomeCarousellProductUiModel?.name.orEmpty(),
                        shopHomeCarousellProductUiModel?.header?.isATC.orZero(),
                        shopHomeCarousellProductUiModel?.widgetMasterId.orEmpty(),
                        shopHomeCarousellProductUiModel?.isFestivity.orFalse()
                    )
                )
            }
            goToPDP(it.productUrl)
        }
    }

    override fun onCarouselProductItemImpression(
        parentPosition: Int,
        itemPosition: Int,
        shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?,
        shopHomeProductViewModel: ShopHomeProductUiModel?
    ) {
        shopHomeProductViewModel?.let {
            if (!isOwner) {
                shopPageHomeTracking.impressionProductShopDecoration(
                    ShopPageHomeTrackingMapper.mapToProductShopDecorationTrackerDataModel(
                        shopId,
                        userId,
                        it.name,
                        it.id,
                        it.displayedPrice,
                        parentPosition,
                        itemPosition,
                        shopHomeCarousellProductUiModel?.name.orEmpty(),
                        shopHomeCarousellProductUiModel?.header?.isATC.orZero(),
                        shopHomeCarousellProductUiModel?.widgetMasterId.orEmpty(),
                        shopHomeCarousellProductUiModel?.isFestivity.orFalse()
                    )
                )
            }
        }
    }

    override fun onThreeDotsCarouselProductItemClicked(
        shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?,
        shopHomeProductViewModel: ShopHomeProductUiModel?
    ) {
        threeDotsClickShopCarouselProductUiModel = shopHomeCarousellProductUiModel
        threeDotsClickShopProductViewModel = shopHomeProductViewModel
        showProductCardOptions(
            this,
            ProductCardOptionsModel(
                hasWishlist = true,
                isWishlisted = shopHomeProductViewModel?.isWishList ?: false,
                productId = shopHomeProductViewModel?.id ?: ""
            )
        )
    }

    override fun onCarouselProductShowcaseItemClicked(
        parentPosition: Int,
        itemPosition: Int,
        shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?,
        shopHomeProductUiModel: ShopHomeProductUiModel?
    ) {
        shopHomeProductUiModel?.let {
            sendShopHomeWidgetClickedTracker(
                ShopPageTrackingConstant.VALUE_SHOP_DECOR_PRODUCT,
                shopHomeCarousellProductUiModel?.name.orEmpty(),
                shopHomeCarousellProductUiModel?.widgetId.orEmpty(),
                ShopUtil.getActualPositionFromIndex(parentPosition),
                shopHomeCarousellProductUiModel?.widgetMasterId.orEmpty(),
                shopHomeCarousellProductUiModel?.isFestivity.orFalse()
            )
            shopPageHomeTracking.clickCarouselProductShowcaseItem(
                isOwner,
                isLogin,
                shopHomeCarousellProductUiModel?.header?.etalaseId.orEmpty(),
                shopHomeProductUiModel.name ?: "",
                shopHomeProductUiModel.id ?: "",
                shopHomeProductUiModel.displayedPrice ?: "",
                shopName,
                ShopUtil.getActualPositionFromIndex(parentPosition),
                ShopUtil.getActualPositionFromIndex(itemPosition),
                shopHomeCarousellProductUiModel?.header?.title ?: "",
                userId,
                CustomDimensionShopPageAttribution.create(
                    shopId,
                    isOfficialStore,
                    isGoldMerchant,
                    shopHomeProductUiModel.id.orEmpty(),
                    shopAttribution,
                    shopRef,
                    shopHomeProductUiModel.labelGroupList.any { it.position == LABEL_GROUP_POSITION_FULFILLMENT },
                    shopHomeProductUiModel.isShowFreeOngkir
                )
            )
            goToPDP(it.productUrl)
        }
    }

    override fun onCarouselProductShowcaseItemImpression(
        parentPosition: Int,
        itemPosition: Int,
        shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?,
        shopHomeProductUiModel: ShopHomeProductUiModel?
    ) {
        shopHomeProductUiModel?.let {
            shopPageHomeTracking.impressionCarouselProductShowcaseItem(
                isOwner,
                isLogin,
                shopHomeCarousellProductUiModel?.header?.etalaseId.orEmpty(),
                shopHomeProductUiModel.name ?: "",
                shopHomeProductUiModel.id ?: "",
                shopHomeProductUiModel.displayedPrice ?: "",
                shopName,
                ShopUtil.getActualPositionFromIndex(parentPosition),
                ShopUtil.getActualPositionFromIndex(itemPosition),
                shopHomeCarousellProductUiModel?.header?.title ?: "",
                userId,
                CustomDimensionShopPageAttribution.create(
                    shopId,
                    isOfficialStore,
                    isGoldMerchant,
                    shopHomeProductUiModel.id.orEmpty(),
                    shopAttribution,
                    shopRef,
                    shopHomeProductUiModel.labelGroupList.any { it.position == LABEL_GROUP_POSITION_FULFILLMENT },
                    shopHomeProductUiModel.isShowFreeOngkir
                )
            )
        }
    }

    override fun onCarouselProductShowcaseItemClickAddToCart(
        parentPosition: Int,
        itemPosition: Int,
        shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?,
        shopHomeProductUiModel: ShopHomeProductUiModel?
    ) {
        if (isLogin) {
            shopHomeProductUiModel?.let { product ->
                viewModel?.addProductToCart(
                    product,
                    shopId,
                    {
                        sendCarouselProductShowcaseAddToCartTracker(
                            it,
                            shopHomeProductUiModel,
                            parentPosition,
                            shopHomeCarousellProductUiModel
                        )
                        view?.let { view ->
                            NetworkErrorHelper.showGreenCloseSnackbar(view, it.message.first())
                        }
                    },
                    {
                        if (!ShopUtil.isExceptionIgnored(it)) {
                            ShopUtil.logShopPageP2BuyerFlowAlerting(
                                tag = SHOP_PAGE_BUYER_FLOW_TAG,
                                functionName = ShopHomeViewModel::addProductToCart.name,
                                liveDataName = "",
                                userId = userId,
                                shopId = shopId,
                                shopName = shopName,
                                errorMessage = ErrorHandler.getErrorMessage(context, it),
                                stackTrace = Log.getStackTraceString(it),
                                errType = SHOP_PAGE_HOME_TAB_BUYER_FLOW_TAG
                            )
                        }
                        onErrorAddToCart(it)
                    }
                )
            }
        } else {
            sendCarouselProductShowcaseAddToCartTracker(
                null,
                shopHomeProductUiModel,
                parentPosition,
                shopHomeCarousellProductUiModel
            )
            redirectToLoginPage()
        }
    }

    private fun sendCarouselProductShowcaseAddToCartTracker(
        dataModelAtc: DataModel?,
        shopHomeProductUiModel: ShopHomeProductUiModel?,
        parentPosition: Int,
        shopHomeCarouselProductUiModel: ShopHomeCarousellProductUiModel?
    ) {
        shopHomeProductUiModel?.let {
            shopPageHomeTracking.addToCartCarouselProductShowcaseItem(
                isOwner,
                isLogin,
                shopHomeCarouselProductUiModel?.header?.etalaseId.orEmpty(),
                shopHomeProductUiModel.name ?: "",
                shopHomeProductUiModel.id ?: "",
                shopHomeProductUiModel.displayedPrice ?: "",
                dataModelAtc?.quantity ?: VALUE_INT_ONE,
                shopName,
                ShopUtil.getActualPositionFromIndex(parentPosition),
                shopHomeCarouselProductUiModel?.header?.title ?: "",
                userId,
                dataModelAtc?.cartId.orEmpty(),
                CustomDimensionShopPageAttribution.create(
                    shopId,
                    isOfficialStore,
                    isGoldMerchant,
                    shopHomeProductUiModel.id.orEmpty(),
                    shopAttribution,
                    shopRef,
                    shopHomeProductUiModel.labelGroupList.any { it.position == LABEL_GROUP_POSITION_FULFILLMENT },
                    shopHomeProductUiModel.isShowFreeOngkir
                )
            )
        }
    }

    override fun onCarouselProductWidgetImpression(
        adapterPosition: Int,
        model: ShopHomeCarousellProductUiModel
    ) {
        val segmentName = ShopPageTrackingConstant.VALUE_SHOP_DECOR_PRODUCT
        sendShopHomeWidgetImpressionTracker(
            segmentName,
            model.name,
            model.widgetId,
            ShopUtil.getActualPositionFromIndex(adapterPosition),
            model.widgetMasterId,
            model.isFestivity
        )
    }

    override fun onCarouselProductPersonalizationTrendingWidgetImpression() {
        shopPageHomeTracking.impressionPersonalizationTrendingWidget(shopId, userId)
    }

    private fun trackClickWishlist(
        shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?,
        shopHomeProductViewModel: ShopHomeProductUiModel,
        isWishlist: Boolean
    ) {
        val customDimensionShopPageProduct = CustomDimensionShopPageProduct.create(
            shopId,
            isOfficialStore,
            isGoldMerchant,
            shopHomeProductViewModel.id,
            shopRef
        )
        shopPageHomeTracking.clickWishlist(
            isOwner,
            isWishlist,
            shopPageHomeLayoutUiModel?.masterLayoutId.toString(),
            isLogin,
            shopHomeCarousellProductUiModel?.header?.title ?: "",
            shopHomeCarousellProductUiModel?.widgetId ?: "",
            shopHomeProductViewModel.id ?: "",
            customDimensionShopPageProduct
        )
    }

    override fun onCtaClicked(shopHomeCarouselProductUiModel: ShopHomeCarousellProductUiModel?) {
        if (!isOwner) {
            shopPageHomeTracking.clickCta(
                ShopPageHomeTrackingMapper.mapToShopHomeCarouselProductWidgetClickCtaTrackerModel(
                    shopId,
                    userId,
                    shopHomeCarouselProductUiModel?.widgetMasterId.orEmpty(),
                    shopHomeCarouselProductUiModel?.isFestivity.orFalse()
                )
            )
        }

        context?.let {
            RouteManager.route(it, shopHomeCarouselProductUiModel?.header?.ctaLink)
        }
    }

    override fun chooseProduct() {
        context?.let {
            RouteManager.route(it, ApplinkConst.PRODUCT_ADD)
        }
    }

    private fun goToWishlist() {
        RouteManager.route(context, ApplinkConst.NEW_WISHLIST)
    }

    private fun goToCart() {
        RouteManager.route(context, ApplinkConst.CART)
    }

    override fun onCarouselProductItemClickAddToCart(
        parentPosition: Int,
        itemPosition: Int,
        shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?,
        shopHomeProductViewModel: ShopHomeProductUiModel?
    ) {
        if (isLogin) {
            shopHomeProductViewModel?.let { product ->
                viewModel?.addProductToCart(
                    product,
                    shopId,
                    {
                        onSuccessAddToCart(
                            it,
                            shopHomeProductViewModel,
                            parentPosition,
                            shopHomeCarousellProductUiModel
                        )
                    },
                    {
                        if (!ShopUtil.isExceptionIgnored(it)) {
                            ShopUtil.logShopPageP2BuyerFlowAlerting(
                                tag = SHOP_PAGE_BUYER_FLOW_TAG,
                                functionName = ShopHomeViewModel::addProductToCart.name,
                                liveDataName = "",
                                userId = userId,
                                shopId = shopId,
                                shopName = shopName,
                                errorMessage = ErrorHandler.getErrorMessage(context, it),
                                stackTrace = Log.getStackTraceString(it),
                                errType = SHOP_PAGE_HOME_TAB_BUYER_FLOW_TAG
                            )
                        }
                        onErrorAddToCart(it)
                    }
                )
            }
        } else {
            trackClickAddToCart(
                null,
                shopHomeProductViewModel,
                parentPosition,
                shopHomeCarousellProductUiModel
            )
            redirectToLoginPage()
        }
    }

    fun goToPDP(pdpAppLink: String) {
        val updatedPdpAppLink = createAffiliateLink(pdpAppLink)
        context?.let {
            val intent = RouteManager.getIntent(
                context,
                updatedPdpAppLink
            )
            startActivity(intent)
        }
    }

    private fun createAffiliateLink(basePdpAppLink: String): String {
        return (activity as? ShopPageSharedListener)?.createPdpAffiliateLink(basePdpAppLink).orEmpty()
    }

    protected fun redirectToLoginPage(requestCode: Int = REQUEST_CODE_USER_LOGIN) {
        context?.let {
            val intent = RouteManager.getIntent(it, ApplinkConst.LOGIN)
            startActivityForResult(intent, requestCode)
        }
    }

    private fun showToastSuccess(message: String) {
        activity?.run {
            view?.let { Toaster.build(it, message).setAnchorViewToShopHeaderBottomViewContainer(getShopHeaderBottomViewContainer()).show() }
        }
    }

    private fun showToastSuccess(
        message: String,
        ctaText: String = "",
        ctaAction: View.OnClickListener? = null
    ) {
        activity?.run {
            view?.let {
                ctaAction?.let { ctaClickListener ->
                    Toaster.build(
                        it,
                        message,
                        Snackbar.LENGTH_LONG,
                        Toaster.TYPE_NORMAL,
                        ctaText,
                        ctaClickListener
                    ).setAnchorViewToShopHeaderBottomViewContainer(getShopHeaderBottomViewContainer()).show()
                } ?: Toaster.build(
                    it,
                    message,
                    Snackbar.LENGTH_LONG,
                    Toaster.TYPE_NORMAL,
                    ctaText
                ).setAnchorViewToShopHeaderBottomViewContainer(getShopHeaderBottomViewContainer()).show()
            }
        }
    }

    protected fun showErrorToast(message: String) {
        activity?.run {
            view?.let { Toaster.build(it, message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).setAnchorViewToShopHeaderBottomViewContainer(getShopHeaderBottomViewContainer()).show() }
        }
    }

    private fun handleWishlistAction(productCardOptionsModel: ProductCardOptionsModel) {
        if (!productCardOptionsModel.wishlistResult.isUserLoggedIn) {
            threeDotsClickShopProductViewModel?.let {
                trackClickWishlist(threeDotsClickShopCarouselProductUiModel, it, true)
            }
            redirectToLoginPage()
        } else {
            handleWishlistActionForLoggedInUser(productCardOptionsModel)
        }
    }

    private fun handleWishlistActionForLoggedInUser(productCardOptionsModel: ProductCardOptionsModel) {
        viewModel?.clearGetShopProductUseCase()
        if (productCardOptionsModel.wishlistResult.isAddWishlist) {
            handleWishlistActionAddToWishlistV2(productCardOptionsModel)
        } else {
            handleWishlistActionRemoveFromWishlistV2(productCardOptionsModel.wishlistResult)
        }
    }

    private fun handleWishlistActionAddToWishlistV2(productCardOptionsModel: ProductCardOptionsModel) {
        context?.let { context ->
            view?.let { v ->
                AddRemoveWishlistV2Handler.showAddToWishlistV2SuccessToaster(productCardOptionsModel.wishlistResult, context, v)
            }
        }
        if (productCardOptionsModel.wishlistResult.isSuccess) {
            shopHomeAdapter?.updateWishlistProduct(threeDotsClickShopProductViewModel?.id ?: "", true)
            threeDotsClickShopProductViewModel?.let {
                trackClickWishlist(
                    threeDotsClickShopCarouselProductUiModel,
                    it,
                    true
                )
            }
        }
    }

    private fun handleWishlistActionRemoveFromWishlistV2(wishlistResult: ProductCardOptionsModel.WishlistResult) {
        context?.let { context ->
            view?.let { v ->
                AddRemoveWishlistV2Handler.showRemoveWishlistV2SuccessToaster(wishlistResult, context, v)
            }
        }
        if (wishlistResult.isSuccess) {
            shopHomeAdapter?.updateWishlistProduct(threeDotsClickShopProductViewModel?.id ?: "", false)
            threeDotsClickShopProductViewModel?.let {
                trackClickWishlist(
                    threeDotsClickShopCarouselProductUiModel,
                    it,
                    false
                )
            }
        }
    }

    fun clearCache() {
        viewModel?.clearCache()
    }

    private fun redirectToShopSortPickerPage() {
        context?.run {
            val intent = ShopProductSortActivity.createIntent(activity, sortId)
            startActivityForResult(intent, REQUEST_CODE_SORT)
        }
    }

    private fun redirectToEtalasePicker() {
        context?.let {
            val bundle = Bundle()
            bundle.putString(ShopShowcaseParamConstant.EXTRA_SELECTED_ETALASE_ID, "")
            bundle.putBoolean(ShopShowcaseParamConstant.EXTRA_IS_SHOW_DEFAULT, true)
            bundle.putBoolean(ShopShowcaseParamConstant.EXTRA_IS_SHOW_ZERO_PRODUCT, false)
            bundle.putString(ShopShowcaseParamConstant.EXTRA_SHOP_ID, shopId)
            bundle.putString(
                ShopShowcaseParamConstant.EXTRA_SHOP_TYPE,
                customDimensionShopPage.shopType
            )

            val intent = RouteManager.getIntent(
                context,
                ApplinkConstInternalMechant.MERCHANT_SHOP_SHOWCASE_LIST
            )
            intent.putExtra(EXTRA_BUNDLE, bundle)
            startActivityForResult(intent, REQUEST_CODE_ETALASE)
        }
    }

    override fun onEtalaseFilterClicked() {
        if (!isOwner) {
            shopPageHomeTracking.clickEtalaseChip(getSelectedTabName(), shopId, userId)
        }
        redirectToEtalasePicker()
    }

    override fun onSortFilterClicked() {
        shopPageHomeTracking.clickSort(isOwner, customDimensionShopPage)
        redirectToShopSortPickerPage()
    }

    override fun onClearFilterClicked() {
        if (shopHomeAdapter?.isLoading == true) {
            return
        }
        shopPageHomeTracking.clickClearFilter(
            isOwner,
            customDimensionShopPage
        )
        changeShopProductFilterParameterSharedData()
        changeSortData("")
        scrollToEtalaseTitlePosition()
    }

    override fun setSortFilterMeasureHeight(measureHeight: Int) {}

    override fun onFilterClicked() {
        shopPageHomeTracking.clickFilterChips(productListName, customDimensionShopPage)
        showBottomSheetFilter()
    }

    private fun showBottomSheetFilter() {
        val mapParameter = if (sortId.isNotEmpty()) {
            shopProductFilterParameter?.getMapData()
        } else {
            shopProductFilterParameter?.getMapDataWithDefaultSortId()
        }
        sortFilterBottomSheet = SortFilterBottomSheet()
        sortFilterBottomSheet?.show(
            requireFragmentManager(),
            mapParameter,
            null,
            this
        )
        sortFilterBottomSheet?.setOnDismissListener {
            sortFilterBottomSheet = null
        }
        viewModel?.getBottomSheetFilterData(shopId)
    }

    private fun refreshProductList() {
        shopHomeAdapter?.removeProductList()
        shopHomeAdapter?.addProductGridListPlaceHolder()
        endlessRecyclerViewScrollListener.resetState()
        getProductList(1)
    }

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
            ShopUtil.getActualPositionFromIndex(parentPosition),
            shopHomeNewProductLaunchCampaignUiModel.widgetMasterId,
            shopHomeNewProductLaunchCampaignUiModel.isFestivity
        )
        shopHomeNewProductLaunchCampaignUiModel.data?.firstOrNull()?.let {
            if (!isOwner) {
                shopPageHomeTracking.clickCampaignWidgetProduct(
                    ShopPageHomeTrackingMapper.mapToShopHomeCampaignWidgetProductTrackerModel(
                        shopId,
                        userId,
                        it.campaignId,
                        it.name,
                        it.statusCampaign,
                        parentPosition,
                        itemPosition,
                        shopHomeNewProductLaunchCampaignUiModel.widgetMasterId,
                        shopHomeNewProductLaunchCampaignUiModel.isFestivity,
                        shopHomeProductViewModel?.id.orEmpty(),
                        shopHomeProductViewModel?.name.orEmpty(),
                        shopHomeProductViewModel?.displayedPrice.orEmpty()
                    )
                )
            }
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
            if (!isOwner) {
                shopPageHomeTracking.onImpressionCampaignWidgetProduct(
                    ShopPageHomeTrackingMapper.mapToShopHomeCampaignWidgetProductTrackerModel(
                        shopId,
                        userId,
                        it.campaignId,
                        it.name,
                        it.statusCampaign,
                        parentPosition,
                        itemPosition,
                        shopHomeNewProductLaunchCampaignUiModel.widgetMasterId,
                        shopHomeNewProductLaunchCampaignUiModel.isFestivity,
                        shopHomeProductViewModel?.id.orEmpty(),
                        shopHomeProductViewModel?.name.orEmpty(),
                        shopHomeProductViewModel?.displayedPrice.orEmpty()
                    )
                )
            }
        }
    }

    override fun onClickTncCampaignNplWidget(model: ShopHomeNewProductLaunchCampaignUiModel) {
        model.data?.firstOrNull()?.let {
            shopPageHomeTracking.clickTncButton(isOwner, it.statusCampaign, customDimensionShopPage)
            showTncBottomSheet(
                it.campaignId,
                it.statusCampaign,
                it.dynamicRule.listDynamicRoleData.map { it.ruleID }
            )
        }
    }

    override fun onClickTncFlashSaleWidget(model: ShopHomeFlashSaleUiModel) {
        model.data?.firstOrNull()?.let {
            shopPageHomeTracking.onClickTnCButtonFlashSaleWidget(
                campaignId = it.campaignId,
                shopId = shopId,
                userId = userId,
                isOwner = isOwner
            )
            showFlashTncSaleBottomSheet(it.campaignId)
        }
    }

    override fun onClickSeeAllFlashSaleWidget(model: ShopHomeFlashSaleUiModel) {
        context?.run {
            if (shopId.isNotBlank() && model.header.ctaLink.isNotBlank()) {
                model.data?.firstOrNull()?.let { flashSaleItem ->
                    if (!isOwner) {
                        shopPageHomeTracking.clickCtaSeeAllCampaignWidget(
                            ShopPageHomeTrackingMapper.mapToShopHomeCampaignWidgetClickCtaSeeAllTrackerModel(
                                shopId,
                                userId,
                                flashSaleItem.campaignId,
                                flashSaleItem.name,
                                flashSaleItem.statusCampaign,
                                model.widgetMasterId,
                                model.isFestivity
                            )
                        )
                    }
                }
                RouteManager.route(this, model.header.ctaLink)
            }
        }
    }

    private fun showTncBottomSheet(
        campaignId: String,
        statusCampaign: String,
        listRuleId: List<String>
    ) {
        val bottomSheet = ShopHomeNplCampaignTncBottomSheet.createInstance(
            campaignId,
            statusCampaign,
            shopId,
            isOfficialStore,
            isGoldMerchant,
            listRuleId
        )
        bottomSheet.show(childFragmentManager, "")
    }

    private fun showFlashTncSaleBottomSheet(campaignId: String) {
        val bottomSheet = ShopHomeFlashSaleTncBottomSheet.createInstance(campaignId)
        bottomSheet.show(childFragmentManager, "")
    }

    override fun onClickRemindMe(model: ShopHomeNewProductLaunchCampaignUiModel) {
        viewModel?.let {
            val campaignId = model.data?.firstOrNull()?.campaignId.orEmpty()
            if (it.isLogin) {
                shopHomeAdapter?.showNplRemindMeLoading(campaignId)
                handleClickRemindMe(model)
            } else {
                model.data?.firstOrNull()?.let {
                    if (!isOwner) {
                        shopPageHomeTracking.clickReminderCampaignWidget(
                            ShopPageHomeTrackingMapper.mapToShopHomeCampaignWidgetClickRemindMeTrackerModel(
                                shopId,
                                userId,
                                it.campaignId,
                                it.name,
                                it.statusCampaign,
                                model.widgetMasterId,
                                model.isFestivity
                            )
                        )
                    }
                }
                setNplRemindMeClickedCampaignId(campaignId)
                redirectToLoginPage()
            }
        }
    }

    override fun onClickFlashSaleReminder(model: ShopHomeFlashSaleUiModel) {
        viewModel?.let {
            val campaignId = model.data?.firstOrNull()?.campaignId.orEmpty()
            if (it.isLogin) {
                handleFlashSaleClickReminder(model)
            } else {
                setFlashSaleRemindMeClickedCampaignId(campaignId)
                redirectToLoginPage()
            }
            model.data?.firstOrNull()?.let {
                if (!isOwner) {
                    shopPageHomeTracking.clickReminderCampaignWidget(
                        ShopPageHomeTrackingMapper.mapToShopHomeCampaignWidgetClickRemindMeTrackerModel(
                            shopId,
                            userId,
                            it.campaignId,
                            it.name,
                            it.statusCampaign,
                            model.widgetMasterId,
                            model.isFestivity
                        )
                    )
                }
            }
        }
    }

    override fun onFlashSaleProductClicked(model: ShopHomeProductUiModel, widgetModel: ShopHomeFlashSaleUiModel, position: Int, parentPosition: Int) {
        widgetModel.data?.firstOrNull()?.let {
            if (!isOwner) {
                shopPageHomeTracking.clickCampaignWidgetProduct(
                    ShopPageHomeTrackingMapper.mapToShopHomeCampaignWidgetProductTrackerModel(
                        shopId,
                        userId,
                        it.campaignId,
                        it.name,
                        it.statusCampaign,
                        parentPosition,
                        position,
                        widgetModel.widgetMasterId,
                        widgetModel.isFestivity,
                        model.id,
                        model.name,
                        model.displayedPrice
                    )
                )
            }
        }
        goToPDP(model.productUrl)
    }

    override fun onFlashSaleProductImpression(
        shopHomeProductUiModel: ShopHomeProductUiModel,
        flashSaleUiModel: ShopHomeFlashSaleUiModel?,
        position: Int,
        parentPosition: Int
    ) {
        flashSaleUiModel?.data?.firstOrNull()?.let {
            if (!isOwner) {
                shopPageHomeTracking.onImpressionCampaignWidgetProduct(
                    ShopPageHomeTrackingMapper.mapToShopHomeCampaignWidgetProductTrackerModel(
                        shopId,
                        userId,
                        it.campaignId,
                        it.name,
                        it.statusCampaign,
                        parentPosition,
                        position,
                        flashSaleUiModel.widgetMasterId,
                        flashSaleUiModel.isFestivity,
                        shopHomeProductUiModel.id,
                        shopHomeProductUiModel.name,
                        shopHomeProductUiModel.displayedPrice
                    )
                )
            }
        }
    }

    override fun onPlaceHolderClickSeeAll(model: ShopHomeFlashSaleUiModel) {
        context?.run {
            if (shopId.isNotBlank() && model.header.ctaLink.isNotBlank()) {
                RouteManager.route(this, model.header.ctaLink)
            }
        }
    }

    private fun handleClickRemindMe(model: ShopHomeNewProductLaunchCampaignUiModel) {
        val isRemindMe = model.data?.firstOrNull()?.isRemindMe ?: false
        val action = if (isRemindMe) {
            UNREGISTER_VALUE
        } else {
            REGISTER_VALUE
        }
        val campaignId = model.data?.firstOrNull()?.campaignId ?: ""
        viewModel?.clickRemindMe(campaignId, action)
    }

    private fun handleFlashSaleClickReminder(model: ShopHomeFlashSaleUiModel) {
        val isRemindMe = model.data?.firstOrNull()?.isRemindMe ?: false
        val action = if (isRemindMe) {
            UNREGISTER_VALUE
        } else {
            REGISTER_VALUE
        }
        val campaignId = model.data?.firstOrNull()?.campaignId ?: ""
        viewModel?.clickFlashSaleReminder(campaignId, action)
    }

    protected fun handleBannerTimerClickRemindMe(model: ShopWidgetDisplayBannerTimerUiModel) {
        val isRemindMe = model.data?.isRemindMe.orFalse()
        val action = if (isRemindMe) {
            UNREGISTER_VALUE
        } else {
            REGISTER_VALUE
        }
        val campaignId = model.getCampaignId()
        viewModel?.clickBannerTimerReminder(campaignId, action)
    }

    override fun onClickCtaCampaignNplWidget(model: ShopHomeNewProductLaunchCampaignUiModel) {
        model.data?.firstOrNull()?.let {
            if (!isOwner) {
                shopPageHomeTracking.clickCtaSeeAllCampaignWidget(
                    ShopPageHomeTrackingMapper.mapToShopHomeCampaignWidgetClickCtaSeeAllTrackerModel(
                        shopId,
                        userId,
                        it.campaignId,
                        it.name,
                        it.statusCampaign,
                        model.widgetMasterId,
                        model.isFestivity
                    )
                )
            }
            context?.let { context ->
                // expected ctaLink produce ApplinkConstInternalMarketplace.SHOP_PAGE_PRODUCT_LIST
                val showcaseIntent = RouteManager.getIntent(context, model.header.ctaLink).apply {
                    // set isNeedToReload data to true for sync shop info data in product result fragment
                    putExtra(ShopCommonExtraConstant.EXTRA_IS_NEED_TO_RELOAD_DATA, true)
                }
                startActivity(showcaseIntent)
            }
        }
    }

    override fun onClickCampaignBannerAreaNplWidget(
        model: ShopHomeNewProductLaunchCampaignUiModel,
        widgetPosition: Int,
        position: Int
    ) {
        model.data?.firstOrNull()?.let {
            if (!isOwner) {
                shopPageHomeTracking.clickBannerWidgetCampaign(
                    ShopPageHomeTrackingMapper.mapToShopHomeCampaignWidgetClickBannerTrackerModel(
                        shopId,
                        userId,
                        it.campaignId,
                        it.name,
                        it.statusCampaign,
                        model.widgetMasterId,
                        model.isFestivity,
                        position
                    )
                )
            }
            context?.let { context ->
                val appLink = model.header.ctaLink
                if (appLink.isNotEmpty()) {
                    RouteManager.route(context, appLink)
                }
            }
        }
    }

    override fun onImpressionCampaignNplWidget(
        position: Int,
        shopHomeNewProductLaunchCampaignUiModel: ShopHomeNewProductLaunchCampaignUiModel
    ) {
        shopHomeNewProductLaunchCampaignUiModel.data?.firstOrNull()?.let {
            val statusCampaign = it.statusCampaign
            val campaignId = it.campaignId
            val campaignName = it.name
            sendShopHomeWidgetImpressionTracker(
                ShopPageTrackingConstant.VALUE_SHOP_DECOR_CAMPAIGN,
                shopHomeNewProductLaunchCampaignUiModel.name,
                shopHomeNewProductLaunchCampaignUiModel.widgetId,
                ShopUtil.getActualPositionFromIndex(position),
                shopHomeNewProductLaunchCampaignUiModel.widgetMasterId,
                shopHomeNewProductLaunchCampaignUiModel.isFestivity
            )
            if (!isOwner) {
                shopPageHomeTracking.impressionShopHomeCampaignWidget(
                    ShopPageHomeTrackingMapper.mapToShopHomeCampaignWidgetImpressionTrackerModel(
                        shopId,
                        userId,
                        campaignId,
                        campaignName,
                        statusCampaign,
                        position,
                        shopHomeNewProductLaunchCampaignUiModel.widgetMasterId,
                        shopHomeNewProductLaunchCampaignUiModel.isFestivity
                    )
                )
            }
        }
    }

    override fun onFlashSaleWidgetImpressed(model: ShopHomeFlashSaleUiModel, position: Int) {
        model.data?.firstOrNull()?.let { itemFlashSale ->
            val campaignId = itemFlashSale.campaignId
            val campaignName = itemFlashSale.name
            val statusCampaign = itemFlashSale.statusCampaign
            if (!isOwner) {
                shopPageHomeTracking.impressionShopHomeCampaignWidget(
                    ShopPageHomeTrackingMapper.mapToShopHomeCampaignWidgetImpressionTrackerModel(
                        shopId,
                        userId,
                        campaignId,
                        campaignName,
                        statusCampaign,
                        position,
                        model.widgetMasterId,
                        model.isFestivity
                    )
                )
            }
        }
    }

    // npl widget
    override fun onTimerFinished(model: ShopHomeNewProductLaunchCampaignUiModel) {
        shopHomeAdapter?.removeWidget(model)
        endlessRecyclerViewScrollListener.resetState()
        getLatestShopHomeWidgetLayoutData()
    }

    // flash sale widget
    override fun onTimerFinished(model: ShopHomeFlashSaleUiModel) {
        shopHomeAdapter?.removeWidget(model)
        endlessRecyclerViewScrollListener.resetState()
        getLatestShopHomeWidgetLayoutData()
    }

    private fun getLatestShopHomeWidgetLayoutData() {
        globalErrorShopPage?.hide()
        shopHomeAdapter?.removeProductList()
        shopHomeAdapter?.showLoading()
        scrollToTop()
        viewModel?.getLatestShopHomeWidgetLayoutData(
            shopId,
            extParam,
            ShopUtil.getShopPageWidgetUserAddressLocalData(context) ?: LocalCacheModel(),
            getSelectedTabName()
        )
    }

    private fun setNplRemindMeClickedCampaignId(campaignId: String) {
        PersistentCacheManager.instance.put(NPL_REMIND_ME_CAMPAIGN_ID, campaignId)
    }

    private fun getNplRemindMeClickedCampaignId(): String {
        return PersistentCacheManager.instance.get(
            NPL_REMIND_ME_CAMPAIGN_ID,
            String::class.java,
            ""
        ).orEmpty()
    }

    private fun setFlashSaleRemindMeClickedCampaignId(campaignId: String) {
        PersistentCacheManager.instance.put(FLASH_SALE_REMIND_ME_CAMPAIGN_ID, campaignId)
    }

    private fun getFlashSaleRemindMeClickedCampaignId(): String {
        return PersistentCacheManager.instance.get(
            FLASH_SALE_REMIND_ME_CAMPAIGN_ID,
            String::class.java,
            ""
        ).orEmpty()
    }

    private fun changeProductListGridView(gridType: ShopProductViewGridType) {
        shopHomeAdapter?.updateShopPageProductChangeGridSectionIcon(gridType)
        shopHomeAdapter?.changeProductCardGridType(gridType)
    }

    override fun onChangeProductGridClicked(
        initialGridType: ShopProductViewGridType,
        finalGridType: ShopProductViewGridType
    ) {
        if (!isOwner) {
            shopPageHomeTracking.clickProductListToggle(
                initialGridType,
                finalGridType,
                shopId,
                userId
            )
        }
        changeProductListGridView(finalGridType)
        scrollToEtalaseTitlePosition()
        shopChangeProductGridSharedViewModel?.changeSharedProductGridType(finalGridType)
    }

    override fun onApplySortFilter(applySortFilterModel: SortFilterBottomSheet.ApplySortFilterModel) {
        val isResetButtonVisible = sortFilterBottomSheet?.bottomSheetAction?.isVisible
        sortFilterBottomSheet = null
        shopProductFilterParameter?.clearParameter()
        shopProductFilterParameter?.setMapData(applySortFilterModel.mapParameter)
        if (isResetButtonVisible == false) {
            sortId = ""
        }
        changeShopProductFilterParameterSharedData()
        changeSortData(shopProductFilterParameter?.getSortId().orEmpty())
        scrollToEtalaseTitlePosition()
        applySortFilterTracking(sortName, applySortFilterModel.selectedFilterMapParameter)
    }

    private fun changeSortData(sortId: String) {
        this.sortId = sortId
        shopHomeAdapter?.changeSelectedSortFilter(this.sortId, sortName)
        shopHomeAdapter?.changeSortFilterIndicatorCounter(
            getIndicatorCount(
                shopProductFilterParameter?.getMapData()
            )
        )
        initialProductListData = null
        shopHomeAdapter?.refreshSticky()
        if (!isLoadInitialData && shopHomeAdapter?.isProductGridListPlaceholderExists() == false) {
            refreshProductList()
        }
    }

    private fun changeShopProductFilterParameterSharedData() {
        shopProductFilterParameterSharedViewModel?.changeSharedSortData(
            shopProductFilterParameter ?: ShopProductFilterParameter()
        )
    }

    override fun getResultCount(mapParameter: Map<String, String>) {
        val tempShopProductFilterParameter = ShopProductFilterParameter()
        tempShopProductFilterParameter.setMapData(mapParameter)
        shopProductFilterParameterSharedViewModel?.setFulfillmentFilterActiveStatus(mapParameter)
        val isFulfillmentFilterActive = shopProductFilterParameterSharedViewModel?.isFulfillmentFilterActive.orFalse()
        if (isFulfillmentFilterActive) {
            // if fulfillment filter is active then avoid gql call to get total product
            onSuccessGetShopProductFilterCount(isFulfillmentFilterActive = isFulfillmentFilterActive)
        } else {
            viewModel?.getFilterResultCount(
                shopId,
                ShopUtil.getProductPerPage(context),
                tempShopProductFilterParameter,
                ShopUtil.getShopPageWidgetUserAddressLocalData(context) ?: LocalCacheModel()
            )
        }
    }

    private fun applySortFilterTracking(
        selectedSortName: String,
        selectedFilterMap: Map<String, String>
    ) {
        if (!isOwner) {
            shopPageHomeTracking.clickApplyFilter(
                selectedSortName,
                selectedFilterMap,
                shopId,
                userId
            )
        }
    }

    fun setInitialProductListData(productListData: ShopProduct.GetShopProduct) {
        this.initialProductListData = productListData
    }

    fun setListWidgetLayoutData(homeLayoutData: HomeLayoutData?) {
        homeLayoutData?.let {
            this.shopPageHomeLayoutUiModel = ShopPageHomeMapper.mapToShopHomeWidgetLayoutData(it)
        }
    }

    override fun scrollToTop(isUserClick: Boolean) {
        if (isUserClick) {
            isClickToScrollToTop = true
        }
        getRecyclerView(view)?.scrollToPosition(0)
    }

    override fun isShowScrollToTopButton(): Boolean {
        return latestCompletelyVisibleItemIndex > 0
    }

    private fun hideScrollToTopButton() {
        if (ShopUtil.isEnableShopPageReImagined()) {
            (getRealParentFragment() as? ShopPageHeaderFragmentV2)?.hideScrollToTopButton()
        } else {
            (getRealParentFragment() as? ShopPageHeaderFragment)?.hideScrollToTopButton()
        }
    }

    private fun showScrollToTopButton() {
        if (ShopUtil.isEnableShopPageReImagined()) {
            (getRealParentFragment() as? ShopPageHeaderFragmentV2)?.showScrollToTopButton()
        } else {
            (getRealParentFragment() as? ShopPageHeaderFragment)?.showScrollToTopButton()
        }
    }

    protected open fun getSelectedTabName(): String {
        return if (ShopUtil.isEnableShopPageReImagined()) {
            (getRealParentFragment() as? ShopPageHeaderFragmentV2)?.getSelectedTabName().orEmpty()
        } else {
            (getRealParentFragment() as? ShopPageHeaderFragment)?.getSelectedTabName().orEmpty()
        }
    }

    override fun getWidgetCarouselPositionSavedState(): SparseIntArray {
        return shopHomeWidgetCarouselPositionSavedState
    }

    override fun getFragmentTrackingQueue(): TrackingQueue? {
        return trackingQueue
    }

    private fun isShopHomeTabHasFestivity(): Boolean {
        return shopHomeAdapter?.anyFestivityOnShopHomeWidget().orFalse()
    }

    override fun getShopPageColorSchema(): ShopPageColorSchema {
        return (getRealParentFragment() as? InterfaceShopPageHeader)?.getBodyColorSchema()
            ?: ShopPageColorSchema()
    }

    override fun onProductCardComparisonImpressed(
        recommendationItem: RecommendationItem,
        comparisonListModel: ComparisonListModel,
        position: Int
    ) {
        sendTrackerImpressionPersoProductComparisonWidget(recommendationItem, position)
    }

    private fun sendTrackerImpressionPersoProductComparisonWidget(
        recommendationItem: RecommendationItem,
        position: Int
    ) {
        shopPageHomeTracking.impressionPersoProductComparisonWidget(
            ShopHomePersoProductComparisonWidgetImpressionTrackerModel(
                shopId,
                userId,
                recommendationItem.productId.toString(),
                recommendationItem.name,
                recommendationItem.price,
                position
            )
        )
    }

    override fun onProductCardComparisonClicked(
        recommendationItem: RecommendationItem,
        comparisonListModel: ComparisonListModel,
        position: Int
    ) {
        sendTrackerClickPersoProductComparisonWidget(recommendationItem, position)
        goToPDP(
            UriUtil.buildUri(
                ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                recommendationItem.productId.toString()
            )
        )
    }

    private fun sendTrackerClickPersoProductComparisonWidget(
        recommendationItem: RecommendationItem,
        position: Int
    ) {
        shopPageHomeTracking.clickPersoProductComparisonWidget(
            ShopHomePersoProductComparisonWidgetImpressionTrackerModel(
                shopId,
                userId,
                recommendationItem.productId.toString(),
                recommendationItem.name,
                recommendationItem.price,
                position
            )
        )
    }

    private fun checkShowConfetti() {
        viewModel?.checkShowConfetti(shopHomeAdapter?.getShopHomeWidgetData().orEmpty(), isShowConfetti())
    }

    private fun showConfetti() {
        if (ShopUtil.isEnableShopPageReImagined()) {
            (getRealParentFragment() as? ShopPageHeaderFragmentV2)?.setupShopPageLottieAnimation(homeTabLottieUrl)
        } else {
            (getRealParentFragment() as? ShopPageHeaderFragment)?.setupShopPageLottieAnimation(homeTabLottieUrl)
        }
    }

    private fun isShowConfetti(): Boolean {
        return if (ShopUtil.isEnableShopPageReImagined()) {
            (getRealParentFragment() as? ShopPageHeaderFragmentV2)?.isShowConfetti().orFalse()
        } else {
            (getRealParentFragment() as? ShopPageHeaderFragment)?.isShowConfetti().orFalse()
        }
    }

    private fun setConfettiAlreadyShown() {
        if (ShopUtil.isEnableShopPageReImagined()) {
            (getRealParentFragment() as? ShopPageHeaderFragmentV2)?.setConfettiAlreadyShown()
        } else {
            (getRealParentFragment() as? ShopPageHeaderFragment)?.setConfettiAlreadyShown()
        }
    }

    fun setHomeTabListBackgroundColor(listBackgroundColor: List<String>) {
        this.listHomeTabBackgroundColor = listBackgroundColor
    }

    fun setHomeTabBackgroundPatternImage(backgroundPatternImage: String) {
        this.homeTabBackgroundPatternImage = backgroundPatternImage
    }

    fun setHomeTabLottieUrl(lottieUrl: String) {
        this.homeTabLottieUrl = lottieUrl
    }

    private fun setHomeTabBackgroundGradient() {
        if (listHomeTabBackgroundColor.isNotEmpty()) {
            topView?.show()
            centerView?.show()
            bottomView?.show()
            val colors = IntArray(listHomeTabBackgroundColor.size)
            for (i in listHomeTabBackgroundColor.indices) {
                colors[i] = ShopUtil.parseColorFromHexString(listHomeTabBackgroundColor.getOrNull(i).orEmpty())
            }
            val gradient = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors)
            gradient.cornerRadius = 0f
            topView?.setBackgroundColor(ShopUtil.parseColorFromHexString(listHomeTabBackgroundColor.firstOrNull().orEmpty()))
            centerView?.background = gradient
            bottomView?.setBackgroundColor(ShopUtil.parseColorFromHexString(listHomeTabBackgroundColor.lastOrNull().orEmpty()))
        } else {
            topView?.hide()
            centerView?.hide()
            bottomView?.hide()
        }
    }

    //region play widget
    /**
     * Play Widget
     */
    override fun onWidgetShouldRefresh(view: PlayWidgetView) {
        getPlayWidgetData()
    }

    override fun onWidgetOpenAppLink(view: View, appLink: String) {
        if (GlobalConfig.isSellerApp()) {
            if (isCustomerAppInstalled()) {
                startActivity(
                    Intent(Intent.ACTION_VIEW).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        data = Uri.parse(appLink)
                    }
                )
            } else {
                widgetWatchDialogContainer.openPlayStore(requireContext())
            }
        } else {
            val intent = RouteManager.getIntent(requireContext(), appLink)
            startActivityForResult(
                intent,
                PlayWidgetConst.KEY_PLAY_WIDGET_REQUEST_CODE
            )
        }
    }

    override fun onToggleReminderClicked(
        view: PlayWidgetMediumView,
        channelId: String,
        reminderType: PlayWidgetReminderType,
        position: Int
    ) {
        viewModel?.shouldUpdatePlayWidgetToggleReminder(channelId, reminderType)
    }

    override fun onDeleteFailedTranscodingChannel(view: PlayWidgetMediumView, channelId: String) {
        viewModel?.deleteChannel(channelId)
    }

    override fun onMenuActionButtonClicked(
        view: PlayWidgetMediumView,
        item: PlayWidgetChannelUiModel,
        position: Int
    ) {
        showPlayWidgetBottomSheet(item)
    }

    private fun setupPlayWidget() {
        playWidgetCoordinator = PlayWidgetCoordinator(this, autoHandleLifecycleMethod = false).apply {
            setListener(this@ShopPageHomeFragment)
        }
    }

    private fun setupPlayWidgetAnalyticListener() {
        playWidgetCoordinator.apply {
            shopPlayWidgetAnalytic.shopId = shopId
            setAnalyticModel(
                PlayWidgetShopAnalyticModel(
                    isOwnShop = shopId == viewModel?.userSessionShopId
                )
            )
        }
    }

    private fun notifyPlayWidgetTotalView(data: Intent) {
        val channelId =
            data.getStringExtra(PlayWidgetConst.KEY_EXTRA_CHANNEL_ID)
        val totalView =
            data.getStringExtra(PlayWidgetConst.KEY_EXTRA_TOTAL_VIEW)
        viewModel?.updatePlayWidgetTotalView(channelId, totalView)
    }

    private fun notifyPlayWidgetReminder(data: Intent) {
        val channelId =
            data.getStringExtra(PlayWidgetConst.KEY_EXTRA_CHANNEL_ID)
        val isReminder =
            data.getBooleanExtra(PlayWidgetConst.KEY_EXTRA_IS_REMINDER, false)
        viewModel?.updatePlayWidgetReminder(channelId, isReminder)
    }

    protected open fun observePlayWidget() {
        viewModel?.playWidgetObservable?.observe(
            viewLifecycleOwner,
            Observer { carouselPlayWidgetUiModel ->
                shopPlayWidgetAnalytic.widgetId = carouselPlayWidgetUiModel?.widgetId.orEmpty()
                shopHomeAdapter?.updatePlayWidget(carouselPlayWidgetUiModel?.playWidgetState)

                val widget = carouselPlayWidgetUiModel?.playWidgetState

                if (widget?.model?.hasSuccessfulTranscodedChannel == true) showWidgetTranscodeSuccessToaster()

                val parent = getRealParentFragment()
                if (parent is InterfaceShopPageHeader) {
                    val recyclerView = getRecyclerView(view)

                    if (parent.isNewlyBroadcastSaved() == true) {
                        parent.clearIsNewlyBroadcastSaved()
                        recyclerView?.addOneTimeGlobalLayoutListener {
                            viewScope.launch {
                                parent.collapseAppBar()
                                val widgetPosition =
                                    shopHomeAdapter?.list.orEmpty().indexOfFirst { it is CarouselPlayWidgetUiModel }
                                val finalPosition = min(
                                    ShopUtil.getActualPositionFromIndex(widgetPosition),
                                    shopHomeAdapter?.itemCount.orZero()
                                )
                                recyclerView.stepScrollToPositionWithDelay(
                                    finalPosition,
                                    PLAY_WIDGET_NEWLY_BROADCAST_SCROLL_DELAY
                                )
                            }
                        }
                    }
                }

                carouselPlayWidgetUiModel?.actionEvent?.getContentIfNotHandled()?.let {
                    when (it) {
                        is CarouselPlayWidgetUiModel.Action.Delete -> showWidgetDeletedToaster()
                        is CarouselPlayWidgetUiModel.Action.DeleteFailed -> showWidgetDeleteFailedToaster(
                            it.channelId,
                            it.reason
                        )
                        else -> {
                        }
                    }
                }
            }
        )
    }

    private fun observePlayWidgetReminder() {
        viewModel?.playWidgetReminderObservable?.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> showToastSuccess(
                        when (it.data) {
                            PlayWidgetReminderType.Reminded -> getString(com.tokopedia.play.widget.R.string.play_widget_success_add_reminder)
                            PlayWidgetReminderType.NotReminded -> getString(com.tokopedia.play.widget.R.string.play_widget_success_remove_reminder)
                        }
                    )
                    is Fail -> showErrorToast(getString(com.tokopedia.play.widget.R.string.play_widget_error_reminder))
                }
            }
        )
    }

    private fun observePlayWidgetReminderEvent() {
        viewModel?.playWidgetReminderEvent?.observe(
            viewLifecycleOwner,
            Observer {
                redirectToLoginPage(requestCode = REQUEST_CODE_USER_LOGIN_PLAY_WIDGET_REMIND_ME)
            }
        )
    }

    private fun playWidgetOnVisibilityChanged(
        isViewResumed: Boolean = if (view == null) {
            false
        } else {
            viewLifecycleOwner.lifecycle.currentState.isAtLeast(
                Lifecycle.State.RESUMED
            )
        },
        isUserVisibleHint: Boolean = userVisibleHint,
        isParentHidden: Boolean = getRealParentFragment()?.isHidden ?: true
    ) {
        if (::playWidgetCoordinator.isInitialized) {
            val isViewVisible = isViewResumed && isUserVisibleHint && !isParentHidden

            if (isViewVisible) {
                playWidgetCoordinator.onResume()
            } else {
                playWidgetCoordinator.onPause()
            }
        }
    }

    private fun observeAddCartLiveData() {
        viewModel?.miniCartAdd?.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    updateMiniCartWidget()
                    showToastSuccess(
                        it.data.data.message.joinToString(separator = ", "),
                        getString(R.string.shop_page_atc_label_cta)
                    )
                }
                is Fail -> {
                    showErrorToast(it.throwable.message.orEmpty())
                }
            }
        })
    }

    private fun observeUpdateCartLiveData() {
        viewModel?.miniCartUpdate?.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    updateMiniCartWidget()
                }
                is Fail -> {
                    showErrorToast(it.throwable.message.orEmpty())
                }
            }
        })
    }

    private fun observeDeleteCartLiveData() {
        viewModel?.miniCartRemove?.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    updateMiniCartWidget()
                    showToastSuccess(
                        it.data.second,
                        getString(R.string.shop_page_atc_label_cta)
                    )
                }
                is Fail -> {
                    val message = it.throwable.message.orEmpty()
                    showErrorToast(message)
                }
            }
        })
    }

    private fun updateMiniCartWidget() {
        if (ShopUtil.isEnableShopPageReImagined()) {
            (getRealParentFragment() as? ShopPageHeaderFragmentV2)?.updateMiniCartWidget()
        } else {
            (getRealParentFragment() as? ShopPageHeaderFragment)?.updateMiniCartWidget()
        }
    }

    private fun showPlayWidgetBottomSheet(channelUiModel: PlayWidgetChannelUiModel) {
        shopPlayWidgetAnalytic.onImpressMoreActionChannel(channelUiModel)
        getPlayWidgetActionBottomSheet(channelUiModel).show(childFragmentManager)
    }

    private fun getPlayWidgetActionBottomSheet(channelUiModel: PlayWidgetChannelUiModel): PlayWidgetActionMenuBottomSheet {
        val playWidgetActionMenuBottomSheet = PlayWidgetActionMenuBottomSheet.getFragment(
            childFragmentManager,
            requireActivity().classLoader
        )

        playWidgetActionMenuBottomSheet.setChannel(channelUiModel)
        playWidgetActionMenuBottomSheet.setListener(object : PlayWidgetActionMenuBottomSheet.Listener {
            override fun onImpressed() {
                playPerformanceDashboardEntryPointAnalytic.onViewBottomSheetContentCard(
                    viewModel?.userSessionShopId.orEmpty()
                )
            }
            override fun onClickShare(channel: PlayWidgetChannelUiModel) {
                shopPlayWidgetAnalytic.onClickMoreActionShareLinkChannel(channelUiModel.channelId)
                copyToClipboard(channelUiModel.share.fullShareContent)
                showLinkCopiedToaster()
            }

            override fun onClickSeePerformance(channel: PlayWidgetChannelUiModel) {
                shopPlayWidgetAnalytic.onClickMoreActionPerformaChannel(channelUiModel.channelId)
                playPerformanceDashboardEntryPointAnalytic.onClickReportPageEntryPointShopPage(
                    viewModel?.userSessionShopId.orEmpty()
                )
                RouteManager.route(requireContext(), channelUiModel.performanceSummaryLink)
            }

            override fun onClickSeePerformanceVideoAnalytics(channel: PlayWidgetChannelUiModel) {
                playPerformanceDashboardEntryPointAnalytic.onClickPerformanceDashboardEntryPointNative(
                    viewModel?.userSessionShopId.orEmpty()
                )
                RouteManager.route(requireContext(), PLAY_BROADCASTER_PERFORMANCE_DASHBOARD_APP_LINK)
            }

            override fun onClickDeleteVideo(channel: PlayWidgetChannelUiModel) {
                shopPlayWidgetAnalytic.onClickMoreActionDeleteChannel(channelUiModel.channelId)
                showDeleteWidgetConfirmationDialog(channelUiModel.channelId)
            }
        })

        return playWidgetActionMenuBottomSheet
    }

    private fun deleteChannel(channelId: String) {
        viewModel?.deleteChannel(channelId)
    }

    protected fun showWidgetDeletedToaster() {
        activity?.run {
            view?.let {
                Toaster.build(
                    it,
                    getString(R.string.shop_page_play_widget_sgc_video_deleted),
                    Toaster.LENGTH_SHORT,
                    Toaster.TYPE_NORMAL
                ).setAnchorViewToShopHeaderBottomViewContainer(getShopHeaderBottomViewContainer()).show()
            }
        }
    }

    protected fun showWidgetDeleteFailedToaster(channelId: String, reason: Throwable) {
        shopPlayWidgetAnalytic.onImpressErrorDeleteChannel(
            channelId,
            reason.localizedMessage.orEmpty()
        )
        activity?.run {
            view?.let {
                Toaster.build(
                    view = it,
                    text = getString(R.string.shop_page_play_widget_sgc_video_saved_fail),
                    duration = Toaster.LENGTH_LONG,
                    type = Toaster.TYPE_ERROR,
                    actionText = getString(R.string.shop_page_play_widget_sgc_try_again),
                    clickListener = View.OnClickListener {
                        deleteChannel(channelId)
                    }
                ).setAnchorViewToShopHeaderBottomViewContainer(getShopHeaderBottomViewContainer()).show()
            }
        }
    }

    protected fun showWidgetTranscodeSuccessToaster() {
        activity?.run {
            view?.let {
                Toaster.build(
                    view = it,
                    text = getString(R.string.shop_page_play_widget_sgc_video_saved_success),
                    duration = Toaster.LENGTH_LONG,
                    type = Toaster.TYPE_NORMAL
                ).setAnchorViewToShopHeaderBottomViewContainer(getShopHeaderBottomViewContainer()).show()
            }
        }
    }

    private fun showDeleteWidgetConfirmationDialog(channelId: String) {
        shopPlayWidgetAnalytic.onImpressDialogDeleteChannel(channelId)
        widgetDeleteDialogContainer.confirmDelete(requireContext(), channelId)
    }

    private fun showLinkCopiedToaster() {
        activity?.run {
            view?.let {
                Toaster.build(
                    view = it,
                    text = getString(R.string.shop_page_play_widget_sgc_link_copied),
                    duration = Toaster.LENGTH_LONG,
                    type = Toaster.TYPE_NORMAL
                ).setAnchorViewToShopHeaderBottomViewContainer(getShopHeaderBottomViewContainer()).show()
            }
        }
    }

    private fun copyToClipboard(shareContents: String) {
        (requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
            .setPrimaryClip(ClipData.newPlainText("play-widget", shareContents))
    }

    private fun isCustomerAppInstalled() = try {
        requireActivity().packageManager.getPackageInfo(CUSTOMER_APP_PACKAGE, 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }

    override fun onPlayWidgetImpression(model: CarouselPlayWidgetUiModel, position: Int) {
        sendShopHomeWidgetImpressionTracker(
            ShopPageTrackingConstant.VALUE_SHOP_DECOR_PLAY,
            model.name,
            model.widgetId,
            ShopUtil.getActualPositionFromIndex(position),
            model.widgetMasterId,
            model.isFestivity
        )
    }
    //endregion

    override fun onCardDonationClick(model: ShopHomeCardDonationUiModel) {
        context?.let {
            shopPageHomeTracking.actionClickCardDonationWidget(isOwner, shopId)
            startActivityForResult(RouteManager.getIntent(it, model.header.ctaLink), REQUEST_CODE_DONATION_DETAIL)
        }
    }

    override fun onImpressCardDonation(model: ShopHomeCardDonationUiModel, position: Int) {
        shopPageHomeTracking.impressionCardDonationWidget(isOwner, shopId)
    }

    protected fun thematicWidgetProductClickListenerImpl(): ThematicWidgetViewHolder.ThematicWidgetListener = object : ThematicWidgetViewHolder.ThematicWidgetListener {

        override fun onThematicWidgetImpressListener(model: ThematicWidgetUiModel, position: Int) {
            shopPageHomeTracking.impressionThematicWidgetCampaign(
                campaignName = model.name,
                campaignId = model.campaignId,
                shopId = shopId,
                userId = userId,
                position = position,
                isFestivity = model.isFestivity
            )
        }

        override fun onProductCardThematicWidgetImpressListener(
            products: List<ProductCardUiModel>,
            position: Int,
            thematicWidgetUiModel: ThematicWidgetUiModel?
        ) {
            shopPageHomeTracking.impressionProductCardThematicWidgetCampaign(
                campaignName = thematicWidgetUiModel?.name.orEmpty(),
                campaignId = thematicWidgetUiModel?.campaignId.orEmpty(),
                shopId = shopId,
                userId = userId,
                products = products,
                isFestivity = thematicWidgetUiModel?.isFestivity.orFalse()
            )
        }

        override fun onProductCardThematicWidgetClickListener(
            product: ProductCardUiModel,
            thematicWidgetUiModel: ThematicWidgetUiModel?,
            position: Int
        ) {
            shopPageHomeTracking.clickProductCardThematicWidgetCampaign(
                campaignName = thematicWidgetUiModel?.name.orEmpty(),
                campaignId = thematicWidgetUiModel?.campaignId.orEmpty(),
                shopId = shopId,
                userId = userId,
                product = product,
                position = position,
                isFestivity = thematicWidgetUiModel?.isFestivity.orFalse()
            )
            goToPDP(product.productUrl.orEmpty())
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
                shopHomeAdapter?.removeWidget(this)
            }
        }
    }

    private fun getRemoteConfigEnableThematicWidgetShop(): Boolean {
        return remoteConfig?.getBoolean(RemoteConfigKey.ENABLE_THEMATIC_WIDGET_SHOP, false) ?: false
    }

    override fun onDisplayBannerTimerClicked(
        position: Int,
        uiModel: ShopWidgetDisplayBannerTimerUiModel
    ) {
        checkShouldSelectCampaignTab(uiModel.data?.appLink.orEmpty())
    }

    private fun checkShouldSelectCampaignTab(appLink: String) {
        val tabValue = Uri.parse(appLink).getQueryParameter(QUERY_PARAM_TAB).orEmpty()
        if (tabValue == ShopPageHeaderTabName.CAMPAIGN) {
            if (ShopUtil.isEnableShopPageReImagined()) {
                (getRealParentFragment() as? ShopPageHeaderFragmentV2)?.selectShopTab(ShopPageHeaderTabName.CAMPAIGN, true)
            } else {
                (getRealParentFragment() as? ShopPageHeaderFragment)?.selectShopTab(ShopPageHeaderTabName.CAMPAIGN, true)
            }
        }
    }

    override fun onClickTncDisplayBannerTimerWidget(uiModel: ShopWidgetDisplayBannerTimerUiModel) {
        showTncBottomSheet(
            uiModel.getCampaignId(),
            uiModel.data?.status?.statusCampaign.orEmpty(),
            uiModel.data?.dynamicRule?.listDynamicRoleData?.map { it.ruleID }.orEmpty()
        )
    }

    override fun onClickRemindMe(position: Int, uiModel: ShopWidgetDisplayBannerTimerUiModel) {
        viewModel?.let {
            if (it.isLogin) {
                shopHomeAdapter?.showBannerTimerRemindMeLoading()
                handleBannerTimerClickRemindMe(uiModel)
                sendClickRemindMeShopHomeBannerTimerTracker(uiModel, position)
            } else {
                redirectToLoginPage()
            }
        }
    }

    private fun sendClickRemindMeShopHomeBannerTimerTracker(
        uiModel: ShopWidgetDisplayBannerTimerUiModel,
        position: Int
    ) {
        shopPageHomeTracking.clickRemindMeBannerTimerWidget(uiModel, position, shopId, userId)
    }

    override fun onClickCtaDisplayBannerTimerWidget(
        position: Int,
        uiModel: ShopWidgetDisplayBannerTimerUiModel
    ) {
        sendClickCtaShopHomeBannerTimerTracker(uiModel, position)
        checkShouldSelectCampaignTab(uiModel.header.ctaLink)
    }

    private fun sendClickCtaShopHomeBannerTimerTracker(
        uiModel: ShopWidgetDisplayBannerTimerUiModel,
        position: Int
    ) {
        shopPageHomeTracking.clickCtaShopHomeBannerTimerWidget(uiModel, position, shopId, userId)
    }

    override fun onImpressionDisplayBannerTimerWidget(
        position: Int,
        uiModel: ShopWidgetDisplayBannerTimerUiModel
    ) {
        sendImpressionShopHomeBannerTimerTracker(uiModel, position)
    }

    private fun sendImpressionShopHomeBannerTimerTracker(
        uiModel: ShopWidgetDisplayBannerTimerUiModel,
        position: Int
    ) {
        shopPageHomeTracking.impressionDisplayBannerTimerWidget(uiModel, position, shopId, userId)
    }

    override fun onTimerFinished(uiModel: ShopWidgetDisplayBannerTimerUiModel) {
        shopHomeAdapter?.removeWidget(uiModel)
        endlessRecyclerViewScrollListener.resetState()
        if (getSelectedFragment() == this) {
            refreshShopHeader()
        }
    }

    protected fun refreshShopHeader() {
        if (ShopUtil.isEnableShopPageReImagined()) {
            (getRealParentFragment() as? ShopPageHeaderFragmentV2)?.apply {
                refreshData()
            }
        } else {
            (getRealParentFragment() as? ShopPageHeaderFragment)?.apply {
                refreshData()
            }
        }
    }

    private fun getRealParentFragment(): Fragment? {
        return if (ShopUtil.isEnableShopPageReImagined()) {
            parentFragment?.parentFragment
        } else {
            parentFragment
        }
    }

    override fun onClickProductBannerHotspot(
        uiModel: ShopWidgetDisplayBannerProductHotspotUiModel,
        bannerItemUiModel: ShopWidgetDisplayBannerProductHotspotUiModel.Data,
        imageBannerPosition: Int,
        bubblePosition: Int
    ) {
        val isSingleBannerImage = uiModel.data.size == Int.ONE
        sendClickProductBannerHotspot(
            bannerItemUiModel.bannerId,
            uiModel.header.ratio,
            bannerItemUiModel.listProductHotspot.getOrNull(bubblePosition)?.productId.orEmpty(),
            bannerItemUiModel.listProductHotspot.getOrNull(bubblePosition)?.name.orEmpty(),
            bannerItemUiModel.listProductHotspot.getOrNull(bubblePosition)?.displayedPrice.orEmpty(),
            imageBannerPosition,
            bubblePosition,
            isSingleBannerImage,
            shopId,
            userId
        )
        RouteManager.route(context, bannerItemUiModel.listProductHotspot.getOrNull(bubblePosition)?.productUrl.orEmpty())
    }

    private fun sendClickProductBannerHotspot(
        bannerId: String,
        ratio: String,
        productId: String,
        productName: String,
        productPrice: String,
        imageBannerPosition: Int,
        bubblePosition: Int,
        singleBannerImage: Boolean,
        shopId: String,
        userId: String
    ) {
        shopPageHomeTracking.clickProductBannerHotspot(
            bannerId,
            ratio,
            productId,
            productName,
            productPrice,
            imageBannerPosition,
            bubblePosition,
            singleBannerImage,
            shopId,
            userId
        )
    }

    override fun onImpressionBannerHotspotImage(
        uiModel: ShopWidgetDisplayBannerProductHotspotUiModel,
        bannerItemUiModel: ShopWidgetDisplayBannerProductHotspotUiModel.Data,
        imageBannerPosition: Int
    ) {
        val isSingleBannerImage = uiModel.data.size == Int.ONE
        sendImpressionBannerHotspot(
            bannerItemUiModel.bannerId,
            uiModel.header.ratio,
            uiModel.widgetId,
            imageBannerPosition,
            isSingleBannerImage
        )
    }

    private fun sendImpressionBannerHotspot(
        bannerId: String,
        ratio: String,
        widgetId: String,
        imageBannerPosition: Int,
        singleBannerImage: Boolean
    ) {
        shopPageHomeTracking.impressionBannerHotspot(
            bannerId,
            ratio,
            widgetId,
            imageBannerPosition,
            singleBannerImage,
            shopId,
            userId
        )
    }

    override fun onBannerProductGroupMainBannerClick(mainBanner: BannerProductGroupUiModel.Tab.ComponentList.Data) {
        try {
            RouteManager.route(activity ?: return, mainBanner.ctaLink)
        } catch (_: Exception) {
        }
    }

    override fun onBannerProductGroupProductClick(selectedProduct: ProductItemType) {
        try {
            RouteManager.route(activity ?: return, selectedProduct.appLink)
        } catch (_: Exception) {
        }
    }

    override fun onBannerProductGroupVerticalBannerClick(shopHomeProductCarouselVerticalBannerItemType: VerticalBannerItemType) {
        try {
            RouteManager.route(activity ?: return, shopHomeProductCarouselVerticalBannerItemType.appLink)
        } catch (_: Exception) {
        }
    }

    override fun onBannerProductGroupViewAllClick(ctaLink: String) {
        try {
            RouteManager.route(activity ?: return, ctaLink)
        } catch (_: Exception) {
        }
    }

    override fun onNavigationBannerViewAllShowcaseClick(
        viewAllCtaAppLink: String,
        appearance: ShopHomeShowcaseNavigationBannerWidgetAppearance,
        showcaseId: String
    ) {
        shopPageHomeTracking.sendShowcaseNavigationBannerWidgetViewAllClick(
            appearance,
            showcaseId,
            shopId,
            userId
        )
        RouteManager.route(activity ?: return, viewAllCtaAppLink)
    }

    override fun onNavigationBannerShowcaseClick(
        selectedShowcase: Showcase,
        uiModel: ShowcaseNavigationUiModel,
        tabCount: Int,
        tabName: String
    ) {
        if (tabCount > 1) {
            shopPageHomeTracking.sendShowcaseNavigationShowcaseWithinTabClick(tabName, selectedShowcase, shopId, userId)
        } else {
            shopPageHomeTracking.sendShowcaseNavigationBannerWidgetShowcaseClick(shopId, userId, uiModel, selectedShowcase)
        }

        RouteManager.route(activity ?: return, selectedShowcase.ctaLink)
    }

    override fun onNavigationBannerImpression(
        uiModel: ShowcaseNavigationUiModel,
        tabCount: Int,
        tabName: String,
        showcaseId: String
    ) {
        if (tabCount > 1) {
            shopPageHomeTracking.sendShowcaseNavigationBannerWidgetWithTabImpression(tabName, showcaseId, shopId, userId)
        } else {
            shopPageHomeTracking.sendShowcaseNavigationBannerWidgetImpression(shopId, userId, uiModel)
        }
    }

    override fun onNavigationBannerTabClick(tabName: String) {
        if (tabName.isNotEmpty()) {
            shopPageHomeTracking.sendShowcaseNavigationTabClick(tabName, shopId, userId)
        }
    }

    override val productCarouselHostFragmentManager: FragmentManager
        get() = childFragmentManager
    override val productCarouselHostLifecycle: Lifecycle
        get() = this.viewLifecycleOwner.lifecycle

    override val currentShopId: String
        get() = shopId

    override val currentFragment: Fragment
        get() = this

    // For home tab, we also need to check whether there is any festivity widget if we want to override the theme
    // need to be improve this logic in the future by combining this 2 logic in BE
    override fun isOverrideTheme(): Boolean {
        return (getRealParentFragment() as? InterfaceShopPageHeader)?.isOverrideTheme().orFalse() && !isShopHomeTabHasFestivity()
    }

    override fun getShopPageHomeFragment(): ShopPageHomeFragment {
        return this
    }

    override fun getPatternColorType(): String {
        return (getRealParentFragment() as? InterfaceShopPageHeader)?.getBodyPatternColorType().orEmpty()
    }

    private fun addToCartDirectPurchaseProductWidget(
        uiModel: ShopDirectPurchaseByEtalaseUiModel,
        productModel: ProductCardDirectPurchaseDataModel,
        selectedSwitcherIndex: Int,
        selectedEtalaseIndex: Int
    ) {
        viewModel?.addToCartDirectPurchaseProductWidget(
            productModel,
            shopId,
            uiModel,
            selectedSwitcherIndex,
            selectedEtalaseIndex
        )
    }

    private fun redirectToEtalasePage(etalaseId: String) {
        RouteManager.route(context, ApplinkConst.SHOP_ETALASE, shopId, etalaseId)
    }

    override fun onImpressionAdvanceCarouselBannerItem(
        uiModel: ShopHomeDisplayWidgetUiModel,
        bannerItemUiModel: ShopHomeDisplayWidgetUiModel.DisplayWidgetItem,
        position: Int
    ) {
        shopPageHomeTracking.impressionAdvanceCarouselBannerItem(
            bannerItemUiModel.bannerId,
            uiModel.header.ratio,
            ShopUtil.getActualPositionFromIndex(position),
            uiModel.widgetId,
            shopId,
            userId
        )
    }

    override fun onClickAdvanceCarouselBannerItem(
        uiModel: ShopHomeDisplayWidgetUiModel,
        bannerItemUiModel: ShopHomeDisplayWidgetUiModel.DisplayWidgetItem,
        position: Int
    ) {
        shopPageHomeTracking.clickAdvanceCarouselBannerItem(
            bannerItemUiModel.bannerId,
            uiModel.header.ratio,
            ShopUtil.getActualPositionFromIndex(position),
            uiModel.widgetId,
            shopId,
            userId
        )
        RouteManager.route(context, bannerItemUiModel.appLink)
    }

    override fun onTriggerLoadProductDirectPurchaseWidget(
        etalaseId: String,
        selectedSwitcherIndex: Int,
        selectedEtalaseIndex: Int
    ) {
        viewModel?.getDirectPurchaseWidgetProductData(
            shopId,
            etalaseId,
            ShopUtil.getShopPageWidgetUserAddressLocalData(context) ?: LocalCacheModel(),
            selectedSwitcherIndex,
            selectedEtalaseIndex,
            shopHomeAdapter?.getNewVisitableItems().orEmpty().toMutableList()
        )
    }

    override fun onImpressionDirectPurchaseByEtalaseWidget(
        uiModel: ShopDirectPurchaseByEtalaseUiModel,
        position: Int
    ) {
        val totalEtalaseGroup = uiModel.widgetData.titleList.size
        val etalaseGroupName = uiModel.widgetData.titleList.firstOrNull()?.title.orEmpty()
        val etalaseId = uiModel.widgetData.titleList.firstOrNull()?.etalaseList?.firstOrNull()?.etalaseId.orEmpty()
        sendImpressionDirectPurchaseByEtalaseWidget(
            totalEtalaseGroup,
            etalaseGroupName,
            etalaseId,
            position,
            uiModel.widgetId
        )
    }

    override fun onClickEtalaseGroupDirectPurchaseWidget(
        uiModel: ShopDirectPurchaseByEtalaseUiModel,
        widgetPosition: Int,
        selectedSwitcherIndex: Int
    ) {
        val etalaseGroupName = uiModel.widgetData.titleList.getOrNull(selectedSwitcherIndex)?.title.orEmpty()
        val etalaseId = uiModel.widgetData.titleList.getOrNull(selectedSwitcherIndex)?.etalaseList?.firstOrNull()?.etalaseId.orEmpty()
        val widgetId = uiModel.widgetId
        sendClickEtalaseGroupDirectPurchaseWidgetTracker(
            etalaseGroupName,
            etalaseId,
            ShopUtil.getActualPositionFromIndex(widgetPosition),
            widgetId
        )
    }

    override fun onClickDirectPurchaseWidgetSeeMore(
        uiModel: ShopDirectPurchaseByEtalaseUiModel,
        selectedSwitcherIndex: Int,
        selectedEtalaseIndex: Int
    ) {
        val etalaseId = uiModel.widgetData.titleList.getOrNull(
            selectedSwitcherIndex
        )?.etalaseList?.getOrNull(selectedEtalaseIndex)?.etalaseId.orEmpty()
        redirectToEtalasePage(etalaseId)
    }

    override fun onClickEtalaseDirectPurchaseWidget(
        uiModel: ShopDirectPurchaseByEtalaseUiModel,
        widgetPosition: Int,
        selectedSwitcherIndex: Int,
        selectedEtalaseIndex: Int
    ) {
        val etalaseId = uiModel.widgetData.titleList.getOrNull(
            selectedSwitcherIndex
        )?.etalaseList?.getOrNull(selectedEtalaseIndex)?.etalaseId.orEmpty()
        val etalaseGroupName = uiModel.widgetData.titleList.getOrNull(
            selectedSwitcherIndex
        )?.title.orEmpty()
        val widgetId = uiModel.widgetId
        val totalEtalaseGroup = uiModel.widgetData.titleList.size
        sendClickEtalaseDirectPurchaseWidgetTracker(
            totalEtalaseGroup,
            etalaseGroupName,
            etalaseId,
            widgetId,
            widgetPosition
        )
    }

    private fun sendClickEtalaseDirectPurchaseWidgetTracker(
        totalEtalaseGroup: Int,
        etalaseGroupName: String,
        etalaseId: String,
        widgetId: String,
        widgetPosition: Int
    ) {
        shopPageHomeTracking.clickEtalaseDirectPurchaseWidget(
            totalEtalaseGroup,
            etalaseGroupName,
            etalaseId,
            widgetId,
            widgetPosition,
            shopId,
            userId
        )
    }

    private fun sendClickEtalaseGroupDirectPurchaseWidgetTracker(
        etalaseGroupName: String,
        etalaseId: String,
        widgetPosition: Int,
        widgetId: String
    ) {
        shopPageHomeTracking.sendClickEtalaseGroupDirectPurchaseWidget(
            etalaseGroupName,
            etalaseId,
            widgetPosition,
            widgetId,
            shopId,
            userId
        )
    }

    private fun sendImpressionDirectPurchaseByEtalaseWidget(
        totalEtalaseGroup: Int,
        etalaseGroupName: String,
        etalaseId: String,
        position: Int,
        widgetId: String
    ) {
        shopPageHomeTracking.impressionDirectPurchaseByEtalaseWidget(
            totalEtalaseGroup,
            etalaseGroupName,
            etalaseId,
            position,
            widgetId,
            shopId,
            userId
        )
    }

    override fun onClickAtcProductDirectPurchaseWidget(
        uiModel: ShopDirectPurchaseByEtalaseUiModel,
        productModel: ProductCardDirectPurchaseDataModel,
        selectedSwitcherIndex: Int,
        selectedEtalaseIndex: Int
    ) {
        if (isOwner) {
            val sellerViewAtcErrorMessage = getString(R.string.shop_page_seller_atc_error_message)
            showErrorToast(sellerViewAtcErrorMessage)
        } else {
            if (productModel.isVariant) {
                AtcVariantHelper.goToAtcVariant(
                    context = requireContext(),
                    productId = productModel.productId,
                    pageSource = VariantPageSource.SHOP_PAGE_PAGESOURCE,
                    shopId = shopId,
                    startActivitResult = this::startActivityForResult,
                    showQuantityEditor = false
                )
            } else {
                if (isLogin) {
                    addToCartDirectPurchaseProductWidget(
                        uiModel,
                        productModel,
                        selectedSwitcherIndex,
                        selectedEtalaseIndex
                    )
                } else {
                    redirectToLoginPage()
                }
            }
        }
    }

    override fun onImpressionProductDirectPurchaseWidget(
        uiModel: ShopDirectPurchaseByEtalaseUiModel,
        productModel: ProductCardDirectPurchaseDataModel,
        selectedSwitcherIndex: Int,
        selectedEtalaseIndex: Int,
        productPosition: Int
    ) {
        val totalEtalaseGroup = uiModel.widgetData.titleList.size
        val etalaseGroupName = uiModel.widgetData.titleList.getOrNull(selectedSwitcherIndex)?.title.orEmpty()
        val etalaseId = uiModel.widgetData.titleList.getOrNull(selectedEtalaseIndex)?.etalaseList?.firstOrNull()?.etalaseId.orEmpty()
        sendImpressionProductDirectPurchaseWidgetTracker(
            totalEtalaseGroup,
            etalaseGroupName,
            etalaseId,
            productPosition,
            productModel.productId,
            productModel.name,
            productModel.isVariant,
            productModel.price
        )
    }

    private fun sendImpressionProductDirectPurchaseWidgetTracker(
        totalEtalaseGroup: Int,
        etalaseGroupName: String,
        etalaseId: String,
        productPosition: Int,
        productId: String,
        productName: String,
        isVariant: Boolean,
        price: String
    ) {
        shopPageHomeTracking.impressionProductDirectPurchaseWidget(
            totalEtalaseGroup,
            etalaseGroupName,
            etalaseId,
            productPosition,
            productId,
            productName,
            isVariant,
            price,
            shopId,
            userId
        )
    }

    override fun onClickProductDirectPurchaseWidget(
        uiModel: ShopDirectPurchaseByEtalaseUiModel,
        productModel: ProductCardDirectPurchaseDataModel,
        selectedSwitcherIndex: Int,
        selectedEtalaseIndex: Int,
        productPosition: Int
    ) {
        val totalEtalaseGroup = uiModel.widgetData.titleList.size
        val etalaseGroupName = uiModel.widgetData.titleList.getOrNull(selectedSwitcherIndex)?.title.orEmpty()
        val etalaseId = uiModel.widgetData.titleList.getOrNull(selectedEtalaseIndex)?.etalaseList?.firstOrNull()?.etalaseId.orEmpty()
        sendClickProductDirectPurchaseWidgetTracker(
            totalEtalaseGroup,
            etalaseGroupName,
            etalaseId,
            productPosition,
            productModel.productId,
            productModel.name,
            productModel.isVariant,
            productModel.price
        )
        goToPDP(
            UriUtil.buildUri(
                ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                productModel.productId
            )
        )
    }

    private fun sendClickProductDirectPurchaseWidgetTracker(
        totalEtalaseGroup: Int,
        etalaseGroupName: String,
        etalaseId: String,
        productPosition: Int,
        productId: String,
        productName: String,
        isVariant: Boolean,
        price: String
    ) {
        shopPageHomeTracking.clickProductDirectPurchaseWidget(
            totalEtalaseGroup,
            etalaseGroupName,
            etalaseId,
            productPosition,
            productId,
            productName,
            isVariant,
            price,
            shopId,
            userId
        )
    }

    private fun getShopHeaderBottomViewContainer(): View? {
        return (getRealParentFragment() as? InterfaceShopPageHeader)?.getBottomViewContainer()
    }

    override fun onProductClick(trackerModel: ShopHomeTerlarisWidgetTrackerDataModel) {
        RouteManager.route(context, ApplinkConst.PRODUCT_INFO, trackerModel.productId)
        if (!isOwner) {
            shopPageHomeTracking.clickReimaginedTerlarisWidget(
                trackerModel = trackerModel,
                shopId = shopId,
                shopName = shopName,
                userId = userId
            )
        }
    }

    override fun onProductImpression(carouselData: List<ShopHomeProductUiModel>, position: Int, widgetId: String) {
        if (!isOwner) {
            shopPageHomeTracking.impressReimaginedTerlarisWidget(
                carouselData = carouselData,
                shopId = shopId,
                shopName = shopName,
                userId = userId,
                widgetId = widgetId
            )
        }
    }
}
