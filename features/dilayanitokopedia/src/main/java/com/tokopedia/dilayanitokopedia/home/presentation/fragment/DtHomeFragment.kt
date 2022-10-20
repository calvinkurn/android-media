package com.tokopedia.dilayanitokopedia.home.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.applink.internal.ApplinkConstInternalDilayaniTokopedia
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.dilayanitokopedia.R
import com.tokopedia.dilayanitokopedia.common.constant.ConstantKey.PARAM_APPLINK_AUTOCOMPLETE
import com.tokopedia.dilayanitokopedia.common.constant.DtLayoutState
import com.tokopedia.dilayanitokopedia.common.util.CustomLinearLayoutManager
import com.tokopedia.dilayanitokopedia.common.util.PageInfo
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
import com.tokopedia.dilayanitokopedia.home.presentation.view.listener.DtDynamicLegoBannerCallback
import com.tokopedia.dilayanitokopedia.home.presentation.view.listener.DtHomeLeftCarouselCallback
import com.tokopedia.dilayanitokopedia.home.presentation.viewmodel.DtHomeViewModel
import com.tokopedia.dilayanitokopedia.home.uimodel.HomeLayoutListUiModel
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.home_component.listener.DynamicLegoBannerListener
import com.tokopedia.home_component.listener.FeaturedShopListener
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.listener.MixLeftComponentListener
import com.tokopedia.home_component.listener.MixTopComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
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
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.lifecycle.autoClearedNullable
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by irpan on 07/09/22.
 */
class DtHomeFragment : Fragment() {

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
                dtView = createDtView(),
                dtChooseAddressWidgetListener = null,
                featuredShopListener = createFeatureShopCallback(),

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
                homeTopComponentListener = createTopComponentCallback(),
                homeTopCarouselListener = createTopCarouselCallback(),
                homeLeftCarouselListener = createLeftCarouselCallback(),
                dynamicLegoBannerCallback = createLegoBannerCallback()
//                playWidgetCoordinator = createPlayWidgetCoordinator()
            ),
            differ = HomeListDiffer()
        )
    }


    private fun createFeatureShopCallback(): FeaturedShopListener {
        return object : FeaturedShopListener {
            override fun onSeeAllClicked(channelModel: ChannelModel, position: Int) {
                TODO("Not yet implemented")
            }

            override fun onSeeAllBannerClicked(channelModel: ChannelModel, applink: String, position: Int) {
                TODO("Not yet implemented")
            }

            override fun onFeaturedShopBannerBackgroundClicked(channel: ChannelModel) {
                TODO("Not yet implemented")
            }

            override fun onFeaturedShopItemImpressed(
                channelModel: ChannelModel,
                channelGrid: ChannelGrid,
                position: Int,
                parentPosition: Int
            ) {
                TODO("Not yet implemented")
            }

            override fun onFeaturedShopItemClicked(
                channelModel: ChannelModel,
                channelGrid: ChannelGrid,
                position: Int,
                parentPosition: Int
            ) {
                TODO("Not yet implemented")
            }

        }
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

        /**
         * Temporary
         * Remove later
         */
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
//                hints = listOf(
//                    HintData(
//                        data.placeholder.orEmpty(),
//                        data.keyword.orEmpty()
//                    )
//                ),
                hints = listOf(
                    HintData(
                        data.placeholder ?: "", data.keyword
                            ?: ""
                    )
                ),
                applink = if (data.keyword?.isEmpty() != false) {
                    ApplinkConstInternalDiscovery.AUTOCOMPLETE
                } else PARAM_APPLINK_AUTOCOMPLETE,
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
//        updateShareHomeData(
//            pageIdConstituents = listOf(PAGE_TYPE_HOME),
//            isScreenShot = false,
//            thumbNailTitle = context?.resources?.getString(R.string.tokopedianow_home_share_thumbnail_title).orEmpty(),
//            linkerType = LinkerData.NOW_TYPE
//        )
//
//        shareClicked(shareHomeTokonow)

        val dummyPageinfo = PageInfo(
            path = "discovery / dilayani - tokopedia",
            name = "Dilayani Tokopedia",
            type = "general",
            searchApplink = "tokopedia://search-autocomplete?hint=Cari%20di%20Dilayani%20Tokopedia&navsource=tokocabang&srp_page_id=45021&srp_page_title=Dilayani%20Tokopedia",
            identifier = "dilayani - tokopedia",
            id = 45021,
//            share = Share(
//                enabled = true,
//                title = "Dilayani Tokopedia | Tokopedia",
//                image = "https://images.tokopedia.net/img/QBrNqa/2022/10/12/facd6ee4-849f-4309-a3c9-69261238929a.png",
//                url = "https://www.tokopedia.com/discovery/dilayani-tokopedia, description=Cek Dilayani Tokopedia! Belanja bebas ongkir dengan harga terbaik hanya di Tokopedia"
//            ),
            campaignCode = "tca00031148_dilayani tokopedia_18march22 -18 march24",
            searchTitle = "Cari di Dilayani Tokopedia",
            showChooseAddress = true,
            tokonowMiniCartActive = false,
//            additionalInfo = AdditionalInfo(category = null, categoryData = null),
            redirectionUrl = null,
            isAdult = 0,
            origin = 0
        )
        showUniversalShareBottomSheet(dummyPageinfo)
    }

    private fun showUniversalShareBottomSheet(data: PageInfo?) {
        data?.let { pageInfo ->
            val universalShareBottomSheet = UniversalShareBottomSheet.createInstance().apply {
                init(bottomSheetShareListener())
                setUtmCampaignData(
                    "Dilayani Tokopedia",
                    if (UserSession(this@DtHomeFragment.requireContext()).userId.isNullOrEmpty()) "0"
                    else UserSession(this@DtHomeFragment.requireContext()).userId,
                    viewModelDtHome.getShareUTM(pageInfo),
                    "share"
                )
//                setMetaData(
//                    pageInfo.share?.title ?: "",
//                    pageInfo.share?.image ?: ""
//                )
//                setOgImageUrl(pageInfo.share?.image ?: "")
            }
            universalShareBottomSheet?.show(fragmentManager, this)
//            shareType = UniversalShareBottomSheet.getShareBottomSheetType()
//            getDiscoveryAnalytics().trackUnifyShare(
//                VIEW_DISCOVERY_IRIS,
//                if (shareType == CUSTOM_SHARE_SHEET) VIEW_UNIFY_SHARE else VIEW_SCREENSHOT_SHARE,
//                getUserID()
//            )
        }
    }

    private fun bottomSheetShareListener(): ShareBottomsheetListener {
        return object : ShareBottomsheetListener {
            override fun onShareOptionClicked(shareModel: ShareModel) {
                TODO("Not yet implemented")
            }

            override fun onCloseOptionClicked() {
                TODO("Not yet implemented")
            }

        }
    }


