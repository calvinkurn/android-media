package com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.ui

import android.os.Bundle
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
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.coachmark.CoachMarkContentPosition
import com.tokopedia.coachmark.CoachMarkPreference
import com.tokopedia.kotlin.extensions.view.onTabSelected
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.commom.analytics.SaldoDetailsAnalytics
import com.tokopedia.saldodetails.commom.design.SaldoHistoryTabItem
import com.tokopedia.saldodetails.commom.di.component.SaldoDetailsComponent
import com.tokopedia.saldodetails.commom.listener.setSafeOnClickListener
import com.tokopedia.saldodetails.commom.utils.SaldoDateUtil
import com.tokopedia.saldodetails.commom.utils.TransactionTitle
import com.tokopedia.saldodetails.saldoDetail.SaldoDepositFragment
import com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.adapter.SaldoHistoryPagerAdapter
import com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.viewmodel.TransactionHistoryViewModel
import com.tokopedia.unifycomponents.getCustomText
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.DateUtil.DEFAULT_VIEW_FORMAT
import kotlinx.android.synthetic.main.fragment_saldo_history.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class SaldoTransactionHistoryFragment : BaseDaggerFragment(), BaseEmptyViewHolder.Callback,
    OnDateRangeSelectListener {

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

    private fun startInitialFetch() {
        SaldoDateUtil.getInitialDateRange(::setDateRangeChanged)
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
        val saldoHistoryPagerAdapter = SaldoHistoryPagerAdapter(childFragmentManager)
        saldoHistoryPagerAdapter.setItems(saldoTabItems)
        transactionHistoryViewPager.adapter = saldoHistoryPagerAdapter
        saldoTransactionTabsUnify.customTabMode = TabLayout.MODE_SCROLLABLE
        saldoTransactionTabsUnify.customTabGravity = TabLayout.GRAVITY_START
        saldoTransactionTabsUnify.setupWithViewPager(transactionHistoryViewPager)

    }

    private fun loadMultipleTabItem() {
        saldoTabItems.clear()
        //semua tab
        saldoTabItems.add(SaldoHistoryTabItem().apply {
            title = TransactionTitle.ALL_TRANSACTION
            fragment = SaldoTransactionListFragment.getInstance(TransactionTitle.ALL_TRANSACTION)
        })

        //penjualan tab
        saldoTabItems.add(SaldoHistoryTabItem().apply {
            title = TransactionTitle.SALDO_SALES
            fragment = SaldoTransactionListFragment.getInstance(TransactionTitle.SALDO_SALES)
        })

        //Saldo Refund
        saldoTabItems.add(SaldoHistoryTabItem().apply {
            title = TransactionTitle.SALDO_REFUND
            fragment = SaldoTransactionListFragment.getInstance(TransactionTitle.SALDO_REFUND)
        })

        //Saldo Penghasilan tab

        saldoTabItems.add(SaldoHistoryTabItem().apply {
            title = TransactionTitle.SALDO_INCOME
            fragment = SaldoTransactionListFragment.getInstance(TransactionTitle.SALDO_INCOME)
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
                    analytics.sendTransactionHistoryEvents(actionLabel)
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

    var coachMark2: CoachMark2? = null

    fun showSaleTabCoachMark() {
        getPenjualanTabView()?.also {
            it.postDelayed({
                showCoachMark(it)
            }, DELAY_COACHMARK)
        }
    }

    private fun showCoachMark(view: View) {
        try {
            if (context != null && activity?.isFinishing == false) {
                coachMark2 = CoachMark2(requireContext())
                val list = arrayListOf<CoachMark2Item>().apply {
                    add(
                        CoachMark2Item(
                            view,
                            getString(R.string.saldo_penjualan_coachmark_title),
                            getString(R.string.saldo_penjualan_coachmark_desc),
                            CoachMarkContentPosition.BOTTOM.position
                        )
                    )
                }
                coachMark2?.showCoachMark(list)
                coachMark2?.setOnDismissListener {
                    coachMark2 = null
                    updatePenjualanCoachMarkDisplayed()
                }
            }
        } catch (e: Exception) {

        }
    }

    fun updatePenjualanCoachMark() {
        val xOffset = (X_OFFSET).toPx()
        val yOffset = 8.toPx()
        coachMark2?.let {
            val tabView = getPenjualanTabView()
            tabView?.post {
                coachMark2?.update(tabView, xOffset, yOffset, -1, -1)
            }
        }
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

    fun hasPenjualanCoachMarkShown(): Boolean {
        context?.let {
            return CoachMarkPreference.hasShown(
                it,
                SaldoDepositFragment.KEY_CAN_SHOW_PENJUALAN_COACHMARK
            )
        } ?: run { return true }
    }

    private fun updatePenjualanCoachMarkDisplayed() {
        context?.let {
            CoachMarkPreference.setShown(
                requireContext(),
                SaldoDepositFragment.KEY_CAN_SHOW_PENJUALAN_COACHMARK, true
            )
        }
    }

    companion object {
        const val X_OFFSET = -70
        const val DELAY_COACHMARK = 500L
    }
}