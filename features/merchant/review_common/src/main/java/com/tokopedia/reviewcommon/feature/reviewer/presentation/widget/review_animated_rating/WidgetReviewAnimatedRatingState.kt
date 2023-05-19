package com.tokopedia.reviewcommon.feature.reviewer.presentation.widget.review_animated_rating

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.ZERO

sealed interface WidgetReviewAnimatedRatingState {
    val colorResId: Int
    val delay: Int

    class Inactive(
        override val delay: Int = Int.ZERO
    ) : WidgetReviewAnimatedRatingState {
        override val colorResId: Int = com.tokopedia.unifyprinciples.R.color.Unify_NN200
    }

    class Active(
        override val delay: Int = Int.ZERO
    ) : WidgetReviewAnimatedRatingState {
        override val colorResId: Int = com.tokopedia.unifyprinciples.R.color.Unify_YN300
    }
}

fun WidgetReviewAnimatedRatingState.toColor(context: Context): Color {
    return Color(ContextCompat.getColor(context, colorResId))
}
