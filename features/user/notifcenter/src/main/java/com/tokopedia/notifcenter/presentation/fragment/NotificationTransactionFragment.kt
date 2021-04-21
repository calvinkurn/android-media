package com.tokopedia.notifcenter.presentation.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.analytics.NotificationTracker
import com.tokopedia.notifcenter.analytics.NotificationTransactionAnalytics
import com.tokopedia.notifcenter.analytics.NotificationUpdateAnalytics
import com.tokopedia.notifcenter.data.consts.EmptyDataStateProvider
import com.tokopedia.notifcenter.data.consts.buyerMenu
import com.tokopedia.notifcenter.data.consts.sellerMenu
import com.tokopedia.notifcenter.data.model.NotificationViewData
import com.tokopedia.notifcenter.data.state.EmptySource
import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean
import com.tokopedia.notifcenter.listener.TransactionMenuListener
import com.tokopedia.notifcenter.presentation.BaseNotificationFragment
import com.tokopedia.notifcenter.presentation.activity.NotificationActivity
import com.tokopedia.notifcenter.presentation.adapter.NotificationTransactionAdapter
import com.tokopedia.notifcenter.presentation.adapter.typefactory.transaction.NotificationTransactionFactory
import com.tokopedia.notifcenter.presentation.adapter.typefactory.transaction.NotificationTransactionFactoryImpl
import com.tokopedia.notifcenter.presentation.adapter.viewholder.base.BaseNotificationItemViewHolder
import com.tokopedia.notifcenter.presentation.viewmodel.NotificationTransactionViewModel
import com.tokopedia.notifcenter.util.viewModelProvider
import com.tokopedia.unifycomponents.floatingbutton.FloatingButtonItem
import com.tokopedia.unifycomponents.floatingbutton.FloatingButtonUnify
import kotlinx.android.synthetic.main.fragment_notification_transaction.*
import javax.inject.Inject

