package com.tokopedia.scp_rewards_common.utils

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.DisplayMetrics
import android.util.Property
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.scp_rewards_common.constants.EASE_IN
import com.tokopedia.scp_rewards_common.constants.EASE_IN_OUT
import com.tokopedia.scp_rewards_common.constants.EASE_OUT
import com.tokopedia.scp_rewards_common.constants.LINEAR
import com.tokopedia.scp_rewards_common.constants.OVER_SHOOT
import com.tokopedia.unifyprinciples.UnifyMotion
import com.tokopedia.unifyprinciples.stringToUnifyColor
import org.json.JSONObject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

fun parseColor(color: String?): Int? {
    return try {
        Color.parseColor(color)
    } catch (err: Exception) {
        FirebaseCrashlytics.getInstance().recordException(err)
        null
    }
}

fun <T> String.parseJsonKey(key: String): T? {
    JSONObject(this).apply {
        return try {
            get(key) as T
        } catch (err: Throwable) {
            FirebaseCrashlytics.getInstance().recordException(err)
            null
        }
    }
}

fun dpToPx(context: Context, dp: Int): Float {
    return dp * (context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT).toFloat()
}

@Suppress("SpreadOperator")
fun View.animateView(animations: Array<PropertyValuesHolder>, duration: Long, interpolatorType: Int, listener: Animator.AnimatorListener? = null) {
    ObjectAnimator.ofPropertyValuesHolder(this, *animations).apply {
        this.duration = duration
        interpolator = when (interpolatorType) {
            LINEAR -> UnifyMotion.LINEAR
            EASE_OUT -> UnifyMotion.EASE_OUT
            EASE_IN_OUT -> UnifyMotion.EASE_IN_OUT
            OVER_SHOOT -> UnifyMotion.EASE_OVERSHOOT
            EASE_IN -> AccelerateDecelerateInterpolator()
            else -> UnifyMotion.EASE_IN_OUT
        }
        listener?.let { addListener(it) }
        start()
    }
}

fun propertyValueHolder(property: Property<View, Float>, from: Float, to: Float): PropertyValuesHolder =
    PropertyValuesHolder.ofFloat(property, from, to)

fun Context.parseColorOrFallback(color: String?, fallback: Int = unifyprinciplesR.color.Unify_NN200): Int {
    return color?.let {
        stringToUnifyColor(this, it).unifyColor ?: parseColor(color) ?: ContextCompat.getColor(this, fallback)
    } ?: run {
        ContextCompat.getColor(this, fallback)
    }
}

@SuppressLint("DiscouragedApi", "InternalInsetResource")
fun Activity.getNavigationBarHeight(view: View?): Int {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
        val imm =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
    val resourceId: Int =
        resources?.getIdentifier("navigation_bar_height", "dimen", "android").toZeroIfNull()
    return if (resourceId > 0) {
        resources?.getDimensionPixelSize(resourceId) ?: 0
    } else {
        0
    }
}


sealed class MergerLiveData<TargetType> : MediatorLiveData<TargetType>() {

    class Two<FirstSourceType, SecondSourceType, TargetType>(
        private val firstSource: LiveData<FirstSourceType>,
        private val secondSource: LiveData<SecondSourceType>,
        private val distinctUntilChanged: Boolean = true,
        private val merging: (FirstSourceType, SecondSourceType) -> TargetType
    ) : MediatorLiveData<TargetType>() {

        private fun <T> MediatorLiveData<T>.postValue(
            distinctUntilChanged: Boolean,
            newValue: T
        ) {
            val value = value ?: postValue(newValue)

            if (distinctUntilChanged && value == newValue) return

            postValue(newValue)
        }

        override fun onActive() {
            super.onActive()

            addSource(firstSource) { value ->
                val newValue = merging(value, secondSource.value ?: return@addSource)
                postValue(distinctUntilChanged = distinctUntilChanged, newValue = newValue)
            }

            addSource(secondSource) { value ->
                val newValue = merging(firstSource.value ?: return@addSource, value)
                postValue(distinctUntilChanged = distinctUntilChanged, newValue = newValue)
            }
        }

        override fun onInactive() {
            removeSource(firstSource)
            removeSource(secondSource)

            super.onInactive()
        }
    }
}
