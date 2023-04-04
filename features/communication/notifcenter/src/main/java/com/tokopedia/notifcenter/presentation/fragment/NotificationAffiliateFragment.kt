package com.tokopedia.notifcenter.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.config.GlobalConfig
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.analytics.MarkAsSeenAnalytic
import com.tokopedia.notifcenter.analytics.NotificationAffiliateAnalytics
import com.tokopedia.notifcenter.analytics.NotificationAnalytic
import com.tokopedia.notifcenter.data.entity.notification.NotificationDetailResponseModel
import com.tokopedia.notifcenter.data.entity.notification.ProductData
import com.tokopedia.notifcenter.data.entity.orderlist.OrderWidgetUiModel
import com.tokopedia.notifcenter.data.model.ScrollToBottomState
import com.tokopedia.notifcenter.data.uimodel.EmptyNotificationUiModel
import com.tokopedia.notifcenter.data.uimodel.LoadMoreUiModel
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.notifcenter.di.DaggerNotificationComponent
import com.tokopedia.notifcenter.di.NotificationComponent
import com.tokopedia.notifcenter.di.module.CommonModule
import com.tokopedia.notifcenter.listener.v3.NotificationItemListener
import com.tokopedia.notifcenter.presentation.adapter.NotificationAdapter
import com.tokopedia.notifcenter.presentation.adapter.decoration.NotificationItemDecoration
import com.tokopedia.notifcenter.presentation.adapter.listener.NotificationEndlessRecyclerViewScrollListener
import com.tokopedia.notifcenter.presentation.adapter.typefactory.notification.NotificationTypeFactory
import com.tokopedia.notifcenter.presentation.adapter.typefactory.notification.NotificationTypeFactoryImpl
import com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3.LoadMoreViewHolder
import com.tokopedia.notifcenter.presentation.fragment.bottomsheet.NotificationLongerContentBottomSheet
import com.tokopedia.notifcenter.presentation.viewmodel.NotificationViewModel
import com.tokopedia.notifcenter.service.MarkAsSeenService
import com.tokopedia.notifcenter.widget.NotificationFilterView
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

open class NotificationAffiliateFragment :
    BaseListFragment<Visitable<*>, NotificationTypeFactory>(),
    NotificationItemListener,
    LoadMoreViewHolder.Listener,
    NotificationEndlessRecyclerViewScrollListener.Listener,
    NotificationAdapter.Listener,
    NotificationLongerContentBottomSheet.Listener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var analytic: NotificationAnalytic

    @Inject
    lateinit var markAsSeenAnalytic: MarkAsSeenAnalytic

    @Inject
    lateinit var userSession: UserSessionInterface

    private var remoteConfig: RemoteConfig? = null

    private var rvLm = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    private var rv: RecyclerView? = null
    private var rvAdapter: NotificationAdapter? = null
    private var rvScrollListener: NotificationEndlessRecyclerViewScrollListener? = null
    private var rvTypeFactory: NotificationTypeFactoryImpl? = null
    private var filter: NotificationFilterView? = null
    private val scrollState = ScrollToBottomState()

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[NotificationViewModel::class.java]
    }

    override fun hasInitialSwipeRefresh(): Boolean = true
    override fun getRecyclerViewResourceId(): Int = R.id.recycler_view
    override fun getSwipeRefreshLayoutResourceId(): Int = R.id.swipe_refresh_layout
    override fun getScreenName(): String = "/notif-center/affiliate"
    override fun onItemClicked(t: Visitable<*>?) = Unit
    override fun isAutoLoadEnabled(): Boolean = true

    override fun loadData(page: Int) {
        if (page == Int.ONE) {
            if (!hasFilter() && !GlobalConfig.isSellerApp()) {
                viewModel.loadNotifOrderList(RoleType.AFFILIATE)
            }
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
        return EmptyNotificationUiModel(viewModel.hasFilter(), title, msg)
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
        viewModel.notificationItems.observe(
            viewLifecycleOwner
        ) {
            when (it) {
                is Success -> {
                    if (viewModel.hasFilter()) {
                        rvAdapter?.removeLoadingComponents()
                    }
                    renderNotifications(it.data)
                    if (!viewModel.hasFilter() && isVisible) {
                        viewModel.clearNotifCounter(RoleType.AFFILIATE)
                    }
                    if (viewModel.hasFilter()) {
                        rvAdapter?.removeAffiliateBanner()
                        rvAdapter?.reAddAffiliateBanner()
                    }
                }
                is Fail -> showGetListError(it.throwable)
            }
        }

        viewModel.filterList.observe(
            viewLifecycleOwner
        ) {
            filter?.updateFilterState(it)
        }

        viewModel.affiliateEducationArticle.observe(viewLifecycleOwner) {
            rvAdapter?.addAffiliateEducationArticles(it)
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
                    rvAdapter?.affiliateBannerPair = null
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
        viewModel.cancelAllUseCase()
        super.onSwipeRefresh()
    }

    override fun loadMoreNew(lastKnownPosition: Int, element: LoadMoreUiModel) {
        rvAdapter?.loadMore(lastKnownPosition, element)
        viewModel.loadMoreNew(
            RoleType.AFFILIATE,
            {
                rvAdapter?.insertNotificationData(lastKnownPosition, element, it)
            },
            {
                rvAdapter?.failLoadMoreNotification(lastKnownPosition, element)
                showErrorMessage(it)
            }
        )
    }

    override fun loadMoreEarlier(
        lastKnownPosition: Int,
        element: LoadMoreUiModel
    ) {
        rvAdapter?.loadMore(lastKnownPosition, element)
        viewModel.loadMoreEarlier(
            RoleType.AFFILIATE,
            {
                rvAdapter?.insertNotificationData(lastKnownPosition, element, it)
            },
            {
                rvAdapter?.failLoadMoreNotification(lastKnownPosition, element)
                showErrorMessage(it)
            }
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

    override fun initInjector() {
        generateDaggerComponent().inject(this)
    }

    protected open fun generateDaggerComponent(): NotificationComponent =
        DaggerNotificationComponent.builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .commonModule(context?.let { CommonModule(it) })
            .build()

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
}
