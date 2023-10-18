package com.tokopedia.topchat.common.util

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.text.method.LinkMovementMethod
import androidx.core.app.ActivityCompat
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.topchat.common.Constant
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifyprinciples.Typography
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    private const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSZ"
    private const val LANGUAGE_CODE = "in"
    private const val COUNTRY_CODE = "ID"
    private const val SEVEN_DAYS = 7
    private var mLocale: Locale? = null

    fun getDateTime(isoTime: String?): String {
        return try {
            val inputFormat = SimpleDateFormat(DATE_FORMAT, locale)
            val dateFormat = SimpleDateFormat("dd MMMM yyyy", locale)
            val date = inputFormat.parse(isoTime)
            dateFormat.format(date)
        } catch (e: ParseException) {
            e.localizedMessage.toString()
        } catch (e: Exception) {
            e.localizedMessage.toString()
        }
    }

    fun getNextParticularDay(dayOfWeek: Int): Long {
        return try {
            val date = Calendar.getInstance()
            var diff: Int = dayOfWeek - date[Calendar.DAY_OF_WEEK]
            if (diff <= 0) {
                diff += SEVEN_DAYS
            }
            date.add(Calendar.DAY_OF_MONTH, diff)
            getMidnightTimeMillis(date)
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    private fun getMidnightTimeMillis(date: Calendar): Long {
        date.set(Calendar.HOUR_OF_DAY, 0)
        date.set(Calendar.MINUTE, 0)
        date.set(Calendar.SECOND, 0)
        return date.time.time
    }

    val locale: Locale?
        get() {
            if (mLocale == null) mLocale = Locale(LANGUAGE_CODE, COUNTRY_CODE, "")
            return mLocale
        }

    fun getOperationalInsightStateReport(isMaintain: Boolean?): String {
        return if (isMaintain == true) {
            Constant.GOOD_PERFORMANCE
        } else {
            Constant.NEED_IMPROVEMENT
        }
    }

    // this bubble will be shown within sellerapp and OS version 11/above
    fun isBubbleChatEnabled() =
        GlobalConfig.isSellerApp() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && getShouldBubbleChatEnabled()

    fun getShouldBubbleChatEnabled(): Boolean {
        return true
    }

    fun Activity?.isFromBubble(): Boolean {
        return try {
            this?.let { activity ->
                ActivityCompat.isLaunchedFromBubble(activity)
            } ?: false
        } catch (ignored: Throwable) {
            false
        }
    }

    fun Typography.setTextMakeHyperlink(text: String, onClick: () -> Unit) {
        val htmlString = HtmlLinkHelper(context, text)
        this.movementMethod = LinkMovementMethod.getInstance()
        this.highlightColor = Color.TRANSPARENT
        this.text = htmlString.spannedString
        htmlString.urlList.getOrNull(Int.ZERO)?.setOnClickListener {
            onClick()
        }
    }

    fun getBuildVersion(): Int {
        return Build.VERSION.SDK_INT
    }
}
