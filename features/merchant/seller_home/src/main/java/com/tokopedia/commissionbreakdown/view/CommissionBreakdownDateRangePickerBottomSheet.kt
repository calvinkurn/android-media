package com.tokopedia.commissionbreakdown.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.calendar.UnifyCalendar
import com.tokopedia.commissionbreakdown.util.CommissionBreakdownDateUtil
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.sellerhome.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.TextFieldUnify2
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.utils.date.DateUtil
import java.text.SimpleDateFormat
import java.util.*

class CommissionBreakdownDateRangePickerBottomSheet : BottomSheetUnify() {

    private val maxDate = Date()
    private var minDate = Date(System.currentTimeMillis() - TWO_YEAR_MILLIS)

    private var defaultDateFrom: Date? = null
    private var defaultDateTo: Date? = null
    private var newSelectedDateFrom : Date? = Date()
    private var newSelectedDateTO : Date? = Date()
    private var maxRange:Long = MAX_RANGE

    private var dateFromTextField : TextFieldUnify2? = null
    private var dateToTextField :TextFieldUnify2? = null
    private var unifyButtonSelect : UnifyButton? = null
    private var calendarUnify : UnifyCalendar? = null

    lateinit var childView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments?.containsKey(ARG_DATE_FROM) == true
            && arguments?.containsKey(ARG_DATE_TO) == true
        ) {
            defaultDateFrom = arguments?.getSerializable(ARG_DATE_FROM) as Date
            defaultDateTo = arguments?.getSerializable(ARG_DATE_TO) as Date
        } else {
            defaultDateFrom = Date()
            defaultDateTo = Date()
        }
        if (arguments?.containsKey(ARG_MAX_RANGE) == true) {
            arguments?.getLong(ARG_MAX_RANGE)?.let {
                maxRange = it
            }
        }

        if (arguments?.containsKey(ARG_MIN_DATE_GAP) == true) {
            arguments?.getLong(ARG_MIN_DATE_GAP)?.let {
                minDate = Date(System.currentTimeMillis() - it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setDefaultParams()
        childView = View.inflate(context, R.layout.commission_breakdown_bottomsheet_choose_date, null)
        setChild(childView)
        dateFromTextField = childView.findViewById(R.id.commission_range_date_from)
        dateToTextField = childView.findViewById(R.id.commission_range_date_to)
        unifyButtonSelect = childView.findViewById(R.id.unifyButtonSelect)
        calendarUnify = childView.findViewById(R.id.calendar_unify)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childView.layoutParams.height = (getScreenHeight() / BOTTOM_SHEET_HEIGHT_3 * BOTTOM_SHEET_HEIGHT_2)
        initCalender()
        unifyButtonSelect?.setOnClickListener {
            if (newSelectedDateFrom != null && newSelectedDateTO != null &&
                !(CommissionBreakdownDateUtil.isDatesAreSame(newSelectedDateTO, defaultDateTo)
                        && CommissionBreakdownDateUtil.isDatesAreSame(newSelectedDateFrom, defaultDateFrom))) {
                if (parentFragment is OnDateRangeSelectListener) {
                    (parentFragment as OnDateRangeSelectListener)
                        .onDateRangeSelected(newSelectedDateFrom!!, newSelectedDateTO!!)

                }
            }
            dismissAllowingStateLoss()
        }

        dateFromTextField?.isEnabled = false
        dateToTextField?.isEnabled = false
    }

    private fun initCalender() {
        newSelectedDateFrom = defaultDateFrom
        newSelectedDateTO = defaultDateTo
        calendarUnify?.calendarPickerView?.init(minDate, maxDate, arrayListOf())
            ?.inMode(CalendarPickerView.SelectionMode.RANGE)
            ?.maxRange(maxRange)
            ?.withSelectedDates(arrayListOf(defaultDateFrom!!, defaultDateTo!!))
        calendarUnify?.calendarPickerView?.selectDateClickListener()
        calendarUnify?.calendarPickerView?.outOfRange()
    }

    private fun CalendarPickerView.selectDateClickListener() {
        setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {
            override fun onDateSelected(date: Date) {
                val selectedDates = calendarUnify?.calendarPickerView?.selectedDates
                when {
                    selectedDates.isNullOrEmpty() -> {
                        newSelectedDateFrom = null
                        newSelectedDateTO = null
                    }
                    selectedDates.size == 1 -> {
                        newSelectedDateFrom = selectedDates[0]
                        newSelectedDateTO = selectedDates[0]
                    }
                    else -> {
                        newSelectedDateFrom = selectedDates.first()
                        newSelectedDateTO = selectedDates.last()
                    }
                }
                setDatePlaceholder()
            }

            override fun onDateUnselected(date: Date) {

            }
        })
    }

    private fun setDatePlaceholder() {
        val dateFormat = SimpleDateFormat(DATE_PATTERN, DateUtil.DEFAULT_LOCALE)

        dateFromTextField?.apply {
            newSelectedDateFrom?.let {
                this.setPlaceholder(dateFormat.format(it))
            }
        }
        dateToTextField?.apply {
            newSelectedDateTO?.let {
                this.setPlaceholder(dateFormat.format(it))
            }
        }
    }

    private fun CalendarPickerView.outOfRange() {
        setMaxRangeListener(object : CalendarPickerView.OnMaxRangeListener {
            override fun onNotifyMax() {
                showMaxDaysDialog()
            }
        })
    }

    private fun showMaxDaysDialog(){
        DialogUnify(context = this.requireContext(),
            actionType = DialogUnify.SINGLE_ACTION,
            imageType = DialogUnify.NO_IMAGE).apply {
            activity?.let {
                setTitle(it.getString(R.string.saldo_calendar_range_error_title))
                setDescription(it.getString(R.string.sp_title_max_day, maxRange))
                setPrimaryCTAText(it.getString(R.string.saldo_btn_oke))
                setPrimaryCTAClickListener {
                    cancel()
                }
            }
            show()
        }
    }

    private fun setDefaultParams() {
        isDragable = true
        showCloseIcon = true
        showHeader = true
        isHideable = true
        isFullpage = false
        customPeekHeight = (getScreenHeight() / BOTTOM_SHEET_HEIGHT_3 * BOTTOM_SHEET_HEIGHT_2)
        setTitle("Pilih rentang waktu")
    }

    companion object {
        const val TWO_YEAR_MILLIS = (2 * 365 * 24 * 3600 * 1000L)
        const val MAX_RANGE = 30L
        const val MAX_RANGE_90 = 90L
        const val ARG_DATE_FROM = "ARG_DATE_FROM"
        const val ARG_DATE_TO = "ARG_DATE_TO"
        const val ARG_MAX_RANGE = "ARG_MAX_RANGE"
        const val ARG_MIN_DATE_GAP = "ARG_MIN_DATE_GAP"
        const val BOTTOM_SHEET_HEIGHT_3 = 3
        const val BOTTOM_SHEET_HEIGHT_2 = 2
        const val DATE_PATTERN = "dd MMMM yyyy"

        fun getInstanceRange(dateFrom: Date?, dateTo: Date?, range: Long, minDate: Long): CommissionBreakdownDateRangePickerBottomSheet {
            return CommissionBreakdownDateRangePickerBottomSheet().apply {
                val bundle = Bundle()
                bundle.putSerializable(ARG_DATE_FROM, dateFrom ?: Date())
                bundle.putSerializable(ARG_DATE_TO, dateTo ?: Date())
                bundle.putLong(ARG_MAX_RANGE, range)
                bundle.putLong(ARG_MIN_DATE_GAP, minDate)
                arguments = bundle
            }
        }
    }
}

interface OnDateRangeSelectListener {
    fun onDateRangeSelected(dateFrom: Date, dateTo: Date)
}