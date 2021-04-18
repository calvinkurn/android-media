package com.tokopedia.notifcenter.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.analytics.NotificationTracker
import com.tokopedia.notifcenter.analytics.NotificationUpdateAnalytics
import com.tokopedia.notifcenter.data.consts.EmptyDataStateProvider
import com.tokopedia.notifcenter.data.entity.NotificationUpdateTotalUnread
import com.tokopedia.notifcenter.data.entity.ProductData
import com.tokopedia.notifcenter.data.model.NotificationViewData
import com.tokopedia.notifcenter.data.state.BottomSheetType
import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean
import com.tokopedia.notifcenter.data.viewbean.NotificationUpdateFilterViewBean
import com.tokopedia.notifcenter.di.DaggerNotificationComponent
import com.tokopedia.notifcenter.di.module.CommonModule
import com.tokopedia.notifcenter.listener.NotificationUpdateListener
import com.tokopedia.notifcenter.presentation.BaseNotificationFragment
import com.tokopedia.notifcenter.presentation.activity.NotificationActivity
import com.tokopedia.notifcenter.presentation.adapter.NotificationUpdateAdapter
import com.tokopedia.notifcenter.presentation.adapter.NotificationUpdateFilterAdapter
import com.tokopedia.notifcenter.presentation.adapter.typefactory.filter.NotificationUpdateFilterSectionTypeFactoryImpl
import com.tokopedia.notifcenter.presentation.adapter.typefactory.update.NotificationUpdateTypeFactoryImpl
import com.tokopedia.notifcenter.presentation.adapter.viewholder.base.BaseNotificationItemViewHolder
import com.tokopedia.notifcenter.presentation.contract.NotificationActivityContract
import com.tokopedia.notifcenter.presentation.contract.NotificationUpdateContract
import com.tokopedia.notifcenter.presentation.presenter.NotificationUpdatePresenter
import com.tokopedia.notifcenter.presentation.viewmodel.NotificationUpdateViewModel
import com.tokopedia.notifcenter.util.isSingleItem
import com.tokopedia.notifcenter.util.viewModelProvider
import com.tokopedia.notifcenter.widget.ChipFilterItemDivider
import com.tokopedia.unifycomponents.floatingbutton.FloatingButtonItem
import com.tokopedia.unifycomponents.floatingbutton.FloatingButtonUnify
import kotlinx.android.synthetic.main.fragment_notification_update.*
import javax.inject.Inject

