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
import com.tokopedia.kotlin.extensions.view.encodeToUtf8
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
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
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.common.view.HomeMainToolbar
import com.tokopedia.tokomart.home.di.component.DaggerTokoMartHomeComponent
import com.tokopedia.tokomart.home.domain.model.Data
import com.tokopedia.tokomart.home.domain.model.SearchPlaceholder
import com.tokopedia.tokomart.home.presentation.adapter.TokoMartHomeAdapter
import com.tokopedia.tokomart.home.presentation.adapter.TokoMartHomeAdapterTypeFactory
import com.tokopedia.tokomart.home.presentation.adapter.differ.TokoMartHomeListDiffer
import com.tokopedia.tokomart.home.presentation.viewholder.HomeChooseAddressWidgetViewHolder
import com.tokopedia.tokomart.home.presentation.viewmodel.TokoMartHomeViewModel
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_tokomart_home.*
import java.util.*
import java.util.concurrent.Callable
import javax.inject.Inject

class TokoMartHomeFragment: Fragment() {

    companion object {
        private const val TOOLBAR_DARK_TYPE = 0
        private const val TOOLBAR_LIGHT_TYPE = 1
        private const val REMOTE_CONFIG_KEY_FIRST_INSTALL_SEARCH = "android_user_first_install_search"
        private const val REMOTE_CONFIG_KEY_FIRST_DURATION_TRANSITION_SEARCH = "android_user_duration_auto_transition_search"
        private const val KEY_FIRST_INSTALL_SEARCH_TOKONOW = "KEY_FIRST_INSTALL_SEARCH_TOKONOW"
        private const val KEY_FIRST_INSTALL_TIME_SEARCH_TOKONOW = "KEY_IS_FIRST_INSTALL_TIME_SEARCH_TOKONOW"
        private const val AB_TEST_AUTO_TRANSITION_KEY = "auto_transition"
        private const val AUTO_TRANSITION_VARIANT = "auto_transition"
        private const val DEFAULT_INTERVAL_HINT: Long = 1000 * 10
        private const val PARAM_APPLINK_AUTOCOMPLETE = "?navsource={source}&hint={hint}&first_install={first_install}"
        private const val HOME_SOURCE = "home"
        private const val ROLLANCE_EXP_NAME = AbTestPlatform.NAVIGATION_EXP_TOP_NAV
        private const val ROLLANCE_VARIANT_OLD = AbTestPlatform.NAVIGATION_VARIANT_OLD
        private const val ROLLANCE_VARIANT_REVAMP = AbTestPlatform.NAVIGATION_VARIANT_REVAMP

        fun newInstance() = TokoMartHomeFragment()
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModel: TokoMartHomeViewModel

    private val adapter by lazy { TokoMartHomeAdapter(TokoMartHomeAdapterTypeFactory(this), TokoMartHomeListDiffer()) }

    private var oldToolbar: HomeMainToolbar? = null
    private var navToolbar: NavToolbar? = null
    private var statusBarBackground: View? = null
    private var localCacheModel: LocalCacheModel? = null
    private var ivHeaderBackground: ImageView? = null
    private var sharedPrefs: SharedPreferences? = null
    private var searchBarTransitionRange = 0
    private var startToTransitionOffset = 0
    private var isShowFirstInstallSearch = false
    private var durationAutoTransition = DEFAULT_INTERVAL_HINT

    private val homeMainToolbarHeight: Int
        get() {
            var height = resources.getDimensionPixelSize(R.dimen.default_toolbar_status_height)
            context?.let {
                if (!isNavRevamp() && oldToolbar != null) {
                    height = oldToolbar?.height ?: resources.getDimensionPixelSize(R.dimen.default_toolbar_status_height)
                    oldToolbar?.let {
                        if (!it.isShadowApplied()) {
                            height += resources.getDimensionPixelSize(R.dimen.dp_8)
                        }
                    }
                } else {
                    navToolbar?.let {
                        height = navToolbar?.height ?: resources.getDimensionPixelSize(R.dimen.default_toolbar_status_height)
                        height += resources.getDimensionPixelSize(R.dimen.dp_8)
                    }
                }
            }
            return height
        }

    private val afterInflationCallable: Callable<Any?>
        get() = Callable {
            calculateSearchBarView(0)
            null
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchBarTransitionRange = resources.getDimensionPixelSize(R.dimen.home_revamp_searchbar_transition_range)
        startToTransitionOffset = resources.getDimensionPixelOffset(R.dimen.dp_1)

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
        setup()
        setupRecyclerView()
        observeLiveData()
        getHomeLayout()
        getSearchHint()
    }

    override fun onAttach(context: Context) {
        initInjector()
        super.onAttach(context)
    }

    override fun onResume() {
        super.onResume()
        checkIfChooseAddressWidgetDataUpdated()
    }

    private fun initInjector() {
        DaggerTokoMartHomeComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun setup() {
        setupNavToolbar()
        setupStatusBar()
        setupRecyclerView()
        observeLiveData()
        updateCurrentPageLocalCacheModelData()
    }

    private fun setupNavToolbar() {
        ivHeaderBackground = view?.findViewById(R.id.view_background_image)
        navToolbar = view?.findViewById(R.id.navToolbar)
        oldToolbar = view?.findViewById(R.id.toolbar)
        navToolbar?.bringToFront()
        oldToolbar?.bringToFront()
        navAbTestCondition (
                ifNavRevamp = {
                    setupNewNav()
                },
                ifNavOld = {
                    setupOldNav()
                }
        )
    }

    private fun setupNewNav() {
        oldToolbar?.gone()
        navToolbar?.show()
        navToolbar?.let { toolbar ->
            viewLifecycleOwner.lifecycle.addObserver(toolbar)
            rvHome?.addOnScrollListener(NavRecyclerViewScrollListener(
                    navToolbar = toolbar,
                    startTransitionPixel = homeMainToolbarHeight,
                    toolbarTransitionRangePixel = searchBarTransitionRange,
                    navScrollCallback = object : NavRecyclerViewScrollListener.NavScrollCallback {
                        override fun onAlphaChanged(offsetAlpha: Float) { /* nothing to do */
                        }

                        override fun onSwitchToLightToolbar() { /* nothing to do */
                        }

                        override fun onSwitchToDarkToolbar() {
                            navAbTestCondition(
                                    ifNavRevamp = {
                                        navToolbar?.hideShadow()
                                    }
                            )
                        }

                        override fun onYposChanged(yOffset: Int) {
                            ivHeaderBackground?.y = -(yOffset.toFloat())
                        }
                    },
                    fixedIconColor = NavToolbar.Companion.Theme.TOOLBAR_LIGHT_TYPE
            ))
            // set icon top nav
            val icons = IconBuilder(
                    IconBuilderFlag(pageSource = ApplinkConsInternalNavigation.SOURCE_HOME)
            ).addIcon(IconList.ID_SHARE) {}
                    .addIcon(IconList.ID_CART) {}
            toolbar.setIcon(icons)
        }
        activity?.let {
            navToolbar?.setupToolbarWithStatusBar(it)
        }
    }

    private fun setupOldNav() {
        oldToolbar?.setAfterInflationCallable(afterInflationCallable)
        oldToolbar?.show()
        navToolbar?.gone()
    }

    private fun evaluateHomeComponentOnScroll(recyclerView: RecyclerView, dy: Int) {
        if (recyclerView.canScrollVertically(1) || dy != 0) {
            navAbTestCondition(
                    ifNavOld = {
                        if (oldToolbar != null && oldToolbar?.getViewHomeMainToolBar() != null) {
                            oldToolbar?.showShadow()
                        }
                    }, ifNavRevamp = {
                        navToolbar?.showShadow(lineShadow = true)
                    }
            )
        } else {
            navAbTestCondition(
                    ifNavOld = {
                        if (oldToolbar != null && oldToolbar?.getViewHomeMainToolBar() != null) {
                            oldToolbar?.hideShadow()
                        }
                    }, ifNavRevamp = {
                        navToolbar?.hideShadow(lineShadow = true)
                    }
            )
        }
    }

    private fun calculateSearchBarView(offset: Int, fixedIconColor: Int = TOOLBAR_DARK_TYPE) {
        val endTransitionOffset = startToTransitionOffset + searchBarTransitionRange
        val maxTransitionOffset = endTransitionOffset - startToTransitionOffset
        //mapping alpha to be rendered per pixel for x height
        var offsetAlpha = 255f / maxTransitionOffset * (offset - startToTransitionOffset)
        //2.5 is maximum
        if (offsetAlpha < 0) {
            offsetAlpha = 0f
        }
        if (oldToolbar != null && oldToolbar?.getViewHomeMainToolBar() != null) {
            when (fixedIconColor) {
                TOOLBAR_DARK_TYPE -> oldToolbar?.switchToDarkToolbar()
                TOOLBAR_LIGHT_TYPE -> oldToolbar?.switchToLightToolbar()
            }
        }
        if (offsetAlpha >= 255) {
            offsetAlpha = 255f
        }
        if (oldToolbar != null && oldToolbar?.getViewHomeMainToolBar() != null) {
            if (offsetAlpha in 0.0..255.0) {
                oldToolbar?.setBackgroundAlpha(offsetAlpha)
                setStatusBarAlpha(offsetAlpha)
            }
        }
    }

    private fun isNavRevamp(): Boolean {
//        return try {
//            val rollanceNavType = RemoteConfigInstance.getInstance().abTestPlatform.getString(ROLLANCE_EXP_NAME, ROLLANCE_VARIANT_OLD)
//            rollanceNavType.equals(ROLLANCE_VARIANT_REVAMP, ignoreCase = true)
//        } catch (e: java.lang.Exception) {
//            false
//        }
        return false
    }

    private fun getAbTestPlatform(): AbTestPlatform {
        val remoteConfigInstance = RemoteConfigInstance(activity?.application)
        return remoteConfigInstance.abTestPlatform
    }

    private fun navAbTestCondition(ifNavRevamp: () -> Unit = {}, ifNavOld: () -> Unit = {}) {
        if (isNavRevamp()) {
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
            setStatusBarAlpha(0f)
        }
    }

    private fun setStatusBarAlpha(alpha: Float) {
        val drawable = statusBarBackground?.background
        drawable?.alpha = alpha.toInt()
        statusBarBackground?.background = drawable
    }

    private fun setupRecyclerView() {
        with(rvHome) {
            adapter = this@TokoMartHomeFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        rvHome?.setItemViewCacheSize(20)
        rvHome?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                evaluateHomeComponentOnScroll(recyclerView, dy)

                val offset = recyclerView.computeVerticalScrollOffset()
                navAbTestCondition(
                        ifNavOld = {
                            ivHeaderBackground?.y = -(offset.toFloat())
                            calculateSearchBarView(recyclerView.computeVerticalScrollOffset())
                        }
                )
            }
        })
    }

