package com.tokopedia.saldodetails.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.date.util.SaldoDatePickerUtil
import com.tokopedia.saldodetails.adapter.SaldoDepositAdapter
import com.tokopedia.saldodetails.adapter.SaldoDetailTransactionFactory
import com.tokopedia.saldodetails.adapter.SaldoHistoryPagerAdapter
import com.tokopedia.saldodetails.contract.SaldoHistoryContract
import com.tokopedia.saldodetails.di.SaldoDetailsComponentInstance
import com.tokopedia.saldodetails.presenter.SaldoHistoryPresenter
import com.tokopedia.saldodetails.view.ui.HeightWrappingViewPager
import com.tokopedia.saldodetails.view.ui.SaldoHistoryTabItem
import java.util.*
import javax.inject.Inject

class SaldoTransactionHistoryFragment : BaseDaggerFragment(), SaldoHistoryContract.View, BaseEmptyViewHolder.Callback {

    private var startDateLayout: RelativeLayout? = null
    private var endDateLayout: RelativeLayout? = null
    private var tabSeparator: View? = null
    private var depositHistoryViewPager: HeightWrappingViewPager? = null
    private var depositHistoryTabLayout: TabLayout? = null

    private var startDateTV: TextView? = null
    private var endDateTV: TextView? = null

    @Inject
    lateinit var saldoHistoryPresenter: SaldoHistoryPresenter

    private var datePicker: SaldoDatePickerUtil? = null

    private val saldoTabItems = ArrayList<SaldoHistoryTabItem>()
    private val singleTabItem: SaldoHistoryTabItem? = null
    private var activePosition = -1
    private var allSaldoHistoryTabItem: SaldoHistoryTabItem? = null
    private var buyerSaldoHistoryTabItem: SaldoHistoryTabItem? = null
    private var sellerSaldoHistoryTabItem: SaldoHistoryTabItem? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(com.tokopedia.saldodetails.R.layout.fragment_saldo_history, container, false)
        initViews(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialVar()
        initListeners()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getInitialdata()
    }

    private fun getInitialdata() {
        setActionsEnabled(false)
        saldoHistoryPresenter.setFirstDateParameter()
        saldoHistoryPresenter.getSummaryDeposit()
    }

    private fun initViews(view: View) {
        startDateLayout = view.findViewById(com.tokopedia.saldodetails.R.id.start_date_layout)
        endDateLayout = view.findViewById(com.tokopedia.saldodetails.R.id.end_date_layout)
        startDateTV = view.findViewById(com.tokopedia.saldodetails.R.id.start_date_tv)
        startDateTV!!.setCompoundDrawablesWithIntrinsicBounds(MethodChecker.getDrawable(context, com.tokopedia.design.R.drawable.ic_calendar_grey), null, null, null)
        endDateTV = view.findViewById(com.tokopedia.saldodetails.R.id.end_date_tv)
        endDateTV!!.setCompoundDrawablesWithIntrinsicBounds(MethodChecker.getDrawable(context, com.tokopedia.design.R.drawable.ic_calendar_grey), null, null, null)

        depositHistoryViewPager = view.findViewById(com.tokopedia.saldodetails.R.id.transaction_history_view_pager)
        depositHistoryTabLayout = view.findViewById(com.tokopedia.saldodetails.R.id.transaction_history_tab_layout)
        tabSeparator = view.findViewById(com.tokopedia.saldodetails.R.id.transaction_history_tab_view_separator)
    }

    private fun initialVar() {

        loadMultipleTabItem()
        val saldoHistoryPagerAdapter = SaldoHistoryPagerAdapter(childFragmentManager)
        saldoHistoryPagerAdapter.setItems(saldoTabItems)
        depositHistoryViewPager!!.offscreenPageLimit = 2
        depositHistoryViewPager!!.adapter = saldoHistoryPagerAdapter
        depositHistoryTabLayout!!.setupWithViewPager(depositHistoryViewPager)

        datePicker = SaldoDatePickerUtil(activity)
    }

