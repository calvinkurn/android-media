package com.tokopedia.tokofood.feature.home.presentation.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseMultiFragment
import com.tokopedia.abstraction.base.view.fragment.enums.BaseMultiFragmentLaunchMode
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.applink.tokofood.DeeplinkMapperTokoFood.BRAND_UID_PARAM
import com.tokopedia.applink.tokofood.DeeplinkMapperTokoFood.CUISINE_PARAM
import com.tokopedia.applink.tokofood.DeeplinkMapperTokoFood.OPTION_PARAM
import com.tokopedia.applink.tokofood.DeeplinkMapperTokoFood.PAGE_TITLE_PARAM
import com.tokopedia.applink.tokofood.DeeplinkMapperTokoFood.SORT_BY_PARAM
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.util.NavToolbarExt
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodData
import com.tokopedia.tokofood.common.domain.response.Merchant
import com.tokopedia.tokofood.common.minicartwidget.view.TokoFoodMiniCartWidget
import com.tokopedia.tokofood.common.presentation.UiEvent
import com.tokopedia.tokofood.common.presentation.listener.HasViewModel
import com.tokopedia.tokofood.common.presentation.viewmodel.MultipleFragmentsViewModel
import com.tokopedia.tokofood.common.util.Constant
import com.tokopedia.tokofood.common.util.TokofoodErrorLogger
import com.tokopedia.tokofood.common.util.TokofoodRouteManager
import com.tokopedia.tokofood.databinding.FragmentTokofoodCategoryBinding
import com.tokopedia.tokofood.feature.home.analytics.TokoFoodCategoryAnalytics
import com.tokopedia.tokofood.feature.home.analytics.TokoFoodHomeCategoryCommonAnalytics
import com.tokopedia.tokofood.feature.home.di.DaggerTokoFoodHomeComponent
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodLayoutState
import com.tokopedia.tokofood.feature.home.presentation.adapter.CustomLinearLayoutManager
import com.tokopedia.tokofood.feature.home.presentation.adapter.TokoFoodCategoryAdapter
import com.tokopedia.tokofood.feature.home.presentation.adapter.TokoFoodCategoryAdapterTypeFactory
import com.tokopedia.tokofood.feature.home.presentation.adapter.TokoFoodListDiffer
import com.tokopedia.tokofood.common.presentation.adapter.viewholder.TokoFoodErrorStateViewHolder
import com.tokopedia.tokofood.common.presentation.listener.TokofoodScrollChangedListener
import com.tokopedia.tokofood.feature.home.presentation.adapter.viewholder.TokoFoodMerchantListViewHolder
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodListUiModel
import com.tokopedia.tokofood.feature.home.presentation.viewmodel.TokoFoodCategoryViewModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.TokoFoodPurchaseFragment
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class TokoFoodCategoryFragment: BaseMultiFragment(),
    TokoFoodMerchantListViewHolder.TokoFoodMerchantListListener,
    TokoFoodErrorStateViewHolder.TokoFoodErrorStateListener,
    TokofoodScrollChangedListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var analytics: TokoFoodCategoryAnalytics

    @Inject
    lateinit var trackingQueue: TrackingQueue

    private var binding by autoClearedNullable<FragmentTokofoodCategoryBinding>()
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(TokoFoodCategoryViewModel::class.java)
    }
    private var parentActivity: HasViewModel<MultipleFragmentsViewModel>? = null
    private val activityViewModel: MultipleFragmentsViewModel?
        get() = parentActivity?.viewModel()
    private val adapter by lazy {
        TokoFoodCategoryAdapter(
            typeFactory = TokoFoodCategoryAdapterTypeFactory(
                merchantListListener = this,
                errorStateListener = this,
                tokofoodScrollChangedListener = this
            ),
            differ = TokoFoodListDiffer()
        )
    }
    private val loadMoreListener by lazy { createLoadMoreListener() }

    private var collectJob: Job? = null

    companion object {
        private const val ITEM_VIEW_CACHE_SIZE = 20
        private const val MINI_CART_SOURCE = "category_page"

        fun createInstance(): TokoFoodCategoryFragment {
            return TokoFoodCategoryFragment()
        }
    }

    private var pageTitle: String = ""
    private var option: Int = 0
    private var sortBy: Int = 0
    private var cuisine: String = ""
    private var brandUId: String = ""
    private var rvCategory: RecyclerView? = null
    private var miniCartCategory: TokoFoodMiniCartWidget? = null
    private var swipeLayout: SwipeRefreshLayout? = null
    private var navToolbar: NavToolbar? = null
    private var rvLayoutManager: CustomLinearLayoutManager? = null
    private var localCacheModel: LocalCacheModel? = null
    private var isShowMiniCart = false
    private var onScrollChangedListenerList = mutableListOf<ViewTreeObserver.OnScrollChangedListener>()
    private val spaceZero: Int
        get() = context?.resources?.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_0)?.toInt() ?: 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = arguments?.getString(Constant.DATA_KEY) ?: ""
        val uri = Uri.parse(bundle)
        pageTitle = uri.getQueryParameter(PAGE_TITLE_PARAM) ?: ""
        option = uri.getQueryParameter(OPTION_PARAM).toIntOrZero()
        cuisine = uri.getQueryParameter(CUISINE_PARAM) ?: ""
        sortBy = uri.getQueryParameter(SORT_BY_PARAM).toIntOrZero()
        brandUId = uri.getQueryParameter(BRAND_UID_PARAM) ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTokofoodCategoryBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBackgroundColor()
        setupUi()
        setupNavToolbar()
        setupRecycleView()
        setupSwipeRefreshLayout()
        observeLiveData()
        updateCurrentPageLocalCacheModelData()
        loadLayout()
    }

    override fun onAttachActivity(context: Context?) {
        super.onAttachActivity(context)
        parentActivity = activity as? HasViewModel<MultipleFragmentsViewModel>
    }

    override fun onStart() {
        super.onStart()
        initializeMiniCartCategory()
        collectValue()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        collectJob?.cancel()
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        removeScrollChangedListener()
    }

    override fun getFragmentTitle(): String {
        return ""
    }

    override fun getFragmentToolbar(): Toolbar? {
        return null
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun getLaunchMode(): BaseMultiFragmentLaunchMode {
        return BaseMultiFragmentLaunchMode.SINGLE_TASK
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

    override fun onClickRetryError() {
        loadLayout()
    }

    private fun setupBackgroundColor() {
        activity?.let {
            it.window.decorView.setBackgroundColor(
                ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_Background)
            )
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

    override fun onClickMerchant(merchant: Merchant, horizontalPosition: Int) {
        analytics.clickMerchant(userSession.userId, localCacheModel?.district_id, merchant, horizontalPosition)
        val merchantApplink = UriUtil.buildUri(ApplinkConst.TokoFood.MERCHANT, merchant.id, "")
        TokofoodRouteManager.routePrioritizeInternal(context, merchantApplink)
    }

    override fun onImpressMerchant(merchant: Merchant, horizontalPosition: Int) {
        trackingQueue.putEETracking(
            TokoFoodHomeCategoryCommonAnalytics.impressMerchant(userSession.userId,
            localCacheModel?.district_id, merchant, horizontalPosition, isHome = false) as HashMap<String, Any>)
    }

    override fun onScrollChangedListenerAdded(onScrollChangedListener: ViewTreeObserver.OnScrollChangedListener) {
        onScrollChangedListenerList.add(onScrollChangedListener)
    }

    private fun onRefreshLayout() {
        activityViewModel?.loadCartList(MINI_CART_SOURCE)
        loadLayout()
    }

    private fun collectValue() {
        collectJob = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            activityViewModel?.cartDataValidationFlow?.collect { uiEvent ->
                when(uiEvent.state) {
                    UiEvent.EVENT_SUCCESS_VALIDATE_CHECKOUT -> {
                        (uiEvent.data as? CheckoutTokoFoodData)?.let {
                            analytics.clickAtc(userSession.userId, localCacheModel?.district_id, it)
                            if (uiEvent.source == MINI_CART_SOURCE){
                                goToPurchasePage()
                            }
                        }
                    }
                    UiEvent.EVENT_SUCCESS_LOAD_CART -> {
                        if (viewModel.isShownEmptyState()){
                            hideMiniCartCategory()
                            isShowMiniCart = false
                        } else {
                            showMiniCartCategory()
                            isShowMiniCart = true
                        }
                    }
                    UiEvent.EVENT_FAILED_LOAD_CART -> {
                        hideMiniCartCategory()
                        isShowMiniCart = false
                    }
                }
            }
        }
    }

    private fun observeLiveData() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.flowLayoutList.collect {
                it.first
                removeScrollListeners()
                when (it.first) {
                    is Success -> onSuccessGetCategoryLayout((it.first as Success).data)
                    is Fail ->  errorHandling((it.first as Fail).throwable, it.second)
                }

                addScrollListeners()
                if (it.second) {
                    resetSwipeLayout()
                }
            }
        }
    }

    private fun errorHandling(throwable: Throwable, isFirstTimeCall: Boolean) {
        if (isFirstTimeCall) {
            logExceptionTokoFoodCategory(
                throwable,
                TokofoodErrorLogger.ErrorType.ERROR_PAGE,
                TokofoodErrorLogger.ErrorDescription.RENDER_PAGE_ERROR
            )
            onErrorGetCategoryLayout(throwable)
        } else {
            logExceptionTokoFoodCategory(
                throwable,
                TokofoodErrorLogger.ErrorType.ERROR_LOAD_MORE_CATEGORY,
                TokofoodErrorLogger.ErrorDescription.ERROR_LOAD_MORE_CATEGORY
            )
        }
    }

    private fun setupUi() {
        view?.apply {
            rvCategory = binding?.rvCategory
            navToolbar = binding?.navToolbar
            swipeLayout = binding?.swipeRefreshLayout
            miniCartCategory = binding?.miniCartTokofoodCategory
        }
    }

    private fun setupRecycleView() {
        context?.let {
            rvCategory?.apply {
                adapter = this@TokoFoodCategoryFragment.adapter
                rvLayoutManager = CustomLinearLayoutManager(it)
                layoutManager = rvLayoutManager
            }
            rvCategory?.setItemViewCacheSize(ITEM_VIEW_CACHE_SIZE)
        }
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
                toolbar.setToolbarTitle(pageTitle)
                toolbar.setBackButtonType(NavToolbar.Companion.BackType.BACK_TYPE_BACK_WITHOUT_COLOR)
            }
        }
    }

    private fun setIconNavigation() {
        val icons =
            IconBuilder(IconBuilderFlag(pageSource = ApplinkConsInternalNavigation.SOURCE_HOME))
        navToolbar?.setIcon(icons)
    }

    private fun onSuccessGetCategoryLayout(data: TokoFoodListUiModel) {
        when (data.state) {
            TokoFoodLayoutState.SHOW -> onShowCategoryLayout(data)
            TokoFoodLayoutState.HIDE -> onHideCategoryLayout(data)
            TokoFoodLayoutState.LOADING -> onLoadingCategorylayout(data)
            else -> showCategoryLayout(data)
        }
    }

    private fun onErrorGetCategoryLayout(throwable: Throwable) {
        viewModel.setErrorState(throwable)
    }

    private fun onShowCategoryLayout(data: TokoFoodListUiModel) {
        showCategoryLayout(data)
    }

    private fun onHideCategoryLayout(data: TokoFoodListUiModel) {
        showCategoryLayout(data)
    }

    private fun onLoadingCategorylayout(data: TokoFoodListUiModel) {
        showCategoryLayout(data)
    }

    private fun showCategoryLayout(data: TokoFoodListUiModel) {
        onRenderCategoryPage()
        adapter.submitList(data.items)
    }


    private fun loadLayout() {
        getCategoryLayout()
    }

    private fun onRenderCategoryPage() {
        if (!viewModel.isShownEmptyState() && isShowMiniCart) {
            showMiniCartCategory()
        } else {
            hideMiniCartCategory()
        }
    }

    private fun updateCurrentPageLocalCacheModelData() {
        context?.let {
            localCacheModel = ChooseAddressUtils.getLocalizingAddressData(it)
        }
    }

    private fun getCategoryLayout() {
        localCacheModel?.let {
            viewModel.setCategoryLayout(
                localCacheModel = it,
                option = option,
                sortBy = sortBy,
                cuisine = cuisine,
                brandUId = brandUId
            )
        }
    }

    private fun resetSwipeLayout() {
        swipeLayout?.isEnabled = true
        swipeLayout?.isRefreshing = false
    }

    private fun removeScrollListeners() {
        rvCategory?.removeOnScrollListener(loadMoreListener)
    }

    private fun addScrollListeners() {
        rvCategory?.addOnScrollListener(loadMoreListener)
    }

    private fun removeScrollChangedListener() {
        onScrollChangedListenerList.forEach {
            view?.viewTreeObserver?.removeOnScrollChangedListener(it)
        }
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
        val layoutManager = rvCategory?.layoutManager as? LinearLayoutManager
        val index = layoutManager?.findLastVisibleItemPosition().orZero()
        val itemCount = layoutManager?.itemCount.orZero()
        localCacheModel?.let {
            viewModel.onScrollProductList(
                index,
                itemCount,
                localCacheModel = it,
                option = option,
                sortBy = sortBy,
                cuisine = cuisine,
                brandUId = brandUId
            )
        }
    }

    private fun initializeMiniCartCategory() {
        activityViewModel?.let {
            miniCartCategory?.initialize(it, viewLifecycleOwner.lifecycleScope,
                MINI_CART_SOURCE
            )
        }
    }

    private fun showMiniCartCategory() {
        setRvPadding(isShowMiniCart = true)
        miniCartCategory?.show()
    }

    private fun hideMiniCartCategory() {
        setRvPadding(isShowMiniCart = false)
        miniCartCategory?.hide()
    }

    private fun goToPurchasePage() {
        navigateToNewFragment(TokoFoodPurchaseFragment.createInstance())
    }

    private fun logExceptionTokoFoodCategory(
        throwable: Throwable,
        errorType: String,
        description: String,
    ){
        TokofoodErrorLogger.logExceptionToServerLogger(
            TokofoodErrorLogger.PAGE.CATEGORY,
            throwable,
            errorType,
            userSession.deviceId.orEmpty(),
            description
        )
    }

    private fun setRvPadding(isShowMiniCart: Boolean) {
        rvCategory?.let {
            if (isShowMiniCart){
                it.setPadding(0,0, 0, context?.resources?.
                getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl7)?: 0)
            } else {
                it.setPadding(0,0, 0,0)
            }
        }
    }
}
