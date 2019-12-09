package com.tokopedia.navigation.presentation.fragment

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
import com.tokopedia.coachmark.CoachMarkBuilder
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.design.button.BottomActionView
import com.tokopedia.navigation.R
import com.tokopedia.navigation.analytics.NotificationUpdateAnalytics
import com.tokopedia.navigation.domain.model.EmptyUpdateState
import com.tokopedia.navigation.domain.pojo.NotificationUpdateTotalUnread
import com.tokopedia.navigation.domain.pojo.ProductData
import com.tokopedia.navigation.listener.NotificationActivityListener
import com.tokopedia.navigation.presentation.adapter.NotificationUpdateAdapter
import com.tokopedia.navigation.presentation.adapter.NotificationUpdateFilterAdapter
import com.tokopedia.navigation.presentation.adapter.typefactory.NotificationUpdateFilterSectionTypeFactoryImpl
import com.tokopedia.navigation.presentation.adapter.typefactory.NotificationUpdateTypeFactoryImpl
import com.tokopedia.navigation.presentation.adapter.viewholder.notificationupdate.NotificationUpdateItemViewHolder
import com.tokopedia.navigation.presentation.di.notification.DaggerNotificationUpdateComponent
import com.tokopedia.navigation.presentation.presenter.NotificationUpdatePresenter
import com.tokopedia.navigation.presentation.view.listener.NotificationActivityContract
import com.tokopedia.navigation.presentation.view.listener.NotificationUpdateContract
import com.tokopedia.navigation.presentation.view.listener.NotificationUpdateItemListener
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateFilterItemViewModel
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateItemViewModel
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateViewModel
import com.tokopedia.navigation.widget.ChipFilterItemDivider
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

/**
 * @author : Steven 10/04/19
 */
class NotificationUpdateFragment : BaseListFragment<Visitable<*>, BaseAdapterTypeFactory>()
        , NotificationUpdateContract.View, NotificationUpdateItemListener,
        NotificationUpdateFilterAdapter.FilterAdapterListener,
        NotificationActivityListener, NotificationUpdateLongerTextFragment.LongerContentListener {

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

    @Inject
    lateinit var presenter: NotificationUpdatePresenter

    @Inject
    lateinit var analytics: NotificationUpdateAnalytics

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

    override fun showOnBoarding(coachMarkItems: ArrayList<CoachMarkItem>, tag: String) {
        if (!::filterRecyclerView.isInitialized) return
        coachMarkItems.add(
                1 ,
                CoachMarkItem(
                        filterRecyclerView,
                        getString(R.string.coachicon_title_filter),
                        getString(R.string.coachicon_description_filter)
                )
        )
        val coachMark = CoachMarkBuilder().build()
        coachMark.show(activity, tag, coachMarkItems)
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

    override fun onItemClicked(datum: Visitable<*>?) {

    }

    override fun getScreenName(): String {
        return ""
    }

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

    private fun onSuccessInitiateData(): (NotificationUpdateViewModel) -> Unit {
        return {
            hideLoading()
            _adapter.removeEmptyState()

            if (it.list.isEmpty()) {
                updateScrollListenerState(false)
                val emptyState = EmptyUpdateState(
                        R.drawable.bg_empty_state_common,
                        getString(R.string.no_notification_update_yet)
                )
                _adapter.addElement(emptyState)
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

    private fun onSuccessGetFilter(): (ArrayList<NotificationUpdateFilterItemViewModel>) -> Unit {
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

    override fun itemClicked(viewModel: NotificationUpdateItemViewModel, adapterPosition: Int) {
        adapter.notifyItemChanged(adapterPosition, NotificationUpdateItemViewHolder.PAYLOAD_CHANGE_BACKGROUND)
        analytics.trackClickNotifList(viewModel)
        presenter.markReadNotif(viewModel.notificationId)
        val needToResetCounter = !viewModel.isRead
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

    override fun showTextLonger(model: NotificationUpdateItemViewModel) {
        val bundle = Bundle()
        bundle.putString(PARAM_CONTENT_IMAGE, model.contentUrl)
        bundle.putString(PARAM_CONTENT_IMAGE_TYPE, model.typeLink.toString())
        bundle.putString(PARAM_CTA_APPLINK, model.appLink)
        bundle.putString(PARAM_CONTENT_TEXT, model.body)
        bundle.putString(PARAM_CONTENT_TITLE, model.title)
        bundle.putString(PARAM_BUTTON_TEXT, model.btnText)
        bundle.putString(PARAM_TEMPLATE_KEY, model.templateKey)

        if (!::longerTextDialog.isInitialized) {
            longerTextDialog = NotificationUpdateLongerTextFragment.createInstance(bundle)
        } else {
            longerTextDialog.arguments = bundle
        }

        if (!longerTextDialog.isAdded)
            longerTextDialog.show(childFragmentManager, "Longer Text Bottom Sheet")
    }

    override fun trackNotificationImpression(element: NotificationUpdateItemViewModel) {
        analytics.saveNotificationImpression(element)
    }

    override fun trackOnClickCtaButton(templateKey: String) {
        analytics.trackOnClickLongerContentBtn(templateKey)
    }

    companion object {
        val PARAM_CONTENT_TITLE = "content title"
        val PARAM_CONTENT_TEXT = "content text"
        val PARAM_CONTENT_IMAGE = "content image"
        val PARAM_CONTENT_IMAGE_TYPE = "content image type"
        val PARAM_CTA_APPLINK = "cta applink"
        val PARAM_BUTTON_TEXT = "button text"
        val PARAM_TEMPLATE_KEY = "template key"
    }
}