open class NotificationUpdateFragment : BaseNotificationFragment(),
        NotificationUpdateContract.View,
        NotificationLongerTextDialog.LongerContentListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var presenter: NotificationUpdatePresenter
    @Inject
    lateinit var analytics: NotificationUpdateAnalytics

    private lateinit var viewModel: NotificationUpdateViewModel

    private val notificationUpdateListener by lazy { context as NotificationUpdateListener }
    private val _adapter by lazy { adapter as NotificationUpdateAdapter }

    val filterAdapter by lazy {
        NotificationUpdateFilterAdapter(
                NotificationUpdateFilterSectionTypeFactoryImpl(),
                this,
                userSession
        )
    }

    /*
    * flag of first time notification loaded
    * purpose: handling visibility of filter list
    * */
    private var isFirstLoaded = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(viewModelFactory)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
                R.layout.fragment_notification_update,
                container,
                false
        )
    }

    /*
    * notification id for buyer info consume
    * the id comes from tokopedia://notif-center/{id}
    * */
    private val notificationId by lazy {
        (activity as? NotificationActivity)?.notificationId ?: ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onListLastScroll(view)
        initLoadPresenter()
        initObservable()

        //setup floatingButtonUnify
        val markReadStr = context?.getString(R.string.mark_all_as_read) ?: "Tandai semua dibaca"
        bottomFilterView()?.let {
            val markReadItem = arrayListOf(
                    FloatingButtonItem(markReadStr, false) {
                        analytics.trackMarkAllAsRead(markAllReadCounter.toString())
                        presenter.markAllReadNotificationUpdate(::onSuccessMarkAllRead)
                    }
            )
            it.addItem(markReadItem)
        }

        lstFilter?.adapter = filterAdapter
        lstFilter?.addItemDecoration(ChipFilterItemDivider(context))
    }

    override fun updateFilter(filter: HashMap<String, Int>) {
        presenter.updateFilter(filter)
        cursor = ""
        loadInitialData()
    }

    override fun onSuccessMarkAllRead() {
        super.onSuccessMarkAllRead()
        (adapter as NotificationUpdateAdapter).markAllAsRead()
        if (activity != null && activity is NotificationActivityContract.View) {
            (activity as NotificationActivityContract.View).resetCounterNotificationUpdate()
        }
    }

    private fun initObservable() {
        viewModel.productStockHandler.observe(viewLifecycleOwner, Observer {
            showNotificationDetail(BottomSheetType.StockHandler, it)
        })
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            showToastMessageError(it)
        })
        viewModel.singleNotification.observe(viewLifecycleOwner, Observer {
            onSuccessInitiateData(it)
        })
    }

    private fun initLoadPresenter() {
        presenter.getFilter(onSuccessGetFilter())
        presenter.getTotalUnreadCounter(onSuccessGetTotalUnreadCounter())
    }

    override fun initInjector() {
        if (GlobalConfig.isSellerApp()) {
            initSellerAppInjector()
        } else {
            (activity as NotificationActivity)
                    .notificationComponent
                    .inject(this)
        }
        presenter.attachView(this)
    }

    private fun initSellerAppInjector() {
        val component = (requireContext().applicationContext as BaseMainApplication).baseAppComponent
        DaggerNotificationComponent.builder()
                .baseAppComponent(component)
                .commonModule(CommonModule(requireContext()))
                .build()
                .inject(this)
    }

    override fun loadData(page: Int) {
        notificationId.let {
            if (it.isNotEmpty()) {
                lstFilter?.hide()
                viewModel.getSingleNotification(it)
            } else {
                presenter.loadData(
                        cursor,
                        ::onSuccessInitiateData,
                        onErrorInitiateData()
                )
            }
        }
    }

    private fun onErrorInitiateData(): (Throwable) -> Unit {
        return {
            if (activity != null) {
                SnackbarManager.make(
                        activity,
                        ErrorHandler.getErrorMessage(activity, it),
                        Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun onSuccessInitiateData(notification: NotificationViewData) {
        hideLoading()
        _adapter.removeEmptyState()

        if (isFirstLoaded && notification.list.isEmpty()) {
            lstFilter?.hide()
        }

        if (notification.list.isEmpty()) {
            updateScrollListenerState(false)
            _adapter.addElement(EmptyDataStateProvider.emptyData())
        } else {
            val canLoadMore = notification.paging.hasNext
            if (canLoadMore && notification.list.isNotEmpty()) {
                cursor = (notification.list.last().notificationId)
            }
            if (swipeToRefresh.isRefreshing) {
                notificationUpdateListener.onSuccessLoadNotificationUpdate()
            }

            isFirstLoaded = false
            lstFilter?.show()

            _adapter.addElement(notification.list)
            updateScrollListenerState(canLoadMore)

            if (_adapter.dataSize < minimumScrollableNumOfItems
                    && endlessRecyclerViewScrollListener != null && canLoadMore) {
                endlessRecyclerViewScrollListener.loadMoreNextPage()
            }
        }
    }

    open fun onSuccessGetFilter(): (ArrayList<NotificationUpdateFilterViewBean>) -> Unit {
        return {
            filterAdapter.updateData(it)
        }
    }

    override fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(getRecyclerView(view)?.layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                showLoading()
                loadData(page)
            }
        }
    }

    override fun itemClicked(notification: NotificationItemViewBean, adapterPosition: Int) {
        super.itemClicked(notification, adapterPosition)
        val payloadBackground = BaseNotificationItemViewHolder.PAYLOAD_CHANGE_BACKGROUND
        adapter.notifyItemChanged(adapterPosition, payloadBackground)
        presenter.markReadNotif(notification.notificationId)

        //if need to reset the counter
        if (!notification.isRead) {
            updateMarkAllReadCounter()
            notifyBottomActionView()
        }
    }

    override fun onItemStockHandlerClick(notification: NotificationItemViewBean) {
        //if product data only one, check product stock
        if (notification.products.isSingleItem()) {
            viewModel.isProductStockHandler(notification.notificationId)
        }
    }

    override fun onItemMultipleStockHandlerClick(notification: NotificationItemViewBean) {
        val productData = notification.getAtcProduct()
        productData?.let {
            viewModel.isProductStockHandlerMultiple(notification.notificationId, it)
        }
    }

    override fun onSwipeRefresh() {
        cursor = ""
        presenter.getTotalUnreadCounter(onSuccessGetTotalUnreadCounter())
        super.onSwipeRefresh()
    }

    override fun showToastMessageError(e: Throwable?) {
        showMessageError(e)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
        viewModel.cleared()
    }

    private fun onSuccessGetTotalUnreadCounter(): (NotificationUpdateTotalUnread) -> Unit {
        return {
            markAllReadCounter = it.pojo.notifUnreadInt
            notifyBottomActionView()
        }
    }

    override fun addProductToCart(
            product: ProductData,
            onSuccessAddToCart: (DataModel) -> Unit
    ) {
        presenter.addProductToCart(userSession.userId, product, onSuccessAddToCart)
    }

    override fun trackOnClickCtaButton(templateKey: String, notificationId: String) {
        analytics.trackOnClickLongerContentBtn(templateKey, notificationId)
    }

    override fun getSwipeRefreshLayout(view: View?): SwipeRefreshLayout? = view?.findViewById(R.id.swipeToRefresh)

    override fun bottomFilterView(): FloatingButtonUnify? = view?.findViewById(R.id.filterBtn)

    override fun getRecyclerViewResourceId(): Int = R.id.recycler_view

    override fun analytics(): NotificationTracker = getAnalytic()

    override fun getAnalytic(): NotificationUpdateAnalytics {
        return NotificationUpdateAnalytics()
    }

    override fun getAdapterTypeFactory(): BaseAdapterTypeFactory {
        return NotificationUpdateTypeFactoryImpl(this)
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory> {
        return NotificationUpdateAdapter(NotificationUpdateTypeFactoryImpl(this))
    }

}