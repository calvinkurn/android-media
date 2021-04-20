package com.tokopedia.purchase_platform.common.utils

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.View
import android.widget.CompoundButton
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.unifycomponents.Toaster
import rx.Emitter
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

/**
 * Created by fwidjaja on 2019-04-25.
 */
object Utils {
    @JvmStatic
    fun getHtmlFormat(text: String?): String {
        if (text == null) return ""
        if (TextUtils.isEmpty(text)) {
            return SpannableStringBuilder("").toString()
        }
        val replacedText = text.replace("&amp;", "&")
        val result: Spanned
        result = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> Html.fromHtml(replacedText, Html.FROM_HTML_MODE_LEGACY)
            else -> Html.fromHtml(replacedText)
        }
        return result.toString()
    }

    @JvmStatic
    fun convertDpToPixel(dp: Float, context: Context): Int {
        val resources = context.resources
        val metrics = resources.displayMetrics
        val px = dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        return px.roundToInt()
    }

    @JvmStatic
    fun removeDecimalSuffix(currencyString: String): String {
        return currencyString.removeDecimalSuffix()
    }

    @JvmStatic
    fun setToasterCustomBottomHeight(bottomHeight: Int) {
        Toaster.toasterCustomBottomHeight = bottomHeight
    }

    @JvmStatic
    fun isNotNullOrEmptyOrZero(string: String): Boolean {
        if (string.toLongOrZero() == 0L) {
            return false
        }
        return true
    }
}

fun convertToString(stringList: List<String>?): String {
    return if (stringList.isNullOrEmpty()) {
        ""
    } else {
        stringList.joinToString()
    }
}

fun isNullOrEmpty(string: String?): Boolean = string.isNullOrEmpty()

fun <T> isNullOrEmpty(list: List<T>?): Boolean = list.isNullOrEmpty()

fun <T : Any> List<T>.each(action: T.() -> Unit) {
    for (item in this) {
        item.action()
    }
}

fun String.removeDecimalSuffix(): String = this.removeSuffix(".00")

fun joinToString(strings: List<String>, separator: String): String = strings.joinToString(separator)

fun joinToStringFromListInt(ints: List<Int>, separator: String): String = ints.joinToString(separator)

fun String.isNotBlankOrZero(): Boolean {
    return this.isNotBlank() && this.toLongOrZero() != 0L
}

const val DEFAULT_DEBOUNCE_IN_MILIS = 250L
fun rxViewClickDebounce(view: View, timeout: Long = DEFAULT_DEBOUNCE_IN_MILIS): Observable<Boolean> =
        Observable.create({ emitter: Emitter<Boolean> ->
            view.setOnClickListener {
                emitter.onNext(true)
            }
        }, Emitter.BackpressureMode.LATEST)
                .debounce(timeout, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())

fun rxCompoundButtonCheckDebounce(compoundButton: CompoundButton, timeout: Long = DEFAULT_DEBOUNCE_IN_MILIS): Observable<Boolean> =
        Observable.create({ emitter: Emitter<Boolean> ->
            compoundButton.setOnCheckedChangeListener { _, isChecked ->
                emitter.onNext(isChecked)
            }
        }, Emitter.BackpressureMode.LATEST)
                .debounce(timeout, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
