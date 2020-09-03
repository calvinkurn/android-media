package com.tokopedia.talk.feature.inbox.presentation.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.TalkInstance
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.talk.common.analytics.TalkPerformanceMonitoringContract
import com.tokopedia.talk.common.analytics.TalkPerformanceMonitoringListener
import com.tokopedia.talk.common.constants.TalkConstants
import com.tokopedia.talk.feature.inbox.data.TalkInboxFilter
import com.tokopedia.talk.feature.inbox.data.TalkInboxTab
import com.tokopedia.talk.feature.inbox.di.DaggerTalkInboxComponent
import com.tokopedia.talk.feature.inbox.di.TalkInboxComponent
import com.tokopedia.talk.feature.inbox.presentation.adapter.TalkInboxAdapterTypeFactory
import com.tokopedia.talk.feature.inbox.presentation.adapter.uimodel.TalkInboxUiModel
import com.tokopedia.talk.feature.inbox.data.TalkInboxViewState
import com.tokopedia.talk.feature.inbox.presentation.viewmodel.TalkInboxViewModel
import com.tokopedia.talk_old.R
import com.tokopedia.talk_old.talkdetails.view.activity.TalkDetailsActivity
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_talk_inbox.*
import kotlinx.android.synthetic.main.partial_talk_connection_error.view.*
import kotlinx.android.synthetic.main.partial_talk_inbox_empty.*
import javax.inject.Inject

