package com.tokopedia.inbox.universalinbox.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.discovery.common.manager.ProductCardOptionsWishlistCallback
import com.tokopedia.discovery.common.manager.handleProductCardOptionsActivityResult
import com.tokopedia.discovery.common.manager.showProductCardOptions
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.inbox.databinding.UniversalInboxFragmentBinding
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalytics
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxTopAdsAnalytic
import com.tokopedia.inbox.universalinbox.data.response.counter.UniversalInboxAllCounterResponse
import com.tokopedia.inbox.universalinbox.di.UniversalInboxComponent
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.CHATBOT_TYPE
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.CLICK_TYPE_WISHLIST
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.COMPONENT_NAME_TOP_ADS
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.GOJEK_TYPE
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.HEADLINE_ADS_BANNER_COUNT
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.HEADLINE_POS_NOT_TO_BE_ADDED
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.PDP_EXTRA_UPDATED_POSITION
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.REQUEST_FROM_PDP
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.SHIFTING_INDEX
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.TOP_ADS_BANNER_COUNT
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.TOP_ADS_BANNER_POS_NOT_TO_BE_ADDED
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.VALUE_X
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.WISHLIST_STATUS_IS_WISHLIST
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.getHeadlineAdsParam
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.getRoleUser
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.getShopIdTracker
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.getVariantTracker
import com.tokopedia.inbox.universalinbox.view.adapter.UniversalInboxAdapter
import com.tokopedia.inbox.universalinbox.view.adapter.decorator.UniversalInboxRecommendationDecoration
import com.tokopedia.inbox.universalinbox.view.listener.UniversalInboxCounterListener
import com.tokopedia.inbox.universalinbox.view.listener.UniversalInboxEndlessScrollListener
import com.tokopedia.inbox.universalinbox.view.listener.UniversalInboxMenuListener
import com.tokopedia.inbox.universalinbox.view.listener.UniversalInboxWidgetListener
import com.tokopedia.inbox.universalinbox.view.uimodel.MenuItemType
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxMenuUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxRecommendationLoaderUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxRecommendationTitleUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxTopAdsBannerUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxTopadsHeadlineUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxWidgetUiModel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.listener.TdnBannerResponseListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewClickListener
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.topads.sdk.viewmodel.TopAdsHeadlineViewModel
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import com.tokopedia.wishlistcommon.util.AddRemoveWishlistV2Handler
import timber.log.Timber
import javax.inject.Inject

