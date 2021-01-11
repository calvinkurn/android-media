package com.tokopedia.sellerorder.common.util

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.LightingColorFilter
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import android.text.Spanned
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.SomConsts.PATTERN_DATE_PARAM
import com.tokopedia.sellerorder.common.util.SomConsts.UNIFY_TICKER_TYPE_ANNOUNCEMENT
import com.tokopedia.sellerorder.common.util.SomConsts.UNIFY_TICKER_TYPE_ERROR
import com.tokopedia.sellerorder.common.util.SomConsts.UNIFY_TICKER_TYPE_INFO
import com.tokopedia.sellerorder.common.util.SomConsts.UNIFY_TICKER_TYPE_WARNING
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterUiModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by fwidjaja on 2019-11-21.
 */
object Utils {
    @JvmStatic
    fun showToasterError(message: String, view: View?) {
        val toasterError = Toaster
        view?.let { v ->
            toasterError.make(v, message, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR, SomConsts.ACTION_OK)
        }
    }

    @JvmStatic
    fun createUserNotAllowedDialog(context: Context): DialogUnify {
        context.run {
            return DialogUnify(this, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.dialog_title_cannot_access_page))
                setDescription(getString(R.string.dialog_description_cannot_access_page))
                setPrimaryCTAText(getString(R.string.button_understand))
                setPrimaryCTAClickListener {
                    goToHome(this@run)
                    dismiss()
                }

                setCancelable(false)
                setCanceledOnTouchOutside(false)
            }
        }
    }

    private fun goToHome(context: Context) {
        context.run {
            RouteManager.getIntent(context, ApplinkConst.HOME).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                startActivity(this)
            }
        }
    }

    @JvmStatic
    fun mapStringTickerTypeToUnifyTickerType(typeString: String): Int {
        return when (typeString) {
            UNIFY_TICKER_TYPE_ANNOUNCEMENT -> Ticker.TYPE_ANNOUNCEMENT
            UNIFY_TICKER_TYPE_INFO -> Ticker.TYPE_INFORMATION
            UNIFY_TICKER_TYPE_WARNING -> Ticker.TYPE_WARNING
            UNIFY_TICKER_TYPE_ERROR -> Ticker.TYPE_ERROR
            else -> Ticker.TYPE_ANNOUNCEMENT
        }
    }

    @JvmStatic
    fun getL2CancellationReason(text: String, textToPrepend: String = ""): String {
        return if (text.contains('-')) {
            "$textToPrepend ${text.split(" - ").last().decapitalize()}"
        } else {
            text
        }
    }

    fun getColoredIndicator(context: Context, colorHex: String): Drawable? {
        val color = if (colorHex.length > 1) Color.parseColor(colorHex)
        else MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0)
        val drawable = MethodChecker.getDrawable(context, R.drawable.ic_order_status_indicator)
        val filter: ColorFilter = LightingColorFilter(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Static_Black), color)
        drawable.colorFilter = filter
        return drawable
    }

    fun getLocale(): Locale {
        return Locale("id")
    }

    fun format(timeMillis: Long, pattern: String, locale: Locale = getLocale()): String {
        val sdf = SimpleDateFormat(pattern, locale)
        return sdf.format(timeMillis)
    }

    fun Long.formatDate(pattern: String, locale: Locale = getLocale()): String {
        val sdf = SimpleDateFormat(pattern, locale)
        return sdf.format(this)
    }

    fun getNPastYearTimeStamp(yearBefore: Int): Date {
        val date = Calendar.getInstance(getLocale())
        date.set(Calendar.YEAR, date.get(Calendar.YEAR) - yearBefore)
        return date.time
    }

    private fun getNPastMonthTimeStamp(monthBefore: Int): Long {
        val date = Calendar.getInstance(getLocale())
        date.set(Calendar.MONTH, date.get(Calendar.MONTH) - monthBefore)
        return date.timeInMillis
    }

    fun getNowTimeStamp(): Long {
        val date = Calendar.getInstance(getLocale())
        return date.timeInMillis
    }

    fun getNPastMonthTimeText(monthBefore: Int): String {
        val pastTwoYear = getNPastMonthTimeStamp(monthBefore)
        return format(pastTwoYear, PATTERN_DATE_PARAM)
    }

    fun List<SomFilterUiModel>.copyListParcelable(): List<SomFilterUiModel> {
        return this.mapNotNull { it.copyParcelable() }
    }

    fun <T: Parcelable> T.copyParcelable(): T? {
        var parcel: Parcel? = null

        return try {
            parcel = Parcel.obtain()
            parcel.writeParcelable(this, 0)
            parcel.setDataPosition(0)
            parcel.readParcelable(this::class.java.classLoader)
        } catch(throwable: Throwable) {
            null
        } finally {
            parcel?.recycle()
        }
    }

    fun List<Int>.copyInt(): List<Int> {
        return this.map { it }
    }

    fun getFormattedDate(daysBefore: Long, format: String) = format(getNPastDaysTimestamp(daysBefore), format)

    fun String.toStringFormatted(maxChar: Int): Spanned {
        return if (MethodChecker.fromHtml(this).toString().length > maxChar) {
            val subDescription = MethodChecker.fromHtml(this).toString().substring(0, maxChar)
            MethodChecker.fromHtml("$subDescription...")
        } else {
            MethodChecker.fromHtml(this)
        }
    }
}