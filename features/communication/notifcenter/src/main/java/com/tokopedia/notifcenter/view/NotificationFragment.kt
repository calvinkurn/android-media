package com.tokopedia.notifcenter.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.collection.ArrayMap
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.atc_common.AtcFromExternalSource
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.config.GlobalConfig
import com.tokopedia.inboxcommon.InboxFragment
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.analytics.MarkAsSeenAnalytic
import com.tokopedia.notifcenter.analytics.NotificationAnalytic
import com.tokopedia.notifcenter.analytics.NotificationTopAdsAnalytic
import com.tokopedia.notifcenter.data.entity.notification.NotificationDetailResponseModel
import com.tokopedia.notifcenter.data.entity.notification.NotificationDetailResponseWrapper
import com.tokopedia.notifcenter.data.entity.notification.ProductData
import com.tokopedia.notifcenter.data.entity.orderlist.NotifOrderListResponse
import com.tokopedia.notifcenter.data.entity.orderlist.OrderWidgetUiModel
import com.tokopedia.notifcenter.data.model.RecommendationDataModel
import com.tokopedia.notifcenter.data.model.ScrollToBottomState
import com.tokopedia.notifcenter.data.state.Resource
import com.tokopedia.notifcenter.data.state.Status
import com.tokopedia.notifcenter.data.uimodel.EmptyNotificationUiModel
import com.tokopedia.notifcenter.data.uimodel.LoadMoreUiModel
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.notifcenter.domain.NotifcenterDetailUseCase
import com.tokopedia.notifcenter.service.MarkAsSeenService
import com.tokopedia.notifcenter.view.adapter.NotificationAdapter
import com.tokopedia.notifcenter.view.adapter.decoration.NotificationItemDecoration
import com.tokopedia.notifcenter.view.adapter.decoration.RecommendationItemDecoration
import com.tokopedia.notifcenter.view.adapter.listener.NotificationEndlessRecyclerViewScrollListener
import com.tokopedia.notifcenter.view.adapter.typefactory.NotificationTypeFactory
import com.tokopedia.notifcenter.view.adapter.typefactory.NotificationTypeFactoryImpl
import com.tokopedia.notifcenter.view.adapter.viewholder.ViewHolderState
import com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3.LoadMoreViewHolder
import com.tokopedia.notifcenter.view.customview.NotificationTopAdsHeadlineHelper
import com.tokopedia.notifcenter.view.customview.bottomsheet.BottomSheetFactory
import com.tokopedia.notifcenter.view.customview.bottomsheet.NotificationLongerContentBottomSheet
import com.tokopedia.notifcenter.view.customview.widget.NotificationFilterView
import com.tokopedia.notifcenter.view.listener.NotificationFragmentContainer
import com.tokopedia.notifcenter.view.listener.NotificationItemListener
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.topads.sdk.viewmodel.TopAdsHeadlineViewModel
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import com.tokopedia.wishlistcommon.util.AddRemoveWishlistV2Handler
import javax.inject.Inject

