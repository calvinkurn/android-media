package com.tokopedia.scp_rewards_common

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.graphics.Color
import android.util.DisplayMetrics
import android.util.Property
import android.view.View
import androidx.core.content.ContextCompat
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.unifyprinciples.UnifyMotion
import org.json.JSONObject

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
fun View.animateView(animations: Array<PropertyValuesHolder>, duration: Long, interpolatorType: Int) {
    ObjectAnimator.ofPropertyValuesHolder(this, *animations).apply {
        this.duration = duration
        interpolator = when (interpolatorType) {
            LINEAR -> UnifyMotion.LINEAR
            EASE_OUT -> UnifyMotion.EASE_OUT
            EASE_IN_OUT -> UnifyMotion.EASE_IN_OUT
            OVER_SHOOT -> UnifyMotion.EASE_OVERSHOOT
            else -> UnifyMotion.EASE_IN_OUT
        }
        start()
    }
}

fun propertyValueHolder(property: Property<View, Float>, from: Float, to: Float): PropertyValuesHolder = PropertyValuesHolder.ofFloat(property, from, to)

fun Context.parseColorOrFallback(color: String?, fallback: Int = R.color.Unify_NN200): Int {
    return parseColor(color) ?: ContextCompat.getColor(this, fallback)
}
