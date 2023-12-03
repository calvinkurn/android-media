package com.tokopedia.home_explore_category.presentation.util

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.shrinkVertically

const val DURATION_CATEGORY_EASING = 300

val categoryToggleEasing = CubicBezierEasing(0.63f, 0.01f, 0.29f, 1f)

val enterExpandVertical =
    expandVertically(animationSpec = categoryToggleTween(DURATION_CATEGORY_EASING)) + fadeIn(
        animationSpec = categoryToggleTween(
            DURATION_CATEGORY_EASING
        )
    )
val exitShrinkVertical =
    shrinkVertically(animationSpec = categoryToggleTween(DURATION_CATEGORY_EASING)) + fadeOut(
        animationSpec = categoryToggleTween(DURATION_CATEGORY_EASING)
    )

val enterExpandIn =
     fadeIn(
        animationSpec = categoryToggleTween(
            DURATION_CATEGORY_EASING
        )
    ) + expandIn(animationSpec = categoryToggleTween(DURATION_CATEGORY_EASING))
val exitShrinkOut =
    shrinkOut(animationSpec = categoryToggleTween(DURATION_CATEGORY_EASING)) + fadeOut(
        animationSpec = categoryToggleTween(DURATION_CATEGORY_EASING)
    )

fun <T> categoryToggleTween(durationMillis: Int): TweenSpec<T> {
    return tween(
        durationMillis = durationMillis, easing = categoryToggleEasing
    )
}