class UniversalInboxFragment :
    BaseDaggerFragment(),
    UniversalInboxEndlessScrollListener.Listener,
    UniversalInboxWidgetListener,
    UniversalInboxMenuListener,
    UniversalInboxCounterListener,
    TdnBannerResponseListener,
    TopAdsImageViewClickListener,
    RecommendationListener {

    private var endlessRecyclerViewScrollListener: UniversalInboxEndlessScrollListener? = null

    @Inject
    lateinit var viewModel: UniversalInboxViewModel

    @Inject
    lateinit var topAdsHeadlineViewModel: TopAdsHeadlineViewModel

    @Inject
    lateinit var analytics: UniversalInboxAnalytics

    @Inject
    lateinit var topAdsAnalytic: UniversalInboxTopAdsAnalytic

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var abTestPlatform: AbTestPlatform

    private var binding: UniversalInboxFragmentBinding? by autoClearedNullable()
    private lateinit var adapter: UniversalInboxAdapter

    // TopAds Banner
    private var topAdsBannerInProductCards: List<TopAdsImageViewModel>? = null
    private var topAdsBannerExperimentPosition: Int = TOP_ADS_BANNER_POS_NOT_TO_BE_ADDED
    private var isTopAdsBannerAdded = false

    // TopAds Headline
    private var headlineData: CpmModel? = null
    private var headlineIndexList: ArrayList<Int>? = null
    private var headlineExperimentPosition: Int = TOP_ADS_BANNER_POS_NOT_TO_BE_ADDED
    private var isAdded = false

    private var trackingQueue: TrackingQueue? = null

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
        getComponent(UniversalInboxComponent::class.java).inject(this)
    }

    override fun getScreenName(): String = TAG

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view, savedInstanceState)
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

    private fun initViews(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        setupRecyclerViewLoadMore()
        setupObservers()
        setupInboxMenu()
        setupListeners()
    }

    private fun setupRecyclerView() {
        adapter = UniversalInboxAdapter(
            userSession,
            this,
            this,
            this,
            this,
            this
        )
        binding?.inboxRv?.layoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )
        binding?.inboxRv?.setHasFixedSize(true)
        binding?.inboxRv?.itemAnimator = null
        binding?.inboxRv?.adapter = adapter
        binding?.inboxRv?.addItemDecoration(UniversalInboxRecommendationDecoration())
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
        viewModel.inboxMenu.observe(viewLifecycleOwner) {
            binding?.inboxLayoutSwipeRefresh?.isRefreshing = false
            if (it.isNotEmpty()) {
                adapter.addItems(it)
                binding?.inboxRv?.post {
                    adapter.notifyItemRangeChanged(Int.ZERO, it.size - Int.ONE)
                }
            }
            loadWidgetMetaAndCounter()
            loadTopAdsAndRecommendation()
        }

        viewModel.widget.observe(viewLifecycleOwner) { (widget, counter) ->
            if (adapter.isWidgetMetaAdded()) {
                adapter.removeItemAt(Int.ZERO)
            }
            // If not empty (if empty then should hide) or Error, show the widget meta
            if (widget.widgetList.isNotEmpty() || widget.isError) {
                adapter.addItem(Int.ZERO, widget)
            }
            binding?.inboxRv?.post {
                val rangePosition = adapter.getFirstTopAdsBannerPositionPair()?.first
                adapter.notifyItemRangeChanged(Int.ZERO, rangePosition ?: adapter.itemCount)
                counter?.let {
                    trackInboxPageImpression(it)
                }
            }
        }

        viewModel.allCounter.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    // Update notif & static menu counters
                    if (activity is UniversalInboxActivity) {
                        val notifUnread = it.data.notifCenterUnread.notifUnread
                        if (notifUnread.toIntOrZero() > Int.ZERO) {
                            (activity as UniversalInboxActivity).updateNotificationCounter(
                                it.data.notifCenterUnread.notifUnread
                            )
                        }
                    }
                    val updatedMenuList = adapter.updateAllCounters(it.data)
                    binding?.inboxRv?.post {
                        if (updatedMenuList.isNotEmpty()) {
                            adapter.notifyItemRangeChanged(Int.ZERO, updatedMenuList.size)
                        }
                    }
                }
                is Fail -> {
                    // Do nothing
                }
            }
        }

        viewModel.firstPageRecommendation.observe(viewLifecycleOwner) {
            removeLoadMoreLoading()
            when (it) {
                is Success -> onSuccessGetFirstRecommendationData(it.data)
                is Fail -> {}
            }
        }

        viewModel.morePageRecommendation.observe(viewLifecycleOwner) {
            removeLoadMoreLoading()
            when (it) {
                is Success -> {
                    addRecommendationItem(it.data)
                }
                is Fail -> {}
            }
        }
    }

    private fun onSuccessGetFirstRecommendationData(
        recommendation: RecommendationWidget
    ) {
        adapter.addItem(UniversalInboxRecommendationTitleUiModel(recommendation.title))
        addRecommendationItem(recommendation.recommendationItemList)
    }

    private fun addRecommendationItem(list: List<RecommendationItem>) {
        val itemCountBefore = adapter.itemCount
        adapter.addItems(list)
        binding?.inboxRv?.post {
            adapter.notifyItemRangeInserted(itemCountBefore, itemCountBefore + list.size)
        }
        setHeadlineAndBannerExperiment()
        endlessRecyclerViewScrollListener?.updateStateAfterGetData()
    }

    private fun setHeadlineAndBannerExperiment() {
        try {
            setTopAdsHeadlineExperiment()
            setTopAdsBannerExperiment()
        } catch (throwable: Throwable) {
            context?.let {
                ErrorHandler.getErrorMessage(it, throwable)
            }
        }
    }

    private fun setTopAdsHeadlineExperiment() {
        var index = Int.ZERO
        if (headlineIndexList != null && headlineIndexList?.isNotEmpty() == true) {
            val pageNum = endlessRecyclerViewScrollListener?.currentPage ?: Int.ZERO
            if (pageNum == Int.ZERO) { // Get headline position for first page recommendation
                headlineExperimentPosition =
                    headlineIndexList?.get(Int.ZERO) ?: HEADLINE_POS_NOT_TO_BE_ADDED
                // If the headline index size is 2 and page number is 1 (second page)
            } else if (headlineIndexList?.size == HEADLINE_ADS_BANNER_COUNT &&
                pageNum < HEADLINE_ADS_BANNER_COUNT
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
                headlineExperimentPosition <= adapter.itemCount && // Prevent out of bound exception
                !isAdded // Only 1 headline allowed
            ) {
                addTopAdsHeadlineUiModel(index)
            }
        }
    }

    private fun addTopAdsHeadlineUiModel(index: Int) {
        val position = if (isTopAdsBannerAdded) {
            headlineExperimentPosition + SHIFTING_INDEX
        } else {
            headlineExperimentPosition
        }
        adapter.addItem(
            position,
            UniversalInboxTopadsHeadlineUiModel(headlineData, Int.ZERO, index)
        )
        binding?.inboxRv?.post {
            adapter.notifyItemInserted(position)
        }
        isAdded = true
    }

    private fun setTopAdsBannerExperiment() {
        if (topAdsBannerExperimentPosition != TOP_ADS_BANNER_POS_NOT_TO_BE_ADDED &&
            topAdsBannerExperimentPosition <= adapter.itemCount && // Prevent out of bound
            !isTopAdsBannerAdded // Only one banner experiment allowed
        ) {
            val position = if (isAdded) {
                topAdsBannerExperimentPosition + SHIFTING_INDEX
            } else {
                topAdsBannerExperimentPosition
            }
            adapter.addItem(
                position,
                UniversalInboxTopAdsBannerUiModel(topAdsBannerInProductCards)
            )
            binding?.inboxRv?.post {
                adapter.notifyItemInserted(position)
            }
            isTopAdsBannerAdded = true
        }
    }

    private fun setupInboxMenu() {
        binding?.inboxLayoutSwipeRefresh?.isRefreshing = true
        viewModel.generateStaticMenu()
    }

    override fun loadWidgetMetaAndCounter() {
        viewModel.loadWidgetMetaAndCounter()
    }

    private fun loadTopAdsAndRecommendation() {
        showLoadMoreLoading()
        topAdsHeadlineViewModel.getTopAdsHeadlineData(
            getHeadlineAdsParam(Int.ZERO, userSession.userId),
            { data ->
                headlineData = data
                if (data.data.isEmpty()) {
                    return@getTopAdsHeadlineData
                }
                setHeadlineIndexList(data)
                viewModel.loadFirstPageRecommendation()
            },
            {
                viewModel.loadFirstPageRecommendation()
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
            com.tokopedia.unifyprinciples.R.color.Unify_GN500
        )
        binding?.inboxLayoutSwipeRefresh?.setOnRefreshListener {
            isAdded = false
            isTopAdsBannerAdded = false
            endlessRecyclerViewScrollListener?.resetState()
            adapter.clearAllItemsAndAnimateChanges()
            setupInboxMenu()
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
                    abVariant = getVariantTracker(abTestPlatform),
                    userRole = getRoleUser(userSession),
                    shopId = getShopIdTracker(userSession),
                    helpCounter = item.counter.toString()
                )
            }
            GOJEK_TYPE -> {
                analytics.clickOnChatDriver(
                    abVariant = getVariantTracker(abTestPlatform),
                    userRole = getRoleUser(userSession),
                    shopId = getShopIdTracker(userSession),
                    chatDriverCounter = item.counter.toString()
                )
            }
        }
        context?.let {
            val intent = RouteManager.getIntent(it, item.applink)
            inboxMenuResultLauncher.launch(intent)
        }
    }

    override fun onRefreshWidgetMeta() {
        loadWidgetMetaAndCounter()
    }

    override fun onRefreshWidgetCard(item: UniversalInboxWidgetUiModel) {
        when (item.type) {
            CHATBOT_TYPE -> {
                loadWidgetMetaAndCounter()
            }
            GOJEK_TYPE -> {
                // Do nothing for phase 1, will be using SDK in phase 2
            }
        }
    }

    private fun trackInboxPageImpression(counter: UniversalInboxAllCounterResponse) {
        val shopId = getShopIdTracker(userSession)
        val sellerChatCounter = if (shopId != VALUE_X) {
            counter.chatUnread.unreadSeller.toString()
        } else {
            VALUE_X
        }
        val helpCounter = if (adapter.isHelpWidgetAdded()) {
            counter.othersUnread.helpUnread.toString()
        } else {
            VALUE_X
        }
        analytics.viewOnInboxPage(
            abVariant = getVariantTracker(abTestPlatform),
            userRole = getRoleUser(userSession),
            shopId = shopId,
            sellerChatCounter = sellerChatCounter,
            buyerChatCounter = counter.chatUnread.unreadBuyer.toString(),
            discussionCounter = counter.othersUnread.discussionUnread.toString(),
            reviewCounter = counter.othersUnread.reviewUnread.toString(),
            notifCenterCounter = counter.notifCenterUnread.notifUnread,
            driverCounter = VALUE_X, // Temporary always X until phase 2
            helpCounter = helpCounter
        )
    }

    override fun onMenuClicked(item: UniversalInboxMenuUiModel) {
        if (item.applink.isEmpty()) return
        when (item.type) {
            MenuItemType.CHAT_BUYER -> {
                analytics.clickOnBuyerChat(
                    abVariant = getVariantTracker(abTestPlatform),
                    userRole = getRoleUser(userSession),
                    shopId = getShopIdTracker(userSession),
                    buyerChatCounter = item.counter.toString()
                )
            }
            MenuItemType.CHAT_SELLER -> {
                analytics.clickOnSellerChat(
                    abVariant = getVariantTracker(abTestPlatform),
                    userRole = getRoleUser(userSession),
                    shopId = getShopIdTracker(userSession),
                    sellerChatCounter = item.counter.toString()
                )
            }
            MenuItemType.DISCUSSION -> {
                analytics.sendNewPageInboxTalkTracking(userSession.userId, item.counter.toString())
                analytics.clickOnDiscussion(
                    abVariant = getVariantTracker(abTestPlatform),
                    userRole = getRoleUser(userSession),
                    shopId = getShopIdTracker(userSession),
                    discussionCounter = item.counter.toString()
                )
            }
            MenuItemType.REVIEW -> {
                analytics.clickOnReview(
                    abVariant = getVariantTracker(abTestPlatform),
                    userRole = getRoleUser(userSession),
                    shopId = getShopIdTracker(userSession),
                    reviewCounter = item.counter.toString()
                )
            }
        }
        context?.let {
            val intent = RouteManager.getIntent(it, item.applink)
            inboxMenuResultLauncher.launch(intent)
        }
    }

    override fun onNotificationIconClicked(counter: String) {
        analytics.clickOnNotifCenter(
            abVariant = getVariantTracker(abTestPlatform),
            userRole = getRoleUser(userSession),
            shopId = getShopIdTracker(userSession),
            notifCenterCounter = counter
        )
        context?.let {
            val intent = RouteManager.getIntent(it, ApplinkConst.NOTIFICATION)
            inboxMenuResultLauncher.launch(intent)
        }
    }

    override fun onLoadMore(page: Int, totalItemsCount: Int) {
        showLoadMoreLoading()
        viewModel.loadMoreRecommendation(page)
    }

    private fun showLoadMoreLoading() {
        adapter.addItem(adapter.getItems().size, UniversalInboxRecommendationLoaderUiModel())
        binding?.inboxRv?.post {
            adapter.notifyItemInserted(adapter.itemCount)
        }
    }

    private fun removeLoadMoreLoading() {
        if (adapter.getItems().isNotEmpty() &&
            adapter.isRecommendationLoader(adapter.getItems().lastIndex)
        ) {
            adapter.removeItemAt(adapter.getItems().lastIndex)
            binding?.inboxRv?.post {
                adapter.notifyItemRemoved(adapter.getItems().size)
            }
        }
    }

    override fun onTdnBannerResponse(categoriesList: MutableList<List<TopAdsImageViewModel>>) {
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
        adapter.getFirstTopAdsBannerPositionPair()?.let { (index, item) ->
            item.ads = categoriesList[Int.ZERO]
            binding?.inboxRv?.post {
                adapter.notifyItemChanged(index)
            }
        }
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
        // Need to use startActivityForResult to be able to use handleProductCardOptionsActivityResult
        startActivityForResult(intent, REQUEST_FROM_PDP)
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
        loadWidgetMetaAndCounter()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_FROM_PDP && data != null) {
            val wishlistStatusFromPdp = data.getBooleanExtra(
                WISHLIST_STATUS_IS_WISHLIST,
                false
            )
            val position = data.getIntExtra(PDP_EXTRA_UPDATED_POSITION, -1)
            if (position < Int.ZERO || adapter.getItems().size <= position) return
            if (adapter.getItems()[position] is RecommendationItem) {
                val recommendation = adapter.getItems()[position] as RecommendationItem
                recommendation.isWishlist = wishlistStatusFromPdp
                binding?.inboxRv?.post {
                    adapter.notifyItemChanged(position)
                }
            }
        }
        handleProductCardOptionsActivityResult(
            requestCode,
            resultCode,
            data,
            object : ProductCardOptionsWishlistCallback {
                override fun onReceiveWishlistResult(
                    productCardOptionsModel: ProductCardOptionsModel
                ) {
                    handleWishListAction(productCardOptionsModel)
                }
            }
        )
    }

    private fun handleWishListAction(
        productCardOptionsModel: ProductCardOptionsModel
    ) {
        if (productCardOptionsModel.wishlistResult.isSuccess) {
            handleWishListV2ActionSuccess(productCardOptionsModel)
        } else {
            showErrorAddRemoveWishlistV2(productCardOptionsModel.wishlistResult)
        }
    }

    private fun handleWishListV2ActionSuccess(productCardOptionsModel: ProductCardOptionsModel) {
        val isAddWishlist = productCardOptionsModel.wishlistResult.isAddWishlist
        topAdsAnalytic.eventClickRecommendationWishlist(isAddWishlist)
        val payloads = Bundle().also {
            it.putBoolean(WISHLIST_STATUS_IS_WISHLIST, isAddWishlist)
        }
        binding?.inboxRv?.post {
            adapter.notifyItemChanged(productCardOptionsModel.productPosition, payloads)
        }
        if (isAddWishlist) {
            showSuccessAddWishlistV2(wishlistResult = productCardOptionsModel.wishlistResult)
        } else {
            showSuccessRemoveWishlistV2(wishlistResult = productCardOptionsModel.wishlistResult)
        }
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

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle
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
