package com.tokopedia.purchase_platform.common.utils

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.unifycomponents.Toaster
import rx.Emitter
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import timber.log.Timber
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
        return MethodChecker.fromHtml(replacedText).toString()
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

    @JvmStatic
    fun toIntOrZero(string: String): Int {
        return string.toIntOrZero()
    }
}

fun isNullOrEmpty(string: String?): Boolean = string.isNullOrEmpty()

fun String.removeDecimalSuffix(): String = this.removeSuffix(".00")

fun joinToString(strings: List<String>, separator: String): String = strings.joinToString(separator)

fun joinToStringFromListInt(ints: List<Int>, separator: String): String = ints.joinToString(separator)

fun String.isNotBlankOrZero(): Boolean {
    return this.isNotBlank() && this.toLongOrZero() != 0L
}

fun String.isBlankOrZero(): Boolean {
    return this.isBlank() || this.toLongOrZero() == 0L
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

fun showSoftKeyboard(context: Context?, view: View) {
    if (context == null) return
    try {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    } catch (t: Throwable) {
        Timber.d(t)
    }
}
