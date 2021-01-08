package com.tokopedia.notifcenter.presentation.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.collection.ArrayMap
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.inboxcommon.InboxFragment
import com.tokopedia.inboxcommon.InboxFragmentContainer
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.analytics.NotificationAnalytic
import com.tokopedia.notifcenter.data.entity.notification.NotificationDetailResponseModel
import com.tokopedia.notifcenter.data.entity.notification.ProductData
import com.tokopedia.notifcenter.data.model.RecommendationDataModel
import com.tokopedia.notifcenter.data.state.Resource
import com.tokopedia.notifcenter.data.state.Status
import com.tokopedia.notifcenter.data.uimodel.EmptyNotificationUiModel
import com.tokopedia.notifcenter.data.uimodel.LoadMoreUiModel
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.notifcenter.di.DaggerNotificationComponent
import com.tokopedia.notifcenter.di.module.CommonModule
import com.tokopedia.notifcenter.listener.v3.NotificationItemListener
import com.tokopedia.notifcenter.presentation.adapter.NotificationAdapter
import com.tokopedia.notifcenter.presentation.adapter.decoration.NotificationItemDecoration
import com.tokopedia.notifcenter.presentation.adapter.decoration.RecommendationItemDecoration
import com.tokopedia.notifcenter.presentation.adapter.listener.NotificationEndlessRecyclerViewScrollListener
import com.tokopedia.notifcenter.presentation.adapter.typefactory.notification.NotificationTypeFactory
import com.tokopedia.notifcenter.presentation.adapter.typefactory.notification.NotificationTypeFactoryImpl
import com.tokopedia.notifcenter.presentation.adapter.viewholder.ViewHolderState
import com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3.LoadMoreViewHolder
import com.tokopedia.notifcenter.presentation.fragment.bottomsheet.BottomSheetFactory
import com.tokopedia.notifcenter.presentation.fragment.bottomsheet.NotificationProductLongerContentBottomSheet
import com.tokopedia.notifcenter.presentation.lifecycleaware.RecommendationLifeCycleAware
import com.tokopedia.notifcenter.presentation.viewmodel.NotificationViewModel
import com.tokopedia.notifcenter.widget.NotificationFilterView
import com.tokopedia.purchase_platform.common.constant.ATC_AND_BUY
import com.tokopedia.purchase_platform.common.constant.ATC_ONLY
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class NotificationFragment : BaseListFragment<Visitable<*>, NotificationTypeFactory>(),
        InboxFragment, NotificationItemListener, LoadMoreViewHolder.Listener,
        NotificationEndlessRecyclerViewScrollListener.Listener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var analytic: NotificationAnalytic

    private var rvLm = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    private var rv: RecyclerView? = null
    private var rvAdapter: NotificationAdapter? = null
    private var rvScrollListener: NotificationEndlessRecyclerViewScrollListener? = null
    private var rvTypeFactory: NotificationTypeFactoryImpl? = null
    private var filter: NotificationFilterView? = null
    private var containerListener: InboxFragmentContainer? = null
    private var recommendationLifeCycleAware: RecommendationLifeCycleAware? = null
    private var trackingQueue: TrackingQueue? = null
    private val viewHolderLoading = ArrayMap<Any, ViewHolderState>()

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(NotificationViewModel::class.java)
    }

    override fun hasInitialSwipeRefresh(): Boolean = true
    override fun getRecyclerViewResourceId(): Int = R.id.recycler_view
    override fun getSwipeRefreshLayoutResourceId(): Int = R.id.swipe_refresh_layout
    override fun getScreenName(): String = "/new-inbox/notif"
    override fun onItemClicked(t: Visitable<*>?) {}
    override fun isAutoLoadEnabled(): Boolean = true

    override fun onAttachActivity(context: Context?) {
        if (context is InboxFragmentContainer) {
            containerListener = context
        }
    }

    override fun loadData(page: Int) {
        if (page == 1) {
            viewModel.loadFirstPageNotification(
                    containerListener?.role
            )
        }
    }

    override fun onLoadMore(page: Int, totalItemsCount: Int) {
        if (!viewModel.hasFilter()) {
            viewModel.loadRecommendations(page)
        } else {
            viewModel.loadMoreEarlier(containerListener?.role)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initRecommendationComponent()
        viewModel.loadNotificationFilter(containerListener?.role)
    }

    private fun initRecommendationComponent() {
        context?.let {
            trackingQueue = TrackingQueue(it)
            recommendationLifeCycleAware = RecommendationLifeCycleAware(
                    analytic, trackingQueue, rvAdapter, viewModel, this, it
            )
        }
        rvTypeFactory?.recommendationListener = recommendationLifeCycleAware
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
                R.layout.fragment_notifcenter_notification, container, false
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
        rvAdapter = NotificationAdapter(adapterTypeFactory)
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
        if (viewModel.hasFilter() || containerListener?.role == RoleType.SELLER) {
            return EmptyNotificationUiModel(viewModel.hasFilter())
        }
        return super.getEmptyDataViewModel()
    }

    private fun initView(view: View) {
        rv = view.findViewById(R.id.recycler_view)
        filter = view.findViewById(R.id.sv_filter)
    }

    private fun setupObserver() {
        viewModel.notificationItems.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> renderNotifications(it.data)
                is Fail -> showGetListError(it.throwable)
                else -> {
                }
            }
        })

        viewModel.topAdsBanner.observe(viewLifecycleOwner, Observer {
            rvAdapter?.addTopAdsBanner(it)
        })

        viewModel.recommendations.observe(viewLifecycleOwner, Observer {
            renderRecomList(it)
        })

        viewModel.filterList.observe(viewLifecycleOwner, Observer {
            filter?.updateFilterState(it)
        })

        viewModel.clearNotif.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> containerListener?.clearNotificationCounter()
                else -> {
                }
            }
        })

        viewModel.bumpReminder.observe(viewLifecycleOwner, Observer {
            updateReminderState(
                    resource = it,
                    isBumpReminder = true
            )
        })

        viewModel.deleteReminder.observe(viewLifecycleOwner, Observer {
            updateReminderState(
                    resource = it,
                    isBumpReminder = false
            )
        })
    }

    private fun updateReminderState(
            resource: Resource<Any>,
            isBumpReminder: Boolean
    ) {
        val viewHolderState: ViewHolderState? = viewHolderLoading[resource.referer]
        val bottomSheet = getProductBottomSheet()
        val isFromBottomSheet = bottomSheet != null
        when (resource.status) {
            Status.LOADING -> {
                rvAdapter?.loadingStateReminder(viewHolderState)
            }
            Status.SUCCESS -> {
                if (!isFromBottomSheet) {
                    if (isBumpReminder) {
                        showMessage(R.string.title_success_bump_reminder)
                    } else {
                        showMessage(R.string.title_success_delete_reminder)
                    }
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
        if (isFromBottomSheet) {
            bottomSheet?.handleEventReminderState(resource, viewHolderState, isBumpReminder)
        }
    }

    private fun getProductBottomSheet(): NotificationProductLongerContentBottomSheet? {
        return childFragmentManager
                .findFragmentByTag(
                        NotificationProductLongerContentBottomSheet::class.java.simpleName
                ) as? NotificationProductLongerContentBottomSheet
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
        updateScrollListenerState(recoms)
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
    }

    private fun setupFilter() {
        filter?.setFilterListener(
                object : NotificationFilterView.FilterListener {
                    override fun onFilterChanged(filterType: Int) {
                        viewModel.filter = filterType
                        loadInitialData()
                    }
                }
        )
    }

    override fun onSwipeRefresh() {
        viewModel.cancelAllUseCase()
        containerListener?.refreshNotificationCounter()
        super.onSwipeRefresh()
    }

    override fun loadMoreNew(lastKnownPosition: Int, element: LoadMoreUiModel) {
        rvAdapter?.loadMore(lastKnownPosition, element)
        viewModel.loadMoreNew(containerListener?.role,
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
        viewModel.loadMoreEarlier(containerListener?.role,
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

    private fun showMessage(@StringRes stringRes: Int) {
        val msg = getString(stringRes)
        view?.let {
            Toaster.build(it, msg, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
        }
    }

    override fun onRoleChanged(role: Int) {
        viewModel.cancelAllUseCase()
        viewModel.reset()
        filter?.reset()
        viewModel.loadNotificationFilter(role)
        loadInitialData()
    }

    override fun onPageClickedAgain() {
        rv?.scrollToPosition(0)
    }

    override fun initInjector() {
        DaggerNotificationComponent.builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .commonModule(context?.let { CommonModule(it) })
                .build()
                .inject(this)
    }

    override fun showLongerContent(element: NotificationUiModel) {
        BottomSheetFactory.showLongerContent(childFragmentManager, element)
    }

    override fun showProductBottomSheet(element: NotificationUiModel) {
        BottomSheetFactory.showProductBottomSheet(childFragmentManager, element)
    }

    override fun buyProduct(product: ProductData) {
        val atcPageIntent = getBuyPageIntent(product)
        startActivity(atcPageIntent)
    }

    override fun addProductToCart(product: ProductData) {
        val atcPageIntent = getAtcPageIntent(product)
        startActivityForResult(atcPageIntent, REQUEST_CHECKOUT)
    }

    override fun markNotificationAsRead(element: NotificationUiModel) {
        viewModel.markNotificationAsRead(containerListener?.role, element)
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

    private fun getBuyPageIntent(product: ProductData): Intent {
        val atcAndBuyAction = ATC_AND_BUY
        val needRefresh = true
        return RouteManager.getIntent(
                context, ApplinkConstInternalMarketplace.NORMAL_CHECKOUT
        ).apply {
            putExtra(ApplinkConst.Transaction.EXTRA_SHOP_ID, product.shop.id.toString())
            putExtra(ApplinkConst.Transaction.EXTRA_PRODUCT_ID, product.productId.toString())
            putExtra(ApplinkConst.Transaction.EXTRA_QUANTITY, product.minOrder)
            putExtra(
                    ApplinkConst.Transaction.EXTRA_SELECTED_VARIANT_ID,
                    product.productId.toString()
            )
            putExtra(ApplinkConst.Transaction.EXTRA_ACTION, atcAndBuyAction)
            putExtra(ApplinkConst.Transaction.EXTRA_SHOP_NAME, product.shop.name)
            putExtra(ApplinkConst.Transaction.EXTRA_OCS, false)
            putExtra(ApplinkConst.Transaction.EXTRA_NEED_REFRESH, needRefresh)
            putExtra(ApplinkConst.Transaction.EXTRA_PRODUCT_TITLE, product.name)
        }
    }

    private fun getAtcPageIntent(product: ProductData): Intent {
        val atcOnly = ATC_ONLY
        val needRefresh = true
        return RouteManager.getIntent(
                context, ApplinkConstInternalMarketplace.NORMAL_CHECKOUT
        ).apply {
            putExtra(ApplinkConst.Transaction.EXTRA_SHOP_ID, product.shop.id.toString())
            putExtra(ApplinkConst.Transaction.EXTRA_PRODUCT_ID, product.productId.toString())
            putExtra(ApplinkConst.Transaction.EXTRA_QUANTITY, product.minOrder)
            putExtra(
                    ApplinkConst.Transaction.EXTRA_SELECTED_VARIANT_ID,
                    product.productId.toString()
            )
            putExtra(ApplinkConst.Transaction.EXTRA_ACTION, atcOnly)
            putExtra(ApplinkConst.Transaction.EXTRA_SHOP_NAME, product.shop.name)
            putExtra(ApplinkConst.Transaction.EXTRA_OCS, false)
            putExtra(ApplinkConst.Transaction.EXTRA_NEED_REFRESH, needRefresh)
        }
    }

    companion object {
        private const val REQUEST_CHECKOUT = 0
    }

}