class NotificationFragment @Inject constructor(
    private val topAdsAnalytic: NotificationTopAdsAnalytic,
    private val analytic: NotificationAnalytic,
    private val markAsSeenAnalytic: MarkAsSeenAnalytic,
    private val userSession: UserSessionInterface,
    private val topAdsHeadlineViewModel: TopAdsHeadlineViewModel,
    private val viewModel: NotificationViewModel
) :
    BaseListFragment<Visitable<*>, NotificationTypeFactory>(),
    InboxFragment,
    NotificationItemListener,
    LoadMoreViewHolder.Listener,
    NotificationEndlessRecyclerViewScrollListener.Listener,
    NotificationAdapter.Listener,
    NotificationLongerContentBottomSheet.Listener {

    var remoteConfig: RemoteConfig? = null

    private var rvLm = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    private var rv: RecyclerView? = null
    private var rvAdapter: NotificationAdapter? = null
    private var rvScrollListener: NotificationEndlessRecyclerViewScrollListener? = null
    private var rvTypeFactory: NotificationTypeFactoryImpl? = null
    private var filter: NotificationFilterView? = null
    private var containerListener: NotificationFragmentContainer? = null
    private var recommendationLifeCycleAware: RecommendationLifeCycleAware? = null
    private var trackingQueue: TrackingQueue? = null
    private val viewHolderLoading = ArrayMap<Any, ViewHolderState>()
    private val scrollState = ScrollToBottomState()

    override fun hasInitialSwipeRefresh(): Boolean = true
    override fun getRecyclerViewResourceId(): Int = R.id.recycler_view
    override fun getSwipeRefreshLayoutResourceId(): Int = R.id.swipe_refresh_layout
    override fun getScreenName(): String = "/new-inbox/notif"
    override fun onItemClicked(t: Visitable<*>?) {}
    override fun isAutoLoadEnabled(): Boolean = true

    override fun onAttachActivity(context: Context?) {
        if (context is NotificationFragmentContainer) {
            containerListener = context
        }
    }

    override fun loadData(page: Int) {
        if (page == 1) {
            if (!hasFilter() && !GlobalConfig.isSellerApp()) {
                containerListener?.role?.let {
                    viewModel.loadNotifOrderList(it)
                }
            }
            containerListener?.role?.let {
                viewModel.loadFirstPageNotification(it)
            }
        }
    }

    override fun onLoadMore(page: Int, totalItemsCount: Int) {
        if (rvAdapter?.lastItemIsErrorNetwork() == true) return
        // The bottom part of non-filtered is product recommendation
        if (!viewModel.hasFilter()) {
            viewModel.loadRecommendations(page)
        } else {
            // The bottom part of filtered is still notification items
            containerListener?.role?.let {
                viewModel.loadMoreEarlier(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initRecommendationComponent()
        initRemoteConfig()
        containerListener?.role?.let {
            viewModel.loadNotificationFilter(it)
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (hidden) {
            triggerMarkAsSeenTracker(containerListener?.role)
            trackIfUserScrollToBottom()
        }
    }

    override fun onStop() {
        super.onStop()
        triggerMarkAsSeenTracker(containerListener?.role)
    }

    private fun initRecommendationComponent() {
        context?.let {
            trackingQueue = TrackingQueue(it)
            recommendationLifeCycleAware = RecommendationLifeCycleAware(
                topAdsAnalytic,
                trackingQueue,
                rvAdapter,
                viewModel,
                this,
                it
            )
        }
        rvTypeFactory?.recommendationListener = recommendationLifeCycleAware
    }

    private fun initRemoteConfig() {
        remoteConfig = FirebaseRemoteConfigImpl(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_notifcenter_notification,
            container,
            false
        )?.also {
            initView(it)
            setupObserver()
            setupRecyclerView()
            setupFilter()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLifecycleObserver()
    }

    private fun setupLifecycleObserver() {
        recommendationLifeCycleAware?.let {
            viewLifecycleOwner.lifecycle.addObserver(it)
        }
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, NotificationTypeFactory> {
        rvAdapter = NotificationAdapter(adapterTypeFactory, this)
        return rvAdapter as NotificationAdapter
    }

    override fun getAdapterTypeFactory(): NotificationTypeFactory {
        rvTypeFactory = NotificationTypeFactoryImpl(this)
        return rvTypeFactory!!
    }

    override fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        rvScrollListener = NotificationEndlessRecyclerViewScrollListener(rvLm, this)
        return rvScrollListener!!
    }

    override fun getEmptyDataViewModel(): Visitable<*> {
        if (viewModel.hasFilter() || amISeller()) {
            return EmptyNotificationUiModel(viewModel.hasFilter())
        }
        return super.getEmptyDataViewModel()
    }

    override fun isListEmpty(): Boolean {
        return super.isListEmpty() ||
            (rvAdapter?.itemCount == 1 && rvAdapter?.hasNotifOrderList() == true)
    }

    override fun showGetListError(throwable: Throwable?) {
        hideLoading()
        updateStateScrollListener()
        if (!isListEmpty) {
            onGetListErrorWithExistingData(throwable)
        } else {
            onGetListErrorWithEmptyData(throwable)
        }
    }

    private fun initView(view: View) {
        rv = view.findViewById(R.id.recycler_view)
        filter = view.findViewById<NotificationFilterView>(R.id.sv_filter)?.also {
            it.initConfig(analytic)
        }
    }

    override fun onPause() {
        super.onPause()
        trackIfUserScrollToBottom()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        trackIfUserScrollToBottom()
        Toaster.onCTAClick = View.OnClickListener { }
    }

    private fun trackIfUserScrollToBottom() {
        if (scrollState.hasScrolledDown()) {
            analytic.trackScrollToBottom(scrollState.lastSeenItem.toString())
            scrollState.updateOffset()
        }
    }

    private fun setupObserver() {
        viewModel.notificationItems.observe(viewLifecycleOwner) {
            when (it.loadType) {
                NotifcenterDetailUseCase.NotificationDetailLoadType.FIRST_PAGE -> {
                    handleFirstPageNotificationItems(it.result)
                }
                NotifcenterDetailUseCase.NotificationDetailLoadType.LOAD_MORE_NEW,
                NotifcenterDetailUseCase.NotificationDetailLoadType.LOAD_MORE_EARLIER -> {
                    handleLoadMoreNotification(it)
                }
            }
        }

        viewModel.topAdsBanner.observe(viewLifecycleOwner) {
            rvAdapter?.addTopAdsBanner(it)
        }

        viewModel.recommendations.observe(viewLifecycleOwner) {
            renderRecomList(it)
        }

        viewModel.filterList.observe(viewLifecycleOwner) {
            filter?.updateFilterState(it)
        }

        viewModel.clearNotif.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> containerListener?.clearNotificationCounter()
                else -> {
                }
            }
        }

        viewModel.bumpReminder.observe(viewLifecycleOwner) {
            updateReminderState(
                resource = it,
                isBumpReminder = true
            )
        }

        viewModel.deleteReminder.observe(viewLifecycleOwner) {
            updateReminderState(
                resource = it,
                isBumpReminder = false
            )
        }

        viewModel.orderList.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    if (rvAdapter?.hasNotifOrderList() == false) {
                        updateOrRenderOrderListState(it.data)
                    }
                }
                Status.SUCCESS -> {
                    updateOrRenderOrderListState(it.data)
                }
                else -> {
                }
            }
        }
    }

    private fun handleFirstPageNotificationItems(
        result: com.tokopedia.usecase.coroutines.Result<NotificationDetailResponseModel>
    ) {
        when (result) {
            is Success<NotificationDetailResponseModel> -> {
                renderNotifications(result.data)
                if (!viewModel.hasFilter() && isVisible) {
                    containerListener?.role?.let { role ->
                        viewModel.clearNotifCounter(role)
                    }
                }
            }
            is Fail -> showGetListError(result.throwable)
        }
    }

    private fun handleLoadMoreNotification(
        result: NotificationDetailResponseWrapper
    ) {
        when (result.result) {
            is Success -> {
                if (result.lastKnownPair != null) {
                    rvAdapter?.insertNotificationData(
                        lastKnownPosition = result.lastKnownPair.first,
                        element = result.lastKnownPair.second as LoadMoreUiModel,
                        response = result.result.data
                    )
                } else {
                    renderNotifications(result.result.data)
                }
            }
            is Fail -> {
                if (result.lastKnownPair != null) {
                    rvAdapter?.failLoadMoreNotification(
                        lastKnownPosition = result.lastKnownPair.first,
                        element = result.lastKnownPair.second as LoadMoreUiModel
                    )
                }
                showErrorMessage(result.result.throwable)
            }
        }
    }

    private fun updateOrRenderOrderListState(data: NotifOrderListResponse?) {
        rvAdapter?.updateOrRenderOrderListState(data) {
            val visibleItems = rvLm.findFirstVisibleItemPositions(null)
            if (visibleItems.isNotEmpty() && visibleItems.first() == 0) {
                moveToTop()
            }
        }
    }

    private fun updateReminderState(
        resource: Resource<Any>,
        isBumpReminder: Boolean
    ) {
        val viewHolderState: ViewHolderState? = viewHolderLoading[resource.referer]
        when (resource.status) {
            Status.LOADING -> {
                rvAdapter?.loadingStateReminder(viewHolderState)
            }
            Status.SUCCESS -> {
                if (isBumpReminder) {
                    showMessage(R.string.title_success_bump_reminder)
                } else {
                    showMessage(R.string.title_success_delete_reminder)
                }
                rvAdapter?.successUpdateReminderState(viewHolderState, isBumpReminder)
                viewHolderLoading.remove(resource.referer)
            }
            Status.ERROR -> {
                resource.throwable?.let { error ->
                    showErrorMessage(error)
                }
                rvAdapter?.successUpdateReminderState(viewHolderState, isBumpReminder)
                viewHolderLoading.remove(resource.referer)
            }
            else -> {
            }
        }
    }

    private fun renderNotifications(data: NotificationDetailResponseModel) {
        val hasNext = isInfiniteNotificationScroll(data)
        renderList(data.items, hasNext)
        if (hasNext) {
            showLoading()
        }
    }

    private fun isInfiniteNotificationScroll(data: NotificationDetailResponseModel): Boolean {
        return data.hasNext && viewModel.hasFilter()
    }

    private fun renderRecomList(recoms: RecommendationDataModel) {
        hideLoading()
        rvAdapter?.addRecomProducts(recoms.item)
        loadShopAds()
        updateScrollListenerState(recoms)
    }

    private fun loadShopAds() {
        if (rvAdapter == null || rvAdapter?.shopAdsWidgetAdded == true) return
        topAdsHeadlineViewModel.getTopAdsHeadlineData(
            NotificationTopAdsHeadlineHelper.getParams(userSession.userId),
            {
                rvAdapter?.addShopAds(it)
            },
            {
                ServerLogger.log(
                    Priority.P1,
                    NotificationFragment::class.java.simpleName,
                    mapOf(ERROR to SHOPADS_LOAD_FAIL_ERROR)
                )
            }
        )
    }

    private fun updateScrollListenerState(recoms: RecommendationDataModel) {
        updateScrollListenerState(recoms.hasNext)
        if (recoms.hasNext) {
            showLoading()
        }
    }

    private fun setupRecyclerView() {
        rv?.layoutManager = rvLm
        rv?.setHasFixedSize(true)
        rv?.addItemDecoration(NotificationItemDecoration(context))
        rv?.addItemDecoration(RecommendationItemDecoration())
        rv?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val currentLastItem = getLastVisibleItemPosition() ?: return
                    if (currentLastItem > scrollState.lastSeenItem) {
                        scrollState.lastSeenItem = currentLastItem
                    }
                }
            }
        })
    }

    private fun getLastVisibleItemPosition(): Int? {
        val layoutManager = rv?.layoutManager as? StaggeredGridLayoutManager
        return layoutManager
            ?.findLastVisibleItemPositions(null)
            ?.getOrNull(1)
    }

    private fun setupFilter() {
        filter?.setFilterListener(
            object : NotificationFilterView.FilterListener {
                override fun onFilterChanged(filterType: Long, filterName: String) {
                    viewModel.filter = filterType
                    loadInitialData()
                    analytic.trackFilterClick(
                        filterType,
                        filterName,
                        containerListener?.role
                    )
                }
            }
        )
    }

    override fun onSwipeRefresh() {
        viewModel.cancelAllUseCase()
        containerListener?.refreshNotificationCounter()
        rvAdapter?.shopAdsWidgetAdded = false
        super.onSwipeRefresh()
    }

    override fun loadMoreNew(lastKnownPosition: Int, element: LoadMoreUiModel) {
        analytic.trackLoadMoreNew()
        rvAdapter?.loadMore(lastKnownPosition, element)
        containerListener?.role?.let { role ->
            viewModel.loadMoreNew(role, lastKnownPosition, element)
        }
    }

    override fun loadMoreEarlier(
        lastKnownPosition: Int,
        element: LoadMoreUiModel
    ) {
        analytic.trackLoadMoreEarlier()
        rvAdapter?.loadMore(lastKnownPosition, element)
        containerListener?.role?.let { role ->
            viewModel.loadMoreEarlier(role, lastKnownPosition, element)
        }
    }

    private fun showErrorMessage(msg: String) {
        view?.let {
            Toaster.build(it, msg, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
        }
    }

    private fun showErrorMessage(throwable: Throwable) {
        val message = ErrorHandler.getErrorMessage(context, throwable)
        view?.let {
            Toaster.build(it, message, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR)
                .show()
        }
    }

    private fun showMessage(@StringRes stringRes: Int) {
        val msg = getString(stringRes)
        view?.let {
            Toaster.build(it, msg, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
        }
    }

    override fun onRoleChanged(@RoleType role: Int) {
        val previousRole = getOppositeRole(role)
        viewModel.cancelAllUseCase()
        viewModel.reset()
        filter?.reset()
        if (isResumed) {
            viewModel.loadNotificationFilter(role)
            triggerMarkAsSeenTracker(previousRole)
            rvAdapter?.removeAllItems()
            loadInitialData()
        }
    }

    private fun triggerMarkAsSeenTracker(@RoleType role: Int?) {
        MarkAsSeenService.startService(context, role, markAsSeenAnalytic)
    }

    private fun getOppositeRole(role: Int): Int {
        return if (role == RoleType.BUYER) {
            RoleType.SELLER
        } else {
            RoleType.BUYER
        }
    }

    override fun onPageClickedAgain() {
        moveToTop()
    }

    private fun moveToTop() {
        rv?.scrollToPosition(0)
    }

    override fun initInjector() {}

    override fun showLongerContent(element: NotificationUiModel) {
        BottomSheetFactory.showLongerContent(childFragmentManager, element)
    }

    override fun buyProduct(notification: NotificationUiModel, product: ProductData) {
        if (product.isVariant) {
            showAtcVariantHelper(
                product.productId,
                product.shop.id,
                product.shop.isTokonow
            )
        } else {
            doBuyAndAtc(product) {
                analytic.trackSuccessDoBuyAndAtc(
                    notification,
                    product,
                    it,
                    NotificationAnalytic.EventAction.CLICK_PRODUCT_BUY
                )
                RouteManager.route(context, ApplinkConst.CART)
            }
        }
    }

    override fun addProductToCart(notification: NotificationUiModel, product: ProductData) {
        if (product.isVariant) {
            showAtcVariantHelper(
                product.productId,
                product.shop.id,
                product.shop.isTokonow
            )
        } else {
            doBuyAndAtc(product) {
                analytic.trackSuccessDoBuyAndAtc(
                    notification,
                    product,
                    it,
                    NotificationAnalytic.EventAction.CLICK_PRODUCT_ATC
                )
                val msg = it.message.getOrNull(0) ?: ""
                view?.let { view ->
                    Toaster.build(
                        view,
                        msg,
                        Toaster.LENGTH_LONG,
                        Toaster.TYPE_NORMAL,
                        view.context.getString(R.string.title_notifcenter_see_cart),
                        View.OnClickListener {
                            RouteManager.route(context, ApplinkConst.CART)
                        }
                    ).show()
                }
            }
        }
    }

    private fun doBuyAndAtc(
        product: ProductData,
        onSuccess: (response: DataModel) -> Unit = {}
    ) {
        val buyParam = getAtcBuyParam(product)
        viewModel.addProductToCart(buyParam, {
            onSuccess(it)
        }, { msg ->
            msg?.let {
                showErrorMessage(it)
            }
        })
    }

    private fun getAtcBuyParam(product: ProductData): AddToCartRequestParams {
        return AddToCartRequestParams(
            productId = product.productId,
            shopId = product.shop.id,
            quantity = product.minOrder,
            atcFromExternalSource = AtcFromExternalSource.ATC_FROM_NOTIFCENTER,
            warehouseId = product.warehouseId
        )
    }

    private fun showAtcVariantHelper(
        productId: String,
        shopId: String,
        isTokonow: Boolean
    ) {
        context?.let { ctx ->
            AtcVariantHelper.goToAtcVariant(
                context = ctx,
                productId = productId,
                pageSource = VariantPageSource.NOTIFCENTER_PAGESOURCE,
                isTokoNow = isTokonow,
                shopId = shopId,
                startActivitResult = { intent, requestCode ->
                    startActivityForResult(intent, requestCode)
                }
            )
        }
    }

    override fun markNotificationAsRead(element: NotificationUiModel) {
        containerListener?.role?.let {
            viewModel.markNotificationAsRead(it, element)
        }
    }

    override fun bumpReminder(
        product: ProductData,
        notification: NotificationUiModel,
        adapterPosition: Int
    ) {
        createViewHolderState(notification, adapterPosition, product)
        viewModel.bumpReminder(product, notification)
    }

    override fun deleteReminder(
        product: ProductData,
        notification: NotificationUiModel,
        adapterPosition: Int
    ) {
        createViewHolderState(notification, adapterPosition, product)
        viewModel.deleteReminder(product, notification)
    }

    override fun addToWishlist(
        notification: NotificationUiModel,
        product: ProductData,
        position: Int
    ) {
        context?.let { context ->
            viewModel.doAddToWishlistV2(
                product.productId,
                object : WishlistV2ActionListener {
                    override fun onErrorAddWishList(throwable: Throwable, productId: String) {
                        val errorMsg = ErrorHandler.getErrorMessage(context, throwable)
                        view?.let { v ->
                            AddRemoveWishlistV2Handler.showWishlistV2ErrorToaster(errorMsg, v)
                        }
                        rvAdapter?.updateFailedAddToWishlist(notification, product, position)
                    }

                    override fun onSuccessAddWishlist(
                        result: AddToWishlistV2Response.Data.WishlistAddV2,
                        productId: String
                    ) {
                        view?.let { v ->
                            AddRemoveWishlistV2Handler.showAddToWishlistV2SuccessToaster(result, context, v)
                        }
                    }

                    override fun onErrorRemoveWishlist(throwable: Throwable, productId: String) {}
                    override fun onSuccessRemoveWishlist(result: DeleteWishlistV2Response.Data.WishlistRemoveV2, productId: String) {}
                }
            )
        }
    }

    override fun goToWishlist() {
        val intent = RouteManager.getIntent(context, ApplinkConst.NEW_WISHLIST)
        startActivity(intent)
    }

    override fun trackProductImpression(
        notification: NotificationUiModel,
        product: ProductData,
        position: Int
    ) {
        analytic.trackProductImpression(notification, product, position)
    }

    override fun onProductClicked(
        notification: NotificationUiModel,
        product: ProductData,
        position: Int
    ) {
        analytic.trackProductClick(notification, product, position)
        val intent = RouteManager.getIntent(context, product.androidUrl)
        startActivity(intent)
    }

    override fun trackBumpReminder() {
        analytic.trackBumpReminder()
    }

    override fun trackDeleteReminder() {
        analytic.trackDeleteReminder()
    }

    override fun markAsSeen(notifId: String) {
        markAsSeenAnalytic.markAsSeen(notifId)
    }

    override fun refreshPage() {
        onRetryClicked()
    }

    override fun trackClickCtaWidget(element: NotificationUiModel) {
        analytic.trackClickCtaWidget(element, containerListener?.role)
    }

    override fun trackExpandTimelineHistory(element: NotificationUiModel) {
        analytic.trackExpandTimelineHistory(element, containerListener?.role)
    }

    override fun amISeller(): Boolean {
        return containerListener?.role == RoleType.SELLER
    }

    override fun trackClickOrderListItem(order: OrderWidgetUiModel) {
        analytic.trackClickOrderListItem(containerListener?.role, order)
    }

    override fun getNotifAnalytic(): NotificationAnalytic {
        return analytic
    }

    override fun getRole(): Int {
        return containerListener?.role ?: -1
    }

    override fun hasFilter(): Boolean {
        return viewModel.hasFilter()
    }

    private fun createViewHolderState(
        notification: NotificationUiModel,
        adapterPosition: Int,
        product: ProductData
    ) {
        val loadingState = ViewHolderState(notification, adapterPosition, product)
        viewHolderLoading[product.productId] = loadingState
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        recommendationLifeCycleAware?.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CHECKOUT -> onReturnFromCheckout(resultCode, data)
        }
    }

    private fun onReturnFromCheckout(resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK || data == null) return
        val message = data.getStringExtra(ApplinkConst.Transaction.RESULT_ATC_SUCCESS_MESSAGE)
            ?: return
        view?.let {
            Toaster.build(
                it,
                message,
                Snackbar.LENGTH_LONG,
                Toaster.TYPE_NORMAL,
                getString(R.string.notifcenter_title_view),
                onClickSeeButtonOnAtcSuccessToaster()
            ).show()
        }
    }

    private fun onClickSeeButtonOnAtcSuccessToaster(): View.OnClickListener {
        return View.OnClickListener {
            RouteManager.route(context, ApplinkConstInternalMarketplace.CART)
        }
    }

    companion object {
        private const val TAG = "NotificationFragment"
        private const val REQUEST_CHECKOUT = 0
        private const val SHOPADS_LOAD_FAIL_ERROR = "Failed to load Shopads in NotifCenter"
        private const val ERROR = "error"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle = Bundle()
        ): NotificationFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? NotificationFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                NotificationFragment::class.java.name
            ).apply {
                arguments = bundle
            } as NotificationFragment
        }
    }
}
