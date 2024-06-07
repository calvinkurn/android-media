package com.tokopedia.inbox.universalinbox.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.analytics.byteio.addVerticalTrackListener
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.discovery.common.manager.showProductCardOptions
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.inbox.databinding.UniversalInboxFragmentBinding
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalytics
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxTopAdsAnalytic
import com.tokopedia.inbox.universalinbox.util.UniversalInboxErrorLogger
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.CHATBOT_TYPE
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.CLICK_TYPE_WISHLIST
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.COMPONENT_NAME_TOP_ADS
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.GOJEK_TYPE
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.HEADLINE_ADS_BANNER_COUNT
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.HEADLINE_POS_NOT_TO_BE_ADDED
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.PDP_EXTRA_UPDATED_POSITION
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.TOP_ADS_BANNER_COUNT
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.TOP_ADS_BANNER_POS_NOT_TO_BE_ADDED
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.VALUE_X
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.getHeadlineAdsParam
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.getRoleUser
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.getShopIdTracker
import com.tokopedia.inbox.universalinbox.util.UniversalInboxViewUtil
import com.tokopedia.inbox.universalinbox.view.adapter.UniversalInboxAdapter
import com.tokopedia.inbox.universalinbox.view.adapter.decorator.UniversalInboxRecommendationDecoration
import com.tokopedia.inbox.universalinbox.view.adapter.typefactory.UniversalInboxTypeFactory
import com.tokopedia.inbox.universalinbox.view.adapter.typefactory.UniversalInboxTypeFactoryImpl
import com.tokopedia.inbox.universalinbox.view.listener.UniversalInboxCounterListener
import com.tokopedia.inbox.universalinbox.view.listener.UniversalInboxEndlessScrollListener
import com.tokopedia.inbox.universalinbox.view.listener.UniversalInboxMenuListener
import com.tokopedia.inbox.universalinbox.view.listener.UniversalInboxWidgetListener
import com.tokopedia.inbox.universalinbox.view.uiState.UniversalInboxMenuUiState
import com.tokopedia.inbox.universalinbox.view.uimodel.MenuItemType
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxMenuUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxTopAdsBannerUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxTopadsHeadlineUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxWidgetMetaUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxWidgetUiModel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.domain.model.TopAdsImageUiModel
import com.tokopedia.topads.sdk.presentation.viewmodel.TopAdsHeadlineViewModel
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.topads.sdk.v2.listener.TopAdsImageViewClickListener
import com.tokopedia.topads.sdk.v2.tdnbanner.listener.TdnBannerResponseListener
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import com.tokopedia.wishlistcommon.util.AddRemoveWishlistV2Handler
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.absoluteValue
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class UniversalInboxFragment @Inject constructor(
    var viewModel: UniversalInboxViewModel,
    var topAdsHeadlineViewModel: TopAdsHeadlineViewModel,
    var analytics: UniversalInboxAnalytics,
    var topAdsAnalytic: UniversalInboxTopAdsAnalytic,
    var userSession: UserSessionInterface
) :
    BaseDaggerFragment(),
    UniversalInboxEndlessScrollListener.Listener,
    UniversalInboxWidgetListener,
    UniversalInboxMenuListener,
    UniversalInboxCounterListener,
    TdnBannerResponseListener,
    TopAdsImageViewClickListener,
    RecommendationListener {

    private var endlessRecyclerViewScrollListener: UniversalInboxEndlessScrollListener? = null

    private var binding: UniversalInboxFragmentBinding? by autoClearedNullable()

    private val adapter by lazy {
        UniversalInboxAdapter(
            UniversalInboxTypeFactoryImpl(
                userSession,
                this,
                this,
                this,
                this,
                this
            )
        )
    }

    // Recomm
    private var shouldTopAdsAndLoadRecommendation = true

    // TopAds Banner
    private var topAdsBannerInProductCards: List<TopAdsImageUiModel>? = null
    private var topAdsBannerExperimentPosition: Int = TOP_ADS_BANNER_POS_NOT_TO_BE_ADDED

    // TopAds Headline
    private var headlineData: CpmModel? = null
    private var headlineIndexList: ArrayList<Int>? = null
    private var headlineExperimentPosition: Int = TOP_ADS_BANNER_POS_NOT_TO_BE_ADDED

    // Tracker
    private var trackingQueue: TrackingQueue? = null
    private var shouldImpressTracker = true
    private var hasApplogScrollListener = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = UniversalInboxFragmentBinding.inflate(
            LayoutInflater.from(context),
            container,
            false
        )
        return binding?.root
    }

    override fun initInjector() {
        // no-op
    }

    override fun getScreenName(): String = TAG

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setupTrackingQueue()
    }

    private fun setupTrackingQueue() {
        context?.let {
            trackingQueue = TrackingQueue(it)
        }
    }

    override fun onPause() {
        super.onPause()
        TopAdsGtmTracker.getInstance().eventInboxProductView(trackingQueue)
        topAdsAnalytic.eventInboxTopAdsProductView(trackingQueue)
        trackingQueue?.sendAll()
    }

    private fun initViews() {
        setupRecyclerView()
        setupRecyclerViewLoadMore()
        setupObservers()
        setupListeners()
        addRecommendationScrollListener()
    }

    private fun setupRecyclerView() {
        binding?.inboxRv?.layoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        ).also {
            it.isItemPrefetchEnabled = true
        }
        binding?.inboxRv?.setHasFixedSize(true)
        binding?.inboxRv?.itemAnimator = null
        binding?.inboxRv?.adapter = adapter
        binding?.inboxRv?.isNestedScrollingEnabled = false
        binding?.inboxRv?.addItemDecoration(UniversalInboxRecommendationDecoration())
    }

    private fun addRecommendationScrollListener() {
        if(hasApplogScrollListener) return
        binding?.inboxRv?.addVerticalTrackListener()
        hasApplogScrollListener = true
    }

    private fun setupRecyclerViewLoadMore() {
        if (endlessRecyclerViewScrollListener == null) {
            binding?.inboxRv?.layoutManager?.let {
                endlessRecyclerViewScrollListener = UniversalInboxEndlessScrollListener(
                    layoutManager = it as StaggeredGridLayoutManager,
                    this@UniversalInboxFragment
                )
            }
        }
        endlessRecyclerViewScrollListener?.let {
            binding?.inboxRv?.addOnScrollListener(it)
        }
    }

    private fun setupObservers() {
        // Setup flow observer
        viewModel.setupViewModelObserver()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                observeProductRecommendation()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                observeInboxMenuAndCounter()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                observeInboxNavigation()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                observeAutoScrollUiState()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                observeError()
            }
        }
    }

    private suspend fun observeInboxMenuAndCounter() {
        // Inbox Menu & Counter
        viewModel.inboxMenuUiState.collectLatest {
            // Set loading
            toggleLoading(it.isLoading)

            if (!it.isLoading) {
                // Set widget meta
                updateWidgetMeta(widget = it.widgetMeta)

                // Set menu list
                updateMenuList(newList = it.menuList + it.miscList)

                // Update counters
                updateNotificationCounter(it.notificationCounter)

                // Load recommendation only when first load
                if (shouldTopAdsAndLoadRecommendation) {
                    shouldTopAdsAndLoadRecommendation = false // do not load again
                    loadTopAdsAndRecommendation()
                }

                // Track impression
                // Double flag: first flag means view is ready, second flag means impression has not tracked yet
                if (it.shouldTrackImpression && shouldImpressTracker) {
                    shouldImpressTracker = false // do not track again
                    trackInboxPageImpression(it)
                }
            }
        }
    }

    private fun toggleLoading(isLoading: Boolean) {
        binding?.inboxLayoutSwipeRefresh?.isRefreshing = isLoading
    }

    private fun updateWidgetMeta(widget: UniversalInboxWidgetMetaUiModel) {
        // Widget has meaning to be shown
        if (widget.widgetList.isNotEmpty() || widget.widgetError.isError) {
            adapter.tryUpdateWidgetMeta(widget)
        } else if (adapter.isWidgetMetaAdded()) { // if widget is exist in the list
            adapter.tryRemoveItemAtPosition(0)
        }
    }

    private fun updateMenuList(newList: List<Visitable<in UniversalInboxTypeFactory>>) {
        if (newList.isEmpty()) return
        adapter.tryUpdateMenuItemsAtPosition(newList = newList)
    }

    private suspend fun observeInboxNavigation() {
        viewModel.inboxNavigationUiState.collectLatest {
            val resultLauncher = when (it.requestType) {
                UniversalInboxRequestType.REQUEST_GENERAL -> {
                    inboxMenuResultLauncher
                }
                UniversalInboxRequestType.REQUEST_WITH_PRODUCT_RECOMMENDATION -> {
                    inboxProductRecommendationResultLauncher
                }
            }
            when {
                (it.intent != null) -> {
                    resultLauncher.launch(it.intent)
                }
                (it.applink.isNotBlank() && context != null) -> {
                    val intent = RouteManager.getIntent(context, it.applink)
                    resultLauncher.launch(intent)
                }
            }
        }
    }

    private fun updateNotificationCounter(counter: String) {
        if (activity is UniversalInboxActivity) {
            val notifUnread = counter.toIntOrZero()
            (activity as UniversalInboxActivity).updateNotificationCounter(
                UniversalInboxViewUtil.getStringCounter(
                    notifUnread
                )
            )
        }
    }

    private suspend fun observeProductRecommendation() {
        viewModel.productRecommendationUiState.collectLatest {
            // Add / Remove loading view
            toggleLoadingProductRecommendation(it.isLoading)

            // Scroll to top when it is loading & empty product list
            // It means refresh
            if (it.isLoading && it.productRecommendation.isEmpty()) {
                adapter.getMenuSeparatorPosition()?.let { position ->
                    binding?.inboxRv?.scrollToPosition(position)
                }
            }

            // Update view only when not loading (not waiting for network)
            // Or product recommendation is empty (refresh / re-shuffle)
            if (!it.isLoading && it.productRecommendation.isNotEmpty()) {
                addProductRecommendation(
                    title = it.title,
                    newList = it.productRecommendation
                )
            }
        }
    }

    private fun toggleLoadingProductRecommendation(isLoading: Boolean) {
        if (isLoading) {
            adapter.addProductRecommendationLoader()
        } else {
            adapter.removeProductRecommendationLoader()
        }
    }

    private fun addProductRecommendation(
        title: String,
        newList: List<Visitable<in UniversalInboxTypeFactory>>
    ) {
        removeProductRecommendation()
        val editedNewList = newList.toMutableList()
        setHeadlineAndBannerExperiment(editedNewList)
        adapter.tryAddProductRecommendation(title, editedNewList)
        endlessRecyclerViewScrollListener?.updateStateAfterGetData()
    }

    private fun removeProductRecommendation() {
        if (endlessRecyclerViewScrollListener?.currentPage.toZeroIfNull() <= 0) {
            adapter.tryRemoveProductRecommendation()
        }
    }

    private suspend fun observeAutoScrollUiState() {
        viewModel.autoScrollUiState.collectLatest {
            if (it.shouldScroll) {
                binding?.inboxRv?.let { recyclerView ->
                    // Scroll one screen away
                    recyclerView.smoothScrollBy(0, recyclerView.height)
                }
                // Reset after scroll
                viewModel.processAction(UniversalInboxAction.ResetUserScrollState)
            }
        }
    }

    private suspend fun observeError() {
        viewModel.errorUiState.collectLatest {
            // Log Error and Show Toaster
            logErrorAndShowToaster(it.error)
            // Enable refresh
            // Sometimes swipe refresh is not updating the UI
            // this is to re-trigger that in case loading got stuck
            binding?.inboxLayoutSwipeRefresh?.isRefreshing = false
        }
    }

    private fun logErrorAndShowToaster(errorPair: Pair<Throwable, String>?) {
        errorPair?.let { (throwable, methodName) ->
            Timber.d(throwable)
            if (view != null) {
                Toaster.build(
                    requireView(),
                    ErrorHandler.getErrorMessage(context, throwable),
                    Snackbar.LENGTH_LONG,
                    Toaster.TYPE_ERROR
                ).show()
            }

            UniversalInboxErrorLogger.logExceptionToServerLogger(
                throwable,
                userSession.deviceId.orEmpty(),
                methodName
            )
        }
    }

    private fun setHeadlineAndBannerExperiment(
        newList: MutableList<Visitable<in UniversalInboxTypeFactory>>
    ) {
        try {
            setTopAdsHeadlineExperiment(newList)
            setTopAdsBannerExperiment(newList)
        } catch (throwable: Throwable) {
            context?.let {
                ErrorHandler.getErrorMessage(it, throwable)
            }
        }
    }

    private fun setTopAdsHeadlineExperiment(newList: MutableList<Visitable<in UniversalInboxTypeFactory>>) {
        var index = Int.ZERO
        if (headlineIndexList != null && headlineIndexList?.isNotEmpty() == true) {
            val pageNum = endlessRecyclerViewScrollListener?.currentPage.toZeroIfNull()
            // Get headline position for first page recommendation
            if (pageNum == 1) {
                headlineExperimentPosition =
                    headlineIndexList?.get(Int.ZERO) ?: HEADLINE_POS_NOT_TO_BE_ADDED
                // If the headline index size is 2 and second page
            } else if (headlineIndexList?.size == HEADLINE_ADS_BANNER_COUNT &&
                pageNum == 2
            ) {
                headlineExperimentPosition =
                    headlineIndexList?.get(Int.ONE) ?: HEADLINE_POS_NOT_TO_BE_ADDED // Get the second index
                index = Int.ONE // Set index for topAds sdk
            }
            if (
                (
                    headlineExperimentPosition != HEADLINE_POS_NOT_TO_BE_ADDED ||
                        (
                            headlineIndexList?.size == HEADLINE_ADS_BANNER_COUNT &&
                                pageNum < HEADLINE_ADS_BANNER_COUNT
                            ) // If the headline index size is 2 and page number is 1 (second page)
                    ) &&
                headlineExperimentPosition <= (adapter.itemCount + newList.size) // Prevent out of bound exception
            ) {
                // Ex: headline exp position 36
                // existing product recommendation total is 20 and new product recommendation is 20
                // 36 - 20 = 16, add to position 16 in the new list
                val totalProductRecommendation = adapter.itemCount - (adapter.getProductRecommendationFirstPosition() ?: adapter.itemCount)
                val positionToAdd = (headlineExperimentPosition - totalProductRecommendation).absoluteValue
                if (positionToAdd <= newList.size - 1) {
                    newList.add(
                        positionToAdd,
                        UniversalInboxTopadsHeadlineUiModel(headlineData, Int.ZERO, index)
                    )
                }
            }
        }
    }

    private fun setTopAdsBannerExperiment(newList: MutableList<Visitable<in UniversalInboxTypeFactory>>) {
        if (topAdsBannerExperimentPosition != TOP_ADS_BANNER_POS_NOT_TO_BE_ADDED &&
            topAdsBannerExperimentPosition <= adapter.itemCount // Prevent out of bound
        ) {
            val position = if (
                // Ex. headline pos 30 and banner pos 35 -> 5 % 2 = 1 -> odd number of products
                abs(headlineExperimentPosition - topAdsBannerExperimentPosition) % 2 == 0
            ) {
                topAdsBannerExperimentPosition + 1 // Shift to even the number of products
            } else {
                topAdsBannerExperimentPosition
            }
            // Ex: banner exp position 36
            // existing product recommendation total is 20 and new product recommendation is 20
            // 36 - 20 = 16, add to position 16 in the new list
            val totalProductRecommendation = adapter.itemCount - (adapter.getProductRecommendationFirstPosition() ?: adapter.itemCount)
            val positionToAdd = (position - totalProductRecommendation).absoluteValue
            if (positionToAdd <= newList.size - 1) {
                newList.add(
                    positionToAdd,
                    UniversalInboxTopAdsBannerUiModel(topAdsBannerInProductCards)
                )
            }
        }
    }

    override fun loadWidgetMetaAndCounter() {
        shouldImpressTracker = true
        shouldTopAdsAndLoadRecommendation = true
        viewModel.processAction(UniversalInboxAction.RefreshPage)
    }

    private fun loadTopAdsAndRecommendation() {
        topAdsHeadlineViewModel.getTopAdsHeadlineData(
            getHeadlineAdsParam(Int.ZERO, userSession.userId),
            { data ->
                headlineData = data
                if (data.data.isEmpty()) {
                    return@getTopAdsHeadlineData
                }
                setHeadlineIndexList(data)
                viewModel.processAction(UniversalInboxAction.RefreshRecommendation)
                endlessRecyclerViewScrollListener?.resetState()
            },
            {
                viewModel.processAction(UniversalInboxAction.RefreshRecommendation)
                endlessRecyclerViewScrollListener?.resetState()
            }
        )
    }

    private fun setHeadlineIndexList(data: CpmModel) {
        headlineIndexList = ArrayList()
        val size = data.header.totalData
        for (i in 0 until size) {
            headlineIndexList?.add(data.data[i].cpm.position + adapter.itemCount)
        }
    }

    private fun setupListeners() {
        binding?.inboxLayoutSwipeRefresh?.setColorSchemeResources(
            unifyprinciplesR.color.Unify_GN500
        )
        binding?.inboxLayoutSwipeRefresh?.setOnRefreshListener {
            loadWidgetMetaAndCounter()
        }
        if (activity is UniversalInboxActivity) {
            (activity as UniversalInboxActivity).listener = this
        }
    }

    override fun onClickWidget(item: UniversalInboxWidgetUiModel) {
        if (item.applink.isEmpty()) return
        when (item.type) {
            CHATBOT_TYPE -> {
                analytics.clickOnHelp(
                    abVariant = UniversalInboxValueUtil.VAR_B,
                    userRole = getRoleUser(userSession),
                    shopId = getShopIdTracker(userSession),
                    helpCounter = item.counter.toString()
                )
            }
            GOJEK_TYPE -> {
                analytics.clickOnChatDriver(
                    abVariant = UniversalInboxValueUtil.VAR_B,
                    userRole = getRoleUser(userSession),
                    shopId = getShopIdTracker(userSession),
                    chatDriverCounter = item.counter.toString()
                )
            }
        }
        viewModel.processAction(
            UniversalInboxAction.NavigateToPage(
                item.applink,
                UniversalInboxRequestType.REQUEST_GENERAL
            )
        )
    }

    override fun onRefreshWidgetMeta() {
        if (adapter.isWidgetMetaAdded()) {
            (adapter.getInboxItem(0) as UniversalInboxWidgetMetaUiModel)
                .widgetError.isLocalLoadLoading = true // flag local loading has started loading
        }
        loadWidgetMetaAndCounter()
    }

    override fun onRefreshWidgetCard(item: UniversalInboxWidgetUiModel) {
        when (item.type) {
            CHATBOT_TYPE -> {
                loadWidgetMetaAndCounter()
            }
            GOJEK_TYPE -> {
                viewModel.processAction(UniversalInboxAction.RefreshDriverWidget)
            }
        }
    }

    private fun trackInboxPageImpression(
        menuUiState: UniversalInboxMenuUiState
    ) {
        val shopId = getShopIdTracker(userSession)
        val sellerChatCounter = if (shopId != VALUE_X) {
            menuUiState.getSellerUnread().toString()
        } else {
            VALUE_X
        }
        val helpCounter = if (menuUiState.getHelpUnread() != null) {
            menuUiState.getHelpUnread().toString()
        } else {
            VALUE_X
        }
        analytics.viewOnInboxPage(
            abVariant = UniversalInboxValueUtil.VAR_B,
            userRole = getRoleUser(userSession),
            shopId = shopId,
            sellerChatCounter = sellerChatCounter,
            buyerChatCounter = menuUiState.getBuyerUnread().toString(),
            discussionCounter = menuUiState.getDiscussionUnread().toString(),
            reviewCounter = menuUiState.getReviewUnread().toString(),
            notifCenterCounter = menuUiState.notificationCounter,
            driverCounter = VALUE_X, // Temporary always X until phase 2
            helpCounter = helpCounter
        )
    }

    override fun onMenuClicked(item: UniversalInboxMenuUiModel) {
        if (item.applink.isEmpty()) return
        when (item.type) {
            MenuItemType.CHAT_BUYER -> {
                analytics.clickOnBuyerChat(
                    abVariant = UniversalInboxValueUtil.VAR_B,
                    userRole = getRoleUser(userSession),
                    shopId = getShopIdTracker(userSession),
                    buyerChatCounter = item.counter.toString()
                )
            }
            MenuItemType.CHAT_SELLER -> {
                analytics.clickOnSellerChat(
                    abVariant = UniversalInboxValueUtil.VAR_B,
                    userRole = getRoleUser(userSession),
                    shopId = getShopIdTracker(userSession),
                    sellerChatCounter = item.counter.toString()
                )
            }
            MenuItemType.DISCUSSION -> {
                analytics.sendNewPageInboxTalkTracking(userSession.userId, item.counter.toString())
                analytics.clickOnDiscussion(
                    abVariant = UniversalInboxValueUtil.VAR_B,
                    userRole = getRoleUser(userSession),
                    shopId = getShopIdTracker(userSession),
                    discussionCounter = item.counter.toString()
                )
            }
            MenuItemType.REVIEW -> {
                analytics.clickOnReview(
                    abVariant = UniversalInboxValueUtil.VAR_B,
                    userRole = getRoleUser(userSession),
                    shopId = getShopIdTracker(userSession),
                    reviewCounter = item.counter.toString()
                )
            }
            else -> Unit // no-op
        }
        viewModel.processAction(
            UniversalInboxAction.NavigateToPage(
                item.applink,
                UniversalInboxRequestType.REQUEST_GENERAL
            )
        )
    }

    override fun onNotificationIconClicked(counter: String) {
        analytics.clickOnNotifCenter(
            abVariant = UniversalInboxValueUtil.VAR_B,
            userRole = getRoleUser(userSession),
            shopId = getShopIdTracker(userSession),
            notifCenterCounter = counter
        )
        viewModel.processAction(
            UniversalInboxAction.NavigateToPage(
                ApplinkConst.NOTIFICATION,
                UniversalInboxRequestType.REQUEST_GENERAL
            )
        )
    }

    override fun onLoadMore(page: Int, totalItemsCount: Int) {
        viewModel.processAction(UniversalInboxAction.LoadNextPage(page))
    }

    override fun onTdnBannerResponse(categoriesList: MutableList<List<TopAdsImageUiModel>>) {
        if (categoriesList.isEmpty()) return
        // If response size is 2, then there are 2 types of banner (carousel & single)
        // one below static menu & one in the product recommendation
        if (categoriesList.size == TOP_ADS_BANNER_COUNT) {
            topAdsBannerInProductCards = categoriesList[Int.ONE]
            setTopAdsBannerExperimentPosition()
            // If response first index size is 2, then there are 2 single banners
        } else if (categoriesList[Int.ZERO].size == TOP_ADS_BANNER_COUNT) {
            topAdsBannerInProductCards = listOf(categoriesList[Int.ZERO][Int.ONE])
            setTopAdsBannerExperimentPosition()
        }
        // Notify the first banner below static menu
        adapter.updateFirstTopAdsBanner(categoriesList[Int.ZERO])
    }
    private fun setTopAdsBannerExperimentPosition() {
        if (!topAdsBannerInProductCards.isNullOrEmpty()) {
            val topAdsBannerInCardsPosition =
                topAdsBannerInProductCards?.get(Int.ZERO)?.position ?: -1
            val productRecommendationFirstPosition =
                adapter.getProductRecommendationFirstPosition() ?: adapter.itemCount
            topAdsBannerExperimentPosition =
                topAdsBannerInCardsPosition + productRecommendationFirstPosition
        }
    }

    override fun onError(t: Throwable) {
        // Do nothing
        Timber.d(t)
    }

    override fun onTopAdsImageViewClicked(applink: String?) {
        if (applink == null) return
        val intent = RouteManager.getIntent(context, applink)
        startActivity(intent)
    }

    override fun onProductClick(
        item: RecommendationItem,
        layoutType: String?,
        vararg position: Int
    ) {
        if (item.isTopAds) {
            onClickTopAds(item)
        } else {
            onClickOrganic(item)
        }
        val intent = RouteManager.getIntent(
            context,
            ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
            item.productId.toString()
        )
        if (position.isNotEmpty()) {
            intent.putExtra(PDP_EXTRA_UPDATED_POSITION, position[Int.ZERO])
        }
        viewModel.processAction(
            UniversalInboxAction.NavigateWithIntent(
                intent,
                UniversalInboxRequestType.REQUEST_WITH_PRODUCT_RECOMMENDATION
            )
        )
    }

    private fun onClickTopAds(item: RecommendationItem) {
        TopAdsUrlHitter(context).hitClickUrl(
            activity?.javaClass?.name,
            item.clickUrl,
            item.productId.toString(),
            item.name,
            item.imageUrl,
            COMPONENT_NAME_TOP_ADS
        )
        topAdsAnalytic.eventInboxTopAdsProductClick(
            item,
            item.position,
            item.isTopAds
        )
    }

    private fun onClickOrganic(item: RecommendationItem) {
        topAdsAnalytic.eventInboxTopAdsProductClick(
            item,
            item.position,
            item.isTopAds
        )
    }

    override fun onProductImpression(item: RecommendationItem) {
        if (item.isTopAds) {
            onImpressionTopAds(item)
        } else {
            onImpressionOrganic(item)
        }
    }

    private fun onImpressionTopAds(item: RecommendationItem) {
        TopAdsUrlHitter(context).hitImpressionUrl(
            activity?.javaClass?.name,
            item.trackerImageUrl,
            item.productId.toString(),
            item.name,
            item.imageUrl,
            COMPONENT_NAME_TOP_ADS
        )
        topAdsAnalytic.addInboxTopAdsProductViewImpressions(item, item.position, item.isTopAds)
    }

    private fun onImpressionOrganic(item: RecommendationItem) {
        topAdsAnalytic.addInboxTopAdsProductViewImpressions(item, item.position, item.isTopAds)
    }

    override fun onWishlistV2Click(item: RecommendationItem, isAddWishlist: Boolean) {
        if (isAddWishlist) {
            // Anonymous used because we need RecommendationItem
            viewModel.addWishlistV2(
                item,
                object : WishlistV2ActionListener {
                    override fun onSuccessAddWishlist(
                        result: AddToWishlistV2Response.Data.WishlistAddV2,
                        productId: String
                    ) {
                        showSuccessAddWishlistV2(wishlistAddResult = result)
                        if (item.isTopAds) {
                            onClickTopAdsWishlistItem(item)
                        }
                    }
                    override fun onErrorAddWishList(throwable: Throwable, productId: String) {
                        showErrorAddRemoveWishlistV2(throwable = throwable)
                    }

                    override fun onErrorRemoveWishlist(throwable: Throwable, productId: String) {}
                    override fun onSuccessRemoveWishlist(
                        result: DeleteWishlistV2Response.Data.WishlistRemoveV2,
                        productId: String
                    ) {}
                }
            )
        } else {
            viewModel.removeWishlistV2(
                item,
                object : WishlistV2ActionListener {
                    override fun onErrorRemoveWishlist(throwable: Throwable, productId: String) {
                        showErrorAddRemoveWishlistV2(throwable = throwable)
                    }
                    override fun onSuccessRemoveWishlist(
                        result: DeleteWishlistV2Response.Data.WishlistRemoveV2,
                        productId: String
                    ) {
                        showSuccessRemoveWishlistV2(wishListRemoveResult = result)
                    }

                    override fun onErrorAddWishList(throwable: Throwable, productId: String) {}
                    override fun onSuccessAddWishlist(
                        result: AddToWishlistV2Response.Data.WishlistAddV2,
                        productId: String
                    ) {}
                }
            )
        }
    }

    private fun onClickTopAdsWishlistItem(item: RecommendationItem) {
        TopAdsUrlHitter(context).hitClickUrl(
            activity?.javaClass?.name,
            item.clickUrl + CLICK_TYPE_WISHLIST,
            item.productId.toString(),
            item.name,
            item.imageUrl,
            COMPONENT_NAME_TOP_ADS
        )
    }

    override fun onThreeDotsClick(item: RecommendationItem, vararg position: Int) {
        if (position.isEmpty()) return
        showProductCardOptions(this, createProductCardOptionsModel(item, position[Int.ZERO]))
    }

    private fun createProductCardOptionsModel(
        recommendationItem: RecommendationItem,
        productPosition: Int
    ): ProductCardOptionsModel {
        val productCardOptionsModel = ProductCardOptionsModel()
        productCardOptionsModel.hasWishlist = true
        productCardOptionsModel.isWishlisted = recommendationItem.isWishlist
        productCardOptionsModel.productId = recommendationItem.productId.toString()
        productCardOptionsModel.isTopAds = recommendationItem.isTopAds
        productCardOptionsModel.topAdsWishlistUrl = recommendationItem.wishlistUrl
        productCardOptionsModel.productPosition = productPosition
        return productCardOptionsModel
    }

    /**
     * Result launcher section
     */
    private val inboxMenuResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        viewModel.processAction(UniversalInboxAction.RefreshCounter)
    }

    private val inboxProductRecommendationResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        viewModel.processAction(UniversalInboxAction.RefreshCounter)
        viewModel.processAction(UniversalInboxAction.AutoScrollRecommendation)
    }

    private fun showSuccessAddWishlistV2(
        wishlistAddResult: AddToWishlistV2Response.Data.WishlistAddV2? = null,
        wishlistResult: ProductCardOptionsModel.WishlistResult? = null
    ) {
        val view: View = activity?.findViewById(android.R.id.content) ?: return
        context?.run {
            wishlistAddResult?.let {
                AddRemoveWishlistV2Handler.showAddToWishlistV2SuccessToaster(
                    it,
                    this,
                    view
                )
            }
            wishlistResult?.let {
                AddRemoveWishlistV2Handler.showAddToWishlistV2SuccessToaster(
                    it,
                    this,
                    view
                )
            }
        }
    }

    private fun showSuccessRemoveWishlistV2(
        wishListRemoveResult: DeleteWishlistV2Response.Data.WishlistRemoveV2? = null,
        wishlistResult: ProductCardOptionsModel.WishlistResult? = null
    ) {
        val view: View = activity?.findViewById(android.R.id.content) ?: return
        context?.run {
            wishListRemoveResult?.let {
                AddRemoveWishlistV2Handler.showRemoveWishlistV2SuccessToaster(
                    it,
                    this,
                    view
                )
            }
            wishlistResult?.let {
                AddRemoveWishlistV2Handler.showRemoveWishlistV2SuccessToaster(
                    it,
                    this,
                    view
                )
            }
        }
    }

    private fun showErrorAddRemoveWishlistV2(
        wishlistResult: ProductCardOptionsModel.WishlistResult? = null,
        throwable: Throwable? = null
    ) {
        val view: View = activity?.findViewById(android.R.id.content) ?: return
        context?.let {
            var errorMessage = ErrorHandler.getErrorMessage(it, throwable)
            if (wishlistResult != null) {
                if (wishlistResult.messageV2.isNotEmpty()) errorMessage = wishlistResult.messageV2
                if (wishlistResult.ctaTextV2.isNotEmpty() && wishlistResult.ctaActionV2.isNotEmpty()) {
                    AddRemoveWishlistV2Handler.showWishlistV2ErrorToasterWithCta(
                        errorMessage,
                        wishlistResult.ctaTextV2,
                        wishlistResult.ctaActionV2,
                        view,
                        it
                    )
                    return
                }
            }
            AddRemoveWishlistV2Handler.showWishlistV2ErrorToaster(errorMessage, view)
        }
    }

    companion object {
        private const val TAG = "UniversalInboxFragment"
        private const val RV_SCROLL_OFFSET = 8

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle = Bundle()
        ): UniversalInboxFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? UniversalInboxFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                UniversalInboxFragment::class.java.name
            ).apply {
                arguments = bundle
            } as UniversalInboxFragment
        }
    }
}
