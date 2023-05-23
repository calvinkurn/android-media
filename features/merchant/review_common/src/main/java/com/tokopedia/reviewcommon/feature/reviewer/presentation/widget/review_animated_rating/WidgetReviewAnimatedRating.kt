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
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
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
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.reviewcommon.R
import kotlin.math.abs

private const val STAR_COUNT = 5
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
    config: ReviewStarConfig
) = with(config) {
    val interactionSource by remember { mutableStateOf(MutableInteractionSource()) }
    Image(
        painter = painterResource(id = R.drawable.review_ic_star),
        colorFilter = ColorFilter.tint(color),
        contentDescription = contentDescription,
        modifier = modifier.clickable(
            interactionSource = interactionSource,
            indication = null
        ) { onClick() }
    )
}

private data class ReviewStarConfig(
    val color: Color,
    val contentDescription: String,
    val onClick: () -> Unit
)

@Composable
private fun WidgetReviewAnimatedStars(
    modifier: Modifier = Modifier,
    config: WidgetReviewAnimatedStarsConfig
) = with(config) {
    Row(modifier = modifier) {
        starScaleStates.forEachIndexed { index, _ ->
            val starRating = index.inc()
            val reviewStarModifier = Modifier
                .size(widgetReviewAnimatedRatingConfig.starSize)
                .scale(starScaleStates[index].value)
            val reviewStarConfig = ReviewStarConfig(
                color = starColorStates[index].value,
                contentDescription = "$starRating star rating",
                onClick = {
                    widgetReviewAnimatedRatingConfig.onStarClicked(
                        widgetReviewAnimatedRatingConfig.rating,
                        starRating
                    )
                }
            )
            ReviewStar(reviewStarModifier, reviewStarConfig)
            if (index < starScaleStates.size.dec()) {
                Spacer(modifier = Modifier.size(widgetReviewAnimatedRatingConfig.spaceInBetween))
            }
        }
    }
}

private data class WidgetReviewAnimatedStarsConfig(
    val starScaleStates: List<State<Float>>,
    val starColorStates: List<State<Color>>,
    val widgetReviewAnimatedRatingConfig: WidgetReviewAnimatedRatingConfig
)

