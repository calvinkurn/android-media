package com.tokopedia.contactus.inboxticket2.view.presenter

import android.app.Activity.RESULT_CANCELED
import android.content.Intent
import android.text.Spanned
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.contactus.R
import com.tokopedia.contactus.common.analytics.ContactUsTracking
import com.tokopedia.contactus.common.analytics.InboxTicketTracking
import com.tokopedia.contactus.home.view.ContactUsHomeActivity
import com.tokopedia.contactus.inboxticket2.data.model.ChipTopBotStatusResponse
import com.tokopedia.contactus.inboxticket2.data.model.InboxTicketListResponse
import com.tokopedia.contactus.inboxticket2.domain.usecase.ChipTopBotStatusUseCase
import com.tokopedia.contactus.inboxticket2.domain.usecase.GetTicketListUseCase
import com.tokopedia.contactus.inboxticket2.view.activity.InboxDetailActivity
import com.tokopedia.contactus.inboxticket2.view.adapter.InboxFilterAdapter
import com.tokopedia.contactus.inboxticket2.view.contract.InboxBaseContract.InboxBaseView
import com.tokopedia.contactus.inboxticket2.view.contract.InboxListContract
import com.tokopedia.contactus.inboxticket2.view.contract.InboxListContract.InboxListView
import com.tokopedia.contactus.inboxticket2.view.customview.CustomEditText
import com.tokopedia.contactus.inboxticket2.view.fragment.InboxBottomSheetFragment
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.htmltags.HtmlUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

const val ALL = 0
const val NEED_RATING = 2
const val IN_PROGRESS = 1
const val CLOSED = 3
const val FILTER_NEED_RATING = 1
const val FILTER_CLOSED = 2
const val FIRST_PAGE = 1

