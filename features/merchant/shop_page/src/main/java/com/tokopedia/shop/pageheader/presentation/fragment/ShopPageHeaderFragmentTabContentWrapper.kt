package com.tokopedia.shop.pageheader.presentation.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.marginTop
import androidx.core.view.updateMargins
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.appbar.AppBarLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.FragmentConst
import com.tokopedia.applink.FragmentConst.FEED_SHOP_FRAGMENT
import com.tokopedia.applink.FragmentConst.SHOP_REVIEW_FRAGMENT
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.common_sdk_affiliate_toko.utils.AffiliateCookieHelper
import com.tokopedia.config.GlobalConfig
import com.tokopedia.content.common.analytic.entrypoint.PlayPerformanceDashboardEntryPointAnalytic
import com.tokopedia.content.common.util.doOnApplyWindowInsets
import com.tokopedia.content.common.util.marginLp
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.encodeToUtf8
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.navigation_component.NavSource
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.searchbar.navigation_component.util.StatusBarUtil
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.analytic.ShopPageTrackingBuyer
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_PAGE
import com.tokopedia.shop.analytic.ShopPageTrackingSGCPlayWidget
import com.tokopedia.shop.campaign.view.fragment.ShopPageCampaignFragment
import com.tokopedia.shop.common.constant.ShopHomeType
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.common.data.model.HomeLayoutData
import com.tokopedia.shop.common.data.model.ShopPageGetDynamicTabResponse
import com.tokopedia.shop.common.data.source.cloud.model.followshop.FollowShop
import com.tokopedia.shop.common.data.source.cloud.model.followstatus.FollowStatus
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.common.view.interfaces.InterfaceShopPageHeader
import com.tokopedia.shop.common.view.listener.InterfaceShopPageClickScrollToTop
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.databinding.ShopHeaderFragmentTabContentBinding
import com.tokopedia.shop.home.view.fragment.ShopPageHomeFragment
import com.tokopedia.shop.home.view.fragment.ShopPagePrefetchFragment
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderDataModel
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderDataModel.Companion.mapperForShopShowCase
import com.tokopedia.shop.pageheader.di.component.DaggerShopPageHeaderComponent
import com.tokopedia.shop.pageheader.di.component.ShopPageHeaderComponent
import com.tokopedia.shop.pageheader.di.module.ShopPageHeaderModule
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.widget.ShopPageHeaderPlayWidgetViewHolder
import com.tokopedia.shop.pageheader.presentation.fragment.ShopPageReimagineHeaderFragment.Companion.SHOWCASE_ID_USED_TO_HIDE_SHARE_CTA
import com.tokopedia.shop.pageheader.presentation.holder.ShopPageHeaderFragmentHeaderViewHolderV2
import com.tokopedia.shop.pageheader.presentation.holder.ShopPageHeaderFragmentViewHolderListener
import com.tokopedia.shop.pageheader.presentation.uimodel.ShopFollowButtonUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.ShopPageHeaderLayoutUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.ShopPageHeaderP1HeaderData
import com.tokopedia.shop.pageheader.presentation.uimodel.ShopPageHeaderTickerData
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopPageHeaderWidgetUiModel
import com.tokopedia.shop.pageheader.util.ShopPageHeaderTabName
import com.tokopedia.shop.product.view.fragment.ShopPageProductListFragment
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.resources.isDarkMode
import java.net.URLEncoder
import javax.inject.Inject
import kotlin.math.abs
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class ShopPageHeaderFragmentTabContentWrapper :
    BaseDaggerFragment(),
    HasComponent<ShopPageHeaderComponent> {

    companion object {
        const val SHOP_ID = "EXTRA_SHOP_ID"
        const val SHOP_ATTRIBUTION = "EXTRA_SHOP_ATTRIBUTION"
        private const val FRAGMENT_SHOWCASE_KEY_SHOP_ID = "SHOP_ID"
        private const val FRAGMENT_SHOWCASE_KEY_SHOP_REF = "SHOP_REF"
        private const val FRAGMENT_SHOWCASE_KEY_SHOP_ATTRIBUTION = "SHOP_ATTRIBUTION"
        private const val FRAGMENT_SHOWCASE_KEY_FORCE_LIGHT_MODE = "force_light_mode"
        private const val FRAGMENT_SHOWCASE_KEY_IS_OS = "IS_OS"
        private const val FRAGMENT_SHOWCASE_KEY_IS_GOLD_MERCHANT = "IS_GOLD_MERCHANT"
        private const val FRAGMENT_SHOWCASE_KEY_FOR_SHARE = "shop_header_for_sharing"
        private const val QUERY_PARAM_EXT_PARAM = "extParam"
        private const val DEFAULT_SHOWCASE_ID = "0"
        private const val SHOP_SEARCH_PAGE_NAV_SOURCE = "shop"
        private const val FEED_SHOP_FRAGMENT_SHOP_ID = "PARAM_SHOP_ID"
        private const val ARGS_SHOP_ID_FOR_REVIEW_TAB = "ARGS_SHOP_ID"
        private const val MAX_ALPHA = 255f
        private const val PADDING_TOP_IN_DP = 8

        @JvmStatic
        fun createInstance() = ShopPageHeaderFragmentTabContentWrapper()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var affiliateCookieHelper: AffiliateCookieHelper

    @Inject
    lateinit var playPerformanceDashboardEntryPointAnalytic: PlayPerformanceDashboardEntryPointAnalytic

    private var shopId = ""
    private var userId = ""
    private val shopName: String
        get() = shopPageHeaderDataModel?.shopName.orEmpty()
    private var shopRef: String = ""
    private var shopDomain: String? = null
    private var shopAttribution: String? = null
    private var isShowFeed: Boolean = false
    private var shopPageHeaderFragmentHeaderViewHolder: ShopPageHeaderFragmentHeaderViewHolderV2? = null
    private var isForceNotShowingTab: Boolean = false

    private var extParam: String = ""
    private var isMyShop: Boolean = false
    private var appBarLayout: AppBarLayout? = null
    private var refreshLayout: SwipeRefreshLayout? = null
    private var mainLayout: View? = null
    private var navToolbar: NavToolbar? = null
    private var viewBinding by autoClearedNullable<ShopHeaderFragmentTabContentBinding>()

    private var isLoadInitialData = false
    private var isUserSessionActive = false
    private var tabData: ShopPageGetDynamicTabResponse.ShopPageGetDynamicTab.TabData? = null
    private var shopPageHeaderDataModel: ShopPageHeaderDataModel ? = null
    private var shopPagePageHeaderWidgetList: List<ShopPageHeaderWidgetUiModel> = listOf()
    private var shopHeaderLayoutData: ShopPageHeaderLayoutUiModel = ShopPageHeaderLayoutUiModel()
    private var tickerResultData: ShopPageHeaderTickerData? = null
    private var chooseAddressWidgetListener: ChooseAddressWidget.ChooseAddressWidgetListener? = null
    private var iconShareId: Int = IconList.ID_SHARE
    private var shopFollowButtonUiModel: ShopFollowButtonUiModel = ShopFollowButtonUiModel()
    private var tabFragment: Fragment? = null
    private var initialShopLayoutData: HomeLayoutData? = null
    private var appbarOffsetRatio: Float = 0f
    private val mockDataSharedPreferences by lazy {
        activity?.getSharedPreferences(ShopPageConstant.SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

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
        viewBinding = ShopHeaderFragmentTabContentBinding.inflate(LayoutInflater.from(context))
        return viewBinding?.root
    }

    private fun initViews(view: View) {
        mainLayout = viewBinding?.root
        navToolbar = viewBinding?.navToolbar
        appBarLayout = viewBinding?.appbarLayout
        refreshLayout = viewBinding?.parentSwipeRefreshLayout
        shopPageHeaderFragmentHeaderViewHolder = ShopPageHeaderFragmentHeaderViewHolderV2(
            viewBinding,
            getHeaderListener(),
            getShopPageTracking(),
            getShopPageTrackingSGCPlay(),
            view.context,
            getPlayHeaderListener(),
            chooseAddressWidgetListener
        )
        mainLayout?.requestFocus()

        setupInsets()
    }

    private fun setupInsets() {
        val initialNavToolbarPaddingTop = navToolbar?.paddingTop.orZero()
        val initialHeaderTopAnchorMarginTop = viewBinding?.headerTopAnchor?.marginTop.orZero()

        mainLayout?.doOnApplyWindowInsets { _, insets, _, _ ->
            val statusBarInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            navToolbar?.updatePadding(top = statusBarInsets.top)
            viewBinding?.headerTopAnchor?.marginLp?.updateMargins(top = initialHeaderTopAnchorMarginTop + statusBarInsets.top - initialNavToolbarPaddingTop)
        }
    }

    private fun getPlayHeaderListener(): ShopPageHeaderPlayWidgetViewHolder.Listener? {
        return parentFragment as? ShopPageHeaderPlayWidgetViewHolder.Listener
    }

    private fun getShopPageTrackingSGCPlay(): ShopPageTrackingSGCPlayWidget? {
        return (parentFragment as? ShopPageReimagineHeaderFragment)?.shopPageTrackingSGCPlay
    }

    private fun getShopPageTracking(): ShopPageTrackingBuyer? {
        return (parentFragment as? ShopPageReimagineHeaderFragment)?.shopPageTracking
    }

    private fun getHeaderListener(): ShopPageHeaderFragmentViewHolderListener? {
        return parentFragment as? ShopPageHeaderFragmentViewHolderListener
    }

    private fun getShopHeaderConfig(): ShopPageHeaderLayoutUiModel.Config? {
        return shopHeaderLayoutData.getShopConfigListByName(ShopPageHeaderLayoutUiModel.ConfigName.SHOP_HEADER)
    }

    private fun getShopBodyConfig(): ShopPageHeaderLayoutUiModel.Config? {
        return shopHeaderLayoutData.getShopConfigListByName(ShopPageHeaderLayoutUiModel.ConfigName.SHOP_BODY)
    }
    private fun setupAppBarLayout() {
        appBarLayout?.addOnOffsetChangedListener { _, verticalOffset ->
            refreshLayout?.isEnabled = (verticalOffset == 0)
            val isOverrideTheme = shopHeaderLayoutData.isOverrideTheme
            setNavToolbarScrollColorTransition(verticalOffset)
            if (isOverrideTheme) {
                if (appbarOffsetRatio < Int.ONE.toFloat()) {
                    resumeHeaderVideo()
                    setToolbarColorFromHeaderConfig()
                    setStatusBarColor(getShopHeaderConfig()?.getFinalPatternColorType(context?.isDarkMode().orFalse()).orEmpty())
                } else {
                    pauseHeaderVideo()
                    setToolbarColorFromBodyConfig()
                    setStatusBarColor(getShopBodyConfig()?.getFinalPatternColorType(context?.isDarkMode().orFalse()).orEmpty())
                }
            }
        }
    }

    private fun setToolbarColorFromBodyConfig() {
        navToolbar?.let {
            val intColor = getShopBodyConfig()?.colorSchema?.getColorIntValue(
                ShopPageColorSchema.ColorSchemaName.ICON_ENABLED_HIGH_COLOR
            ).orZero()
            val patternColorType = getShopBodyConfig()?.getFinalPatternColorType(context?.isDarkMode().orFalse())
            configToolbarColor(it, intColor, patternColorType)
        }
    }

    private fun setToolbarColorFromHeaderConfig() {
        navToolbar?.let {
            val intColor = getShopHeaderConfig()?.colorSchema?.getColorIntValue(
                ShopPageColorSchema.ColorSchemaName.ICON_ENABLED_HIGH_COLOR
            ).orZero()
            val patternColorType = getShopHeaderConfig()?.getFinalPatternColorType(context?.isDarkMode().orFalse())
            configToolbarColor(it, intColor, patternColorType)
        }
    }

    private fun setNavToolbarScrollColorTransition(verticalOffset: Int) {
        val bodyBackgroundColor = getShopBodyConfig()?.listBackgroundColor?.firstOrNull().orEmpty()
        val endColor = if (shopHeaderLayoutData.isOverrideTheme && bodyBackgroundColor.isNotEmpty()) {
            ShopUtil.parseColorFromHexString(bodyBackgroundColor)
        } else {
            MethodChecker.getColor(context, unifyprinciplesR.color.Unify_NN0)
        }
        // Calculate the scroll range
        val scrollRange = appBarLayout?.totalScrollRange
        // Calculate the offset ratio (between 0 and 1) based on the scroll position
        appbarOffsetRatio = abs(verticalOffset).toFloat() / (scrollRange?.toFloat() ?: Int.ZERO.toFloat())
        val blendedColor = blendColors(endColor, appbarOffsetRatio)
        // Set the toolbar background color
        navToolbar?.background = ColorDrawable(blendedColor)
        navToolbar?.apply {
            val drawable = background
            drawable.alpha = (appbarOffsetRatio * MAX_ALPHA).toInt()
            background = drawable
        }
    }

    private fun blendColors(endColor: Int, ratio: Float): Int {
        val startColor = Color.TRANSPARENT
        val inverseRatio = Int.ONE.toFloat() - ratio
        val r = (Color.red(endColor) * ratio + Color.red(startColor) * inverseRatio).toInt()
        val g = (Color.green(endColor) * ratio + Color.green(startColor) * inverseRatio).toInt()
        val b = (Color.blue(endColor) * ratio + Color.blue(startColor) * inverseRatio).toInt()
        return Color.rgb(r, g, b)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        iniSwipeRefreshLayout()
        isLoadInitialData = true
    }

    override fun onDestroy() {
        releaseHeaderVideo()
        clearTimerDynamicUsp()
        super.onDestroy()
    }

    private fun clearTimerDynamicUsp() {
        shopPageHeaderFragmentHeaderViewHolder?.clearTimerDynamicUsp()
    }

    private fun releaseHeaderVideo() {
        shopPageHeaderFragmentHeaderViewHolder?.releaseVideo()
    }

    private fun iniSwipeRefreshLayout() {
        refreshLayout?.setOnRefreshListener {
            (parentFragment as? ShopPageReimagineHeaderFragment)?.refreshData()
        }
    }

    private fun setShopHeaderData() {
        shopPageHeaderFragmentHeaderViewHolder?.setupShopHeader(
            shopPagePageHeaderWidgetList,
            shopFollowButtonUiModel,
            getShopHeaderConfig(),
            shopHeaderLayoutData.isOverrideTheme,
            shopPageHeaderDataModel
        )
    }

    private fun setupFragment() {
        createContentFragment()?.let {
            tabFragment = it
            val ft: FragmentTransaction = childFragmentManager.beginTransaction()
            ft.replace(
                viewBinding?.tabFragment?.id.orZero(),
                it
            )
            ft.commit()
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

    private fun setupToolbar() {
        initToolbar()
        if (isMyShop) {
            configToolbarForSellerView()
        } else {
            configToolbarForBuyerView()
        }
        configureSearchStyle()
    }

    private fun configureSearchStyle() {
        navToolbar?.updateSearchBarStyle(showSearchBtn = false)
    }

    private fun configToolbarForSellerView() {
        navToolbar?.apply {
            setToolbarContentType(NavToolbar.Companion.ContentType.TOOLBAR_TYPE_TITLE)
            setToolbarTitle(getString(R.string.text_your_shop))
            val iconBuilder = IconBuilder(
                builderFlags = IconBuilderFlag(
                    pageSource = NavSource.SHOP
                )
            )
            iconBuilder.addIcon(iconShareId, disableRouteManager = true) { clickShopShare() }
            iconBuilder.addIcon(IconList.ID_SEARCH, disableRouteManager = true) { clickSearch() }
            iconBuilder.addIcon(IconList.ID_SETTING, disableRouteManager = true) { clickSettingButton() }
            setIcon(iconBuilder)
            show()
        }
    }

    private fun configToolbarForBuyerView() {
        navToolbar?.apply {
            setToolbarContentType(NavToolbar.Companion.ContentType.TOOLBAR_TYPE_SEARCH)
            val iconBuilder =
                IconBuilder(builderFlags = IconBuilderFlag(pageSource = NavSource.SHOP))
            iconBuilder.addIcon(iconShareId, disableRouteManager = true) { clickShopShare() }
            if (isCartShownInNewNavToolbar()) {
                iconBuilder.addIcon(IconList.ID_CART) {}
            }
            iconBuilder.addIcon(IconList.ID_NAV_GLOBAL) {}
            setIcon(iconBuilder)
            if (isUserSessionActive) {
                setBadgeCounter(IconList.ID_CART, getCartCounter())
            }
            val searchBarHintText = MethodChecker.fromHtml(
                getString(
                    R.string.shop_product_search_hint_2,
                    shopPageHeaderDataModel?.shopName.orEmpty()
                )
            ).toString()
            setupSearchbar(
                hints = listOf(HintData(placeholder = searchBarHintText)),
                searchbarClickCallback = {
                    redirectToSearchAutoCompletePage()
                }
            )
            show()
        }
    }

    private fun initToolbar() {
        navToolbar?.apply {
            val isOverrideTheme = shopHeaderLayoutData.isOverrideTheme
            if (isOverrideTheme) {
                val hexIconColor = getShopHeaderConfig()?.colorSchema?.getColorSchema(
                    ShopPageColorSchema.ColorSchemaName.ICON_ENABLED_HIGH_COLOR
                )?.value.orEmpty()
                setupSearchBarWithStaticLightModeColor()
                val color = ShopUtil.parseColorFromHexString(hexIconColor)
                val patternColorType = getShopHeaderConfig()?.getFinalPatternColorType(context?.isDarkMode().orFalse())
                configToolbarColor(this, color, patternColorType)
            }
            setBackButtonType(NavToolbar.Companion.BackType.BACK_TYPE_BACK)
            viewLifecycleOwner.lifecycle.addObserver(this)
            setToolbarPageName(SHOP_PAGE)
        }
    }

    private fun configToolbarColor(navToolbar: NavToolbar, color: Int, patternColorType: String?) {
        navToolbar.apply {
            setNavToolbarIconCustomColor(
                navToolbarIconCustomLightColor = color,
                navToolbarIconCustomDarkColor = color
            )
            setIconCustomColor(
                darkColor = color,
                lightColor = color
            )
            if (patternColorType == ShopPageHeaderLayoutUiModel.ColorType.LIGHT.value) {
                switchToLightToolbar()
            } else {
                switchToDarkToolbar()
            }
        }
    }

    private fun getCartCounter(): Int {
        return (parentFragment as? InterfaceShopPageHeader)?.getCartCounterData().orZero()
    }

    internal fun updateShareIcon(newShareIconId: Int) {
        iconShareId = newShareIconId
        navToolbar?.updateIcon(IconList.ID_SHARE, iconShareId)
    }

    private fun isCartShownInNewNavToolbar(): Boolean {
        return !GlobalConfig.isSellerApp()
    }

    private fun redirectToSearchAutoCompletePage() {
        val shopSrpAppLink = URLEncoder.encode(
            UriUtil.buildUri(
                ApplinkConst.SHOP_ETALASE,
                shopId,
                SHOWCASE_ID_USED_TO_HIDE_SHARE_CTA
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

    override fun onResume() {
        super.onResume()
        renderPageAfterOnViewCreated()
        resumeHeaderVideo()
        resumeTimerDynamicUspCycle()
    }

    private fun resumeTimerDynamicUspCycle() {
        shopPageHeaderFragmentHeaderViewHolder?.startDynamicUspCycle(shopPagePageHeaderWidgetList)
    }

    private fun resumeHeaderVideo() {
        if (appbarOffsetRatio < Int.ONE.toFloat()) {
            shopPageHeaderFragmentHeaderViewHolder?.resumeVideo()
        }
    }

    override fun onPause() {
        super.onPause()
        pauseHeaderVideo()
        pauseTimerDynamicUspCycle()
    }

    private fun pauseTimerDynamicUspCycle() {
        shopPageHeaderFragmentHeaderViewHolder?.pauseTimerDynamicUspCycle()
    }

    private fun pauseHeaderVideo() {
        if (appbarOffsetRatio >= Int.ONE.toFloat()) {
            shopPageHeaderFragmentHeaderViewHolder?.pauseVideo()
        }
    }

    private fun renderPageAfterOnViewCreated() {
        if (isLoadInitialData) {
            appBarLayout?.show()
            setStatusBarColor(getShopHeaderConfig()?.getFinalPatternColorType(context?.isDarkMode().orFalse()).orEmpty())
            setupToolbar()
            setupAppBarLayout()
            setupChooseAddressWidget()
            setShopHeaderData()
            setupFragment()
            setupFragmentBackgroundColor()
            setupTicker()
            getShopP2Data()
            isLoadInitialData = false
        }
    }

    private fun setStatusBarColor(patternColorType: String) {
        if (shopHeaderLayoutData.isOverrideTheme) {
            if (patternColorType == ShopPageHeaderLayoutUiModel.ColorType.LIGHT.value) {
                setStatusBarDarkColor()
            } else {
                setStatusBarLightColor()
            }
        } else {
            if (context?.isDarkMode() == false) {
                setStatusBarDarkColor()
            } else {
                setStatusBarLightColor()
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun setStatusBarLightColor() {
        val flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        activity?.window?.decorView?.systemUiVisibility = flag
        StatusBarUtil.setWindowFlag(
            activity,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
            false
        )
        activity?.window?.statusBarColor = Color.TRANSPARENT
    }

    @Suppress("DEPRECATION")
    private fun setStatusBarDarkColor() {
        var flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        var statusBarColor = MethodChecker.getColor(
            context,
            R.color.dms_static_status_bar_dark_color
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flag = flag or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            statusBarColor = Color.TRANSPARENT
        }
        activity?.window?.decorView?.systemUiVisibility = flag
        StatusBarUtil.setWindowFlag(
            activity,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
            false
        )
        activity?.window?.statusBarColor = statusBarColor
    }

    private fun setupFragmentBackgroundColor() {
        context?.let {
            if (shopHeaderLayoutData.isOverrideTheme) {
                val defaultFragmentBackgroundColorStringHex = ShopUtil.getColorHexString(it,unifyprinciplesR.color.Unify_NN0)
                val fragmentBackgroundColor = getShopBodyConfig()?.listBackgroundColor?.firstOrNull().orEmpty().takeIf {
                    it.isNotEmpty()
                } ?: defaultFragmentBackgroundColorStringHex
                viewBinding?.tabFragment?.background = ColorDrawable(ShopUtil.parseColorFromHexString(fragmentBackgroundColor))
            }
        }
    }

    private fun getShopP2Data() {
        (parentFragment as? InterfaceShopPageHeader)?.onTabFragmentWrapperFinishLoad()
    }

    private fun setupChooseAddressWidget() {
        shopPageHeaderFragmentHeaderViewHolder?.setupChooseAddressWidget(isMyShop)
    }

    fun setupTicker() {
        tickerResultData?.let {
            shopPageHeaderFragmentHeaderViewHolder?.updateShopTicker(it, isMyShop)
        }
    }

    private fun clickShopShare() {
        (parentFragment as? ShopPageReimagineHeaderFragment)?.clickShopShare()
    }

    private fun clickSearch() {
        (parentFragment as? ShopPageReimagineHeaderFragment)?.clickSearch()
    }

    private fun clickSettingButton() {
        (parentFragment as? ShopPageReimagineHeaderFragment)?.clickSettingButton()
    }

    private fun createContentFragment(): Fragment? {
        return tabData?.let {
            when (it.name) {
                ShopPageHeaderTabName.PRE_FETCH_DATA -> {
                    ShopPagePrefetchFragment.newInstance()
                }
                ShopPageHeaderTabName.HOME -> {
                    ShopPageHomeFragment.createInstance(
                        shopId,
                        shopPageHeaderDataModel?.isOfficial ?: false,
                        shopPageHeaderDataModel?.isGoldMerchant ?: false,
                        shopPageHeaderDataModel?.shopName.orEmpty(),
                        shopAttribution ?: "",
                        shopRef,
                        shopPageHeaderDataModel?.isEnableDirectPurchase.orFalse()
                    ).apply {
                        setHomeTabListBackgroundColor(it.listBackgroundColor)
                        setHomeTabBackgroundPatternImage(it.backgroundImage)
                        setHomeTabLottieUrl(
                            it.lottieUrl.ifEmpty {
                            mockDataSharedPreferences?.getString(ShopPageConstant.SHARED_PREF_MOCK_LOTTIE_URL_DATA, null).orEmpty()
                        })
                        if (initialShopLayoutData != null) {
                            setListWidgetLayoutData(initialShopLayoutData)
                        }
                    }
                }

                ShopPageHeaderTabName.PRODUCT -> {
                    val shopPageProductFragment = ShopPageProductListFragment.createInstance(
                        shopId = shopId,
                        shopName = shopPageHeaderDataModel?.shopName.orEmpty(),
                        isOfficial = shopPageHeaderDataModel?.isOfficial ?: false,
                        isGoldMerchant = shopPageHeaderDataModel?.isGoldMerchant ?: false,
                        shopAttribution = shopAttribution,
                        shopRef = shopRef,
                        isEnableDirectPurchase = shopPageHeaderDataModel?.isEnableDirectPurchase.orFalse()
                    )
                    shopPageProductFragment
                }

                ShopPageHeaderTabName.SHOWCASE -> {
                    val shopShowcaseTabFragment = RouteManager.instantiateFragmentDF(
                        activity as AppCompatActivity,
                        FragmentConst.SHOP_SHOWCASE_TAB_FRAGMENT_CLASS_PATH,
                        Bundle().apply {
                            putString(FRAGMENT_SHOWCASE_KEY_SHOP_ID, shopId)
                            putString(FRAGMENT_SHOWCASE_KEY_SHOP_REF, shopRef)
                            putString(FRAGMENT_SHOWCASE_KEY_SHOP_ATTRIBUTION, shopAttribution)
                            putBoolean(
                                FRAGMENT_SHOWCASE_KEY_IS_OS,
                                shopPageHeaderDataModel?.isOfficial ?: false
                            )
                            putBoolean(
                                FRAGMENT_SHOWCASE_KEY_IS_GOLD_MERCHANT,
                                shopPageHeaderDataModel?.isGoldMerchant ?: false
                            )
                            putParcelable(
                                FRAGMENT_SHOWCASE_KEY_FOR_SHARE,
                                shopPageHeaderDataModel?.mapperForShopShowCase()
                            )
                            putBoolean(
                                FRAGMENT_SHOWCASE_KEY_FORCE_LIGHT_MODE,
                                shopHeaderLayoutData.isOverrideTheme
                            )
                        }
                    )
                    shopShowcaseTabFragment
                }

                ShopPageHeaderTabName.FEED -> {
                    if (isShowFeed) {
                        val feedFragment = RouteManager.instantiateFragmentDF(
                            activity as AppCompatActivity,
                            FEED_SHOP_FRAGMENT,
                            Bundle().apply {
                                putString(FEED_SHOP_FRAGMENT_SHOP_ID, shopId)
                            }
                        )
                        feedFragment
                    } else {
                        null
                    }
                }

                ShopPageHeaderTabName.REVIEW -> {
                    val reviewTabFragment = RouteManager.instantiateFragmentDF(
                        activity as AppCompatActivity,
                        SHOP_REVIEW_FRAGMENT,
                        Bundle().apply {
                            putString(ARGS_SHOP_ID_FOR_REVIEW_TAB, shopId)
                        }
                    )
                    reviewTabFragment
                }

                ShopPageHeaderTabName.CAMPAIGN -> {
                    createCampaignTabFragment(it)
                }

                else -> {
                    null
                }
            }
        }
    }

    private fun createCampaignTabFragment(
        tabData: ShopPageGetDynamicTabResponse.ShopPageGetDynamicTab.TabData
    ): Fragment {
        return ShopPageCampaignFragment.createInstance(shopId).apply {
            setCampaignTabListBackgroundColor(tabData.listBackgroundColor)
            setListPatternImage(tabData.bgImages)
            setIsDarkTheme(tabData.isDark)
            if (initialShopLayoutData != null) {
                setListWidgetLayoutData(initialShopLayoutData)
            }
        }
    }

    fun setExpandHeader(isExpandHeader: Boolean) {
        appBarLayout?.post {
            appBarLayout?.setExpanded(isExpandHeader)
        }
    }

    
    fun setShopPageHeaderP1Data(
        shopPageHeaderP1Data: ShopPageHeaderP1HeaderData,
        isEnableDirectPurchase: Boolean,
        isShouldShowFeed: Boolean
    ) {
        isShowFeed = isShouldShowFeed
        shopPageHeaderDataModel = ShopPageHeaderDataModel().apply {
            shopId = this@ShopPageHeaderFragmentTabContentWrapper.shopId
            isOfficial = shopPageHeaderP1Data.isOfficial
            isGoldMerchant = shopPageHeaderP1Data.isGoldMerchant
            pmTier = shopPageHeaderP1Data.pmTier
            shopHomeType = shopPageHeaderP1Data.shopHomeType.takeIf { !isForceNotShowingTab }
                ?: ShopHomeType.NONE
            shopName = MethodChecker.fromHtml(shopPageHeaderP1Data.shopName).toString()
            shopDomain = shopPageHeaderP1Data.shopDomain
            avatar = shopPageHeaderP1Data.shopAvatar
            listDynamicTabData = shopPageHeaderP1Data.listDynamicTabData
            this.isEnableDirectPurchase = isEnableDirectPurchase
            this.shopBadge = shopPageHeaderP1Data.shopBadge
        }
        shopPagePageHeaderWidgetList = shopPageHeaderP1Data.listShopPageHeaderWidget
        shopHeaderLayoutData = shopPageHeaderP1Data.shopHeaderLayoutData
    }

    fun setTabData(tabData: ShopPageGetDynamicTabResponse.ShopPageGetDynamicTab.TabData) {
        this.tabData = tabData
    }

    fun updateShopTickerData(tickerResultData: ShopPageHeaderTickerData) {
        this.tickerResultData = tickerResultData
        setupTicker()
    }

    fun setShopId(shopId: String) {
        this.shopId = shopId
    }

    fun setIsMyShop(isMyShop: Boolean) {
        this.isMyShop = isMyShop
    }

    fun setChooseAddressListener(chooseAddressWidgetListener: ChooseAddressWidget.ChooseAddressWidgetListener) {
        this.chooseAddressWidgetListener = chooseAddressWidgetListener
    }

    fun setFollowButtonLoading(isLoading: Boolean) {
        shopFollowButtonUiModel = shopFollowButtonUiModel.copy(
            isShowLoading = isLoading
        )
        shopPageHeaderFragmentHeaderViewHolder?.updateFollowButton(shopFollowButtonUiModel)
    }

    fun setFollowButtonUsingFollowStatusData(followStatus: FollowStatus?) {
        shopFollowButtonUiModel = shopFollowButtonUiModel.copy(
            isFollowing = followStatus?.status?.userIsFollowing == true,
            textLabel = followStatus?.followButton?.buttonLabel.orEmpty(),
            leftDrawableUrl = followStatus?.followButton?.voucherIconURL.orEmpty(),
            isNeverFollow = followStatus?.status?.userNeverFollow == true
        )
        shopPageHeaderFragmentHeaderViewHolder?.updateFollowButton(shopFollowButtonUiModel)
    }

    fun setUserId(userId: String) {
        this.userId = userId
    }

    fun showFollowButtonCoachMark(followStatus: FollowStatus?) {
        shopPageHeaderFragmentHeaderViewHolder?.showCoachMark(
            followStatus,
            shopId,
            userId
        )
    }

    fun setFollowButtonStatusUsingFollowShopData(followShop: FollowShop) {
        shopFollowButtonUiModel = shopFollowButtonUiModel.copy(
            isFollowing = followShop.isFollowing == true,
            textLabel = followShop.buttonLabel.orEmpty()
        )
        shopPageHeaderFragmentHeaderViewHolder?.updateFollowButton(shopFollowButtonUiModel)
    }

    fun updateShopName(shopName: String) {
        shopPageHeaderFragmentHeaderViewHolder?.updateShopName(shopName)
    }

    fun scrollToTop() {
        (tabFragment as? InterfaceShopPageClickScrollToTop)?.scrollToTop()
    }

    fun setSgcPlayWidgetData() {
        shopPageHeaderFragmentHeaderViewHolder?.setSgcPlaySection(shopPagePageHeaderWidgetList, getShopHeaderConfig())
    }

    fun updateNavToolbarNotification() {
        navToolbar?.updateNotification()
    }

    fun getTabFragment(): Fragment? {
        return tabFragment
    }

    fun getTabData(): ShopPageGetDynamicTabResponse.ShopPageGetDynamicTab.TabData? {
        return tabData
    }

    fun setInitialShopLayoutData(initialShopLayoutData: HomeLayoutData?) {
        this.initialShopLayoutData = initialShopLayoutData
    }
}
