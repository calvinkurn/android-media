package com.tokopedia.purchase_platform.common.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import rx.Emitter
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Created by fwidjaja on 2019-04-25.
 */
object Utils {
    @JvmStatic
    fun getHtmlFormat(text: String?): String {
        if (text.isNullOrEmpty()) return ""
        val replacedText = text.replace("&amp;", "&")
        return MethodChecker.fromHtml(replacedText).toString()
    }
}

fun String.removeDecimalSuffix(): String = this.removeSuffix(".00")

fun String.removeSingleDecimalSuffix(): String = this.removeSuffix(".0")

fun String.isNotBlankOrZero(): Boolean {
    return this.isNotBlank() && this.toLongOrZero() != 0L
}

fun String.isBlankOrZero(): Boolean {
    return this.isBlank() || this.toLongOrZero() == 0L
}

const val DEFAULT_DEBOUNCE_IN_MILIS = 250L
const val DEFAULT_THROTTLE_IN_MILIS = 2_000L
fun rxViewClickDebounce(view: View, timeout: Long = DEFAULT_DEBOUNCE_IN_MILIS): Observable<Boolean> =
    Observable.create({ emitter: Emitter<Boolean> ->
        view.setOnClickListener {
            emitter.onNext(true)
        }
    }, Emitter.BackpressureMode.LATEST)
        .debounce(timeout, TimeUnit.MILLISECONDS)
        .subscribeOn(AndroidSchedulers.mainThread())
        .observeOn(AndroidSchedulers.mainThread())

fun rxViewClickThrottle(view: View, timeout: Long = DEFAULT_THROTTLE_IN_MILIS): Observable<Boolean> =
    Observable.create({ emitter: Emitter<Boolean> ->
        view.setOnClickListener {
            emitter.onNext(true)
        }
    }, Emitter.BackpressureMode.LATEST)
        .throttleFirst(timeout, TimeUnit.MILLISECONDS)
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