    private fun loadMultipleTabItem() {

        saldoTabItems.clear()

        allSaldoHistoryTabItem = SaldoHistoryTabItem()
        allSaldoHistoryTabItem!!.title = "Semua"
        allSaldoHistoryTabItem!!.fragment = SaldoHistoryListFragment.createInstance(FOR_ALL, saldoHistoryPresenter)

        saldoTabItems.add(allSaldoHistoryTabItem!!)

        buyerSaldoHistoryTabItem = SaldoHistoryTabItem()
        buyerSaldoHistoryTabItem!!.title = "Refund"
        buyerSaldoHistoryTabItem!!.fragment = SaldoHistoryListFragment.createInstance(FOR_BUYER, saldoHistoryPresenter)

        saldoTabItems.add(buyerSaldoHistoryTabItem!!)

        sellerSaldoHistoryTabItem = SaldoHistoryTabItem()
        sellerSaldoHistoryTabItem!!.title = "Penghasilan"
        sellerSaldoHistoryTabItem!!.fragment = SaldoHistoryListFragment.createInstance(FOR_SELLER, saldoHistoryPresenter)

        saldoTabItems.add(sellerSaldoHistoryTabItem!!)

        depositHistoryTabLayout!!.visibility = View.VISIBLE
        tabSeparator!!.visibility = View.VISIBLE

    }

    private fun initListeners() {

        depositHistoryViewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if (activePosition != position) {
                    activePosition = position
                }
            }

