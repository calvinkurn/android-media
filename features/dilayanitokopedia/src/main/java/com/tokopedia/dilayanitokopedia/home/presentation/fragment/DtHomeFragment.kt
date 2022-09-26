package com.tokopedia.dilayanitokopedia.home.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.applink.internal.ApplinkConstInternalDilayaniTokopedia
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.dilayanitokopedia.R
import com.tokopedia.dilayanitokopedia.common.constant.ConstantKey.PARAM_APPLINK_AUTOCOMPLETE
import com.tokopedia.dilayanitokopedia.common.constant.DtLayoutState
import com.tokopedia.dilayanitokopedia.common.util.CustomLinearLayoutManager
import com.tokopedia.dilayanitokopedia.common.view.DtView
import com.tokopedia.dilayanitokopedia.databinding.FragmentDtHomeBinding
import com.tokopedia.dilayanitokopedia.home.constant.HomeStaticLayoutId
import com.tokopedia.dilayanitokopedia.home.constant.HomeStaticLayoutId.Companion.EMPTY_STATE_OUT_OF_COVERAGE
import com.tokopedia.dilayanitokopedia.home.di.component.DaggerHomeComponent
import com.tokopedia.dilayanitokopedia.home.domain.model.Data
import com.tokopedia.dilayanitokopedia.home.domain.model.SearchPlaceholder
import com.tokopedia.dilayanitokopedia.home.presentation.adapter.DtHomeAdapter
import com.tokopedia.dilayanitokopedia.home.presentation.adapter.DtHomeAdapterTypeFactory
import com.tokopedia.dilayanitokopedia.home.presentation.adapter.differ.HomeListDiffer
import com.tokopedia.dilayanitokopedia.home.presentation.viewmodel.DtHomeViewModel
import com.tokopedia.dilayanitokopedia.home.uimodel.HomeLayoutListUiModel
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.domain.mapper.TokonowWarehouseMapper
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by irpan on 07/09/22.
 */
class DtHomeFragment : Fragment(), DtView {

    companion object {
        const val SOURCE = "dilayanitokopedia"
        const val SOURCE_TRACKING = "tokonow page"

    }

    @Inject
    lateinit var viewModelDtHome: DtHomeViewModel


    private var navToolbar: NavToolbar? = null
    private var statusBarBackground: View? = null
    private var rvHome: RecyclerView? = null

    private var rvLayoutManager: CustomLinearLayoutManager? = null


    private var localCacheModel: LocalCacheModel? = null


    private val adapter by lazy {
        DtHomeAdapter(
            typeFactory = DtHomeAdapterTypeFactory(
                dtView = this
//                homeTickerListener = this,
//                tokoNowChooseAddressWidgetListener = this,
//                tokoNowCategoryGridListener = this,
//                bannerComponentListener = createSlideBannerCallback(),
//                homeProductRecomListener = this,
//                tokoNowProductCardListener = this,
//                homeSharingEducationListener = this,
//                homeEducationalInformationListener = this,
//                serverErrorListener = this,
//                tokoNowEmptyStateOocListener = createTokoNowEmptyStateOocListener(),
//                homeQuestSequenceWidgetListener = createQuestWidgetCallback(),
//                dynamicLegoBannerCallback = createLegoBannerCallback(),
//                homeSwitcherListener = createHomeSwitcherListener(),
//                homeLeftCarouselAtcListener = createLeftCarouselAtcCallback(),
//                homeLeftCarouselListener = createLeftCarouselCallback(),
//                playWidgetCoordinator = createPlayWidgetCoordinator()
            ),
            differ = HomeListDiffer()
        )
    }

    override fun onAttach(context: Context) {
        initInjector()
        super.onAttach(context)
    }

    private fun initInjector() {
        DaggerHomeComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }
    private var binding by autoClearedNullable<FragmentDtHomeBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDtHomeBinding.inflate(inflater, container, false)
        return binding?.root as View
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUiVariable()
        initNavToolbar()
        initRecyclerView()
        updateCurrentPageLocalCacheModelData()

