package com.tokopedia.play.util

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.play.view.measurement.bounds.provider.videobounds.PortraitVideoBoundsProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withTimeout
import kotlin.reflect.KProperty1

/**
 * Created by jegul on 14/04/20
 */
internal inline fun View.changeConstraint(transform: ConstraintSet.() -> Unit) {
    require(this is ConstraintLayout)

    val constraintSet = ConstraintSet()

    constraintSet.clone(this)
    constraintSet.transform()
    constraintSet.applyTo(this)
}

/**
 * Current implementation of ViewPager2 add tag to each fragment that follows this pattern:
 * "f{pos}" -> {pos} here means the current fragment index position in the ViewPager2
 * e.g.
 * To get fragment in position 5 of the viewpager, the tag would be f4
 * since position 5 has index position of 4
 */
internal fun ViewPager2.findFragmentByPosition(
        fragmentManager: FragmentManager,
        pos: Int,
): Fragment? {
    return fragmentManager.findFragmentByTag("f$pos")
}

internal fun ViewPager2.findCurrentFragment(fragmentManager: FragmentManager): Fragment? {
    return findFragmentByPosition(fragmentManager, currentItem)
}

private const val MEASURE_TIMEOUT_IN_MS: Long = 500

internal suspend inline fun measureWithTimeout(
        timeout: Long = MEASURE_TIMEOUT_IN_MS,
        crossinline measureFn: suspend () -> Unit
) = withTimeout(timeout) {
    measureFn()
}

data class CachedState<T>(val prevValue: T? = null, val value: T) {

    fun <V> isValueChanged(prop: KProperty1<T, V>): Boolean {
        val prevState = this.prevValue
        val currState = this.value

        return when {
            currState == null -> false
            prevState == null -> true
            else -> {
                val prevValue = prop.get(prevState)
                val currentValue = prop.get(currState)
                prevValue != currentValue
            }
        }
    }
}

internal fun <T: Any> Flow<T>.withCache(): Flow<CachedState<T>> {
    var cachedValue : T? = null
    return map {
        val prevValue = cachedValue
        cachedValue = it
        CachedState(prevValue, it)
    }
}

internal fun <T: Any> MutableStateFlow<T>.setValue(fn: T.() -> T) {
    value = value.fn()
}