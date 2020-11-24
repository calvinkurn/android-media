package com.tokopedia.top_ads_headline.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.sellermigration.SellerMigrationApplinkConst
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.top_ads_headline.R
import com.tokopedia.top_ads_headline.data.CreateHeadlineAdsStepperModel
import com.tokopedia.top_ads_headline.data.TopAdsManageHeadlineInput
import com.tokopedia.top_ads_headline.di.DaggerHeadlineAdsComponent
import com.tokopedia.top_ads_headline.view.activity.HeadlineStepperActivity
import com.tokopedia.top_ads_headline.view.sheet.HeadlinePreviewBottomSheet
import com.tokopedia.top_ads_headline.view.viewmodel.AdScheduleAndBudgetViewModel
import com.tokopedia.topads.common.activity.*
import com.tokopedia.topads.common.data.internal.ParamObject.ACTION_CREATE
import com.tokopedia.topads.common.data.internal.ParamObject.HEADLINE_SOURCE
import com.tokopedia.topads.common.data.util.DateTimeUtils.getSpecifiedDateFromStartDate
import com.tokopedia.topads.common.data.util.DateTimeUtils.getSpecifiedDateFromToday
import com.tokopedia.topads.common.data.util.DateTimeUtils.getToday
import com.tokopedia.topads.common.data.util.Utils.convertToCurrency
import com.tokopedia.topads.common.data.util.Utils.removeCommaRawString
import com.tokopedia.topads.common.getSellerMigrationFeatureName
import com.tokopedia.topads.common.getSellerMigrationRedirectionApplinks
import com.tokopedia.topads.common.isFromPdpSellerMigration
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiHeaderModel
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiModel
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiRowModel
import com.tokopedia.topads.common.view.sheet.TipsListSheet
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.text.currency.NumberTextWatcher
import kotlinx.android.synthetic.main.fragment_ad_schedule_and_budget.*
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.collections.ArrayList

private const val COUNTRY_ID = "ID"
private const val LANGUAGE_ID = "in"
private const val HEADLINE_DATETIME_FORMAT1 = "dd MMM yyyy, HH:mm"
private const val HEADLINE_DATETIME_FORMAT2 = "dd/MM/yyyy hh:mm aa"
private const val MAX_DAILY_BUDGET = "1.000.000.000.000"
private const val MINUTE_INTERVAL = 30
private const val MULTIPLIER = 3

class AdScheduleAndBudgetFragment : BaseHeadlineStepperFragment<CreateHeadlineAdsStepperModel>() {

    private val localeID = Locale(LANGUAGE_ID, COUNTRY_ID)
    private var selectedStartDate: Calendar? = null
    private var selectedEndDate: Calendar? = null

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: AdScheduleAndBudgetViewModel

