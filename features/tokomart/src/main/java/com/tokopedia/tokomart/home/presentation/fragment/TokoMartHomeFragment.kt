package com.tokopedia.tokomart.home.presentation.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.applink.internal.ApplinkConstInternalTokoMart
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.home_component.listener.BannerComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.kotlin.extensions.view.encodeToUtf8
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.widget.MiniCartWidgetListener
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.helper.ViewHelper
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.NavToolbar.Companion.Theme.TOOLBAR_LIGHT_TYPE
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.searchbar.navigation_component.listener.NavRecyclerViewScrollListener
import com.tokopedia.searchbar.navigation_component.util.NavToolbarExt
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.common.constant.ConstantKey.AB_TEST_AUTO_TRANSITION_KEY
import com.tokopedia.tokomart.common.constant.ConstantKey.AB_TEST_EXP_NAME
import com.tokopedia.tokomart.common.constant.ConstantKey.AB_TEST_VARIANT_OLD
import com.tokopedia.tokomart.common.constant.ConstantKey.PARAM_APPLINK_AUTOCOMPLETE
import com.tokopedia.tokomart.common.constant.ConstantKey.REMOTE_CONFIG_KEY_FIRST_DURATION_TRANSITION_SEARCH
import com.tokopedia.tokomart.common.constant.ConstantKey.REMOTE_CONFIG_KEY_FIRST_INSTALL_SEARCH
import com.tokopedia.tokomart.common.constant.ConstantKey.SHARED_PREFERENCES_KEY_FIRST_INSTALL_SEARCH
import com.tokopedia.tokomart.common.constant.ConstantKey.SHARED_PREFERENCES_KEY_FIRST_INSTALL_TIME_SEARCH
import com.tokopedia.tokomart.common.view.TokoMartHomeView
import com.tokopedia.tokomart.home.constant.TokoNowConstant.SHOP_ID
import com.tokopedia.tokomart.home.di.component.DaggerTokoMartHomeComponent
import com.tokopedia.tokomart.home.domain.model.Data
import com.tokopedia.tokomart.home.domain.model.SearchPlaceholder
import com.tokopedia.tokomart.home.presentation.adapter.TokoMartHomeAdapter
import com.tokopedia.tokomart.home.presentation.adapter.TokoMartHomeAdapterTypeFactory
import com.tokopedia.tokomart.home.presentation.adapter.differ.TokoMartHomeListDiffer
import com.tokopedia.tokomart.home.presentation.uimodel.HomeLayoutListUiModel
import com.tokopedia.tokomart.home.presentation.viewholder.HomeChooseAddressWidgetViewHolder
import com.tokopedia.tokomart.home.presentation.viewholder.HomeTickerViewHolder
import com.tokopedia.tokomart.home.presentation.viewmodel.TokoMartHomeViewModel
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_tokomart_home.*
import java.util.*
import javax.inject.Inject

