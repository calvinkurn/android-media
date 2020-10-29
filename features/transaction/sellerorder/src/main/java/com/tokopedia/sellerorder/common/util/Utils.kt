package com.tokopedia.sellerorder.common.util

import android.content.Context
import android.content.Intent
import android.view.View
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.SomConsts.UNIFY_TICKER_TYPE_ANNOUNCEMENT
import com.tokopedia.sellerorder.common.util.SomConsts.UNIFY_TICKER_TYPE_ERROR
import com.tokopedia.sellerorder.common.util.SomConsts.UNIFY_TICKER_TYPE_INFO
import com.tokopedia.sellerorder.common.util.SomConsts.UNIFY_TICKER_TYPE_WARNING
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

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

    fun getLocale(): Locale {
        return Locale("id")
    }

    fun format(timeMillis: Long, pattern: String, locale: Locale = getLocale()): String {
        val sdf = SimpleDateFormat(pattern, locale)
        return sdf.format(timeMillis)
    }

    fun getNPastDaysTimestamp(daysBefore: Long): Long {
        return Calendar.getInstance(getLocale()).timeInMillis.minus(TimeUnit.DAYS.toMillis(daysBefore))
    }

    fun getNPastYearTimeStamp(yearBefore: Int): Date {
        val date = Calendar.getInstance(getLocale())
        date.set(Calendar.YEAR, date.get(Calendar.YEAR) - yearBefore)
        return date.time
    }

    fun getNowDaysTimestamp(): Date  {
        return Calendar.getInstance(getLocale()).time
    }

    fun getFormattedDate(daysBefore: Long, format: String) = format(getNPastDaysTimestamp(daysBefore), format)
}