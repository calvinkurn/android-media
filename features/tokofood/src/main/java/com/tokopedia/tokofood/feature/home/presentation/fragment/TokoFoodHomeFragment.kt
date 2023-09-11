package com.tokopedia.tokofood.feature.home.presentation.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseMultiFragment
import com.tokopedia.abstraction.base.view.fragment.enums.BaseMultiFragmentLaunchMode
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic.PARAM_SOURCE
import com.tokopedia.applink.internal.ApplinkConstInternalTokoFood
import com.tokopedia.applink.tokofood.DeeplinkMapperTokoFood
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.model.LinkerData.FOOD_TYPE
import com.tokopedia.localizationchooseaddress.domain.mapper.TokonowWarehouseMapper
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressBottomSheet
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressBottomSheet.Companion.SCREEN_NAME_CHOOSE_ADDRESS_NEW_USER
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.logisticCommon.data.constant.AddEditAddressSource
import com.tokopedia.logisticCommon.data.constant.AddressConstant
import com.tokopedia.logisticCommon.data.constant.LogisticConstant
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.navigation_component.NavSource
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.searchbar.navigation_component.util.NavToolbarExt
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.common.domain.response.CartGeneralCartListData
import com.tokopedia.tokofood.common.domain.response.Merchant
import com.tokopedia.tokofood.common.minicartwidget.view.TokoFoodMiniCartWidget
import com.tokopedia.tokofood.common.presentation.UiEvent
import com.tokopedia.tokofood.common.presentation.adapter.viewholder.TokoFoodErrorStateViewHolder
import com.tokopedia.tokofood.common.presentation.listener.HasViewModel
import com.tokopedia.tokofood.common.presentation.listener.TokofoodScrollChangedListener
import com.tokopedia.tokofood.common.presentation.view.BaseTokofoodActivity
import com.tokopedia.tokofood.common.presentation.viewmodel.MultipleFragmentsViewModel
import com.tokopedia.tokofood.common.util.TokofoodErrorLogger
import com.tokopedia.tokofood.common.util.TokofoodRouteManager
import com.tokopedia.tokofood.databinding.FragmentTokofoodHomeBinding
import com.tokopedia.tokofood.feature.home.analytics.TokoFoodHomeAnalytics
import com.tokopedia.tokofood.feature.home.analytics.TokoFoodHomeCategoryCommonAnalytics
import com.tokopedia.tokofood.feature.home.analytics.TokoFoodHomePageLoadTimeMonitoring
import com.tokopedia.tokofood.feature.home.di.DaggerTokoFoodHomeComponent
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodLayoutState
import com.tokopedia.tokofood.feature.home.domain.data.DynamicIcon
import com.tokopedia.tokofood.feature.home.domain.data.USPResponse
import com.tokopedia.tokofood.feature.home.presentation.adapter.CustomLinearLayoutManager
import com.tokopedia.tokofood.feature.home.presentation.adapter.TokoFoodHomeAdapter
import com.tokopedia.tokofood.feature.home.presentation.adapter.TokoFoodHomeAdapterTypeFactory
import com.tokopedia.tokofood.feature.home.presentation.adapter.TokoFoodListDiffer
import com.tokopedia.tokofood.feature.home.presentation.adapter.viewholder.TokoFoodHomeChooseAddressViewHolder
import com.tokopedia.tokofood.feature.home.presentation.adapter.viewholder.TokoFoodHomeEmptyStateLocationViewHolder
import com.tokopedia.tokofood.feature.home.presentation.adapter.viewholder.TokoFoodHomeIconsViewHolder
import com.tokopedia.tokofood.feature.home.presentation.adapter.viewholder.TokoFoodHomeTickerViewHolder
import com.tokopedia.tokofood.feature.home.presentation.adapter.viewholder.TokoFoodHomeUSPViewHolder
import com.tokopedia.tokofood.feature.home.presentation.adapter.viewholder.TokoFoodMerchantListViewHolder
import com.tokopedia.tokofood.feature.home.presentation.bottomsheet.TokoFoodUSPBottomSheet
import com.tokopedia.tokofood.feature.home.presentation.share.TokoFoodHomeShare
import com.tokopedia.tokofood.feature.home.presentation.share.TokoFoodUniversalShareUtil.shareOptionRequest
import com.tokopedia.tokofood.feature.home.presentation.share.TokoFoodUniversalShareUtil.shareRequest
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodListUiModel
import com.tokopedia.tokofood.feature.home.presentation.view.listener.TokoFoodHomeBannerComponentCallback
import com.tokopedia.tokofood.feature.home.presentation.view.listener.TokoFoodHomeCategoryWidgetV2ComponentCallback
import com.tokopedia.tokofood.feature.home.presentation.view.listener.TokoFoodHomeLegoComponentCallback
import com.tokopedia.tokofood.feature.home.presentation.view.listener.TokoFoodView
import com.tokopedia.tokofood.feature.home.presentation.viewmodel.TokoFoodHomeViewModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.TokoFoodPurchaseFragment
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class TokoFoodHomeFragment :
    BaseMultiFragment(),
    TokoFoodView,
    TokoFoodHomeUSPViewHolder.TokoFoodUSPListener,
    TokoFoodHomeChooseAddressViewHolder.TokoFoodChooseAddressWidgetListener,
    TokoFoodHomeEmptyStateLocationViewHolder.TokoFoodHomeEmptyStateLocationListener,
    TokoFoodHomeIconsViewHolder.TokoFoodHomeIconsListener,
    TokoFoodMerchantListViewHolder.TokoFoodMerchantListListener,
    TokoFoodHomeTickerViewHolder.TokoFoodHomeTickerListener,
    TokoFoodErrorStateViewHolder.TokoFoodErrorStateListener,
    ChooseAddressBottomSheet.ChooseAddressBottomSheetListener,
    ShareBottomsheetListener,
    TokofoodScrollChangedListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var analytics: TokoFoodHomeAnalytics

    @Inject
    lateinit var trackingQueue: TrackingQueue

    private var binding by autoClearedNullable<FragmentTokofoodHomeBinding>()
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(TokoFoodHomeViewModel::class.java)
    }
    private var parentActivity: HasViewModel<MultipleFragmentsViewModel>? = null
    private val activityViewModel: MultipleFragmentsViewModel?
        get() = parentActivity?.viewModel()
    private val adapter by lazy {
        TokoFoodHomeAdapter(
            typeFactory = TokoFoodHomeAdapterTypeFactory(
                this,
                dynamicLegoBannerCallback = createLegoBannerCallback(),
                bannerComponentCallback = createBannerCallback(),
                categoryWidgetCallback = createCategoryWidgetCallback(),
                uspListener = this,
                chooseAddressWidgetListener = this,
                emptyStateLocationListener = this,
                homeIconListener = this,
                merchantListListener = this,
                tickerListener = this,
                errorStateListener = this,
                tokofoodScrollChangedListener = this
            ),
            differ = TokoFoodListDiffer()
        )
    }
    private val loadMoreListener by lazy { createLoadMoreListener() }

    private var collectJob: Job? = null

    companion object {
        private const val HEIGHT_DIVIDER = 4
        private const val ITEM_VIEW_CACHE_SIZE = 20
        private const val REQUEST_CODE_SET_PINPOINT = 112
        private const val REQUEST_CODE_ADD_ADDRESS = 113
        private const val NEW_ADDRESS_PARCELABLE = "EXTRA_ADDRESS_NEW"
        private const val TOTO_LATITUDE = "-6.2216771"
        private const val TOTO_LONGITUDE = "106.8184023"
        private const val MINI_CART_SOURCE = "home_page"
        private const val PAGE_SHARE_NAME = "TokoFood"
        private const val SHARE = "share"
        private const val PAGE_TYPE_HOME = "home"
        private const val SHARE_URL = "https://www.tokopedia.com/gofood"
        private const val SHARE_DEEPLINK = "tokopedia://food/home"
        private const val THUMBNAIL_IMAGE_SHARE_URL =
            "https://images.tokopedia.net/img/tokofood/gofood.png"
        private const val OG_IMAGE_SHARE_URL =
            "https://images.tokopedia.net/img/gofood_home_og_image.jpg"
        const val SOURCE = "tokofood"

        fun createInstance(): TokoFoodHomeFragment {
            return TokoFoodHomeFragment()
        }
    }

    private var jumpToTopView: View? = null
    private var navToolbar: NavToolbar? = null
    private var rvHome: RecyclerView? = null
    private var swipeLayout: SwipeRefreshLayout? = null
    private var rvLayoutManager: CustomLinearLayoutManager? = null
    private var miniCartHome: TokoFoodMiniCartWidget? = null
    private var searchCoachMark: CoachMark2? = null
    private var universalShareBottomSheet: UniversalShareBottomSheet? = null
    private var shareHomeTokoFood: TokoFoodHomeShare? = null
    private var localCacheModel: LocalCacheModel? = null
    private var pageLoadTimeMonitoring: TokoFoodHomePageLoadTimeMonitoring? = null
    private var isShowMiniCart = false
    private var isBackFromOtherPage = false
    private var totalScrolled = 0
    private var onScrollChangedListenerList = mutableListOf<ViewTreeObserver.OnScrollChangedListener>()
    private val spaceZero: Int
        get() = context?.resources?.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_0)
            ?.toInt() ?: 0

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        activity?.let {
            DaggerTokoFoodHomeComponent
                .builder()
                .baseAppComponent((it.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
        }
    }

    override fun onAttachActivity(context: Context?) {
        super.onAttachActivity(context)
        parentActivity = activity as? HasViewModel<MultipleFragmentsViewModel>
    }

    override fun getFragmentTitle(): String? {
        return null
    }

    override fun getFragmentToolbar(): Toolbar? = null

    override fun getLaunchMode(): BaseMultiFragmentLaunchMode {
        return BaseMultiFragmentLaunchMode.SINGLE_TOP
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initPerformanceMonitoring()
        super.onCreate(savedInstanceState)
        shareHomeTokoFood = createShareHome()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTokofoodHomeBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setupNavToolbar()
        setupRecycleView()
        setupSwipeRefreshLayout()
        observeLiveData()
        updateCurrentPageLocalCacheModelData()
        loadLayout()
    }

    override fun onStart() {
        super.onStart()
        initializeMiniCartHome()
        collectValue()
    }

    override fun onResume() {
        super.onResume()
        if (isChooseAddressWidgetDataUpdated()) {
            onRefreshLayout()
        }
    }

    override fun onPause() {
        searchCoachMark?.dismissCoachMark()
        super.onPause()
    }

    override fun onStop() {
        collectJob?.cancel()
        isBackFromOtherPage = true
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        removeScrollChangedListener()
        removeSearchCoachMark()
    }

    override fun getFragmentPage(): Fragment = this

    override fun getFragmentManagerPage(): FragmentManager = childFragmentManager

    override fun refreshLayoutPage() = onRefreshLayout()

    override fun onUSPClicked(uspResponse: USPResponse) {
        showUSPBottomSheet(uspResponse)
    }

    override fun onClickChooseAddressWidgetTracker() {
        analytics.clickLCAWidget(userSession.userId, localCacheModel?.district_id)
    }

    override fun onClickSetPinPoin(errorState: String, title: String, desc: String) {
        onShowEmptyState(errorState, title, desc)
        navigateToSetPinpoint()
    }

    override fun onClickBackToHome() {
        RouteManager.route(context, ApplinkConst.HOME)
    }

    override fun onClickSetAddress(errorState: String, title: String, desc: String) {
        onShowEmptyState(errorState, title, desc)
        navigateAddAddress()
    }

    override fun onClickSetAddressInCoverage(errorState: String, title: String, desc: String) {
        onShowEmptyState(errorState, title, desc)
        showChooseAddressBottomSheet()
    }

    override fun onAddressDataChanged() {
        refreshLayoutPage()
    }

    override fun getLocalizingAddressHostSourceBottomSheet(): String {
        return SOURCE
    }

    override fun onDismissChooseAddressBottomSheet() {}

    override fun onLocalizingAddressLoginSuccessBottomSheet() {}

    override fun onLocalizingAddressServerDown() {}

    override fun onClickHomeIcon(
        applink: String,
        data: List<DynamicIcon>,
        horizontalPosition: Int,
        verticalPosition: Int
    ) {
        analytics.clickIconWidget(
            userSession.userId,
            localCacheModel?.district_id,
            data,
            horizontalPosition,
            verticalPosition
        )
        TokofoodRouteManager.routePrioritizeInternal(context, applink)
    }

    override fun onImpressHomeIcon(data: List<DynamicIcon>, verticalPosition: Int) {
        analytics.impressionIconWidget(
            userSession.userId,
            localCacheModel?.district_id,
            data,
            verticalPosition
        )
    }

    override fun onClickMerchant(merchant: Merchant, horizontalPosition: Int) {
        analytics.clickMerchant(
            userSession.userId,
            localCacheModel?.district_id,
            merchant,
            horizontalPosition
        )
        val merchantPageUri = Uri.parse(ApplinkConstInternalTokoFood.MERCHANT)
            .buildUpon()
            .appendQueryParameter(DeeplinkMapperTokoFood.PARAM_MERCHANT_ID, merchant.id)
            .build()
        TokofoodRouteManager.routePrioritizeInternal(context, merchantPageUri.toString())
    }

    override fun onImpressMerchant(merchant: Merchant, horizontalPosition: Int) {
        trackingQueue.putEETracking(
            TokoFoodHomeCategoryCommonAnalytics.impressMerchant(
                userSession.userId,
                localCacheModel?.district_id,
                merchant,
                horizontalPosition,
                isHome = true
            ) as HashMap<String, Any>
        )
    }

    override fun onTickerDismissed(id: String) {
        viewModel.setRemoveTicker(id)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_ADD_ADDRESS -> onResultFromAddAddress(resultCode, data)
            REQUEST_CODE_SET_PINPOINT -> onResultFromSetPinpoint(resultCode, data)
        }
    }

    override fun onClickRetryError() {
        loadLayout()
    }

    override fun onCloseOptionClicked() {}

    override fun onShareOptionClicked(shareModel: ShareModel) {
        shareOptionRequest(
            shareModel = shareModel,
            shareHomeTokoFood = shareHomeTokoFood,
            activity = activity,
            view = view,
            onSuccess = {
                universalShareBottomSheet?.dismiss()
            }
        )
    }

    override fun viewNoPinPoin() {
        onShowNoPinPoin()
    }

    override fun viewOutOfCoverage() {
        onShowOutOfCoverage()
    }

    override fun onScrollChangedListenerAdded(onScrollChangedListener: ViewTreeObserver.OnScrollChangedListener) {
        onScrollChangedListenerList.add(onScrollChangedListener)
    }

    private fun createLegoBannerCallback(): TokoFoodHomeLegoComponentCallback {
        return TokoFoodHomeLegoComponentCallback(this, userSession, analytics)
    }

    private fun createBannerCallback(): TokoFoodHomeBannerComponentCallback {
        return TokoFoodHomeBannerComponentCallback(this, userSession, analytics)
    }

    private fun createCategoryWidgetCallback(): TokoFoodHomeCategoryWidgetV2ComponentCallback {
        return TokoFoodHomeCategoryWidgetV2ComponentCallback(this, userSession, analytics)
    }

    private fun getLayoutComponentData() {
        viewModel.setLayoutComponentData(localCacheModel)
    }

    private fun loadLayout() {
        viewModel.setHomeLayout(localCacheModel, userSession.isLoggedIn)
    }

    private fun getChooseAddress() {
        viewModel.getChooseAddress(SOURCE)
    }

    private fun setupUi() {
        view?.apply {
            jumpToTopView = binding?.icJumpToTop?.root
            navToolbar = binding?.navToolbar
            rvHome = binding?.rvHome
            swipeLayout = binding?.swipeRefreshLayout
            miniCartHome = binding?.miniCartTokofoodHome
        }
    }

    private fun setupRecycleView() {
        context?.let {
            rvHome?.apply {
                adapter = this@TokoFoodHomeFragment.adapter
                rvLayoutManager = CustomLinearLayoutManager(it)
                layoutManager = rvLayoutManager
            }
        }

        rvHome?.setItemViewCacheSize(ITEM_VIEW_CACHE_SIZE)
    }

    private fun setupNavToolbar() {
        setupTopNavigation()
        setIconNavigation()
    }

    private fun setupTopNavigation() {
        navToolbar?.let { toolbar ->
            viewLifecycleOwner.lifecycle.addObserver(toolbar)
            activity?.let {
                toolbar.showShadow(true)
                toolbar.setupToolbarWithStatusBar(it, applyPadding = false, applyPaddingNegative = true)
                toolbar.setToolbarTitle(getString(R.string.tokofood_title))
                toolbar.setBackButtonType(NavToolbar.Companion.BackType.BACK_TYPE_BACK_WITHOUT_COLOR)
                setToolbarSearch()
            }
        }
    }

    private fun setIconNavigation() {
        val icons =
            IconBuilder(IconBuilderFlag(pageSource = NavSource.TOKOFOOD))
                .addIcon(IconList.ID_SHARE, onClick = ::onClickShareButton)
                .addIcon(IconList.ID_LIST_TRANSACTION, onClick = ::onClickListTransactionButton)
                .addIcon(IconList.ID_NAV_GLOBAL, onClick = {})
        navToolbar?.setIcon(icons)
    }

    private fun setToolbarSearch() {
        if (isGoToSearchPage()) {
            viewModel.checkForSearchCoachMark()
            setToolbarSearchHint()
        }
    }

    private fun setToolbarSearchHint() {
        navToolbar?.setupSearchbar(
            hints = listOf(
                HintData(
                    placeholder = getString(com.tokopedia.tokofood.R.string.search_hint_searchbar_gofood)
                )
            ),
            searchbarClickCallback = { onSearchBarClick() }
        )
    }

    // Act as toggle
    private fun isGoToSearchPage(): Boolean {
        return true
    }

    private fun onSearchBarClick() {
        analytics.clickSearchBar(userSession.userId, localCacheModel?.district_id)
        context?.let {
            TokofoodRouteManager.routePrioritizeInternal(it, ApplinkConstInternalTokoFood.SEARCH)
        }
    }

    private fun onClickShareButton() {
        shareClicked()
    }

    private fun onClickListTransactionButton() {
        RouteManager.route(context, ApplinkConst.TokoFood.TOKOFOOD_ORDER)
    }

    private fun observeLiveData() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.flowLayoutList.collect {
                removeAllScrollListener()
                when (it) {
                    is Success -> {
                        onSuccessGetHomeLayout(it.data)
                    }
                    is Fail -> {
                        logExceptionTokoFoodHome(
                            it.throwable,
                            TokofoodErrorLogger.ErrorType.ERROR_PAGE,
                            TokofoodErrorLogger.ErrorDescription.RENDER_PAGE_ERROR
                        )
                        onErrorGetHomeLayout(it.throwable)
                    }
                }

                rvHome?.post {
                    addScrollListener()
                    resetSwipeLayout()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.flowUpdatePinPointState.collect {
                when (it) {
                    is Success -> {
                        getChooseAddress()
                    }

                    is Fail -> {
                        showToaster(it.throwable.message)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.flowChooseAddress.collect {
                when (it) {
                    is Success -> {
                        setupChooseAddress(it.data)
                    }

                    is Fail -> {
                        showToaster(it.throwable.message)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.flowShouldShowSearchCoachMark.collect { shouldShow ->
                updateSearchCoachMark(shouldShow)
            }
        }
    }

    private fun navigateAddAddress() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalLogistic.ADD_ADDRESS_V3)
        intent.putExtra(ChooseAddressBottomSheet.EXTRA_REF, SCREEN_NAME_CHOOSE_ADDRESS_NEW_USER)
        intent.putExtra(ChooseAddressBottomSheet.EXTRA_IS_FULL_FLOW, true)
        intent.putExtra(ChooseAddressBottomSheet.EXTRA_IS_LOGISTIC_LABEL, false)
        intent.putExtra(PARAM_SOURCE, AddEditAddressSource.TOKOFOOD.source)
        startActivityForResult(intent, REQUEST_CODE_ADD_ADDRESS)
    }

    private fun collectValue() {
        // TODO: Upgrade androidx.lifecycle:lifecycle-runtime-ktx to 2.4.0
        collectJob = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            activityViewModel?.cartDataValidationFlow?.collect { uiEvent ->
                when (uiEvent.state) {
                    UiEvent.EVENT_SUCCESS_VALIDATE_CHECKOUT -> {
                        (uiEvent.data as? CartGeneralCartListData)?.let {
                            analytics.clickAtc(userSession.userId, localCacheModel?.district_id, it)
                        }
                        if (uiEvent.source == MINI_CART_SOURCE) {
                            goToPurchasePage()
                        }
                    }
                    UiEvent.EVENT_SUCCESS_LOAD_CART -> {
                        if (!isBackFromOtherPage) {
                            if (viewModel.isShownEmptyState()) {
                                hideMiniCartHome()
                                isShowMiniCart = false
                            } else {
                                showMiniCartHome()
                                isShowMiniCart = true
                            }
                        } else {
                            isBackFromOtherPage = false
                        }
                    }
                    UiEvent.EVENT_FAILED_LOAD_CART -> {
                        hideMiniCartHome()
                        isShowMiniCart = false
                    }
                }
            }
        }
    }

    private fun onSuccessGetHomeLayout(data: TokoFoodListUiModel) {
        when (data.state) {
            TokoFoodLayoutState.SHOW -> onShowHomeLayout(data)
            TokoFoodLayoutState.HIDE -> onHideHomeLayout(data)
            TokoFoodLayoutState.LOADING -> onLoadingHomelayout(data)
            else -> showHomeLayout(data)
        }
    }

    private fun onErrorGetHomeLayout(throwable: Throwable) {
        viewModel.setErrorState(throwable)
    }

    private fun onHideHomeLayout(data: TokoFoodListUiModel) {
        showHomeLayout(data)
        stopPerformanceMonitoring()
    }

    private fun onShowHomeLayout(data: TokoFoodListUiModel) {
        startRenderPerformanceMonitoring()
        onOpenHomepage()
        showHomeLayout(data)
        getLayoutComponentData()
        activityViewModel?.loadCartList(MINI_CART_SOURCE)
        stopRenderPerformanceMonitoring()
    }

    private fun onLoadingHomelayout(data: TokoFoodListUiModel) {
        hideJumpToTop()
        showHomeLayout(data)
        checkAddressDataAndServiceArea()
    }

    private fun showHomeLayout(data: TokoFoodListUiModel) {
        onRenderHomepage()
        rvHome?.post {
            adapter.submitList(data.items)
        }
    }

    private fun setupSwipeRefreshLayout() {
        context?.let {
            swipeLayout?.setMargin(
                spaceZero,
                NavToolbarExt.getToolbarHeight(it),
                spaceZero,
                spaceZero
            )
            swipeLayout?.setOnRefreshListener {
                onRefreshLayout()
            }
        }
    }

    private fun resetSwipeLayout() {
        swipeLayout?.isEnabled = true
        swipeLayout?.isRefreshing = false
    }

    private fun updateSearchCoachMark(shouldShow: Boolean) {
        if (searchCoachMark == null) {
            context?.let {
                searchCoachMark = CoachMark2(it).apply {
                    onDismissListener = ::onCoachMarkDismissedListener
                }
            }
        }
        setSearchCoachMarkVisibility(shouldShow)
    }

    private fun setSearchCoachMarkVisibility(shouldShow: Boolean) {
        searchCoachMark?.run {
            if (shouldShow && !isShowing) {
                val title =
                    context?.getString(com.tokopedia.tokofood.R.string.home_coachmark_search_title)
                        .orEmpty()
                val description =
                    context?.getString(com.tokopedia.tokofood.R.string.home_coachmark_search_desc)
                        .orEmpty()
                getSearchCoachMarkItem(title, description)?.let { coachMarkItem ->
                    showCoachMark(arrayListOf(coachMarkItem), null, Int.ZERO)
                    analytics.viewSearchBarCoachmark(
                        userSession.userId,
                        localCacheModel?.district_id,
                        title,
                        description
                    )
                }
            } else {
                hideCoachMark()
            }
        }
    }

    private fun getSearchCoachMarkItem(title: String, description: String): CoachMark2Item? {
        return navToolbar?.let { toolbar ->
            CoachMark2Item(
                anchorView = toolbar,
                title = title,
                description = description,
                position = CoachMark2.POSITION_BOTTOM
            )
        }
    }

    private fun onCoachMarkDismissedListener() {
        viewModel.setSearchCoachMarkHasShown()
    }

    private fun removeAllScrollListener() {
        rvHome?.removeOnScrollListener(loadMoreListener)
    }

    private fun addScrollListener() {
        rvHome?.addOnScrollListener(loadMoreListener)
    }

    private fun removeScrollChangedListener() {
        onScrollChangedListenerList.forEach {
            view?.viewTreeObserver?.removeOnScrollChangedListener(it)
        }
    }

    private fun removeSearchCoachMark() {
        searchCoachMark?.onDismissListener = {}
        searchCoachMark = null
    }

    private fun createLoadMoreListener(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                setupJumpToTop(recyclerView, dy)
                onScrollProductList()
            }
        }
    }

    private fun onScrollProductList() {
        val layoutManager = rvHome?.layoutManager as? LinearLayoutManager
        val index = layoutManager?.findLastVisibleItemPosition().orZero()
        val itemCount = layoutManager?.itemCount.orZero()
        localCacheModel?.let {
            viewModel.onScrollProductList(
                index,
                itemCount,
                localCacheModel = it
            )
        }
    }

    private fun onRefreshLayout() {
        removeAllScrollListener()
        rvLayoutManager?.setScrollEnabled(true)
        updateCurrentPageLocalCacheModelData()
        loadLayout()
    }

    private fun showUSPBottomSheet(uspResponse: USPResponse) {
        val tokoFoodUSPBottomSheet = TokoFoodUSPBottomSheet.getInstance()
        tokoFoodUSPBottomSheet.setUSP(uspResponse, getString(com.tokopedia.tokofood.R.string.home_usp_bottom_sheet_title))
        tokoFoodUSPBottomSheet.show(parentFragmentManager, "")
    }

    private fun showChooseAddressBottomSheet() {
        if (isAdded) {
            val chooseAddressBottomSheet = ChooseAddressBottomSheet()
            chooseAddressBottomSheet.setListener(this)
            chooseAddressBottomSheet.show(childFragmentManager, "")
        }
    }

    private fun checkAddressDataAndServiceArea() {
        checkIfChooseAddressWidgetDataUpdated()
    }

    private fun checkIfChooseAddressWidgetDataUpdated() {
        if (isChooseAddressWidgetDataUpdated()) {
            updateCurrentPageLocalCacheModelData()
        }
    }

    private fun isChooseAddressWidgetDataUpdated(): Boolean {
        localCacheModel?.let { cacheModel ->
            context?.let {
                return ChooseAddressUtils.isLocalizingAddressHasUpdated(
                    it,
                    cacheModel
                )
            }
        }
        return false
    }

    private fun updateCurrentPageLocalCacheModelData() {
        context?.let {
            localCacheModel = ChooseAddressUtils.getLocalizingAddressData(it)
        }
    }
    private fun navigateToSetPinpoint() {
        activity?.let {
            val bundle = Bundle().apply {
                putBoolean(AddressConstant.EXTRA_IS_GET_PINPOINT_ONLY, true)
                putDouble(AddressConstant.EXTRA_LAT, TOTO_LATITUDE.toDouble())
                putDouble(AddressConstant.EXTRA_LONG, TOTO_LONGITUDE.toDouble())
            }
            RouteManager.getIntent(it, ApplinkConstInternalLogistic.PINPOINT).apply {
                putExtra(AddressConstant.EXTRA_BUNDLE, bundle)
                startActivityForResult(this, REQUEST_CODE_SET_PINPOINT)
            }
        }
    }

    private fun onResultFromSetPinpoint(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            data?.let {
                val locationPass =
                    it.getParcelableExtra(LogisticConstant.EXTRA_EXISTING_LOCATION) as? LocationPass
                if (locationPass == null) {
                    val addressData = it.getParcelableExtra(AddressConstant.EXTRA_SAVE_DATA_UI_MODEL) as? SaveAddressDataModel
                    addressData?.let { address ->
                        localCacheModel?.address_id?.let { addressId ->
                            viewModel.setUpdatePinPoint(
                                addressId,
                                address.latitude,
                                address.longitude
                            )
                        }
                    }
                } else {
                    localCacheModel?.address_id?.let { addressId ->
                        viewModel.setUpdatePinPoint(
                            addressId,
                            locationPass.latitude,
                            locationPass.longitude
                        )
                    }
                }
            }
        }
    }

    private fun onResultFromAddAddress(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            data?.let {
                val addressDataModel = data.getParcelableExtra(NEW_ADDRESS_PARCELABLE) as? SaveAddressDataModel
                addressDataModel?.let {
                    setupChooseAddress(it)
                }
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
        loadLayout()
    }

    private fun setupChooseAddress(addressDataModel: SaveAddressDataModel) {
        context?.let {
            ChooseAddressUtils.updateLocalizingAddressDataFromOther(
                it,
                addressDataModel.id.toString(),
                addressDataModel.cityId.toString(),
                addressDataModel.districtId.toString(),
                addressDataModel.latitude,
                addressDataModel.longitude,
                "${addressDataModel.addressName} ${addressDataModel.receiverName}",
                addressDataModel.postalCode,
                addressDataModel.shopId.toString(),
                addressDataModel.warehouseId.toString(),
                TokonowWarehouseMapper.mapWarehousesAddAddressModelToLocal(addressDataModel.warehouses),
                addressDataModel.serviceType
            )
        }
        checkIfChooseAddressWidgetDataUpdated()
        loadLayout()
    }

    private fun showToaster(message: String?) {
        view?.let {
            if (!message.isNullOrEmpty()) {
                Toaster.build(it, message, Toaster.LENGTH_LONG).show()
            }
        }
    }

    private fun initializeMiniCartHome() {
        activityViewModel?.let {
            miniCartHome?.initialize(it, viewLifecycleOwner.lifecycleScope, MINI_CART_SOURCE)
        }
    }

    private fun showMiniCartHome() {
        setRvPadding(isShowMiniCart = true)
        miniCartHome?.show()
    }

    private fun hideMiniCartHome() {
        setRvPadding(isShowMiniCart = false)
        miniCartHome?.hide()
    }

    private fun goToPurchasePage() {
        navigateToNewFragment(TokoFoodPurchaseFragment.createInstance())
    }

    private fun createShareHome(): TokoFoodHomeShare {
        return TokoFoodHomeShare(
            sharingText = context?.resources?.getString(R.string.home_share_main_text).orEmpty(),
            sharingUrl = SHARE_URL,
            sharingDeeplink = SHARE_DEEPLINK,
            thumbNailImage = THUMBNAIL_IMAGE_SHARE_URL,
            thumbNailTitle = context?.resources?.getString(R.string.home_share_tn_title).orEmpty(),
            ogImageUrl = OG_IMAGE_SHARE_URL,
            specificPageName = context?.resources?.getString(R.string.home_share_title).orEmpty(),
            specificPageDescription = context?.resources?.getString(R.string.home_share_desc).orEmpty(),
            linkerType = FOOD_TYPE
        )
    }

    private fun shareClicked() {
        if (SharingUtil.isCustomSharingEnabled(context)) {
            context?.let {
                SharingUtil.saveImageFromURLToStorage(
                    it,
                    OG_IMAGE_SHARE_URL
                ) { storageImagePath ->
                    showUniversalShareBottomSheet(storageImagePath)
                }
            }
        } else {
            LinkerManager.getInstance().executeShareRequest(shareRequest(context, shareHomeTokoFood))
        }
    }

    private fun showUniversalShareBottomSheet(imageSaved: String) {
        if (isAdded) {
            universalShareBottomSheet = UniversalShareBottomSheet.createInstance().apply {
                setFeatureFlagRemoteConfigKey()
                init(this@TokoFoodHomeFragment)
                setUtmCampaignData(
                    pageName = PAGE_SHARE_NAME,
                    userId = userSession.userId,
                    pageId = PAGE_TYPE_HOME,
                    feature = SHARE
                )
                setMetaData(
                    tnTitle = shareHomeTokoFood?.thumbNailTitle.orEmpty(),
                    tnImage = shareHomeTokoFood?.thumbNailImage.orEmpty()
                )
                setOgImageUrl(imgUrl = shareHomeTokoFood?.ogImageUrl.orEmpty())
                imageSaved(imageSaved)
            }

            universalShareBottomSheet?.show(childFragmentManager, this)
        }
    }

    private fun logExceptionTokoFoodHome(
        throwable: Throwable,
        errorType: String,
        description: String
    ) {
        TokofoodErrorLogger.logExceptionToServerLogger(
            TokofoodErrorLogger.PAGE.HOME,
            throwable,
            errorType,
            userSession.deviceId.orEmpty(),
            description
        )
    }

    private fun onOpenHomepage() {
        if (!viewModel.isShownEmptyState()) {
            localCacheModel?.let {
                analytics.openScreenHomePage(
                    userSession.userId,
                    localCacheModel?.district_id,
                    userSession.isLoggedIn
                )
            }
        }
    }

    private fun onRenderHomepage() {
        if (!viewModel.isShownEmptyState() && isShowMiniCart) {
            showMiniCartHome()
        } else {
            hideMiniCartHome()
            hideJumpToTop()
        }
    }

    private fun onShowOutOfCoverage() {
        localCacheModel?.let {
            analytics.openScreenOutOfCoverage(
                userSession.userId,
                localCacheModel?.district_id,
                userSession.isLoggedIn
            )
        }
    }

    private fun onShowNoPinPoin() {
        localCacheModel?.let {
            analytics.openScreenNoPinPoin(
                userSession.userId,
                localCacheModel?.district_id,
                userSession.isLoggedIn
            )
        }
    }

    private fun onShowEmptyState(errorState: String, title: String, desc: String) {
        analytics.clickEmptyState(
            userSession.userId,
            localCacheModel?.district_id,
            errorState,
            title,
            desc
        )
    }

    private fun initPerformanceMonitoring() {
        pageLoadTimeMonitoring = (activity as? BaseTokofoodActivity)?.pageLoadTimeMonitoring
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

    private fun hideJumpToTop() {
        jumpToTopView?.hide()
    }

    private fun showJumpToTop(recyclerView: RecyclerView) {
        jumpToTopView?.show()
        jumpToTopView?.setOnClickListener {
            recyclerView.scrollToPosition(Int.ZERO)
            totalScrolled = 0
            hideJumpToTop()
        }
    }

    private fun setupJumpToTop(recyclerView: RecyclerView, dy: Int) {
        totalScrolled += dy
        binding?.root?.height?.let {
            if (totalScrolled > (it / HEIGHT_DIVIDER)) {
                showJumpToTop(recyclerView)
            } else {
                hideJumpToTop()
            }
        }
    }

    private fun setRvPadding(isShowMiniCart: Boolean) {
        rvHome?.let {
            if (isShowMiniCart) {
                it.setPadding(
                    Int.ZERO,
                    Int.ZERO,
                    Int.ZERO,
                    context?.resources?.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl7) ?: Int.ZERO
                )
            } else {
                it.setPadding(Int.ZERO, Int.ZERO, Int.ZERO, Int.ZERO)
            }
        }
    }
}
