package com.tokopedia.navigation.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
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
    private lateinit var longerTextDialog: BottomSheetDialogFragment

    //last notification id
    private var cursor = ""

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
            _adapter.updateValue(it.notifications)

            //get filter data
            viewModel.getNotificationFilter()
        })

        viewModel.filterNotification.observe(this, Observer {
            _adapter.addElement(it)

            //get notification
            getNotification(cursor)
        })

        viewModel.notification.observe(this, Observer {
            /* flag for showing filter */
            if (it.list.isNotEmpty()) {
                _notification.add(it.list.first())
            }

            /* hide filter item if there's no notification */
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
        //pagination
        val pagination = notification.paging.hasNext
        if (pagination && !notification.list.isEmpty()) {
            cursor = (notification.list.last().notificationId)
        }

        _adapter.addElement(notification.list)
    }

    override fun itemClicked(notification: TransactionItemNotification, adapterPosition: Int) {
        adapter.notifyItemChanged(adapterPosition, NotificationTransactionItemViewHolder.PAYLOAD_CHANGE_BACKGROUND)
        viewModel.markReadNotification(notification.notificationId)
    }

    override fun updateFilter(filter: HashMap<String, Int>) {
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

    override fun addProductToCart(product: ProductData, onSuccessAddToCart: () -> Unit) {}

    override fun showTextLonger(element: TransactionItemNotification) {
        val bundle = Bundle().apply {
            with(element) {
                putString(PARAM_CONTENT_IMAGE, contentUrl)
                putString(PARAM_CONTENT_IMAGE_TYPE, typeLink.toString())
                putString(PARAM_CTA_APPLINK, appLink)
                putString(PARAM_CONTENT_TEXT, body)
                putString(PARAM_CONTENT_TITLE, title)
                putString(PARAM_BUTTON_TEXT, btnText)
                putString(PARAM_TEMPLATE_KEY, templateKey)
            }
        }

        if (!::longerTextDialog.isInitialized) {
            longerTextDialog = NotificationUpdateLongerTextFragment.createInstance(bundle)
        } else {
            longerTextDialog.arguments = bundle
        }

        if (!longerTextDialog.isAdded) {
            longerTextDialog.show(childFragmentManager, "Longer Text Bottom Sheet")
        }
    }

    override fun getAdapterTypeFactory(): BaseAdapterTypeFactory {
        return NotificationTransactionFactoryImpl(
                notificationUpdateListener = this,
                notificationFilterListener = this,
                transactionMenuListener = this)
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
    override fun onItemClicked(t: Visitable<*>?) = Unit
    override fun getScreenName() = SCREEN_NAME

    companion object {
        private const val SCREEN_NAME = "Notification Transaction"

        private const val PARAM_CONTENT_TITLE = "content title"
        private const val PARAM_CONTENT_TEXT = "content text"
        private const val PARAM_CONTENT_IMAGE = "content image"
        private const val PARAM_CONTENT_IMAGE_TYPE = "content image type"
        private const val PARAM_CTA_APPLINK = "cta applink"
        private const val PARAM_BUTTON_TEXT = "button text"
        private const val PARAM_TEMPLATE_KEY = "template key"
    }

}