class InboxListPresenter(private val mUseCase: GetTicketListUseCase,
                         private val topBotStatusUseCase: ChipTopBotStatusUseCase,
                         private val userSession: UserSessionInterface) : InboxListContract.Presenter, CustomEditText.Listener, CoroutineScope {
    private var status: Int = 0
    private var rating: Int = 0
    private var mView: InboxListView? = null
    private val filterList: ArrayList<String> by lazy { ArrayList<String>() }
    private val originalList: ArrayList<InboxTicketListResponse.Ticket.Data.TicketItem> by lazy { ArrayList<InboxTicketListResponse.Ticket.Data.TicketItem>() }
    var isLoading = false
    var isLastPage = false
    private var selectedFilter = 0
    private var page = 1
    var fromFilter: Boolean = false
    private var nextUrl: String? = null
    private var topBotStatusResponse: ChipTopBotStatusResponse? = null

    override fun attachView(view: InboxBaseView?) {
        mView = view as InboxListView
        filterList.addAll(Arrays.asList(*mView?.getActivity()?.resources?.getStringArray(R.array.contact_us_filter_array)))
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    override fun getTicketList(requestParams: RequestParams?) {
        mView?.showProgressBar()
        if (requestParams == null) selectedFilter = 0
        launchCatchError(
                block = {
                    val ticketListResponse = mUseCase.getTicketListResponse(requestParams
                            ?: mUseCase.getRequestParams(FIRST_PAGE, ALL))
                    val ticketData = ticketListResponse.ticket?.TicketData
                    when {
                        !ticketData?.ticketItems.isNullOrEmpty() -> {
                            mView?.toggleEmptyLayout(View.GONE)
                            originalList.clear()
                            ticketData?.ticketItems?.let { originalList.addAll(it) }
                            nextUrl = ticketData?.nextPage
                            isLastPage = nextUrl?.isEmpty() == true
                            mView?.renderTicketList(originalList)
                            mView?.showFilter()
                        }
                        fromFilter -> {
                            originalList.clear()
                            mView?.toggleEmptyLayout(View.VISIBLE)
                            mView?.showFilter()
                            fromFilter = false
                        }
                        else -> {
                            mView?.toggleNoTicketLayout(View.VISIBLE, userSession.name)
                            mView?.hideFilter()
                        }
                    }

                    mView?.hideProgressBar()

                },
                onError = {
                    it.printStackTrace()
                }
        )
    }

    override fun getTopBotStatus() {
        launchCatchError(
                block = {
                    topBotStatusResponse = topBotStatusUseCase.getChipTopBotStatus()
                    if (topBotStatusResponse?.chipTopBotStatusInbox?.chipTopBotStatusData?.isSuccess == 1
                            && topBotStatusResponse?.chipTopBotStatusInbox?.chipTopBotStatusData?.isActive == true) {
                        mView?.showChatBotWidget()
                    } else {
                        mView?.hideChatBotWidget()
                    }
                },
                onError = {
                    mView?.hideChatBotWidget()
                    it.printStackTrace()
                }
        )
    }

    override fun getChatbotApplink(): String {
        val applinkPrefix = mView?.getActivity()?.getString(R.string.contactus_chat_bot_applink)
                ?: ""
        return String.format(applinkPrefix, topBotStatusResponse?.chipTopBotStatusInbox?.chipTopBotStatusData?.messageId)
    }

    override fun getWelcomeMessage(): CharSequence {
        return MethodChecker.fromHtmlWithoutExtraSpace(topBotStatusResponse?.chipTopBotStatusInbox?.chipTopBotStatusData?.welcomeMessage
                ?: "")
    }

    override fun getNotifiactionIndiactor(): Boolean {
        return topBotStatusResponse?.chipTopBotStatusInbox?.chipTopBotStatusData?.unreadNotif
                ?: false
    }

    override fun detachView() {}
    override fun onClickFilter() {
        mView?.showBottomFragment()
    }

    override fun setFilter(position: Int) {
        var selected = ""
        fromFilter = true
        when (position) {
            ALL -> {
                val requestParams = mUseCase.getRequestParams(FIRST_PAGE, ALL)
                getTicketList(requestParams)
                selected = getFilterList(filterList, ALL)
                selectedFilter = ALL
                status = ALL
                rating = ALL
                page = FIRST_PAGE
            }
            IN_PROGRESS -> {
                val requestParams = mUseCase.getRequestParams(FIRST_PAGE, IN_PROGRESS)
                selected = getFilterList(filterList, IN_PROGRESS)
                getTicketList(requestParams)
                selectedFilter = IN_PROGRESS
                status = IN_PROGRESS
                rating = ALL
                page = FIRST_PAGE
            }
            NEED_RATING -> {
                val requestParams = mUseCase.getRequestParams(FIRST_PAGE, NEED_RATING, FILTER_NEED_RATING)
                selected = getFilterList(filterList, NEED_RATING)
                getTicketList(requestParams)
                selectedFilter = NEED_RATING
                status = NEED_RATING
                rating = FILTER_NEED_RATING
                page = FIRST_PAGE
            }
            CLOSED -> {
                val requestParams = mUseCase.getRequestParams(FIRST_PAGE, NEED_RATING, FILTER_CLOSED)
                selected = getFilterList(filterList, CLOSED)
                getTicketList(requestParams)
                selectedFilter = CLOSED
                status = NEED_RATING
                rating = FILTER_CLOSED
                page = FIRST_PAGE
            }
        }
        ContactUsTracking.sendGTMInboxTicket(mView?.getActivity(), "",
                InboxTicketTracking.Category.EventInboxTicket,
                InboxTicketTracking.Action.EventClickFilter,
                selected)
        mView?.hideBottomFragment()
    }

    private fun getFilterList(filterList: ArrayList<String>, listType: Int): String {
        return if (filterList.isNotEmpty()) {
            filterList[listType]
        } else {
            ""
        }
    }

    override fun onClickTicket(index: Int, isOfficialStore: Boolean) {
        val detailIntent =
                InboxDetailActivity.getIntent(mView?.getActivity(), originalList[index].id, isOfficialStore)
        mView?.navigateToActivityRequest(detailIntent, InboxBaseView.REQUEST_DETAILS)
        ContactUsTracking.sendGTMInboxTicket(mView?.getActivity(), "",
                InboxTicketTracking.Category.EventInboxTicket,
                InboxTicketTracking.Action.EventTicketClick,
                originalList[index].status)
    }

    override fun scrollList() {
        mView?.scrollRv()
    }

    override fun onRecyclerViewScrolled(layoutManager: LinearLayoutManager) {
        checkIfToLoad(layoutManager)
    }

    override fun getSearchListener(): CustomEditText.Listener {
        return this
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != RESULT_CANCELED && requestCode == InboxBaseView.REQUEST_DETAILS) {
            if (resultCode == InboxBaseView.RESULT_FINISH) {
                mView?.navigateToActivityRequest(Intent(mView?.getActivity(), ContactUsHomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP), 100)
                mView?.getActivity()?.finish()
            }
        }
    }

    override fun onDestroy() {}

    private fun getFilterAdapter(selected: Int): InboxFilterAdapter {
        val adapter: InboxFilterAdapter by lazy { InboxFilterAdapter(filterList, selected, this) }
        return adapter
    }

    override fun getBottomFragment(resID: Int): BottomSheetDialogFragment? {
        val bottomFragment = InboxBottomSheetFragment.getBottomSheetFragment(resID)
        bottomFragment?.setAdapter(getFilterAdapter(selectedFilter))
        return bottomFragment
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_search) {
            mView?.toggleSearch(View.VISIBLE)
            return true
        }
        return false
    }

    override fun reAttachView() {
        val requestParams = mUseCase.getRequestParams(FIRST_PAGE, status, rating)
        getTicketList(requestParams)
        getTopBotStatus()
    }

    override fun clickCloseSearch() {
        if (mView?.isSearchMode() == true) {
            mView?.toggleSearch(View.GONE)
        } else {
            mView?.clearSearch()
        }
    }

    override fun refreshLayout() {}
    fun checkIfToLoad(layoutManager: LinearLayoutManager) {
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        if (!isLoading && !isLastPage) {
            val PAGE_SIZE = 10
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= PAGE_SIZE) {
                page++
                loadMoreItems(page)
            }
        }
    }

    fun loadMoreItems(page: Int) {
        isLoading = true
        mView?.addFooter()
        launchCatchError(
                block = {
                    val requestParams = mUseCase.getRequestParams(page, status, rating)
                    val ticketListResponse = mUseCase.getTicketListResponse(requestParams)
                    val ticketData = ticketListResponse.ticket?.TicketData
                    if (ticketData?.ticketItems?.isNullOrEmpty() == false) {
                        mView?.removeFooter()
                        originalList.addAll(ticketData.ticketItems)
                        nextUrl = ticketData.nextPage
                        isLastPage = nextUrl?.isEmpty() == true
                        mView?.updateDataSet()
                    }
                    isLoading = false
                },
                onError = { isLoading = false }
        )
    }

    override fun onSearchSubmitted(text: String) {}
    override fun onSearchTextChanged(text: String) {
        if (text.isNotEmpty()) {
            mView?.toggleSearch(View.VISIBLE)
        }
    }

}