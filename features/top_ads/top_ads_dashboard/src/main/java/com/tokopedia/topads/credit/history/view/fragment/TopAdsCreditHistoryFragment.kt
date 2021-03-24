package com.tokopedia.topads.credit.history.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.credit.history.data.model.CreditHistory
import com.tokopedia.topads.credit.history.data.model.TopAdsCreditHistory
import com.tokopedia.topads.credit.history.view.adapter.TopAdsCreditHistoryTypeFactory
import com.tokopedia.topads.credit.history.view.viewmodel.TopAdsCreditHistoryViewModel
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.ACTIVE_STATUS
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.DATE_PICKER_SHEET
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.REQUEST_CODE_ADD_CREDIT
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.sheet.CustomDatePicker
import com.tokopedia.topads.dashboard.view.sheet.DatePickerSheet
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpStatus
import com.tokopedia.topads.debit.autotopup.view.activity.TopAdsAddCreditActivity
import com.tokopedia.topads.debit.autotopup.view.activity.TopAdsEditAutoTopUpActivity
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_topads_credit_history.*
import kotlinx.android.synthetic.main.partial_credit_history_header.*
import kotlinx.android.synthetic.main.topads_dash_layout_hari_ini.*
import kotlinx.android.synthetic.main.topads_dash_layout_hari_ini.view.*
import java.util.*
import javax.inject.Inject