    private fun observeLiveData() {
        observe(viewModel.homeLayoutList) {
            if (it is Success) {
                adapter.submitList(it.data)
                // TO-DO: Lazy Load Data
                viewModel.getLayoutData()
            }
        }

        observe(viewModel.homeLayoutData) {
            if(it is Success) {
                adapter.submitList(it.data)
            }
        }

        observe(viewModel.searchHint) {
            setHint(it)
        }
    }

    private fun getHomeLayout() {
        viewModel.getHomeLayout()
    }

    private fun getSearchHint() {
        viewModel.getSearchHint(isFirstInstall(), userSession.deviceId, userSession.userId)
    }

    private fun checkIfChooseAddressWidgetDataUpdated() {
        localCacheModel?.let {
            context?.apply {
                val isUpdated = ChooseAddressUtils.isLocalizingAddressHasUpdated(
                        this,
                        it
                )
                if (isUpdated) {
                    adapter.updateHomeChooseAddressWidget(true)
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
//        return isRollOutUser && isRemoteConfigChooseAddressWidgetEnabled && !isMyShop
        return true
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
                val sharedPrefs = it.getSharedPreferences(KEY_FIRST_INSTALL_SEARCH_TOKONOW, Context.MODE_PRIVATE)
                var firstInstallCacheValue = sharedPrefs.getLong(KEY_FIRST_INSTALL_TIME_SEARCH_TOKONOW, 0)
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
            sharedPrefs = it.getSharedPreferences(KEY_FIRST_INSTALL_SEARCH_TOKONOW, Context.MODE_PRIVATE)
            sharedPrefs?.edit()?.putLong(KEY_FIRST_INSTALL_TIME_SEARCH_TOKONOW, 0)?.apply()
        }
    }

    private fun setHint(searchPlaceholder: SearchPlaceholder) {
        searchPlaceholder.data?.let { data ->
            navAbTestCondition(
                    ifNavOld = {
                        oldToolbar?.setHint(
                                HintData(data.placeholder.orEmpty(), data.keyword.orEmpty()),
                                placeholderToHint(data),
                                isFirstInstall(),
                                shouldShowTransition(),
                                durationAutoTransition)
                        oldToolbar?.setInboxNumber(30)
                    },
                    ifNavRevamp = {
                        navToolbar?.setupSearchbar(
                                hints = listOf(
                                        HintData(
                                                data.placeholder.orEmpty(),
                                                data.keyword.orEmpty()
                                        )
                                ),
                                applink = if (data.keyword?.isEmpty() != false) ApplinkConstInternalDiscovery.AUTOCOMPLETE else PARAM_APPLINK_AUTOCOMPLETE,
                                searchbarClickCallback = {
                                    RouteManager.route(context,
                                            ApplinkConstInternalDiscovery.AUTOCOMPLETE + PARAM_APPLINK_AUTOCOMPLETE,
                                            HOME_SOURCE,
                                            data.keyword.safeEncodeUtf8(),
                                            isFirstInstall().toString())
                                },
                                searchbarImpressionCallback = {},
                                durationAutoTransition = durationAutoTransition,
                                shouldShowTransition = shouldShowTransition()
                        )
                    }
            )
        }
    }

    private fun placeholderToHint(data: Data): ArrayList<HintData> {
        var hints = arrayListOf(HintData(data.placeholder.orEmpty(), data.keyword.orEmpty()))
        data.placeholders?.let { placeholders ->
            if (placeholders.isNotEmpty()) {
                hints = arrayListOf()
                placeholders.forEach { placeholder ->
                    hints.add(HintData(placeholder.placeholder.orEmpty(), placeholder.keyword.orEmpty()))
                }
            }
        }
        return hints
    }

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