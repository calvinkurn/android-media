package com.tokopedia.navigation.presentation.fragment

import android.animation.LayoutTransition
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.design.button.BottomActionView
import com.tokopedia.design.component.TextViewCompat
import com.tokopedia.navigation.R
import com.tokopedia.navigation.analytics.NotificationUpdateAnalytics
import com.tokopedia.navigation.domain.pojo.NotificationUpdateTotalUnread
import com.tokopedia.navigation.presentation.adapter.NotificationUpdateAdapter
import com.tokopedia.navigation.presentation.adapter.NotificationUpdateFilterAdapter
import com.tokopedia.navigation.presentation.adapter.typefactory.NotificationUpdateFilterSectionTypeFactoryImpl
import com.tokopedia.navigation.presentation.adapter.typefactory.NotificationUpdateTypeFactoryImpl
import com.tokopedia.navigation.presentation.di.notification.DaggerNotificationUpdateComponent
import com.tokopedia.navigation.presentation.presenter.NotificationUpdatePresenter
import com.tokopedia.navigation.presentation.view.listener.NotificationActivityContract
import com.tokopedia.navigation.presentation.view.listener.NotificationSectionFilterListener
import com.tokopedia.navigation.presentation.view.listener.NotificationUpdateContract
import com.tokopedia.navigation.presentation.view.listener.NotificationUpdateItemListener
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateFilterItemViewModel
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateItemViewModel
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateViewModel
import com.tokopedia.navigation.widget.ChipFilterItemDivider
import javax.inject.Inject

/**
 * @author : Steven 10/04/19
 */
