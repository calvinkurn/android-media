package com.tokopedia.shop.pageheader.presentation.fragment

import android.animation.Animator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.LayerDrawable
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.lottie.LottieCompositionFactory
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.FragmentConst.FEED_SHOP_FRAGMENT
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.applink.internal.ApplinkConstInternalContent.PLAY_BROADCASTER_PERFORMANCE_DASHBOARD_APP_LINK
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.merchant.DeeplinkMapperMerchant
import com.tokopedia.applink.sellermigration.SellerMigrationFeatureName
import com.tokopedia.common_sdk_affiliate_toko.utils.AffiliateCookieHelper
import com.tokopedia.config.GlobalConfig
import com.tokopedia.content.common.analytic.entrypoint.PlayPerformanceDashboardEntryPointAnalytic
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.encodeToUtf8
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isValidGlideContext
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toDoubleOrZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.utils.AffiliateLinkType
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.minicart.common.widget.MiniCartWidgetListener
import com.tokopedia.minicart.common.widget.general.MiniCartGeneralWidget
import com.tokopedia.mvcwidget.trackers.MvcSource
import com.tokopedia.mvcwidget.views.activities.TransParentActivity
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.exception.UserNotLoginException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.seller_migration_common.analytics.SellerMigrationTracking
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants
import com.tokopedia.seller_migration_common.isSellerMigrationEnabled
import com.tokopedia.seller_migration_common.presentation.activity.SellerMigrationActivity
import com.tokopedia.seller_migration_common.presentation.util.setOnClickLinkSpannable
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.analytic.ShopPageTrackingBuyer
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_PAGE_SHARE_BOTTOM_SHEET_FEATURE_NAME
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_PAGE_SHARE_BOTTOM_SHEET_PAGE_NAME
import com.tokopedia.shop.analytic.ShopPageTrackingSGCPlayWidget
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.common.constant.ShopHomeType
import com.tokopedia.shop.common.constant.ShopModerateRequestStatusCode
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.common.constant.ShopPageConstant.SHOP_PAGE_SHARED_PREFERENCE
import com.tokopedia.shop.common.constant.ShopPageConstant.ShopLayoutFeatures.DIRECT_PURCHASE
import com.tokopedia.shop.common.constant.ShopPageLoggerConstant.Tag.SHOP_PAGE_BUYER_FLOW_TAG
import com.tokopedia.shop.common.constant.ShopPageLoggerConstant.Tag.SHOP_PAGE_HEADER_BUYER_FLOW_TAG
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.PltConstant.SHOP_TRACE_ACTIVITY_PREPARE
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.PltConstant.SHOP_TRACE_HEADER_SHOP_NAME_AND_PICTURE_RENDER
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.PltConstant.SHOP_TRACE_P1_MIDDLE
import com.tokopedia.shop.common.constant.ShopShowcaseParamConstant
import com.tokopedia.shop.common.data.model.HomeLayoutData
import com.tokopedia.shop.common.data.model.ShopAffiliateData
import com.tokopedia.shop.common.data.source.cloud.model.ShopModerateRequestResult
import com.tokopedia.shop.common.data.source.cloud.model.followshop.FollowShop
import com.tokopedia.shop.common.data.source.cloud.model.followstatus.FollowStatus
import com.tokopedia.shop.common.domain.interactor.UpdateFollowStatusUseCase
import com.tokopedia.shop.common.graphql.data.shopinfo.Broadcaster
import com.tokopedia.shop.common.util.*
import com.tokopedia.shop.common.util.ShopUtil.getShopPageWidgetUserAddressLocalData
import com.tokopedia.shop.common.view.ShopPageCountDrawable
import com.tokopedia.shop.common.view.interfaces.InterfaceShopPageHeader
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.common.view.model.ShopPageFabConfig
import com.tokopedia.shop.common.view.model.ShopProductFilterParameter
import com.tokopedia.shop.common.view.viewmodel.ShopPageFeedTabSharedViewModel
import com.tokopedia.shop.common.view.viewmodel.ShopPageFeedTabSharedViewModel.Companion.FAB_ACTION_HIDE
import com.tokopedia.shop.common.view.viewmodel.ShopPageFeedTabSharedViewModel.Companion.FAB_ACTION_SETUP
import com.tokopedia.shop.common.view.viewmodel.ShopPageFeedTabSharedViewModel.Companion.FAB_ACTION_SHOW
import com.tokopedia.shop.common.view.viewmodel.ShopPageFollowingStatusSharedViewModel
import com.tokopedia.shop.common.view.viewmodel.ShopPageMiniCartSharedViewModel
import com.tokopedia.shop.common.view.viewmodel.ShopProductFilterParameterSharedViewModel
import com.tokopedia.shop.databinding.ShopHeaderFragmentBinding
import com.tokopedia.shop.databinding.WidgetSellerMigrationBottomSheetHasPostBinding
import com.tokopedia.shop.home.view.fragment.ShopPageHomeFragment
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderDataModel
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderTabModel
import com.tokopedia.shop.pageheader.di.component.DaggerShopPageHeaderComponent
import com.tokopedia.shop.pageheader.di.component.ShopPageHeaderComponent
import com.tokopedia.shop.pageheader.di.module.ShopPageHeaderModule
import com.tokopedia.shop.pageheader.presentation.ShopPageHeaderViewModel
import com.tokopedia.shop.pageheader.presentation.activity.ShopPageHeaderActivity
import com.tokopedia.shop.pageheader.presentation.adapter.ShopPageHeaderFragmentPagerAdapter
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component.ShopPageHeaderActionButtonWidgetChatButtonComponentViewHolder
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component.ShopPageHeaderActionButtonWidgetFollowButtonComponentViewHolder
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component.ShopPageHeaderActionButtonWidgetNoteButtonComponentViewHolder
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component.ShopPageHeaderPerformanceWidgetBadgeTextValueComponentViewHolder
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component.ShopPageHeaderPerformanceWidgetImageOnlyComponentViewHolder
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component.ShopPageHeaderPerformanceWidgetImageTextComponentViewHolder
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.widget.ShopPageHeaderBasicInfoWidgetViewHolder
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.widget.ShopPageHeaderPlayWidgetViewHolder
import com.tokopedia.shop.pageheader.presentation.bottomsheet.ShopPageHeaderContentCreationOptionBottomSheet
import com.tokopedia.shop.pageheader.presentation.bottomsheet.ShopPageHeaderRequestUnmoderateBottomSheet
import com.tokopedia.shop.pageheader.presentation.holder.ShopPageHeaderFragmentViewHolderListener
import com.tokopedia.shop.pageheader.presentation.listener.ShopPageHeaderPerformanceMonitoringListener
import com.tokopedia.shop.pageheader.presentation.uimodel.ShopPageHeaderLayoutUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.ShopPageHeaderP1HeaderData
import com.tokopedia.shop.pageheader.presentation.uimodel.ShopPageHeaderTickerData
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopPageHeaderComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopPageHeaderBadgeTextValueComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopPageHeaderButtonComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopPageHeaderImageOnlyComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopPageHeaderImageTextComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopPageHeaderPlayWidgetButtonComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopPageHeaderWidgetUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopPageHeaderWidgetUiModel.WidgetType.SHOP_ACTION
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopPageHeaderWidgetUiModel.WidgetType.SHOP_PLAY
import com.tokopedia.shop.pageheader.util.ShopPageHeaderTabName
import com.tokopedia.shop.product.view.fragment.ShopPageProductListFragment
import com.tokopedia.shop.search.view.activity.ShopSearchProductActivity
import com.tokopedia.shop_widget.favourite.view.activity.ShopFavouriteListActivity
import com.tokopedia.shop_widget.mvc_locked_to_product.util.MvcLockedToProductUtil
import com.tokopedia.shop_widget.note.view.bottomsheet.ShopNoteBottomSheet
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.*
import com.tokopedia.unifycomponents.R.id.bottom_sheet_wrapper
import com.tokopedia.unifycomponents.floatingbutton.FloatingButtonUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.universal_sharing.constants.ImageGeneratorConstants
import com.tokopedia.universal_sharing.model.ShopPageParamModel
import com.tokopedia.universal_sharing.tracker.PageType
import com.tokopedia.universal_sharing.view.bottomsheet.ScreenshotDetector
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.PermissionListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ScreenShotListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.AffiliateInput
import com.tokopedia.universal_sharing.view.model.LinkProperties
import com.tokopedia.universal_sharing.view.model.PageDetail
import com.tokopedia.universal_sharing.view.model.Product
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.universal_sharing.view.model.Shop
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.usercomponents.stickylogin.common.StickyLoginConstant
import com.tokopedia.usercomponents.stickylogin.view.StickyLoginAction
import com.tokopedia.usercomponents.stickylogin.view.StickyLoginView
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.webview.WebViewHelper
import java.io.File
import java.net.URLEncoder
import java.util.*
import javax.inject.Inject

