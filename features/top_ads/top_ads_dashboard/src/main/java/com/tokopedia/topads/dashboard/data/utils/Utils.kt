package com.tokopedia.topads.dashboard.data.utils

import android.content.Context
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils.DEFAULT_LOCALE
import com.tokopedia.datepicker.range.view.constant.DatePickerConstant
import com.tokopedia.datepicker.range.view.model.PeriodRangeModel
import com.tokopedia.topads.common.data.util.Utils.locale
import com.tokopedia.topads.common.data.util.Utils.removeCommaRawString
import com.tokopedia.topads.dashboard.R
import com.tokopedia.unifycomponents.SearchBarUnify
import java.text.DateFormat
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Pika on 8/6/20.
 */

object Utils {

    val outputFormat: DateFormat = SimpleDateFormat("dd MMM yyyy", locale)
    val format = SimpleDateFormat("yyyy-MM-dd", locale)

    fun setSearchListener(context: Context?, view: View, onSuccess: () -> Unit) {
        val searchbar = view.findViewById<SearchBarUnify>(R.id.searchBar)
        val searchTextField = searchbar?.searchBarTextField
        val searchClearButton = searchbar?.searchBarIcon
        searchTextField?.imeOptions = EditorInfo.IME_ACTION_SEARCH
        searchTextField?.setOnEditorActionListener(object : TextView.OnEditorActionListener {

            override fun onEditorAction(textView: TextView?, actionId: Int, even: KeyEvent?): Boolean {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    onSuccess.invoke()
                    dismissKeyboard(context, view)
                    return true
                }
                return false
            }
        })
        searchClearButton?.setOnClickListener {
            searchTextField?.text?.clear()
            onSuccess.invoke()
            dismissKeyboard(context, view)
        }
    }

    fun getPeriodRangeList(context: Context): ArrayList<PeriodRangeModel> {
        val periodRangeList = ArrayList<PeriodRangeModel>()
        var startCalendar = Calendar.getInstance()
        val endCalendar = Calendar.getInstance()
        periodRangeList.add(PeriodRangeModel(endCalendar.timeInMillis, endCalendar.timeInMillis, context.getString(com.tokopedia.datepicker.range.R.string.label_today)))
        startCalendar.add(Calendar.DATE, -1)
        periodRangeList.add(PeriodRangeModel(startCalendar.timeInMillis, startCalendar.timeInMillis, context.getString(com.tokopedia.datepicker.range.R.string.yesterday)))
        startCalendar = Calendar.getInstance()
        startCalendar.add(Calendar.DATE, -DatePickerConstant.DIFF_ONE_WEEK)
        periodRangeList.add(PeriodRangeModel(startCalendar.timeInMillis, endCalendar.timeInMillis, context.getString(com.tokopedia.datepicker.range.R.string.seven_days_ago)))
        startCalendar = Calendar.getInstance()
        startCalendar.add(Calendar.DATE, -DatePickerConstant.DIFF_ONE_MONTH)
        periodRangeList.add(PeriodRangeModel(startCalendar.timeInMillis, endCalendar.timeInMillis, context.getString(com.tokopedia.datepicker.range.R.string.thirty_days_ago)))
        startCalendar = Calendar.getInstance()
        startCalendar.set(Calendar.DATE, 1)
        periodRangeList.add(PeriodRangeModel(startCalendar.timeInMillis, endCalendar.timeInMillis, context.getString(com.tokopedia.datepicker.range.R.string.label_this_month)))
        periodRangeList.add(PeriodRangeModel(endCalendar.timeInMillis, endCalendar.timeInMillis, context.getString(R.string.topads_dash_date_custom)))
        return periodRangeList
    }

    @Throws(ParseException::class)
    fun String.stringToDate(format: String): Date {
        val fromFormat = SimpleDateFormat(format, DEFAULT_LOCALE)
        try {
            return fromFormat.parse(this)
        } catch (e: ParseException) {
            e.printStackTrace()
            throw RuntimeException("Date doesnt valid ($this) with format$format")
        }
    }

    fun dismissKeyboard(context: Context?, view: View?) {
        val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        if (inputMethodManager?.isAcceptingText == true)
            inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    fun getStartDate(): Date? {
        val startCalendar = Calendar.getInstance()
        startCalendar.add(Calendar.DAY_OF_YEAR, -DatePickerConstant.DIFF_ONE_WEEK)
        return startCalendar.time


    }

    fun getEndDate(): Date? {
        val endCalendar = Calendar.getInstance()
        return endCalendar.time
    }


    fun convertToCurrencyString(value: Long): String {
        return (NumberFormat.getNumberInstance(locale).format(value))
    }

    fun convertMoneyToValue(price: String): Int {
        return price.replace("Rp", "").replace(".", "").replace(",", "").trim().toInt()
    }

    fun calculatePercentage(number: String, percent: Double): Double {
        val price = number.removeCommaRawString()
        var result = 0.0
        if (price.isNotEmpty())
            result = (price.toInt() * percent) / 100
        return result
    }
}