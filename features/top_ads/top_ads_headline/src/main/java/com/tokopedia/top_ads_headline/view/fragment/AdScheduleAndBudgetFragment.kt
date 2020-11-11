package com.tokopedia.top_ads_headline.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.datepicker.OnDateChangedListener
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.top_ads_headline.R
import com.tokopedia.top_ads_headline.data.CreateHeadlineAdsStepperModel
import com.tokopedia.top_ads_headline.di.DaggerHeadlineAdsComponent
import com.tokopedia.top_ads_headline.view.activity.HeadlineStepperActivity
import com.tokopedia.top_ads_headline.view.sheet.HeadlinePreviewBottomSheet
import com.tokopedia.top_ads_headline.view.viewmodel.AdScheduleAndBudgetViewModel
import com.tokopedia.topads.common.data.util.DateTImeUtils.getSpecifiedDateFromStartDate
import com.tokopedia.topads.common.data.util.DateTImeUtils.getSpecifiedDateFromToday
import com.tokopedia.topads.common.data.util.DateTImeUtils.getToday
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiHeaderModel
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiModel
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiRowModel
import com.tokopedia.topads.common.view.sheet.TipsListSheet
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.fragment_ad_schedule_and_budget.*
import kotlinx.android.synthetic.main.fragment_topads_product_list.tooltipBtn
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

private const val COUNTRY_ID = "ID"
private const val LANGUAGE_ID = "in"
private const val HEADLINE_DATETIME_FORMAT = "dd MMM yyyy, HH:mm"
private const val MINUTE_INTERVAL = 30

class AdScheduleAndBudgetFragment : BaseHeadlineStepperFragment<CreateHeadlineAdsStepperModel>() {

    private val localeID = Locale(LANGUAGE_ID, COUNTRY_ID)
    private var selectedStartDate: GregorianCalendar? = context?.getToday()

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
        setMinBid()
        setUpToolTip()
        setUpScheduleView()
        setUpLimitView()
        previewBtn.setOnClickListener {
            openPreviewBottomSheet()
        }
    }

    private fun openPreviewBottomSheet() {
        val previewBottomSheet = context?.let { it1 ->
            HeadlinePreviewBottomSheet.newInstance(stepperModel?.groupName ?: "",
                    stepperModel?.cpmModel)
        }
        previewBottomSheet?.show(fragmentManager!!, "")
    }

    private fun setMinBid() {
        stepperModel?.minBid?.let {
            val cost = Utils.convertToCurrency(it.toLong())
            advertisingCost.textFieldInput.setText(cost)
            val budget = it * 3
            budgetCost.textFieldInput.setText(Utils.convertToCurrency(budget.toLong()))
            budgetCostMessage.text = getString(R.string.topads_headline_schedule_budget_cost_message, budget)
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
        startDate.textFieldInput.isFocusable = false
        endDate.textFieldInput.isFocusable = false
        val padding = resources.getDimensionPixelSize(R.dimen.dp_8)
        startDate.textFieldIcon1.setPadding(padding, padding, padding, padding)
        endDate.textFieldIcon1.setPadding(padding, padding, padding, padding)
        context?.run {
            val startDateString = getToday().time.toFormattedString(HEADLINE_DATETIME_FORMAT, localeID)
            startDate.textFieldInput.setText(startDateString)
            val endDateString = getSpecifiedDateFromToday(month = 1).time.toFormattedString(HEADLINE_DATETIME_FORMAT, localeID)
            endDate.textFieldInput.setText(endDateString)
            startDate.textFieldInput.setOnClickListener {
                openSetStartDateTimePicker(getString(R.string.topads_headline_start_date_header), "", getToday(), getSpecifiedDateFromToday(years = 50), startDateListener)
            }
            endDate.textFieldInput.setOnClickListener {
                getSpecifiedDateFromStartDate(selectedStartDate)?.let { it1 ->
                    openSetStartDateTimePicker(getString(R.string.topads_headline_start_date_header), getString(R.string.topads_headline_end_date_info),
                            it1, getSpecifiedDateFromToday(years = 50), endDateListener)
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
    }

    private val startDateListener = object : OnDateChangedListener {
        override fun onDateChanged(date: Long) {
            val calendar = GregorianCalendar.getInstance()
            calendar.timeInMillis = date
            val startDateString = calendar.time.toFormattedString(HEADLINE_DATETIME_FORMAT, localeID)
            startDate.textFieldInput.setText(startDateString)
            selectedStartDate = calendar as GregorianCalendar
        }
    }

    private val endDateListener = object : OnDateChangedListener {
        override fun onDateChanged(date: Long) {
            val calendar = GregorianCalendar.getInstance()
            calendar.timeInMillis = date
            val endDateString = calendar.time.toFormattedString(HEADLINE_DATETIME_FORMAT, localeID)
            endDate.textFieldInput.setText(endDateString)
        }
    }

    private fun openSetStartDateTimePicker(header: String, info: String, minDate: GregorianCalendar, maxDate: GregorianCalendar, onDateChanged: OnDateChangedListener) {
        context?.run {
            val startDateTimePicker = DateTimePickerUnify(this, minDate, getToday(), maxDate, onDateChanged,
                    DateTimePickerUnify.TYPE_DATETIMEPICKER).apply {
                setTitle(header)
                if (info.isNotEmpty()) {
                    setInfo(info)
                    setInfoVisible(true)
                } else {
                    setInfoVisible(false)
                }
                minuteInterval = MINUTE_INTERVAL
            }
            startDateTimePicker.show(fragmentManager!!, "")
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
            val tipsListSheet = context?.let { it1 ->
                TipsListSheet.newInstance(it1,
                        tipsList = tipsList)
            }
            tipsListSheet?.showKnob = true
            tipsListSheet?.show(fragmentManager!!, "")
        }
    }

}