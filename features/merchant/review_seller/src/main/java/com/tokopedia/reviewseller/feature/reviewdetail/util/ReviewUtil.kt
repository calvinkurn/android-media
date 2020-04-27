package com.tokopedia.reviewseller.feature.reviewdetail.util

import android.content.Context
import android.text.Spanned
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.relativeWeekAndDay
import com.tokopedia.reviewseller.R
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Yehezkiel on 24/04/20
 */
object ReviewUtil {
    fun getReviewStar(ratingCount: Int): Int {
        return when (ratingCount) {
            1 -> {
                R.drawable.ic_rating_star_one
            }
            2 -> {
                R.drawable.ic_rating_star_two
            }
            3 -> {
                R.drawable.ic_rating_star_three
            }
            4 -> {
                R.drawable.ic_rating_star_four
            }
            5 -> {
                R.drawable.ic_rating_star_five
            }
            else -> {
                R.drawable.ic_rating_star_zero
            }
        }
    }
}

fun String.toReviewDescriptionFormatted(context: Context, maxChar: Int): Spanned {
    val seeMoreTextLength = context.getString(R.string.label_read_more).length + context.getString(R.string.label_ellipsis_read_more).length
    return if (seeMoreTextLength < maxChar) {
        val subDescription = MethodChecker.fromHtml(this).toString().substring(0, maxChar - seeMoreTextLength)
        MethodChecker
                .fromHtml(subDescription.replace("(\r\n|\n)".toRegex(), "<br />") + "... "
                        + "<font color='#42b549'>Selengkapnya</font>")
    } else {
        MethodChecker.fromHtml(this)
    }
}

infix fun String.toRelativeDayAndWeek(format: String): String {
    return if (this.isNotEmpty()) {
        val sdf = SimpleDateFormat(format)
        val date: Date = sdf.parse(this)
        val millis: Long = date.time

        return try {
            val cal = Calendar.getInstance()
            cal.timeInMillis = millis
            cal.time.relativeWeekAndDay
        } catch (t: Throwable) {
            ""
        }
    } else {
        ""
    }
}