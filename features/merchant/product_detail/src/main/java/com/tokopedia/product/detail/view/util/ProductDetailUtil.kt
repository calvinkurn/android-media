package com.tokopedia.product.detail.view.util

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.unifyprinciples.getTypeface
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
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

fun String.boldTextWithGiven(context: Context, textToBold: String): SpannableStringBuilder {
    val builder = SpannableStringBuilder()

    if (this.isNotEmpty() || this.isNotBlank()) {
        val rawText = this.toLowerCase(Locale.getDefault())
        val rawTextToBold = textToBold.toLowerCase(Locale.getDefault())
        val startIndex = rawText.indexOf(rawTextToBold)
        val endIndex = startIndex + rawTextToBold.length
        val typographyBoldTypeFace = getTypeface(context, "RobotoBold.ttf")

        if (startIndex < 0 || endIndex < 0) {
            return builder.append(this)
        } else {
            builder.append(this)
            builder.setSpan(StyleSpan(Typeface.BOLD), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            builder.setSpan(CustomTypeSpan(typographyBoldTypeFace), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    } else {
        builder.append(this)
    }

    return builder
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

fun ArrayList<String>.asThrowable(): Throwable = Throwable(message = this.firstOrNull()?.toString()
        ?: "")

fun <T : Any> Result<T>.doSuccessOrFail(success: (Success<T>) -> Unit, fail: (Fail: Throwable) -> Unit) {
    when (this) {
        is Success -> {
            success.invoke(this)
        }
        is Fail -> {
            fail.invoke(this.throwable)
        }
    }
}

fun <T : Any> T.asSuccess(): Success<T> = Success(this)
fun Throwable.asFail(): Fail = Fail(this)

