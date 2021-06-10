package com.tokopedia.oneclickcheckout.common.view.utils

import android.view.View
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.tokopedia.kotlin.extensions.view.isVisible

private fun View.fadeTo(visible: Boolean, duration: Long) {
    // Make this idempotent.
    val tagKey = "fadeTo".hashCode()
    if (visible == isVisible && animation == null && getTag(tagKey) == null) return
    if (getTag(tagKey) == visible) return

    setTag(tagKey, visible)

    if (visible && alpha == 1f) alpha = 0f
    animate()
            .alpha(if (visible) 1f else 0f)
            .withStartAction {
                if (visible) isVisible = true
            }
            .withEndAction {
                setTag(tagKey, null)
                if (isAttachedToWindow && !visible) isVisible = false
            }
            .setInterpolator(FastOutSlowInInterpolator())
            .setDuration(duration)
            .start()
}

internal fun View.animateShow(duration: Long = 400) = fadeTo(true, duration)
internal fun View.animateGone(duration: Long = 400) = fadeTo(false, duration)