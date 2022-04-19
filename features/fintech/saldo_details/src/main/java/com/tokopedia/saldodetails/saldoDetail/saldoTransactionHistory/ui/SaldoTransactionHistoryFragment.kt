package com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.onTabSelected
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.commom.analytics.SaldoDetailsAnalytics
import com.tokopedia.saldodetails.commom.design.SaldoHistoryTabItem
import com.tokopedia.saldodetails.commom.di.component.SaldoDetailsComponent
import com.tokopedia.saldodetails.commom.listener.setSafeOnClickListener
import com.tokopedia.saldodetails.commom.utils.AllTransaction
import com.tokopedia.saldodetails.commom.utils.SaldoDateUtil
import com.tokopedia.saldodetails.commom.utils.TransactionTitle
import com.tokopedia.saldodetails.saldoDetail.coachmark.SaldoCoachMarkListener
import com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.adapter.SaldoHistoryPagerAdapter
import com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.viewmodel.TransactionHistoryViewModel
import com.tokopedia.unifycomponents.getCustomText
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.DateUtil.DEFAULT_VIEW_FORMAT
import kotlinx.android.synthetic.main.fragment_saldo_history.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class SaldoTransactionHistoryFragment : BaseDaggerFragment(), BaseEmptyViewHolder.Callback,
    OnDateRangeSelectListener {

    private lateinit var saldoHistoryPagerAdapter: SaldoHistoryPagerAdapter
    private var selectedDateFrom: Date = Date()
    private var selectedDateTo: Date = Date()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var analytics: SaldoDetailsAnalytics

    private val transactionHistoryViewModel: TransactionHistoryViewModel by lazy {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory)
        viewModelProvider.get(TransactionHistoryViewModel::class.java)
    }

    private val saldoTabItems = ArrayList<SaldoHistoryTabItem>()
    private val handler = Handler(Looper.getMainLooper())
    private var isCoachMarkStarted = false
    private val delayStartCoachMarkRunnable = Runnable {
        isCoachMarkStarted = true
        (activity as SaldoCoachMarkListener).startCoachMarkFlow(getPenjualanTabView())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_saldo_history, container, false)
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun initInjector() {
        getComponent(SaldoDetailsComponent::class.java).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialVar()
        initListeners()
        startInitialFetch()
    }

    fun startInitialFetch() {
        // when back from saldo withdrawal reset current item
        transactionHistoryViewPager.setCurrentItem(0, true)
        SaldoDateUtil.getInitialDateRange(::setDateRangeChanged)
        transactionHistoryViewModel.selectTransactionFilter(0, AllTransaction)
    }

    private fun setDateRangeChanged(dateFrom: Date, endDate: Date) {
        this.selectedDateFrom = dateFrom
        this.selectedDateTo = endDate
        val dateFormat = SimpleDateFormat(DEFAULT_VIEW_FORMAT, DateUtil.DEFAULT_LOCALE)
        val startDateStr = dateFormat.format(selectedDateFrom)
        val endDateStr = dateFormat.format(endDate)
        tvSelectedDateRange.text = "$startDateStr - $endDateStr"
        transactionHistoryViewModel.refreshAllTabsData(
            selectedDateFrom,
            selectedDateTo,
        )
    }

    override fun onDateRangeSelected(dateFrom: Date, dateTo: Date) {
        setDateRangeChanged(dateFrom, dateTo)
    }

    private fun initialVar() {
        loadMultipleTabItem()
        saldoHistoryPagerAdapter = SaldoHistoryPagerAdapter(childFragmentManager)
        saldoHistoryPagerAdapter.setItems(saldoTabItems)
        transactionHistoryViewPager.adapter = saldoHistoryPagerAdapter
        saldoTransactionTabsUnify.customTabMode = TabLayout.MODE_FIXED
        saldoTransactionTabsUnify.customTabGravity = TabLayout.GRAVITY_FILL
        saldoTransactionTabsUnify.setupWithViewPager(transactionHistoryViewPager)
    }

    private fun loadMultipleTabItem() {
        saldoTabItems.clear()
        //semua tab
        saldoTabItems.add(SaldoHistoryTabItem().apply {
            title = TransactionTitle.ALL_TAB
            fragment = FilteredSaldoTransactionListFragment.getInstance(TransactionTitle.ALL_TRANSACTION)
        })

        //penjualan tab
        saldoTabItems.add(SaldoHistoryTabItem().apply {
            title = TransactionTitle.SALDO_SALES
            fragment = BaseSaldoTransactionListFragment.getInstance(TransactionTitle.SALDO_SALES)
        })

        saldoTransactionTabsUnify.run {
            getUnifyTabLayout().removeAllTabs()
            for (tabItem in saldoTabItems)
                addNewTab(tabItem.title)
        }
        saldoTransactionTabsUnify.visibility = View.VISIBLE
    }

    private fun initListeners() {
        cardUnifyDateContainer.setSafeOnClickListener {
            openCalender()
        }
        saldoTransactionTabsUnify.tabLayout.onTabSelected {
            transactionHistoryViewModel.getEventLabelForTab(it.getCustomText())
                .also { actionLabel ->
                    // different event names for different tabs here
                    if (it.position == 0)
                        analytics.sendClickPaymentEvents(actionLabel)
                    else analytics.sendTransactionHistoryEvents(actionLabel)
                }
        }
    }

    private fun openCalender() {
        DateRangePickerBottomSheet.getInstance(selectedDateFrom, selectedDateTo)
            .show(childFragmentManager, "")
    }

    override fun onEmptyContentItemTextClicked() {}

    override fun onEmptyButtonClicked() {
        val intent = RouteManager.getIntent(context, ApplinkConst.HOME)
        startActivity(intent)
    }

    fun onRefresh() {
        transactionHistoryViewModel.refreshAllTabsData(
            selectedDateFrom,
            selectedDateTo
        )
    }

    override fun onResume() {
        super.onResume()
        // tabs are visible now start, coachMark
        if (!isCoachMarkStarted)
          handler.postDelayed(delayStartCoachMarkRunnable, DELAY_COACH_MARK_MILLIS)
    }

    override fun onPause() {
        handler.removeCallbacks(delayStartCoachMarkRunnable)
        super.onPause()
    }

    private fun getPenjualanTabView(): View? {
        var totalTabCount = saldoTransactionTabsUnify.tabLayout.tabCount
        do {
            val tab = saldoTransactionTabsUnify.tabLayout.getTabAt(totalTabCount - 1)
            if (tab != null && tab.getCustomText().isNotEmpty()
                && tab.getCustomText() == TransactionTitle.SALDO_SALES
            ) {
                return tab.customView
            }
            totalTabCount--
        } while (totalTabCount > 0)
        return null
    }

    companion object {
        const val DELAY_COACH_MARK_MILLIS = 400L
    }
}