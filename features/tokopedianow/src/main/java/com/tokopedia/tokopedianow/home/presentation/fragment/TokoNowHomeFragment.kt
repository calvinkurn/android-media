package com.tokopedia.tokopedianow.home.presentation.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.home_component.listener.MixLeftComponentListener
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.model.LinkerData.NOW_TYPE
import com.tokopedia.linker.model.LinkerData.WEBVIEW_TYPE
import com.tokopedia.localizationchooseaddress.domain.mapper.TokonowWarehouseMapper
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.model.LocalWarehouseModel
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.minicart.common.widget.MiniCartWidget
import com.tokopedia.minicart.common.widget.MiniCartWidgetListener
import com.tokopedia.play.widget.analytic.impression.DefaultImpressionValidator
import com.tokopedia.play.widget.analytic.impression.ImpressionHelper
import com.tokopedia.play.widget.ui.PlayWidgetView
import com.tokopedia.play.widget.ui.coordinator.PlayWidgetCoordinator
import com.tokopedia.play.widget.ui.listener.PlayWidgetListener
import com.tokopedia.play.widget.ui.listener.PlayWidgetRouterListener
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.helper.ViewHelper
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.searchbar.navigation_component.listener.NavRecyclerViewScrollListener
import com.tokopedia.searchbar.navigation_component.util.NavToolbarExt
import com.tokopedia.usercomponents.stickylogin.common.StickyLoginConstant
import com.tokopedia.usercomponents.stickylogin.view.StickyLoginAction
import com.tokopedia.usercomponents.stickylogin.view.StickyLoginView
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.SCREEN_NAME_TOKONOW_OOC
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics
import com.tokopedia.tokopedianow.common.bottomsheet.TokoNowOnBoard20mBottomSheet
import com.tokopedia.tokopedianow.common.constant.ConstantKey.AB_TEST_AUTO_TRANSITION_KEY
import com.tokopedia.tokopedianow.common.constant.ConstantKey.PARAM_APPLINK_AUTOCOMPLETE
import com.tokopedia.tokopedianow.common.constant.ConstantKey.REMOTE_CONFIG_KEY_FIRST_DURATION_TRANSITION_SEARCH
import com.tokopedia.tokopedianow.common.constant.ConstantKey.REMOTE_CONFIG_KEY_FIRST_INSTALL_SEARCH
import com.tokopedia.tokopedianow.common.constant.ConstantKey.SHARED_PREFERENCES_KEY_FIRST_INSTALL_SEARCH
import com.tokopedia.tokopedianow.common.constant.ConstantKey.SHARED_PREFERENCES_KEY_FIRST_INSTALL_TIME_SEARCH
import com.tokopedia.tokopedianow.common.constant.ConstantValue.ADDITIONAL_POSITION
import com.tokopedia.tokopedianow.common.constant.RequestCode.REQUEST_CODE_LOGIN
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType.Companion.MAIN_QUEST
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType.Companion.REPURCHASE_PRODUCT
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType.Companion.SHARING_EDUCATION
import com.tokopedia.tokopedianow.common.domain.model.SetUserPreference.SetUserPreferenceData
import com.tokopedia.tokopedianow.common.model.ShareTokonow
import com.tokopedia.tokopedianow.common.model.TokoNowCategoryGridUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardUiModel
import com.tokopedia.tokopedianow.common.util.CustomLinearLayoutManager
import com.tokopedia.tokopedianow.common.util.SharedPreferencesUtil
import com.tokopedia.tokopedianow.common.util.StringUtil.getOrDefaultZeroString
import com.tokopedia.tokopedianow.common.util.TokoMartHomeErrorLogger
import com.tokopedia.tokopedianow.common.util.TokoMartHomeErrorLogger.ATC_QUANTITY_ERROR
import com.tokopedia.tokopedianow.common.util.TokoMartHomeErrorLogger.ErrorType.ERROR_CHOOSE_ADDRESS
import com.tokopedia.tokopedianow.common.util.TokoMartHomeErrorLogger.ErrorType.ERROR_LAYOUT
import com.tokopedia.tokopedianow.common.util.TokoMartHomeErrorLogger.LOAD_LAYOUT_ERROR
import com.tokopedia.tokopedianow.common.util.TokoNowServiceTypeUtil.SWITCH_SERVICE_TYPE_TOASTER_RESOURCE_ID
import com.tokopedia.tokopedianow.common.util.TokoNowServiceTypeUtil.getServiceTypeRes
import com.tokopedia.tokopedianow.common.util.TokoNowUniversalShareUtil.shareOptionRequest
import com.tokopedia.tokopedianow.common.util.TokoNowUniversalShareUtil.shareRequest
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.common.viewholder.TokoNowCategoryGridViewHolder.TokoNowCategoryGridListener
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChooseAddressWidgetViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChooseAddressWidgetViewHolder.TokoNowChooseAddressWidgetListener
import com.tokopedia.tokopedianow.common.viewholder.TokoNowEmptyStateOocViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowProductCardViewHolder.TokoNowProductCardListener
import com.tokopedia.tokopedianow.common.viewholder.TokoNowServerErrorViewHolder.ServerErrorListener
import com.tokopedia.tokopedianow.databinding.FragmentTokopedianowHomeBinding
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.CATEGORY.EVENT_CATEGORY_HOME_PAGE
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.VALUE.HOMEPAGE_TOKONOW
import com.tokopedia.tokopedianow.home.analytic.HomePageLoadTimeMonitoring
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.EMPTY_STATE_FAILED_TO_FETCH_DATA
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.EMPTY_STATE_NO_ADDRESS_AND_LOCAL_CACHE
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.EMPTY_STATE_OUT_OF_COVERAGE
import com.tokopedia.tokopedianow.home.di.component.DaggerHomeComponent
import com.tokopedia.tokopedianow.home.domain.model.Data
import com.tokopedia.tokopedianow.home.domain.model.HomeRemoveAbleWidget
import com.tokopedia.tokopedianow.home.domain.model.SearchPlaceholder
import com.tokopedia.tokopedianow.home.presentation.activity.TokoNowHomeActivity
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcProductCardUiModel
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeAdapter
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeAdapterTypeFactory
import com.tokopedia.tokopedianow.home.presentation.adapter.differ.HomeListDiffer
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutListUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductRecomUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeSharingWidgetUiModel.HomeSharingReferralWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeSwitcherUiModel.Home2hSwitcher
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeSwitcherUiModel.Home20mSwitcher
import com.tokopedia.tokopedianow.home.presentation.view.coachmark.SwitcherCoachMark
import com.tokopedia.tokopedianow.home.presentation.view.listener.BannerComponentCallback
import com.tokopedia.tokopedianow.home.presentation.view.listener.DynamicLegoBannerCallback
import com.tokopedia.tokopedianow.home.presentation.view.listener.HomeSwitcherListener
import com.tokopedia.tokopedianow.home.presentation.view.listener.QuestWidgetCallback
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeEducationalInformationWidgetViewHolder.HomeEducationalInformationListener
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeProductRecomViewHolder.HomeProductRecomListener
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeQuestSequenceWidgetViewHolder.HomeQuestSequenceWidgetListener
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeSharingWidgetViewHolder.HomeSharingListener
import com.tokopedia.tokopedianow.home.presentation.view.listener.HomeLeftCarouselAtcCallback
import com.tokopedia.tokopedianow.home.presentation.view.listener.HomeLeftCarouselCallback
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeTickerViewHolder
import com.tokopedia.tokopedianow.home.presentation.viewmodel.TokoNowHomeViewModel
import com.tokopedia.tokopedianow.common.util.TokoNowSharedPreference
import com.tokopedia.tokopedianow.home.analytic.HomePlayWidgetAnalyticModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomePlayWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.view.listener.OnBoard20mBottomSheetCallback
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.Toaster.LENGTH_SHORT
import com.tokopedia.unifycomponents.Toaster.TYPE_ERROR
import com.tokopedia.unifycomponents.Toaster.TYPE_NORMAL
import com.tokopedia.universal_sharing.view.bottomsheet.ScreenshotDetector
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.PermissionListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ScreenShotListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.*
import javax.inject.Inject

