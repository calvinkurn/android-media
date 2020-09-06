package com.tokopedia.troubleshooter.notification.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.tokopedia.troubleshooter.notification.ui.uiview.RingtoneState
import com.tokopedia.troubleshooter.notification.ui.state.Result
import com.tokopedia.troubleshooter.notification.ui.state.Success
import java.lang.Exception

fun <T> MutableList<T>.dropFirst() {
    if (isEmpty()) return
    removeAt(0)
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

fun <T1, T2, T3, T4> combineFourth(
        f1: LiveData<T1>,
        f2: LiveData<T2>,
        f3: LiveData<T3>,
        f4: LiveData<T4>
): MediatorLiveData<Fourth<T1?, T2?, T3?, T4?>>
        = MediatorLiveData<Fourth<T1?, T2?, T3?, T4?>>().also { mediator ->
    mediator.value = Fourth(f1.value, f2.value, f3.value, f4.value)

    mediator.addSource(f1) { t1: T1? ->
        val (_, t2, t3, t4) = mediator.value!!
        mediator.value = Fourth(t1, t2, t3, t4)
    }

    mediator.addSource(f2) { t2: T2? ->
        val (t1, _, t3, t4) = mediator.value!!
        mediator.value = Fourth(t1, t2, t3, t4)
    }

    mediator.addSource(f3) { t3: T3? ->
        val (t1, t2, _, t4) = mediator.value!!
        mediator.value = Fourth(t1, t2, t3, t4)
    }

    mediator.addSource(f4) { t4: T4? ->
        val (t1, t2, t3, _) = mediator.value!!
        mediator.value = Fourth(t1, t2, t3, t4)
    }
}

inline fun <reified T: Any> Result<T>?.isTrue(): Boolean {
    return when (this) {
        is Success -> true
        else -> false
    }
}

fun RingtoneState?.isRinging(): Boolean {
    return when (this) {
        is RingtoneState.Normal -> true
        is RingtoneState.Vibrate -> false
        is RingtoneState.Silent -> false
        else -> false
    }
}