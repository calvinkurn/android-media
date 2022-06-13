package com.tokopedia.tokofood.feature.home.presentation.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseMultiFragActivity
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.fragment.IBaseMultiFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
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
import com.tokopedia.logisticCommon.data.constant.LogisticConstant
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.searchbar.navigation_component.util.NavToolbarExt
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodData
import com.tokopedia.tokofood.common.constants.ShareComponentConstants
import com.tokopedia.tokofood.common.minicartwidget.view.TokoFoodMiniCartWidget
import com.tokopedia.tokofood.common.presentation.UiEvent
import com.tokopedia.tokofood.common.presentation.listener.HasViewModel
import com.tokopedia.tokofood.common.presentation.view.BaseTokofoodActivity
import com.tokopedia.tokofood.common.presentation.viewmodel.MultipleFragmentsViewModel
import com.tokopedia.tokofood.common.util.TokofoodErrorLogger
import com.tokopedia.tokofood.databinding.FragmentTokofoodHomeBinding
import com.tokopedia.tokofood.feature.home.analytics.TokoFoodHomeAnalytics
import com.tokopedia.tokofood.feature.home.analytics.TokoFoodHomePageLoadTimeMonitoring
import com.tokopedia.tokofood.feature.home.di.DaggerTokoFoodHomeComponent
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodLayoutState
import com.tokopedia.tokofood.feature.home.domain.data.DynamicIcon
import com.tokopedia.tokofood.feature.home.domain.data.Merchant
import com.tokopedia.tokofood.feature.home.domain.data.USPResponse
import com.tokopedia.tokofood.feature.home.presentation.adapter.CustomLinearLayoutManager
import com.tokopedia.tokofood.feature.home.presentation.adapter.TokoFoodHomeAdapter
import com.tokopedia.tokofood.feature.home.presentation.adapter.TokoFoodHomeAdapterTypeFactory
import com.tokopedia.tokofood.feature.home.presentation.adapter.TokoFoodListDiffer
import com.tokopedia.tokofood.feature.home.presentation.adapter.viewholder.TokoFoodErrorStateViewHolder
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
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeEmptyStateLocationUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodListUiModel
import com.tokopedia.tokofood.feature.home.presentation.view.listener.TokoFoodHomeBannerComponentCallback
import com.tokopedia.tokofood.feature.home.presentation.view.listener.TokoFoodHomeCategoryWidgetV2ComponentCallback
import com.tokopedia.tokofood.feature.home.presentation.view.listener.TokoFoodHomeLegoComponentCallback
import com.tokopedia.tokofood.feature.home.presentation.view.listener.TokoFoodView
import com.tokopedia.tokofood.feature.home.presentation.viewmodel.TokoFoodHomeViewModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.TokoFoodPurchaseFragment
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class TokoFoodHomeFragment : BaseDaggerFragment(),
        IBaseMultiFragment,
        TokoFoodView,
        TokoFoodHomeUSPViewHolder.TokoFoodUSPListener,
        TokoFoodHomeChooseAddressViewHolder.TokoFoodChooseAddressWidgetListener,
        TokoFoodHomeEmptyStateLocationViewHolder.TokoFoodHomeEmptyStateLocationListener,
        TokoFoodHomeIconsViewHolder.TokoFoodHomeIconsListener,
        TokoFoodMerchantListViewHolder.TokoFoodMerchantListListener,
        TokoFoodHomeTickerViewHolder.TokoFoodHomeTickerListener,
        TokoFoodErrorStateViewHolder.TokoFoodErrorStateListener,
        ChooseAddressBottomSheet.ChooseAddressBottomSheetListener,
        ShareBottomsheetListener
{

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var analytics: TokoFoodHomeAnalytics

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
                errorStateListener = this
            ),
            differ = TokoFoodListDiffer(),
        )
    }
    private val loadMoreListener by lazy { createLoadMoreListener() }

    companion object {
        private const val ITEM_VIEW_CACHE_SIZE = 20
        private const val REQUEST_CODE_SET_PINPOINT = 112
        private const val REQUEST_CODE_ADD_ADDRESS = 113
        private const val NEW_ADDRESS_PARCELABLE = "EXTRA_ADDRESS_NEW"
        private const val TOTO_LATITUDE = "-6.2216771"
        private const val TOTO_LONGITUDE = "106.8184023"
        private const val EMPTY_LOCATION = "0.0"
        private const val MINI_CART_SOURCE = "home_page"
        private const val PAGE_SHARE_NAME = "TokoFood"
        private const val SHARE = "share"
        private const val PAGE_TYPE_HOME = "home"
        private const val SHARE_URL = "https://www.tokopedia.com/gofood"
        private const val SHARE_DEEPLINK = "tokopedia://food/home"
        //TODO Dummy OG IMAGE
        private const val THUMBNAIL_AND_OG_IMAGE_SHARE_URL = "https://images.tokopedia.net/img/android/now/PN-RICH.jpg"
        const val SOURCE = "tokofood"

        fun createInstance(): TokoFoodHomeFragment {
            return TokoFoodHomeFragment()
        }
    }

    private var navToolbar: NavToolbar? = null
    private var rvHome: RecyclerView? = null
    private var swipeLayout: SwipeRefreshLayout? = null
    private var rvLayoutManager: CustomLinearLayoutManager? = null
    private var miniCartHome: TokoFoodMiniCartWidget? = null
    private var universalShareBottomSheet: UniversalShareBottomSheet? = null
    private var shareHomeTokoFood: TokoFoodHomeShare? = null
    private var localCacheModel: LocalCacheModel? = null
    private var pageLoadTimeMonitoring: TokoFoodHomePageLoadTimeMonitoring? = null
    private var movingPosition = 0
    private val spaceZero: Int
        get() = resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_0).toInt()

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

    override fun getFragmentToolbar(): Toolbar? {
        return null
    }

    override fun navigateToNewFragment(fragment: Fragment) {
        (activity as? BaseMultiFragActivity)?.navigateToNewFragment(fragment)
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
        collectValue()
        setupUi()
        setupNavToolbar()
        setupRecycleView()
        setupSwipeRefreshLayout()
        observeLiveData()
        updateCurrentPageLocalCacheModelData()
        loadLayout()
    }

    override fun onResume() {
        super.onResume()
        if (isChooseAddressWidgetDataUpdated()) {
            onRefreshLayout()
        }
        initializeMiniCartHome()
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
        checkUserEligibilityForAnaRevamp()
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

    override fun onClickHomeIcon(applink: String, data: List<DynamicIcon>, horizontalPosition: Int, verticalPosition: Int) {
        analytics.clickIconWidget(userSession.userId, localCacheModel?.district_id, data, horizontalPosition, verticalPosition)
        RouteManager.route(context, applink)
    }

    override fun onImpressHomeIcon(data: List<DynamicIcon>, verticalPosition: Int) {
        analytics.impressionIconWidget(userSession.userId, localCacheModel?.district_id, data, verticalPosition)
    }

    override fun onClickMerchant(merchant: Merchant, horizontalPosition: Int) {
        analytics.clickMerchant(userSession.userId, localCacheModel?.district_id, merchant, horizontalPosition)
        val merchantApplink = UriUtil.buildUri(ApplinkConst.TokoFood.MERCHANT, merchant.id, "")
        RouteManager.route(context, merchantApplink)
    }

    override fun onImpressMerchant(merchant: Merchant, horizontalPosition: Int) {
        analytics.impressMerchant(userSession.userId, localCacheModel?.district_id, merchant, horizontalPosition)
    }

    override fun onTickerDismissed(id: String) {
        viewModel.removeTickerWidget(id)
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

    override fun onCloseOptionClicked() {

    }

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

    private fun createLegoBannerCallback(): TokoFoodHomeLegoComponentCallback {
        return TokoFoodHomeLegoComponentCallback(this, userSession, analytics)
    }

    private fun createBannerCallback(): TokoFoodHomeBannerComponentCallback {
        return TokoFoodHomeBannerComponentCallback(this, userSession, analytics)
    }

    private fun createCategoryWidgetCallback(): TokoFoodHomeCategoryWidgetV2ComponentCallback {
        return TokoFoodHomeCategoryWidgetV2ComponentCallback(this, userSession, analytics)
    }

    private fun showLayout() {
        getHomeLayout()
    }

    private fun getHomeLayout() {
        localCacheModel?.let {
            viewModel.getHomeLayout(it)
        }
    }

    private fun getLayoutComponentData() {
        viewModel.getLayoutComponentData(localCacheModel)
    }

    private fun loadLayout() {
        viewModel.getLoadingState()
    }

    private fun showNoPinPoin() {
        viewModel.getNoPinPoinState()
    }

    private fun showNoAddress() {
        viewModel.getNoAddressState()
    }

    private fun getChooseAddress() {
        viewModel.getChooseAddress(SOURCE)
    }

    private fun checkUserEligibilityForAnaRevamp() {
        viewModel.checkUserEligibilityForAnaRevamp()
    }

    private fun setupUi() {
        view?.apply {
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
            }
        }
    }

    private fun setIconNavigation() {
        val icons =
            IconBuilder(IconBuilderFlag(pageSource = ApplinkConsInternalNavigation.SOURCE_HOME))
                .addIcon(IconList.ID_SHARE, onClick = ::onClickShareButton)
                .addIcon(IconList.ID_LIST_TRANSACTION, onClick = ::onClickListTransactionButton)
                .addIcon(IconList.ID_NAV_GLOBAL, onClick = {})
        navToolbar?.setIcon(icons)
    }

    private fun onClickShareButton() {
        shareClicked()
    }

    private fun onClickListTransactionButton() {
        RouteManager.route(context, ApplinkConst.TokoFood.TOKOFOOD_ORDER)
    }

    private fun observeLiveData() {
        observe(viewModel.layoutList) {
            removeAllScrollListener()
            when (it) {
                is Success -> onSuccessGetHomeLayout(it.data)
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

        observe(viewModel.updatePinPointState) { isSuccess ->
            if (isSuccess) {
                getChooseAddress()
            }
        }

        observe(viewModel.errorMessage) { message ->
            showToaster(message)
        }

        observe(viewModel.chooseAddress) {
            when(it) {
                is Success -> {
                    setupChooseAddress(it.data)
                }

                is Fail -> {
                    showToaster(it.throwable.message)
                }
            }
        }

        observe(viewModel.eligibleForAnaRevamp) {
            when(it) {
                is Success -> {
                    if (it.data.eligible) {
                        val intent = RouteManager.getIntent(context, ApplinkConstInternalLogistic.ADD_ADDRESS_V3)
                        intent.putExtra(ChooseAddressBottomSheet.EXTRA_REF, SCREEN_NAME_CHOOSE_ADDRESS_NEW_USER)
                        intent.putExtra(ChooseAddressBottomSheet.EXTRA_IS_FULL_FLOW, true)
                        intent.putExtra(ChooseAddressBottomSheet.EXTRA_IS_LOGISTIC_LABEL, false)
                        startActivityForResult(intent, REQUEST_CODE_ADD_ADDRESS)
                    } else {
                        val intent = RouteManager.getIntent(context, ApplinkConstInternalLogistic.ADD_ADDRESS_V2)
                        intent.putExtra(ChooseAddressBottomSheet.EXTRA_REF, SCREEN_NAME_CHOOSE_ADDRESS_NEW_USER)
                        intent.putExtra(ChooseAddressBottomSheet.EXTRA_IS_FULL_FLOW, true)
                        intent.putExtra(ChooseAddressBottomSheet.EXTRA_IS_LOGISTIC_LABEL, false)
                        startActivityForResult(intent, REQUEST_CODE_ADD_ADDRESS)
                    }
                }

                is Fail -> {
                    logExceptionTokoFoodHome(
                        it.throwable,
                        TokofoodErrorLogger.ErrorType.ERROR_ELIGIBLE_SET_ADDRESS,
                        TokofoodErrorLogger.ErrorDescription.ERROR_ELIGIBLE_SET_ADDRESS
                    )
                    showToaster(it.throwable.message)
                }
            }
        }
    }

    private fun collectValue() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            activityViewModel?.cartDataValidationFlow?.collect { uiEvent ->
                when(uiEvent.state) {
                    UiEvent.EVENT_SUCCESS_VALIDATE_CHECKOUT -> {
                        (uiEvent.data as? CheckoutTokoFoodData)?.let {
                            analytics.clickAtc(userSession.userId, localCacheModel?.district_id, it)
                        }
                        goToPurchasePage()
                    }
                    UiEvent.EVENT_SUCCESS_LOAD_CART -> {
                        showMiniCartHome()
                    }
                    UiEvent.EVENT_FAILED_LOAD_CART -> {
                        hideMiniCartHome()
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
        viewModel.getErrorState(throwable)
        hideMiniCartHome()
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
        initializeMiniCartHome()
        stopRenderPerformanceMonitoring()
    }

    private fun onLoadingHomelayout(data: TokoFoodListUiModel) {
        hideMiniCartHome()
        showHomeLayout(data)
        checkAddressDataAndServiceArea()
    }

    private fun showHomeLayout(data: TokoFoodListUiModel) {
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

    private fun resetMovingPosition() {
        movingPosition = 0
    }

    private fun removeAllScrollListener() {
        rvHome?.removeOnScrollListener(loadMoreListener)
    }

    private fun addScrollListener() {
        rvHome?.addOnScrollListener(loadMoreListener)
    }

    private fun createLoadMoreListener(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
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
        resetMovingPosition()
        removeAllScrollListener()
        rvLayoutManager?.setScrollEnabled(true)
        loadLayout()
    }

    private fun showUSPBottomSheet(uspResponse: USPResponse) {
        val tokoFoodUSPBottomSheet = TokoFoodUSPBottomSheet.getInstance()
        tokoFoodUSPBottomSheet.setUSP(uspResponse, getString(com.tokopedia.tokofood.R.string.home_usp_bottom_sheet_title))
        tokoFoodUSPBottomSheet.show(parentFragmentManager, "")
    }

    private fun showChooseAddressBottomSheet() {
        val chooseAddressBottomSheet = ChooseAddressBottomSheet()
        chooseAddressBottomSheet.setListener(this)
        chooseAddressBottomSheet.show(childFragmentManager, "")
    }

    private fun checkAddressDataAndServiceArea(){
        checkIfChooseAddressWidgetDataUpdated()

        if (hasNoAddress()) {
            showNoAddress()
        } else if (hasNoPinPoin()){
            showNoPinPoin()
        } else {
            showLayout()
        }
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

    private fun updateCurrentPageLocalCacheModelData() {
        context?.let {
            localCacheModel = ChooseAddressUtils.getLocalizingAddressData(it)
        }
    }

    private fun hasNoAddress(): Boolean {
        return userSession.isLoggedIn && localCacheModel?.address_id.isNullOrEmpty()
    }

    private fun hasNoPinPoin(): Boolean {
        return userSession.isLoggedIn && !localCacheModel?.address_id.isNullOrEmpty()
                && (localCacheModel?.lat.isNullOrEmpty() || localCacheModel?.long.isNullOrEmpty() ||
                localCacheModel?.lat.equals(EMPTY_LOCATION) || localCacheModel?.long.equals(EMPTY_LOCATION))
    }

    private fun navigateToSetPinpoint() {
        val locationPass =  LocationPass().apply {
            latitude = TOTO_LATITUDE
            longitude = TOTO_LONGITUDE
        }
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.GEOLOCATION)
        val bundle = Bundle().apply {
            putParcelable(LogisticConstant.EXTRA_EXISTING_LOCATION, locationPass)
            putBoolean(LogisticConstant.EXTRA_IS_FROM_MARKETPLACE_CART, true)
        }
        intent.putExtras(bundle)
        startActivityForResult(intent, REQUEST_CODE_SET_PINPOINT)
    }

    private fun onResultFromSetPinpoint(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            data?.let {
                val locationPass = it.getParcelableExtra(LogisticConstant.EXTRA_EXISTING_LOCATION) as? LocationPass
                locationPass?.let { locationPass ->
                    localCacheModel?.address_id?.let { addressId ->
                        viewModel.updatePinPoin(addressId, locationPass.latitude, locationPass.longitude)
                    }
                }
            }
        }
    }

    private fun onResultFromAddAddress(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            data?.let {
                val addressDataModel = data?.getParcelableExtra<SaveAddressDataModel>(NEW_ADDRESS_PARCELABLE)
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
                serviceType = chooseAddressData.tokonow.serviceType
            )
        }
        checkIfChooseAddressWidgetDataUpdated()
        loadLayout()
    }

    private fun setupChooseAddress(addressDataModel: SaveAddressDataModel) {
        context?.let {
            ChooseAddressUtils.updateLocalizingAddressDataFromOther(it,
                addressDataModel.id.toString(), addressDataModel.cityId.toString(), addressDataModel.districtId.toString(),
                addressDataModel.latitude, addressDataModel.longitude, "${addressDataModel.addressName} ${addressDataModel.receiverName}",
                addressDataModel.postalCode, addressDataModel.shopId.toString(), addressDataModel.warehouseId.toString(),
                TokonowWarehouseMapper.mapWarehousesAddAddressModelToLocal(addressDataModel.warehouses), addressDataModel.serviceType)
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
        miniCartHome?.show()
    }

    private fun hideMiniCartHome() {
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
            thumbNailImage = THUMBNAIL_AND_OG_IMAGE_SHARE_URL,
            thumbNailTitle = context?.resources?.getString(R.string.home_share_tn_title).orEmpty(),
            ogImageUrl = THUMBNAIL_AND_OG_IMAGE_SHARE_URL,
            specificPageName = context?.resources?.getString(R.string.home_share_title).orEmpty(),
            specificPageDescription = context?.resources?.getString(R.string.home_share_desc).orEmpty(),
            linkerType = FOOD_TYPE
        )
    }

    private fun shareClicked() {
        if (UniversalShareBottomSheet.isCustomSharingEnabled(context)) {
            showUniversalShareBottomSheet()
        } else {
            LinkerManager.getInstance().executeShareRequest(shareRequest(context, shareHomeTokoFood))
        }
    }

    private fun showUniversalShareBottomSheet() {
        universalShareBottomSheet = UniversalShareBottomSheet.createInstance().apply {
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
        }

        universalShareBottomSheet?.show(childFragmentManager, this)
    }

    private fun logExceptionTokoFoodHome(
        throwable: Throwable,
        errorType: String,
        description: String,
    ){
        TokofoodErrorLogger.logExceptionToServerLogger(
            TokofoodErrorLogger.PAGE.HOME,
            throwable,
            errorType,
            userSession.deviceId.orEmpty(),
            description
        )
    }

    private fun onOpenHomepage(){
        if (!viewModel.isShownEmptyState()) {
            localCacheModel?.let {
                analytics.openScreenHomePage(
                    userSession.userId, localCacheModel?.district_id,
                    userSession.isLoggedIn
                )
            }
        }
    }

    private fun onShowOutOfCoverage(){
        localCacheModel?.let {
            analytics.openScreenOutOfCoverage(userSession.userId, localCacheModel?.district_id,
                userSession.isLoggedIn)
        }
    }

    private fun onShowNoPinPoin(){
        localCacheModel?.let {
            analytics.openScreenNoPinPoin(userSession.userId, localCacheModel?.district_id,
                userSession.isLoggedIn)
        }
    }

    private fun onShowEmptyState(errorState: String, title: String, desc: String){
        analytics.clickEmptyState(userSession.userId, localCacheModel?.district_id, errorState, title, desc)
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
}