@Composable
fun WidgetReviewAnimatedRating(
    modifier: Modifier = Modifier,
    config: WidgetReviewAnimatedRatingConfig
) {
    with(config) {
        val starStates = remember { mutableStateListOf(*createInitialStarStates()) }
        val starTransitions = starStates.createStateTransition()
        val starScaleStates = starTransitions.createScaleAnimation()
        val starColorStates = starTransitions.createColorAnimation()
        val widgetReviewAnimatedStarsConfig = WidgetReviewAnimatedStarsConfig(
            starScaleStates = starScaleStates,
            starColorStates = starColorStates,
            widgetReviewAnimatedRatingConfig = config
        )

        LaunchedEffect(rating) {
            if (rating in Int.ZERO..STAR_COUNT) {
                updateStarStates(rating = rating, starStates = starStates)
            }
        }

        WidgetReviewAnimatedStars(modifier = modifier, config = widgetReviewAnimatedStarsConfig)
    }
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

private fun createInitialStarStates(): Array<WidgetReviewAnimatedRatingState> {
    return List(STAR_COUNT) {
        WidgetReviewAnimatedRatingState.Inactive()
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

//region Preview
private const val DEFAULT_PREVIEW_STAR_SIZE = 24
private const val DEFAULT_PREVIEW_STAR_SPACE_IN_BETWEEN = 8
private const val DEFAULT_PREVIEW_SIZE_MULTIPLIER = 3
private const val DEFAULT_PREVIEW_SPACE_IN_BETWEEN_MULTIPLIER = 3

private data class WidgetReviewAnimatedRatingPreviewConfig(
    val sizeMultiplier: Int,
    val spaceInBetweenMultiplier: Int,
    val configs: MutableList<WidgetReviewAnimatedRatingConfig>
)

private val previewConfig = mutableStateOf(createPreviewConfig())

@Preview
@Composable
fun WidgetReviewAnimatedRatingPreview() {
    val config by remember { previewConfig }

    Column {
        config.configs.forEach { config -> WidgetReviewAnimatedRating(config = config) }
        Button(onClick = ::onEnlargeWidgets) { Text(text = "Enlarge Widgets") }
        Button(onClick = ::onEnlargeSpaceInBetween) { Text(text = "Enlarge Space in Between") }
        Button(onClick = ::onIncreaseRating) { Text(text = "Increase Ratings") }
        Button(onClick = ::onDecreaseRating) { Text(text = "Decrease Ratings") }
        Button(onClick = ::onResetRating) { Text(text = "Reset Ratings") }
    }
}

private fun createPreviewConfig(): WidgetReviewAnimatedRatingPreviewConfig {
    return WidgetReviewAnimatedRatingPreviewConfig(
        sizeMultiplier = DEFAULT_PREVIEW_SIZE_MULTIPLIER,
        spaceInBetweenMultiplier = DEFAULT_PREVIEW_SPACE_IN_BETWEEN_MULTIPLIER,
        configs = createPreviewConfigs()
    )
}

private fun createPreviewConfigs(): MutableList<WidgetReviewAnimatedRatingConfig> {
    return (Int.ZERO..STAR_COUNT).mapIndexed { index, rating ->
        WidgetReviewAnimatedRatingConfig(
            rating = rating,
            starSize = calculateStarSize(index, DEFAULT_PREVIEW_SIZE_MULTIPLIER),
            spaceInBetween = calculateSpaceInBetween(index, DEFAULT_PREVIEW_SPACE_IN_BETWEEN_MULTIPLIER),
            onStarClicked = { previousRating, currentRating ->
                if (previousRating != currentRating) onRatingChanged(index, currentRating)
            }
        )
    }.toMutableList()
}

private fun calculateStarSize(index: Int, multiplier: Int): Dp {
    return (DEFAULT_PREVIEW_STAR_SIZE + index * multiplier).dp
}

private fun calculateSpaceInBetween(index: Int, multiplier: Int): Dp {
    return (DEFAULT_PREVIEW_STAR_SPACE_IN_BETWEEN + index * multiplier).dp
}

private fun onRatingChanged(index: Int, rating: Int) {
    previewConfig.value.let {
        previewConfig.value = it.copy(
            configs = it.configs.mapIndexed { configIndex, config ->
                if (configIndex == index) config.copy(rating = rating) else config
            }.toMutableList()
        )
    }
}

private fun onEnlargeWidgets() {
    previewConfig.value.let {
        previewConfig.value = it.copy(
            sizeMultiplier = it.sizeMultiplier.inc(),
            configs = it.configs.mapIndexed { index, config ->
                config.copy(
                    starSize = calculateStarSize(
                        index = index,
                        multiplier = it.sizeMultiplier.inc()
                    )
                )
            }.toMutableList()
        )
    }
}

private fun onEnlargeSpaceInBetween() {
    previewConfig.value.let {
        previewConfig.value = it.copy(
            spaceInBetweenMultiplier = it.spaceInBetweenMultiplier.inc(),
            configs = it.configs.mapIndexed { index, config ->
                config.copy(
                    spaceInBetween = calculateSpaceInBetween(
                        index = index,
                        multiplier = it.spaceInBetweenMultiplier.inc()
                    )
                )
            }.toMutableList()
        )
    }
}

private fun onIncreaseRating() {
    previewConfig.value.let {
        previewConfig.value = it.copy(
            configs = it.configs.map { config ->
                config.copy(rating = config.rating.inc())
            }.toMutableList()
        )
    }
}

private fun onDecreaseRating() {
    previewConfig.value.let {
        previewConfig.value = it.copy(
            configs = it.configs.map { config ->
                config.copy(rating = config.rating.dec())
            }.toMutableList()
        )
    }
}

private fun onResetRating() {
    previewConfig.value = createPreviewConfig()
}
//endregion Preview
