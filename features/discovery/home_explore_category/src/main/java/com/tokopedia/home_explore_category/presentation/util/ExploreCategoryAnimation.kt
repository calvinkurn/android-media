package com.tokopedia.home_explore_category.presentation.util

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.ui.Alignment

const val DURATION_CATEGORY_EASING = 300

val categoryToggleEasing = CubicBezierEasing(0.63f, 0.01f, 0.29f, 1f)

val enterExpandVertical =
    expandVertically(
        expandFrom = Alignment.Top,
        animationSpec = categoryToggleTween(DURATION_CATEGORY_EASING)
    ) + fadeIn(
        animationSpec = categoryToggleTween(
            DURATION_CATEGORY_EASING
        )
    )
val exitShrinkVertical =
    shrinkVertically(
        shrinkTowards = Alignment.Top,
        animationSpec = categoryToggleTween(DURATION_CATEGORY_EASING)
    ) + fadeOut(
        animationSpec = categoryToggleTween(DURATION_CATEGORY_EASING)
    )

fun <T> categoryToggleTween(durationMillis: Int): TweenSpec<T> {
    return tween(
        durationMillis = durationMillis,
        easing = categoryToggleEasing
    )
}
