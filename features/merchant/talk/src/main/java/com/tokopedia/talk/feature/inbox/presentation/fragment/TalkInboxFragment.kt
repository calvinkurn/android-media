package com.tokopedia.talk.feature.inbox.presentation.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.TalkInstance
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.loadImage
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
import kotlinx.android.synthetic.main.fragment_talk_inbox.*
import kotlinx.android.synthetic.main.partial_talk_inbox_empty.*
import javax.inject.Inject

class TalkInboxFragment : BaseListFragment<TalkInboxUiModel, TalkInboxAdapterTypeFactory>(),
        HasComponent<TalkInboxComponent>, TalkPerformanceMonitoringContract {

    companion object {
        const val TAB_PARAM = "tab_param"
        const val EMPTY_DISCUSSION_IMAGE = "https://ecs7.tokopedia.net/android/talk_inbox_empty.png"

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
        // Add viewtree observer stuff
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
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeInboxList()
    }

    private fun goToReply(questionId: String, shopId: String) {
        RouteManager.route(context, Uri.parse(UriUtil.buildUri(ApplinkConstInternalGlobal.TALK_REPLY, questionId))
                .buildUpon()
                .appendQueryParameter(TalkConstants.PARAM_SHOP_ID, shopId)
                .appendQueryParameter(TalkConstants.PARAM_SOURCE, TalkDetailsActivity.SOURCE_INBOX)
                .build().toString()
        )
    }

    private fun observeInboxList() {
        viewModel.inboxList.observe(viewLifecycleOwner, Observer {
            when(it) {
                is TalkInboxViewState.Success -> {
                    if(it.page == 1 && it.data.isEmpty()) {

                    }
                    renderList(it.data, it.hasNext)
                }
                is TalkInboxViewState.Fail -> {

                }
            }
        })
    }

    private fun initEmptyState() {
        talkInboxEmptyImage.loadImage(EMPTY_DISCUSSION_IMAGE)
    }

    private fun showEmptyInbox() {
        talkInboxEmptyTitle.text = getString(R.string.inbox_all_empty)
    }

    private fun showEmptyUnread() {
        talkInboxEmptyTitle.text = getString(R.string.inbox_empty_title)
        talkInboxEmptySubtitle.text = getString(R.string.inbox_empty_unread_discussion)
    }

    private fun showEmptyRead() {
        talkInboxEmptyTitle.text = getString(R.string.inbox_empty_title)
        talkInboxEmptySubtitle.text = getString(R.string.inbox_empty_read_discussion)
    }

    private fun getDataFromArgument() {
        arguments?.getString(TAB_PARAM)?.let { viewModel.setInboxType(it) }
    }

    private fun initSortFilter() {
        talkInboxSortFilter.apply {
            sortFilterItems.removeAllViews()
            addItem(getFilterList())
        }
    }

    private fun getFilterList(): ArrayList<SortFilterItem> {
        return arrayListOf(
                SortFilterItem(getString(R.string.inbox_read)) { selectFilter(TalkInboxFilter.TalkInboxReadFilter()) },
                SortFilterItem(getString(R.string.inbox_unread)) { selectFilter(TalkInboxFilter.TalkInboxUnreadFilter()) }
        )
    }

    private fun selectFilter(filter: TalkInboxFilter) {
        viewModel.setFilter(filter.filterParam)
        showLoading()
        clearAllData()
    }

}