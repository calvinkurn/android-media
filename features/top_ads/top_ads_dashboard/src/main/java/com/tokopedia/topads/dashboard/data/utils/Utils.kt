package com.tokopedia.topads.dashboard.data.utils

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.BulletSpan
import android.text.style.StyleSpan
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils.DEFAULT_LOCALE
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.datepicker.range.view.constant.DatePickerConstant
import com.tokopedia.datepicker.range.view.model.PeriodRangeModel
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.common.data.util.Utils.locale
import com.tokopedia.topads.common.data.util.Utils.removeCommaRawString
import com.tokopedia.topads.common.extension.ZERO
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.TopAdsCreditTopUpConstant.TIME_DURATION_FOR_INTERRUPT_SHEET
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.TopAdsCreditTopUpConstant.TOP_ADS_TOP_UP_CREDIT_SP_KEY_NAME
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.TopAdsCreditTopUpConstant.TOP_ADS_TOP_UP_CREDIT_SP_NAME
import com.tokopedia.unifycomponents.SearchBarUnify
import com.tokopedia.unifycomponents.toDp
import java.text.DateFormat
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Pika on 8/6/20.
 */

object Utils {

    val outputFormat: DateFormat = SimpleDateFormat("dd MMM yyyy", locale)
    val format = SimpleDateFormat("yyyy-MM-dd", locale)

    fun Date?.orDefaultStart(): Date {
        return this ?: getStartDate()
    }

    fun Date?.orDefaultEnd(): Date {
        return this ?: getEndDate()
    }

    internal suspend fun <T : Any> executeQuery(
        query: String, responseClass: Class<T>, params: Map<String, Any?>
    ): T? {
        val gql = MultiRequestGraphqlUseCase()
        val request = GraphqlRequest(query, responseClass, params)
        gql.addRequest(request)
        val response = gql.executeOnBackground()
        return response.getData(responseClass) as? T
    }

    fun Context.openWebView(url: String) {
        RouteManager.route(this, ApplinkConstInternalGlobal.WEBVIEW, url)
    }

