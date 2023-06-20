package com.tokopedia.troubleshooter.notification.util

import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success

fun TextView.setCustomSpan(word: String, color: Int) {
    val spannableString = SpannableString(text)
    val start = text.indexOf(word)
    val end = text.indexOf(word) + word.length
    try {
        val textColor = ForegroundColorSpan(color)
        val textStyle = StyleSpan(Typeface.BOLD)
        spannableString.setSpan(textColor, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(textStyle, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        text = spannableString
    } catch (e: IndexOutOfBoundsException) {
        println("invalid '$word' in TextView")
    }
}

fun Any?.isNotNull(): Boolean {
    return this != null
}

fun String.prefixToken(): String {
    return try {
        this.substring(this.length - 8)
    } catch (e: Exception) {
        ""
    }
}

inline fun <T> Iterable<T>.getWithIndex(predicate: (T) -> Boolean): Pair<Int, T>? {
    forEachIndexed { index, element -> if (predicate(element)) return Pair(index, element) }
    return null
}

fun <T1, T2, T3, T4, T5> combine(
        f1: LiveData<T1>,
        f2: LiveData<T2>,
        f3: LiveData<T3>,
        f4: LiveData<T4>,
        f5: LiveData<T5>
): MediatorLiveData<Combination<T1?, T2?, T3?, T4?, T5?>>
        = MediatorLiveData<Combination<T1?, T2?, T3?, T4?, T5?>>().also { mediator ->
    mediator.value = Combination(f1.value, f2.value, f3.value, f4.value, f5.value)

    mediator.addSource(f1) { t1: T1? ->
        val (_, t2, t3, t4, t5) = mediator.value!!
        mediator.value = Combination(t1, t2, t3, t4, t5)
    }

    mediator.addSource(f2) { t2: T2? ->
        val (t1, _, t3, t4, t5) = mediator.value!!
        mediator.value = Combination(t1, t2, t3, t4, t5)
    }

    mediator.addSource(f3) { t3: T3? ->
        val (t1, t2, _, t4, t5) = mediator.value!!
        mediator.value = Combination(t1, t2, t3, t4, t5)
    }

    mediator.addSource(f4) { t4: T4? ->
        val (t1, t2, t3, _, t5) = mediator.value!!
        mediator.value = Combination(t1, t2, t3, t4, t5)
    }

    mediator.addSource(f5) { t5: T5? ->
        val (t1, t2, t3, t4, _) = mediator.value!!
        mediator.value = Combination(t1, t2, t3, t4, t5)
    }
}

inline fun <reified T: Any> Result<T>?.isTrue(): Boolean {
    return when (this) {
        is Success -> true
        else -> false
    }
}