            override fun onPageSelected(position: Int) {
                if (activePosition != position) {
                    activePosition = position
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        startDateLayout!!.setOnClickListener {
            saldoHistoryPresenter.onStartDateClicked(datePicker!!)
        }
        endDateLayout!!.setOnClickListener {
            saldoHistoryPresenter.onEndDateClicked(datePicker!!)
        }

    }

    override fun onEmptyContentItemTextClicked() {

    }

    override fun onEmptyButtonClicked() {
        val intent = RouteManager.getIntent(context, ApplinkConst.HOME)
        startActivity(intent)
    }

    override fun setActionsEnabled(isEnabled: Boolean?) {
        if (!isAdded || startDateTV == null || endDateTV == null) {
            return
        }
        startDateLayout!!.isEnabled = isEnabled!!
        endDateLayout!!.isEnabled = isEnabled
    }

    override fun setLoading() {

    }

    override fun refresh() {

    }

    override fun showEmptyState() {
        setActionsEnabled(false)
        NetworkErrorHelper.showEmptyState(activity, view) { saldoHistoryPresenter.getSummaryDeposit() }
    }

    override fun setRetry() {
        setActionsEnabled(false)
        NetworkErrorHelper.createSnackbarWithAction(activity) { saldoHistoryPresenter.getSummaryDeposit() }.showRetrySnackbar()
    }

    override fun showEmptyState(error: String) {
        setActionsEnabled(false)
        NetworkErrorHelper.showEmptyState(activity, view, error
        ) { saldoHistoryPresenter.getSummaryDeposit() }
    }

    override fun setRetry(error: String) {
        setActionsEnabled(false)
        NetworkErrorHelper.createSnackbarWithAction(activity, error
        ) { saldoHistoryPresenter.getSummaryDeposit() }.showRetrySnackbar()
    }

    override fun setStartDate(date: String) {
        startDateTV!!.text = date
    }

    override fun setEndDate(date: String) {
        endDateTV!!.text = date
    }

    override fun getStartDate(): String? {
        return startDateTV!!.text.toString()
    }

    override fun getEndDate(): String? {
        return endDateTV!!.text.toString()
    }

    override fun getDefaultEmptyViewModel(): Visitable<*>? {
        return EmptyModel()
    }

    override fun finishLoading() {
        if (getSingleTabAdapter() != null) {
            getSingleTabAdapter()!!.hideLoading()
        }

        if (getAllHistoryAdapter() != null) {
            getAllHistoryAdapter()!!.hideLoading()
        }

        if (getBuyerHistoryAdapter() != null) {
            getBuyerHistoryAdapter()!!.hideLoading()
        }

        if (getSellerHistoryAdapter() != null) {
            getSellerHistoryAdapter()!!.hideLoading()
        }
    }

    override fun showErrorMessage(s: String) {
        NetworkErrorHelper.showRedCloseSnackbar(activity, s)
    }

    override fun showInvalidDateError(errorMessage: String) {
        NetworkErrorHelper.showRedCloseSnackbar(activity, errorMessage)
    }

    override fun removeError() {
        if (getSingleTabAdapter() != null) {
            getSingleTabAdapter()!!.removeErrorNetwork()
        }

        if (getAllHistoryAdapter() != null) {
            getAllHistoryAdapter()!!.removeErrorNetwork()
        }

        if (getBuyerHistoryAdapter() != null) {
            getBuyerHistoryAdapter()!!.removeErrorNetwork()
        }

        if (getSellerHistoryAdapter() != null) {
            getSellerHistoryAdapter()!!.removeErrorNetwork()
        }

    }

    override fun getSingleTabAdapter(): SaldoDepositAdapter? {
        return if (singleTabItem != null) {
            (singleTabItem.fragment as SaldoHistoryListFragment).adapter
        } else {
            createNewAdapter()
        }
    }

    override fun getAllHistoryAdapter(): SaldoDepositAdapter? {
        return if (allSaldoHistoryTabItem != null) {
            (allSaldoHistoryTabItem!!.fragment as SaldoHistoryListFragment).adapter
        } else {
            createNewAdapter()
        }
    }

    override fun getAllSaldoHistoryTabItem(): SaldoHistoryTabItem? {
        return allSaldoHistoryTabItem
    }


    override fun getBuyerHistoryAdapter(): SaldoDepositAdapter? {
        return if (buyerSaldoHistoryTabItem != null) {
            (buyerSaldoHistoryTabItem!!.fragment as SaldoHistoryListFragment).adapter
        } else {
            createNewAdapter()
        }
    }

    override fun getBuyerSaldoHistoryTabItem(): SaldoHistoryTabItem? {
        return buyerSaldoHistoryTabItem
    }

    override fun getSellerHistoryAdapter(): SaldoDepositAdapter? {

        return if (sellerSaldoHistoryTabItem != null) {
            if ((sellerSaldoHistoryTabItem!!.fragment as SaldoHistoryListFragment).adapter == null)
                createNewAdapter()
            else
                (sellerSaldoHistoryTabItem!!.fragment as SaldoHistoryListFragment).adapter
        } else {
            createNewAdapter()
        }
    }

    override fun getSellerSaldoHistoryTabItem(): SaldoHistoryTabItem? {
        return sellerSaldoHistoryTabItem
    }

    private fun createNewAdapter(): SaldoDepositAdapter {
        return SaldoDepositAdapter(SaldoDetailTransactionFactory())
    }

    override fun getAdapter(): SaldoDepositAdapter? {
        if (activePosition == 0) {
            return (allSaldoHistoryTabItem!!.fragment as SaldoHistoryListFragment).adapter
        } else if (activePosition == 1) {
            return (buyerSaldoHistoryTabItem!!.fragment as SaldoHistoryListFragment).adapter
        } else if (activePosition == 2) {
            return (sellerSaldoHistoryTabItem!!.fragment as SaldoHistoryListFragment).adapter
        }

        return SaldoDepositAdapter(SaldoDetailTransactionFactory())
    }

    override fun onDestroy() {
        saldoHistoryPresenter.detachView()
        super.onDestroy()
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun initInjector() {
        try {
            val saldoDetailsComponent = SaldoDetailsComponentInstance.getComponent(activity!!.application)
            saldoDetailsComponent!!.inject(this)
            saldoHistoryPresenter.attachView(this)
        } catch (e: NullPointerException) {

        }

    }

    fun onRefresh() {
        saldoHistoryPresenter.onRefresh()
    }

    companion object {

        val TRANSACTION_TYPE = "type"
        val FOR_SELLER = "for_seller"
        val FOR_BUYER = "for_buyer"
        val FOR_ALL = "for_all"
    }
}
