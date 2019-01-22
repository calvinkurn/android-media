package com.tokopedia.topads.credit.history.view.fragment

import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.datepicker.range.view.activity.DatePickerActivity
import com.tokopedia.datepicker.range.view.constant.DatePickerConstant
import com.tokopedia.design.utils.DateLabelUtils
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.topads.common.view.TopAdsDatePickerViewModel
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.credit.history.data.model.CreditHistory
import com.tokopedia.topads.credit.history.data.model.TopAdsCreditHistory
import com.tokopedia.topads.dashboard.data.utils.TopAdsDatePeriodUtil
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.credit.history.view.adapter.TopAdsCreditHistoryTypeFactory
import com.tokopedia.topads.credit.history.view.viewmodel.TopAdsCreditHistoryViewModel
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpStatus
import com.tokopedia.topads.debit.autotopup.view.activity.TopAdsAutoTopUpActivity
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_topads_credit_history.*
import kotlinx.android.synthetic.main.partial_credit_history_header.*
import java.util.*
import javax.inject.Inject

class TopAdsCreditHistoryFragment: BaseListFragment<CreditHistory, TopAdsCreditHistoryTypeFactory>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: TopAdsCreditHistoryViewModel
    lateinit var datePickerViewModel: TopAdsDatePickerViewModel

    companion object {
        private const val MAX_RANGE_DATE = 30
        private const val REQUEST_CODE_SET_AUTO_TOPUP = 1
        fun createInstance() = TopAdsCreditHistoryFragment()
    }

    override fun onStart() {
        context?.let {
            GraphqlClient.init(it)
        }
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            viewModel = viewModelProvider.get(TopAdsCreditHistoryViewModel::class.java)
            datePickerViewModel = viewModelProvider.get(TopAdsDatePickerViewModel::class.java)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        datePickerViewModel.dateRange.observe(this, android.arch.lifecycle.Observer { onDateRangeChanged(it) })
        viewModel.creditsHistory.observe(this, android.arch.lifecycle.Observer { when (it) {
            is Success -> onSuccessGetCredit(it.data)
            is Fail -> onErrorGetCredit(it.throwable)
        } })
        viewModel.getAutoTopUpStatus.observe(this, android.arch.lifecycle.Observer { when(it) {
            is Success -> onSuccessGetAutoTopUpStatus(it.data)
            is Fail -> {}
        } })
    }

    private fun onSuccessGetAutoTopUpStatus(data: AutoTopUpStatus) {
        card_auto_topup_status.visibility = View.VISIBLE
        auto_topup_status.text = data.statusDesc
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_topads_credit_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getRecyclerView(view).addItemDecoration(DividerItemDecoration(activity))
        card_auto_topup_status.setOnClickListener { gotoAutoTopUp() }
        selected_date.setOnClickListener { onDateClicked() }
        app_bar_layout.addOnOffsetChangedListener { _, verticalOffset -> swipe_refresh_layout.isEnabled = (verticalOffset == 0) }
        viewModel.getAutoTopUpStatus(GraphqlHelper.loadRawString(resources, R.raw.gql_query_get_status_auto_topup))
    }

    private fun gotoAutoTopUp() {
        activity?.let {
            startActivityForResult(TopAdsAutoTopUpActivity.createInstance(it), REQUEST_CODE_SET_AUTO_TOPUP)
        }
    }

    private fun sendResultIntentOk() {
        activity?.run {
            setResult(Activity.RESULT_OK, Intent())
        }
    }

    override fun onDestroyView() {
        datePickerViewModel.dateRange.removeObservers(this)
        viewModel.creditsHistory.removeObservers(this)
        viewModel.getAutoTopUpStatus.removeObservers(this)
        super.onDestroyView()
    }

    override fun hasInitialSwipeRefresh() = true

    override fun onSwipeRefresh() {
        super.onSwipeRefresh()
        viewModel.getAutoTopUpStatus(GraphqlHelper.loadRawString(resources, R.raw.gql_query_get_status_auto_topup))
    }

    override fun getEmptyDataViewModel() = EmptyModel().apply { contentRes = R.string.top_ads_no_credit_history }

    private fun onDateRangeChanged(dateRange: TopAdsDatePickerViewModel.DateRange?){
        if (dateRange == null) return

        updateDateLabelView(dateRange.startDate, dateRange.endDate)
        loadInitialData()
    }

    private fun onDateClicked() {
        val selectedDateRange = datePickerViewModel.dateRange.value?.copy() ?: return

        activity?.let {
            startActivityForResult(getDateSelectionIntent(it, selectedDateRange.startDate, selectedDateRange.endDate),
                    DatePickerConstant.REQUEST_CODE_DATE)
        }
    }

    private fun getDateSelectionIntent(activity: Activity, startDate: Date, endDate: Date): Intent {
        val maxCalendar = Calendar.getInstance()
        maxCalendar.set(Calendar.HOUR_OF_DAY, 23)
        maxCalendar.set(Calendar.MINUTE, 59)
        maxCalendar.set(Calendar.SECOND, 59)

        val minCalendar = Calendar.getInstance()
        minCalendar.add(Calendar.MONTH, -6)
        minCalendar.add(Calendar.DATE, 1)
        minCalendar.set(Calendar.HOUR_OF_DAY, 0)
        minCalendar.set(Calendar.MINUTE, 0)
        minCalendar.set(Calendar.SECOND, 0)
        minCalendar.set(Calendar.MILLISECOND, 0)


        return Intent(context, DatePickerActivity::class.java).apply {
            putExtra(DatePickerConstant.EXTRA_START_DATE, startDate.time)
            putExtra(DatePickerConstant.EXTRA_END_DATE, endDate.time)

            putExtra(DatePickerConstant.EXTRA_MIN_START_DATE, minCalendar.timeInMillis)
            putExtra(DatePickerConstant.EXTRA_MAX_END_DATE, maxCalendar.timeInMillis)
            putExtra(DatePickerConstant.EXTRA_MAX_DATE_RANGE, MAX_RANGE_DATE)

            putExtra(DatePickerConstant.EXTRA_DATE_PERIOD_LIST, TopAdsDatePeriodUtil.getPeriodRangeList(activity))
            putExtra(DatePickerConstant.EXTRA_SELECTION_PERIOD, datePickerViewModel.lastSelectionDatePickerIndex)
            putExtra(DatePickerConstant.EXTRA_SELECTION_TYPE, datePickerViewModel.lastSelectionDatePickerType)

            putExtra(DatePickerConstant.EXTRA_PAGE_TITLE, getString(R.string.title_date_picker))
        }
    }

    override fun callInitialLoadAutomatically() = false

    override fun onResume() {
        super.onResume()
        datePickerViewModel.checkUpdatedDate()
    }

    private fun updateDateLabelView(startDate: Date, endDate: Date) {
        context?.let {
            selected_date.text = DateLabelUtils.getRangeDateFormatted(it, startDate.time, endDate.time)
        }
    }

    override fun getAdapterTypeFactory() = TopAdsCreditHistoryTypeFactory()

    override fun onItemClicked(t: CreditHistory?) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == DatePickerConstant.REQUEST_CODE_DATE && data != null) {
            val sDate = data.getLongExtra(DatePickerConstant.EXTRA_START_DATE, -1)
            val eDate = data.getLongExtra(DatePickerConstant.EXTRA_END_DATE, -1)
            val lastSelection = data.getIntExtra(DatePickerConstant.EXTRA_SELECTION_PERIOD, 1)
            val selectionType = data.getIntExtra(DatePickerConstant.EXTRA_SELECTION_TYPE, DatePickerConstant.SELECTION_TYPE_PERIOD_DATE)
            if (sDate != -1L && eDate != -1L) {
                datePickerViewModel.saveSelectionDatePicker(selectionType, lastSelection)
                datePickerViewModel.saveDate(Date(sDate), Date(eDate))
            }
        } else if (requestCode == REQUEST_CODE_SET_AUTO_TOPUP && resultCode == Activity.RESULT_OK){
            sendResultIntentOk()
            viewModel.getAutoTopUpStatus(GraphqlHelper.loadRawString(resources, R.raw.gql_query_get_status_auto_topup))
        }
    }

    override fun loadData(page: Int) {
        val selectedDateRange = datePickerViewModel.dateRange.value?.copy()
        viewModel.getCreditHistory(GraphqlHelper.loadRawString(resources, R.raw.gql_query_credit_history),
                selectedDateRange?.startDate, selectedDateRange?.endDate)
    }

    override fun getScreenName(): String? = null

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    private fun onSuccessGetCredit(creditHistory: TopAdsCreditHistory){
        topads_credit_addition.text = creditHistory.totalAdditionFmt
        topads_credit_used.text = creditHistory.totalUsedFmt
        card_credit_summary.visibility = View.VISIBLE
        super.renderList(creditHistory.creditHistory)
    }

    private fun onErrorGetCredit(t: Throwable?){
        super.showGetListError(t)
        card_credit_summary.visibility = View.GONE
    }

    override fun onDestroy() {
        viewModel.clear()
        super.onDestroy()
    }
}