    companion object {
        fun createInstance() = AdScheduleAndBudgetFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AdScheduleAndBudgetViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_ad_schedule_and_budget, container, false)
    }

    override fun initiateStepperModel() {
        stepperModel = stepperModel ?: CreateHeadlineAdsStepperModel()
    }

    override fun gotoNextPage() {
        stepperListener?.goToNextPage(stepperModel)
    }

    override fun populateView() {
        advertisingCost.textFieldInput.isSaveEnabled = false
        budgetCost.textFieldInput.isSaveEnabled = false
        startDate.textFieldInput.isSaveEnabled = false
        endDate.textFieldInput.isSaveEnabled = false
        setMinBid()
        setUpToolTip()
        setUpScheduleView()
        setUpLimitView()
        previewBtn.setOnClickListener {
            openPreviewBottomSheet()
        }
        btnNext.setOnClickListener {
            createHeadlineAd()
        }
    }

    private fun createHeadlineAd() {
        val topAdsHeadlineInput = getTopAdsManageHeadlineInput()
        viewModel.createHeadlineAd(topAdsHeadlineInput, this::onCreationSuccess, this::onCreationError)

    }

    private fun onCreationError(message: String) {
        view?.let { Toaster.build(it, message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show() }
    }

    private fun onCreationSuccess() {
        val intent: Intent = Intent(context, SuccessActivity::class.java).apply {
            if (isFromPdpSellerMigration(activity?.intent?.extras)) {
                putExtra(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME, getSellerMigrationFeatureName(activity?.intent?.extras))
                putStringArrayListExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA, getSellerMigrationRedirectionApplinks(activity?.intent?.extras))
            }
            putExtra(EXTRA_HIDE_TOOLBAR, true)
            putExtra(EXTRA_TITLE, getString(R.string.topads_headline_success_title_message, stepperModel?.groupName))
            putExtra(EXTRA_SUBTITLE, getString(R.string.topads_headline_success_subtitle_message))
            putExtra(EXTRA_BUTTON, getString(R.string.topads_headline_success_button_message))
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun getTimeSelected(): Long? {
        var endDate = selectedEndDate?.time?.time ?: 0
        var startDate = selectedStartDate?.time?.time ?: 0
        context?.let {
            if (selectedEndDate == null)
                endDate = it.getSpecifiedDateFromToday(month = 1).time.time
            if (selectedStartDate == null)
                startDate = it.getToday().time.time
        }
        val diff = endDate.minus(startDate)
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
    }

    private fun getTopAdsManageHeadlineInput(): TopAdsManageHeadlineInput {
        val scheduleStart = if (adScheduleSwitch.isChecked) {
            selectedStartDate?.time?.toFormattedString(HEADLINE_DATETIME_FORMAT2, localeID) ?: ""
        } else {
            context?.getToday()?.time?.toFormattedString(HEADLINE_DATETIME_FORMAT2, localeID) ?: ""
        }
        val scheduleEnd = if (adScheduleSwitch.isChecked) {
            selectedEndDate?.time?.toFormattedString(HEADLINE_DATETIME_FORMAT2, localeID) ?: ""
        } else {
            ""
        }
        val dailyBudget: Float = if (limitBudgetSwitch.isChecked) {
            budgetCost.textFieldInput.text.toString().removeCommaRawString().toFloatOrZero()
        } else
            0.0F
        return TopAdsManageHeadlineInput().apply {
            source = HEADLINE_SOURCE
            operation = TopAdsManageHeadlineInput.Operation(
                    action = ACTION_CREATE,
                    group = TopAdsManageHeadlineInput.Operation.Group(
                            id = "0",
                            shopID = userSession.shopId,
                            name = stepperModel?.groupName ?: "",
                            priceBid = advertisingCost.textFieldInput.text.toString().removeCommaRawString().toFloatOrZero(),
                            dailyBudget = dailyBudget,
                            scheduleStart = scheduleStart,
                            scheduleEnd = scheduleEnd,
                            adOperations = stepperModel?.adOperations ?: emptyList(),
                            keywordOperations = stepperModel?.keywordOperations ?: emptyList()
                    )
            )
        }
    }

    private fun openPreviewBottomSheet() {
        val previewBottomSheet = HeadlinePreviewBottomSheet.newInstance(stepperModel?.groupName
                ?: "",
                stepperModel?.cpmModel)
        previewBottomSheet.show(childFragmentManager, "")
    }

    private fun setMinBid() {
        stepperModel?.minBid?.let {
            val cost = convertToCurrency(it.toLong())
            advertisingCost.textFieldInput.setText(cost)
            advertisingCost.textFieldInput.addTextChangedListener(advertisingCostTextWatcher())
            val budget = it * MULTIPLIER
            stepperModel?.dailyBudget = budget
            budgetCost.textFieldInput.setText(convertToCurrency(budget.toLong()))
            budgetCost.textFieldInput.addTextChangedListener(budgetCostTextWatcher())
            budgetCostMessage.text = getString(R.string.topads_headline_schedule_budget_cost_message, budget)
        }
    }

    private fun advertisingCostTextWatcher(): NumberTextWatcher? {
        return object : NumberTextWatcher(advertisingCost.textFieldInput, "0") {
            override fun onNumberChanged(number: Double) {
                super.onNumberChanged(number)
                val input = number.toInt()
                when {
                    input < stepperModel?.minBid ?: 0 -> {
                        advertisingCost.setError(true)
                        advertisingCost.setMessage(String.format(getString(R.string.topads_headline_min_budget_cost_error), convertToCurrency(stepperModel?.minBid?.toLong()
                                ?: 0)))
                        btnNext.isEnabled = false
                    }
                    input > stepperModel?.maxBid ?: 0 -> {
                        advertisingCost.setError(true)
                        advertisingCost.setMessage(String.format(getString(R.string.topads_headline_max_budget_cost_error), convertToCurrency(stepperModel?.maxBid?.toLong()
                                ?: 0)))
                        btnNext.isEnabled = false
                    }
                    else -> {
                        stepperModel?.dailyBudget = input * MULTIPLIER
                        btnNext.isEnabled = true
                        advertisingCost.setMessage("")
                        advertisingCost.setError(false)
                        budgetCost.textFieldInput.setText(convertToCurrency(stepperModel?.dailyBudget?.toLong()
                                ?: 0))
                    }
                }
            }
        }
    }

    private fun budgetCostTextWatcher(): NumberTextWatcher? {
        return object : NumberTextWatcher(budgetCost.textFieldInput, "0") {
            override fun onNumberChanged(number: Double) {
                super.onNumberChanged(number)
                val input = number.toInt()
                budgetCostMessage.text = getString(R.string.topads_headline_schedule_budget_cost_message, convertToCurrency(input.toLong()))
                val minBid = if (advertisingCost.isTextFieldError) {
                    stepperModel?.minBid ?: 0
                } else {
                    advertisingCost.textFieldInput.text.toString().removeCommaRawString().toIntOrZero()
                }
                val maxDailyBudget = MAX_DAILY_BUDGET.removeCommaRawString().toIntOrZero()
                if (input < minBid * MULTIPLIER && budgetCost.isVisible) {
                    budgetCost.setError(true)
                    budgetCost.setMessage(String.format(getString(R.string.topads_headline_min_budget_cost_error), convertToCurrency(stepperModel?.dailyBudget?.toLong()
                            ?: 0)))
                    btnNext.isEnabled = false
                } else if (input > maxDailyBudget) {
                    budgetCost.setError(true)
                    budgetCost.setMessage(String.format(getString(R.string.topads_headline_max_budget_cost_error), MAX_DAILY_BUDGET))
                    btnNext.isEnabled = false
                } else {
                    stepperModel?.dailyBudget = input
                    btnNext.isEnabled = true
                    budgetCost.setMessage("")
                    budgetCost.setError(false)
                }
            }
        }
    }

    private fun setUpLimitView() {
        limitBudgetSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                budgetWarningMessage.hide()
                budgetCost.show()
                hari.show()
                budgetCostMessage.show()
                btnNext.isEnabled = !budgetCost.isTextFieldError
            } else {
                budgetWarningMessage.show()
                budgetCost.hide()
                hari.hide()
                budgetCostMessage.hide()
                btnNext.isEnabled = !advertisingCost.isTextFieldError
            }
        }
    }

    private fun setUpScheduleView() {
        startDate.textFieldInput.isFocusable = false
        endDate.textFieldInput.isFocusable = false
        val padding = resources.getDimensionPixelSize(R.dimen.dp_8)
        startDate.textFieldIcon1.setPadding(padding, padding, padding, padding)
        endDate.textFieldIcon1.setPadding(padding, padding, padding, padding)
        context?.run {
            setDate()
            startDate.textFieldInput.setOnClickListener {
                openSetStartDateTimePicker(getString(R.string.topads_headline_start_date_header), "", getToday(),
                        getSpecifiedDateFromToday(years = 50), this@AdScheduleAndBudgetFragment::onStartDateChanged)
            }
            endDate.textFieldInput.setOnClickListener {
                getSpecifiedDateFromStartDate(selectedStartDate as? GregorianCalendar, month = 1)?.let { it1 ->
                    openSetStartDateTimePicker(getString(R.string.topads_headline_start_date_header), getString(R.string.topads_headline_end_date_info),
                            it1, getSpecifiedDateFromToday(years = 50), this@AdScheduleAndBudgetFragment::onEndDateChanged)
                }
            }
        }
        adScheduleSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                startDate.show()
                endDate.show()
                adAppearanceMessage.show()
            } else {
                startDate.hide()
                endDate.hide()
                adAppearanceMessage.hide()
            }
        }
        setAdAppearanceMessage()
    }

    private fun setDate() {
        context?.run {
            if (selectedStartDate == null || selectedEndDate == null) {
                selectedStartDate = getToday()
                selectedEndDate = getSpecifiedDateFromToday(month = 1)
                val startDateString = getToday().time.toFormattedString(HEADLINE_DATETIME_FORMAT1, localeID)
                startDate.textFieldInput.setText(startDateString)
                val endDateString = getSpecifiedDateFromToday(month = 1).time.toFormattedString(HEADLINE_DATETIME_FORMAT1, localeID)
                endDate.textFieldInput.setText(endDateString)
            } else {
                startDate.textFieldInput.setText(selectedStartDate?.time?.toFormattedString(HEADLINE_DATETIME_FORMAT1, localeID))
                endDate.textFieldInput.setText(selectedEndDate?.time?.toFormattedString(HEADLINE_DATETIME_FORMAT1, localeID))
            }
        }
    }

    private fun setAdAppearanceMessage() {
        adAppearanceMessage.text = String.format(getString(R.string.topads_headline_schedule_add_appearance_message), getTimeSelected()?.toInt())
    }

    private fun onStartDateChanged(calendar: Calendar) {
        val startDateString = calendar.time.toFormattedString(HEADLINE_DATETIME_FORMAT1, localeID)
        startDate.textFieldInput.setText(startDateString)
        selectedStartDate = calendar
        setAdAppearanceMessage()
    }

    private fun onEndDateChanged(calendar: Calendar) {
        val endDateString = calendar.time.toFormattedString(HEADLINE_DATETIME_FORMAT1, localeID)
        endDate.textFieldInput.setText(endDateString)
        selectedEndDate = calendar
        setAdAppearanceMessage()
    }

    private fun openSetStartDateTimePicker(header: String, info: String, minDate: GregorianCalendar, maxDate: GregorianCalendar, onDateChanged: (Calendar) -> Unit) {
        context?.run {
            val startDateTimePicker = DateTimePickerUnify(this, minDate, getToday(), maxDate, null,
                    DateTimePickerUnify.TYPE_DATETIMEPICKER).apply {
                setTitle(header)
                if (info.isNotEmpty()) {
                    setInfo(info)
                    setInfoVisible(true)
                } else {
                    setInfoVisible(false)
                }
                minuteInterval = MINUTE_INTERVAL
                datePickerButton.let { button ->
                    button.text = this@run.getString(R.string.topads_headline_date_picker_calendar_button)
                    button.setOnClickListener {
                        onDateChanged.invoke(getDate())
                        dismiss()
                    }
                }
            }
            startDateTimePicker.show(childFragmentManager, "")
        }
    }

    override fun updateToolBar() {
        if (activity is HeadlineStepperActivity) {
            (activity as HeadlineStepperActivity).updateToolbarTitle(getString(R.string.topads_headline_ad_schedule_and_budget_fragment_label))
        }
    }

    override fun getScreenName(): String {
        return AdScheduleAndBudgetFragment::class.java.simpleName
    }

    override fun initInjector() {
        DaggerHeadlineAdsComponent.builder().baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
                .build().inject(this)
    }

    private fun setUpToolTip() {
        val tooltipView = layoutInflater.inflate(com.tokopedia.topads.common.R.layout.tooltip_custom_view, null).apply {
            val tvToolTipText = this.findViewById<Typography>(R.id.tooltip_text)
            tvToolTipText?.text = getString(R.string.topads_headline_schedule_tooltip_text)
            val imgTooltipIcon = this.findViewById<ImageUnify>(R.id.tooltip_icon)
            imgTooltipIcon?.setImageDrawable(context?.getResDrawable(R.drawable.topads_ic_tips))
        }
        tooltipBtn.addItem(tooltipView)
        tooltipBtn.setOnClickListener {
            val tipsList: ArrayList<TipsUiModel> = ArrayList()
            tipsList.apply {
                add(TipsUiHeaderModel(R.string.topads_headline_tips_schedule_header))
                add(TipsUiRowModel(R.string.topads_headline_tips_schedule_row1))
                add(TipsUiHeaderModel(R.string.topads_headline_tips_budget_header))
                add(TipsUiRowModel(R.string.topads_headline_tips_budget_row1, R.drawable.topads_create_ic_checklist))
                add(TipsUiRowModel(R.string.topads_headline_tips_budget_row2, R.drawable.topads_create_ic_checklist))
                add(TipsUiRowModel(R.string.topads_headline_tips_budget_row3, R.drawable.topads_create_ic_checklist))
            }
            val tipsListSheet = context?.let { it1 -> TipsListSheet.newInstance(it1, tipsList = tipsList) }
            tipsListSheet?.show(childFragmentManager, "")
        }
    }

}