class TokoMartHomeFragment: Fragment(), TokoMartHomeView, HomeTickerViewHolder.HomeTickerListener,
        MiniCartWidgetListener, BannerComponentListener {

    companion object {
        private const val AUTO_TRANSITION_VARIANT = "auto_transition"
        private const val DEFAULT_INTERVAL_HINT: Long = 1000 * 10
        const val SOURCE = "tokonow"

        fun newInstance() = TokoMartHomeFragment()
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModel: TokoMartHomeViewModel

    private val adapter by lazy { TokoMartHomeAdapter(TokoMartHomeAdapterTypeFactory(this, this, this), TokoMartHomeListDiffer()) }

    private var navToolbar: NavToolbar? = null
    private var statusBarBackground: View? = null
    private var localCacheModel: LocalCacheModel? = null
    private var ivHeaderBackground: ImageView? = null
    private var sharedPrefs: SharedPreferences? = null
    private var isShowFirstInstallSearch = false
    private var durationAutoTransition = DEFAULT_INTERVAL_HINT
    private var isRefreshWidget = false
    private var movingPosition = 0

    private val homeMainToolbarHeight: Int
        get() {
            var height = navToolbar?.height ?: resources.getDimensionPixelSize(R.dimen.default_toolbar_status_height)
            height += resources.getDimensionPixelSize(R.dimen.dp_8)
            return height
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val firebaseRemoteConfig = FirebaseRemoteConfigImpl(activity)
        firebaseRemoteConfig.let {
            isShowFirstInstallSearch = it.getBoolean(REMOTE_CONFIG_KEY_FIRST_INSTALL_SEARCH, false)
            durationAutoTransition = it.getLong(REMOTE_CONFIG_KEY_FIRST_DURATION_TRANSITION_SEARCH, DEFAULT_INTERVAL_HINT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tokomart_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavToolbar()
        setupStatusBar()
        setupRecyclerView()
        observeLiveData()
        updateCurrentPageLocalCacheModelData()

        getHomeLayout()
        getMiniCart()
    }

    override fun onAttach(context: Context) {
        initInjector()
        super.onAttach(context)
    }

    override fun onResume() {
        super.onResume()
        checkIfChooseAddressWidgetDataUpdated()
    }

    override fun getFragment(): Fragment = this

    override fun onTickerDismiss() {
        adapter.removeTickerWidget()
    }

    override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {
    }

    override fun onBannerClickListener(position: Int, channelGrid: ChannelGrid, channelModel: ChannelModel) {
        context?.let {
            RouteManager.route(it, channelGrid.applink)
        }
    }

    override fun isMainViewVisible(): Boolean {
        return true
    }

    override fun onPromoScrolled(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int) {
    }

    override fun onPageDragStateChanged(isDrag: Boolean) {
    }

    override fun onPromoAllClick(channelModel: ChannelModel) {
    }

    override fun isBannerImpressed(id: String): Boolean {
        return true
    }

    override fun onChannelBannerImpressed(channelModel: ChannelModel, parentPosition: Int) {
    }


    private fun initInjector() {
        DaggerTokoMartHomeComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun setupNavToolbar() {
        ivHeaderBackground = view?.findViewById(R.id.view_background_image)
        navToolbar = view?.findViewById(R.id.navToolbar)
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
            setHint(SearchPlaceholder(Data(null, "Cari di TokoNOW!","")))
            rvHome?.addOnScrollListener(NavRecyclerViewScrollListener(
                    navToolbar = toolbar,
                    startTransitionPixel = homeMainToolbarHeight,
                    toolbarTransitionRangePixel = resources.getDimensionPixelSize(R.dimen.home_revamp_searchbar_transition_range),
                    navScrollCallback = object : NavRecyclerViewScrollListener.NavScrollCallback {
                        override fun onAlphaChanged(offsetAlpha: Float) { /* nothing to do */ }
                        override fun onSwitchToLightToolbar() { /* nothing to do */ }
                        override fun onSwitchToDarkToolbar() {
                            navToolbar?.hideShadow()
                        }
                        override fun onYposChanged(yOffset: Int) {}
                    },
                    fixedIconColor = TOOLBAR_LIGHT_TYPE
            ))
            activity?.let {
                toolbar.setupToolbarWithStatusBar(it)
            }
        }
    }

    private fun setIconNewTopNavigation() {
        val icons = IconBuilder(IconBuilderFlag(pageSource = ApplinkConsInternalNavigation.SOURCE_HOME))
                .addIcon(IconList.ID_CART) {}
                .addIcon(IconList.ID_NAV_GLOBAL) {}
        navToolbar?.setIcon(icons)
    }

    private fun setIconOldTopNavigation() {
        val icons = IconBuilder(IconBuilderFlag(pageSource = ApplinkConsInternalNavigation.SOURCE_HOME))
                .addIcon(IconList.ID_CART) {}
        navToolbar?.setIcon(icons)
    }

    private fun evaluateHomeComponentOnScroll(recyclerView: RecyclerView, dy: Int) {
        movingPosition += dy
        ivHeaderBackground?.y = -(movingPosition.toFloat())
        if (recyclerView.canScrollVertically(1) || movingPosition != 0) {
            navToolbar?.showShadow(lineShadow = true)
        } else {
            navToolbar?.hideShadow(lineShadow = true)
        }
    }

    private fun isNavOld(): Boolean {
        return try {
            getAbTestPlatform().getString(AB_TEST_EXP_NAME, AB_TEST_VARIANT_OLD) == AB_TEST_VARIANT_OLD
        } catch (e: Exception) {
            e.printStackTrace()
            true
        }
    }

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
        statusBarBackground = view?.findViewById(R.id.status_bar_bg)
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
        with(rvHome) {
            adapter = this@TokoMartHomeFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        context?.let {
            rvHome?.setPadding(0, NavToolbarExt.getFullToolbarHeight(it) - 8, 0, 0)
            rvHome?.setItemViewCacheSize(20)
            rvHome?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    evaluateHomeComponentOnScroll(recyclerView, dy)
                }
            })
        }
    }

    private fun observeLiveData() {
        observe(viewModel.homeLayoutList) {
            if (it is Success) {
                loadHomeLayout(it.data)
            }
        }

        observe(viewModel.miniCart) {
            if(it is Success) {
                setupMiniCart(it.data)
            }
        }
    }

    private fun setupMiniCart(data: MiniCartSimplifiedData) {
        if(data.isShowMiniCartWidget) {
            val shopIds = listOf(SHOP_ID)
            miniCartWidget.initialize(shopIds, this, this)
            miniCartWidget.show()
        } else {
            miniCartWidget.hide()
        }
    }

    private fun loadHomeLayout(data: HomeLayoutListUiModel) {
        data.run {
            if (isInitialLoad) {
                adapter.submitList(result)
                // TO-DO: Lazy Load Data
                viewModel.getLayoutData()

                // isMyShop needs shopId to differentiate
                if (!isChooseAddressWidgetShowed(false))
                    adapter.removeHomeChooseAddressWidget()
            } else {
                adapter.submitList(result)
            }
        }
    }

    private fun getHomeLayout() {
        viewModel.getHomeLayout()
    }

    private fun getMiniCart()  {
        viewModel.getMiniCart()
    }

    //  because searchHint has not been discussed so for current situation we only use hardcoded placeholder
    private fun getSearchHint() {
        viewModel.getKeywordSearch(isFirstInstall(), userSession.deviceId, userSession.userId)
    }

    private fun checkIfChooseAddressWidgetDataUpdated() {
        localCacheModel?.let {
            context?.apply {
                val isUpdated = ChooseAddressUtils.isLocalizingAddressHasUpdated(
                        this,
                        it
                )
                if (isUpdated) {
                    isRefreshWidget = !isRefreshWidget
                    adapter.updateHomeChooseAddressWidget(isRefreshWidget)
                }
            }
        }
    }

    private fun isChooseAddressWidgetShowed(isMyShop: Boolean): Boolean {
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        val isRollOutUser = ChooseAddressUtils.isRollOutUser(context)
        val isRemoteConfigChooseAddressWidgetEnabled = remoteConfig.getBoolean(
                HomeChooseAddressWidgetViewHolder.ENABLE_CHOOSE_ADDRESS_WIDGET,
                true
        )
        return isRollOutUser && isRemoteConfigChooseAddressWidgetEnabled && !isMyShop
    }

    private fun updateCurrentPageLocalCacheModelData() {
        localCacheModel = getWidgetUserAddressLocalData(context)
    }

    private fun getWidgetUserAddressLocalData(context: Context?): LocalCacheModel? {
        return context?.let{
            ChooseAddressUtils.getLocalizingAddressData(it)
        }
    }

    private fun isFirstInstall(): Boolean {
        context?.let {
            if (!userSession.isLoggedIn && isShowFirstInstallSearch) {
                val sharedPrefs = it.getSharedPreferences(SHARED_PREFERENCES_KEY_FIRST_INSTALL_SEARCH, Context.MODE_PRIVATE)
                var firstInstallCacheValue = sharedPrefs.getLong(SHARED_PREFERENCES_KEY_FIRST_INSTALL_TIME_SEARCH, 0)
                if (firstInstallCacheValue == 0L) return false
                firstInstallCacheValue += (30 * 60000).toLong()
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
                    searchbarClickCallback = {
                        RouteManager.route(context,
                                getAutoCompleteApplinkPattern(),
                                SOURCE,
                                data.keyword.safeEncodeUtf8(),
                                isFirstInstall().toString())
                    },
                    searchbarImpressionCallback = {},
                    durationAutoTransition = durationAutoTransition,
                    shouldShowTransition = shouldShowTransition()
            )
        }
    }

    private fun getAutoCompleteApplinkPattern() =
            ApplinkConstInternalDiscovery.AUTOCOMPLETE +
                    PARAM_APPLINK_AUTOCOMPLETE +
                    "&" + getParamTokonowSRP()

    private fun getParamTokonowSRP() =
            "${SearchApiConst.BASE_SRP_APPLINK}=${ApplinkConstInternalTokoMart.SEARCH}"

    private fun shouldShowTransition(): Boolean {
        val abTestValue = getAbTestPlatform().getString(AB_TEST_AUTO_TRANSITION_KEY, "")
        return abTestValue == AUTO_TRANSITION_VARIANT
    }

    private fun String?.safeEncodeUtf8(): String {
        return try {
            this?.encodeToUtf8().orEmpty()
        } catch (throwable: Throwable) {
            ""
        }
    }


}