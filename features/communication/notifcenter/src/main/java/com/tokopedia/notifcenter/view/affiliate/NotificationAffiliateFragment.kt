package com.tokopedia.notifcenter.view.affiliate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.analytics.MarkAsSeenAnalytic
import com.tokopedia.notifcenter.analytics.NotificationAffiliateAnalytics
import com.tokopedia.notifcenter.analytics.NotificationAnalytic
import com.tokopedia.notifcenter.data.entity.affiliate.AffiliateEducationArticleResponse
import com.tokopedia.notifcenter.data.entity.notification.NotificationDetailResponseModel
import com.tokopedia.notifcenter.data.entity.notification.NotificationDetailResponseWrapper
import com.tokopedia.notifcenter.data.entity.notification.ProductData
import com.tokopedia.notifcenter.data.entity.orderlist.OrderWidgetUiModel
import com.tokopedia.notifcenter.data.model.ScrollToBottomState
import com.tokopedia.notifcenter.data.uimodel.EmptyNotificationUiModel
import com.tokopedia.notifcenter.data.uimodel.LoadMoreUiModel
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.notifcenter.data.uimodel.affiliate.NotificationAffiliateEducationUiModel
import com.tokopedia.notifcenter.domain.NotifcenterDetailUseCase
import com.tokopedia.notifcenter.service.MarkAsSeenService
import com.tokopedia.notifcenter.view.NotificationViewModel
import com.tokopedia.notifcenter.view.adapter.NotificationAdapter
import com.tokopedia.notifcenter.view.adapter.decoration.NotificationItemDecoration
import com.tokopedia.notifcenter.view.adapter.listener.NotificationEndlessRecyclerViewScrollListener
import com.tokopedia.notifcenter.view.adapter.typefactory.NotificationTypeFactory
import com.tokopedia.notifcenter.view.adapter.typefactory.NotificationTypeFactoryImpl
import com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3.LoadMoreViewHolder
import com.tokopedia.notifcenter.view.customview.bottomsheet.NotificationLongerContentBottomSheet
import com.tokopedia.notifcenter.view.customview.widget.NotificationFilterView
import com.tokopedia.notifcenter.view.listener.NotificationAffiliateEduEventListener
import com.tokopedia.notifcenter.view.listener.NotificationItemListener
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject

