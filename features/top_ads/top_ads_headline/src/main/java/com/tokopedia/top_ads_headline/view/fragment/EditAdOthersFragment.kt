package com.tokopedia.top_ads_headline.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.kotlin.extensions.convertToDate
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.top_ads_headline.R
import com.tokopedia.top_ads_headline.data.HeadlineAdStepperModel
import com.tokopedia.top_ads_headline.di.DaggerHeadlineAdsComponent
import com.tokopedia.top_ads_headline.view.activity.SaveButtonState
import com.tokopedia.top_ads_headline.view.viewmodel.EditAdOthersViewModel
import com.tokopedia.top_ads_headline.view.viewmodel.SharedEditHeadlineViewModel
import com.tokopedia.topads.common.data.util.DateTimeUtils.getSpecifiedDateFromStartDate
import com.tokopedia.topads.common.data.util.DateTimeUtils.getSpecifiedDateFromToday
import com.tokopedia.topads.common.data.util.DateTimeUtils.getToday
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.common.data.util.Utils.removeCommaRawString
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiHeaderModel
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiModel
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiRowModel
import com.tokopedia.topads.common.view.sheet.TipsListSheet
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.text.currency.NumberTextWatcher
import kotlinx.android.synthetic.main.fragment_edit_ad_others.*
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class EditAdOthersFragment : BaseDaggerFragment() {

    private val localeID = Locale(LANGUAGE_ID, COUNTRY_ID)
    private var selectedStartDate: Calendar? = null
    private var selectedEndDate: Calendar? = null

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var editAdOthersViewModel: EditAdOthersViewModel

    private var sharedEditHeadlineViewModel: SharedEditHeadlineViewModel? = null

    private var stepperModel: HeadlineAdStepperModel? = null

    private val saveButtonState by lazy {
        activity as? SaveButtonState
    }

    override fun getScreenName(): String {
        return EditAdOthersFragment::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editAdOthersViewModel = ViewModelProvider(this, viewModelFactory).get(EditAdOthersViewModel::class.java)
        activity?.let {
            sharedEditHeadlineViewModel = ViewModelProvider(it, viewModelFactory).get(SharedEditHeadlineViewModel::class.java)
        }
    }

    override fun initInjector() {
        DaggerHeadlineAdsComponent.builder().baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
                .build().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit_ad_others, container, false)
    }

    companion object {
        fun newInstance(): EditAdOthersFragment = EditAdOthersFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        budgetCost.textFieldInput.isSaveEnabled = false
        startDate.textFieldInput.isSaveEnabled = false
        endDate.textFieldInput.isSaveEnabled = false
        setUpObservers()
        setUpAdScheduleView()
        setUpLimitView()
        setUpToolTip()
    }

    private fun setUpAdScheduleView() {
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
    }

    private fun setUpObservers() {
        sharedEditHeadlineViewModel?.getEditHeadlineAdLiveData()?.observe(viewLifecycleOwner, Observer {
            stepperModel = it
            setUpAdNameEditText()
            setUpScheduleView()
            setMinBid()
        })
    }

    fun setDailyBudget(minBid: Double) {
        val budget: Long = (minBid * MULTIPLIER).toLong()
        stepperModel?.dailyBudget = budget.toFloat()
        budgetCost.textFieldInput.setText(Utils.convertToCurrency(budget))
    }

    private fun setMinBid() {
        stepperModel?.let { stepperModel ->
            val budget: Long
            when {
                stepperModel.dailyBudget != 0F -> {
                    budget = stepperModel.dailyBudget.toLong()
                    limitBudgetSwitch.isChecked = true
                }
                stepperModel.adBidPrice != 0.0 -> {
                    budget = stepperModel.adBidPrice.toLong() * MULTIPLIER.toLong()
                }
                else -> {
                    budget = (stepperModel.minBid.toLong() * MULTIPLIER)
                }
            }
            budgetCost.textFieldInput.setText(Utils.convertToCurrency(budget))
            budgetCost.textFieldInput.addTextChangedListener(budgetCostTextWatcher())
            budgetCostMessage.text = getString(R.string.topads_headline_schedule_budget_cost_message, budget.toString())
        }
    }

    private fun budgetCostTextWatcher(): NumberTextWatcher? {
        return object : NumberTextWatcher(budgetCost.textFieldInput, "0") {
            override fun onNumberChanged(number: Double) {
                super.onNumberChanged(number)
                budgetCostMessage.text = getString(R.string.topads_headline_schedule_budget_cost_message, Utils.convertToCurrency(number.toLong()))
                val minBid: String = stepperModel?.minBid ?: "0"
                var minBudget = (stepperModel?.currentBid ?: 0.0) * MULTIPLIER
                if (minBid.toDouble() > stepperModel?.currentBid ?: 0.0) {
                    minBudget = minBid.toDouble() * MULTIPLIER
                }
                val maxDailyBudget = MAX_DAILY_BUDGET.removeCommaRawString().toFloatOrZero()
                if (number < minBudget && budgetCost.isVisible) {
                    budgetCost.setError(true)
                    budgetCost.setMessage(String.format(getString(R.string.topads_headline_min_budget_cost_error), Utils.convertToCurrency(minBudget.toLong())))
                    saveButtonState?.setButtonState(false)
                } else if (number > maxDailyBudget) {
                    budgetCost.setError(true)
                    budgetCost.setMessage(String.format(getString(R.string.topads_headline_max_budget_cost_error), MAX_DAILY_BUDGET))
                    saveButtonState?.setButtonState(false)
                } else {
                    stepperModel?.dailyBudget = number.toFloat()
                    budgetCost.setMessage("")
                    budgetCost.setError(false)
                    saveButtonState?.setButtonState(true)
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
            } else {
                budgetWarningMessage.show()
                budgetCost.hide()
                hari.hide()
                budgetCostMessage.hide()
            }
        }
    }

    private fun setUpScheduleView() {
        if (stepperModel?.startDate?.isNotBlank() == true && stepperModel?.endDate?.isNotBlank() == true) {
            adScheduleSwitch.isChecked = true
            stepperModel?.startDate?.convertToDate(HEADLINE_DATETIME_FORMAT2, localeID)?.let {
                selectedStartDate = Calendar.getInstance()
                selectedStartDate?.time = it
            }
            stepperModel?.endDate?.convertToDate(HEADLINE_DATETIME_FORMAT2, localeID)?.let {
                selectedEndDate = Calendar.getInstance()
                selectedEndDate?.time = it
            }
        }
        startDate.textFieldInput.isFocusable = false
        endDate.textFieldInput.isFocusable = false
        val padding = resources.getDimensionPixelSize(R.dimen.dp_8)
        startDate.textFieldIcon1.setPadding(padding, padding, padding, padding)
        endDate.textFieldIcon1.setPadding(padding, padding, padding, padding)
        context?.run {
            setDate()
            startDate.textFieldInput.setOnClickListener {
                (selectedEndDate as? GregorianCalendar)?.let { selectedEndDate ->
                    openSetDateTimePicker(getString(R.string.topads_headline_start_date_header), "", getToday(), selectedStartDate as GregorianCalendar,
                            selectedEndDate, this@EditAdOthersFragment::onStartDateChanged)
                }
            }
            endDate.textFieldInput.setOnClickListener {
                getSpecifiedDateFromStartDate(selectedStartDate as? GregorianCalendar)?.let { it1 ->
                    (selectedEndDate as? GregorianCalendar)?.let { it2 ->
                        openSetDateTimePicker(getString(R.string.topads_headline_end_date_header), getString(R.string.topads_headline_end_date_info),
                                it1, it2, getSpecifiedDateFromToday(years = 50), this@EditAdOthersFragment::onEndDateChanged)
                    }
                }
            }
        }
        setAdAppearanceMessage()
    }

    private fun setDate() {
        context?.run {
            if (selectedStartDate == null) {
                selectedStartDate = getToday()
                val startDateString = getToday().time.toFormattedString(HEADLINE_DATETIME_FORMAT1, localeID)
                startDate.textFieldInput.setText(startDateString)
            } else {
                startDate.textFieldInput.setText(selectedStartDate?.time?.toFormattedString(HEADLINE_DATETIME_FORMAT1, localeID))
                selectedEndDate = getSpecifiedDateFromStartDate(selectedStartDate as? GregorianCalendar, month = 1)
            }
            if (selectedEndDate == null) {
                selectedEndDate = getSpecifiedDateFromStartDate(month = 1, startCalendar = selectedStartDate as GregorianCalendar?)
                val endDateString = selectedEndDate?.time?.toFormattedString(HEADLINE_DATETIME_FORMAT1, localeID)
                endDate.textFieldInput.setText(endDateString)
            } else {
                endDate.textFieldInput.setText(selectedEndDate?.time?.toFormattedString(HEADLINE_DATETIME_FORMAT1, localeID))
            }
        }
    }

    private fun setAdAppearanceMessage() {
        adAppearanceMessage.text = String.format(getString(R.string.topads_headline_schedule_add_appearance_message), getTimeSelected()?.toInt())
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

    private fun openSetDateTimePicker(header: String, info: String, minDate: GregorianCalendar, defaultDate: GregorianCalendar,
                                      maxDate: GregorianCalendar, onDateChanged: (Calendar) -> Unit) {
        context?.run {
            val startDateTimePicker = DateTimePickerUnify(this, minDate, defaultDate, maxDate, null,
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

    private fun setUpAdNameEditText() {
        headlineAdNameInput?.textFieldInput?.setText(MethodChecker.fromHtml(stepperModel?.groupName))
        headlineAdNameInput?.textFieldInput?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                headlineAdNameInput?.setError(false)
                if (s.toString().isBlank()) {
                    headlineAdNameInput?.getFirstIcon()?.hide()
                } else {
                    headlineAdNameInput?.getFirstIcon()?.show()
                }
            }
        })
        headlineAdNameInput?.textFieldInput?.setOnEditorActionListener { v, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> validateGroup(v?.text.toString())
            }
            Utils.dismissKeyboard(context, v)
            true
        }
        headlineAdNameInput?.getFirstIcon()?.setOnClickListener {
            headlineAdNameInput?.textFieldInput?.setText("")
            it.hide()
        }
    }

    private fun validateGroup(s: String?) {
        s?.let {
            editAdOthersViewModel.validateGroup(it, this::onSuccess, this::onError)
        }
    }

    private fun onError(errorMsg: String) {
        errorTextVisibility(true)
        headlineAdNameInput?.setMessage(errorMsg)
    }

    private fun onSuccess() {
        errorTextVisibility(false)
    }

    private fun errorTextVisibility(visible: Boolean) {
        saveButtonState?.setButtonState(!visible)
        headlineAdNameInput?.setError(visible)
    }

    fun onClickSubmit() {
        stepperModel?.groupName = headlineAdNameInput?.textFieldInput?.text.toString()
        stepperModel?.startDate = if (adScheduleSwitch.isChecked) {
            selectedStartDate?.time?.toFormattedString(HEADLINE_DATETIME_FORMAT2, localeID) ?: ""
        } else {
            ""
        }
        stepperModel?.endDate = if (adScheduleSwitch.isChecked) {
            selectedEndDate?.time?.toFormattedString(HEADLINE_DATETIME_FORMAT2, localeID) ?: ""
        } else {
            ""
        }
        stepperModel?.dailyBudget = if (limitBudgetSwitch.isChecked) {
            budgetCost.textFieldInput.text.toString().removeCommaRawString().toFloatOrZero()
        } else
            0.0F
        stepperModel?.let { sharedEditHeadlineViewModel?.saveOtherDetails(it) }
    }

}