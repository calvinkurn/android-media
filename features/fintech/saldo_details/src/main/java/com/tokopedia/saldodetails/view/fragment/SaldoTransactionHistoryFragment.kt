package com.tokopedia.saldodetails.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.saldodetails.adapter.SaldoDepositAdapter
import com.tokopedia.saldodetails.adapter.SaldoDetailTransactionFactory
import com.tokopedia.saldodetails.adapter.SaldoHistoryPagerAdapter
import com.tokopedia.saldodetails.contract.SaldoHistoryContract
import com.tokopedia.saldodetails.di.SaldoDetailsComponent
import com.tokopedia.saldodetails.utils.ErrorType
import com.tokopedia.saldodetails.utils.IN_VALID_DATE_ERROR
import com.tokopedia.saldodetails.utils.NORMAL
import com.tokopedia.saldodetails.utils.SaldoDatePickerUtil
import com.tokopedia.saldodetails.view.fragment.new.TransactionTitle
import com.tokopedia.saldodetails.view.ui.HeightWrappingViewPager
import com.tokopedia.saldodetails.view.ui.SaldoHistoryTabItem
import com.tokopedia.saldodetails.view.viewmodel.TransactionHistoryViewModel
import com.tokopedia.saldodetails.viewmodels.SaldoHistoryViewModel
import com.tokopedia.unifycomponents.TabsUnify
import java.util.*
import javax.inject.Inject

class SaldoTransactionHistoryFragment : BaseDaggerFragment(), SaldoHistoryContract.View, BaseEmptyViewHolder.Callback {

    private var startDateLayout: ConstraintLayout? = null
    private var endDateLayout: ConstraintLayout? = null
    private var tabSeparator: View? = null
    private var depositHistoryViewPager: HeightWrappingViewPager? = null
    private var depositHistoryTabLayout: TabsUnify? = null