class NotificationUpdateFragment : BaseListFragment<Visitable<*>, BaseAdapterTypeFactory>()
        , NotificationUpdateContract.View, NotificationSectionFilterListener,
        NotificationUpdateItemListener, NotificationUpdateFilterAdapter.FilterAdapterListener {

    private var cursor = ""
    private var lastItem = 0
    private var markAllReadCounter = 0L

    private lateinit var filterViewModel: ArrayList<NotificationUpdateFilterItemViewModel>
    private val selectedItemList = HashMap<Int, Int>()
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var bottomActionView: BottomActionView

    private lateinit var filterRecyclerView: RecyclerView
    private lateinit var longerTextDialog: BottomSheetDialogFragment
    private val filterAdapter = NotificationUpdateFilterAdapter(
            NotificationUpdateFilterSectionTypeFactoryImpl(),
            this
    )

    override fun getAdapterTypeFactory(): BaseAdapterTypeFactory {
        return NotificationUpdateTypeFactoryImpl(this)
    }

    @Inject
    lateinit var presenter: NotificationUpdatePresenter

    @Inject
    lateinit var analytics: NotificationUpdateAnalytics

    private var notificationUpdateListener: NotificationUpdateListener? = null

    interface NotificationUpdateListener {
        fun onSuccessLoadNotifUpdate()
    }

    override fun onAttachActivity(context: Context?) {
        if (context is NotificationUpdateListener) {
            notificationUpdateListener = context
        }
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
            if (::bottomSheetDialog.isInitialized) {
                bottomSheetDialog.show()
            }
        }

        bottomActionView.setButton2OnClickListener {
            analytics.trackMarkAllAsRead(markAllReadCounter.toString())
            presenter.markAllReadNotificationUpdate(onSuccessMarkAllReadNotificationUpdate())
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy < 0) { // going up
                    if (adapter.dataSize > 0) {
                        bottomActionView.show()
                    }
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
                        if(temp > lastItem){
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
        loadInitialData()
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

    private fun createFilterView(): View? {
        val filterView = activity?.layoutInflater?.inflate(R.layout
                .fragment_filter_notification_update, null)
//        var filterRecyclerView = filterView?.findViewById<RecyclerView>(R.id.section_filter_list)
//        var reset = filterView?.findViewById<View>(R.id.reset)
//        var submit = filterView?.findViewById<TextViewCompat>(R.id.submit)
//        var closeFilter = filterView?.findViewById<View>(R.id.close_cross)
//
//        setActionViewProperties(submit, reset, false)
//
//        closeFilter?.setOnClickListener {
//            bottomSheetDialog.dismiss()
//        }
//
//        filterRecyclerView?.let {
//            for (i in 0 until it.itemDecorationCount) {
//                filterRecyclerView.removeItemDecorationAt(i)
//            }
//            val staticDimen24dp = it.context.resources.getDimensionPixelOffset(R.dimen.dp_24)
//            it.addItemDecoration(SpacingItemDecoration(staticDimen24dp, filterViewModel.size))
//            it.layoutManager = LinearLayoutManager(it.context)
//            val filterAdapter = NotificationUpdateFilterAdapter(NotificationUpdateFilterTypeFactoryImpl(this))
//            it.adapter = filterAdapter
//            filterAdapter.addElement(filterViewModel)
//        }
//        reset?.setOnClickListener { resetView ->
//            (filterRecyclerView?.adapter as NotificationUpdateFilterAdapter)?.let {
//                for ((key, value) in selectedItemList) {
//                    filterViewModel[key].list[value].selected = false
//                    (filterRecyclerView.adapter as NotificationUpdateFilterAdapter).notifyItemChanged(
//                            key,
//                            value
//                    )
//                }
//                selectedItemList.clear()
//                setActionViewProperties(
//                        bottomSheetDialog?.findViewById(R.id.submit),
//                        bottomSheetDialog?.findViewById(R.id.reset),
//                        selectedItemList.size > 0
//                )
//                presenter.resetFilter()
//            }
//        }
//
//        submit?.setOnClickListener {
//            cursor = ""
//            presenter.filterBy(selectedItemList, filterViewModel)
//            analytics.trackClickFilterRequest(getLabelFilterName())
//            loadInitialData()
//            bottomSheetDialog.dismiss()
//        }
        return filterView
    }


    private fun getLabelFilterName(): String {
        var typeName = ""
        var tagName = ""
        for ((key, value) in selectedItemList) {
            if (filterViewModel[key].filterType == NotificationUpdateFilterItemViewModel.FilterType.TYPE_ID.type) {
                typeName = filterViewModel[key].list[value].text
            } else if (filterViewModel[key].filterType == NotificationUpdateFilterItemViewModel.FilterType.TAG_ID.type) {
                tagName = filterViewModel[key].list[value].text
            }
        }
        var labelFormat = String.format("%s - %s",
                typeName,
                tagName)
        return labelFormat
    }

    private fun notifyBottomActionView() {
        bottomActionView?.let {
            if (markAllReadCounter == 0L) {
                it.hideBav2()
            } else {
                it.showBav2()
            }
        }
    }

    override fun sectionPicked(sectionIndex: Int, sectionItemIndex: Int) {
        selectedItemList[sectionIndex] = sectionItemIndex
        if (selectedItemList[sectionIndex] == -1) {
            selectedItemList.remove(sectionIndex)
        }

        setActionViewProperties(
                bottomSheetDialog?.findViewById(R.id.submit),
                bottomSheetDialog?.findViewById(R.id.reset),
                selectedItemList.size > 0
        )
    }

    private fun setActionViewProperties(
            button: TextViewCompat?,
            reset: View?,
            hasSelectedItem: Boolean
    ) {
        reset?.let {
            if (hasSelectedItem) {
                it.visibility = View.VISIBLE
            } else {
                it.visibility = View.GONE
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
            if(activity != null) {
                SnackbarManager.make(activity, ErrorHandler.getErrorMessage(activity, it), Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun onSuccessInitiateData(): (NotificationUpdateViewModel) -> Unit {
        return {
            var canLoadMore = it.paging.hasNext
            if (canLoadMore && !it.list.isEmpty()) {
                cursor = (it.list.last().notificationId)
            }
            if (swipeToRefresh.isRefreshing) {
                notificationUpdateListener?.onSuccessLoadNotifUpdate()
            }
            renderList(it.list, canLoadMore)
        }
    }

    private fun onSuccessGetFilter(): (ArrayList<NotificationUpdateFilterItemViewModel>) -> Unit {
        return {
            filterViewModel = it
            var bottomSheetDialog = CloseableBottomSheetDialog.createInstanceRounded(activity)
            bottomSheetDialog.setCustomContentView(createFilterView(), "", false)
            this.bottomSheetDialog = bottomSheetDialog

            filterAdapter.updateData(it)
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
        adapter.notifyItemChanged(adapterPosition)
        analytics.trackClickNotifList(viewModel.templateKey)
        presenter.markReadNotif(viewModel.notificationId)
        val needToResetCounter = !viewModel.isRead
        if (needToResetCounter) {
            updateMarkAllReadCounter()
            notifyBottomActionView()
        }
        showTextLonger(viewModel)
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

    class SpacingItemDecoration(private val dimen: Int, val size: Int) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = this.dimen
                outRect.bottom = this.dimen / 2
            } else if (parent.getChildAdapterPosition(view) == this.size - 1) {
                outRect.top = this.dimen / 2
                outRect.bottom = this.dimen
            } else {
                outRect.top = this.dimen / 2
                outRect.bottom = this.dimen / 2
            }
        }
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
        if(lastItem > 0) analytics.trackScrollBottom(lastItem.toString())
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

    private fun showTextLonger(model: NotificationUpdateItemViewModel) {

        val bundle = Bundle()
        bundle.putString(PARAM_CONTENT_IMAGE, model.contentUrl)
        bundle.putString(PARAM_CONTENT_IMAGE_TYPE, model.typeLink.toString())
        bundle.putString(PARAM_CTA_APPLINK, model.appLink)
        bundle.putString(PARAM_CONTENT_TEXT, model.body)
        bundle.putString(PARAM_CONTENT_TITLE, model.title)


        if (!::longerTextDialog.isInitialized) {
            longerTextDialog = NotificationUpdateLongerTextFragment.createInstance(bundle)
        } else {
            longerTextDialog.arguments = bundle
        }

        if (!longerTextDialog.isAdded)
            longerTextDialog.show(activity?.supportFragmentManager, "Longer Text Bottom Sheet")
    }

    companion object {
        val PARAM_CONTENT_TITLE = "content title"
        val PARAM_CONTENT_TEXT = "content text"
        val PARAM_CONTENT_IMAGE = "content image"
        val PARAM_CONTENT_IMAGE_TYPE = "content image type"
        val PARAM_CTA_TEXT = "cta text"
        val PARAM_CTA_APPLINK = "cta applink"
    }
}