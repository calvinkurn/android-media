package com.tokopedia.notifcenter.presentation.fragment

import android.animation.LayoutTransition
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.design.button.BottomActionView
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.analytics.NotificationUpdateAnalytics
import com.tokopedia.notifcenter.data.consts.EmptyDataStateProvider
import com.tokopedia.notifcenter.data.entity.NotificationUpdateTotalUnread
import com.tokopedia.notifcenter.data.entity.ProductData
import com.tokopedia.notifcenter.data.model.NotificationViewData
import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean
import com.tokopedia.notifcenter.data.viewbean.NotificationUpdateFilterViewBean
import com.tokopedia.notifcenter.di.DaggerNotificationUpdateComponent
import com.tokopedia.notifcenter.listener.NotificationItemListener
import com.tokopedia.notifcenter.presentation.adapter.NotificationUpdateAdapter
import com.tokopedia.notifcenter.presentation.adapter.NotificationUpdateFilterAdapter
import com.tokopedia.notifcenter.presentation.adapter.typefactory.filter.NotificationUpdateFilterSectionTypeFactoryImpl
import com.tokopedia.notifcenter.presentation.adapter.typefactory.update.NotificationUpdateTypeFactoryImpl
import com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.BaseNotificationItemViewHolder
import com.tokopedia.notifcenter.presentation.contract.NotificationActivityContract
import com.tokopedia.notifcenter.presentation.contract.NotificationUpdateContract
import com.tokopedia.notifcenter.presentation.presenter.NotificationUpdatePresenter
import com.tokopedia.notifcenter.widget.ChipFilterItemDivider
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

/**
 * @author : Steven 10/04/19
 */
