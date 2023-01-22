package com.tokopedia.topads.credit.history.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.toIntOrZero
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
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CUSTOM_DATE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.DATE_PICKER_DEFAULT_INDEX
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.DATE_PICKER_SHEET
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.REQUEST_CODE_ADD_CREDIT
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.REQUEST_CODE_TOP_UP_CREDIT
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.TopAdsCreditTopUpConstant.IS_SHOW_OLD_FLOW
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.sheet.CustomDatePicker
import com.tokopedia.topads.dashboard.view.sheet.DatePickerSheet
import com.tokopedia.topads.dashboard.view.sheet.RewardPendingInfoBottomSheet
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpStatus
import com.tokopedia.topads.debit.autotopup.view.activity.TopAdsAddCreditActivity
import com.tokopedia.topads.debit.autotopup.view.activity.TopAdsCreditTopUpActivity
import com.tokopedia.topads.debit.autotopup.view.activity.TopAdsEditAutoTopUpActivity
import com.tokopedia.topads.tracker.topup.TopadsTopupTracker
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import java.util.*
import javax.inject.Inject

class TopAdsCreditHistoryFragment :
    BaseListFragment<CreditHistory, TopAdsCreditHistoryTypeFactory>(),
    CustomDatePicker.ActionListener {

    private var hariIni: ChipsUnify? = null
    private var cardAutoTopupStatus: CardUnify? = null
    private var autoTopupStatus: Typography? = null
    private var creditAmount: Typography? = null
    private var addCredit: UnifyButton? = null
    private var topadsCreditAddition: Typography? = null
    private var topadsCreditUsed: Typography? = null
    private var dateImage: ImageUnify? = null
    private var nextImage: ImageUnify? = null
    private var txtRewardPendingValue: Typography? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: TopAdsCreditHistoryViewModel
    private var datePickerSheet: DatePickerSheet? = null
    internal var startDate: Date? = null
    internal var endDate: Date? = null
    private var datePickerIndex = DATE_PICKER_DEFAULT_INDEX
    private var isAutoTopUpActive: Boolean = false
    private var isAutoTopUpSelected: Boolean = false
    private var topUpUCount: Int = 0
    private var autoTopUpBonus: Double = 0.0

    companion object {
        private const val REQUEST_CODE_SET_AUTO_TOPUP = 1
        private const val PARAM_IS_FROM_SELECTION = "is_from_selection"
        fun createInstance(isFromSelection: Boolean = false, showAutoTopUpOldFlow: Boolean = true, datePickerIndex: Int) =
            TopAdsCreditHistoryFragment().apply {
                arguments = Bundle().also {
                    it.putBoolean(PARAM_IS_FROM_SELECTION, isFromSelection)
                    it.putBoolean(IS_SHOW_OLD_FLOW, showAutoTopUpOldFlow)
                    it.putInt(PARAM_DATE_PICKER_INDEX, datePickerIndex)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            val viewModelProvider = ViewModelProvider(this, viewModelFactory)
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
        viewModel.creditsHistory.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> onSuccessGetCredit(it.data)
                is Fail -> onErrorGetCredit(it.throwable)
            }
        }
        viewModel.getAutoTopUpStatus.observe(viewLifecycleOwner){
            when (it) {
                is Success -> onSuccessGetAutoTopUpStatus(it.data)
                is Fail -> {
                }
            }
        }

        viewModel.creditAmount.observe(viewLifecycleOwner) {
            creditAmount?.text = it
        }

        viewModel.expiryDateHiddenTrial.observe(viewLifecycleOwner) {
            val rewardValue = it.toIntOrZero()
            view?.findViewById<ConstraintLayout>(R.id.layoutRewardPending)?.visibility =
                if (rewardValue != 0) {
                    txtRewardPendingValue?.text = rewardValue.toString()
                    View.VISIBLE
                } else {
                    View.GONE
                }
        }

        viewModel.getAutoTopUpDefaultSate.observe(viewLifecycleOwner) {
            if (it is Success) {
                isAutoTopUpActive = it.data.isAutoTopUp
                isAutoTopUpSelected = it.data.isAutoTopUpSelected
                topUpUCount = it.data.countTopUp
            }
        }
    }

    private fun onSuccessGetAutoTopUpStatus(data: AutoTopUpStatus) {
        cardAutoTopupStatus?.visibility = View.VISIBLE
        autoTopupStatus?.text = data.statusDesc
        context?.let {
            autoTopupStatus?.setTextColor(ContextCompat.getColor(it,
                if (data.status == ACTIVE_STATUS) {
                    com.tokopedia.topads.common.R.color.Unify_G500
                } else {
                    com.tokopedia.topads.common.R.color.Unify_N700_32
                }
            ))
        }
        autoTopUpBonus = data.statusBonus
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
        cardAutoTopupStatus = view.findViewById(R.id.card_auto_topup_status)
        autoTopupStatus = view.findViewById(R.id.auto_topup_status)
        creditAmount = view.findViewById(R.id.creditAmount)
        addCredit = view.findViewById(R.id.addCredit)
        dateImage = view.findViewById(R.id.date_image)
        nextImage = view.findViewById(R.id.next_image)
        txtRewardPendingValue = view.findViewById(R.id.txtRewardPendingValue)
        view.findViewById<ConstraintLayout>(R.id.totalKredit).apply {
            topadsCreditAddition = this.findViewById(R.id.txtSubTitle)
            findViewById<Typography>(R.id.txtTitle).text =
                context?.resources?.getString(R.string.topads_dash_total_credit)
            findViewById<ImageUnify>(R.id.ivIconInfo).hide()
        }
        view.findViewById<ConstraintLayout>(R.id.totalTerpakai).apply {
            topadsCreditUsed = this.findViewById(R.id.txtSubTitle)
            findViewById<Typography>(R.id.txtTitle).text =
                context?.resources?.getString(R.string.topads_dash_total_used)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initListeners()
        initialDateSetup()
        loadData()
    }

    private fun loadData() {
        viewModel.getShopDeposit()
        viewModel.getAutoTopUpStatus()
        viewModel.loadPendingReward()
        viewModel.getSelectedTopUpType()
    }

    private fun initView() {
        dateImage?.setImageDrawable(context?.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_calendar))
        nextImage?.setImageDrawable(context?.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_arrow))
        hariIni?.apply {
            chip_right_icon.setImageDrawable(ContextCompat.getDrawable(context,
                com.tokopedia.iconunify.R.drawable.iconunify_chevron_down))
            //called the listener just to show the right icon of chip
            setChevronClickListener {}
        }
    }

    private fun initListeners() {
        cardAutoTopupStatus?.setOnClickListener {
            TopadsTopupTracker.clickTambahKreditOtomatis()
            gotoAutoTopUp()
        }
        addCredit?.setOnClickListener {
            TopadsTopupTracker.clickTambahKreditHistoryPage()
            chooseFlow()
        }
        hariIni?.setOnClickListener {
            showBottomSheet()
        }
        view?.findViewById<ImageUnify>(R.id.iconPendingRewardInfo)?.setOnClickListener {
            RewardPendingInfoBottomSheet().show(childFragmentManager, "")
        }
    }

    private fun chooseFlow() {
        if (isShowAutoTopUpOldFlow()) {
            startActivityForResult(
                Intent(context, TopAdsAddCreditActivity::class.java),
                REQUEST_CODE_ADD_CREDIT
            )
        } else {
            startActivityForResult(
                Intent(context, TopAdsCreditTopUpActivity::class.java).also {
                    it.putExtra(TopAdsCreditTopUpActivity.IS_AUTO_TOP_UP_ACTIVE, isAutoTopUpActive)
                    it.putExtra(
                        TopAdsCreditTopUpActivity.IS_AUTO_TOP_UP_SELECTED,
                        isAutoTopUpSelected
                    )
                    it.putExtra(TopAdsCreditTopUpActivity.TOP_UP_COUNT, topUpUCount)
                    it.putExtra(TopAdsCreditTopUpActivity.AUTO_TOP_UP_BONUS, autoTopUpBonus)
                },
                REQUEST_CODE_TOP_UP_CREDIT
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
                datePickerIndex = CUSTOM_DATE
                startCustomDatePicker()
            }
        }
        TopadsTopupTracker.clickDateRange()
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
        hariIni?.chip_text?.text = when (position) {
            CONST_1 -> context?.getString(com.tokopedia.datepicker.range.R.string.yesterday)
            CONST_0 -> context?.getString(R.string.topads_dash_hari_ini)
            CONST_2 -> context?.getString(com.tokopedia.datepicker.range.R.string.seven_days_ago)
            else -> Utils.outputFormat.format(startDate ?: Utils.getStartDate()) + " - " +
                    Utils.outputFormat.format(endDate ?: Utils.getEndDate())
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
        loadData()
    }

    override fun getEmptyDataViewModel() =
        EmptyModel().apply { contentRes = R.string.top_ads_no_credit_history }

    override fun getAdapterTypeFactory() = TopAdsCreditHistoryTypeFactory()

    override fun onItemClicked(t: CreditHistory?) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_CREDIT || requestCode == REQUEST_CODE_TOP_UP_CREDIT && resultCode == Activity.RESULT_OK) {
            viewModel.getShopDeposit()
        } else if (requestCode == REQUEST_CODE_SET_AUTO_TOPUP && resultCode == Activity.RESULT_OK) {
            sendResultIntentOk()
            viewModel.getAutoTopUpStatus()
        }
    }

    override fun loadData(page: Int) {
        adapter.clearAllElements()
        viewModel.getCreditHistory(startDate, endDate)
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

    private fun isShowAutoTopUpOldFlow(): Boolean {
        return arguments?.getBoolean(IS_SHOW_OLD_FLOW, true) ?: true
    }
}