//TODO need to cleanup tracker class in the future
class ShopPageHeaderFragmentV2 :
    BaseDaggerFragment(),
    HasComponent<ShopPageHeaderComponent>,
    ShopPageHeaderFragmentViewHolderListener,
    ChooseAddressWidget.ChooseAddressWidgetListener,
    InterfaceShopPageHeader,
    ShopPageHeaderBasicInfoWidgetViewHolder.Listener,
    ShopPageHeaderPerformanceWidgetBadgeTextValueComponentViewHolder.Listener,
    ShopPageHeaderPerformanceWidgetImageOnlyComponentViewHolder.Listener,
    ShopPageHeaderActionButtonWidgetChatButtonComponentViewHolder.Listener,
    ShopPageHeaderActionButtonWidgetFollowButtonComponentViewHolder.Listener,
    ShopPageHeaderActionButtonWidgetNoteButtonComponentViewHolder.Listener,
    ShopPageHeaderPlayWidgetViewHolder.Listener,
    ShopPageHeaderPerformanceWidgetImageTextComponentViewHolder.Listener,
    ShareBottomsheetListener,
    ScreenShotListener,
    PermissionListener,
    MiniCartWidgetListener {

    companion object {
        const val SHOP_ID = "EXTRA_SHOP_ID"
        const val SHOP_REF = "EXTRA_SHOP_REF"
        const val SHOP_DOMAIN = "domain"
        const val SHOP_ATTRIBUTION = "EXTRA_SHOP_ATTRIBUTION"
        private const val QUERY_PARAM_EXT_PARAM = "extParam"
        const val NEWLY_BROADCAST_CHANNEL_SAVED = "EXTRA_NEWLY_BROADCAST_SAVED"
        const val EXTRA_STATE_TAB_POSITION = "EXTRA_STATE_TAB_POSITION"
        const val TAB_POSITION_HOME = 0
        const val SHOP_STATUS_FAVOURITE = "SHOP_STATUS_FAVOURITE"
        const val SHOP_STICKY_LOGIN = "SHOP_STICKY_LOGIN"
        const val SAVED_INITIAL_FILTER = "saved_initial_filter"
        const val SAVED_IS_CONFETTI_ALREADY_SHOWN = "saved_is_confetti_already_shown"
        const val FORCE_NOT_SHOWING_HOME_TAB = "FORCE_NOT_SHOWING_HOME_TAB"
        private const val REQUEST_CODER_USER_LOGIN = 100
        private const val REQUEST_CODE_FOLLOW = 101
        private const val REQUEST_CODE_USER_LOGIN_CART = 102
        private const val VIEW_CONTENT = 1
        private const val VIEW_LOADING = 2
        private const val VIEW_ERROR = 3
        private const val VIEWPAGER_PAGE_LIMIT = 1
        private const val SOURCE_SHOP = "shop"
        private const val CART_LOCAL_CACHE_NAME = "CART"
        private const val TOTAL_CART_CACHE_KEY = "CACHE_TOTAL_CART"
        private const val PATH_HOME = "home"
        private const val PATH_PRODUCT = "product"
        private const val PATH_FEED = "feed"
        private const val PATH_REVIEW = "review"
        private const val PATH_NOTE = "note"
        private const val PATH_CAMPAIGN = "campaign"
        private const val QUERY_SHOP_REF = "shop_ref"
        private const val QUERY_SHOP_ATTRIBUTION = "tracker_attribution"
        private const val QUERY_AFFILIATE_UUID = "aff_unique_id"
        private const val QUERY_AFFILIATE_CHANNEL = "channel"
        private const val QUERY_CAMPAIGN_ID = "campaign_id"
        private const val QUERY_VARIANT_ID = "variant_id"
        private const val QUERY_TAB = "tab"
        private const val START_PAGE = 1
        private const val IS_FIRST_TIME_VISIT = "isFirstTimeVisit"
        private const val SOURCE = "shop page"

        private const val REQUEST_CODE_START_LIVE_STREAMING = 7621

        private const val MARGIN_BOTTOM_STICKY_LOGIN = 16
        private const val DEFAULT_SHOWCASE_ID = "0"
        private const val SHOP_SEARCH_PAGE_NAV_SOURCE = "shop"
        private const val FEED_SHOP_FRAGMENT_SHOP_ID = "PARAM_SHOP_ID"
        private const val FEED_SHOP_FRAGMENT_CREATE_POST_URL = "PARAM_CREATE_POST_URL"
        private const val ARGS_SHOP_ID_FOR_REVIEW_TAB = "ARGS_SHOP_ID"
        private const val DELAY_MINI_CART_RESUME = 1000L
        private const val IDR_CURRENCY_TO_RAW_STRING_REGEX = "[Rp, .]"
        private const val PRODUCT_LIST_INDEX_ZERO = 0
        private const val PRODUCT_LIST_INDEX_ONE = 1
        private const val PRODUCT_LIST_INDEX_TWO = 2
        private const val PRODUCT_LIST_INDEX_THREE = 3
        private const val PRODUCT_LIST_INDEX_FOUR = 4
        private const val PRODUCT_LIST_INDEX_FIVE = 5
        private const val PRODUCT_LIST_IMG_GENERATOR_MAX_SIZE = 6
        private const val IMG_GENERATOR_SHOP_INFO_1 = 1
        private const val IMG_GENERATOR_SHOP_INFO_2 = 2
        private const val IMG_GENERATOR_SHOP_INFO_3 = 3
        private const val IMG_GENERATOR_SHOP_INFO_MAX_SIZE = 3
        private const val PARTIAL_SHOP_HEADER_MARGIN_BOTTOM_FOLDABLE = 8
        private const val WEBVIEW_ALLOW_OVERRIDE_FORMAT = "%s?allow_override=%b&url=%s"
        private const val AFFILIATE_SITE_ID = "1"
        private const val AFFILIATE_VERTICAL_ID = "1"

        @JvmStatic
        fun createInstance() = ShopPageHeaderFragmentV2()
    }

    private var initialScrollToTopButtonMarginBottom: Int = 0

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var affiliateCookieHelper: AffiliateCookieHelper

    @Inject
    lateinit var playPerformanceDashboardEntryPointAnalytic: PlayPerformanceDashboardEntryPointAnalytic

    var shopHeaderViewModel: ShopPageHeaderViewModel? = null
    private var remoteConfig: RemoteConfig? = null
    private var cartLocalCacheHandler: LocalCacheHandler? = null
    var shopPageTracking: ShopPageTrackingBuyer? = null
    var shopPageTrackingSGCPlay: ShopPageTrackingSGCPlayWidget? = null
    private var shopId = ""
    private val shopName: String
        get() = shopPageHeaderDataModel?.shopName.orEmpty()
    var shopRef: String = ""
    var shopDomain: String? = null
    var shopAttribution: String? = null
    var campaignId: String = ""
    var variantId: String = ""
    private var affiliateData: ShopAffiliateData? = null
    var isFirstCreateShop: Boolean = false
    var isShowFeed: Boolean = false
    var createPostUrl: String = ""
    private var isTabClickByUser = false
    private var isFollowing: Boolean = false
    private var tabPosition = TAB_POSITION_HOME
    private var stickyLoginView: StickyLoginView? = null
    private var viewPagerAdapterHeader: ShopPageHeaderFragmentPagerAdapter? = null
    private var errorTextView: Typography? = null
    private var subErrorTextView: Typography? = null
    private var errorButton: View? = null
    private var shopPageFab: FloatingButtonUnify? = null
    private var isForceNotShowingTab: Boolean = false
    private var isAffiliateShareIcon = false
    private var scrollToTopButton: FloatingButtonUnify? = null
    private val intentData: Intent = Intent()
    private var isRefresh: Boolean = false
    private var extParam: String = ""
    private var shouldOverrideTabToHome: Boolean = false
    private var shouldOverrideTabToProduct: Boolean = false
    private var shouldOverrideTabToFeed: Boolean = false
    private var shouldOverrideTabToReview: Boolean = false
    private var shouldOpenShopNoteBottomSheet: Boolean = false
    private var shouldAutoRedirectToCreateEtalase: Boolean = false
    private var listShopPageTabModel = listOf<ShopPageHeaderTabModel>()
    private val customDimensionShopPage: CustomDimensionShopPage by lazy {
        CustomDimensionShopPage.create(
            shopId,
            shopPageHeaderDataModel?.isOfficial ?: false,
            shopPageHeaderDataModel?.isGoldMerchant ?: false
        )
    }
    private var shopPageHeaderDataModel: ShopPageHeaderDataModel? = null
    private var shopPagePageHeaderWidgetList: List<ShopPageHeaderWidgetUiModel> = listOf()
    private var initialProductFilterParameter: ShopProductFilterParameter? = ShopProductFilterParameter()
    private var universalShareBottomSheet: UniversalShareBottomSheet? = null
    private var screenShotDetector: ScreenshotDetector? = null
    private var shopPageHeaderUnmoderateBottomSheet: ShopPageHeaderRequestUnmoderateBottomSheet? = null
    private var shopImageFilePath: String = ""
    private var shopProductFilterParameterSharedViewModel: ShopProductFilterParameterSharedViewModel? = null
    private var shopPageMiniCartSharedViewModel: ShopPageMiniCartSharedViewModel? = null
    private var shopPageFollowingStatusSharedViewModel: ShopPageFollowingStatusSharedViewModel? = null
    private var shopPageFeedTabSharedViewModel: ShopPageFeedTabSharedViewModel? = null
    private var sharedPreferences: SharedPreferences? = null
    private var isGeneralShareBottomSheet = false
    var selectedPosition = -1
    val isMyShop: Boolean
        get() = shopHeaderViewModel?.isMyShop(shopId) == true
    var localCacheModel: LocalCacheModel? = null
    val userId: String
        get() = shopHeaderViewModel?.userId.orEmpty()
    private var mainLayout: View? = null
    private var loadingState: View? = null
    private var shopPageErrorState: View? = null
    private var viewPager: ViewPager2? = null
    private var tabLayout: TabLayout? = null
    private var bottomViewContainer: LinearLayout? = null
    private var miniCart: MiniCartGeneralWidget? = null
    private var viewBinding by autoClearedNullable<ShopHeaderFragmentBinding>()
    private var viewBindingSellerMigrationBottomSheet by autoClearedNullable<WidgetSellerMigrationBottomSheetHasPostBinding>()
    private val isLogin: Boolean
        get() = shopHeaderViewModel?.isUserSessionActive ?: false

    private var isConfettiAlreadyShown = false
    private var mBroadcasterConfig = Broadcaster.Config()
    private var queryParamTab: String = ""
    private var shopPageHeaderP1Data: ShopPageHeaderP1HeaderData? = null
    private var isAlreadyGetShopPageP2Data: Boolean = false

    override fun getComponent() = activity?.run {
        DaggerShopPageHeaderComponent.builder().shopPageHeaderModule(ShopPageHeaderModule())
            .shopComponent(ShopComponentHelper().getComponent(application, this)).build()
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        component?.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setDataFromAppLinkQueryParam()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = ShopHeaderFragmentBinding.inflate(LayoutInflater.from(context))
        configStatusBarAndSetFullScreen()
        return viewBinding?.root
    }

    private fun configStatusBarAndSetFullScreen() {
        activity?.window?.decorView?.systemUiVisibility =View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        activity?.window?.statusBarColor = Color.TRANSPARENT
    }

    override fun onStop() {
        SharingUtil.clearState(screenShotDetector)
        super.onStop()
    }

    override fun onDestroy() {
        shopHeaderViewModel?.shopPageP1Data?.removeObservers(this)
        shopHeaderViewModel?.shopImagePath?.removeObservers(this)
        shopHeaderViewModel?.shopUnmoderateData?.removeObservers(this)
        shopHeaderViewModel?.shopModerateRequestStatus?.removeObservers(this)
        shopHeaderViewModel?.shopShareTracker?.removeObservers(this)
        shopHeaderViewModel?.followStatusData?.removeObservers(this)
        shopHeaderViewModel?.followShopData?.removeObservers(this)
        shopHeaderViewModel?.shopSellerPLayWidgetData?.removeObservers(this)
        shopHeaderViewModel?.shopPageHeaderTickerData?.removeObservers(this)
        shopHeaderViewModel?.shopPageShopShareData?.removeObservers(this)
        shopProductFilterParameterSharedViewModel?.sharedShopProductFilterParameter?.removeObservers(this)
        shopPageFollowingStatusSharedViewModel?.shopPageFollowingStatusLiveData?.removeObservers(this)
        shopPageFeedTabSharedViewModel?.sellerMigrationBottomSheet?.removeObservers(this)
        shopPageFeedTabSharedViewModel?.shopPageFab?.removeObservers(this)
        shopPageMiniCartSharedViewModel?.miniCartSimplifiedData?.removeObservers(this)
        shopHeaderViewModel?.flush()
        removeTemporaryShopImage(shopImageFilePath)
        SharingUtil.clearState(screenShotDetector)
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(SAVED_INITIAL_FILTER, initialProductFilterParameter)
        outState.putBoolean(SAVED_IS_CONFETTI_ALREADY_SHOWN, isConfettiAlreadyShown)
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        when (childFragment) {
            is ShopPageHeaderContentCreationOptionBottomSheet -> {
                childFragment.setData(mBroadcasterConfig)
                childFragment.setListener(object : ShopPageHeaderContentCreationOptionBottomSheet.Listener {
                    override fun onShortsCreationClicked() {
                        goToShortsCreation()
                    }

                    override fun onBroadcastCreationClicked() {
                        goToBroadcaster()
                    }

                    override fun onPerformanceDashboardEntryClicked() {
                        playPerformanceDashboardEntryPointAnalytic.onClickPerformanceDashboardEntryPointShopPage(
                            shopHeaderViewModel?.userShopId.orEmpty()
                        )
                        goToPerformanceDashboard()
                    }
                })
            }
        }
    }

    private fun initViews(view: View) {
        mainLayout = viewBinding?.root
        loadingState = viewBinding?.loadingState?.root
        shopPageErrorState = viewBinding?.errorState
        viewPager = viewBinding?.viewPager
        tabLayout = viewBinding?.tabLayout
        bottomViewContainer = viewBinding?.bottomViewContainer
        shopPageFab = viewBinding?.fabShopPage
        scrollToTopButton = viewBinding?.buttonScrollToTop
        miniCart = viewBinding?.miniCart
        //    we can't use viewbinding for the code below, since the layout from abstraction hasn't implement viewbinding
        errorTextView = shopPageErrorState?.findViewById(com.tokopedia.abstraction.R.id.message_retry)
        subErrorTextView = shopPageErrorState?.findViewById(com.tokopedia.abstraction.R.id.sub_message_retry)
        errorButton = shopPageErrorState?.findViewById(com.tokopedia.abstraction.R.id.button_retry)
        setupBottomSheetSellerMigration(view)
        initAdapter()
        initViewPager()
        mainLayout?.requestFocus()
        getScrollToTopButtonInitialMargin()
        if (shopHeaderViewModel?.isUserSessionActive == false) initStickyLogin()
        scrollToTopButton?.apply {
            circleMainMenu.setOnClickListener {
                if (!isMyShop) {
                    shopPageTracking?.clickScrollToTop(shopId, userId)
                }
                val selectedFragment = viewPagerAdapterHeader?.getRegisteredFragment(viewPager?.currentItem.orZero())
                (selectedFragment as? ShopPageHeaderFragmentTabContentWrapper)?.let {
                    it.scrollToTop()
                }
            }
        }
        hideShopPageFab()
    }

    private fun initViewPager() {
        @SuppressLint("WrongConstant") // Suggested constant not same with actual needed value for offscreenPageLimit
        viewPager?.offscreenPageLimit = VIEWPAGER_PAGE_LIMIT
        viewPager?.isUserInputEnabled = false
        viewPager?.adapter = viewPagerAdapterHeader
    }

    private fun setupBottomSheetSellerMigration(view: View) {
        //    we can't use viewbinding for the code below, since the layout from BottomSheetUnify hasn't implement viewbinding
        try {
            val viewTarget: LinearLayout = view.findViewById(bottom_sheet_wrapper)
            hideBottomSheetSellerMigration()

            if (isSellerMigrationEnabled(context)) {
                BottomSheetUnify.bottomSheetBehaviorKnob(viewTarget, false)
                BottomSheetUnify.bottomSheetBehaviorHeader(viewTarget, false)
                viewBindingSellerMigrationBottomSheet =
                    WidgetSellerMigrationBottomSheetHasPostBinding.inflate(
                        LayoutInflater.from(context)
                    )
                viewTarget.addView(viewBindingSellerMigrationBottomSheet?.root)

                val ivTabFeedHasPost: ImageUnify? =
                    viewBindingSellerMigrationBottomSheet?.ivTabFeedHasPost
                val tvTitleTabFeedHasPost: Typography? =
                    viewBindingSellerMigrationBottomSheet?.tvTitleTabFeedHasPost
                tvTitleTabFeedHasPost?.movementMethod = LinkMovementMethod.getInstance()
                try {
                    if (ivTabFeedHasPost?.context.isValidGlideContext()) {
                        ivTabFeedHasPost?.setImageUrl(SellerMigrationConstants.SELLER_MIGRATION_SHOP_PAGE_TAB_FEED_LINK)
                    }
                } catch (e: Throwable) {
                }
                tvTitleTabFeedHasPost?.setOnClickLinkSpannable(
                    getString(com.tokopedia.seller_migration_common.R.string.seller_migration_tab_feed_bottom_sheet_content),
                    ::trackContentFeedBottomSheet
                ) {
                    val shopAppLink = UriUtil.buildUri(ApplinkConst.SHOP, shopId).orEmpty()
                    val appLinkShopPageFeed =
                        UriUtil.buildUri(ApplinkConstInternalMarketplace.SHOP_PAGE_FEED, shopId)
                            .orEmpty()
                    val intent = SellerMigrationActivity.createIntent(
                        context = requireContext(),
                        featureName = SellerMigrationFeatureName.FEATURE_POST_FEED,
                        screenName = getFeedTabFragmentClassName()?.simpleName.orEmpty(),
                        appLinks = arrayListOf(
                            ApplinkConstInternalSellerapp.SELLER_HOME,
                            shopAppLink,
                            appLinkShopPageFeed
                        )
                    )
                    startActivity(intent)
                }
            }
        } catch (_: Exception) {
        }
    }

    private fun trackContentFeedBottomSheet() {
        val userSession = UserSession(context)
        SellerMigrationTracking.trackClickShopAccount(userSession.userId.orEmpty())
    }

    private fun getScrollToTopButtonInitialMargin() {
        val scrollToTopButtonLayoutParams = (scrollToTopButton?.layoutParams as ViewGroup.MarginLayoutParams)
        initialScrollToTopButtonMarginBottom = scrollToTopButtonLayoutParams.bottomMargin
    }

    private fun observeLiveData(owner: LifecycleOwner) {
        shopHeaderViewModel?.shopPageP1Data?.observe(owner) { result ->
            stopMonitoringPltCustomMetric(SHOP_TRACE_P1_MIDDLE)
            startMonitoringPltCustomMetric(SHOP_TRACE_HEADER_SHOP_NAME_AND_PICTURE_RENDER)
            when (result) {
                is Success -> {
                    onSuccessGetShopPageP1Data(result.data)
                    initMiniCart()
                    //TODO sendTrackerImpressionShopHeader()
                    sendTrackerImpressionShopBottomNav()
                }

                is Fail -> {
                    val throwable = result.throwable
                    val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
                    if (throwable is ShopAsyncErrorException) {
                        val actionName = when (throwable.asyncQueryType) {
                            ShopAsyncErrorException.AsyncQueryType.SHOP_PAGE_P1 -> {
                                ShopLogger.SHOP_EMBRACE_BREADCRUMB_ACTION_FAIL_GET_P1
                            }

                            ShopAsyncErrorException.AsyncQueryType.SHOP_HEADER_WIDGET -> {
                                ShopLogger.SHOP_EMBRACE_BREADCRUMB_ACTION_FAIL_GET_SHOP_HEADER_WIDGET
                            }

                            ShopAsyncErrorException.AsyncQueryType.SHOP_INITIAL_PRODUCT_LIST -> {
                                ShopLogger.SHOP_EMBRACE_BREADCRUMB_ACTION_FAIL_GET_INITIAL_PRODUCT_LIST
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
                            functionName = this::observeLiveData.name,
                            liveDataName = ShopPageHeaderViewModel::shopPageP1Data.name,
                            userId = userId,
                            shopId = shopId,
                            shopName = shopName,
                            errorMessage = errorMessage,
                            stackTrace = Log.getStackTraceString(throwable),
                            errType = SHOP_PAGE_HEADER_BUYER_FLOW_TAG
                        )
                    }
                    onErrorGetShopPageTabData()
                }
            }
            stopMonitoringPltCustomMetric(SHOP_TRACE_HEADER_SHOP_NAME_AND_PICTURE_RENDER)
            stopMonitoringPerformance()
        }

        shopHeaderViewModel?.followStatusData?.observe(owner) {
            setFragmentTabContentWrapperFollowButtonLoading(false)
            when (it) {
                is Success -> {
                    it.data.followStatus.apply {
                        setFragmentTabContentWrapperFollowButtonUsingFollowStatusData(this)
                        isFollowing = this?.status?.userIsFollowing == true
                    }
                }

                else -> {
                    // no-op
                }
            }
            val followStatusData = (it as? Success)?.data?.followStatus
            showFollowButtonCoachMarkOnSelectedFragmentTabContentWrapper(followStatusData)
        }

        shopHeaderViewModel?.followShopData?.observe(owner) {
            setFragmentTabContentWrapperFollowButtonLoading(false)
            when (it) {
                is Success -> {
                    it.data.followShop?.let { followShop ->
                        onSuccessUpdateFollowStatus(followShop)
                    }
                }

                is Fail -> {
                    onErrorUpdateFollowStatus(it.throwable)
                }
            }
        }

        shopHeaderViewModel?.shopIdFromDomainData?.observe(owner) { result ->
            when (result) {
                is Success -> {
                    onSuccessGetShopIdFromDomain(result.data)
                }

                is Fail -> {
                    val throwable = result.throwable
                    if (!ShopUtil.isExceptionIgnored(throwable)) {
                        ShopUtil.logShopPageP2BuyerFlowAlerting(
                            tag = SHOP_PAGE_BUYER_FLOW_TAG,
                            functionName = this::observeLiveData.name,
                            liveDataName = ShopPageHeaderViewModel::shopIdFromDomainData.name,
                            userId = userId,
                            shopId = shopId,
                            shopName = shopName,
                            errorMessage = ErrorHandler.getErrorMessage(context, throwable),
                            stackTrace = Log.getStackTraceString(throwable),
                            errType = SHOP_PAGE_HEADER_BUYER_FLOW_TAG
                        )
                    }
                    onErrorGetShopPageTabData()
                }
            }
        }

        shopHeaderViewModel?.shopImagePath?.observe(owner) {
            shopImageFilePath = it
            if (shopImageFilePath.isNotEmpty()) {
                isGeneralShareBottomSheet = true
                showUniversalShareBottomSheet()
            }
        }

        shopHeaderViewModel?.shopUnmoderateData?.observe(owner) {
            when (it) {
                is Success -> {
                    onCompleteSendRequestOpenModerate()
                    it.data.moderateShop?.let { moderateShop ->
                        if (moderateShop.success) {
                            showToasterShopUnmoderate(
                                getString(R.string.shop_page_header_request_unmoderate_success_message),
                                Toaster.TYPE_NORMAL
                            )
                        } else {
                            val errorMessage = ErrorHandler.getErrorMessage(
                                context,
                                MessageErrorException(moderateShop.message)
                            )
                            showToasterShopUnmoderate(errorMessage, Toaster.TYPE_ERROR)
                        }
                    }
                }

                is Fail -> {
                    onCompleteSendRequestOpenModerate()
                    val errorMessage = ErrorHandler.getErrorMessage(context, it.throwable)
                    showToasterShopUnmoderate(errorMessage, Toaster.TYPE_ERROR)
                }
            }
        }

        shopHeaderViewModel?.shopModerateRequestStatus?.observe(owner) {
            when (it) {
                is Success -> {
                    val moderateStatusRequestResponse = it.data.shopModerateRequestStatus
                    if (moderateStatusRequestResponse.error.message.isEmpty()) {
                        onCompleteCheckRequestModerateStatus(moderateStatusRequestResponse.result)
                    } else {
                        val errorMessage = ErrorHandler.getErrorMessage(
                            context,
                            MessageErrorException(moderateStatusRequestResponse.error.message)
                        )
                        showToasterShopUnmoderate(errorMessage, Toaster.TYPE_ERROR)
                    }
                }

                is Fail -> {
                    val errorMessage = ErrorHandler.getErrorMessage(context, it.throwable)
                    showToasterShopUnmoderate(errorMessage, Toaster.TYPE_ERROR)
                }
            }
        }

        shopHeaderViewModel?.shopPageHeaderTickerData?.observe(owner) { result ->
            if (result is Success) {
                updateFragmentTabContentWrapperTickerData(result.data)
                refreshSelectedFragmentTickerData()
            }
        }

        shopHeaderViewModel?.shopPageShopShareData?.observe(owner) { result ->
            if (result is Success) {
                shopPageHeaderDataModel?.let {
                    it.shopSnippetUrl = result.data.shopSnippetUrl
                    it.shopCoreUrl = result.data.shopCore.url
                    it.shopBranchLinkDomain = result.data.branchLinkDomain
                    it.location = result.data.location
                    it.description = result.data.shopCore.description
                    it.tagline = result.data.shopCore.tagLine
                    it.shopStatus = result.data.statusInfo.shopStatus
                }

                // check whether shop is eligible for affiliate or not
                checkAffiliate()
            }
        }

        shopHeaderViewModel?.shopSellerPLayWidgetData?.observe(owner) { result ->
            if (result is Success) {
                shopPageHeaderDataModel?.let {
                    it.broadcaster = result.data
                    shopPagePageHeaderWidgetList.firstOrNull { headerWidgetUiModel ->
                        headerWidgetUiModel.name == SHOP_PLAY
                    }?.componentPages?.filterIsInstance<ShopPageHeaderPlayWidgetButtonComponentUiModel>()?.firstOrNull {
                        it.name == BaseShopPageHeaderComponentUiModel.ComponentName.BUTTON_PLAY
                    }?.shopPageHeaderDataModel = it
                    setFragmentTabContentWrapperSgcPlayWidget()
                }
            }
        }

        shopHeaderViewModel?.resultAffiliate?.observe(viewLifecycleOwner) {
            if (it is Success) {
                updateShareIcon(it.data.eligibleCommission?.isEligible.orFalse())
            }
        }
    }

    private fun sendTrackerImpressionShopBottomNav() {
        shopPageTracking?.impressionShopBottomNav(shopId, userId)
    }

    private fun setFragmentTabContentWrapperSgcPlayWidget() {
        listShopPageTabModel.forEach {
            if (it.tabFragment is ShopPageHeaderFragmentTabContentWrapper) {
                it.tabFragment.setSgcPlayWidgetData()
            }
        }
    }

    private fun showFollowButtonCoachMarkOnSelectedFragmentTabContentWrapper(
        followStatus: FollowStatus?
    ) {
        getSelectedFragmentInstance()?.let {
            if (it is ShopPageHeaderFragmentTabContentWrapper) {
                it.showFollowButtonCoachMark(followStatus)
            }
        }
    }

    private fun setFragmentTabContentWrapperFollowButtonUsingFollowStatusData(followStatus: FollowStatus?) {
        listShopPageTabModel.forEach {
            if (it.tabFragment is ShopPageHeaderFragmentTabContentWrapper) {
                it.tabFragment.setFollowButtonUsingFollowStatusData(followStatus)
            }
        }
    }

    private fun setFragmentTabContentWrapperFollowButtonLoading(isLoading: Boolean) {
        listShopPageTabModel.forEach {
            if (it.tabFragment is ShopPageHeaderFragmentTabContentWrapper) {
                it.tabFragment.setFollowButtonLoading(isLoading)
            }
        }
    }

    private fun refreshSelectedFragmentTickerData() {
        getSelectedFragmentInstance()?.let {
            if (it is ShopPageHeaderFragmentTabContentWrapper) {
                it.setupTicker()
            }
        }
    }

    private fun updateFragmentTabContentWrapperTickerData(tickerResultData: ShopPageHeaderTickerData) {
        listShopPageTabModel.forEach {
            if(it.tabFragment is ShopPageHeaderFragmentTabContentWrapper){
                it.tabFragment.updateShopTickerData(
                    tickerResultData
                )
            }
        }
    }

    private fun refreshCartCounterData() {
        if (isLogin && !MvcLockedToProductUtil.isSellerApp()) {
            updateFragmentTabContentWrapperNavToolbarNotification()
        }
    }

    private fun updateFragmentTabContentWrapperNavToolbarNotification() {
        listShopPageTabModel.forEach {
            if (it.tabFragment is ShopPageHeaderFragmentTabContentWrapper) {
                it.tabFragment.updateNavToolbarNotification()
            }
        }
    }

    private fun hideMiniCartWidget() {
        miniCart?.hide()
    }

    private fun showMiniCartWidget() {
        miniCart?.show()
    }

    private fun initMiniCart() {
        if (shopPageHeaderDataModel?.isEnableDirectPurchase == true) {
            val shopIds = listOf(shopId)
            miniCart?.initialize(
                shopIds = shopIds,
                fragment = this,
                listener = this,
                isShopDirectPurchase = true,
                source = MiniCartSource.ShopPage,
                page = MiniCartAnalytics.Page.SHOP_PAGE
            )
        }
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

    private fun onSuccessUpdateFollowStatus(followShop: FollowShop) {
        val success = followShop.success == true
        if (success) {
            isFollowing = followShop.isFollowing == true
            setFragmentTabContentWrapperFollowButtonStatusUsingFollowShopData(followShop)
            updateFavouriteResult(isFollowing)
            showSuccessUpdateFollowToaster(followShop)
        } else {
            followShop.message?.let {
                showErrorUpdateFollowToaster(
                    it,
                    isFollowing = followShop.isFollowing == true,
                    isSuccess = followShop.success == true
                )
            }
            val errorMessage = ErrorHandler.getErrorMessage(context, MessageErrorException(followShop.message))
            ShopPageExceptionHandler.logExceptionToCrashlytics(ShopPageExceptionHandler.ERROR_WHEN_UPDATE_FOLLOW_SHOP_DATA, Throwable(errorMessage))
        }
    }

    private fun setFragmentTabContentWrapperFollowButtonStatusUsingFollowShopData(followShop: FollowShop) {
        listShopPageTabModel.forEach {
            if (it.tabFragment is ShopPageHeaderFragmentTabContentWrapper) {
                it.tabFragment.setFollowButtonStatusUsingFollowShopData(followShop)
            }
        }
    }

    private fun onErrorUpdateFollowStatus(e: Throwable) {
        context?.let {
            if (e is UserNotLoginException) {
                val intent = RouteManager.getIntent(it, ApplinkConst.LOGIN)
                startActivityForResult(intent, REQUEST_CODER_USER_LOGIN)
                return
            }
        }

        activity?.run {
            val errorMessage = ErrorHandler.getErrorMessage(context, MessageErrorException(getString(R.string.shop_follow_error_toaster)))
            showErrorUpdateFollowToaster(errorMessage, !isFollowing, false)
            ShopPageExceptionHandler.logExceptionToCrashlytics(ShopPageExceptionHandler.ERROR_WHEN_UPDATE_FOLLOW_SHOP_DATA, e)
        }
    }

    private fun showSuccessUpdateFollowToaster(followShop: FollowShop) {
        followShop.toaster?.apply {
            if (!toasterText.isNullOrBlank()) {
                view?.let {
                    Toaster.build(
                        it,
                        toasterText ?: "",
                        Toaster.LENGTH_LONG,
                        Toaster.TYPE_NORMAL,
                        buttonLabel ?: ""
                    ) {
                        if (shopId.isNotBlank()) {
                            showMerchantVoucherCouponBottomSheet(shopId.toIntOrZero())
                            shopPageTracking?.clickCekToasterSuccess(
                                shopId,
                                shopHeaderViewModel?.userId
                            )
                        }
                    }.show()
                }
                trackViewToasterFollowUnfollow(
                    followShop.isFollowing == true,
                    followShop.success == true
                )
            }
        }
    }

    private fun showErrorUpdateFollowToaster(message: String, isFollowing: Boolean, isSuccess: Boolean) {
        view?.let {
            Toaster.build(
                it,
                message,
                Toaster.LENGTH_LONG,
                Toaster.TYPE_ERROR,
                getString(R.string.shop_follow_error_toaster_action_text)
            ) {
                toggleFollowUnfollowButton()
            }.show()
            trackViewToasterFollowUnfollow(
                isFollowing,
                isSuccess
            )
        }
    }

    private fun showMerchantVoucherCouponBottomSheet(shopId: Int) {
        context?.startActivity(
            TransParentActivity.getIntent(
                context = requireContext(),
                shopId = shopId.toString(),
                source = MvcSource.SHOP
            )
        )
    }

    private fun trackViewToasterFollowUnfollow(isFollowing: Boolean, isSuccess: Boolean) {
        if (isFollowing) {
            shopPageTracking?.impressionToasterFollow(
                isSuccess,
                shopId,
                shopHeaderViewModel?.userId
            )
        } else {
            shopPageTracking?.impressionToasterUnfollow(
                isSuccess,
                shopId,
                shopHeaderViewModel?.userId
            )
        }
    }

    private fun onSuccessGetShopIdFromDomain(shopId: String) {
        this.shopId = shopId
        getShopPageP1Data()
    }

    private fun getShopPageP2Data() {
        if(!isAlreadyGetShopPageP2Data) {
            getShopShareAndOperationalHourStatusData()
            getFollowStatus()
            getSellerPlayWidget()
            isAlreadyGetShopPageP2Data = true
        }
    }

    private fun getShopShareAndOperationalHourStatusData() {
        shopHeaderViewModel?.getShopShareAndOperationalHourStatusData(shopId, shopDomain ?: "", isRefresh)
    }

    private fun getSellerPlayWidget() {
        if (checkIfSellerPlayWidgetExistsOnHeader()) {
            shopHeaderViewModel?.getSellerPlayWidgetData(shopId)
        }
    }

    private fun checkIfSellerPlayWidgetExistsOnHeader(): Boolean {
        return shopPagePageHeaderWidgetList.find {
            it.name == SHOP_PLAY
        } != null
    }

    private fun getFollowStatus() {
        val shopFollowButtonVariantType = ShopUtil.getShopFollowButtonAbTestVariant().orEmpty()
        if (checkIfActionButtonSectionWidgetExistsOnHeader()) {
            setFragmentTabContentWrapperFollowButtonLoading(true)
            shopHeaderViewModel?.getFollowStatusData(shopId, shopFollowButtonVariantType)
        }
    }

    private fun checkIfActionButtonSectionWidgetExistsOnHeader(): Boolean {
        return shopPagePageHeaderWidgetList.find {
            it.name == SHOP_ACTION
        } != null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stopMonitoringPltPreparePage()
        stopMonitoringPltCustomMetric(SHOP_TRACE_ACTIVITY_PREPARE)
        sharedPreferences = activity?.getSharedPreferences(SHOP_PAGE_SHARED_PREFERENCE, Context.MODE_PRIVATE)
        shopHeaderViewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopPageHeaderViewModel::class.java)
        shopProductFilterParameterSharedViewModel = ViewModelProviders.of(requireActivity()).get(ShopProductFilterParameterSharedViewModel::class.java)
        shopPageMiniCartSharedViewModel = ViewModelProviders.of(requireActivity()).get(
            ShopPageMiniCartSharedViewModel::class.java
        )
        shopPageFollowingStatusSharedViewModel = ViewModelProviders.of(requireActivity()).get(ShopPageFollowingStatusSharedViewModel::class.java)
        shopPageFeedTabSharedViewModel = ViewModelProviders.of(requireActivity()).get(ShopPageFeedTabSharedViewModel::class.java)
        context?.let {
            remoteConfig = FirebaseRemoteConfigImpl(it)
            cartLocalCacheHandler = LocalCacheHandler(it, CART_LOCAL_CACHE_NAME)
            shopPageTracking = ShopPageTrackingBuyer(TrackingQueue(it))
            shopPageTrackingSGCPlay = ShopPageTrackingSGCPlayWidget(TrackingQueue(it))
            activity?.intent?.run {
                shopId = getStringExtra(SHOP_ID).orEmpty()
                shopRef = getStringExtra(SHOP_REF).orEmpty()
                shopDomain = getStringExtra(SHOP_DOMAIN)
                shopAttribution = getStringExtra(SHOP_ATTRIBUTION)
                tabPosition = getIntExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_HOME)
                isFirstCreateShop = getBooleanExtra(ApplinkConstInternalMarketplace.PARAM_FIRST_CREATE_SHOP, false)
                isForceNotShowingTab = getBooleanExtra(FORCE_NOT_SHOWING_HOME_TAB, false)
                data?.run {
                    if (shopId.isEmpty()) {
                        if (pathSegments.size > 1) {
                            shopId = pathSegments[1]
                        } else if (!getQueryParameter(SHOP_ID).isNullOrEmpty()) {
                            shopId = getQueryParameter(SHOP_ID)!!
                        }
                    }
                    if (shopDomain.isNullOrEmpty()) {
                        shopDomain = getQueryParameter(SHOP_DOMAIN)
                    }
                    if (lastPathSegment.orEmpty() == PATH_HOME) {
                        shouldOverrideTabToHome = true
                    }
                    if (lastPathSegment.orEmpty() == PATH_PRODUCT) {
                        shouldOverrideTabToProduct = true

                        // check applink query param if have to auto redirect to create showcase page
                        val isRedirectToCreateShowcase = getQueryParameter(DeeplinkMapperMerchant.PARAM_CREATE_SHOWCASE).orEmpty()
                        if (GlobalConfig.isSellerApp() && (isRedirectToCreateShowcase.isNotEmpty() && isRedirectToCreateShowcase.toBoolean())) {
                            shouldAutoRedirectToCreateEtalase = true
                        }
                    }
                    if (lastPathSegment.orEmpty() == PATH_FEED) {
                        shouldOverrideTabToFeed = true
                    }
                    if (lastPathSegment.orEmpty() == PATH_REVIEW) {
                        // override to review tab shop page
                        shouldOverrideTabToReview = true
                    }
                    if (lastPathSegment.orEmpty() == PATH_NOTE) {
                        shouldOpenShopNoteBottomSheet = true
                    }
                    queryParamTab = getQueryParameter(QUERY_TAB).orEmpty()
                    shopRef = getQueryParameter(QUERY_SHOP_REF) ?: ""
                    shopAttribution = getQueryParameter(QUERY_SHOP_ATTRIBUTION) ?: ""
                    checkAffiliateAppLink(this)
                    getMarketingServiceQueryParamData(this)
                }
                handlePlayBroadcastExtra(this@run)
            }
            if (GlobalConfig.isSellerApp()) {
                shopId = shopHeaderViewModel?.userShopId.orEmpty()
                if (shouldAutoRedirectToCreateEtalase) {
                    goToCreateEtalase()
                }
            }
            getSavedInstanceStateData(savedInstanceState)
            observeLiveData(this)
            observeShopProductFilterParameterSharedViewModel()
            observeShopPageFollowingStatusSharedViewModel()
            observeShopPageFeedTabSharedViewModel()
            observeShopPageMiniCartSharedViewModel()
            getInitialData()
            initViews(view)
            setViewState(VIEW_LOADING)
        }
        context?.let {
            screenShotDetector = SharingUtil.createAndStartScreenShotDetector(
                it,
                this,
                this,
                permissionListener = this
            )
        }
        shopLandingPageInitAffiliateCookie()
    }

    private fun checkAffiliateAppLink(uri: Uri) {
        val isAppLinkContainAffiliateUuid = uri.queryParameterNames.contains(QUERY_AFFILIATE_UUID)
        if (isAppLinkContainAffiliateUuid) {
            setAffiliateData(uri)
        }
    }

    private fun getMarketingServiceQueryParamData(data: Uri) {
        campaignId = data.getQueryParameter(QUERY_CAMPAIGN_ID).orEmpty()
        variantId = data.getQueryParameter(QUERY_VARIANT_ID).orEmpty()
    }

    private fun setAffiliateData(uri: Uri) {
        val affiliateTrackerId = UUID.randomUUID().toString()
        val affiliateChannel = uri.getQueryParameter(QUERY_AFFILIATE_CHANNEL).orEmpty()
        shopHeaderViewModel?.saveAffiliateChannel(affiliateChannel)
        affiliateData = ShopAffiliateData(
            uri.getQueryParameter(QUERY_AFFILIATE_UUID).orEmpty(),
            affiliateChannel,
            affiliateTrackerId
        )
    }

    private fun shopLandingPageInitAffiliateCookie() {
        shopHeaderViewModel?.shopLandingPageInitAffiliateCookie(
            affiliateCookieHelper,
            affiliateData?.affiliateUUId.orEmpty(),
            affiliateData?.affiliateChannel.orEmpty(),
            shopId
        )
    }

    private fun observeShopProductFilterParameterSharedViewModel() {
        shopProductFilterParameterSharedViewModel?.sharedShopProductFilterParameter?.observe(
            viewLifecycleOwner
        ) {
            initialProductFilterParameter = it
        }
    }

    private fun observeShopPageFollowingStatusSharedViewModel() {
        shopPageFollowingStatusSharedViewModel?.shopPageFollowingStatusLiveData?.observe(
            viewLifecycleOwner
        ) {
            setFragmentTabContentWrapperFollowButtonStatusUsingFollowShopData(it)
            isFollowing = it.isFollowing == true
        }
    }

    private fun observeShopPageFeedTabSharedViewModel() {
        // observe seller migration bottomsheet
        shopPageFeedTabSharedViewModel?.sellerMigrationBottomSheet?.observe(
            viewLifecycleOwner
        ) { isShow ->
            if (isShow) {
                showBottomSheetSellerMigration()
            } else {
                hideBottomSheetSellerMigration()
            }
        }

        // observe shop page fab
        shopPageFeedTabSharedViewModel?.shopPageFab?.observe(viewLifecycleOwner) { fabAction ->
            when (fabAction) {
                FAB_ACTION_SETUP -> setupShopPageFab(
                    shopPageFeedTabSharedViewModel?.shopPageFabConfig ?: ShopPageFabConfig()
                )

                FAB_ACTION_SHOW -> showShopPageFab()
                FAB_ACTION_HIDE -> hideShopPageFab()
            }
        }
    }

    private fun observeShopPageMiniCartSharedViewModel() {
        shopPageMiniCartSharedViewModel?.miniCartSimplifiedData?.observe(viewLifecycleOwner) {
            refreshCartCounterData()
            if (it.isShowMiniCartWidget) {
                showMiniCartWidget()
            } else {
                hideMiniCartWidget()
            }
        }
    }

    private fun getSavedInstanceStateData(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            initialProductFilterParameter = it.getParcelable(SAVED_INITIAL_FILTER)
            isConfettiAlreadyShown = it.getBoolean(SAVED_IS_CONFETTI_ALREADY_SHOWN)
        }
    }

    private fun stopMonitoringPltPreparePage() {
        (activity as? ShopPageHeaderPerformanceMonitoringListener)?.let { shopPageActivity ->
            shopPageActivity.getShopPageLoadTimePerformanceCallback()?.let {
                shopPageActivity.stopMonitoringPltPreparePage(it)
            }
        }
    }

    private fun startMonitoringPltNetworkRequest() {
        (activity as? ShopPageHeaderPerformanceMonitoringListener)?.let { shopPageActivity ->
            shopPageActivity.getShopPageLoadTimePerformanceCallback()?.let {
                shopPageActivity.startMonitoringPltNetworkRequest(it)
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

    private fun stopMonitoringPerformance() {
        (activity as? ShopPageHeaderActivity)?.stopShopHeaderPerformanceMonitoring()
    }

    private fun initStickyLogin() {
        stickyLoginView = viewBinding?.stickyLoginText
        stickyLoginView?.page = StickyLoginConstant.Page.SHOP
        stickyLoginView?.lifecycleOwner = viewLifecycleOwner
        stickyLoginView?.setStickyAction(object : StickyLoginAction {
            override fun onClick() {
                startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN), REQUEST_CODER_USER_LOGIN)
            }

            override fun onDismiss() {
            }

            override fun onViewChange(isShowing: Boolean) {
                updateViewPagerPadding()
                updateScrollToTopButtonMargin()
            }
        })

        stickyLoginView?.hide()
    }

    private fun getInitialData() {
        hideMiniCartWidget()
        updateCurrentPageLocalCacheModelData()
        startMonitoringPltNetworkRequest()
        startMonitoringPltCustomMetric(SHOP_TRACE_P1_MIDDLE)
        if (shopId.isEmpty()) {
            shopHeaderViewModel?.getShopIdFromDomain(shopDomain.orEmpty())
        } else {
            getShopPageP1Data()
        }
    }

    private fun getShopPageP1Data() {
        if (shopId.toIntOrZero() == 0 && shopDomain.orEmpty().isEmpty()) return
        shopHeaderViewModel?.getNewShopPageTabData(
            shopId = shopId,
            shopDomain = shopDomain.orEmpty(),
            page = START_PAGE,
            itemPerPage = ShopUtil.getProductPerPage(context),
            shopProductFilterParameter = initialProductFilterParameter ?: ShopProductFilterParameter(),
            keyword = "",
            etalaseId = "",
            isRefresh = isRefresh,
            widgetUserAddressLocalData = localCacheModel ?: LocalCacheModel(),
            extParam = extParam,
            tabName = getSelectedTabName().takeIf { it.isNotEmpty() } ?: queryParamTab
        )
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

    private fun isEnableAffiliateShareIcon(): Boolean {
        val abTestValue = RemoteConfigInstance.getInstance().abTestPlatform.getString(
            RollenceKey.AFFILIATE_SHARE_ICON
        )
        return abTestValue == RollenceKey.AFFILIATE_SHARE_ICON
    }

    /**
     * only enable affiliate share icon when
     * @param isAffiliate == true && [isMyShop] == true
     */
    private fun updateShareIcon(isAffiliate: Boolean) {
        if (isEnableAffiliateShareIcon() && isAffiliate && !isMyShop) {
            listShopPageTabModel.forEach {
                if(it.tabFragment is ShopPageHeaderFragmentTabContentWrapper){
                    it.tabFragment.updateShareIcon(IconList.ID_SHARE_AB_TEST)
                }
            }
            isAffiliateShareIcon = true
        }
    }

    override fun getCartCounterData(): Int {
        return cartLocalCacheHandler?.getInt(TOTAL_CART_CACHE_KEY, 0).orZero()
    }

    override fun getBodyColorSchema(): ShopPageColorSchema? {
        return getShopBodyConfig()?.colorSchema
    }

    override fun isOverrideTheme(): Boolean {
        return shopPageHeaderP1Data?.shopHeaderLayoutData?.isOverrideTheme.orFalse()
    }

    override fun getBodyPatternColorType(): String {
        return getShopBodyConfig()?.patternColorType.orEmpty()
    }

    override fun getBodyBackgroundHexColor(): String {
        return getShopNavBarConfig()?.listBackgroundColor?.firstOrNull().orEmpty()
    }

    private fun redirectToSearchAutoCompletePage() {
        val shopSrpAppLink = URLEncoder.encode(
            UriUtil.buildUri(
                ApplinkConst.SHOP_ETALASE,
                shopId,
                DEFAULT_SHOWCASE_ID
            ),
            "utf-8"
        )
        val searchPageUri = Uri.parse(ApplinkConstInternalDiscovery.AUTOCOMPLETE)
            .buildUpon()
            .appendQueryParameter(SearchApiConst.Q, "")
            .appendQueryParameter(SearchApiConst.SRP_PAGE_ID, shopId)
            .appendQueryParameter(SearchApiConst.SRP_PAGE_TITLE, shopName)
            .appendQueryParameter(SearchApiConst.NAVSOURCE, SHOP_SEARCH_PAGE_NAV_SOURCE)
            .appendQueryParameter(
                SearchApiConst.PLACEHOLDER,
                String.format(
                    getString(R.string.shop_product_search_hint_2),
                    shopPageHeaderDataModel?.shopName.orEmpty()
                )
            )
            .appendQueryParameter(SearchApiConst.BASE_SRP_APPLINK, shopSrpAppLink)
            .build()
            .toString()
        RouteManager.route(context, searchPageUri)
    }

    private fun redirectToShopSearchProductPage() {
        context?.let { context ->
            shopPageHeaderDataModel?.let { shopPageHeaderDataModel ->
                startActivity(
                    ShopSearchProductActivity.createIntent(
                        context,
                        shopId,
                        shopPageHeaderDataModel.shopName,
                        shopPageHeaderDataModel.isOfficial,
                        shopPageHeaderDataModel.isGoldMerchant,
                        "",
                        shopAttribution,
                        shopRef
                    )
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setShopName()
        checkIfChooseAddressWidgetDataUpdated()
        screenShotDetector?.start()
        refreshCartCounterData()
        // Add delay update mini cart when on resume, to prevent race condition with cart page's update cart
        updateMiniCartWidget(delay = DELAY_MINI_CART_RESUME)
    }

    private fun checkIfChooseAddressWidgetDataUpdated() {
        context?.let { context ->
            localCacheModel?.let {
                val isUpdated = ChooseAddressUtils.isLocalizingAddressHasUpdated(
                    context,
                    it
                )
                if (isUpdated) {
                    refreshData()
                }
            }
        }
    }

    private fun setViewState(viewState: Int) {
        when (viewState) {
            VIEW_LOADING -> {
                loadingState?.visibility = View.VISIBLE
                shopPageErrorState?.visibility = View.GONE
                viewPager?.visibility = View.INVISIBLE
                scrollToTopButton?.gone()
                tabLayout?.invisible()
                hideShopPageFab()
            }
            VIEW_ERROR -> {
                loadingState?.visibility = View.GONE
                shopPageErrorState?.visibility = View.VISIBLE
                viewPager?.visibility = View.INVISIBLE
            }
            else -> {
                loadingState?.visibility = View.GONE
                shopPageErrorState?.visibility = View.GONE
                viewPager?.visibility = View.VISIBLE
                tabLayout?.visible()
            }
        }
    }

    private fun initAdapter() {
        activity?.run {
            viewPagerAdapterHeader = ShopPageHeaderFragmentPagerAdapter(this, this@ShopPageHeaderFragmentV2)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (isMyShop) {
            inflater.inflate(R.menu.menu_shop_page_fragment_seller, menu)
        } else {
            inflater.inflate(R.menu.menu_shop_page_fragment_buyer, menu)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        context?.let {
            val userSession = UserSession(it)
            if (userSession.isLoggedIn) {
                showCartBadge(menu)
            }
        }
    }

    private fun showCartBadge(menu: Menu?) {
        context?.let {
            val drawable = ContextCompat.getDrawable(it, R.drawable.ic_cart_menu)
            if (drawable is LayerDrawable) {
                val countDrawable = ShopPageCountDrawable(it)
                val cartCount = cartLocalCacheHandler?.getInt(TOTAL_CART_CACHE_KEY, 0).orZero()
                countDrawable.setCount(cartCount.toString())
                drawable.mutate()
                drawable.setDrawableByLayerId(R.id.ic_cart_count, countDrawable)
                menu?.findItem(R.id.menu_action_cart)?.icon = drawable
            }
        }
    }

    private fun removeTemporaryShopImage(uri: String) {
        if (uri.isNotEmpty()) {
            File(uri).apply {
                if (exists()) {
                    delete()
                }
            }
        }
    }

    internal fun clickShopShare() {
        shopPageTracking?.clickShareButtonNewBottomSheet(customDimensionShopPage, userId, isAffiliateShareIcon)
        if (!isMyShop) {
            shopPageTracking?.clickGlobalHeaderShareButton(customDimensionShopPage, userId)
        }
        removeTemporaryShopImage(shopImageFilePath)
        saveShopImage()
    }

    internal fun clickSearch() {
        shopPageTracking?.clickSearch(isMyShop, customDimensionShopPage)
        if (GlobalConfig.isSellerApp()) {
            redirectToShopSearchProductPage()
        } else {
            redirectToSearchAutoCompletePage()
        }
    }

    internal fun clickSettingButton() {
        shopPageTracking?.clickSettingButton(customDimensionShopPage)
        redirectToShopSettingsPage()
    }

    private fun redirectToShopInfoPage() {
        context?.let { context ->
            shopPageTracking?.clickShopProfile(customDimensionShopPage)
            RouteManager.route(context, ApplinkConstInternalMarketplace.SHOP_INFO, shopId)
        }
    }

    private fun redirectToShopSettingsPage() {
        context?.let { context ->
            if (GlobalConfig.isSellerApp()) {
                RouteManager.route(context, ApplinkConstInternalSellerapp.MENU_SETTING)
            } else {
                RouteManager.route(context, ApplinkConstInternalMarketplace.SHOP_PAGE_SETTING_CUSTOMER_APP_WITH_SHOP_ID, shopId)
            }
        }
    }

    private fun goToCart() {
        context?.let {
            val userSession = UserSession(it)
            if (userSession.isLoggedIn) {
                startActivity(RouteManager.getIntent(it, ApplinkConst.CART))
            } else {
                startActivityForResult(
                    RouteManager.getIntent(it, ApplinkConst.LOGIN),
                    REQUEST_CODE_USER_LOGIN_CART
                )
            }
        }
    }

    private fun goToCreateEtalase() {
        val showcaseListIntent = RouteManager.getIntent(context, ApplinkConstInternalMechant.MERCHANT_SHOP_SHOWCASE_LIST)
        val showcaseListBundle = Bundle().apply {
            putString(ShopShowcaseParamConstant.EXTRA_SHOP_ID, shopId)
            putString(ShopShowcaseParamConstant.EXTRA_SELECTED_ETALASE_ID, "")
            putBoolean(ShopShowcaseParamConstant.EXTRA_IS_SHOW_DEFAULT, true)
            putBoolean(ShopShowcaseParamConstant.EXTRA_IS_SHOW_ZERO_PRODUCT, false)
            putBoolean(ShopShowcaseParamConstant.EXTRA_IS_NEED_TO_OPEN_CREATE_SHOWCASE, true)
        }
        showcaseListIntent.putExtra(ShopShowcaseParamConstant.EXTRA_BUNDLE, showcaseListBundle)
        startActivity(showcaseListIntent)
    }

    private fun showContentCreationOptionBottomSheet() {
        ShopPageHeaderContentCreationOptionBottomSheet
            .getFragment(childFragmentManager, requireActivity().classLoader)
            .show(childFragmentManager)
    }

    private fun goToShortsCreation() {
        RouteManager.route(context, ApplinkConst.PLAY_SHORTS)
    }

    private fun goToBroadcaster() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalContent.INTERNAL_PLAY_BROADCASTER)
        startActivityForResult(intent, REQUEST_CODE_START_LIVE_STREAMING)
    }

    private fun goToPerformanceDashboard() {
        RouteManager.route(context, PLAY_BROADCASTER_PERFORMANCE_DASHBOARD_APP_LINK)
    }

    private fun onSuccessGetShopPageP1Data(shopPageHeaderP1Data: ShopPageHeaderP1HeaderData) {
        this.shopPageHeaderP1Data = shopPageHeaderP1Data
        isShowFeed = shopPageHeaderP1Data.isWhitelist
        createPostUrl = shopPageHeaderP1Data.feedUrl
        shopPageHeaderDataModel = ShopPageHeaderDataModel().apply {
            shopId = this@ShopPageHeaderFragmentV2.shopId
            isOfficial = shopPageHeaderP1Data.isOfficial
            isGoldMerchant = shopPageHeaderP1Data.isGoldMerchant
            pmTier = shopPageHeaderP1Data.pmTier
            shopHomeType = shopPageHeaderP1Data.shopHomeType.takeIf { !isForceNotShowingTab }
                ?: ShopHomeType.NONE
            shopName = MethodChecker.fromHtml(shopPageHeaderP1Data.shopName).toString()
            shopDomain = shopPageHeaderP1Data.shopDomain
            avatar = shopPageHeaderP1Data.shopAvatar
            listDynamicTabData = shopPageHeaderP1Data.listDynamicTabData
            isEnableDirectPurchase = getIsEnableDirectPurchase(shopPageHeaderP1Data)
        }
        shopPagePageHeaderWidgetList = shopPageHeaderP1Data.listShopPageHeaderWidget
        customDimensionShopPage.updateCustomDimensionData(
            shopId,
            shopPageHeaderDataModel?.isOfficial ?: false,
            shopPageHeaderDataModel?.isGoldMerchant ?: false
        )
        setViewState(VIEW_CONTENT)
        setupTabs()
        sendShopPageOpenScreenTracker()
        if (shouldOpenShopNoteBottomSheet) {
            showShopNoteBottomSheet()
        }
        view?.let { onToasterNoUploadProduct(it, getString(R.string.shop_page_product_no_upload_product), isFirstCreateShop) }
        stickyLoginView?.loadContent()
    }

    private fun getIsEnableDirectPurchase(shopPageHeaderP1Data: ShopPageHeaderP1HeaderData): Boolean {
        return shopPageHeaderP1Data.listDynamicTabData.firstOrNull {
            it.name == ShopPageHeaderTabName.HOME
        }?.shopLayoutFeature?.firstOrNull {
            it.name == DIRECT_PURCHASE && it.isActive
        } != null
    }

    private fun sendShopPageOpenScreenTracker() {
        val selectedTabName = getSelectedTabName()
        if (selectedTabName.isNotEmpty()) {
            if (!isMyShop) {
                shopPageTracking?.sendScreenShopPage(shopId, isLogin, selectedTabName, campaignId, variantId, affiliateData)
                shopPageTracking?.sendBranchScreenShop(userId)
            }
        }
    }

    fun getSelectedTabName(): String {
        return listShopPageTabModel.getOrNull(
            getSelectedDynamicTabPosition()
        )?.tabName.orEmpty()
    }

    override fun onBackPressed() {
        shopPageTracking?.clickBackArrow(isMyShop, customDimensionShopPage)
        removeTemporaryShopImage(shopImageFilePath)
    }

    override fun onPause() {
        super.onPause()
        shopPageTracking?.sendAllTrackingQueue()
    }

    private fun setupTabs() {
        listShopPageTabModel = (setupTabContentWrapper() as? List<ShopPageHeaderTabModel>) ?: listOf()
        viewPagerAdapterHeader?.setPageTheme(
            shopPageHeaderP1Data?.shopHeaderLayoutData?.isOverrideTheme.orFalse(),
            getShopNavBarConfig()?.patternColorType.orEmpty()
        )
        viewPagerAdapterHeader?.setTabData(listShopPageTabModel)
        selectedPosition = getSelectedDynamicTabPosition()
        tabLayout?.removeAllTabs()
        listShopPageTabModel.forEach { _ ->
            tabLayout?.newTab()?.apply {
                view.setOnClickListener {
                    isTabClickByUser = true
                }
            }?.let {
                tabLayout?.addTab(it, false)
            }
        }
        viewPagerAdapterHeader?.notifyDataSetChanged()
        tabLayout?.apply {
            for (i in 0 until tabCount) {
                val tab = getTabAt(i)
                tab?.customView = getTabView(i)
                setupTabPressedEffect(tab, i)
            }
        }
        viewPager?.setCurrentItem(selectedPosition, false)
        setShopLayoutDataToSelectedTab()
        tabLayout?.getTabAt(selectedPosition)?.select()
        tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {
                handleSelectedTab(tab, true)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                handleSelectedTab(tab, false)
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                val position = tab.position
                viewPager?.setCurrentItem(position, true)
                tabLayout?.getTabAt(position)?.let {
                    handleSelectedTab(tab, true)
                }
                if (isTabClickByUser) {
                    sendShopPageClickTabTracker(position)
                }
                if (isSellerMigrationEnabled(context)) {
                    getFeedTabFragmentClassName()?.let {
                        if (isMyShop && viewPagerAdapterHeader?.isFragmentObjectExists(it) == true) {
                            val tabFeedPosition = viewPagerAdapterHeader?.getFragmentPosition(it)
                            if (position == tabFeedPosition) {
                                showBottomSheetSellerMigration()
                            } else {
                                hideBottomSheetSellerMigration()
                            }
                        } else {
                            hideBottomSheetSellerMigration()
                        }
                    }
                }
                checkShouldSendCampaignTabOpenScreenTracker()
                hideScrollToTopButton()
                hideShopPageFab()
                isTabClickByUser = false
            }
        })
        setTabLayoutBackgroundColor()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupTabPressedEffect(tab: TabLayout.Tab?, position: Int) {
        tab?.view?.let { tabView ->
            tabView.setOnTouchListener{ v, event ->
                if(position != getSelectedDynamicTabPosition()) {
                    when(event.action){
                        MotionEvent.ACTION_MOVE -> {
                            (tabView.parent as ViewGroup).requestDisallowInterceptTouchEvent(true)
                            if(event.x <= Int.ZERO || event.x >= v.width || event.y <= Int.ZERO || event.y >= v.height){
                                viewPagerAdapterHeader?.handleSelectedTab(tab, false)
                            }
                        }
                        MotionEvent.ACTION_DOWN -> {
                            viewPagerAdapterHeader?.handleSelectedTab(tab, true)
                        }
                        MotionEvent.ACTION_CANCEL -> {
                            viewPagerAdapterHeader?.handleSelectedTab(tab, false)
                        }
                    }
                }
                false
            }
        }
    }

    private fun setTabLayoutBackgroundColor() {
        if(shopPageHeaderP1Data?.shopHeaderLayoutData?.isOverrideTheme == true) {
            val fragmentBackgroundColor = getShopNavBarConfig()?.listBackgroundColor?.firstOrNull().orEmpty()
            tabLayout?.background = ColorDrawable(ShopUtil.parseColorFromHexString(fragmentBackgroundColor))
        }
    }

    private fun getShopNavBarConfig(): ShopPageHeaderLayoutUiModel.Config? {
        return shopPageHeaderP1Data?.shopHeaderLayoutData?.getShopConfigListByName(ShopPageHeaderLayoutUiModel.ConfigName.SHOP_BOTTOM_NAVBAR)
    }

    private fun getShopBodyConfig(): ShopPageHeaderLayoutUiModel.Config? {
        return shopPageHeaderP1Data?.shopHeaderLayoutData?.getShopConfigListByName(ShopPageHeaderLayoutUiModel.ConfigName.SHOP_BODY)
    }

    private fun checkShouldSendCampaignTabOpenScreenTracker() {
        if (getSelectedTabName() == ShopPageHeaderTabName.CAMPAIGN) {
            sendShopCampaignTabOpenScreenTracker()
        }
    }

    private fun sendShopCampaignTabOpenScreenTracker() {
        shopPageTracking?.sendOpenScreenShopCampaignTab(shopId, userId, isLogin)
    }

    private fun setShopLayoutDataToSelectedTab() {
        val initialShopLayoutData = getShopLayoutDataBasedOnSelectedTab()
        when (val selectedFragment = getSelectedFragmentInstance()) {
            is ShopPageHeaderFragmentTabContentWrapper -> {
                when(selectedFragment.getTabData()?.name.orEmpty()){
                    ShopPageHeaderTabName.HOME, ShopPageHeaderTabName.CAMPAIGN -> {
                        selectedFragment.setInitialShopLayoutData(initialShopLayoutData)
                    }
                }
            }

            else -> {}
        }
    }

    private fun getShopLayoutDataBasedOnSelectedTab(): HomeLayoutData? {
        return shopPageHeaderDataModel?.listDynamicTabData?.getOrNull(
            getSelectedDynamicTabPosition()
        )?.data?.homeLayoutData
    }

    private fun handleSelectedTab(tab: TabLayout.Tab, isActive: Boolean) {
        viewPagerAdapterHeader?.handleSelectedTab(tab, isActive)
    }

    private fun getTabView(index: Int): View? {
        return viewPagerAdapterHeader?.getDynamicTabView(index, selectedPosition)
    }

    private fun sendShopPageClickTabTracker(position: Int) {
        if (!isMyShop) {
            shopPageTracking?.clickShopBottomNav(listShopPageTabModel[position].tabName, shopId, userId)
        }
    }

    private fun getSelectedDynamicTabPosition(): Int {
        var selectedPosition = viewPager?.currentItem.orZero()
        if (tabLayout?.tabCount.isZero()) {
            if (shouldOverrideTabToHome || shouldOverrideTabToProduct || shouldOverrideTabToFeed || shouldOverrideTabToReview) {
                val overrideTabName = when {
                    shouldOverrideTabToHome -> {
                        ShopPageHeaderTabName.HOME
                    }
                    shouldOverrideTabToProduct -> {
                        ShopPageHeaderTabName.PRODUCT
                    }
                    shouldOverrideTabToFeed -> {
                        ShopPageHeaderTabName.FEED
                    }
                    shouldOverrideTabToReview -> {
                        ShopPageHeaderTabName.REVIEW
                    }
                    else -> {
                        ""
                    }
                }
                selectedPosition = getTabPositionBasedOnTabName(overrideTabName)
            } else {
                val selectedTabData = listShopPageTabModel.firstOrNull {
                    it.isFocus
                } ?: run {
                    listShopPageTabModel.firstOrNull {
                        it.isDefault
                    }
                }
                selectedPosition = listShopPageTabModel.indexOf(selectedTabData).takeIf {
                    it >= Int.ZERO
                } ?: Int.ZERO
            }
        }
        return selectedPosition
    }

    private fun getTabPositionBasedOnTabName(overrideTabName: String): Int {
        return listShopPageTabModel.indexOfFirst {
            it.tabName == overrideTabName
        }.takeIf {
            it >= Int.ZERO
        } ?: Int.ZERO
    }

    private fun setupTabContentWrapper(): List<ShopPageHeaderTabModel> {
        val listShopPageTabModel = mutableListOf<ShopPageHeaderTabModel>()
        shopPageHeaderDataModel?.listDynamicTabData?.forEach {
            if(it.name == ShopPageHeaderTabName.FEED && shopPageHeaderP1Data?.isWhitelist != true){
                return@forEach
            }
            val tabContentWrapper = ShopPageHeaderFragmentTabContentWrapper.createInstance().apply {
                setTabData(it)
                shopPageHeaderP1Data?.let { headerData ->
                    setShopPageHeaderP1Data(headerData, getIsEnableDirectPurchase(headerData))
                }
                shopHeaderViewModel?.productListData?.let { productListData ->
                    setInitialProductListData(productListData)
                }
                setShopId(shopId)
                setUserId(userId)
                setIsMyShop(isMyShop)
                setChooseAddressListener(this@ShopPageHeaderFragmentV2)
            }
            listShopPageTabModel.add(
                ShopPageHeaderTabModel(
                    tabName = it.name,
                    tabFragment = tabContentWrapper,
                    iconUrl = it.icon,
                    iconActiveUrl = it.iconFocus,
                    isFocus = it.isFocus == Int.ONE,
                    isDefault = it.isDefault,
                    tabText = it.text
                )
            )
        }
        return listShopPageTabModel
    }

    private fun getFeedTabFragmentClassName(): Class<*>? {
        return try {
            Class.forName(FEED_SHOP_FRAGMENT)
        } catch (e: Exception) {
            null
        }
    }

    private fun onErrorGetShopPageTabData() {
        context?.run {
            setViewState(VIEW_ERROR)
            errorTextView?.apply {
                setType(Typography.HEADING_2)
                text = getString(R.string.shop_page_error_title_get_p1)
            }
            subErrorTextView?.apply {
                setType(Typography.DISPLAY_2)
                setTextColor(
                    MethodChecker.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN950_68
                    )
                )
                text = getString(R.string.shop_page_error_sub_title_get_p1)
            }
            errorButton?.setOnClickListener {
                isRefresh = true
                getInitialData()
                setViewState(VIEW_LOADING)
            }
        }
    }

    private fun updateFavouriteResult(isFavorite: Boolean) {
        activity?.run {
            val userSession = UserSession(this)
            setResult(
                Activity.RESULT_OK,
                intentData.apply {
                    putExtra(SHOP_STATUS_FAVOURITE, isFavorite)
                    putExtra(SHOP_STICKY_LOGIN, userSession.isLoggedIn)
                }
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODER_USER_LOGIN) {
            if (resultCode == Activity.RESULT_OK) {
                refreshData()
            }
        } else if (requestCode == REQUEST_CODE_FOLLOW) {
            if (resultCode == Activity.RESULT_OK) {
                refreshData()
            }
        } else if (requestCode == REQUEST_CODE_USER_LOGIN_CART) {
            if (resultCode == Activity.RESULT_OK) {
                refreshData()
                goToCart()
            }
        } else if (requestCode == REQUEST_CODE_START_LIVE_STREAMING) {
            if (data != null) handleResultVideoFromLiveStreaming(resultCode, data)
        }
    }

    override fun refreshData() {
        isAlreadyGetShopPageP2Data = false
        hideShopPageFab()
        val shopProductListFragment: Fragment? = viewPagerAdapterHeader?.getRegisteredFragment(if (shopPageHeaderDataModel?.isOfficial == true) TAB_POSITION_HOME + 1 else TAB_POSITION_HOME)
        if (shopProductListFragment is ShopPageProductListFragment) {
            shopProductListFragment.clearCache()
        }

        // clear cache feed tab
        shopPageFeedTabSharedViewModel?.clearCache()

        val shopPageHomeFragment: Fragment? = viewPagerAdapterHeader?.getRegisteredFragment(TAB_POSITION_HOME)
        if (shopPageHomeFragment is ShopPageHomeFragment) {
            shopPageHomeFragment.clearCache()
        }
        isRefresh = true
        resetShopProductFilterParameterSharedViewModel()
        getInitialData()
        setViewState(VIEW_LOADING)
        stickyLoginView?.loadContent()
    }

    private fun resetShopProductFilterParameterSharedViewModel() {
        initialProductFilterParameter = ShopProductFilterParameter()
        initialProductFilterParameter?.let {
            shopProductFilterParameterSharedViewModel?.changeSharedSortData(it)
        }
    }

    override fun collapseAppBar() {
        getSelectedFragmentInstance()?.let {
            (it as? ShopPageHeaderFragmentTabContentWrapper)?.setExpandHeader(false)
        }
    }

    override fun isNewlyBroadcastSaved(): Boolean? {
        val args = arguments
        return args?.containsKey(NEWLY_BROADCAST_CHANNEL_SAVED)?.let {
            args.getBoolean(NEWLY_BROADCAST_CHANNEL_SAVED)
        }
    }

    override fun clearIsNewlyBroadcastSaved() {
        arguments?.remove(NEWLY_BROADCAST_CHANNEL_SAVED)
    }

    override fun onFollowerTextClicked(shopFavourited: Boolean) {
        context?.run {
            shopPageTracking?.clickFollowUnfollow(shopFavourited, customDimensionShopPage)
            startActivityForResult(
                ShopFavouriteListActivity.createIntent(
                    this,
                    shopId
                ),
                REQUEST_CODE_FOLLOW
            )
        }
    }

    private fun goToChatSeller() {
        context?.let { context ->
            shopPageTracking?.clickMessageSeller(
                CustomDimensionShopPage.create(
                    shopId,
                    shopPageHeaderDataModel?.isOfficial ?: false,
                    shopPageHeaderDataModel?.isGoldMerchant ?: false
                )
            )
            if (shopHeaderViewModel?.isUserSessionActive == true) {
                shopPageTracking?.eventShopSendChat()
                val intent = RouteManager.getIntent(
                    context,
                    ApplinkConst.TOPCHAT_ASKSELLER,
                    shopId,
                    "",
                    SOURCE_SHOP,
                    shopPageHeaderDataModel?.shopName.orEmpty(),
                    shopPageHeaderDataModel?.avatar.orEmpty()
                )
                startActivity(intent)
            } else {
                startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN), REQUEST_CODER_USER_LOGIN)
            }
        }
    }

    override fun onShopStatusTickerClickableDescriptionClicked(linkUrl: CharSequence) {
        context?.let {
            val link = linkUrl.toString()
            if (WebViewHelper.isUrlValid(linkUrl.toString())) {
                RouteManager.route(
                    context,
                    String.format(
                        Locale.getDefault(),
                        WEBVIEW_ALLOW_OVERRIDE_FORMAT,
                        ApplinkConst.WEBVIEW,
                        false,
                        link
                    )
                )
            } else {
                RouteManager.route(context, link)
            }
        }
    }

    override fun openShopInfo() {
        redirectToShopInfoPage()
    }

    override fun onShopCoverClicked(isOfficial: Boolean, isPowerMerchant: Boolean) {
        RouteManager.route(context, ApplinkConstInternalMarketplace.SHOP_SETTINGS_INFO)
    }

    override fun onSendRequestOpenModerate(choosenOptionValue: String) {
        shopHeaderViewModel?.sendRequestUnmoderateShop(
            shopId = shopId.toDoubleOrZero(),
            optionValue = choosenOptionValue
        )
    }

    override fun setShopUnmoderateRequestBottomSheet(bottomSheetPageHeader: ShopPageHeaderRequestUnmoderateBottomSheet) {
        shopPageHeaderUnmoderateBottomSheet = bottomSheetPageHeader
        shopPageHeaderUnmoderateBottomSheet?.show(fragmentManager)
        shopPageHeaderUnmoderateBottomSheet?.showLoading()
        shopHeaderViewModel?.checkShopRequestModerateStatus()
    }

    override fun onClickShopBasicInfoSection(appLink: String) {
        sendTrackerClickHeaderShopName()
        if (isShopInfoAppLink(appLink)) {
            redirectToShopInfoPage()
        } else {
            RouteManager.route(context, appLink)
        }
    }

    private fun sendTrackerClickHeaderShopName() {
        shopPageTracking?.clickHeaderShopName(shopId, userId)
    }

    override fun onShopReviewClicked(appLink: String) {
        sendTrackerClickHeaderShopReview()
        RouteManager.route(context, appLink)
    }

    private fun sendTrackerClickHeaderShopReview() {
        shopPageTracking?.clickHeaderShopReview(shopId, userId)
    }

    override fun onChatButtonClicked() {
        sendTrackerClickHeaderShopChat()
        goToChatSeller()
    }

    private fun sendTrackerClickHeaderShopChat() {
        shopPageTracking?.clickHeaderShopChat(shopId, userId)
    }

    override fun onFollowButtonClicked() {
        sendTrackerClickHeaderShopFollow()
        toggleFollowUnfollowButton()
    }

    private fun sendTrackerClickHeaderShopFollow() {
        shopPageTracking?.clickHeaderShopFollow(shopId, userId)
    }

    override fun onUspClicked(listDynamicUspValue: List<String>) {
        sendTrackerClickHeaderShopUsp(listDynamicUspValue)
    }

    private fun sendTrackerClickHeaderShopUsp(listDynamicUspValue: List<String>) {
        shopPageTracking?.clickHeaderShopUsp(shopId, userId, listDynamicUspValue)
    }

    override fun onCompleteCheckRequestModerateStatus(moderateStatusResult: ShopModerateRequestResult) {
        when (moderateStatusResult.status) {
            ShopModerateRequestStatusCode.SHOP_MODERATED, ShopModerateRequestStatusCode.SHOP_MODERATION_REQUEST_REJECTED -> {
                shopPageHeaderUnmoderateBottomSheet?.showOptionList()
            }
            ShopModerateRequestStatusCode.SHOP_REQUESTING_OPEN_MODERATE -> {
                shopPageHeaderUnmoderateBottomSheet?.showModerateStatus()
            }
        }
    }

    override fun onCompleteSendRequestOpenModerate() {
        shopPageHeaderUnmoderateBottomSheet?.setLoadingButton(false)
        shopPageHeaderUnmoderateBottomSheet?.dismiss()
    }

    private fun saveShopImage() {
        shopPageHeaderDataModel?.shopSnippetUrl?.let {
            shopHeaderViewModel?.saveShopImageToPhoneStorage(context, it)
        }
    }

    private fun updateScrollToTopButtonMargin() {
        val scrollToTopButtonLayoutParams = (scrollToTopButton?.layoutParams as ViewGroup.MarginLayoutParams)
        if (stickyLoginView?.isShowing() == true) {
            stickyLoginView?.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            val stickyLoginViewHeight = stickyLoginView?.measuredHeight.orZero()
            scrollToTopButtonLayoutParams.setMargins(
                scrollToTopButtonLayoutParams.leftMargin,
                scrollToTopButtonLayoutParams.topMargin,
                scrollToTopButtonLayoutParams.rightMargin,
                stickyLoginViewHeight + MARGIN_BOTTOM_STICKY_LOGIN
            )
        } else {
            scrollToTopButtonLayoutParams.setMargins(
                scrollToTopButtonLayoutParams.leftMargin,
                scrollToTopButtonLayoutParams.topMargin,
                scrollToTopButtonLayoutParams.rightMargin,
                initialScrollToTopButtonMarginBottom + MARGIN_BOTTOM_STICKY_LOGIN
            )
        }
        scrollToTopButton?.layoutParams = scrollToTopButtonLayoutParams
    }

    private fun updateViewPagerPadding() {
        if (stickyLoginView?.isShowing() == true) {
            viewPager?.setPadding(0, 0, 0, stickyLoginView?.height.orZero())
        } else {
            viewPager?.setPadding(0, 0, 0, 0)
        }
    }

    private fun onToasterNoUploadProduct(view: View, message: String, isFirstCreateShop: Boolean) {
        if (isFirstCreateShop) {
            Toaster.make(view, message, actionText = getString(R.string.shop_page_product_action_no_upload_product), type = Toaster.TYPE_NORMAL)
            this.isFirstCreateShop = false
        }
    }

    fun showBottomSheetSellerMigration() {
        view?.findViewById<View>(bottom_sheet_wrapper)?.visibility = View.VISIBLE
    }

    fun hideBottomSheetSellerMigration() {
        view?.findViewById<View>(bottom_sheet_wrapper)?.visibility = View.GONE
    }

    private fun setShopName() {
        if (isMyShop) {
            shopPageHeaderDataModel?.shopName = shopHeaderViewModel?.ownerShopName.orEmpty()
            setFragmentTabContentWrapperShopName(shopHeaderViewModel?.ownerShopName.orEmpty())
        }
    }

    private fun setFragmentTabContentWrapperShopName(shopName: String) {
        listShopPageTabModel.forEach {
            if(it.tabFragment is ShopPageHeaderFragmentTabContentWrapper){
                it.tabFragment.updateShopName(
                    shopName
                )
            }
        }
    }

    override fun onStartLiveStreamingClicked() {
        // will be deleted later
    }

    /**
     * Play Widget "Start Live Streaming"
     */
    override fun onStartLiveStreamingClicked(
        componentModel: ShopPageHeaderPlayWidgetButtonComponentUiModel,
        shopPageHeaderWidgetUiModel: ShopPageHeaderWidgetUiModel,
        broadcasterConfig: Broadcaster.Config
    ) {
        val valueDisplayed = componentModel.label
        mBroadcasterConfig = broadcasterConfig
        sendClickShopHeaderComponentTracking(
            shopPageHeaderWidgetUiModel,
            componentModel,
            valueDisplayed
        )

        showContentCreationOptionBottomSheet()
    }

    override fun onImpressionPlayWidgetComponent(componentModel: ShopPageHeaderPlayWidgetButtonComponentUiModel, shopPageHeaderWidgetUiModel: ShopPageHeaderWidgetUiModel) {
        val valueDisplayed = componentModel.label
        sendImpressionShopHeaderComponentTracking(
            shopPageHeaderWidgetUiModel,
            componentModel,
            valueDisplayed
        )
    }

    private fun handleResultVideoFromLiveStreaming(resultCode: Int, data: Intent) {
        if (resultCode == Activity.RESULT_OK) {
            handlePlayBroadcastExtra(data)
            refreshData()
        }
    }

    private fun handlePlayBroadcastExtra(data: Intent) {
        val isChannelSaved: Boolean = if (data.hasExtra(NEWLY_BROADCAST_CHANNEL_SAVED)) {
            data.getBooleanExtra(NEWLY_BROADCAST_CHANNEL_SAVED, false)
        } else {
            return
        }

        if (arguments == null) arguments = Bundle()
        arguments?.putBoolean(NEWLY_BROADCAST_CHANNEL_SAVED, isChannelSaved)

        if (isChannelSaved) {
            showWidgetTranscodingToaster()
        } else {
            showWidgetDeletedToaster()
        }
    }

    private fun showWidgetTranscodingToaster() {
        Toaster.build(
            view = requireView(),
            text = getString(R.string.shop_page_play_widget_sgc_save_video),
            duration = Toaster.LENGTH_LONG,
            type = Toaster.TYPE_NORMAL
        ).show()
    }

    private fun showWidgetDeletedToaster() {
        Toaster.build(
            requireView(),
            getString(R.string.shop_page_play_widget_sgc_video_deleted),
            Toaster.LENGTH_SHORT,
            Toaster.TYPE_NORMAL
        ).show()
    }

    private fun showToasterShopUnmoderate(message: String, type: Int) {
        Toaster.build(
            requireView(),
            message,
            Toaster.LENGTH_SHORT,
            type
        ).show()
    }

    private fun isShopInfoAppLink(appLink: String): Boolean {
        val appLinkUri = Uri.parse(appLink)
        return appLinkUri.lastPathSegment.orEmpty() == ShopPageHeaderActivity.PATH_INFO
    }

    override fun isTabSelected(tabFragmentClass: Class<out Any>): Boolean {
        return if (viewPagerAdapterHeader?.isFragmentObjectExists(tabFragmentClass) == true) {
            viewPagerAdapterHeader?.getFragmentPosition(tabFragmentClass) == selectedPosition
        } else {
            false
        }
    }

    override fun onShopBasicInfoWidgetComponentClicked(
        componentModel: ShopPageHeaderBadgeTextValueComponentUiModel?,
        shopPageHeaderWidgetUiModel: ShopPageHeaderWidgetUiModel?
    ) {
        val valueDisplayed = componentModel?.text?.getOrNull(1)?.textHtml?.split("•")?.getOrNull(0).orEmpty().trim()
        val appLink = componentModel?.text?.getOrNull(0)?.textLink.orEmpty()
        sendClickShopHeaderComponentTracking(
            shopPageHeaderWidgetUiModel,
            componentModel,
            valueDisplayed
        )
        if (isShopInfoAppLink(appLink)) {
            redirectToShopInfoPage()
        } else {
            RouteManager.route(context, appLink)
        }
    }

    override fun onImpressionShopBasicInfoWidgetComponent(
        componentModel: ShopPageHeaderBadgeTextValueComponentUiModel?,
        shopPageHeaderWidgetUiModel: ShopPageHeaderWidgetUiModel?
    ) {
        val valueDisplayed = componentModel?.text?.getOrNull(1)?.textHtml?.split("•")?.getOrNull(0).orEmpty().trim()
        sendImpressionShopHeaderComponentTracking(
            shopPageHeaderWidgetUiModel,
            componentModel,
            valueDisplayed
        )
    }

    override fun onShopPerformanceWidgetBadgeTextValueItemClicked(
        componentModel: ShopPageHeaderBadgeTextValueComponentUiModel,
        shopPageHeaderWidgetUiModel: ShopPageHeaderWidgetUiModel
    ) {
        val appLink = componentModel.text.getOrNull(0)?.textLink.orEmpty()
        val valueDisplayed = componentModel.text.getOrNull(0)?.textHtml?.trim().orEmpty()
        val componentName = componentModel.name
        sendClickShopHeaderComponentTracking(
            shopPageHeaderWidgetUiModel,
            componentModel,
            valueDisplayed
        )

        // check type for non applink component
        if (componentName == BaseShopPageHeaderComponentUiModel.ComponentName.SHOP_OPERATIONAL_HOUR) {
            // show shop operational hour bottomsheet
            RouteManager.route(context, ApplinkConstInternalMarketplace.SHOP_OPERATIONAL_HOUR_BOTTOM_SHEET, shopId)
        }
        RouteManager.route(context, appLink)
    }

    override fun onImpressionShopPerformanceWidgetBadgeTextValueItem(
        componentModel: ShopPageHeaderBadgeTextValueComponentUiModel,
        shopPageHeaderWidgetUiModel: ShopPageHeaderWidgetUiModel
    ) {
        val valueDisplayed = componentModel.text.getOrNull(0)?.textHtml.orEmpty()
        sendImpressionShopHeaderComponentTracking(
            shopPageHeaderWidgetUiModel,
            componentModel,
            valueDisplayed
        )
    }

    override fun onButtonChatClicked(
        componentModel: ShopPageHeaderButtonComponentUiModel,
        shopPageHeaderWidgetUiModel: ShopPageHeaderWidgetUiModel
    ) {
        val valueDisplayed = componentModel.label
        sendClickShopHeaderComponentTracking(
            shopPageHeaderWidgetUiModel,
            componentModel,
            valueDisplayed
        )
        goToChatSeller()
    }

    override fun onImpressionButtonChat(
        componentModel: ShopPageHeaderButtonComponentUiModel,
        shopPageHeaderWidgetUiModel: ShopPageHeaderWidgetUiModel
    ) {
        val valueDisplayed = componentModel.label
        sendImpressionShopHeaderComponentTracking(
            shopPageHeaderWidgetUiModel,
            componentModel,
            valueDisplayed
        )
    }

    private fun sendImpressionShopHeaderComponentTracking(
        shopPageHeaderWidgetUiModel: ShopPageHeaderWidgetUiModel?,
        componentModelPage: BaseShopPageHeaderComponentUiModel?,
        valueDisplayed: String
    ) {
        val componentId = componentModelPage?.name.orEmpty()
        val componentName = componentModelPage?.name.orEmpty()
        val componentPosition = componentModelPage?.componentPosition.orZero()
        val headerId = shopPageHeaderWidgetUiModel?.widgetId.orEmpty()
        val headerType = shopPageHeaderWidgetUiModel?.type.orEmpty()
        shopPageTracking?.impressionShopHeaderComponent(
            isMyShop,
            shopId,
            userId,
            valueDisplayed,
            componentId,
            componentName,
            headerId,
            headerType,
            componentPosition,
            customDimensionShopPage
        )
    }

    private fun sendClickShopHeaderComponentTracking(
        shopPageHeaderWidgetUiModel: ShopPageHeaderWidgetUiModel?,
        componentModelPage: BaseShopPageHeaderComponentUiModel?,
        valueDisplayed: String
    ) {
        val componentId = componentModelPage?.name.orEmpty()
        val componentName = componentModelPage?.name.orEmpty()
        val componentPosition = componentModelPage?.componentPosition.orZero()
        val headerId = shopPageHeaderWidgetUiModel?.widgetId.orEmpty()
        val headerType = shopPageHeaderWidgetUiModel?.type.orEmpty()
        shopPageTracking?.clickShopHeaderComponent(
            isMyShop,
            shopId,
            userId,
            valueDisplayed,
            componentId,
            componentName,
            headerId,
            headerType,
            componentPosition,
            customDimensionShopPage
        )
    }

    private fun showShopNoteBottomSheet() {
        ShopNoteBottomSheet.createInstance(shopId).show(childFragmentManager, ShopNoteBottomSheet.TAG)
    }

    override fun onClickNoteButton(link: String) {
        showShopNoteBottomSheet()
    }

    override fun setFollowStatus(isFollowing: Boolean) {
        // will be deleted later
    }

    override fun isFirstTimeVisit(): Boolean? {
        return sharedPreferences?.getBoolean(IS_FIRST_TIME_VISIT, false)
    }

    override fun saveFirstTimeVisit() {
        sharedPreferences?.edit()?.run {
            putBoolean(IS_FIRST_TIME_VISIT, true)
        }?.apply()
    }

    override fun onImpressionVoucherFollowUnFollowShop() {
        shopPageTracking?.impressionVoucherFollowUnfollowShop(shopId, userId)
    }

    override fun onClickFollowUnFollowButton(
        componentModel: ShopPageHeaderButtonComponentUiModel,
        shopPageHeaderWidgetUiModel: ShopPageHeaderWidgetUiModel
    ) {
        val valueDisplayed = componentModel.label
        sendClickShopHeaderComponentTracking(
            shopPageHeaderWidgetUiModel,
            componentModel,
            valueDisplayed
        )
        toggleFollowUnfollowButton()
    }

    override fun onImpressionFollowButtonComponent(componentModel: ShopPageHeaderButtonComponentUiModel, shopPageHeaderWidgetUiModel: ShopPageHeaderWidgetUiModel) {
        val valueDisplayed = componentModel.label
        sendImpressionShopHeaderComponentTracking(
            shopPageHeaderWidgetUiModel,
            componentModel,
            valueDisplayed
        )
    }

    private fun toggleFollowUnfollowButton() {
        shopPageTracking?.clickFollowUnfollowShopWithoutShopFollower(
            !isFollowing,
            CustomDimensionShopPage.create(
                shopId,
                shopPageHeaderDataModel?.isOfficial ?: false,
                shopPageHeaderDataModel?.isGoldMerchant ?: false
            )
        )

        shopPageTracking?.clickFollowUnfollowShop(
            !isFollowing,
            shopId,
            shopHeaderViewModel?.userId
        )

        shopPageTracking?.sendMoEngageFavoriteEvent(
            shopPageHeaderDataModel?.shopName.orEmpty(),
            shopId,
            shopPageHeaderDataModel?.domain.orEmpty(),
            shopPageHeaderDataModel?.location.orEmpty(),
            shopPageHeaderDataModel?.isOfficial ?: false,
            isFollowing
        )

        val action = if (isFollowing) {
            UpdateFollowStatusUseCase.ACTION_UNFOLLOW
        } else {
            UpdateFollowStatusUseCase.ACTION_FOLLOW
        }
        setFragmentTabContentWrapperFollowButtonLoading(true)
        shopHeaderViewModel?.updateFollowStatus(shopId, action)
    }

    override fun onImpressionShopPerformanceWidgetImageOnlyItem(
        componentModel: ShopPageHeaderImageOnlyComponentUiModel,
        shopPageHeaderWidgetUiModel: ShopPageHeaderWidgetUiModel
    ) {
        sendImpressionShopHeaderComponentTracking(
            shopPageHeaderWidgetUiModel,
            componentModel,
            ""
        )
    }

    override fun onImpressionShopPerformanceWidgetImageTextItem(componentModel: ShopPageHeaderImageTextComponentUiModel, shopPageHeaderWidgetUiModel: ShopPageHeaderWidgetUiModel) {
        sendImpressionShopHeaderComponentTracking(
            shopPageHeaderWidgetUiModel,
            componentModel,
            ""
        )
    }

    private fun updateCurrentPageLocalCacheModelData() {
        localCacheModel = getShopPageWidgetUserAddressLocalData(context)
    }

    override fun onLocalizingAddressUpdatedFromWidget() {
        refreshData()
    }

    override fun onLocalizingAddressUpdatedFromBackground() {
    }

    override fun onLocalizingAddressServerDown() {
        refreshData()
    }

    override fun onLocalizingAddressRollOutUser(isRollOutUser: Boolean) {}

    override fun getLocalizingAddressHostFragment(): Fragment {
        return this
    }

    override fun getLocalizingAddressHostSourceData(): String {
        return "shop"
    }

    override fun getLocalizingAddressHostSourceTrackingData(): String {
        return SOURCE
    }

    override fun onLocalizingAddressLoginSuccess() {
        refreshData()
    }

    override fun onChangeTextColor(): Int {
        val headerLayoutData = shopPageHeaderP1Data?.shopHeaderLayoutData
        val isOverrideTextColor = headerLayoutData?.isOverrideTheme.orFalse()
        return if (isOverrideTextColor) {
            if(getShopHeaderConfig()?.patternColorType == ShopPageHeaderLayoutUiModel.ColorType.DARK.value) {
                R.color.dms_static_Unify_NN950_light
            } else {
                com.tokopedia.unifyprinciples.R.color.Unify_Static_White
            }
        } else {
            super.onChangeTextColor()
        }
    }

    private fun getShopHeaderConfig(): ShopPageHeaderLayoutUiModel.Config? {
        return shopPageHeaderP1Data?.shopHeaderLayoutData?.getShopConfigListByName(ShopPageHeaderLayoutUiModel.ConfigName.SHOP_HEADER)
    }

    fun expandHeader() {
        getSelectedFragmentInstance()?.let {
            (it as? ShopPageHeaderFragmentTabContentWrapper)?.setExpandHeader(true)
        }
    }

    fun showScrollToTopButton() {
        if (scrollToTopButton?.isShown == false) {
            scrollToTopButton?.show()
            scrollToTopButton?.visible()
        }
    }

    fun hideScrollToTopButton() {
        if (scrollToTopButton?.isShown == true) {
            scrollToTopButton?.hide()
            scrollToTopButton?.gone()
        }
    }

    fun isShopWidgetAlreadyShown(): Boolean {
        return shopPageHeaderDataModel?.listDynamicTabData?.any {
            it.name == ShopPageHeaderTabName.HOME || it.name == ShopPageHeaderTabName.CAMPAIGN
        } ?: false
    }

    override fun onShareOptionClicked(shareModel: ShareModel) {
        shareModel.socialMediaName?.let { name ->
            shopHeaderViewModel?.sendShopShareTracker(
                shopId,
                channel = when (shareModel) {
                    is ShareModel.CopyLink -> {
                        ShopPageConstant.SHOP_SHARE_DEFAULT_CHANNEL
                    }
                    is ShareModel.Others -> {
                        ShopPageConstant.SHOP_SHARE_OTHERS_CHANNEL
                    }
                    else -> name
                }
            )
        }

        if (isGeneralShareBottomSheet) {
            shopPageTracking?.clickShareBottomSheetOption(
                shareModel.channel.orEmpty(),
                customDimensionShopPage,
                userId,
                shareModel.campaign?.split("-")?.lastOrNull().orEmpty(),
                universalShareBottomSheet?.getUserType() ?: ""
            )
            if (!isMyShop) {
                shopPageTracking?.clickGlobalHeaderShareBottomSheetOption(
                    shareModel.channel.orEmpty(),
                    customDimensionShopPage,
                    userId
                )
            }
        } else {
            shopPageTracking?.clickScreenshotShareBottomSheetOption(
                shareModel.channel.orEmpty(),
                customDimensionShopPage,
                userId,
                universalShareBottomSheet?.getUserType() ?: "",
                shareModel.campaign?.split("-")?.lastOrNull().orEmpty()
            )
        }
    }

    private fun getShareBottomSheetOgTitle(): String {
        return shopPageHeaderDataModel?.shopName.orEmpty()
    }

    private fun getShareBottomSheetOgDescription(): String {
        var ogDescription = shopPageHeaderDataModel?.location.orEmpty()
        if (shopPagePageHeaderWidgetList.isNotEmpty()) {
            val performanceWidget = shopPagePageHeaderWidgetList.filter { it.type == ShopPageParamModel.ShopInfoType.SHOP_PERFORMANCE.typeName }
            val performanceBadgeTextWidget = performanceWidget.firstOrNull()?.componentPages?.filter {
                (it.type == ShopPageParamModel.ShopInfoType.BADGE_TEXT.typeName)
            }.orEmpty()
            if (performanceBadgeTextWidget.isNotEmpty()) {
                val opsHourWidget = performanceBadgeTextWidget.filter {
                    (it as ShopPageHeaderBadgeTextValueComponentUiModel).text.getOrNull(Int.ONE)?.textHtml ==
                        ShopPageParamModel.ShopPerformanceLabel.OPERATIONAL_HOURS.labelName
                }
                if (opsHourWidget.isNotEmpty()) {
                    var infoValue = (opsHourWidget.firstOrNull() as? ShopPageHeaderBadgeTextValueComponentUiModel)?.text?.firstOrNull()?.textHtml.orEmpty()
                    if (infoValue != getString(R.string.shop_ops_hour_open_all_day) && infoValue != getString(R.string.shop_ops_hour_holiday)) {
                        infoValue = getString(R.string.shop_page_share_chat_bubble_operational_hour_format, infoValue)
                    }
                    ogDescription += " | $infoValue"
                }
            }
        }
        return ogDescription
    }

    override fun onCloseOptionClicked() {
        if (isGeneralShareBottomSheet) {
            shopPageTracking?.clickCloseNewShareBottomSheet(customDimensionShopPage, userId, universalShareBottomSheet?.getUserType() ?: "")
        } else {
            shopPageTracking?.clickCloseNewScreenshotShareBottomSheet(customDimensionShopPage, userId, universalShareBottomSheet?.getUserType() ?: "")
        }
    }

    override fun screenShotTaken(path: String) {
        isGeneralShareBottomSheet = false
        showUniversalShareBottomSheet(path)
        shopPageTracking?.onImpressionScreenshotShareBottomSheet(
            customDimensionShopPage,
            userId,
            universalShareBottomSheet?.getUserType() ?: ""
        )
    }

    private fun extractIdrPriceToRawValue(priceTextIdr: String): Long {
        return priceTextIdr.replace(IDR_CURRENCY_TO_RAW_STRING_REGEX.toRegex(), "").toLong()
    }

    private fun showUniversalShareBottomSheet(path: String? = null) {
        universalShareBottomSheet = UniversalShareBottomSheet.createInstance(view).apply {
            init(this@ShopPageHeaderFragmentV2)
            path?.let {
                setImageOnlySharingOption(true)
                setScreenShotImagePath(path)
            }
            enableDefaultShareIntent()
            setMetaData(
                shopPageHeaderDataModel?.shopName.orEmpty(),
                shopPageHeaderDataModel?.avatar.orEmpty(),
                ""
            )
            imageSaved(shopImageFilePath)
            setSubject(shopPageHeaderDataModel?.shopName.toString())
            setLinkProperties(
                LinkProperties(
                    linkerType = LinkerData.SHOP_TYPE,
                    ogTitle = getShareBottomSheetOgTitle(),
                    ogDescription = getShareBottomSheetOgDescription(),
                    desktopUrl = shopPageHeaderDataModel?.shopCoreUrl ?: "",
                    id = shopPageHeaderDataModel?.shopId ?: ""
                )
            )
            val shareString = this@ShopPageHeaderFragmentV2.getString(R.string.shop_page_share_text, shopPageHeaderDataModel?.shopName)
            setShareText("$shareString%s")
        }

        configShopShareBottomSheetImpressionTracker()
        // activate contextual image
        val initialProductListData = shopHeaderViewModel?.productListData?.data ?: listOf()
        val initialProductListSize = initialProductListData.size

        // core params
        val shopPageParamModel = ShopPageParamModel(
            shopProfileImgUrl = shopPageHeaderDataModel?.avatar.orEmpty(),
            shopName = shopPageHeaderDataModel?.shopName.orEmpty(),
            shopLocation = shopPageHeaderDataModel?.location.orEmpty()
        )

        // shop type / badge
        val shopType = when {
            shopPageHeaderDataModel?.isOfficial == true -> ShopPageParamModel.ShopTier.OFFICIAL_STORE.tierId
            shopPageHeaderDataModel?.isGoldMerchant == true -> {
                if (shopPageHeaderDataModel?.pmTier == Int.ZERO) {
                    ShopPageParamModel.ShopTier.POWER_MERCHANT.tierId
                } else {
                    ShopPageParamModel.ShopTier.POWER_MERCHANT_PRO.tierId
                }
            }
            else -> ShopPageParamModel.ShopTier.REGULAR.tierId
        }
        shopPageParamModel.shopBadge = shopType

        // shop performance info params
        if (shopPagePageHeaderWidgetList.isNotEmpty()) {
            val performanceWidget = shopPagePageHeaderWidgetList.filter { it.type == ShopPageParamModel.ShopInfoType.SHOP_PERFORMANCE.typeName }
            var totalShopInfo = Int.ZERO
            shopPageParamModel.isHeadless = performanceWidget.isEmpty()
            performanceWidget.firstOrNull()?.componentPages?.forEach {
                if (totalShopInfo >= IMG_GENERATOR_SHOP_INFO_MAX_SIZE) {
                    return@forEach
                }
                var shopInfoType = ""
                var shopInfoLabel = ""
                var shopInfoValue = ""
                when (it.type) {
                    ShopPageParamModel.ShopInfoType.BADGE_TEXT.typeName -> {
                        val textValueComponentUiModel = it as ShopPageHeaderBadgeTextValueComponentUiModel
                        shopInfoType = if (it.name == ShopPageParamModel.ShopInfoName.SHOP_RATING.infoName) {
                            ShopPageParamModel.ShopInfoName.SHOP_RATING.infoNameValue
                        } else {
                            ShopPageParamModel.ShopInfoName.FREE_TEXT.infoNameValue
                        }
                        shopInfoValue = textValueComponentUiModel.text[Int.ZERO].textHtml
                        shopInfoLabel = textValueComponentUiModel.text[Int.ONE].textHtml
                    }
                    ShopPageParamModel.ShopInfoType.IMAGE_ONLY.typeName -> {
                        if (it.name == ShopPageParamModel.ShopInfoName.FREE_SHIPPING.infoName) {
                            shopInfoType = ShopPageParamModel.ShopInfoName.FREE_SHIPPING.infoNameValue
                        }
                    }
                }
                totalShopInfo++
                when (totalShopInfo) {
                    IMG_GENERATOR_SHOP_INFO_1 -> {
                        // assign first shop info type
                        shopPageParamModel.info1Type = shopInfoType
                        shopPageParamModel.info1Value = shopInfoValue
                        shopPageParamModel.info1Label = shopInfoLabel
                    }
                    IMG_GENERATOR_SHOP_INFO_2 -> {
                        // assign first shop info type
                        shopPageParamModel.info2Type = shopInfoType
                        shopPageParamModel.info2Value = shopInfoValue
                        shopPageParamModel.info2Label = shopInfoLabel
                    }
                    IMG_GENERATOR_SHOP_INFO_3 -> {
                        // assign first shop info type
                        shopPageParamModel.info3Type = shopInfoType
                        shopPageParamModel.info3Value = shopInfoValue
                        shopPageParamModel.info3Label = shopInfoLabel
                    }
                }
            }
        }

        // shop products params
        if (initialProductListSize.isMoreThanZero()) {
            val isHasOneProduct = initialProductListSize >= PRODUCT_LIST_INDEX_ONE
            val isHasTwoProducts = initialProductListSize >= PRODUCT_LIST_INDEX_TWO
            val isHasThreeProducts = initialProductListSize >= PRODUCT_LIST_INDEX_THREE
            val isHasSixProducts = initialProductListSize >= PRODUCT_LIST_IMG_GENERATOR_MAX_SIZE
            when {
                isHasSixProducts -> {
                    val productOne = initialProductListData[PRODUCT_LIST_INDEX_ZERO]
                    val productTwo = initialProductListData[PRODUCT_LIST_INDEX_ONE]
                    val productThree = initialProductListData[PRODUCT_LIST_INDEX_TWO]
                    val productFour = initialProductListData[PRODUCT_LIST_INDEX_THREE]
                    val productFive = initialProductListData[PRODUCT_LIST_INDEX_FOUR]
                    val productSix = initialProductListData[PRODUCT_LIST_INDEX_FIVE]
                    val productImage1 = productOne.primaryImage.original
                    val productPrice1 = extractIdrPriceToRawValue(productOne.price.textIdr)
                    val productImage2 = productTwo.primaryImage.original
                    val productPrice2 = extractIdrPriceToRawValue(productTwo.price.textIdr)
                    val productImage3 = productThree.primaryImage.original
                    val productPrice3 = extractIdrPriceToRawValue(productThree.price.textIdr)
                    val productImage4 = productFour.primaryImage.original
                    val productPrice4 = extractIdrPriceToRawValue(productFour.price.textIdr)
                    val productImage5 = productFive.primaryImage.original
                    val productPrice5 = extractIdrPriceToRawValue(productFive.price.textIdr)
                    val productImage6 = productSix.primaryImage.original
                    val productPrice6 = extractIdrPriceToRawValue(productSix.price.textIdr)
                    shopPageParamModel.productImage1 = productImage1
                    shopPageParamModel.productPrice1 = productPrice1
                    shopPageParamModel.productImage2 = productImage2
                    shopPageParamModel.productPrice2 = productPrice2
                    shopPageParamModel.productImage3 = productImage3
                    shopPageParamModel.productPrice3 = productPrice3
                    shopPageParamModel.productImage4 = productImage4
                    shopPageParamModel.productPrice4 = productPrice4
                    shopPageParamModel.productImage5 = productImage5
                    shopPageParamModel.productPrice5 = productPrice5
                    shopPageParamModel.productImage6 = productImage6
                    shopPageParamModel.productPrice6 = productPrice6
                    shopPageParamModel.productCount = PRODUCT_LIST_IMG_GENERATOR_MAX_SIZE
                }
                isHasThreeProducts -> {
                    val productOne = initialProductListData[PRODUCT_LIST_INDEX_ZERO]
                    val productTwo = initialProductListData[PRODUCT_LIST_INDEX_ONE]
                    val productThree = initialProductListData[PRODUCT_LIST_INDEX_TWO]
                    val productImage1 = productOne.primaryImage.original
                    val productPrice1 = extractIdrPriceToRawValue(productOne.price.textIdr)
                    val productImage2 = productTwo.primaryImage.original
                    val productPrice2 = extractIdrPriceToRawValue(productTwo.price.textIdr)
                    val productImage3 = productThree.primaryImage.original
                    val productPrice3 = extractIdrPriceToRawValue(productThree.price.textIdr)
                    shopPageParamModel.productImage1 = productImage1
                    shopPageParamModel.productPrice1 = productPrice1
                    shopPageParamModel.productImage2 = productImage2
                    shopPageParamModel.productPrice2 = productPrice2
                    shopPageParamModel.productImage3 = productImage3
                    shopPageParamModel.productPrice3 = productPrice3
                    shopPageParamModel.productCount = PRODUCT_LIST_INDEX_THREE
                }
                isHasTwoProducts -> {
                    val productOne = initialProductListData[PRODUCT_LIST_INDEX_ZERO]
                    val productTwo = initialProductListData[PRODUCT_LIST_INDEX_ONE]
                    val productImage1 = productOne.primaryImage.original
                    val productPrice1 = extractIdrPriceToRawValue(productOne.price.textIdr)
                    val productImage2 = productTwo.primaryImage.original
                    val productPrice2 = extractIdrPriceToRawValue(productTwo.price.textIdr)
                    shopPageParamModel.productImage1 = productImage1
                    shopPageParamModel.productPrice1 = productPrice1
                    shopPageParamModel.productImage2 = productImage2
                    shopPageParamModel.productPrice2 = productPrice2
                    shopPageParamModel.productCount = PRODUCT_LIST_INDEX_TWO
                }
                isHasOneProduct -> {
                    val productOne = initialProductListData[PRODUCT_LIST_INDEX_ZERO]
                    val productImage1 = productOne.primaryImage.original
                    val productPrice1 = extractIdrPriceToRawValue(productOne.price.textIdr)
                    shopPageParamModel.productImage1 = productImage1
                    shopPageParamModel.productPrice1 = productPrice1
                    shopPageParamModel.productCount = PRODUCT_LIST_INDEX_ONE
                }
            }
        }
        universalShareBottomSheet?.setImageGeneratorParam(shopPageParamModel)
        universalShareBottomSheet?.getImageFromMedia(shopPageParamModel.shopProfileImgUrl.isNotEmpty())
        universalShareBottomSheet?.setMediaPageSourceId(ImageGeneratorConstants.ImageGeneratorSourceId.SHOP_PAGE)
        val inputShare = AffiliateInput().apply {
            pageDetail = PageDetail(pageId = shopId, pageType = PageType.SHOP.value, siteId = "1", verticalId = "1")
            pageType = PageType.SHOP.value
            product = Product()
            shop = Shop(shopID = shopId, shopStatus = shopPageHeaderDataModel?.shopStatus, isOS = shopPageHeaderDataModel?.isOfficial == true, isPM = shopPageHeaderDataModel?.isGoldMerchant == true)
            affiliateLinkType = AffiliateLinkType.SHOP
        }
        universalShareBottomSheet?.enableAffiliateCommission(inputShare)
        universalShareBottomSheet?.show(activity?.supportFragmentManager, this, screenShotDetector)
        universalShareBottomSheet?.setUtmCampaignData(
            SHOP_PAGE_SHARE_BOTTOM_SHEET_PAGE_NAME,
            userId.ifEmpty { "0" },
            shopId,
            SHOP_PAGE_SHARE_BOTTOM_SHEET_FEATURE_NAME
        )
    }

    private fun configShopShareBottomSheetImpressionTracker() {
        if (isLogin) {
            universalShareBottomSheet?.setOnGetAffiliateData {
                trackImpressionShareBottomSheet()
            }
        } else {
            trackImpressionShareBottomSheet()
        }
    }

    private fun trackImpressionShareBottomSheet() {
        shopPageTracking?.onImpressionShareBottomSheet(
            customDimensionShopPage,
            userId,
            universalShareBottomSheet?.getUserType() ?: ""
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        screenShotDetector?.onRequestPermissionsResult(requestCode, grantResults, this)
    }

    fun setupShopPageFab(config: ShopPageFabConfig) {
        shopPageFab?.let { fab ->
            fab.color = config.color
            fab.type = config.type
            fab.addItem(config.items)
            config.onMainCircleButtonClicked?.let {
                fab.circleMainMenu.setOnClickListener(it)
            }
        }
    }

    fun showShopPageFab() {
        shopPageFab?.show()
    }

    fun hideShopPageFab() {
        shopPageFab?.hide()
    }

    fun getSelectedFragmentInstance(): Fragment? {
        return viewPagerAdapterHeader?.getRegisteredFragment(viewPager?.currentItem.orZero())
    }

    override fun permissionAction(action: String, label: String) {
        shopPageTracking?.clickUniversalSharingPermission(action, label.replaceFirstChar(Char::titlecase), shopId, userId)
    }

    fun setupShopPageLottieAnimation(lottieUrl: String) {
        context?.let {
            val lottieCompositionLottieTask = LottieCompositionFactory.fromUrl(it, lottieUrl)
            lottieCompositionLottieTask.addListener { result ->
                viewBinding?.shopPageLottie?.apply {
                    show()
                    setComposition(result)
                    playAnimation()
                    this.addAnimatorListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(p0: Animator) {}

                        override fun onAnimationEnd(p0: Animator) {
                            hide()
                        }

                        override fun onAnimationCancel(p0: Animator) {}
                        override fun onAnimationRepeat(p0: Animator) {}
                    })
                }
            }
        }
    }

    fun isShowConfetti(): Boolean {
        return !isConfettiAlreadyShown
    }

    fun setConfettiAlreadyShown() {
        isConfettiAlreadyShown = true
    }

    override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {
        shopPageMiniCartSharedViewModel?.updateSharedMiniCartData(miniCartSimplifiedData)
    }

    fun updateMiniCartWidget(delay: Long = 0) {
        miniCart?.updateData(delay)
    }

    fun createPdpAffiliateLink(basePdpAppLink: String): String {
        return affiliateCookieHelper.createAffiliateLink(
            basePdpAppLink,
            affiliateData?.affiliateTrackerId.orEmpty()
        )
    }

    fun createAffiliateCookieAtcProduct(
        productId: String,
        isVariant: Boolean,
        stockQty: Int
    ) {
        shopHeaderViewModel?.createAffiliateCookieShopAtcProduct(
            affiliateData?.affiliateUUId.orEmpty(),
            affiliateCookieHelper,
            affiliateData?.affiliateChannel.orEmpty(),
            productId,
            isVariant,
            stockQty,
            shopId
        )
    }

    private var shopTickerVisibilityState: Int? = null

    /**
     * function to check whether the input has been valid
     * before hit [com.tokopedia.shop.pageheader.presentation.ShopPageHeaderViewModel.checkAffiliate]
     * by checking shop status is > 0 due to this value the last updated property
     */
    private fun isAffiliateInputValid(shopStatus: Int): Boolean = shopStatus.isMoreThanZero()

    /**
     * check affiliate if [isEnableAffiliateShareIcon] == true && [isMyShop] != true
     */
    private fun checkAffiliate() {
        if (!isEnableAffiliateShareIcon() || isMyShop) return
        val inputAffiliate = AffiliateInput().apply {
            pageDetail = PageDetail(pageId = shopId, pageType = PageType.SHOP.value, siteId = AFFILIATE_SITE_ID, verticalId = AFFILIATE_VERTICAL_ID)
            pageType = PageType.SHOP.value
            product = Product()
            shop = Shop(shopID = shopId, shopStatus = shopPageHeaderDataModel?.shopStatus, isOS = shopPageHeaderDataModel?.isOfficial == true, isPM = shopPageHeaderDataModel?.isGoldMerchant == true)
            affiliateLinkType = AffiliateLinkType.SHOP
        }

        if (isAffiliateInputValid(inputAffiliate.shop?.shopStatus ?: 0)) {
            shopHeaderViewModel?.checkAffiliate(inputAffiliate)
        }
    }

    fun selectShopTab(tabName: String, isScrollToTop: Boolean = false) {
        val selectedTabPosition = getTabPositionBasedOnTabName(tabName)
        viewPager?.setCurrentItem(selectedTabPosition, false)
        tabLayout?.getTabAt(selectedTabPosition)?.select()
        if(isScrollToTop) {
            val selectedFragment = viewPagerAdapterHeader?.getRegisteredFragment(viewPager?.currentItem.orZero())
            (selectedFragment as? ShopPageHeaderFragmentTabContentWrapper)?.scrollToTop()
        }
    }

    override fun onTabFragmentWrapperFinishLoad() {
        getShopPageP2Data()
    }

    override fun getBottomViewContainer(): View? {
        return bottomViewContainer
    }
}
