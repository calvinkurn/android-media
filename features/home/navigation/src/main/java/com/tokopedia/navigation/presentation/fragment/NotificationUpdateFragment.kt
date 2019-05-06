package com.tokopedia.navigation.presentation.fragment

import android.graphics.Rect
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.design.button.BottomActionView
import com.tokopedia.design.component.TextViewCompat
import com.tokopedia.navigation.R
import com.tokopedia.navigation.analytics.NotificationUpdateAnalytics
import com.tokopedia.navigation.presentation.adapter.NotificationUpdateAdapter
import com.tokopedia.navigation.presentation.adapter.NotificationUpdateFilterAdapter
import com.tokopedia.navigation.presentation.adapter.typefactory.NotificationUpdateFilterTypeFactoryImpl
import com.tokopedia.navigation.presentation.adapter.typefactory.NotificationUpdateTypeFactoryImpl
import com.tokopedia.navigation.presentation.di.notification.DaggerNotificationUpdateComponent
import com.tokopedia.navigation.presentation.presenter.NotificationUpdatePresenter
import com.tokopedia.navigation.presentation.view.listener.NotificationSectionFilterListener
import com.tokopedia.navigation.presentation.view.listener.NotificationUpdateContract
import com.tokopedia.navigation.presentation.view.listener.NotificationUpdateItemListener
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateFilterItemViewModel
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateItemViewModel
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateViewModel
import javax.inject.Inject

/**
 * @author : Steven 10/04/19
 */
