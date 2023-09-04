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
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.scp_rewards_common.constants.EASE_IN
import com.tokopedia.scp_rewards_common.constants.EASE_IN_OUT
import com.tokopedia.scp_rewards_common.constants.EASE_OUT
import com.tokopedia.scp_rewards_common.constants.LINEAR
import com.tokopedia.scp_rewards_common.constants.OVER_SHOOT
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

fun propertyValueHolder(property: Property<View, Float>, from: Float, to: Float) = PropertyValuesHolder.ofFloat(property, from, to)
