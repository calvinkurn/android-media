package com.tokopedia.product.detail.view.util

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.product.detail.R
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

fun String.linkTextWithGiven(context: Context, vararg textToBold: Pair<String, () -> Unit>): SpannableString {
    val builder = SpannableString(this)

    if (this.isNotEmpty() || this.isNotBlank()) {
        for (it in textToBold) {
            if (it.first.isEmpty()) continue

            val rawText = this.toLowerCase(Locale.getDefault())
            val rawTextToBold = it.first.toLowerCase(Locale.getDefault())

            val startIndex = rawText.indexOf(rawTextToBold)
            val endIndex = startIndex + rawTextToBold.length

            val typographyBoldTypeFace = getTypeface(context, "RobotoBold.ttf")

            if (startIndex < 0 || endIndex < 0) {
                continue
            } else {
                builder.setSpan(StyleSpan(Typeface.BOLD), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                builder.setSpan(CustomTypeSpan(typographyBoldTypeFace), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                builder.setSpan(object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        it.second.invoke()
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.isUnderlineText = false
                        ds.color = MethodChecker.getColor(context, R.color.green_500)
                    }
                }, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
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

fun String.goToWebView(context: Context){
    RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, this))
}

fun <T : Any> T.asSuccess(): Success<T> = Success(this)
fun Throwable.asFail(): Fail = Fail(this)

