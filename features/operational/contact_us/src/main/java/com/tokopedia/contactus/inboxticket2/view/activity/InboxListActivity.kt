package com.tokopedia.contactus.inboxticket2.view.activity

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tkpd.remoteresourcerequest.view.DeferredImageView
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.contactus.R
import com.tokopedia.contactus.common.analytics.ContactUsTracking
import com.tokopedia.contactus.common.analytics.InboxTicketTracking
import com.tokopedia.contactus.home.view.ContactUsHomeActivity
import com.tokopedia.contactus.inboxticket2.data.model.InboxTicketListResponse
import com.tokopedia.contactus.inboxticket2.view.adapter.TicketListAdapter
import com.tokopedia.contactus.inboxticket2.view.contract.InboxBaseContract.InboxBasePresenter
import com.tokopedia.contactus.inboxticket2.view.contract.InboxListContract.InboxListPresenter
import com.tokopedia.contactus.inboxticket2.view.contract.InboxListContract.InboxListView
import com.tokopedia.contactus.inboxticket2.view.customview.CustomEditText
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.webview.KEY_TITLE

private const val RAISE_TICKET_TAG = "raiseTicket"
class InboxListActivity : InboxBaseActivity(), InboxListView, View.OnClickListener {
    private var ivNoTicket: DeferredImageView? = null
    private var tvNoTicket: Typography? = null
    private var tvRaiseTicket: Typography? = null
    private var tvGreetNoTicket: Typography? = null
    private var rvEmailList: VerticalRecyclerView? = null
    private var btnFilter: View? = null
    private var searchView: View? = null
    private var editText: CustomEditText? = null
    private var clearSearch: View? = null
    private var mAdapter: TicketListAdapter? = null
    private var btnFilterTv: TextView? = null

    override fun renderTicketList(ticketItemList: MutableList<InboxTicketListResponse.Ticket.Data.TicketItem>) {
        if (mAdapter == null) {
            mAdapter = TicketListAdapter(ticketItemList, mPresenter as InboxListPresenter)
        } else {
            mAdapter?.notifyDataSetChanged()
        }
        rvEmailList?.adapter = mAdapter
        rvEmailList?.show()
    }

    override fun hideFilter() {
        btnFilter?.hide()
    }

    override fun showFilter() {
        btnFilter?.show()
    }

    override fun toggleSearch(visibility: Int) {
        searchView?.visibility = visibility
    }

    override fun toggleEmptyLayout(visibility: Int) {
        ivNoTicket?.visibility = visibility
        tvNoTicket?.visibility = visibility
        tvRaiseTicket?.visibility = visibility
        rvEmailList?.hide()
    }

    override fun toggleNoTicketLayout(visibility: Int, name: String) {
        ivNoTicket?.loadRemoteImageDrawable("no_messages.png")
        ivNoTicket?.visibility = visibility
        tvNoTicket?.text = getString(R.string.contact_us_no_ticket_message)
        tvNoTicket?.visibility = visibility
        tvRaiseTicket?.text = getString(R.string.contact_us_tokopedia_care)
        tvRaiseTicket?.tag = RAISE_TICKET_TAG
        tvRaiseTicket?.visibility = visibility
        tvGreetNoTicket?.text = String.format(getString(R.string.contact_us_greet_user), name)
        tvGreetNoTicket?.visibility = visibility
        rvEmailList?.hide()
    }

    override fun removeFooter() {
        mAdapter?.removeFooter()
    }

    override fun addFooter() {
        mAdapter?.addFooter()
    }

    override fun getLayoutManager(): LinearLayoutManager {
        return rvEmailList?.layoutManager as LinearLayoutManager
    }

    override fun scrollRv() {
        rvEmailList?.scrollBy(0, 0)
    }

    override fun getLayoutRes(): Int {
        return R.layout.layout_ticket_list_activity
    }

    override fun getPresenter(): InboxBasePresenter {
        return component.getTicketListPresenter()
    }


    override fun getToolbarResourceID(): Int {
        return R.id.toolbar
    }

    override fun initView() {
        this.findingViewsId()
        (mPresenter as InboxListPresenter).getTicketList(null)
        settingOnClickListener()
        btnFilterTv?.setCompoundDrawablesWithIntrinsicBounds(MethodChecker.getDrawable(this, R.drawable.contactus_ic_filter_list), null, null, null)
        rvEmailList?.addOnScrollListener(rvOnScrollListener)
        editText?.setListener((mPresenter as InboxListPresenter).getSearchListener())
    }

    private fun findingViewsId() {
        ivNoTicket = findViewById(R.id.iv_no_ticket)
        tvNoTicket = findViewById(R.id.tv_no_ticket)
        tvRaiseTicket = findViewById(R.id.tv_raise_ticket)
        tvGreetNoTicket = findViewById(R.id.tv_greeting)
        rvEmailList = findViewById(R.id.rv_email_list)
        btnFilter = findViewById(R.id.btn_filter)
        searchView = findViewById(R.id.inbox_search_view)
        editText = findViewById(R.id.custom_search)
        clearSearch = findViewById(R.id.close_search)
        btnFilterTv = findViewById(R.id.btn_filter_tv)
    }

    private fun settingOnClickListener() {
        tvRaiseTicket?.setOnClickListener(this)
        btnFilter?.setOnClickListener(this)
        clearSearch?.setOnClickListener(this)
    }

    override fun getMenuRes(): Int {
        return -1
    }

    override fun getBottomSheetLayoutRes(): Int {
        return R.layout.layout_bottom_sheet_fragment
    }

    override fun doNeedReattach(): Boolean {
        return true
    }

    private fun onClickFilter(v: View) {
        if (v.id == R.id.btn_filter) {
            (mPresenter as InboxListPresenter).onClickFilter()
        } else if (v.id == R.id.close_search) {
            mPresenter?.clickCloseSearch()
        }
    }

    private fun raiseTicket() {
        if (tvRaiseTicket?.tag == RAISE_TICKET_TAG) {
            val contactUsHome = Intent(this, ContactUsHomeActivity::class.java)
            contactUsHome.putExtra(KEY_TITLE, getString(R.string.contact_us_title_home))
            contactUsHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(contactUsHome)
            ContactUsTracking.sendGTMInboxTicket("",
                    InboxTicketTracking.Category.EventInboxTicket,
                    InboxTicketTracking.Action.EventClickHubungi,
                    InboxTicketTracking.Label.InboxEmpty)
            finish()
        } else {
            (mPresenter as InboxListPresenter).getTicketList(null)
        }
    }

    override fun isSearchMode(): Boolean {
        return editText?.text?.isEmpty() == true
    }

    override fun updateDataSet() {
        mAdapter?.notifyDataSetChanged()
    }

    override fun clearSearch() {
        editText?.setText("")
    }

    private val rvOnScrollListener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            (mPresenter as InboxListPresenter).onRecyclerViewScrolled(getLayoutManager())
        }
    }

    override fun onClick(view: View) {
        val id = view.id
        if (id == R.id.btn_filter || id == R.id.close_search) {
            onClickFilter(view)
        } else if (id == R.id.tv_raise_ticket) {
            raiseTicket()
        }
    }

    companion object {
        @JvmStatic
        fun getCallingIntent(context: Context?): Intent {
            return Intent(context, InboxListActivity::class.java)
        }
    }
}