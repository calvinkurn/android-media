package com.tokopedia.product.detail.view.util

import android.text.Spanned
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.toFormattedString
import java.util.*

object ProductDetailUtil {

    private const val MAX_CHAR = 150
    private const val MORE_DESCRIPTION = "<font color='#42b549'>Selengkapnya</font>"

    fun reviewDescFormatter(review: String): Spanned {
        return if (MethodChecker.fromHtml(review).length > MAX_CHAR) {
            val subDescription = MethodChecker.fromHtml(review).toString().substring(0, MAX_CHAR)
            MethodChecker
                    .fromHtml(subDescription.replace("(\r\n|\n)".toRegex(), "<br />") + "... "
                            + MORE_DESCRIPTION)
        } else {
            MethodChecker.fromHtml(review)
        }
    }
}

infix fun String?.toDate(format: String): String {
    this?.let {
        val isLongFormat = try {
            it.toLong()
            true
        } catch (e: Throwable) {
            false
        }

        return if (isLongFormat) {
            val date = Date(it.toLong() * 1000)
            date.toFormattedString(format)
        } else {
            this
        }
    }
    return ""
}
