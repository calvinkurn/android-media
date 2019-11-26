package com.tokopedia.navigation.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.navigation.R
import com.tokopedia.navigation.analytics.NotificationTransactionAnalytics
import com.tokopedia.navigation.analytics.NotificationUpdateAnalytics
import com.tokopedia.navigation.data.consts.buyerMenu
import com.tokopedia.navigation.data.consts.sellerMenu
import com.tokopedia.navigation.data.mapper.NotificationMapper
import com.tokopedia.navigation.domain.model.TransactionItemNotification
import com.tokopedia.navigation.domain.model.TransactionNotification
import com.tokopedia.navigation.domain.pojo.ProductData
import com.tokopedia.navigation.listener.TransactionMenuListener
import com.tokopedia.navigation.presentation.adapter.NotificationTransactionAdapter
import com.tokopedia.navigation.presentation.adapter.typefactory.NotificationTransactionFactory
import com.tokopedia.navigation.presentation.adapter.typefactory.NotificationTransactionFactoryImpl
import com.tokopedia.navigation.presentation.adapter.viewholder.transaction.NotificationFilterViewHolder
import com.tokopedia.navigation.presentation.adapter.viewholder.transaction.NotificationTransactionItemViewHolder
import com.tokopedia.navigation.presentation.di.notification.DaggerNotificationTransactionComponent
import com.tokopedia.navigation.presentation.view.listener.NotificationTransactionItemListener
import com.tokopedia.navigation.presentation.viewmodel.NotificationTransactionViewModel
import kotlinx.android.synthetic.main.fragment_notification_transaction.*
import javax.inject.Inject

class NotificationTransactionFragment: BaseListFragment<Visitable<*>, BaseAdapterTypeFactory>(),
        NotificationTransactionItemListener,
        NotificationFilterViewHolder.NotifFilterListener,
        TransactionMenuListener {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var analytics: NotificationTransactionAnalytics

    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(NotificationTransactionViewModel::class.java) }

    private val _adapter by lazy { adapter as NotificationTransactionAdapter }

    //last notification id
    private var cursor = ""

    //flag filter
    private var isNotificationFilter = false

    //flag for notification transaction
    private val _notification = mutableListOf<TransactionItemNotification>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notification_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.errorMessage.observe(this, onViewError())
        viewModel.infoNotification.observe(this, Observer {
            if (NotificationMapper.isHasShop(it)) {
                _adapter.addElement(sellerMenu())
            }
            viewModel.getNotificationFilter()
            _adapter.updateValue(it.notifications)
        })
        viewModel.filterNotification.observe(this, Observer {
            _adapter.addElement(it)

            //get notification
            getNotification(cursor)
        })
        viewModel.notification.observe(this, Observer {
            _notification.add(it.list.first()) //flag
            if (_notification.isEmpty()) {
                _adapter.hideFilterItem()
            }
            onSuccessNotificationData(it)
        })
    }

    private fun getNotification(position: String) {
        viewModel.getTransactionNotification(position)
    }

    private fun onViewError() = Observer<String> { message ->
        swipeRefresh.hide()
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
        //first data and mandatory menu is buyer on first item
        _adapter.clearAllElements()
        renderList(buyerMenu(), false)
        viewModel.getInfoStatusNotification()
    }

    override fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(getRecyclerView(view).layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                showLoading()
                getNotification(page.toString())
            }
        }
    }

    private fun onSuccessNotificationData(notification: TransactionNotification) {
        val pagination = notification.paging.hasNext
        if (pagination && !notification.list.isEmpty()) {
            cursor = (notification.list.last().notificationId)
        }
        if (isNotificationFilter) {
            _adapter.removeTransaction()
        }
        _adapter.addElement(notification.list)
    }

    override fun itemClicked(notification: TransactionItemNotification, adapterPosition: Int) {
        adapter.notifyItemChanged(adapterPosition, NotificationTransactionItemViewHolder.PAYLOAD_CHANGE_BACKGROUND)
        viewModel.markReadNotification(notification.notificationId)
    }

    override fun updateFilter(filter: HashMap<String, Int>) {
        viewModel.updateNotificationFilter(filter)
        isNotificationFilter = true
        cursor = ""
        getNotification(cursor)
    }

    override fun getAnalytic(): NotificationUpdateAnalytics = NotificationUpdateAnalytics()

    override fun addProductToCart(product: ProductData, onSuccessAddToCart: () -> Unit) {}

    override fun showTextLonger(element: TransactionItemNotification) {}

    override fun sentFilterAnalytic(analyticData: String) {
        analytics.trackClickFilterRequest(analyticData)
    }

    override fun sendTrackingData(parent: String, child: String) {
        analytics.sendTrackTransactionTab(parent, child)
    }

    override fun trackNotificationImpression(element: TransactionItemNotification) {
        analytics.saveNotificationImpression(element)
    }

    override fun getSwipeRefreshLayoutResourceId(): Int = R.id.swipeRefresh

    override fun getRecyclerViewResourceId() = R.id.lstNotification

    override fun hasInitialSwipeRefresh(): Boolean = true

    override fun getAdapterTypeFactory(): BaseAdapterTypeFactory {
        return NotificationTransactionFactoryImpl(this, this, this)
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory> {
        if (adapterTypeFactory !is NotificationTransactionFactory) throw IllegalStateException()
        val typeFactory = adapterTypeFactory as NotificationTransactionFactoryImpl
        return NotificationTransactionAdapter(typeFactory)
    }

    override fun initInjector() {
        DaggerNotificationTransactionComponent.builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    override fun onItemClicked(t: Visitable<*>?) {}
    override fun getScreenName() = SCREEN_NAME

    companion object {
        const val SCREEN_NAME = "Notification Transaction"
    }

}