        observeLiveData()
        switchServiceOrLoadLayout()

//        /**
//         * Temporary
//         * Remove later
//         */
        showLayout()
    }

    private fun initUiVariable() {
        view?.apply {
//            ivHeaderBackground = binding?.viewBackgroundImage
            navToolbar = binding?.dtHomeNavToolbar
            statusBarBackground = binding?.dtHomeStatusBarBackground
            rvHome = binding?.rvHome
//            swipeLayout = binding?.swipeRefreshLayout

        }
    }

    private fun initNavToolbar() {
        setupTopNavigation()
        setIconNewTopNavigation()
    }

    private fun setIconNewTopNavigation() {
        val icons = IconBuilder(IconBuilderFlag(pageSource = ApplinkConsInternalNavigation.SOURCE_HOME))
            .addIcon(IconList.ID_SHARE, onClick = ::onClickShareButton, disableDefaultGtmTracker = true)
            .addIcon(IconList.ID_CART, onClick = ::onClickCartButton)
            .addIcon(IconList.ID_NAV_GLOBAL) {}
        navToolbar?.setIcon(icons)
    }

    private fun setupTopNavigation() {
        navToolbar?.let { toolbar ->
            viewLifecycleOwner.lifecycle.addObserver(toolbar)
            initHint(SearchPlaceholder(Data(null, context?.resources?.getString(R.string.dt_search_bar_hint).orEmpty(), "")))
            addNavBarScrollListener()
            activity?.let {
                toolbar.setupToolbarWithStatusBar(it)
                toolbar.setToolbarTitle(getString(R.string.dt_home_title))
            }
        }
    }

    private fun initHint(searchPlaceholder: SearchPlaceholder) {
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
//                durationAutoTransition = durationAutoTransition,
//                shouldShowTransition = shouldShowTransition()
            )
        }
    }

    private fun onClickCartButton() {
    }

    private fun onClickShareButton() {

    }

    private fun addNavBarScrollListener() {
//        navBarScrollListener?.let {
//            rvHome?.addOnScrollListener(it)
//        }
    }



    private fun isFirstInstall(): Boolean {
//        context?.let {
//            if (!userSession.isLoggedIn && isShowFirstInstallSearch) {
//                val sharedPrefs = it.getSharedPreferences(SHARED_PREFERENCES_KEY_FIRST_INSTALL_SEARCH, Context.MODE_PRIVATE)
//                var firstInstallCacheValue = sharedPrefs.getLong(SHARED_PREFERENCES_KEY_FIRST_INSTALL_TIME_SEARCH, 0)
//                if (firstInstallCacheValue == 0L) return false
//                firstInstallCacheValue += FIRST_INSTALL_CACHE_VALUE
//                val now = Date()
//                val firstInstallTime = Date(firstInstallCacheValue)
//                return if (now <= firstInstallTime) {
//                    true
//                } else {
//                    saveFirstInstallTime()
//                    false
//                }
//            } else {
//                return false
//            }
//        }
        return false
    }


    private fun getParamDtSRP() = "${SearchApiConst.BASE_SRP_APPLINK}=${ApplinkConstInternalDilayaniTokopedia.SEARCH}"


    private fun showHomeLayout(data: HomeLayoutListUiModel) {
        rvHome?.post {
            Timber.d("HomeLayoutt ${data.items}")
            adapter.submitList(data.items)
        }
    }

    private fun showEmptyState(@HomeStaticLayoutId id: String) {
        localCacheModel?.service_type?.let { serviceType ->
            if (id != EMPTY_STATE_OUT_OF_COVERAGE) {
//                rvLayoutManager?.setScrollEnabled(false)
                viewModelDtHome.getEmptyState(id, serviceType)
            } else {
                viewModelDtHome.getEmptyState(id, serviceType)
//                viewModelDtHome.getProductRecomOoc()
            }

//            miniCartWidgetget?.hide()
//            miniCartWidget?.hideCoachMark()
//            setToolbarTypeTitle()
//            setupPadding(false)
        }
    }

    private fun updateCurrentPageLocalCacheModelData() {
        context?.let {
            localCacheModel = ChooseAddressUtils.getLocalizingAddressData(it)
        }
    }

    private fun onSuccessGetHomeLayout(data: HomeLayoutListUiModel) {
        when (data.state) {
            DtLayoutState.SHOW -> onShowHomeLayout(data)
//            DtLayoutState.HIDE -> onHideHomeLayout(data)
            DtLayoutState.LOADING -> onLoadingHomeLayout(data)
            else -> showHomeLayout(data)
        }
    }

    private fun onShowHomeLayout(data: HomeLayoutListUiModel) {
//        startRenderPerformanceMonitoring()
        showHomeLayout(data)
//        showHeaderBackground()
//        stickyLoginLoadContent()
//        showOnBoarding()
//        getLayoutComponentData()
//        stopRenderPerformanceMonitoring()
    }

    private fun observeLiveData() {
        observe(viewModelDtHome.homeLayoutList) {
//            removeAllScrollListener()

            when (it) {
                is Success -> onSuccessGetHomeLayout(it.data)
//                is Fail -> onFailedGetHomeLayout(it.throwable)
            }

//            rvHome?.post {
//                addScrollListener()
//                resetSwipeLayout()
//            }
        }

        observe(viewModelDtHome.chooseAddress) {
            when(it) {
                is Success -> {
                    setupChooseAddress(it.data)
                }
                is Fail -> {
                    showEmptyStateNoAddress()
//                    logChooseAddressError(it.throwable)
                }
            }
        }
    }

    private fun getHomeLayout() {
        localCacheModel?.let {
//            val removeAbleWidgets = listOf(
//                HomeRemoveAbleWidget(SHARING_EDUCATION, SharedPreferencesUtil.isSharingEducationRemoved(activity)),
//                HomeRemoveAbleWidget(MAIN_QUEST, SharedPreferencesUtil.isQuestAllClaimedRemoved(activity))
//            )
            viewModelDtHome.getHomeLayout(it,
//                removeAbleWidgets
            )
        }
    }


    private fun showLayout() {
        getHomeLayout()
        navToolbar?.setToolbarContentType(NavToolbar.Companion.ContentType.TOOLBAR_TYPE_SEARCH)
    }

    private fun initRecyclerView() {
        context?.let {
            rvHome?.apply {
                adapter = this@DtHomeFragment.adapter
                rvLayoutManager = CustomLinearLayoutManager(it)
                layoutManager = rvLayoutManager
            }

//            rvHome?.setItemViewCacheSize(ITEM_VIEW_CACHE_SIZE)
//            addHomeComponentScrollListener()
        }
    }

    private fun onSearchBarClick() {
        RouteManager.route(context,
            getAutoCompleteApplinkPattern(),
            SOURCE,
            context?.resources?.getString(R.string.dt_search_bar_hint).orEmpty(),
            isFirstInstall().toString())
    }


    private fun getAutoCompleteApplinkPattern() =
        ApplinkConstInternalDiscovery.AUTOCOMPLETE + PARAM_APPLINK_AUTOCOMPLETE + "&" + getParamDtSRP()



    private fun switchServiceOrLoadLayout() {
        localCacheModel?.apply {
//            viewModelDtHome.switchServiceOrLoadLayout(
//                externalServiceType = externalServiceType,
//                localCacheModel = this
//            )
        }
    }

    private fun onLoadingHomeLayout(data: HomeLayoutListUiModel) {
        showHomeLayout(data)
//        loadHeaderBackground()
        checkAddressDataAndServiceArea()
//        showHideChooseAddress()
//        hideSwitcherCoachMark()
    }

    private fun checkAddressDataAndServiceArea() {
        checkIfChooseAddressWidgetDataUpdated()
        val shopId = localCacheModel?.shop_id.toLongOrZero()
        val warehouseId = localCacheModel?.warehouse_id.toLongOrZero()
        checkStateNotInServiceArea(shopId,warehouseId)
    }

    private fun checkStateNotInServiceArea(shopId: Long = -1L, warehouseId: Long) {
//        context?.let {
//            when {
//                shopId == 0L -> {
//                    viewModelDtHome.getChooseAddress(SOURCE)
//                }
//                warehouseId == 0L -> {
////                    showEmptyStateNoAddress()
//                }
//                else -> {
                    showLayout()
////                    viewModelTokoNow.trackOpeningScreen(HOMEPAGE_TOKONOW)
//                }
//            }
//        }
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


    /**
     * DtView
     */
    override fun getFragmentPage()  = this


    override fun getFragmentManagerPage()= childFragmentManager

    override fun refreshLayoutPage() {
        onRefreshLayout()
    }

    override fun getScrollState(adapterPosition: Int): Parcelable? {
        TODO("Not yet implemented")
    }

    override fun saveScrollState(adapterPosition: Int, scrollState: Parcelable?) {
        TODO("Not yet implemented")
    }

    override fun saveParallaxState(mapParallaxState: Map<String, Float>) {
        TODO("Not yet implemented")
    }

    override fun getParallaxState(): Map<String, Float> {
        TODO("Not yet implemented")
    }

    /*
    end DtView
     */

    private fun onRefreshLayout() {
//        refreshMiniCart()
//        resetMovingPosition()
//        removeAllScrollListener()
//        hideStickyLogin()
//        rvLayoutManager?.setScrollEnabled(true)
//        carouselScrollState.clear()
//        carouselParallaxState.clear()
//        isRefreshed = true
        loadLayout()
    }

    private fun loadLayout() {
        viewModelDtHome.getLoadingState()
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

    private fun showEmptyStateNoAddress() {
//        if (localCacheModel?.city_id?.isBlank() == true && localCacheModel?.district_id?.isBlank() == true) {
//            showEmptyState(EMPTY_STATE_NO_ADDRESS_AND_LOCAL_CACHE)
//        } else {
//            viewModelTokoNow.trackOpeningScreen(SCREEN_NAME_TOKONOW_OOC + HOMEPAGE_TOKONOW)
            showEmptyState(EMPTY_STATE_OUT_OF_COVERAGE)
//        }
    }
}