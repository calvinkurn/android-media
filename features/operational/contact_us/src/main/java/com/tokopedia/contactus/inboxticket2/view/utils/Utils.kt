package com.tokopedia.contactus.inboxticket2.view.utils

import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.format.DateUtils
import android.text.format.Time
import android.text.style.BackgroundColorSpan
import android.util.TypedValue
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class Utils(private val mContext: Context) {
    private var mLocale: Locale? = null
    var UPLOAD_URL = "https://u12.tokopedia.net"
    @JvmField
    var CLOSED = "closed"
    @JvmField
    var OPEN = "open"
    @JvmField
    var SOLVED = "solved"
    fun containsIgnoreCase(src: String, what: String): Boolean {
        val length = what.length
        if (length == 0) return true // Empty string is contained
        val firstLo = Character.toLowerCase(what[0])
        val firstUp = Character.toUpperCase(what[0])
        for (i in src.length - length downTo 0) { // Quick check before calling the more expensive regionMatches() method:
            val ch = src[i]
            if (ch != firstLo && ch != firstUp) continue
            if (src.regionMatches(i, what, 0, length, ignoreCase = true)) return true
        }
        return false
    }

    private fun findOccurrences(what: String, src: String): List<Int> {
        val occurrences = ArrayList<Int>()
        val regex = "\\b$what\\w*"
        val pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(src)
        while (matcher.find()) {
            occurrences.add(matcher.start())
        }
        return occurrences
    }

    fun getHighlightText(what: String, src: String): SpannableString {
        val spannableString = SpannableString(src)
        if (what.isNotEmpty()) {
            val occurences = findOccurrences(what, src)
            for (start in occurences) {
                val styleSpan = BackgroundColorSpan(Color.parseColor("#ecdb77"))
                spannableString.setSpan(styleSpan, start, start + what.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            }
        }
        return spannableString
    }

    fun getStatusTitle(src: String, background: Int, textColor: Int, textSizeSp: Int): SpannableString {
        val spannableString = SpannableString(src)
        val start = src.lastIndexOf(".") + 4
        val roundedBackgroundSpan = RoundedBackgroundSpan(background, textColor, convertSpToPx(textSizeSp),
                convertDpToPx(8), convertDpToPx(4), convertDpToPx(2), convertDpToPx(2))
        spannableString.setSpan(roundedBackgroundSpan, start, src.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        return spannableString
    }

    fun getDateTime(isoTime: String?): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", locale)
        val dateFormat = SimpleDateFormat("d MMM 'pukul' HH:mm", locale)
        dateFormat.timeZone = TimeZone.getDefault()
        return try {
            val date = inputFormat.parse(isoTime)
            dateFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            e.localizedMessage
        }
    }

    val dateTimeCurrent: String
        get() {
            val dateFormat = SimpleDateFormat("d MMM 'pukul' HH:mm", locale)
            dateFormat.timeZone = TimeZone.getDefault()
            return dateFormat.format(Date(System.currentTimeMillis()))
        }

    fun getDateTimeYear(isoTime: String?): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", locale)
        val dateFormat = SimpleDateFormat("d MMMM yyyy", locale)
        val timeFormat = SimpleDateFormat("HH:mm 'WIB'", locale)
        dateFormat.timeZone = TimeZone.getTimeZone("Asia/Jakarta")
        timeFormat.timeZone = TimeZone.getTimeZone("Asia/Jakarta")
        return try {
            val date = inputFormat.parse(isoTime)
            if (DateUtils.isToday(date.time)) timeFormat.format(date) else if (isYesterday(date.time)) {
                "Kemarin"
            } else {
                dateFormat.format(date)
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            e.localizedMessage
        }
    }

    private fun convertDpToPx(dp: Int): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), mContext.resources.displayMetrics)
    }

    private fun convertSpToPx(dp: Int): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, dp.toFloat(), mContext.resources.displayMetrics)
    }

    private val locale: Locale
         get() {
            if (mLocale == null) mLocale = Locale("in", "ID", "")
            return mLocale!!
        }

    private fun isYesterday(time: Long): Boolean {
        val mTime = Time()
        mTime.set(time)
        val thenYear = mTime.year
        val thenMonth = mTime.month
        val thenMonthDay = mTime.monthDay
        mTime.set(System.currentTimeMillis())
        return (thenYear == mTime.year
                && thenMonth == mTime.month
                && thenMonthDay == mTime.monthDay - 1)
    }

}