class NotificationAffiliateFragment @Inject constructor(
    private val viewModel: NotificationViewModel,
    private val analytic: NotificationAnalytic,
    private val markAsSeenAnalytic: MarkAsSeenAnalytic,
    private val userSession: UserSessionInterface
) :
    BaseListFragment<Visitable<*>, NotificationTypeFactory>(),
    NotificationItemListener,
    LoadMoreViewHolder.Listener,
    NotificationEndlessRecyclerViewScrollListener.Listener,
    NotificationAdapter.Listener,
    NotificationLongerContentBottomSheet.Listener,
    NotificationAffiliateEduEventListener {

    private var remoteConfig: RemoteConfig? = null

    private var rvLm = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    private var rv: RecyclerView? = null
    private var rvAdapter: NotificationAdapter? = null
    private var rvScrollListener: NotificationEndlessRecyclerViewScrollListener? = null
    private var rvTypeFactory: NotificationTypeFactoryImpl? = null
    private var filter: NotificationFilterView? = null
    private val scrollState = ScrollToBottomState()

    private var isEmptyState = false

    override fun hasInitialSwipeRefresh(): Boolean = true
    override fun getRecyclerViewResourceId(): Int = R.id.recycler_view
    override fun getSwipeRefreshLayoutResourceId(): Int = R.id.swipe_refresh_layout
    override fun getScreenName(): String = "/notif-center/affiliate"
    override fun onItemClicked(t: Visitable<*>?) = Unit
    override fun isAutoLoadEnabled(): Boolean = true

    override fun loadData(page: Int) {
        if (page == Int.ONE) {
            viewModel.loadFirstPageNotification(
                RoleType.AFFILIATE
            )
        }
    }

    override fun onLoadMore(page: Int, totalItemsCount: Int) {
        if (rvAdapter?.lastItemIsErrorNetwork() == true) return
        if (viewModel.hasFilter()) {
            viewModel.loadMoreEarlier(RoleType.AFFILIATE)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initRemoteConfig()
        rvTypeFactory?.affiliateEducationListener = this
        viewModel.loadNotificationFilter(RoleType.AFFILIATE)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (hidden) {
            triggerMarkAsSeenTracker()
            if (scrollState.hasScrolledDown()) {
                scrollState.updateOffset()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        triggerMarkAsSeenTracker()
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
        val title: String
        val msg: String
        if (viewModel.hasFilter()) {
            title = getString(R.string.affiliate_notification_empty_filter_title)
            msg = getString(R.string.affiliate_notification_empty_filter_msg)
        } else {
            title = getString(R.string.affiliate_notification_empty_title)
            msg = getString(R.string.affiliate_notification_empty_msg)
        }
        isEmptyState = true
        return EmptyNotificationUiModel(viewModel.hasFilter(), title, msg)
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
            it.findViewById<IconUnify>(R.id.iv_setting_notif).gone()
        }
    }

    override fun onPause() {
        super.onPause()
        if (scrollState.hasScrolledDown()) {
            scrollState.updateOffset()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (scrollState.hasScrolledDown()) {
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

        viewModel.filterList.observe(
            viewLifecycleOwner
        ) {
            filter?.updateFilterState(it)
        }

        viewModel.affiliateEducationArticle.observe(viewLifecycleOwner) {
            if (!isListEmpty && !isEmptyState) {
                rvAdapter?.addAffiliateEducationArticles(it)
            }
        }
    }

    private fun handleFirstPageNotificationItems(
        result: com.tokopedia.usecase.coroutines.Result<NotificationDetailResponseModel>
    ) {
        when (result) {
            is Success<NotificationDetailResponseModel> -> {
                if (viewModel.hasFilter()) {
                    rvAdapter?.removeLoadingComponents()
                }
                renderNotifications(result.data)
                if (!viewModel.hasFilter() && isVisible) {
                    viewModel.clearNotifCounter(RoleType.AFFILIATE)
                }
                if (viewModel.hasFilter() && !isListEmpty && !isEmptyState) {
                    rvAdapter?.removeAffiliateBanner()
                    rvAdapter?.reAddAffiliateBanner()
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
                rvAdapter?.insertNotificationData(
                    lastKnownPosition = result.lastKnownPair?.first ?: RecyclerView.NO_POSITION,
                    element = result.lastKnownPair?.second as LoadMoreUiModel,
                    response = result.result.data
                )
            }
            is Fail -> {
                rvAdapter?.failLoadMoreNotification(
                    lastKnownPosition = result.lastKnownPair?.first ?: RecyclerView.NO_POSITION,
                    element = result.lastKnownPair?.second as LoadMoreUiModel
                )
                showErrorMessage(result.result.throwable)
            }
        }
    }

    private fun renderNotifications(data: NotificationDetailResponseModel) {
        val hasNext = isInfiniteNotificationScroll(data)
        isEmptyState = isLoadingInitialData && data.items.isEmpty()
        renderList(data.items, hasNext)
        if (hasNext) {
            showLoading()
        }
    }

    private fun isInfiniteNotificationScroll(data: NotificationDetailResponseModel): Boolean {
        return data.hasNext && viewModel.hasFilter()
    }

    private fun setupRecyclerView() {
        rv?.layoutManager = rvLm
        rv?.setHasFixedSize(true)
        rv?.addItemDecoration(NotificationItemDecoration(context))
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
                    rvAdapter?.affiliateBannerPair =
                        null // reset education position on filter change
                    analytic.trackFilterClick(
                        filterType,
                        filterName,
                        RoleType.AFFILIATE,
                        eventCategory = NotificationAffiliateAnalytics.EventCategory.AFFILIATE_NOTIFICATION_CENTER
                    )
                }
            }
        )
    }

    override fun onSwipeRefresh() {
        rvAdapter?.affiliateBannerPair = null // reset education position on refresh
        viewModel.cancelAllUseCase()
        super.onSwipeRefresh()
    }

    override fun loadMoreNew(lastKnownPosition: Int, element: LoadMoreUiModel) {
        rvAdapter?.loadMore(lastKnownPosition, element)
        viewModel.loadMoreNew(
            RoleType.AFFILIATE,
            lastKnownPosition,
            element
        )
    }

    override fun loadMoreEarlier(
        lastKnownPosition: Int,
        element: LoadMoreUiModel
    ) {
        rvAdapter?.loadMore(lastKnownPosition, element)
        viewModel.loadMoreEarlier(
            RoleType.AFFILIATE,
            lastKnownPosition,
            element
        )
    }

    private fun showErrorMessage(throwable: Throwable) {
        val message = ErrorHandler.getErrorMessage(context, throwable)
        view?.let {
            Toaster.build(it, message, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR)
                .show()
        }
    }

    private fun triggerMarkAsSeenTracker() {
        MarkAsSeenService.startService(context, RoleType.AFFILIATE, markAsSeenAnalytic)
    }

    override fun initInjector() {}

    override fun showLongerContent(element: NotificationUiModel) = Unit

    override fun buyProduct(notification: NotificationUiModel, product: ProductData) = Unit

    override fun addProductToCart(notification: NotificationUiModel, product: ProductData) = Unit

    override fun markNotificationAsRead(element: NotificationUiModel) =
        viewModel.markNotificationAsRead(RoleType.AFFILIATE, element)

    override fun bumpReminder(
        product: ProductData,
        notification: NotificationUiModel,
        adapterPosition: Int
    ) = Unit

    override fun deleteReminder(
        product: ProductData,
        notification: NotificationUiModel,
        adapterPosition: Int
    ) = Unit

    override fun addToWishlist(
        notification: NotificationUiModel,
        product: ProductData,
        position: Int
    ) = Unit

    override fun goToWishlist() = Unit

    override fun trackProductImpression(
        notification: NotificationUiModel,
        product: ProductData,
        position: Int
    ) = Unit

    override fun onProductClicked(
        notification: NotificationUiModel,
        product: ProductData,
        position: Int
    ) = Unit

    override fun trackBumpReminder() = Unit

    override fun trackDeleteReminder() = Unit

    override fun markAsSeen(notifId: String) = markAsSeenAnalytic.markAsSeen(notifId)

    override fun refreshPage() = onRetryClicked()

    override fun trackClickCtaWidget(element: NotificationUiModel) {
        analytic.trackClickCtaWidget(
            element,
            RoleType.AFFILIATE,
            eventCategory = NotificationAffiliateAnalytics.EventCategory.AFFILIATE_NOTIFICATION_CENTER
        )
    }

    override fun trackExpandTimelineHistory(element: NotificationUiModel) {
        analytic.trackExpandTimelineHistory(
            element,
            RoleType.AFFILIATE,
            eventCategory = NotificationAffiliateAnalytics.EventCategory.AFFILIATE_NOTIFICATION_CENTER
        )
    }

    override fun amISeller(): Boolean = false

    override fun trackClickOrderListItem(order: OrderWidgetUiModel) = Unit

    override fun getNotifAnalytic(): NotificationAnalytic {
        return analytic
    }

    override fun getRole(): Int {
        return RoleType.AFFILIATE
    }

    override fun hasFilter(): Boolean {
        return viewModel.hasFilter()
    }

    override fun onEducationActiveIndexChanged(
        currentIndex: Int,
        notificationAffiliateEducationUiModel: NotificationAffiliateEducationUiModel
    ) {
        NotificationAffiliateAnalytics.trackAffiliateEducationImpression(
            notificationAffiliateEducationUiModel,
            currentIndex,
            userSession.userId
        )
    }

    override fun onEducationItemClick(
        data: AffiliateEducationArticleResponse.CardsArticle.Data.CardsItem.Article
    ) {
        context?.let { ctx ->
            NotificationAffiliateAnalytics.trackAffiliateEducationClick(userSession.userId)
            RouteManager.route(ctx, getArticleEventUrl(data.slug.toString()))
        }
    }

    override fun onEducationLihatSemuaClick() {
        context?.let { ctx ->
            NotificationAffiliateAnalytics.trackAffiliateEducationSeeMoreClick(userSession.userId)
            RouteManager.route(ctx, ApplinkConst.AFFILIATE_TOKO_EDU_PAGE)
        }
    }

    private fun getArticleEventUrl(slug: String): String {
        return String.format(
            Locale.getDefault(),
            "%s?url=%s%s?navigation=hide",
            ApplinkConst.WEBVIEW,
            TokopediaUrl.getInstance().AFFILIATE + "/edu/",
            slug
        )
    }

    companion object {
        private const val TAG = "NotificationAffiliateFragment"
        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle = Bundle()
        ): NotificationAffiliateFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? NotificationAffiliateFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                NotificationAffiliateFragment::class.java.name
            ).apply {
                arguments = bundle
            } as NotificationAffiliateFragment
        }
    }
}