class TopAdsCreditHistoryFragment : BaseListFragment<CreditHistory, TopAdsCreditHistoryTypeFactory>(), CustomDatePicker.ActionListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: TopAdsCreditHistoryViewModel
    private var datePickerSheet: DatePickerSheet? = null
    internal var startDate: Date? = null
    internal var endDate: Date? = null

    companion object {
        private const val REQUEST_CODE_SET_AUTO_TOPUP = 1
        private const val PARAM_IS_FROM_SELECTION = "is_from_selection"
        fun createInstance(isFromSelection: Boolean = false) = TopAdsCreditHistoryFragment().apply {
            arguments = Bundle().also { it.putBoolean(PARAM_IS_FROM_SELECTION, isFromSelection) }
        }
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
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.creditsHistory.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Success -> onSuccessGetCredit(it.data)
                is Fail -> onErrorGetCredit(it.throwable)
            }
        })
        viewModel.getAutoTopUpStatus.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Success -> onSuccessGetAutoTopUpStatus(it.data)
                is Fail -> {
                }
            }
        })

        viewModel.creditAmount.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            credits.text = it
        })
    }

    private fun onSuccessGetAutoTopUpStatus(data: AutoTopUpStatus) {
        card_auto_topup_status.visibility = View.VISIBLE
        auto_topup_status.text = data.statusDesc
        if (data.status == ACTIVE_STATUS) {
            auto_topup_status.setTextColor(resources.getColor(com.tokopedia.topads.common.R.color.topads_common_select_color_checked))
        } else {
            auto_topup_status.setTextColor(resources.getColor(com.tokopedia.topads.common.R.color.topads_common_text_disabled))
        }
    }

    override fun getRecyclerViewResourceId(): Int {
        return R.id.recycler_view
    }

    override fun getSwipeRefreshLayoutResourceId(): Int {
        return R.id.swipe_refresh_layout
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_topads_credit_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        card_auto_topup_status.setOnClickListener { gotoAutoTopUp() }
        hari_ini?.date_image?.setImageDrawable(context?.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_calendar))
        hari_ini?.next_image?.setImageDrawable(context?.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_arrow))
        hari_ini?.setOnClickListener {
            showBottomSheet()
        }
        viewModel.getShopDeposit()

        viewModel.getAutoTopUpStatus(GraphqlHelper.loadRawString(resources, R.raw.gql_query_get_status_auto_topup))
        addCredit?.setOnClickListener {
            startActivityForResult(Intent(context, TopAdsAddCreditActivity::class.java), REQUEST_CODE_ADD_CREDIT)
        }

    }

    private fun showBottomSheet() {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val index = sharedPref?.getInt(TopAdsDashboardConstant.DATE_RANGE_BERANDA, 2)
        val customStartDate = sharedPref?.getString(TopAdsDashboardConstant.START_DATE_BERANDA, "")
        val customEndDate = sharedPref?.getString(TopAdsDashboardConstant.END_DATE_BERANDA, "")
        val dateRange: String
        dateRange = if (customStartDate?.isNotEmpty()!!) {
            "$customStartDate - $customEndDate"
        } else
            context?.getString(R.string.topads_dash_custom_date_desc) ?: ""
        context?.let {
            datePickerSheet = DatePickerSheet.newInstance(it, index ?: 2, dateRange)
            datePickerSheet?.show()
            datePickerSheet?.onItemClick = { date1, date2, position ->
                handleDate(date1, date2, position)
                loadData(0)
            }
            datePickerSheet?.customDatepicker = {
                startCustomDatePicker()
            }
        }

    }

    private fun startCustomDatePicker() {
        val sheet = CustomDatePicker.getInstance()
        sheet.setCreditSheet()
        sheet.setListener(this)
        sheet.show(childFragmentManager, DATE_PICKER_SHEET)
    }

    private fun handleDate(date1: Long, date2: Long, position: Int) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putInt(TopAdsDashboardConstant.DATE_RANGE_BERANDA, position)
            commit()
        }
        startDate = Date(date1)
        endDate = Date(date2)
        setDateRangeText(position)
    }

    private fun setDateRangeText(position: Int) {
        when (position) {
            1 -> current_date.text = context?.getString(com.tokopedia.datepicker.range.R.string.yesterday)
            0 -> current_date.text = context?.getString(R.string.topads_dash_hari_ini)
            2 -> current_date.text = context?.getString(com.tokopedia.datepicker.range.R.string.seven_days_ago)
            else -> {
                val text = Utils.outputFormat.format(startDate) + " - " + Utils.outputFormat.format(endDate)
                current_date.text = text
            }
        }
    }


    private fun gotoAutoTopUp() {
        activity?.let {
            startActivityForResult(Intent(it, TopAdsEditAutoTopUpActivity::class.java), REQUEST_CODE_SET_AUTO_TOPUP)
        }
    }

    private fun sendResultIntentOk() {
        activity?.run {
            setResult(Activity.RESULT_OK, Intent().putExtra("no_redirect", true))
        }
    }

    override fun onDestroyView() {
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

    override fun getAdapterTypeFactory() = TopAdsCreditHistoryTypeFactory()

    override fun onItemClicked(t: CreditHistory?) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_CREDIT && resultCode == Activity.RESULT_OK) {
            viewModel.getShopDeposit()
        } else if (requestCode == REQUEST_CODE_SET_AUTO_TOPUP && resultCode == Activity.RESULT_OK) {
            sendResultIntentOk()
            viewModel.getAutoTopUpStatus(GraphqlHelper.loadRawString(resources, R.raw.gql_query_get_status_auto_topup))
        }
    }

    override fun loadData(page: Int) {
        adapter.clearAllElements()
        viewModel.getCreditHistory(GraphqlHelper.loadRawString(resources, R.raw.gql_query_credit_history),
                startDate, endDate)
    }

    override fun getScreenName(): String? = null

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    private fun onSuccessGetCredit(creditHistory: TopAdsCreditHistory) {
        topads_credit_addition.text = creditHistory.totalAdditionFmt
        topads_credit_used.text = creditHistory.totalUsedFmt
        super.renderList(creditHistory.creditHistory)
    }

    private fun onErrorGetCredit(t: Throwable?) {
        super.showGetListError(t)
    }

    override fun onDestroy() {
        viewModel.flush()
        super.onDestroy()
    }

    override fun onCustomDateSelected(dateSelected: Date, dateEnd: Date) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putInt(TopAdsDashboardConstant.DATE_RANGE_BERANDA, TopAdsDashboardConstant.CUSTOM_DATE)
            commit()
        }
        startDate = dateSelected
        with(sharedPref.edit()) {
            putString(TopAdsDashboardConstant.START_DATE_BERANDA, Utils.outputFormat.format(startDate))
            commit()
        }
        endDate = dateEnd
        with(sharedPref.edit()) {
            putString(TopAdsDashboardConstant.END_DATE_BERANDA, Utils.outputFormat.format(endDate))
            commit()
        }
        setDateRangeText(TopAdsDashboardConstant.CUSTOM_DATE)
        loadData(0)
    }
}
