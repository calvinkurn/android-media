package com.tokopedia.reviewcommon.feature.reviewer.presentation.widget.review_animated_rating

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.ZERO

sealed interface ReviewStarState {
    val color: AnimatedState<Int>
    val size: AnimatedState<Dp>
    val contentDescription: String
    val onClick: () -> Unit

    data class Inactive(
        override val color: AnimatedState<Int>,
        override val size: AnimatedState<Dp>,
        override val contentDescription: String,
        override val onClick: () -> Unit
    ) : ReviewStarState {
        companion object {
            val COLOR_RES_ID = com.tokopedia.unifyprinciples.R.color.Unify_NN500
        }
    }

    data class Active(
        override val color: AnimatedState<Int>,
        override val size: AnimatedState<Dp>,
        override val contentDescription: String,
        override val onClick: () -> Unit
    ) : ReviewStarState {
        companion object {
            val COLOR_RES_ID = com.tokopedia.unifyprinciples.R.color.Unify_YN300
        }
    }

    data class AnimatedState<T : Any>(
        val delay: Int,
        val value: T
    )
}

fun ReviewStarState.AnimatedState<Int>.toColor(context: Context): Color {
    return Color(
        runCatching {
            ContextCompat.getColor(context, value)
        }.getOrElse {
            Int.ZERO
        }
    )
}
