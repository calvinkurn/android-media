package com.tokopedia.topads.credit.history.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.tokopedia.unifyprinciples.Typography
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.credit.history.data.model.CreditHistory
import com.tokopedia.topads.credit.history.data.model.TopAdsCreditHistory
import com.tokopedia.topads.credit.history.view.activity.PARAM_DATE_PICKER_INDEX
import com.tokopedia.topads.credit.history.view.adapter.TopAdsCreditHistoryTypeFactory
import com.tokopedia.topads.credit.history.view.viewmodel.TopAdsCreditHistoryViewModel
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.ACTIVE_STATUS
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_0
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_1
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_2
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.DATE_PICKER_DEFAULT_INDEX
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.DATE_PICKER_SHEET
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.REQUEST_CODE_ADD_CREDIT
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.sheet.CustomDatePicker
import com.tokopedia.topads.dashboard.view.sheet.DatePickerSheet
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpStatus
import com.tokopedia.topads.debit.autotopup.view.activity.TopAdsAddCreditActivity
import com.tokopedia.topads.debit.autotopup.view.activity.TopAdsEditAutoTopUpActivity
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import java.util.*
import javax.inject.Inject

class TopAdsCreditHistoryFragment :
    BaseListFragment<CreditHistory, TopAdsCreditHistoryTypeFactory>(),
    CustomDatePicker.ActionListener {

    private var hariIni: ConstraintLayout? = null
    private var currentDate: Typography? = null
    private var cardAutoTopupStatus: CardUnify? = null
    private var autoTopupStatus: Typography? = null
    private var creditAmount: Typography? = null
    private var addCredit: UnifyButton? = null
    private var topadsCreditAddition: Typography? = null
    private var topadsCreditUsed: Typography? = null
    private var dateImage: ImageUnify? = null
    private var nextImage: ImageUnify? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: TopAdsCreditHistoryViewModel
    private var datePickerSheet: DatePickerSheet? = null
    internal var startDate: Date? = null
    internal var endDate: Date? = null
    private var datePickerIndex = DATE_PICKER_DEFAULT_INDEX

    companion object {
        private const val REQUEST_CODE_SET_AUTO_TOPUP = 1
        private const val PARAM_IS_FROM_SELECTION = "is_from_selection"
        fun createInstance(isFromSelection: Boolean = false, datePickerIndex: Int) =
            TopAdsCreditHistoryFragment().apply {
                arguments = Bundle().also {
                    it.putBoolean(PARAM_IS_FROM_SELECTION, isFromSelection)
                    it.putInt(PARAM_DATE_PICKER_INDEX, datePickerIndex)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            viewModel = viewModelProvider.get(TopAdsCreditHistoryViewModel::class.java)
        }
    }

    private fun initialDateSetup() {
        datePickerIndex = arguments?.getInt(PARAM_DATE_PICKER_INDEX) ?: DATE_PICKER_DEFAULT_INDEX
        context?.let {
            val dateList = Utils.getPeriodRangeList(it)
            if (datePickerIndex < dateList.size) {
                startDate = Date(dateList[datePickerIndex].startDate)
                endDate = Date(dateList[datePickerIndex].endDate)
            }
            setDateRangeText(datePickerIndex)
            loadData(CONST_0)
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
            creditAmount?.text = it
        })
    }

    private fun onSuccessGetAutoTopUpStatus(data: AutoTopUpStatus) {
        cardAutoTopupStatus?.visibility = View.VISIBLE
        autoTopupStatus?.text = data.statusDesc
        if (data.status == ACTIVE_STATUS) {
            autoTopupStatus?.setTextColor(resources.getColor(com.tokopedia.topads.common.R.color.topads_common_select_color_checked))
        } else {
            autoTopupStatus?.setTextColor(resources.getColor(com.tokopedia.topads.common.R.color.topads_common_text_disabled))
        }
    }

    override fun getRecyclerViewResourceId(): Int {
        return R.id.recycler_view
    }

    override fun getSwipeRefreshLayoutResourceId(): Int {
        return R.id.swipe_refresh_layout
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_topads_credit_history, container, false)
        hariIni = view.findViewById(R.id.hari_ini)
        currentDate = view.findViewById(R.id.current_date)
        cardAutoTopupStatus = view.findViewById(R.id.card_auto_topup_status)
        autoTopupStatus = view.findViewById(R.id.auto_topup_status)
        creditAmount = view.findViewById(R.id.creditAmount)
        addCredit = view.findViewById(R.id.addCredit)
        topadsCreditAddition = view.findViewById(R.id.topads_credit_addition)
        topadsCreditUsed = view.findViewById(R.id.topads_credit_used)
        dateImage = view.findViewById(R.id.date_image)
        nextImage = view.findViewById(R.id.next_image)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialDateSetup()
        cardAutoTopupStatus?.setOnClickListener { gotoAutoTopUp() }
        dateImage?.setImageDrawable(context?.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_calendar))
        nextImage?.setImageDrawable(context?.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_arrow))
        hariIni?.setOnClickListener {
            showBottomSheet()
        }
        viewModel.getShopDeposit()

        viewModel.getAutoTopUpStatus()
        addCredit?.setOnClickListener {
            startActivityForResult(
                Intent(context, TopAdsAddCreditActivity::class.java),
                REQUEST_CODE_ADD_CREDIT
            )
        }

    }

    private fun showBottomSheet() {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val customStartDate = sharedPref?.getString(TopAdsDashboardConstant.START_DATE_BERANDA, "")
        val customEndDate = sharedPref?.getString(TopAdsDashboardConstant.END_DATE_BERANDA, "")
        val dateRange = if (customStartDate?.isNotEmpty()!!) {
            "$customStartDate - $customEndDate"
        } else
            context?.getString(R.string.topads_dash_custom_date_desc) ?: ""
        context?.let {
            datePickerSheet = DatePickerSheet.newInstance(it, datePickerIndex, dateRange)
            datePickerSheet?.show()
            datePickerSheet?.onItemClick = { date1, date2, position ->
                datePickerIndex = position
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
            CONST_1 -> currentDate?.text =
                context?.getString(com.tokopedia.datepicker.range.R.string.yesterday)
            CONST_0 -> currentDate?.text = context?.getString(R.string.topads_dash_hari_ini)
            CONST_2 -> currentDate?.text =
                context?.getString(com.tokopedia.datepicker.range.R.string.seven_days_ago)
            else -> {
                val text =
                    Utils.outputFormat.format(startDate) + " - " + Utils.outputFormat.format(endDate)
                currentDate?.text = text
            }
        }
    }


    private fun gotoAutoTopUp() {
        activity?.let {
            startActivityForResult(
                Intent(it, TopAdsEditAutoTopUpActivity::class.java),
                REQUEST_CODE_SET_AUTO_TOPUP
            )
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
        viewModel.getAutoTopUpStatus()
    }

    override fun getEmptyDataViewModel() =
        EmptyModel().apply { contentRes = R.string.top_ads_no_credit_history }

    override fun getAdapterTypeFactory() = TopAdsCreditHistoryTypeFactory()

    override fun onItemClicked(t: CreditHistory?) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_CREDIT && resultCode == Activity.RESULT_OK) {
            viewModel.getShopDeposit()
        } else if (requestCode == REQUEST_CODE_SET_AUTO_TOPUP && resultCode == Activity.RESULT_OK) {
            sendResultIntentOk()
            viewModel.getAutoTopUpStatus()
        }
    }

    override fun loadData(page: Int) {
        adapter.clearAllElements()
        viewModel.getCreditHistory(
            GraphqlHelper.loadRawString(resources, R.raw.gql_query_credit_history),
            startDate, endDate
        )
    }

    override fun getScreenName(): String? = null

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    private fun onSuccessGetCredit(creditHistory: TopAdsCreditHistory) {
        topadsCreditAddition?.text = creditHistory.totalAdditionFmt
        topadsCreditUsed?.text = creditHistory.totalUsedFmt
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
            putString(
                TopAdsDashboardConstant.START_DATE_BERANDA,
                Utils.outputFormat.format(startDate)
            )
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