class NotificationUpdateFragment : BaseListFragment<Visitable<*>,
        BaseAdapterTypeFactory>(),
        NotificationUpdateContract.View,
        NotificationItemListener,
        NotificationUpdateFilterAdapter.FilterAdapterListener,
        NotificationUpdateLongerTextFragment.LongerContentListener {

    private var cursor = ""
    private var lastItem = 0
    private var markAllReadCounter = 0L

    private lateinit var bottomActionView: BottomActionView

    private lateinit var filterRecyclerView: RecyclerView
    private lateinit var longerTextDialog: BottomSheetDialogFragment
    private var filterAdapter: NotificationUpdateFilterAdapter? = null

    override fun getAdapterTypeFactory(): BaseAdapterTypeFactory {
        return NotificationUpdateTypeFactoryImpl(this)
    }

    @Inject lateinit var presenter: NotificationUpdatePresenter
    @Inject lateinit var analytics: NotificationUpdateAnalytics

    private var notificationUpdateListener: NotificationUpdateListener? = null

    private val _adapter by lazy { adapter as NotificationUpdateAdapter }

    interface NotificationUpdateListener {
        fun onSuccessLoadNotifUpdate()
    }

    override fun onAttachActivity(context: Context?) {
        if (context is NotificationUpdateListener) {
            notificationUpdateListener = context
        }

        filterAdapter = NotificationUpdateFilterAdapter(
                NotificationUpdateFilterSectionTypeFactoryImpl(),
                this,
                UserSession(context?.applicationContext)
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notification_update, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.getFilter(onSuccessGetFilter())
        presenter.getTotalUnreadCounter(onSuccessGetTotalUnreadCounter())

        bottomActionView = view.findViewById(R.id.filterBtn)
        val recyclerView = super.getRecyclerView(view)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            bottomActionView.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        }

        bottomActionView.setButton1OnClickListener {
            analytics.trackMarkAllAsRead(markAllReadCounter.toString())
            presenter.markAllReadNotificationUpdate(onSuccessMarkAllReadNotificationUpdate())
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy < 0) { // going up
                    notifyBottomActionView()
                } else if (dy > 0) { // going down
                    bottomActionView.hide()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == SCROLL_STATE_IDLE) {
                    val layoutManager = (recyclerView?.layoutManager)
                    if (layoutManager != null && layoutManager is LinearLayoutManager) {
                        val temp = layoutManager.findLastVisibleItemPosition()
                        if (temp > lastItem) {
                            lastItem = temp
                        }
                    }
                }
            }
        })


        filterRecyclerView = view.findViewById(R.id.filter_list)
        filterRecyclerView.adapter = filterAdapter
        filterRecyclerView.addItemDecoration(ChipFilterItemDivider(context))
    }

    override fun updateFilter(filter: HashMap<String, Int>) {
        presenter.updateFilter(filter)
        cursor = ""
        loadInitialData()
    }

    override fun sentFilterAnalytic(analyticData: String) {
        analytics.trackClickFilterRequest(analyticData)
    }

    private fun onSuccessMarkAllReadNotificationUpdate(): () -> Unit {
        return {
            (adapter as NotificationUpdateAdapter).markAllAsRead()
            if (activity != null && activity is NotificationActivityContract.View) {
                (activity as NotificationActivityContract.View).resetCounterNotificationUpdate()
            }
            markAllReadCounter = 0L
            notifyBottomActionView()
        }
    }

    private fun notifyBottomActionView() {
        bottomActionView?.let {
            if (markAllReadCounter == 0L) {
                it.hide()
            } else {
                it.show()
            }
        }
    }

    override fun onItemClicked(datum: Visitable<*>?) {}

    override fun getScreenName(): String = ""

    override fun initInjector() {
        activity?.let {
            (it.applicationContext as BaseMainApplication).baseAppComponent
        }.let {
            DaggerNotificationUpdateComponent.builder()
                    .baseAppComponent(it)
                    .build()
                    .inject(this)
        }
        presenter.attachView(this)
    }

    override fun loadData(page: Int) {
        presenter.loadData(cursor, onSuccessInitiateData(), onErrorInitiateData())
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory> {
        return NotificationUpdateAdapter(NotificationUpdateTypeFactoryImpl(this))
    }

    private fun onErrorInitiateData(): (Throwable) -> Unit {
        return {
            if (activity != null) {
                SnackbarManager.make(activity, ErrorHandler.getErrorMessage(activity, it), Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun onSuccessInitiateData(): (NotificationViewData) -> Unit {
        return {
            hideLoading()
            _adapter.removeEmptyState()

            if (it.list.isEmpty()) {
                updateScrollListenerState(false)
                _adapter.addElement(EmptyDataStateProvider.emptyData())
            } else {
                val canLoadMore = it.paging.hasNext
                if (canLoadMore && !it.list.isEmpty()) {
                    cursor = (it.list.last().notificationId)
                }
                if (swipeToRefresh.isRefreshing) {
                    notificationUpdateListener?.onSuccessLoadNotifUpdate()
                }

                _adapter.addElement(it.list)
                updateScrollListenerState(canLoadMore)

                if (_adapter.dataSize < minimumScrollableNumOfItems
                        && endlessRecyclerViewScrollListener != null && canLoadMore) {
                    endlessRecyclerViewScrollListener.loadMoreNextPage()
                }
            }
        }
    }

    private fun onSuccessGetFilter(): (ArrayList<NotificationUpdateFilterViewBean>) -> Unit {
        return {
            filterAdapter?.updateData(it)
        }
    }


    override fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(getRecyclerView(view).layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                showLoading()
                loadData(page)
            }
        }
    }

    override fun itemClicked(notification: NotificationItemViewBean, adapterPosition: Int) {
        adapter.notifyItemChanged(adapterPosition, BaseNotificationItemViewHolder.PAYLOAD_CHANGE_BACKGROUND)
        analytics.trackClickNotifList(notification)
        presenter.markReadNotif(notification.notificationId)
        val needToResetCounter = !notification.isRead
        if (needToResetCounter) {
            updateMarkAllReadCounter()
            notifyBottomActionView()
        }
    }

    private fun updateMarkAllReadCounter() {
        markAllReadCounter -= 1
    }

    override fun onSwipeRefresh() {
        cursor = ""
        presenter.getTotalUnreadCounter(onSuccessGetTotalUnreadCounter())
        super.onSwipeRefresh()
    }

    override fun getSwipeRefreshLayout(view: View?): SwipeRefreshLayout? {
        return view?.findViewById(R.id.swipeToRefresh)
    }

    override fun onPause() {
        super.onPause()
        sendAnalyticsScrollBottom()
    }

    override fun onDestroyView() {
        sendAnalyticsScrollBottom()
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    private fun sendAnalyticsScrollBottom() {
        if (lastItem > 0) analytics.trackScrollBottom(lastItem.toString())
    }

    private fun onSuccessGetTotalUnreadCounter(): (NotificationUpdateTotalUnread) -> Unit {
        return {
            markAllReadCounter = it.pojo.notifUnreadInt
            notifyBottomActionView()
        }
    }

    override fun getAnalytic(): NotificationUpdateAnalytics {
        return analytics
    }

    override fun addProductToCart(product: ProductData, onSuccessAddToCart: () -> Unit) {
        presenter.addProductToCart(product, onSuccessAddToCart)
    }

    override fun onTrackerAddToCart(product: ProductData, atc: DataModel) {
        analytics.trackAtcOnClick(product, atc)
    }

    override fun showMessageAtcError(e: Throwable?) {
        view?.let {
            val errorMessage = com.tokopedia.network.utils.ErrorHandler.getErrorMessage(it.context, e)
            Toaster.showError(it, errorMessage, Snackbar.LENGTH_LONG)
        }
    }

    override fun showMessageAtcSuccess(message: String) {
        view?.let {
            Toaster.showNormalWithAction(
                    it,
                    message,
                    Snackbar.LENGTH_LONG,
                    getString(R.string.wishlist_check_cart),
                    onClickSeeButtonOnAtcSuccessToaster()
            )
        }
    }

    private fun onClickSeeButtonOnAtcSuccessToaster(): View.OnClickListener {
        return View.OnClickListener {
            RouteManager.route(it.context, ApplinkConstInternalMarketplace.CART)
        }
    }

    override fun showTextLonger(element: NotificationItemViewBean) {
        val bundle = Bundle()

        bundle.putString(PARAM_CONTENT_IMAGE, element.contentUrl)
        bundle.putString(PARAM_CONTENT_IMAGE_TYPE, element.typeLink.toString())
        bundle.putString(PARAM_CTA_APPLINK, element.appLink)
        bundle.putString(PARAM_CONTENT_TEXT, element.body)
        bundle.putString(PARAM_CONTENT_TITLE, element.title)
        bundle.putString(PARAM_BUTTON_TEXT, element.btnText)
        bundle.putString(PARAM_TEMPLATE_KEY, element.templateKey)

        if (!::longerTextDialog.isInitialized) {
            longerTextDialog = NotificationUpdateLongerTextFragment.createInstance(bundle)
        } else {
            longerTextDialog.arguments = bundle
        }

        if (!longerTextDialog.isAdded) {
            longerTextDialog.show(childFragmentManager, "Longer Text Bottom Sheet")
        }
    }

    override fun trackNotificationImpression(element: NotificationItemViewBean) {
        analytics.saveNotificationImpression(element)
    }

    override fun trackOnClickCtaButton(templateKey: String) {
        analytics.trackOnClickLongerContentBtn(templateKey)
    }

    companion object {
        const val PARAM_CONTENT_TITLE = "content title"
        const val PARAM_CONTENT_TEXT = "content text"
        const val PARAM_CONTENT_IMAGE = "content image"
        const val PARAM_CONTENT_IMAGE_TYPE = "content image type"
        const val PARAM_CTA_APPLINK = "cta applink"
        const val PARAM_BUTTON_TEXT = "button text"
        const val PARAM_TEMPLATE_KEY = "template key"
    }
}