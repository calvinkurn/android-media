package com.tokopedia.reviewcommon.feature.reviewer.presentation.widget.review_animated_rating

import android.annotation.SuppressLint
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tokopedia.reviewcommon.R
import kotlin.math.abs

const val DEFAULT_RATING = 0
private const val STAR_COUNT = 5
private const val DEFAULT_STAR_SIZE = 24
private const val DEFAULT_STAR_SPACE_IN_BETWEEN = 8
private const val STAR_ANIM_DELAY = 50
private const val STAR_COLOR_ANIM_DURATION = 100
private const val STAR_SCALE = 1f
private const val STAR_SCALE_ANIM_DURATION = 400
private const val STAR_SCALE_KEYFRAME_1_VALUE = 0.98546f
private const val STAR_SCALE_KEYFRAME_1_TIME = 0
private const val STAR_SCALE_KEYFRAME_2_VALUE = 0.57546f
private const val STAR_SCALE_KEYFRAME_2_TIME = 133
private const val STAR_SCALE_KEYFRAME_3_VALUE = 1.1366f
private const val STAR_SCALE_KEYFRAME_3_TIME = 333

@Composable
private fun ReviewStar(
    modifier: Modifier = Modifier,
    color: Color,
    contentDescription: String,
    onClick: () -> Unit
) {
    Image(
        painter = painterResource(id = R.drawable.review_ic_star),
        colorFilter = ColorFilter.tint(color),
        contentDescription = contentDescription,
        modifier = modifier.clickable { onClick() }
    )
}

@Composable
private fun WidgetReviewAnimatedStars(
    modifier: Modifier = Modifier,
    rating: Int,
    starSize: Dp,
    spaceInBetween: Dp,
    starScaleStates: List<State<Float>>,
    starColorStates: List<State<Color>>,
    onStarClicked: (previousRating: Int, currentRating: Int) -> Unit
) {
    Row(modifier = modifier) {
        starScaleStates.forEachIndexed { index, _ ->
            val starRating = index.inc()
            ReviewStar(
                modifier = Modifier
                    .size(starSize)
                    .scale(starScaleStates[index].value),
                color = starColorStates[index].value,
                contentDescription = "$starRating star rating",
                onClick = { onStarClicked(rating, starRating) }
            )
            if (index < starScaleStates.size.dec()) {
                Spacer(modifier = Modifier.size(spaceInBetween))
            }
        }
    }
}

@Composable
fun WidgetReviewAnimatedRating(
    modifier: Modifier = Modifier,
    rating: Int = DEFAULT_RATING,
    starSize: Dp = DEFAULT_STAR_SIZE.dp,
    spaceInBetween: Dp = DEFAULT_STAR_SPACE_IN_BETWEEN.dp,
    onStarClicked: (previousRating: Int, currentRating: Int) -> Unit = { _, _ -> }
) {
    if (rating > STAR_COUNT) throw IllegalStateException("Rating cannot be more than $STAR_COUNT")

    val starStates = remember { mutableStateListOf(*createStarStates(rating)) }
    val starTransitions = starStates.createStateTransition()
    val starScaleStates = starTransitions.createScaleAnimation()
    val starColorStates = starTransitions.createColorAnimation()

    LaunchedEffect(rating) { updateStarStates(rating = rating, starStates = starStates) }

    WidgetReviewAnimatedStars(
        modifier = modifier,
        rating = rating,
        starSize = starSize,
        spaceInBetween = spaceInBetween,
        starScaleStates = starScaleStates,
        starColorStates = starColorStates,
        onStarClicked = onStarClicked
    )
}

@Composable
private fun SnapshotStateList<WidgetReviewAnimatedRatingState>.createStateTransition(): List<Transition<WidgetReviewAnimatedRatingState>> {
    return mapIndexed { index, state ->
        updateTransition(state, label = "star $index state")
    }
}

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
private fun List<Transition<WidgetReviewAnimatedRatingState>>.createScaleAnimation(): List<State<Float>> {
    return mapIndexed { index, transition ->
        transition.animateFloat(
            transitionSpec = {
                keyframes {
                    durationMillis = STAR_SCALE_ANIM_DURATION
                    delayMillis = targetState.delay
                    STAR_SCALE_KEYFRAME_1_VALUE at STAR_SCALE_KEYFRAME_1_TIME
                    STAR_SCALE_KEYFRAME_2_VALUE at STAR_SCALE_KEYFRAME_2_TIME
                    STAR_SCALE_KEYFRAME_3_VALUE at STAR_SCALE_KEYFRAME_3_TIME
                }
            },
            label = "star $index scale"
        ) { _ -> STAR_SCALE }
    }
}

@Composable
private fun List<Transition<WidgetReviewAnimatedRatingState>>.createColorAnimation(): List<State<Color>> {
    return mapIndexed { index, transition ->
        transition.animateColor(
            transitionSpec = {
                tween(
                    durationMillis = STAR_COLOR_ANIM_DURATION,
                    delayMillis = targetState.delay
                )
            },
            label = "star $index color"
        ) { state -> state.toColor(LocalContext.current) }
    }
}

private fun createStarStates(rating: Int): Array<WidgetReviewAnimatedRatingState> {
    return List(STAR_COUNT) { index ->
        if (rating <= index) {
            WidgetReviewAnimatedRatingState.Inactive()
        } else {
            WidgetReviewAnimatedRatingState.Active()
        }
    }.toTypedArray()
}

private fun updateStarStates(
    rating: Int,
    starStates: MutableList<WidgetReviewAnimatedRatingState>
) {
    val previousLastActiveRatingStar = starStates.indexOfLast {
        it is WidgetReviewAnimatedRatingState.Active
    }
    val currentLastActiveRatingStar = rating.dec()

    if (previousLastActiveRatingStar > currentLastActiveRatingStar) {
        val lastActiveStarToDeactivate = currentLastActiveRatingStar.inc()
        (previousLastActiveRatingStar downTo lastActiveStarToDeactivate).forEach {
            starStates[it] = WidgetReviewAnimatedRatingState.Inactive(
                delay = abs(previousLastActiveRatingStar - it) * STAR_ANIM_DELAY
            )
        }
    } else if (previousLastActiveRatingStar < currentLastActiveRatingStar) {
        val firstInactiveStarToActivate = previousLastActiveRatingStar.inc()
        (firstInactiveStarToActivate..currentLastActiveRatingStar).forEach {
            starStates[it] = WidgetReviewAnimatedRatingState.Active(
                delay = abs(firstInactiveStarToActivate - it) * STAR_ANIM_DELAY
            )
        }
    }
}

@Preview
@Composable
fun WidgetReviewAnimatedRatingPreview() {
    val ratings = remember {
        val initialRatings = (DEFAULT_RATING..STAR_COUNT).map { rating -> rating }
        mutableStateListOf(*initialRatings.toTypedArray())
    }
    Column {
        ratings.forEachIndexed { index, rating ->
            WidgetReviewAnimatedRating(
                rating = rating,
                starSize = (DEFAULT_STAR_SIZE + index * 3).dp,
                spaceInBetween = (DEFAULT_STAR_SPACE_IN_BETWEEN + index * 3).dp,
                onStarClicked = { _, currentRating ->
                    ratings[index] = currentRating
                }
            )
        }
        Button(
            onClick = {
                (DEFAULT_RATING..STAR_COUNT).forEachIndexed { index, rating ->
                    ratings[index] = rating
                }
            }
        ) {
            Text(text = "Reset Ratings")
        }
    }
}