    private var startDateTV: TextView? = null
    private var endDateTV: TextView? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val saldoHistoryViewModel: SaldoHistoryViewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(SaldoHistoryViewModel::class.java) }

    private val transactionHistoryViewModel: TransactionHistoryViewModel by lazy {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory)
        viewModelProvider.get(TransactionHistoryViewModel::class.java)
    }

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
        addObserver()
    }

    private fun addObserver() {
        saldoHistoryViewModel.errors.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                if (it is ErrorType<*>) {
                    val message = if (it.data is Int) getString(it.data) else it.data.toString()
                    when (it.type) {
                        NORMAL -> showErrorMessage(message)
                        IN_VALID_DATE_ERROR -> showInvalidDateError(message)
                    }
                }
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getInitialData()
    }

    private fun getInitialData() {
        setActionsEnabled(false)
        saldoHistoryViewModel.setFirstDateParameter(this)
        transactionHistoryViewModel.refreshAllTabsData(System.currentTimeMillis()
                - (3*24*3600*1000), System.currentTimeMillis())
        saldoHistoryViewModel.getSummaryDeposit()
    }

    private fun initViews(view: View) {
        startDateLayout = view.findViewById(com.tokopedia.saldodetails.R.id.start_date_layout)
        endDateLayout = view.findViewById(com.tokopedia.saldodetails.R.id.end_date_layout)
        startDateTV = view.findViewById(com.tokopedia.saldodetails.R.id.start_date_tv)
        endDateTV = view.findViewById(com.tokopedia.saldodetails.R.id.end_date_tv)
        depositHistoryViewPager = view.findViewById(com.tokopedia.saldodetails.R.id.transaction_history_view_pager)
        depositHistoryTabLayout = view.findViewById(com.tokopedia.saldodetails.R.id.transaction_history_tab_layout)
        tabSeparator = view.findViewById(com.tokopedia.saldodetails.R.id.transaction_history_tab_view_separator)
    }

    private fun initialVar() {

        loadMultipleTabItem()
        val saldoHistoryPagerAdapter = SaldoHistoryPagerAdapter(childFragmentManager)
        saldoHistoryPagerAdapter.setItems(saldoTabItems)
       // depositHistoryViewPager?.offscreenPageLimit = 2
        depositHistoryViewPager?.adapter = saldoHistoryPagerAdapter
        depositHistoryTabLayout?.setupWithViewPager(depositHistoryViewPager!!)

        datePicker = activity?.let { SaldoDatePickerUtil(it) }
    }

    private fun loadMultipleTabItem() {

        saldoTabItems.clear()

        allSaldoHistoryTabItem = SaldoHistoryTabItem()
        allSaldoHistoryTabItem!!.title = TransactionTitle.ALL_TRANSACTION
        allSaldoHistoryTabItem!!.fragment = SaldoTransactionListFragment.getInstance(TransactionTitle.ALL_TRANSACTION)//SaldoHistoryListFragment.createInstance(FOR_ALL, saldoHistoryViewModel, this)

        saldoTabItems.add(allSaldoHistoryTabItem!!)

        buyerSaldoHistoryTabItem = SaldoHistoryTabItem()
        buyerSaldoHistoryTabItem!!.title = TransactionTitle.SALDO_REFUND
        buyerSaldoHistoryTabItem!!.fragment = SaldoTransactionListFragment.getInstance(TransactionTitle.SALDO_REFUND)//SaldoHistoryListFragment.createInstance(FOR_BUYER, saldoHistoryViewModel, this)

        saldoTabItems.add(buyerSaldoHistoryTabItem!!)

        sellerSaldoHistoryTabItem = SaldoHistoryTabItem()
        sellerSaldoHistoryTabItem!!.title = TransactionTitle.SALDO_INCOME
        sellerSaldoHistoryTabItem!!.fragment =  SaldoTransactionListFragment.getInstance(TransactionTitle.SALDO_INCOME)//SaldoHistoryListFragment.createInstance(FOR_SELLER, saldoHistoryViewModel, this)

        saldoTabItems.add(sellerSaldoHistoryTabItem!!)
        depositHistoryTabLayout?.run {
            getUnifyTabLayout().removeAllTabs()
            for(tabItem in saldoTabItems)
                addNewTab(tabItem.title!!)
        }
        depositHistoryTabLayout!!.visibility = View.VISIBLE
        tabSeparator!!.visibility = View.VISIBLE

    }

    private fun initListeners() {

        depositHistoryViewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
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
            saldoHistoryViewModel.onStartDateClicked(datePicker!!, this)
        }
        endDateLayout!!.setOnClickListener {
            saldoHistoryViewModel.onEndDateClicked(datePicker!!, this)
        }

    }

    override fun onEmptyContentItemTextClicked() {}

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


    override fun refresh() {}

    override fun showEmptyState() {
        setActionsEnabled(false)
        NetworkErrorHelper.showEmptyState(activity, view) { saldoHistoryViewModel.getSummaryDeposit() }
    }

    override fun setRetry() {
        setActionsEnabled(false)
        NetworkErrorHelper.createSnackbarWithAction(activity) { saldoHistoryViewModel.getSummaryDeposit() }.showRetrySnackbar()
    }

    override fun showEmptyState(error: String) {
        setActionsEnabled(false)
        NetworkErrorHelper.showEmptyState(activity, view, error
        ) { saldoHistoryViewModel.getSummaryDeposit() }
    }

    override fun setRetry(error: String) {
        setActionsEnabled(false)
        NetworkErrorHelper.createSnackbarWithAction(activity, error
        ) { saldoHistoryViewModel.getSummaryDeposit() }.showRetrySnackbar()
    }

    override fun setStartDate(date: String) {
        startDateTV!!.text = date
    }

    override fun setEndDate(date: String) {
        endDateTV!!.text = date
    }

    override fun getStartDate() = startDateTV!!.text.toString()

    override fun getEndDate() = endDateTV!!.text.toString()

    override fun showErrorMessage(s: String) {
        NetworkErrorHelper.showRedCloseSnackbar(activity, s)
    }

    override fun showInvalidDateError(errorMessage: String) {
        NetworkErrorHelper.showRedCloseSnackbar(activity, errorMessage)
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

    override fun getScreenName(): String? {
        return null
    }

    override fun initInjector() {
        getComponent(SaldoDetailsComponent::class.java).inject(this)
    }

    fun onRefresh() {
        saldoHistoryViewModel.onRefresh()
    }

    companion object {

        val TRANSACTION_TYPE = "type"
        val FOR_SELLER = "for_seller"
        val FOR_BUYER = "for_buyer"
        val FOR_ALL = "for_all"
    }
}