    fun Date?.asString(): String {
        return if (this == null) ""
        else
            SimpleDateFormat(TopAdsCommonConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(this)
    }

    fun Int.asPercentage(): String {
        val startValue = if (this > 0) "+" else ""
        return "${startValue}${this}%"
    }

    fun setSearchListener(context: Context?, view: View, onSuccess: () -> Unit) {
        val searchbar = view.findViewById<SearchBarUnify>(R.id.searchBar)
        val searchTextField = searchbar?.searchBarTextField
        val searchClearButton = searchbar?.searchBarIcon
        searchTextField?.imeOptions = EditorInfo.IME_ACTION_SEARCH
        searchTextField?.setOnEditorActionListener(object : TextView.OnEditorActionListener {

            override fun onEditorAction(
                textView: TextView?,
                actionId: Int,
                even: KeyEvent?
            ): Boolean {

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
        periodRangeList.add(
            PeriodRangeModel(
                endCalendar.timeInMillis,
                endCalendar.timeInMillis,
                context.getString(com.tokopedia.datepicker.range.R.string.label_today)
            )
        )
        startCalendar.add(Calendar.DATE, -1)
        periodRangeList.add(
            PeriodRangeModel(
                startCalendar.timeInMillis,
                startCalendar.timeInMillis,
                context.getString(com.tokopedia.datepicker.range.R.string.yesterday)
            )
        )
        startCalendar = Calendar.getInstance()
        startCalendar.add(Calendar.DATE, -DatePickerConstant.DIFF_ONE_WEEK)
        periodRangeList.add(
            PeriodRangeModel(
                startCalendar.timeInMillis,
                endCalendar.timeInMillis,
                context.getString(com.tokopedia.datepicker.range.R.string.seven_days_ago)
            )
        )
        startCalendar = Calendar.getInstance()
        startCalendar.add(Calendar.DATE, -DatePickerConstant.DIFF_ONE_MONTH)
        periodRangeList.add(
            PeriodRangeModel(
                startCalendar.timeInMillis,
                endCalendar.timeInMillis,
                context.getString(com.tokopedia.datepicker.range.R.string.thirty_days_ago)
            )
        )
        startCalendar = Calendar.getInstance()
        startCalendar.set(Calendar.DATE, 1)
        periodRangeList.add(
            PeriodRangeModel(
                startCalendar.timeInMillis,
                endCalendar.timeInMillis,
                context.getString(com.tokopedia.datepicker.range.R.string.label_this_month)
            )
        )
        periodRangeList.add(
            PeriodRangeModel(
                endCalendar.timeInMillis,
                endCalendar.timeInMillis,
                context.getString(R.string.topads_dash_date_custom)
            )
        )
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
        val inputMethodManager =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        if (inputMethodManager?.isAcceptingText == true)
            inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    fun getStartDate(): Date {
        val startCalendar = Calendar.getInstance()
        startCalendar.add(Calendar.DAY_OF_YEAR, -DatePickerConstant.DIFF_ONE_WEEK)
        return startCalendar.time
    }

    fun getEndDate(): Date {
        val endCalendar = Calendar.getInstance()
        return endCalendar.time
    }


    fun convertToCurrencyString(value: Long): String {
        return (NumberFormat.getNumberInstance(locale).format(value))
    }

    fun convertMoneyToValue(price: String): Int {
        return price.replace("Rp", "").replace(".", "").replace(",", "").trim().toIntOrZero()
    }

    fun calculatePercentage(number: String, percent: Double): Double {
        val price = number.removeCommaRawString()
        var result = 0.0
        if (price.isNotEmpty())
            result = (price.toIntOrZero() * percent) / 100
        return result
    }

    fun getSpannableForTips(
        context: Context,
        autoTopUpMaxCreditLimit: Long
    ): SpannableStringBuilder {
        val spannableOne =
            SpannableString(context.getString(R.string.top_ads_auto_top_up_tips_bullet_one))
        val spannableTwo =
            SpannableString(" Rp${convertToCurrencyString(autoTopUpMaxCreditLimit)}.\n")
        val spannableThree =
            SpannableString(context.getString(R.string.top_ads_auto_top_up_tips_bullet_two))

        spannableOne.setSpan(
            BulletSpan(12.toDp(), ContextCompat.getColor(
                context,
                com.tokopedia.unifyprinciples.R.color.Unify_Static_Black,
            )),
            Int.ZERO, spannableOne.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableTwo.setSpan(
            StyleSpan(Typeface.BOLD),
            Int.ZERO,
            spannableTwo.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableThree.setSpan(
            BulletSpan(12.toDp(),
                ContextCompat.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_Static_Black,
                )),

            Int.ZERO, spannableTwo.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        val spn = SpannableStringBuilder()
        spn.append(spannableOne)
        spn.append(spannableTwo)
        spn.append(spannableThree)
        return spn
    }

    fun getTncSpannable(context: Context, minCreditFmt: String): SpannableStringBuilder {
        val prefix =
            SpannableString(context.getString(R.string.top_ads_auto_top_up_tnc_prefix))
        val minCredit =
            SpannableString(" $minCreditFmt ")
        val suffix = SpannableString(context.getString(R.string.top_ads_auto_top_up_tnc_suffix))
        minCredit.setSpan(
            StyleSpan(Typeface.BOLD),
            Int.ZERO,
            minCredit.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        val spn = SpannableStringBuilder()
        spn.append(prefix)
        spn.append(minCredit)
        spn.append(suffix)
        return spn
    }

    fun isShowInterruptSheet(context: Context): Boolean {
        val sharedPref =
            context.getSharedPreferences(TOP_ADS_TOP_UP_CREDIT_SP_NAME, BaseSimpleActivity.MODE_PRIVATE)
        val storedTime = sharedPref.getLong(TOP_ADS_TOP_UP_CREDIT_SP_KEY_NAME, Long.ZERO)
        if (storedTime == Long.ZERO) {
            storeNewTime(Calendar.getInstance().timeInMillis, sharedPref)
            return true
        } else {
            val currentTime = Calendar.getInstance().timeInMillis
            val timeDiffMillis = currentTime - storedTime
            val days = timeDiffMillis/(1000*60*60*24)
            if (days > TIME_DURATION_FOR_INTERRUPT_SHEET) {
                storeNewTime(currentTime, sharedPref)
                return true
            }
        }
        return false
    }

    private fun storeNewTime(time: Long, sharedPref: SharedPreferences){
        val editor = sharedPref.edit()
        editor.putLong(TOP_ADS_TOP_UP_CREDIT_SP_KEY_NAME, time)
        editor.apply()
    }

    fun getTextFromFrequency(context: Context?, autoTopUpFrequencySelected: Int?): String? {
        return when (autoTopUpFrequencySelected) {
            TopAdsDashboardConstant.TopAdsCreditTopUpConstant.TOP_UP_FREQUENCY_FOUR -> context?.getString(R.string.topads_frequency_four_text)
            TopAdsDashboardConstant.TopAdsCreditTopUpConstant.DEFAULT_TOP_UP_FREQUENCY -> context?.getString(R.string.topads_frequency_six_text)
            TopAdsDashboardConstant.TopAdsCreditTopUpConstant.TOP_UP_FREQUENCY_EIGHT -> context?.getString(R.string.topads_frequency_eight_text)
            else -> context?.getString(R.string.topads_frequency_six_text)
        }
    }
}