class TokoNowHomeFragment: Fragment(),
        TokoNowView,
        TokoNowChooseAddressWidgetListener,
        HomeTickerViewHolder.HomeTickerListener,
        TokoNowCategoryGridListener,
        MiniCartWidgetListener,
        HomeProductRecomListener,
        TokoNowProductCardListener,
        ShareBottomsheetListener,
        ScreenShotListener,
        HomeSharingListener,
        HomeEducationalInformationListener,
        ServerErrorListener,
        PermissionListener,
        PlayWidgetListener,
        PlayWidgetRouterListener
{

    companion object {
        private const val AUTO_TRANSITION_VARIANT = "auto_transition"
        private const val DEFAULT_INTERVAL_HINT: Long = 10000L
        private const val FIRST_INSTALL_CACHE_VALUE: Long = 1800000L
        private const val REQUEST_CODE_LOGIN_STICKY_LOGIN = 130
        private const val REQUEST_CODE_PLAY_WIDGET = 100
        private const val ITEM_VIEW_CACHE_SIZE = 20

        private const val EXTRA_PLAY_CHANNEL_ID = "EXTRA_CHANNEL_ID"
        private const val EXTRA_PLAY_TOTAL_VIEW = "EXTRA_TOTAL_VIEW"

        const val CATEGORY_LEVEL_DEPTH = 1
        const val SOURCE = "tokonow"
        const val SOURCE_TRACKING = "tokonow page"
        const val DEFAULT_QUANTITY = 0
        const val SHARE_HOME_URL = "https://www.tokopedia.com/now"
        const val THUMBNAIL_AND_OG_IMAGE_SHARE_URL = "https://images.tokopedia.net/img/android/now/PN-RICH.jpg"
        const val REFERRAL_PAGE_URL = "https://www.tokopedia.com/seru/undang-untung/"
        const val PAGE_SHARE_NAME = "Tokonow"
        const val SHARE = "share"
        const val PAGE_TYPE_HOME = "home"
        const val SUCCESS_CODE = "200"
        const val KEY_IS_OPEN_MINICART_LIST = "isMiniCartOpen"
        const val KEY_SERVICE_TYPE = "service_type"

        fun newInstance() = TokoNowHomeFragment()
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelTokoNow: TokoNowHomeViewModel

    @Inject
    lateinit var analytics: HomeAnalytics

    @Inject
    lateinit var homeSharedPref: TokoNowSharedPreference

    @Inject
    lateinit var playWidgetImpressionValidator: DefaultImpressionValidator

    private var binding by autoClearedNullable<FragmentTokopedianowHomeBinding>()

    private val adapter by lazy {
        HomeAdapter(
            typeFactory = HomeAdapterTypeFactory(
                tokoNowView = this,
                homeTickerListener = this,
                tokoNowChooseAddressWidgetListener = this,
                tokoNowCategoryGridListener = this,
                bannerComponentListener = createSlideBannerCallback(),
                homeProductRecomListener = this,
                tokoNowProductCardListener = this,
                homeSharingEducationListener = this,
                homeEducationalInformationListener = this,
                serverErrorListener = this,
                tokoNowEmptyStateOocListener = createTokoNowEmptyStateOocListener(),
                homeQuestSequenceWidgetListener = createQuestWidgetCallback(),
                dynamicLegoBannerCallback = createLegoBannerCallback(),
                homeSwitcherListener = createHomeSwitcherListener(),
                homeLeftCarouselAtcListener = createLeftCarouselAtcCallback(),
                homeLeftCarouselListener = createLeftCarouselCallback(),
                playWidgetCoordinator = createPlayWidgetCoordinator()
            ),
            differ = HomeListDiffer()
        )
    }

    private var navToolbar: NavToolbar? = null
    private var statusBarBackground: View? = null
    private var localCacheModel: LocalCacheModel? = null
    private var ivHeaderBackground: ImageView? = null
    private var swipeLayout: SwipeRefreshLayout? = null
    private var sharedPrefs: SharedPreferences? = null
    private var rvHome: RecyclerView? = null
    private var miniCartWidget: MiniCartWidget? = null
    private var stickyLoginTokonow: StickyLoginView? = null
    private var rvLayoutManager: CustomLinearLayoutManager? = null
    private var isShowFirstInstallSearch = false
    private var durationAutoTransition = DEFAULT_INTERVAL_HINT
    private var movingPosition = 0
    private var isOpenMiniCartList = false
    private var externalServiceType = ""
    private var shareHomeTokonow: ShareTokonow? = null
    private var universalShareBottomSheet: UniversalShareBottomSheet? = null
    private var screenshotDetector : ScreenshotDetector? = null
    private var carouselScrollState = mutableMapOf<Int, Parcelable?>()
    private var carouselParallaxState = mutableMapOf<String, Float>()
    private var hasEducationalInformationAppeared = false
    private var pageLoadTimeMonitoring: HomePageLoadTimeMonitoring? = null
    private var switcherCoachMark: SwitcherCoachMark? = null
    private var playWidgetCoordinator: PlayWidgetCoordinator? = null
    private var bannerComponentCallback: BannerComponentCallback? = null

    private val homeMainToolbarHeight: Int
        get() {
            val defaultHeight = context?.resources?.getDimensionPixelSize(
                R.dimen.tokopedianow_default_toolbar_status_height).orZero()
            val height = (navToolbar?.height ?: defaultHeight)
            val padding = context?.resources?.getDimensionPixelSize(
                com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3).orZero()

            return height + padding
        }
    private val spaceZero: Int
        get() = context?.resources?.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_0).orZero().toIntSafely()

    private val loadMoreListener by lazy { createLoadMoreListener() }
    private val navBarScrollListener by lazy { createNavBarScrollListener() }
    private val homeComponentScrollListener by lazy { createHomeComponentScrollListener() }

    override fun onCreate(savedInstanceState: Bundle?) {
        initPerformanceMonitoring()
        super.onCreate(savedInstanceState)
        setUriData()
        val firebaseRemoteConfig = FirebaseRemoteConfigImpl(activity)
        shareHomeTokonow = createShareHomeTokonow()
        firebaseRemoteConfig.let {
            isShowFirstInstallSearch = it.getBoolean(REMOTE_CONFIG_KEY_FIRST_INSTALL_SEARCH, false)
            durationAutoTransition = it.getLong(REMOTE_CONFIG_KEY_FIRST_DURATION_TRANSITION_SEARCH, DEFAULT_INTERVAL_HINT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTokopedianowHomeBinding.inflate(inflater, container, false)
        return binding?.root as View
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setupNavToolbar()
        stickyLoginSetup()
        setupStatusBar()
        setupRecyclerView()
        setupSwipeRefreshLayout()
        observeLiveData()
        updateCurrentPageLocalCacheModelData()
        switchServiceOrLoadLayout()
        initScreenShotDetector()
    }

    override fun getFragmentPage(): Fragment = this

    override fun getFragmentManagerPage(): FragmentManager = childFragmentManager

    override fun refreshLayoutPage() = onRefreshLayout()

    override fun onClickRetryButton() = onRefreshLayout()

    override fun onAttach(context: Context) {
        initInjector()
        super.onAttach(context)
    }

    override fun onResume() {
        super.onResume()
        if (isChooseAddressWidgetDataUpdated()) {
            onRefreshLayout()
        } else {
            getMiniCart()
        }
        screenshotDetector?.start()
    }

    override fun onStop() {
        UniversalShareBottomSheet.clearState(screenshotDetector)
        super.onStop()
    }

    override fun onDestroy() {
        UniversalShareBottomSheet.clearState(screenshotDetector)
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != Activity.RESULT_OK) return

        when (requestCode) {
            REQUEST_CODE_LOGIN_STICKY_LOGIN -> {
                stickyLoginLoadContent()
                onRefreshLayout()
            }
            REQUEST_CODE_LOGIN -> {
                onRefreshLayout()
            }
            REQUEST_CODE_PLAY_WIDGET -> {
                onUpdatePlayWidget(data)
            }
        }
    }

    override fun onTickerDismissed(id: String) {
        viewModelTokoNow.removeTickerWidget(id)
    }

    override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {
        if (!miniCartSimplifiedData.isShowMiniCartWidget) {
            miniCartWidget?.hide()
            miniCartWidget?.hideCoachMark()
        }
        viewModelTokoNow.setProductAddToCartQuantity(miniCartSimplifiedData)
        setupPadding(miniCartSimplifiedData.isShowMiniCartWidget)
    }

    override fun screenShotTaken() {
        updateShareHomeData(
            pageIdConstituents = listOf(PAGE_TYPE_HOME),
            isScreenShot = true,
            thumbNailTitle = context?.resources?.getString(R.string.tokopedianow_home_share_thumbnail_title_ss).orEmpty(),
            linkerType = NOW_TYPE
        )

        showUniversalShareBottomSheet(shareHomeTokonow)
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        screenshotDetector?.onRequestPermissionsResult(requestCode, grantResults, this)
    }

    override fun onChooseAddressWidgetRemoved() {
        if(rvHome?.isComputingLayout == false) {
            adapter.removeHomeChooseAddressWidget()
        }
    }

    override fun onClickChooseAddressWidgetTracker() { }

    override fun onCategoryRetried() {
        adapter.getItem(TokoNowCategoryGridUiModel::class.java)?.let {
            viewModelTokoNow.getCategoryGrid(it, localCacheModel?.warehouse_id.orEmpty())
        }
    }

    override fun onAllCategoryClicked() {
        analytics.onClickAllCategory()
    }

    override fun onCategoryClicked(position: Int, categoryId: String, headerName: String) {
        analytics.onClickCategory(position, categoryId, headerName)
    }

    override fun onCategoryImpression(data: TokoNowCategoryGridUiModel) {
        val warehouseId = localCacheModel?.warehouse_id.orEmpty()
        analytics.trackCategoryImpression(data, warehouseId)
    }

    override fun onRecomProductCardClicked(
        recomItem: RecommendationItem,
        channelId: String,
        headerName: String,
        position: String,
        isOoc: Boolean,
        applink: String
    ) {
        RouteManager.route(context, applink)

        analytics.onClickProductRecom(
            channelId = channelId,
            headerName = headerName,
            recommendationItem = recomItem,
            position = position,
            isOoc = isOoc
        )
    }

    override fun onRecomProductCardImpressed(
        recomItem: RecommendationItem,
        channelId: String,
        headerName: String,
        pageName: String,
        isOoc: Boolean
    ) {
        analytics.onImpressProductRecom(
            channelId = channelId,
            headerName = headerName,
            recomItem = recomItem,
            isOoc = isOoc
        )
    }

    override fun onSeeAllBannerClicked(channelId: String, headerName: String, isOoc: Boolean, applink: String) {
        RouteManager.route(context, applink)
        analytics.onClickAllProductRecom(
            channelId = channelId,
            headerName = headerName,
            isOoc = isOoc
        )
    }

    override fun onProductRecomNonVariantClick(
        recomItem: RecommendationItem,
        quantity: Int,
        headerName: String,
        channelId: String,
        position: String
    ) {
        if (userSession.isLoggedIn) {
            viewModelTokoNow.addProductToCart(
                recomItem.productId.toString(),
                quantity,
                recomItem.shopId.toString(),
                TokoNowLayoutType.PRODUCT_RECOM
            )
        } else {
            RouteManager.route(context, ApplinkConst.LOGIN)
        }
    }

    override fun saveScrollState(adapterPosition: Int, scrollState: Parcelable?) {
        carouselScrollState[adapterPosition] = scrollState
    }

    override fun saveParallaxState(mapParallaxState: Map<String, Float>) {
        carouselParallaxState = mapParallaxState.toMutableMap()
    }

    override fun getScrollState(adapterPosition: Int): Parcelable? {
        return carouselScrollState[adapterPosition]
    }

    override fun getParallaxState(): Map<String, Float> {
        return carouselParallaxState
    }

    override fun onProductQuantityChanged(data: TokoNowProductCardUiModel, quantity: Int) {
        if (userSession.isLoggedIn) {
            viewModelTokoNow.addProductToCart(
                data.productId,
                quantity,
                data.shopId,
                data.type
            )
        } else {
            RouteManager.route(context, ApplinkConst.LOGIN)
        }
    }

    override fun onProductCardImpressed(position: Int, data: TokoNowProductCardUiModel) {
        when(data.type) {
            REPURCHASE_PRODUCT -> trackRepurchaseImpression(position, data)
        }
    }

    override fun onProductCardClicked(position: Int, data: TokoNowProductCardUiModel) {
        when(data.type) {
            REPURCHASE_PRODUCT -> trackRepurchaseClick(position, data)
        }
    }

    override fun onAddVariantClicked(data: TokoNowProductCardUiModel) {
        AtcVariantHelper.goToAtcVariant(
            context = requireContext(),
            productId = data.productId,
            pageSource = VariantPageSource.TOKONOW_PAGESOURCE,
            isTokoNow = true,
            shopId = data.shopId,
            startActivitResult = this::startActivityForResult
        )
    }

    override fun onMoreReferralClicked(referral: HomeSharingReferralWidgetUiModel, linkUrl: String) {
        openWebView(linkUrl)

        analytics.sendClickMoreSenderReferralWidget(
            slug = referral.slug,
            referralCode = "",
            userStatus = referral.userStatus,
            campaignCode = referral.campaignCode,
            warehouseId = referral.warehouseId,
        )
    }

    override fun onShareBtnReferralSenderClicked(referral: HomeSharingReferralWidgetUiModel) {
        setupReferralData(referral)
        showUniversalShareBottomSheet(shareHomeTokonow)
        trackClickShareSenderReferralWidget(referral)
    }

    override fun onShareBtnReferralReceiverClicked(referral: HomeSharingReferralWidgetUiModel) {
        openWebView(REFERRAL_PAGE_URL + referral.slug)

        analytics.sendClickCheckDetailReceiverReferralWidget(
            slug = referral.slug,
            referralCode = "",
            userStatus = referral.userStatus,
            campaignCode = referral.campaignCode,
            warehouseId = referral.warehouseId,
        )
    }

    override fun onShareReferralSenderWidgetImpressed(referral: HomeSharingReferralWidgetUiModel) {
        analytics.sendImpressSenderReferralWidget(
            slug = referral.slug,
            referralCode = "",
            userStatus = referral.userStatus,
            campaignCode = referral.campaignCode,
            warehouseId = referral.warehouseId,
        )
    }

    override fun onShareReferralReceiverWidgetImpressed(referral: HomeSharingReferralWidgetUiModel) {
        analytics.sendImpressReceiverReferralWidget(
            slug = referral.slug,
            referralCode = "",
            userStatus = referral.userStatus,
            campaignCode = referral.campaignCode,
            warehouseId = referral.warehouseId,
        )
    }

    override fun onShareBtnSharingEducationalInfoClicked() {
        updateShareHomeData(
            pageIdConstituents = listOf(PAGE_TYPE_HOME),
            isScreenShot = false,
            thumbNailTitle = context?.resources?.getString(R.string.tokopedianow_home_share_thumbnail_title).orEmpty(),
            linkerType = NOW_TYPE
        )

        shareClicked(shareHomeTokonow)
        analytics.trackClickShareButtonWidget()
    }

    override fun onCloseBtnSharingEducationalInfoClicked(id: String) {
        SharedPreferencesUtil.setSharingEducationState(activity)
        viewModelTokoNow.removeWidget(id)
    }

    override fun isEducationInformationLottieStopped(): Boolean = SharedPreferencesUtil.isEducationalInformationStopped(activity)

    override fun onEducationInformationLottieClicked() {
        SharedPreferencesUtil.setEducationalInformationState(activity)
    }

    override fun onEducationInformationWidgetImpressed() {
        hasEducationalInformationAppeared = true
        analytics.trackImpressionUSPWidget()
    }

    override fun onEducationInformationDropDownClicked() {
        analytics.trackClickUSPWidget()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (!SharedPreferencesUtil.isEducationalInformationStopped(activity) && hasEducationalInformationAppeared) {
            SharedPreferencesUtil.setEducationalInformationState(activity)
        }
        playWidgetCoordinator?.onDestroy()
    }

    override fun onWidgetShouldRefresh(view: PlayWidgetView) {
        adapter.data.filterIsInstance<HomePlayWidgetUiModel>().forEach {
            viewModelTokoNow.autoRefreshPlayWidget(it)
        }
    }

    override fun onWidgetOpenAppLink(view: View, appLink: String) {
        val intent = RouteManager.getIntent(requireContext(), appLink)
        startActivityForResult(intent, REQUEST_CODE_PLAY_WIDGET)
    }

    private fun initInjector() {
        DaggerHomeComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun initScreenShotDetector() {
        context?.let {
            screenshotDetector = UniversalShareBottomSheet.createAndStartScreenShotDetector(
                context = it,
                screenShotListener = this,
                fragment = this,
                addFragmentLifecycleObserver = true,
                permissionListener = this
            )
        }
    }

    private fun switchServiceOrLoadLayout() {
        localCacheModel?.apply {
            viewModelTokoNow.switchServiceOrLoadLayout(
                externalServiceType = externalServiceType,
                localCacheModel = this
            )
        }
    }

    private fun initPerformanceMonitoring() {
        pageLoadTimeMonitoring = (activity as? TokoNowHomeActivity)?.pageLoadTimeMonitoring
        pageLoadTimeMonitoring?.startNetworkPerformanceMonitoring()
    }

    private fun startRenderPerformanceMonitoring() {
        pageLoadTimeMonitoring?.startRenderPerformanceMonitoring()
    }

    private fun stopRenderPerformanceMonitoring() {
        rvHome?.addOneTimeGlobalLayoutListener {
            pageLoadTimeMonitoring?.stopRenderPerformanceMonitoring()
        }
    }

    private fun stopPerformanceMonitoring() {
        pageLoadTimeMonitoring?.stopPerformanceMonitoring()
    }

    private fun checkStateNotInServiceArea(shopId: Long = -1L, warehouseId: Long) {
        context?.let {
            when {
                shopId == 0L -> {
                    viewModelTokoNow.getChooseAddress(SOURCE)
                }
                warehouseId == 0L -> {
                    showEmptyStateNoAddress()
                }
                else -> {
                    showLayout()
                    viewModelTokoNow.trackOpeningScreen(HOMEPAGE_TOKONOW)
                }
            }
        }
    }

    private fun showEmptyState(@HomeStaticLayoutId id: String) {
        localCacheModel?.service_type?.let { serviceType ->
            if (id != EMPTY_STATE_OUT_OF_COVERAGE) {
                rvLayoutManager?.setScrollEnabled(false)
                viewModelTokoNow.getEmptyState(id, serviceType)
            } else {
                viewModelTokoNow.getEmptyState(id, serviceType)
                viewModelTokoNow.getProductRecomOoc()
            }

            miniCartWidget?.hide()
            miniCartWidget?.hideCoachMark()
            setToolbarTypeTitle()
            setupPadding(false)
        }
    }

    private fun setToolbarTypeTitle() {
        navToolbar?.setToolbarContentType(
            NavToolbar.Companion.ContentType.TOOLBAR_TYPE_TITLE
        )
    }

    private fun showFailedToFetchData() {
        showEmptyState(EMPTY_STATE_FAILED_TO_FETCH_DATA)
    }

    private fun logHomeLayoutError(throwable: Throwable) {
        TokoMartHomeErrorLogger.logExceptionToScalyr(
            throwable = throwable,
            errorType = ERROR_LAYOUT,
            deviceId = userSession.deviceId,
            description = LOAD_LAYOUT_ERROR
        )
    }

    private fun logATCQuantityError(throwable: Throwable) {
        TokoMartHomeErrorLogger.logExceptionToScalyr(
            throwable = throwable,
            errorType = ERROR_LAYOUT,
            deviceId = userSession.deviceId,
            description = ATC_QUANTITY_ERROR
        )
    }

    private fun logChooseAddressError(throwable: Throwable) {
        TokoMartHomeErrorLogger.logExceptionToScalyr(
            throwable = throwable,
            errorType = ERROR_CHOOSE_ADDRESS,
            deviceId = userSession.deviceId,
            description = ERROR_CHOOSE_ADDRESS
        )
    }

    private fun showEmptyStateNoAddress() {
        if (localCacheModel?.city_id?.isBlank() == true && localCacheModel?.district_id?.isBlank() == true) {
            showEmptyState(EMPTY_STATE_NO_ADDRESS_AND_LOCAL_CACHE)
        } else {
            viewModelTokoNow.trackOpeningScreen(SCREEN_NAME_TOKONOW_OOC + HOMEPAGE_TOKONOW)
            showEmptyState(EMPTY_STATE_OUT_OF_COVERAGE)
        }
    }

    private fun showLayout() {
        getHomeLayout()
        navToolbar?.setToolbarContentType(NavToolbar.Companion.ContentType.TOOLBAR_TYPE_SEARCH)
    }

    private fun stickyLoginSetup(){
        stickyLoginTokonow?.let {
            it.page = StickyLoginConstant.Page.TOKONOW
            it.lifecycleOwner = viewLifecycleOwner
            it.setStickyAction(object : StickyLoginAction {
                override fun onClick() {
                    context?.let { context ->
                        val intent = RouteManager.getIntent(context, ApplinkConst.LOGIN)
                        startActivityForResult(intent, REQUEST_CODE_LOGIN_STICKY_LOGIN)
                    }
                }

                override fun onDismiss() {
                }

                override fun onViewChange(isShowing: Boolean) {
                }
            })
        }
        hideStickyLogin()
    }

    private fun stickyLoginLoadContent(){
        stickyLoginTokonow?.loadContent()
    }

    private fun hideStickyLogin(){
        stickyLoginTokonow?.hide()
    }

    private fun loadHeaderBackground() {
        ivHeaderBackground?.show()
        ivHeaderBackground?.setImageResource(R.drawable.tokopedianow_ic_header_background_shimmering)
    }

    private fun showHeaderBackground() {
        context?.resources?.apply {
            val background = VectorDrawableCompat.create(
                this,
                R.drawable.tokopedianow_ic_header_background,
                context?.theme
            )
            ivHeaderBackground?.setImageDrawable(background)
            ivHeaderBackground?.show()
        }
    }

    private fun hideHeaderBackground() {
        ivHeaderBackground?.hide()
    }

    private fun setupSwipeRefreshLayout() {
        context?.let {
            swipeLayout?.setMargin(spaceZero, NavToolbarExt.getFullToolbarHeight(it), spaceZero, spaceZero)
            swipeLayout?.setOnRefreshListener {
                onRefreshLayout()
            }
        }
    }

    private fun onRefreshLayout() {
        refreshMiniCart()
        resetMovingPosition()
        removeAllScrollListener()
        hideStickyLogin()
        rvLayoutManager?.setScrollEnabled(true)
        carouselScrollState.clear()
        carouselParallaxState.clear()
        bannerComponentCallback?.resetImpression()
        loadLayout()
    }

    private fun refreshMiniCart() {
        checkIfChooseAddressWidgetDataUpdated()
        getMiniCart()
    }

    private fun onUpdatePlayWidget(data: Intent?) {
        val channelId = data?.getStringExtra(EXTRA_PLAY_CHANNEL_ID).orEmpty()
        val totalView = data?.getStringExtra(EXTRA_PLAY_TOTAL_VIEW).orEmpty()
        viewModelTokoNow.updatePlayWidget(channelId, totalView)
    }

    private fun setupUi() {
        view?.apply {
            ivHeaderBackground = binding?.viewBackgroundImage
            navToolbar = binding?.navToolbar
            statusBarBackground = binding?.statusBarBg
            rvHome = binding?.rvHome
            swipeLayout = binding?.swipeRefreshLayout
            miniCartWidget = binding?.miniCartWidget
            stickyLoginTokonow = binding?.stickyLoginTokonow
        }
    }

    private fun setupNavToolbar() {
        setupTopNavigation()
        navAbTestCondition (
                ifNavRevamp = {
                    setIconNewTopNavigation()
                },
                ifNavOld = {
                    setIconOldTopNavigation()
                }
        )
    }

    private fun setupTopNavigation() {
        navToolbar?.let { toolbar ->
            viewLifecycleOwner.lifecycle.addObserver(toolbar)
            //  because searchHint has not been discussed so for current situation we only use hardcoded placeholder
            setHint(SearchPlaceholder(Data(null, context?.resources?.getString(R.string.tokopedianow_search_bar_hint).orEmpty(),"")))
            addNavBarScrollListener()
            activity?.let {
                toolbar.setupToolbarWithStatusBar(it)
                toolbar.setToolbarTitle(getString(R.string.tokopedianow_home_title))
            }
        }
    }

    private fun setIconNewTopNavigation() {
        val icons = IconBuilder(IconBuilderFlag(pageSource = ApplinkConsInternalNavigation.SOURCE_HOME))
                .addIcon(IconList.ID_SHARE, onClick = ::onClickShareButton, disableDefaultGtmTracker = true)
                .addIcon(IconList.ID_CART, onClick = ::onClickCartButton)
                .addIcon(IconList.ID_NAV_GLOBAL) {}
        navToolbar?.setIcon(icons)
    }

    private fun setIconOldTopNavigation() {
        val icons = IconBuilder(IconBuilderFlag(pageSource = ApplinkConsInternalNavigation.SOURCE_HOME))
                .addIcon(IconList.ID_SHARE, onClick = ::onClickShareButton, disableDefaultGtmTracker = true)
                .addIcon(IconList.ID_CART, onClick = ::onClickCartButton)
        navToolbar?.setIcon(icons)
    }

    private fun onClickCartButton() {
        analytics.onClickCartButton()
    }

    private fun onClickShareButton() {
        updateShareHomeData(
            pageIdConstituents = listOf(PAGE_TYPE_HOME),
            isScreenShot = false,
            thumbNailTitle = context?.resources?.getString(R.string.tokopedianow_home_share_thumbnail_title).orEmpty(),
            linkerType = NOW_TYPE
        )

        shareClicked(shareHomeTokonow)
        analytics.trackClickShareButtonTopNav()
    }

    private fun updateShareHomeData(pageIdConstituents: List<String>, isScreenShot: Boolean, thumbNailTitle: String, linkerType: String, id: String = "", url: String = SHARE_HOME_URL) {
        shareHomeTokonow?.pageIdConstituents = pageIdConstituents
        shareHomeTokonow?.isScreenShot = isScreenShot
        shareHomeTokonow?.thumbNailTitle = thumbNailTitle
        shareHomeTokonow?.linkerType = linkerType
        shareHomeTokonow?.id = id
        shareHomeTokonow?.sharingUrl = url
    }

    private fun evaluateHomeComponentOnScroll(recyclerView: RecyclerView, dy: Int) {
        movingPosition += dy
        ivHeaderBackground?.y = if(movingPosition >= 0) {
            -(movingPosition.toFloat())
        } else {
            resetMovingPosition()
            movingPosition.toFloat()
        }
        if (recyclerView.canScrollVertically(1) || movingPosition != 0) {
            navToolbar?.showShadow(lineShadow = true)
        } else {
            navToolbar?.hideShadow(lineShadow = true)
        }
    }

    private fun isNavOld(): Boolean = false

    private fun getAbTestPlatform(): AbTestPlatform {
        val remoteConfigInstance = RemoteConfigInstance(activity?.application)
        return remoteConfigInstance.abTestPlatform
    }

    private fun navAbTestCondition(ifNavRevamp: () -> Unit = {}, ifNavOld: () -> Unit = {}) {
        if (!isNavOld()) {
            ifNavRevamp.invoke()
        } else {
            ifNavOld.invoke()
        }
    }

    private fun setupStatusBar() {
        /*
            this status bar background only shows for android Kitkat below
            In that version, status bar can't be forced to dark mode
            We must set background to keep status bar icon visible
        */
        activity?.let {
            statusBarBackground?.apply {
                layoutParams?.height = ViewHelper.getStatusBarHeight(activity)
                visibility = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) View.INVISIBLE else View.VISIBLE
            }
            setStatusBarAlpha()
        }
    }

    private fun setStatusBarAlpha() {
        val drawable = statusBarBackground?.background
        drawable?.alpha = 0
        statusBarBackground?.background = drawable
    }

    private fun setupRecyclerView() {
        context?.let {
            rvHome?.apply {
                adapter = this@TokoNowHomeFragment.adapter
                rvLayoutManager = CustomLinearLayoutManager(it)
                layoutManager = rvLayoutManager
            }

            rvHome?.setItemViewCacheSize(ITEM_VIEW_CACHE_SIZE)
            addHomeComponentScrollListener()
        }
    }

    private fun observeLiveData() {
        observe(viewModelTokoNow.homeLayoutList) {
            removeAllScrollListener()

            when(it) {
                is Success -> onSuccessGetHomeLayout(it.data)
                is Fail -> onFailedGetHomeLayout(it.throwable)
            }

            rvHome?.post {
                addScrollListener()
                resetSwipeLayout()
            }
        }

        observe(viewModelTokoNow.atcQuantity) {
            when(it) {
                is Success -> showHomeLayout(it.data)
                is Fail -> logATCQuantityError(it.throwable)
            }
        }

        observe(viewModelTokoNow.miniCart) {
            if(it is Success) {
                setupMiniCart(it.data)
                setupPadding(it.data.isShowMiniCartWidget)
            }
        }

        observe(viewModelTokoNow.chooseAddress) {
            when(it) {
                is Success -> {
                    setupChooseAddress(it.data)
                }
                is Fail -> {
                    showEmptyStateNoAddress()
                    logChooseAddressError(it.throwable)
                }
            }
        }

        observe(viewModelTokoNow.miniCartAdd) {
            when(it) {
                is Success -> {
                    getMiniCart()
                    showToaster(
                        message = it.data.errorMessage.joinToString(separator = ", "),
                        actionText = getString(R.string.tokopedianow_lihat),
                        onClickActionBtn = {
                            showMiniCartBottomSheet()
                        },
                        type = TYPE_NORMAL
                    )
                }
                is Fail -> {
                    showToaster(
                        message = it.throwable.message.orEmpty(),
                        type = TYPE_ERROR
                    )
                }
            }
        }

        observe(viewModelTokoNow.miniCartUpdate) {
            when(it) {
                is Success -> {
                    val shopIds = listOf(localCacheModel?.shop_id.orEmpty())
                    miniCartWidget?.updateData(shopIds)
                }
                is Fail -> {
                    showToaster(
                        message = it.throwable.message.orEmpty(),
                        type = TYPE_ERROR
                    )
                }
            }
        }

        observe(viewModelTokoNow.miniCartRemove) {
            when(it) {
                is Success -> {
                    getMiniCart()
                    showToaster(
                        message = it.data.second,
                        type = TYPE_NORMAL
                    )
                }
                is Fail -> {
                    val message = it.throwable.message.orEmpty()
                    showToaster(message = message, type = TYPE_ERROR)
                }
            }
        }

        observe(viewModelTokoNow.homeAddToCartTracker) {
            when(it.data) {
                is TokoNowProductCardUiModel -> trackRepurchaseAddToCart(
                    it.quantity,
                    it.data
                )
                is HomeProductRecomUiModel -> trackProductRecomAddToCart(
                    it.quantity,
                    it.position,
                    it.cartId,
                    it.data
                )
                is HomeLeftCarouselAtcProductCardUiModel -> trackLeftCarouselAddToCart(
                    it.quantity,
                    it.cartId,
                    it.data
                )
            }
        }

        observe(viewModelTokoNow.homeRemoveFromCartTracker) {
            when(it.data) {
                is HomeProductRecomUiModel -> trackProductRecomRemoveFromCart(
                    it.quantity,
                    it.position,
                    it.data
                )
            }
        }

        observe(viewModelTokoNow.openScreenTracker) { screenName ->
            TokoNowCommonAnalytics.onOpenScreen(
                isLoggedInStatus = userSession.isLoggedIn,
                screenName = screenName
            )
        }

        observe(viewModelTokoNow.setUserPreference) {
            when(it) {
                is Success -> onSuccessSetUserPreference(it.data)
                is Fail -> showFailedToFetchData()
            }
        }

        observe(viewModelTokoNow.getReferralResult) {
            if(it is Fail) {
                showToaster(
                    message = getString(R.string.tokopedianow_home_referral_toaster),
                    type = TYPE_ERROR
                )
            }
        }

        observe(viewModelTokoNow.homeSwitchServiceTracker) {
            if (it.isImpressionTracker) {
                analytics.sendImpressSwitcherWidget(
                    userId = it.userId,
                    whIdOrigin = it.whIdOrigin,
                    whIdDestination = it.whIdDestination,
                    isNow15 = it.isNow15
                )
            } else {
                analytics.sendClickSwitcherWidget(
                    userId = it.userId,
                    whIdOrigin = it.whIdOrigin,
                    whIdDestination = it.whIdDestination,
                    isNow15 = it.isNow15
                )
            }
        }

        observe(viewModelTokoNow.invalidatePlayImpression) { invalidate ->
            if(invalidate) {
                playWidgetImpressionValidator.invalidate()
            }
        }
    }

    private fun setupChooseAddress(data: GetStateChosenAddressResponse) {
        data.let { chooseAddressData ->
            ChooseAddressUtils.updateLocalizingAddressDataFromOther(
                context = requireContext(),
                addressId = chooseAddressData.data.addressId.toString(),
                cityId = chooseAddressData.data.cityId.toString(),
                districtId = chooseAddressData.data.districtId.toString(),
                lat = chooseAddressData.data.latitude,
                long = chooseAddressData.data.longitude,
                label = String.format(
                    "%s %s",
                    chooseAddressData.data.addressName,
                    chooseAddressData.data.receiverName
                ),
                postalCode = chooseAddressData.data.postalCode,
                warehouseId = chooseAddressData.tokonow.warehouseId.toString(),
                shopId = chooseAddressData.tokonow.shopId.toString(),
                warehouses = TokonowWarehouseMapper.mapWarehousesResponseToLocal(chooseAddressData.tokonow.warehouses),
                serviceType = chooseAddressData.tokonow.serviceType,
                lastUpdate = chooseAddressData.tokonow.tokonowLastUpdate
            )
        }
        checkIfChooseAddressWidgetDataUpdated()
        checkStateNotInServiceArea(
            warehouseId = data.tokonow.warehouseId
        )
    }

    private fun trackProductRecomAddToCart(quantity: Int, position: Int, cartId: String, productRecomModel: HomeProductRecomUiModel) {
        analytics.onClickProductRecomAddToCart(
            channelId = productRecomModel.id,
            headerName = productRecomModel.recomWidget.title,
            quantity = quantity.toString(),
            recommendationItem = productRecomModel.recomWidget.recommendationItemList[position - ADDITIONAL_POSITION],
            position = position.toString(),
            cartId = cartId
        )
    }

    private fun trackProductRecomRemoveFromCart(quantity: Int, position: Int, productRecomModel: HomeProductRecomUiModel) {
        analytics.onClickProductRecomRemoveFromCart(
            channelId = productRecomModel.id,
            headerName = productRecomModel.recomWidget.title,
            quantity = quantity.toString(),
            recommendationItem = productRecomModel.recomWidget.recommendationItemList[position - ADDITIONAL_POSITION],
            position = position.toString()
        )
    }

    private fun trackLeftCarouselAddToCart(quantity: Int, cartId: String, product: HomeLeftCarouselAtcProductCardUiModel) {
        analytics.onClickLeftCarouselAddToCart(
            quantity = quantity.toString(),
            uiModel = product,
            cartId = cartId,
        )
    }

    private fun onSuccessSetUserPreference(data: SetUserPreferenceData) {
        val warehouses = data.warehouses.map {
            LocalWarehouseModel(
                it.warehouseId.toLongOrZero(),
                it.serviceType
            )
        }

        ChooseAddressUtils.updateTokoNowData(
            requireContext(),
            data.warehouseId,
            data.shopId,
            warehouses,
            data.serviceType
        )

        onRefreshLayout()

        localCacheModel?.apply {

            val has2hCoachMarkBeenShown = homeSharedPref.get2hCoachMarkOnBoardShown()
            val has20mCoachMarkBeenShown = homeSharedPref.get20mCoachMarkOnBoardShown()

            val needToShowOnBoardToaster = viewModelTokoNow.needToShowOnBoardToaster(
                serviceType = service_type,
                has20mCoachMarkBeenShown = has20mCoachMarkBeenShown,
                has2hCoachMarkBeenShown = has2hCoachMarkBeenShown,
                isWarehouseIdZero = warehouse_id.toLongOrZero().isZero()
            )

            if (needToShowOnBoardToaster) {
                showSwitcherToaster(service_type)
            }

        }
    }

    private fun showSwitcherToaster(serviceType: String) {
        getServiceTypeRes(
            key = SWITCH_SERVICE_TYPE_TOASTER_RESOURCE_ID,
            serviceType = serviceType
        )?.let {
            showToaster(
                message = getString(it),
                type = TYPE_NORMAL
            )
        }
    }

    private fun setupReferralData(referral: HomeSharingReferralWidgetUiModel) {
        val url = REFERRAL_PAGE_URL + referral.sharingUrlParam

        updateShareHomeData(
            pageIdConstituents = listOf(PAGE_TYPE_HOME),
            isScreenShot = false,
            thumbNailTitle = context?.resources?.getString(R.string.tokopedianow_home_share_thumbnail_title).orEmpty(),
            linkerType = WEBVIEW_TYPE,
            id = "url=$url",
            url = url
        )

        shareHomeTokonow?.apply {
            sharingText = referral.textDescription
            specificPageName = referral.ogTitle
            specificPageDescription = referral.ogDescription
            ogImageUrl = referral.ogImage
            thumbNailImage = referral.ogImage
        }
    }

    private fun trackRepurchaseImpression(position: Int, data: TokoNowProductCardUiModel) {
        analytics.onImpressRepurchase(position, data)
    }

    private fun trackRepurchaseClick(position: Int, data: TokoNowProductCardUiModel) {
        analytics.onClickRepurchase(position, data)
    }

    private fun trackRepurchaseAddToCart(quantity: Int, data: TokoNowProductCardUiModel) {
        analytics.onRepurchaseAddToCart(quantity, data)
    }

    private fun trackClickShareSenderReferralWidget(referral: HomeSharingReferralWidgetUiModel) {
        analytics.sendClickShareSenderReferralWidget(
            slug = referral.slug,
            userStatus = referral.userStatus,
            campaignCode = referral.campaignCode,
            warehouseId = referral.warehouseId,
            referralCode = referral.sharingUrlParam.removePrefix("${referral.slug}/")
        )
    }

    private fun showToaster(
        message: String,
        duration: Int = LENGTH_SHORT,
        type: Int,
        actionText: String = "",
        onClickActionBtn: View.OnClickListener = View.OnClickListener {  }
    ) {
        view?.let { view ->
            if (message.isNotBlank()) {
                Toaster.toasterCustomBottomHeight = getMiniCartHeight()
                val toaster = Toaster.build(
                    view = view,
                    text = message,
                    duration = duration,
                    type = type,
                    actionText = actionText,
                    clickListener = onClickActionBtn
                )
                toaster.show()
            }
        }
    }

    private fun resetSwipeLayout() {
        swipeLayout?.isEnabled = true
        swipeLayout?.isRefreshing = false
    }

    private fun resetMovingPosition() {
        movingPosition = 0
    }

    private fun setupMiniCart(data: MiniCartSimplifiedData) {
        val showMiniCartWidget = data.isShowMiniCartWidget
        val outOfCoverage = localCacheModel?.isOutOfCoverage() == true

        if(showMiniCartWidget && !outOfCoverage) {
            val pageName = MiniCartAnalytics.Page.HOME_PAGE
            val shopIds = listOf(localCacheModel?.shop_id.orEmpty())
            val source = MiniCartSource.TokonowHome
            miniCartWidget?.initialize(shopIds, this, this, pageName = pageName, source = source)
            miniCartWidget?.show()
            hideStickyLogin()

            showBottomSheetMiniCartList()
        } else {
            miniCartWidget?.hide()
            miniCartWidget?.hideCoachMark()
            isOpenMiniCartList = false
        }
    }

    private fun setUriData() {
        activity?.intent?.data?.let {
            isOpenMiniCartList = getIsOpenMiniCartListFromUri(it)
            externalServiceType = getExternalServiceType(it)
        }
    }

    private fun showBottomSheetMiniCartList() {
        if (isOpenMiniCartList) {
            showMiniCartBottomSheet()
            isOpenMiniCartList = false
        }
    }

    private fun showMiniCartBottomSheet() {
        miniCartWidget?.showMiniCartListBottomSheet(this)
    }

    private fun getIsOpenMiniCartListFromUri(uri: Uri): Boolean {
        return uri.getQueryParameter(KEY_IS_OPEN_MINICART_LIST)?.toBooleanStrictOrNull().orFalse()
    }

    private fun getExternalServiceType(uri: Uri): String {
        return uri.getQueryParameter(KEY_SERVICE_TYPE)?.toBlankOrString().orEmpty()
    }

    private fun setupPadding(isShowMiniCartWidget: Boolean) {
        miniCartWidget?.post {
            val outOfCoverage = localCacheModel?.isOutOfCoverage() == true
            val paddingBottom = if (isShowMiniCartWidget && !outOfCoverage) {
                getMiniCartHeight()
            } else {
                context?.resources?.getDimensionPixelSize(
                    com.tokopedia.unifyprinciples.R.dimen.layout_lvl0).orZero()
            }
            swipeLayout?.setPadding(0, 0, 0, paddingBottom)
        }
    }

    private fun onSuccessGetHomeLayout(data: HomeLayoutListUiModel) {
        when (data.state) {
            TokoNowLayoutState.SHOW -> onShowHomeLayout(data)
            TokoNowLayoutState.HIDE -> onHideHomeLayout(data)
            TokoNowLayoutState.LOADING -> onLoadingHomeLayout(data)
            else -> showHomeLayout(data)
        }
    }

    private fun onFailedGetHomeLayout(throwable: Throwable) {
        showFailedToFetchData()
        stickyLoginLoadContent()
        logHomeLayoutError(throwable)
    }

    private fun onLoadingHomeLayout(data: HomeLayoutListUiModel) {
        showHomeLayout(data)
        loadHeaderBackground()
        checkAddressDataAndServiceArea()
        showHideChooseAddress()
        hideSwitcherCoachMark()
    }

    private fun showHideChooseAddress() {
        if (!isChooseAddressWidgetShowed()) {
            adapter.removeHomeChooseAddressWidget()
        }
    }

    private fun onHideHomeLayout(data: HomeLayoutListUiModel) {
        showHomeLayout(data)
        hideHeaderBackground()
        stickyLoginLoadContent()
        stopPerformanceMonitoring()
    }

    private fun onShowHomeLayout(data: HomeLayoutListUiModel) {
        startRenderPerformanceMonitoring()
        showHomeLayout(data)
        showHeaderBackground()
        stickyLoginLoadContent()
        showOnBoarding()
        getLayoutComponentData()
        stopRenderPerformanceMonitoring()
    }

    private fun getLayoutComponentData() {
        localCacheModel?.let {
            viewModelTokoNow.getLayoutComponentData(it)
        }
    }

    private fun showOnBoarding() {
        rvHome?.post {
            when {
                // When in 2 hours state, if coach mark is never shown and the 20 minutes switcher widget is exist then show coach mark
                !homeSharedPref.get20mCoachMarkOnBoardShown() && adapter.getItem(Home20mSwitcher::class.java) != null -> {
                    rvHome?.addOneTimeGlobalLayoutListener {
                        show20mSwitcherCoachMark()
                    }
                }
                // When in 20 minutes state, if bottomsheet is never shown and the 2 hours switcher widget is exist then show bottomsheet
                !homeSharedPref.get20mBottomSheetOnBoardShown() && adapter.getItem(Home2hSwitcher::class.java) != null -> {
                    show20mBottomSheet()
                }
                //When in 20 minutes state, if coach mark is never shown and the 2 hours switcher widget is exist then show coach mark
                !homeSharedPref.get2hCoachMarkOnBoardShown() && adapter.getItem(Home2hSwitcher::class.java) != null -> {
                    rvHome?.addOneTimeGlobalLayoutListener {
                        show2hSwitcherCoachMark()
                    }
                }
            }
        }
    }

    private fun show20mBottomSheet() {
        var isBackTo2hClicked = false
        TokoNowOnBoard20mBottomSheet
            .newInstance()
            .show(childFragmentManager, OnBoard20mBottomSheetCallback(
                onBackTo2hClicked = {
                    localCacheModel?.let {
                        viewModelTokoNow.switchService(it)
                    }
                    isBackTo2hClicked = true
                },
                onDismiss = {
                    homeSharedPref.set20mBottomSheetOnBoardShown(true)
                    if (!isBackTo2hClicked) {
                        show2hSwitcherCoachMark()
                    }
                    isBackTo2hClicked = false
                }
            )
        )
    }

    private fun show20mSwitcherCoachMark() {
        adapter.getItem(Home20mSwitcher::class.java)?.let {

            // search viewholder by index
            val index = adapter.findPosition(it)
            rvHome?.findViewHolderForAdapterPosition(index)?.itemView?.findViewById<View>(R.id.tp_title)?.let { tpTitle ->

                // set switcher coachmark for specified views
                switcherCoachMark = SwitcherCoachMark(
                    context = tpTitle.context
                ) {
                    homeSharedPref.set20mCoachMarkOnBoardShown(
                        shown = true
                    )
                }.apply {
                    set20mCoachMark(
                        tpTitle = tpTitle
                    )
                    show()
                }
            }
        }
    }

    private fun show2hSwitcherCoachMark() {
        adapter.getItem(Home2hSwitcher::class.java)?.let {

            // search viewholder by index
            val index = adapter.findPosition(it)
            rvHome?.findViewHolderForAdapterPosition(index)?.itemView?.apply {

                val tpTitle = findViewById<View>(R.id.tp_title)
                val tpSubtitle = findViewById<View>(R.id.tp_subtitle)

                // set switcher coachmark for specified views
                switcherCoachMark = SwitcherCoachMark(
                    context = context
                ) {
                    homeSharedPref.set2hCoachMarkOnBoardShown(
                        shown = true
                    )
                }.apply {
                    set2hCoachMark(
                        tpTitle = tpTitle,
                        tpSubtitle = tpSubtitle
                    )
                    show()
                }
            }
        }
    }

    private fun hideSwitcherCoachMark() {
        switcherCoachMark?.hide()
    }

    private fun checkAddressDataAndServiceArea() {
        checkIfChooseAddressWidgetDataUpdated()
        val shopId = localCacheModel?.shop_id.toLongOrZero()
        val warehouseId = localCacheModel?.warehouse_id.toLongOrZero()
        checkStateNotInServiceArea(shopId = shopId, warehouseId = warehouseId)
    }

    private fun showHomeLayout(data: HomeLayoutListUiModel) {
        rvHome?.post {
            adapter.submitList(data.items)
        }
    }

    private fun addLoadMoreListener() {
        rvHome?.post {
            rvHome?.addOnScrollListener(loadMoreListener)
        }
    }

    private fun removeLoadMoreListener() {
        rvHome?.removeOnScrollListener(loadMoreListener)
    }

    private fun addNavBarScrollListener() {
        navBarScrollListener?.let {
            rvHome?.addOnScrollListener(it)
        }
    }

    private fun removeNavBarScrollListener() {
        navBarScrollListener?.let {
            rvHome?.removeOnScrollListener(it)
        }
    }

    private fun addHomeComponentScrollListener() {
        rvHome?.addOnScrollListener(homeComponentScrollListener)
    }

    private fun removeHomeComponentScrollListener() {
        rvHome?.removeOnScrollListener(homeComponentScrollListener)
    }

    private fun removeAllScrollListener() {
        removeLoadMoreListener()
        removeNavBarScrollListener()
        removeHomeComponentScrollListener()
    }

    private fun addScrollListener() {
        addLoadMoreListener()
        addNavBarScrollListener()
        addHomeComponentScrollListener()
    }

    private fun onScrollTokoMartHome() {
        localCacheModel?.let {
            val layoutManager = rvHome?.layoutManager as? LinearLayoutManager
            val lastVisibleItemIndex = layoutManager?.findLastVisibleItemPosition().orZero()
            val removeAbleWidgets = listOf(
                HomeRemoveAbleWidget(SHARING_EDUCATION, SharedPreferencesUtil.isSharingEducationRemoved(activity)),
                HomeRemoveAbleWidget(MAIN_QUEST, SharedPreferencesUtil.isQuestAllClaimedRemoved(activity))
            )
            viewModelTokoNow.onScrollTokoMartHome(lastVisibleItemIndex, it, removeAbleWidgets)
        }
    }

    private fun getHomeLayout() {
        localCacheModel?.let {
            val removeAbleWidgets = listOf(
                HomeRemoveAbleWidget(SHARING_EDUCATION, SharedPreferencesUtil.isSharingEducationRemoved(activity)),
                HomeRemoveAbleWidget(MAIN_QUEST, SharedPreferencesUtil.isQuestAllClaimedRemoved(activity))
            )
            viewModelTokoNow.getHomeLayout(it, removeAbleWidgets)
        }
    }

    private fun getMiniCart() {
        val shopId = listOf(localCacheModel?.shop_id.orEmpty())
        val warehouseId = localCacheModel?.warehouse_id
        viewModelTokoNow.getMiniCart(shopId, warehouseId)
    }

    private fun loadLayout() {
        viewModelTokoNow.getLoadingState()
    }

    private fun checkIfChooseAddressWidgetDataUpdated() {
        if (isChooseAddressWidgetDataUpdated()) {
            updateCurrentPageLocalCacheModelData()
        }
    }

    private fun isChooseAddressWidgetDataUpdated(): Boolean {
        localCacheModel?.let {
            context?.apply {
                return ChooseAddressUtils.isLocalizingAddressHasUpdated(
                    this,
                    it
                )
            }
        }
        return false
    }

    private fun isChooseAddressWidgetShowed(): Boolean {
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        return remoteConfig.getBoolean(
            TokoNowChooseAddressWidgetViewHolder.ENABLE_CHOOSE_ADDRESS_WIDGET,
            true
        )
    }

    private fun updateCurrentPageLocalCacheModelData() {
        context?.let {
            localCacheModel = ChooseAddressUtils.getLocalizingAddressData(it)
        }
    }

    private fun isFirstInstall(): Boolean {
        context?.let {
            if (!userSession.isLoggedIn && isShowFirstInstallSearch) {
                val sharedPrefs = it.getSharedPreferences(SHARED_PREFERENCES_KEY_FIRST_INSTALL_SEARCH, Context.MODE_PRIVATE)
                var firstInstallCacheValue = sharedPrefs.getLong(SHARED_PREFERENCES_KEY_FIRST_INSTALL_TIME_SEARCH, 0)
                if (firstInstallCacheValue == 0L) return false
                firstInstallCacheValue += FIRST_INSTALL_CACHE_VALUE
                val now = Date()
                val firstInstallTime = Date(firstInstallCacheValue)
                return if (now <= firstInstallTime) {
                    true
                } else {
                    saveFirstInstallTime()
                    false
                }
            } else {
                return false
            }
        }
        return false
    }

    private fun saveFirstInstallTime() {
        context?.let {
            sharedPrefs = it.getSharedPreferences(SHARED_PREFERENCES_KEY_FIRST_INSTALL_SEARCH, Context.MODE_PRIVATE)
            sharedPrefs?.edit()?.putLong(SHARED_PREFERENCES_KEY_FIRST_INSTALL_TIME_SEARCH, 0)?.apply()
        }
    }

    private fun setHint(searchPlaceholder: SearchPlaceholder) {
        searchPlaceholder.data?.let { data ->
            navToolbar?.setupSearchbar(
                    hints = listOf(
                            HintData(
                                    data.placeholder.orEmpty(),
                                    data.keyword.orEmpty()
                            )
                    ),
                    searchbarClickCallback = { onSearchBarClick() },
                    searchbarImpressionCallback = {},
                    durationAutoTransition = durationAutoTransition,
                    shouldShowTransition = shouldShowTransition()
            )
        }
    }

    private fun onSearchBarClick() {
        analytics.onClickSearchBar()
        RouteManager.route(context,
                getAutoCompleteApplinkPattern(),
                SOURCE,
                context?.resources?.getString(R.string.tokopedianow_search_bar_hint).orEmpty(),
                isFirstInstall().toString())
    }

    private fun getAutoCompleteApplinkPattern() =
            ApplinkConstInternalDiscovery.AUTOCOMPLETE +
                    PARAM_APPLINK_AUTOCOMPLETE +
                    "&" + getParamTokonowSRP()

    private fun getParamTokonowSRP() =
            "${SearchApiConst.BASE_SRP_APPLINK}=${ApplinkConstInternalTokopediaNow.SEARCH}"

    private fun shouldShowTransition(): Boolean {
        val abTestValue = getAbTestPlatform().getString(AB_TEST_AUTO_TRANSITION_KEY, "")
        return abTestValue == AUTO_TRANSITION_VARIANT
    }

    private fun createNavBarScrollListener(): NavRecyclerViewScrollListener? {
        return navToolbar?.let { toolbar ->
            context?.let { context ->
                NavRecyclerViewScrollListener(
                    navToolbar = toolbar,
                    startTransitionPixel = homeMainToolbarHeight,
                    toolbarTransitionRangePixel = context.resources.getDimensionPixelSize(R.dimen.tokopedianow_searchbar_transition_range),
                    navScrollCallback = object : NavRecyclerViewScrollListener.NavScrollCallback {
                        override fun onAlphaChanged(offsetAlpha: Float) { /* nothing to do */
                        }

                        override fun onSwitchToLightToolbar() { /* nothing to do */
                        }

                        override fun onSwitchToDarkToolbar() {
                            navToolbar?.hideShadow()
                        }

                        override fun onYposChanged(yOffset: Int) {}
                    },
                    fixedIconColor = NavToolbar.Companion.Theme.TOOLBAR_LIGHT_TYPE
                )
            }
        }
    }

    private fun createHomeComponentScrollListener(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                evaluateHomeComponentOnScroll(recyclerView, dy)
            }
        }
    }

    private fun createLoadMoreListener(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                onScrollTokoMartHome()
            }
        }
    }

    private fun shareClicked(shareHomeTokonow: ShareTokonow?){
        if(UniversalShareBottomSheet.isCustomSharingEnabled(context)){
            showUniversalShareBottomSheet(shareHomeTokonow)
        } else {
            LinkerManager.getInstance().executeShareRequest(shareRequest(context, shareHomeTokonow))
        }
    }

    private fun showUniversalShareBottomSheet(shareHomeTokonow: ShareTokonow?) {
        universalShareBottomSheet = UniversalShareBottomSheet.createInstance().apply {
            init(this@TokoNowHomeFragment)
            setUtmCampaignData(
                    pageName = PAGE_SHARE_NAME,
                    userId = userSession.userId.getOrDefaultZeroString(),
                    pageIdConstituents = shareHomeTokonow?.pageIdConstituents.orEmpty(),
                    feature = SHARE
            )
            setMetaData(
                    tnTitle = shareHomeTokonow?.thumbNailTitle.orEmpty(),
                    tnImage = shareHomeTokonow?.thumbNailImage.orEmpty(),
            )
            //set the Image Url of the Image that represents page
            setOgImageUrl(imgUrl = shareHomeTokonow?.ogImageUrl.orEmpty())
        }

        if (shareHomeTokonow?.isScreenShot == true) {
            analytics.trackImpressChannelShareBottomSheetScreenShot()
        } else {
            analytics.trackImpressChannelShareBottomSheet()
        }
        universalShareBottomSheet?.show(childFragmentManager, this, screenshotDetector)
    }

    private fun createShareHomeTokonow(): ShareTokonow {
        return ShareTokonow(
                sharingText = context?.resources?.getString(R.string.tokopedianow_home_share_main_text).orEmpty(),
                thumbNailImage = THUMBNAIL_AND_OG_IMAGE_SHARE_URL,
                ogImageUrl = THUMBNAIL_AND_OG_IMAGE_SHARE_URL,
                specificPageName = context?.resources?.getString(R.string.tokopedianow_home_share_title).orEmpty(),
                specificPageDescription = context?.resources?.getString(R.string.tokopedianow_home_share_desc).orEmpty()
        )
    }

    private fun createTokoNowEmptyStateOocListener(): TokoNowEmptyStateOocViewHolder.TokoNowEmptyStateOocListener {
        return object : TokoNowEmptyStateOocViewHolder.TokoNowEmptyStateOocListener {
            override fun onRefreshLayoutPage() {
                onRefreshLayout()
            }

            override fun onGetFragmentManager(): FragmentManager = parentFragmentManager

            override fun onGetEventCategory(): String = EVENT_CATEGORY_HOME_PAGE

            override fun onSwitchService() {
                localCacheModel?.let {
                    viewModelTokoNow.switchService(it)
                }
            }
        }
    }

    private fun createQuestWidgetCallback(): HomeQuestSequenceWidgetListener {
        return QuestWidgetCallback(this, viewModelTokoNow, analytics)
    }

    private fun createSlideBannerCallback(): BannerComponentCallback? {
        bannerComponentCallback = BannerComponentCallback(this, viewModelTokoNow, userSession, analytics)
        return bannerComponentCallback
    }

    private fun createLegoBannerCallback(): DynamicLegoBannerCallback {
        return DynamicLegoBannerCallback(this, viewModelTokoNow, userSession, analytics)
    }

    private fun createHomeSwitcherListener(): HomeSwitcherListener {
        return HomeSwitcherListener(requireContext(), viewModelTokoNow)
    }

    private fun createLeftCarouselAtcCallback(): HomeLeftCarouselAtcCallback {
        return HomeLeftCarouselAtcCallback(
            context = requireContext(),
            userSession = userSession,
            viewModel = viewModelTokoNow,
            analytics = analytics,
            startActivityForResult = this::startActivityForResult
        )
    }

    private fun createLeftCarouselCallback(): MixLeftComponentListener {
        return HomeLeftCarouselCallback(this, analytics)
    }

    private fun createPlayWidgetCoordinator(): PlayWidgetCoordinator {
        val playWidgetCoordinator = PlayWidgetCoordinator(this).apply {
            setImpressionHelper(ImpressionHelper(validator = playWidgetImpressionValidator))
            setAnalyticModel(HomePlayWidgetAnalyticModel)
            setListener(this@TokoNowHomeFragment)
        }
        this.playWidgetCoordinator = playWidgetCoordinator
        return playWidgetCoordinator
    }

    override fun onShareOptionClicked(shareModel: ShareModel) {
        if (shareHomeTokonow?.isScreenShot == true) {
            analytics.trackClickChannelShareBottomSheetScreenshot(shareModel.channel.orEmpty())
        } else {
            analytics.trackClickChannelShareBottomSheet(shareModel.channel.orEmpty())
        }

        shareOptionRequest(
            shareModel = shareModel,
            shareHomeTokonow = shareHomeTokonow,
            activity = activity,
            view = view,
            onSuccess = {
                universalShareBottomSheet?.dismiss()
            }
        )
    }

    override fun onCloseOptionClicked() {
        if (shareHomeTokonow?.isScreenShot == true) {
            analytics.trackClickCloseScreenShotShareBottomSheet()
        } else {
            analytics.trackClickCloseShareBottomSheet()
        }
    }

    private fun getMiniCartHeight(): Int {
        return miniCartWidget?.height.orZero() - context?.resources?.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_16)?.toInt().orZero()
    }

    override fun permissionAction(action: String, label: String) {
        analytics.trackClickAccessMediaAndFiles(label)
    }

    private fun openWebView(linkUrl: String) {
        RouteManager.route(context, "${ApplinkConst.WEBVIEW}?url=${linkUrl}")
    }
}