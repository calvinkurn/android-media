package com.tokopedia.contactus.inboxtickets.view.utils

import android.content.Context
import android.graphics.BitmapFactory
import android.text.SpannableString
import android.text.Spanned
import android.text.format.DateUtils
import android.text.format.Time
import android.text.style.AbsoluteSizeSpan
import android.util.TypedValue
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.contactus.inboxtickets.data.ImageUpload
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException
import kotlin.math.ceil

const val CLOSED = "closed"
const val OPEN = "open"
const val SOLVED = "solved"
const val NEW = "new"

class Utils {

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
        return try {
            val occurrences = ArrayList<Int>()
            val regex = "\\b$what\\w*"
            val pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
            val matcher = pattern.matcher(src)
            while (matcher.find()) {
                occurrences.add(matcher.start())
            }
            occurrences
        } catch(e : PatternSyntaxException){
            emptyList()
        }
    }

    fun getHighlightText(context: Context, what: String, src: String): SpannableString {
        val spannableString = SpannableString(src)
        if (what.isNotEmpty()) {
            val occurences = findOccurrences(what, src)
            for (start in occurences) {
                val styleSpan = getColorResource(
                    com.tokopedia.unifyprinciples.R.color.Unify_YN200,
                    context
                )
                spannableString.setSpan(
                    styleSpan,
                    start,
                    start + what.length,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE
                )
            }
        }
        return spannableString
    }

    private fun getColorResource(colorId: Int, context: Context): Int {
        return MethodChecker.getColor(context, colorId)
    }

    fun getStatusTitle(
        src: String,
        background: Int,
        textColor: Int,
        textSize: Int,
        mContext: Context
    ): SpannableString {
        val spannableString = SpannableString(src)
        val start = src.lastIndexOf(".") + 4
        val roundedBackgroundSpan = RoundedBackgroundSpan(
            background,
            textColor,
            textSize.toFloat(),
            convertDpToPx(8, mContext),
            convertDpToPx(4, mContext),
            convertDpToPx(2, mContext),
            convertDpToPx(2, mContext)
        )
        spannableString.setSpan(
            roundedBackgroundSpan,
            start,
            src.length,
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            AbsoluteSizeSpan(textSize),
            start,
            src.length,
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )
        return spannableString
    }

    fun getDateTime(isoTime: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", locale)
        val dateFormat = SimpleDateFormat("d MMM 'pukul' HH:mm", locale)
        dateFormat.timeZone = TimeZone.getDefault()
        return try {
            val date = inputFormat.parse(isoTime)
            date?.let { dateFormat.format(it) }?:""
        } catch (e: ParseException) {
            ""
        }
    }

    val dateTimeCurrent: String
        get() {
            dateFormat.timeZone = TimeZone.getDefault()
            return dateFormat.format(Date(System.currentTimeMillis()))
        }

    private val dateFormat: SimpleDateFormat by lazy {
        SimpleDateFormat(
            "d MMM 'pukul' HH:mm",
            locale
        )
    }

    fun getDateTimeYear(isoTime: String?): String {
        if(isoTime.isNullOrEmpty()){
            return ""
        }
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", locale)
        val dateFormat = SimpleDateFormat("d MMMM yyyy", locale)
        val timeFormat = SimpleDateFormat("HH:mm 'WIB'", locale)
        dateFormat.timeZone = TimeZone.getTimeZone("Asia/Jakarta")
        timeFormat.timeZone = TimeZone.getTimeZone("Asia/Jakarta")
        return try {
            val date = inputFormat.parse(isoTime)
            date?.let {
                if (DateUtils.isToday(date.time)) timeFormat.format(date) else if (isYesterday(
                        date.time
                    )
                ) {
                    "Kemarin"
                } else {
                    dateFormat.format(date)
                }
            }.orEmpty()
        } catch (e: ParseException) {
            ""
        }
    }

    fun fileSizeValid(fileLoc: String): Boolean {
        val file = File(fileLoc)
        val size = file.length()
        return size / 1024 < 10240
    }

    fun isBitmapDimenValid(fileLoc: String): Boolean {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(File(fileLoc).absolutePath, options)
        val imageHeight = options.outHeight
        val imageWidth = options.outWidth
        return !(imageHeight < 300 && imageWidth < 300)
    }

    fun verifyAllImages(uploadImageList: List<ImageUpload>): Int {
        var count = 0
        val size = uploadImageList.size
        for (item in 0 until size) {
            val image = uploadImageList.get(item)
            if (fileSizeValid(image.fileLoc ?: "") &&
                isBitmapDimenValid(image.fileLoc ?: "")
            ) {
                count++
            }
        }
        return count
    }

    /**
     * change list object to string with ~ as  separator instead ,
     * */
    fun getAttachmentAsString(list: List<String>): String {
        var attachmentString = StringBuilder()
        list.forEach {
            attachmentString.append("~").append(it)
        }
        attachmentString = StringBuilder(attachmentString.toString().replace("~~", "~"))
        if (attachmentString.isNotEmpty()) attachmentString = StringBuilder(
            attachmentString.substring(1)
        )

        return attachmentString.toString()
    }

    fun createUniqID() :String  {
        val lenghtID= 29
        val consistID= arrayListOf(
            '0',
            '1',
            '2',
            '3',
            '4',
            '5',
            '6',
            '7',
            '8',
            '9',
            'a',
            'b',
            'c',
            'd',
            'e',
            'f',
            'g',
            'h',
            'i',
            'j',
            'k',
            'l',
            'm',
            'n',
            'o',
            'p',
            'q',
            'r',
            's',
            't',
            'u',
            'v',
            'w',
            'x',
            'y',
            'z',
            'A',
            'B',
            'C',
            'D',
            'E',
            'F',
            'G',
            'H',
            'I',
            'J',
            'K',
            'L',
            'M',
            'N',
            'O',
            'P',
            'Q',
            'R',
            'S',
            'T',
            'U',
            'V',
            'W',
            'X',
            'Y',
            'Z',
        )
        var uniqId = ""
        for (i in 1 until lenghtID){
            val rndNum = ceil(Math.random() * consistID.size) - 1
            uniqId += consistID[rndNum.toInt()]
        }
        return "O_${uniqId}"

    }

    private fun convertDpToPx(dp: Int, mContext: Context): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            mContext.resources.displayMetrics
        )
    }

    private val locale: Locale by lazy { Locale("in", "ID", "") }

    @Suppress("DEPRECATION")
    private fun isYesterday(time: Long): Boolean {
        val mTime = Time()
        mTime.set(time)
        val thenYear = mTime.year
        val thenMonth = mTime.month
        val thenMonthDay = mTime.monthDay
        mTime.set(System.currentTimeMillis())
        return (
            thenYear == mTime.year &&
                thenMonth == mTime.month &&
                thenMonthDay == mTime.monthDay - 1
            )
    }
}