class NotificationUpdateFragment : BaseListFragment<Visitable<*>, BaseAdapterTypeFactory>()
        , NotificationUpdateContract.View, NotificationSectionFilterListener, NotificationUpdateItemListener {

    private var cursor = ""

    private lateinit var filterViewModel: ArrayList<NotificationUpdateFilterItemViewModel>
    private val selectedItemList = HashMap<Int, Int>()
    private lateinit var bottomSheetDialog: BottomSheetDialog

    override fun getAdapterTypeFactory(): BaseAdapterTypeFactory {
        return NotificationUpdateTypeFactoryImpl(this)
    }

    @Inject
    lateinit var presenter: NotificationUpdatePresenter

    @Inject
    lateinit var analytics: NotificationUpdateAnalytics


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notification_update, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.getFilter(onSuccessGetFilter())

        val bottomActionView: BottomActionView = view.findViewById(R.id.filterBtn)
        val recyclerView = super.getRecyclerView(view)

        bottomActionView.setButton1OnClickListener {
            if (::bottomSheetDialog.isInitialized) {
                bottomSheetDialog.show()
            }
        }

        bottomActionView.setButton2OnClickListener {
            analytics.trackMarkAllAsRead()
            presenter.markAllReadNotificationUpdate(onSuccessMarkAllReadNotificationUpdate())
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy < 0) { // going up
                    if (adapter.dataSize > 0) {
                        bottomActionView.show()
                    }
                } else if (dy > 0) { // going down
                    bottomActionView.hide()
                }
            }
        })
    }

    private fun onSuccessMarkAllReadNotificationUpdate(): () -> Unit {
        return {
            (adapter as NotificationUpdateAdapter).markAllAsRead()
        }
    }

    private fun createFilterView(): View? {
        val filterView = activity?.layoutInflater?.inflate(R.layout
                .fragment_filter_notification_update, null)
        var filterRecyclerView = filterView?.findViewById<RecyclerView>(R.id.section_filter_list)
        var reset = filterView?.findViewById<View>(R.id.reset)
        var submit = filterView?.findViewById<TextViewCompat>(R.id.submit)

        setActionViewProperties(submit, reset, false)

        filterRecyclerView?.let {
            for (i in 0 until it.itemDecorationCount) {
                filterRecyclerView.removeItemDecorationAt(i)
            }
            val staticDimen24dp = it.context.resources.getDimensionPixelOffset(R.dimen.dp_24)
            it.addItemDecoration(SpacingItemDecoration(staticDimen24dp, filterViewModel.size))
            it.layoutManager = LinearLayoutManager(it.context)
            val filterAdapter = NotificationUpdateFilterAdapter(NotificationUpdateFilterTypeFactoryImpl(this))
            it.adapter = filterAdapter
            filterAdapter.addElement(filterViewModel)
        }
        reset?.setOnClickListener { resetView ->
            (filterRecyclerView?.adapter as NotificationUpdateFilterAdapter)?.let {
                for ((key, value) in selectedItemList) {
                    filterViewModel[key].list[value].selected = false
                    (filterRecyclerView.adapter as NotificationUpdateFilterAdapter).notifyItemChanged(
                            key,
                            value
                    )
                }
                selectedItemList.clear()
                setActionViewProperties(
                        bottomSheetDialog?.findViewById(R.id.submit),
                        bottomSheetDialog?.findViewById(R.id.reset),
                        selectedItemList.size > 0
                )
                presenter.resetFilter()
            }
        }

        submit?.setOnClickListener {
            cursor = ""
            presenter.filterBy(selectedItemList, filterViewModel)
            analytics.trackClickFilterRequest(getLabelFilterName())
            loadInitialData()
            bottomSheetDialog.dismiss()
        }
        return filterView
    }

    private fun getLabelFilterName(): String {
        var typeName = ""
        var tagName = ""
        for ((key, value) in selectedItemList) {
            if(filterViewModel[key].filterType == NotificationUpdateFilterItemViewModel.FilterType.TYPE_ID.type){
                typeName = filterViewModel[key].list[value].text
            }else if(filterViewModel[key].filterType == NotificationUpdateFilterItemViewModel.FilterType.TAG_ID.type){
                tagName = filterViewModel[key].list[value].text
            }
        }
        var labelFormat = String.format("%s - %s",
                typeName,
                tagName)
        return labelFormat
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
        if (datum is NotificationUpdateItemViewModel) {
            analytics.trackClickNotifList(datum.templateKey)
            datum.isRead
        }
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

        }
    }

    private fun onSuccessInitiateData(): (NotificationUpdateViewModel) -> Unit {
        return {
            var canLoadMore = it.paging.hasNext
            if (canLoadMore && !it.list.isEmpty()) {
                cursor = (it.list.last().notificationId)
            }
            renderList(it.list, canLoadMore)
        }
    }

    private fun onSuccessFilter(): (NotificationUpdateViewModel) -> Unit {
        return {
            var canLoadMore = it.paging.hasNext
            if (canLoadMore && !it.list.isEmpty()) {
                cursor = (it.list.last().notificationId)
            }
            isLoadingInitialData = true
            renderList(it.list, canLoadMore)
        }
    }

    private fun onSuccessGetFilter(): (ArrayList<NotificationUpdateFilterItemViewModel>) -> Unit {
        return {
            filterViewModel = it
            var bottomSheetDialog = CloseableBottomSheetDialog.createInstanceRounded(activity)
            bottomSheetDialog.setCustomContentView(createFilterView(), "", false)
            this.bottomSheetDialog = bottomSheetDialog
        }
    }


    override fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(getRecyclerView(view).layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                showLoading()
                loadData(page)
                analytics.trackScrollBottom()
            }
        }
    }

    override fun itemClicked(notifId: String, adapterPosition: Int) {
        adapter.notifyItemChanged(adapterPosition)
        presenter.markReadNotif(notifId)
    }


    override fun resetCounter() {
        presenter.clearNotifCounter()
    }


    override fun onSwipeRefresh() {
        cursor = ""
        super.onSwipeRefresh()
    }

    override fun getSwipeRefreshLayout(view: View?): SwipeRefreshLayout? {
        return view?.findViewById(R.id.swipeToRefresh)
    }

    class SpacingItemDecoration(private val dimen: Int, val size: Int) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
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

}