//    private fun updateShareHomeData(pageIdConstituents: List<String>, isScreenShot: Boolean, thumbNailTitle: String, linkerType: String, id: String = "", url: String = SHARE_HOME_URL) {
//        shareHomeTokonow?.pageIdConstituents = pageIdConstituents
//        shareHomeTokonow?.isScreenShot = isScreenShot
//        shareHomeTokonow?.thumbNailTitle = thumbNailTitle
//        shareHomeTokonow?.linkerType = linkerType
//        shareHomeTokonow?.id = id
//        shareHomeTokonow?.sharingUrl = url
//    }

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

        //additional in DT
//        setupAnchorTabComponent(data)
    }

    private fun observeLiveData() {
        observe(viewModelDtHome.homeLayoutList) {
//            removeAllScrollListener()

            when (it) {
                is Success -> onSuccessGetHomeLayout(it.data)
                is Fail -> Timber.d(it.throwable)
//                is Fail -> onFailedGetHomeLayout(it.throwable)
            }

//            rvHome?.post {
//                addScrollListener()
//                resetSwipeLayout()
//            }
        }

        observe(viewModelDtHome.chooseAddress) {
            when (it) {
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
            viewModelDtHome.getHomeLayout(
                it,
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

    private fun createLeftCarouselCallback(): MixLeftComponentListener {
        return DtHomeLeftCarouselCallback(
            requireContext(),
//            analytics
        )
    }


    private fun createLegoBannerCallback(): DynamicLegoBannerListener? {
        return DtDynamicLegoBannerCallback(
            requireContext(), viewModelDtHome
//            viewModelTokoNow, userSession, analytics
        )

    }

    private fun createDtView(): DtView? {
        return object : DtView {

            override fun getFragmentPage() = this@DtHomeFragment
            override fun getFragmentManagerPage(): FragmentManager = this@DtHomeFragment.childFragmentManager
            override fun refreshLayoutPage() = onRefreshLayout()

            override fun getScrollState(adapterPosition: Int): Parcelable? {
                //TODO -update later
                return null
            }

            override fun saveScrollState(adapterPosition: Int, scrollState: Parcelable?) {
                //TODO -update later
            }

            override fun saveParallaxState(mapParallaxState: Map<String, Float>) {
                TODO("Not yet implemented")
            }

            override fun getParallaxState(): Map<String, Float> {
                TODO("Not yet implemented")
            }

        }
    }

    private fun onRefreshLayout() {
//        refreshMiniCart()
//        resetMovingPosition()
//        removeAllScrollListener()
//        hideStickyLogin()
        rvLayoutManager?.setScrollEnabled(true)
//        carouselScrollState.clear()
//        carouselParallaxState.clear()
//        isRefreshed = true
        loadLayout()
    }

    private fun loadLayout() {
        viewModelDtHome.getLoadingState()
    }

    private fun onLoadingHomeLayout(data: HomeLayoutListUiModel) {
        showHomeLayout(data)
//        loadHeaderBackground()
        checkAddressDataAndServiceArea()
        showLayout()
//        showHideChooseAddress()
//        hideSwitcherCoachMark()
    }


    private fun onSearchBarClick() {
        RouteManager.route(
            context,
            getAutoCompleteApplinkPattern(),
            SOURCE,
            context?.resources?.getString(R.string.dt_search_bar_hint).orEmpty(),
            isFirstInstall().toString()
        )
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


    private fun checkAddressDataAndServiceArea() {
        checkIfChooseAddressWidgetDataUpdated()
        val shopId = localCacheModel?.shop_id.toLongOrZero()
        val warehouseId = localCacheModel?.warehouse_id.toLongOrZero()
        checkStateNotInServiceArea(shopId, warehouseId)
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


//    private fun setupAnchorTabComponent(homeLayoutListUiModel: HomeLayoutListUiModel) {
//        if(anchorViewHolder == null) {
//            val view = layoutInflater.inflate(ComponentsList.AnchorTabs.id, null, false)
//            anchorViewHolder = AnchorTabsViewHolder(view, this)
//            val viewModel =
//                AnchorTabsViewModel(context?.applicationContext as Application, homeLayoutListUiModel, 0)
//            anchorViewHolder?.bindView(viewModel)
//            viewModel.onAttachToViewHolder()
//            anchorViewHolder?.onViewAttachedToWindow()
//        }
//        setupObserveAndShowAnchor()
//    }
//
//    private fun setupObserveAndShowAnchor() {
//        if (!stickyHeaderShowing)
//            anchorViewHolder?.let {
//                if (!it.viewModel.getCarouselItemsListData().hasActiveObservers())
//                    anchorViewHolder?.setUpObservers(viewLifecycleOwner)
//                if (mAnchorHeaderView.findViewById<RecyclerView>(R.id.anchor_rv) == null) {
//                    mAnchorHeaderView.removeAllViews()
//                    (anchorViewHolder?.itemView?.parent as? FrameLayout)?.removeView(
//                        anchorViewHolder?.itemView
//                    )
//                    mAnchorHeaderView.addView(it.itemView)
//                }
//            }
//    }

    private fun createTopComponentCallback(): HomeComponentListener? {
        return object : HomeComponentListener {
            override fun onChannelExpired(channelModel: ChannelModel, channelPosition: Int, visitable: Visitable<*>) {
                if (channelModel.channelConfig.isAutoRefreshAfterExpired) {
//                    homeCategoryListener.getDynamicChannelData(visitable, channelModel, channelPosition)
                } else {
//                    homeCategoryListener.removeViewHolderAtPosition(channelPosition)
                }
            }

        }

    }

    private fun createTopCarouselCallback(): MixTopComponentListener? {
        return object : MixTopComponentListener {
            override fun onMixTopImpressed(channel: ChannelModel, parentPos: Int) {
            }

            override fun onSeeAllBannerClicked(channel: ChannelModel, applink: String) {
                TODO("Not yet implemented")
            }

            override fun onMixtopButtonClicked(channel: ChannelModel) {
                TODO("Not yet implemented")
            }

            override fun onSectionItemClicked(applink: String) {
                TODO("Not yet implemented")
            }

            override fun onBackgroundClicked(channel: ChannelModel) {
                TODO("Not yet implemented")
            }

            override fun onProductCardImpressed(
                channel: ChannelModel,
                channelGrid: ChannelGrid,
                adapterPosition: Int,
                position: Int
            ) {
            }

            override fun onProductCardClicked(
                channel: ChannelModel,
                channelGrid: ChannelGrid,
                adapterPosition: Int,
                position: Int,
                applink: String
            ) {
               onActionLinkClicked(channelGrid.applink)
            }

            override fun onSeeMoreCardClicked(channel: ChannelModel, applink: String) {
                TODO("Not yet implemented")
            }

        }
    }


    private fun onActionLinkClicked(actionLink: String, trackingAttribution: String = "") {
        var mLastClickTime = System.currentTimeMillis()
        val CLICK_TIME_INTERVAL: Long = 500

        val now = System.currentTimeMillis()
        if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
            return
        }
        mLastClickTime = now
        if (TextUtils.isEmpty(actionLink)) {
            return
        }
        if (activity != null
            && RouteManager.isSupportApplink(activity, actionLink)
        ) {
            openApplink(actionLink)
        } else {
//            openWebViewURL(actionLink, activity)
        }
    }

    private fun openApplink(appLink: String) {
        if (appLink.isNotEmpty()) {
            RouteManager.route(context, appLink)
        }
    }

}