class TalkInboxFragment : BaseListFragment<TalkInboxUiModel, TalkInboxAdapterTypeFactory>(),
        HasComponent<TalkInboxComponent>, TalkPerformanceMonitoringContract {

    companion object {
        const val TAB_PARAM = "tab_param"
        const val EMPTY_DISCUSSION_IMAGE = "https://ecs7.tokopedia.net/android/talk_inbox_empty.png"
        const val REPLY_REQUEST_CODE = 420

        fun createNewInstance(tab: TalkInboxTab): TalkInboxFragment {
            return TalkInboxFragment().apply {
                arguments = Bundle().apply {
                    putString(TAB_PARAM, tab.tabParam)
                }
            }
        }
    }

    @Inject
    lateinit var viewModel: TalkInboxViewModel

    private var talkPerformanceMonitoringListener: TalkPerformanceMonitoringListener? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REPLY_REQUEST_CODE) {
            loadInitialData()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun getAdapterTypeFactory(): TalkInboxAdapterTypeFactory {
        return TalkInboxAdapterTypeFactory()
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component?.inject(this)
    }

    override fun onItemClicked(talkUiModel: TalkInboxUiModel?) {
        talkUiModel?.let {
            goToReply(it.inboxDetail.questionID, viewModel.getShopId())
        }
    }

    override fun loadData(page: Int) {
        viewModel.updatePage(page)
    }

    override fun getComponent(): TalkInboxComponent? {
        return activity?.run {
            DaggerTalkInboxComponent
                    .builder()
                    .talkComponent(TalkInstance.getComponent(application))
                    .build()
        }
    }

    override fun stopPreparePerfomancePageMonitoring() {
        talkPerformanceMonitoringListener?.stopPreparePagePerformanceMonitoring()
    }

    override fun startNetworkRequestPerformanceMonitoring() {
        talkPerformanceMonitoringListener?.startNetworkRequestPerformanceMonitoring()
    }

    override fun stopNetworkRequestPerformanceMonitoring() {
        talkPerformanceMonitoringListener?.stopNetworkRequestPerformanceMonitoring()
    }

    override fun startRenderPerformanceMonitoring() {
        talkPerformanceMonitoringListener?.startRenderPerformanceMonitoring()
        talkInboxRecyclerView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                talkPerformanceMonitoringListener?.stopRenderPerformanceMonitoring()
                talkPerformanceMonitoringListener?.stopPerformanceMonitoring()
                talkInboxRecyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    override fun castContextToTalkPerformanceMonitoringListener(context: Context): TalkPerformanceMonitoringListener? {
        return if(context is TalkPerformanceMonitoringListener) {
            context
        } else {
            null
        }
    }

    override fun getRecyclerView(view: View?): RecyclerView {
        return talkInboxRecyclerView
    }

    override fun getSwipeRefreshLayout(view: View?): SwipeRefreshLayout? {
        return talkInboxSwipeToRefresh
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        talkPerformanceMonitoringListener = castContextToTalkPerformanceMonitoringListener(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDataFromArgument()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_talk_inbox, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSortFilter()
        initEmptyState()
        initErrorPage()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeInboxList()
    }

    private fun goToReply(questionId: String, shopId: String) {
        val intent = RouteManager.getIntent(context, Uri.parse(UriUtil.buildUri(ApplinkConstInternalGlobal.TALK_REPLY, questionId))
                .buildUpon()
                .appendQueryParameter(TalkConstants.PARAM_SHOP_ID, shopId)
                .appendQueryParameter(TalkConstants.PARAM_SOURCE, TalkDetailsActivity.SOURCE_INBOX)
                .build().toString()
        )
        startActivityForResult(intent, REPLY_REQUEST_CODE)
    }

    private fun observeInboxList() {
        viewModel.inboxList.observe(viewLifecycleOwner, Observer {
            when(it) {
                is TalkInboxViewState.Success -> {
                    hideFullPageError()
                    hideFullPageLoading()
                    if(it.page == TalkConstants.DEFAULT_INITIAL_PAGE && it.data.isEmpty()) {
                        hideLoading()
                        when(it.filter) {
                            is TalkInboxFilter.TalkInboxNoFilter -> {
                                showEmptyInbox()
                            }
                            is TalkInboxFilter.TalkInboxUnreadFilter -> {
                                showEmptyUnread()
                            }
                            is TalkInboxFilter.TalkInboxReadFilter -> {
                                showEmptyRead()
                            }
                        }
                    } else {
                        renderData(it.data, it.hasNext)
                    }
                }
                is TalkInboxViewState.Fail -> {
                    hideFullPageLoading()
                    if(it.page == TalkConstants.DEFAULT_INITIAL_PAGE) {
                        showFullPageError()
                    } else {
                        showErrorToaster()
                    }
                }
                is TalkInboxViewState.Loading -> {
                    if(it.page == TalkConstants.DEFAULT_INITIAL_PAGE) {
                        showFullPageLoading()
                    }
                }
            }
        })
    }

    private fun renderData(data: List<TalkInboxUiModel>, hasNext: Boolean) {
        hideEmpty()
        talkInboxRecyclerView.show()
        renderList(data, hasNext)
    }

    private fun initErrorPage() {
        inboxPageError.talkConnectionErrorRetryButton.setOnClickListener {
            loadInitialData()
        }
    }

    private fun showFullPageError() {
        inboxPageError.show()
    }

    private fun hideFullPageError() {
        inboxPageError.hide()
    }

    private fun showErrorToaster() {
        view?.let {
            Toaster.build(talkInboxContainer, getString(R.string.inbox_toaster_connection_error), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, getString(R.string.talk_retry), View.OnClickListener { loadData(currentPage) }). show() }
    }

    private fun initEmptyState() {
        talkInboxEmptyImage.loadImage(EMPTY_DISCUSSION_IMAGE)
    }

    private fun showEmptyInbox() {
        talkInboxEmptyTitle.text = getString(R.string.inbox_all_empty)
        talkInboxSortFilter.hide()
        talkInboxEmpty.show()
    }

    private fun showEmptyUnread() {
        talkInboxEmptyTitle.text = getString(R.string.inbox_empty_title)
        talkInboxEmptySubtitle.text = getString(R.string.inbox_empty_unread_discussion)
        talkInboxEmpty.show()
        talkInboxRecyclerView.hide()
    }

    private fun showEmptyRead() {
        talkInboxEmptyTitle.text = getString(R.string.inbox_empty_title)
        talkInboxEmptySubtitle.text = getString(R.string.inbox_empty_read_discussion)
        talkInboxEmpty.show()
        talkInboxRecyclerView.hide()
    }

    private fun hideEmpty() {
        talkInboxEmpty.hide()
    }

    private fun showFullPageLoading() {
        inboxPageLoading.show()
    }

    private fun hideFullPageLoading() {
        inboxPageLoading.hide()
    }

    private fun getDataFromArgument() {
        arguments?.getString(TAB_PARAM)?.let { viewModel.setInboxType(it) }
    }

    private fun initSortFilter() {
        talkInboxSortFilter.apply {
            sortFilterItems.removeAllViews()
            sortFilterPrefix.removeAllViews()
            addItem(getFilterList())
        }
    }

    private fun getFilterList(): ArrayList<SortFilterItem> {
        val readFilter = SortFilterItem(getString(R.string.inbox_read))
        val unreadFilter = SortFilterItem(getString(R.string.inbox_unread))
        readFilter.listener = {
            readFilter.toggle()
            selectFilter(TalkInboxFilter.TalkInboxReadFilter())
            if(readFilter.type == ChipsUnify.TYPE_SELECTED) {
                unreadFilter.type = ChipsUnify.TYPE_NORMAL
            }
        }
        unreadFilter.listener = {
            unreadFilter.toggle()
            selectFilter(TalkInboxFilter.TalkInboxUnreadFilter())
            if(unreadFilter.type == ChipsUnify.TYPE_SELECTED) {
                readFilter.type = ChipsUnify.TYPE_NORMAL
            }
        }
        return arrayListOf(readFilter, unreadFilter)
    }

    private fun selectFilter(filter: TalkInboxFilter) {
        viewModel.setFilter(filter)
        showLoading()
        clearAllData()
    }

    private fun SortFilterItem.toggle() {
        type = if(type == ChipsUnify.TYPE_NORMAL) {
            ChipsUnify.TYPE_SELECTED
        } else {
            ChipsUnify.TYPE_NORMAL
        }
    }

}