package com.tokopedia.contactus.inboxticket2.view.presenter

import android.app.Activity.RESULT_CANCELED
import android.content.Intent
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.contactus.R
import com.tokopedia.contactus.common.analytics.ContactUsTracking
import com.tokopedia.contactus.common.analytics.InboxTicketTracking
import com.tokopedia.contactus.home.view.ContactUsHomeActivity
import com.tokopedia.contactus.inboxticket2.data.model.InboxTicketListResponse2
import com.tokopedia.contactus.inboxticket2.domain.usecase.GetTicketListUseCase
import com.tokopedia.contactus.inboxticket2.view.activity.InboxDetailActivity
import com.tokopedia.contactus.inboxticket2.view.adapter.InboxFilterAdapter
import com.tokopedia.contactus.inboxticket2.view.contract.InboxBaseContract.InboxBaseView
import com.tokopedia.contactus.inboxticket2.view.contract.InboxListContract.InboxListPresenter
import com.tokopedia.contactus.inboxticket2.view.contract.InboxListContract.InboxListView
import com.tokopedia.contactus.inboxticket2.view.customview.CustomEditText
import com.tokopedia.contactus.inboxticket2.view.fragment.InboxBottomSheetFragment
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

const val ALL = 0
const val UNREAD = 1
const val NEEDRATING = 2
const val INPROGRESS = 3
const val READ = 4
const val CLOSED = 5

class InboxListPresenterImpl(private val mUseCase: GetTicketListUseCase) : InboxListPresenter, CustomEditText.Listener, CoroutineScope {
    private val status: Int = 0
    private var mView: InboxListView? = null
    private val filterList: ArrayList<String> by lazy { ArrayList<String>() }
    private val originalList: ArrayList<InboxTicketListResponse2.Ticket.Data.TicketItem> by lazy { ArrayList<InboxTicketListResponse2.Ticket.Data.TicketItem>() }
    var isLoading = false
    var isLastPage = false
    private var page = 1
    var fromFilter: Boolean = false
    private var nextUrl: String? = null

    override fun attachView(view: InboxBaseView?) {
        mView = view as InboxListView
        filterList.addAll(Arrays.asList(*mView?.getActivity()?.resources?.getStringArray(R.array.filterarray)))
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    override fun getTicketList(requestParams: RequestParams?) {
            mView?.showProgressBar()
            launchCatchError(
                    block = {
                        val ticketListResponse = mUseCase.getTicketListResponse(requestParams?: mUseCase.getRequestParams(1,0))
                        when {
                            !ticketListResponse.ticket?.data?.ticketItems.isNullOrEmpty() -> {
                                mView?.toggleEmptyLayout(View.GONE)
                                originalList.clear()
                                ticketListResponse.ticket?.data?.ticketItems?.let { originalList.addAll(it) }
                                nextUrl = ticketListResponse.ticket?.data?.nextPage
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
                                mView?.toggleEmptyLayout(View.VISIBLE)
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

    override fun detachView() {}
    override fun onClickFilter() {
        mView?.showBottomFragment()
    }

    override fun setFilter(position: Int) {
        var selectedFilter = ""
        fromFilter = true
        when (position) {
            ALL -> {
                val requestParams = mUseCase.getRequestParams(1,0)
                getTicketList(requestParams)
                selectedFilter = getFilterList(filterList, ALL)
                getFilterAdapter().setSelected(ALL)
            }
            UNREAD -> {
//                queryMap = mUseCase.setQueryMap2(1,0)
//                selectedFilter = getFilterList(filterList, UNREAD)
//                ticketList
            }
            NEEDRATING -> {
//                queryMap = mUseCase.setQueryMap2(1,0)
//                selectedFilter = getFilterList(filterList, NEEDRATING)
//                ticketList
            }
            INPROGRESS -> {
//                queryMap = mUseCase.setQueryMap2(1,0)
//                selectedFilter = getFilterList(filterList, INPROGRESS)
//                ticketList
            }
            READ -> {
//                queryMap = mUseCase.setQueryMap2(1,0)
//                selectedFilter = getFilterList(filterList, READ)
//                ticketList
            }
            CLOSED -> {
//                queryMap = mUseCase.setQueryMap2(1,0)
//                selectedFilter = getFilterList(filterList, CLOSED)
//                ticketList
            }
        }
        ContactUsTracking.sendGTMInboxTicket("",
                InboxTicketTracking.Category.EventInboxTicket,
                InboxTicketTracking.Action.EventClickFilter,
                selectedFilter)
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
        ContactUsTracking.sendGTMInboxTicket("",
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

    private fun getFilterAdapter(): InboxFilterAdapter {
        return InboxFilterAdapter(filterList, this)
    }

    override fun getBottomFragment(resID: Int): BottomSheetDialogFragment? {
        val bottomFragment = InboxBottomSheetFragment.getBottomSheetFragment(resID)
        bottomFragment?.setAdapter(getFilterAdapter())
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
//        val requestParams = mUseCase.getRequestParams(1,status)
//        getTicketList(requestParams)
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
            } else {
                mView?.addFooter()
            }
        } else {
            mView?.removeFooter()
        }
    }

    fun loadMoreItems(page: Int) {
        isLoading = true
        mView?.addFooter()
        launchCatchError(
                block = {
                    val requestParams = mUseCase.getRequestParams(page, status)
                    val ticketListResponse = mUseCase.getTicketListResponse(requestParams)
                    if (ticketListResponse.ticket?.data?.ticketItems?.isNullOrEmpty()==false) {
                        originalList.addAll(ticketListResponse.ticket.data.ticketItems)
                        nextUrl = ticketListResponse.ticket.data.nextPage
                        isLastPage = nextUrl?.isEmpty() == true
                        mView?.updateDataSet()
                    }

                    isLoading = false
                    mView?.removeFooter()
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