class NotificationTransactionFragment : BaseNotificationFragment(), TransactionMenuListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: NotificationTransactionViewModel

    private val _adapter by lazy {
        adapter as NotificationTransactionAdapter
    }

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
                R.layout.fragment_notification_transaction,
                container,
                false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onListLastScroll(view)
        initObservable()

        //setup floatingButtonUnify
        val markReadStr = context?.getString(R.string.mark_all_as_read) ?: "Tandai semua dibaca"
        bottomFilterView()?.let {
            val markReadItem = arrayListOf(
                    FloatingButtonItem(markReadStr, false) {
                        viewModel.markAllReadNotification()
                        analytics().trackMarkAllAsRead(markAllReadCounter.toString())
                    }
            )
            it.addItem(markReadItem)
        }

        swipeRefresh?.setOnRefreshListener {
            swipeRefresh?.isRefreshing = true
            fetchUpdateFilter(hashMapOf())

            /*
            * add some delay for 1 sec to
            * preventing twice swipe to refresh*/
            Handler().postDelayed({
                loadInitialData()
            }, REFRESH_DELAY)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getInfoStatusNotification()
    }

    override fun updateFilter(filter: HashMap<String, Int>) {
        fetchUpdateFilter(filter)
    }

    override fun onSuccessMarkAllRead() {
        super.onSuccessMarkAllRead()
        _adapter.markAllAsRead()
    }

    private fun initObservable() {
        viewModel.errorMessage.observe(viewLifecycleOwner, onViewError())

        viewModel.infoNotification.observe(viewLifecycleOwner, Observer {
            _adapter.updateValue(it.notifications)
        })

        viewModel.filterNotification.observe(viewLifecycleOwner, Observer {
            _adapter.addElement(it)
        })

        viewModel.notification.observe(viewLifecycleOwner, Observer {
            _adapter.removeEmptyState()
            if (it.list.isEmpty()) {
                updateScrollListenerState(false)
                _adapter.addElement(EmptyDataStateProvider.emptyData(
                        EmptySource.Transaction
                ))
            } else {
                onSuccessInitiateData(it)
            }
        })

        viewModel.lastNotificationId.observe(viewLifecycleOwner, Observer {
            viewModel.getNotification(it)
        })

        viewModel.markAllNotification.observe(viewLifecycleOwner, Observer {
            onSuccessMarkAllRead()
        })

        viewModel.totalUnreadNotification.observe(viewLifecycleOwner, Observer {
            markAllReadCounter = it
            notifyBottomActionView()
        })
    }

    private fun getNotification(position: String) {
        viewModel.setLastNotificationId(position)
    }

    private fun onViewError() = Observer<String> { message ->
        swipeRefresh?.hide()
        notificationEmpty.show()
        activity?.let {
            NetworkErrorHelper.showEmptyState(it,
                    notificationEmpty,
                    message) {
                viewModel.getInfoStatusNotification()
            }
        }
    }

    override fun loadData(page: Int) {
        swipeRefresh?.isRefreshing = false

        // adding a static buyer notification
        renderList(buyerMenu(), false)

        // adding a static seller notification if user has shop
        if (userSession.hasShop()) {
            _adapter.addElement(sellerMenu())
        }

        viewModel.getInfoStatusNotification()
        viewModel.getNotificationFilter()
    }

    override fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(super.getRecyclerView(view)?.layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                showLoading()
                getNotification(cursor)
            }
        }
    }

    private fun onSuccessInitiateData(notification: NotificationViewData) {
        hideLoading()

        val pagination = notification.paging.hasNext
        if (pagination && notification.list.isNotEmpty()) {
            cursor = (notification.list.last().notificationId)
        }
        _adapter.addElement(notification.list)
        updateScrollListenerState(pagination)

        if (_adapter.dataSize < minimumScrollableNumOfItems
                && endlessRecyclerViewScrollListener != null && pagination) {
            endlessRecyclerViewScrollListener.loadMoreNextPage()
        }
    }

    override fun itemClicked(notification: NotificationItemViewBean, adapterPosition: Int) {
        super.itemClicked(notification, adapterPosition)
        val payloadBackground = BaseNotificationItemViewHolder.PAYLOAD_CHANGE_BACKGROUND
        _adapter.notifyItemChanged(adapterPosition, payloadBackground)
        viewModel.markReadNotification(notification.notificationId)

        //if need to reset the counter
        if (!notification.isRead) {
            updateMarkAllReadCounter()
            notifyBottomActionView()
        }
    }

    private fun fetchUpdateFilter(filter: HashMap<String, Int>) {
        //preventing bounce notification
        _adapter.addElement(EmptyDataStateProvider.clearEmptyData())

        viewModel.updateNotificationFilter(filter)
        cursor = ""
        _adapter.removeItem()
        getNotification(cursor)
    }

    override fun getAnalytic(): NotificationUpdateAnalytics {
        /* Trackers from Update Notification:
         * for tracking atc to pdp,
         * tracking impression of product recommendation */
        return NotificationUpdateAnalytics()
    }

    override fun initInjector() {
        (activity as NotificationActivity)
                .notificationComponent
                .inject(this)
    }

    override fun isHasNotification(): Boolean {
        return viewModel.hasNotification.value ?: false
    }

    override fun sendTrackingData(parent: String, child: String) {
        analytics().sendTrackTransactionTab(parent, child)
    }

    override fun bottomFilterView(): FloatingButtonUnify? = view?.findViewById(R.id.btnFilter)
    override fun onItemStockHandlerClick(notification: NotificationItemViewBean) {}
    override fun getSwipeRefreshLayoutResourceId(): Int = R.id.swipeRefresh
    override fun getRecyclerViewResourceId() = R.id.lstNotification
    override fun onItemClicked(t: Visitable<*>?) = Unit

    override fun analytics(): NotificationTracker {
        return NotificationTransactionAnalytics()
    }

    override fun getAdapterTypeFactory(): BaseAdapterTypeFactory {
        return NotificationTransactionFactoryImpl(
                notificationUpdateListener = this,
                notificationFilterListener = this,
                transactionMenuListener = this,
                userSession = userSession
        )
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory> {
        check(adapterTypeFactory is NotificationTransactionFactory)
        return NotificationTransactionAdapter(
                adapterTypeFactory as NotificationTransactionFactoryImpl
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.cleared()
    }

    companion object {
        private const val REFRESH_DELAY = 1000L
    }

}