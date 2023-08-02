package com.tokopedia.reviewcommon.feature.reviewer.presentation.widget.review_animated_rating

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateDp
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.reviewcommon.R
import com.tokopedia.unifycomponents.toPx
import kotlin.math.abs

private const val STAR_COUNT = 5
private const val STAR_ANIM_DELAY = 50
private const val STAR_COLOR_ANIM_DURATION = 100
private const val STAR_SCALE = 1f
private const val STAR_SCALE_ANIM_DURATION = 400
private const val STAR_SIZE_ANIM_DURATION = 400
private const val STAR_SCALE_KEYFRAME_1_VALUE = 0.98546f
private const val STAR_SCALE_KEYFRAME_1_TIME = 0
private const val STAR_SCALE_KEYFRAME_2_VALUE = 0.57546f
private const val STAR_SCALE_KEYFRAME_2_TIME = 133
private const val STAR_SCALE_KEYFRAME_3_VALUE = 1.1366f
private const val STAR_SCALE_KEYFRAME_3_TIME = 333

@Composable
private fun ReviewStar(state: MutableState<ReviewStarState>) {
    val resources = LocalContext.current.resources

    /**
     * `starStates` represent the active/inactive state of the stars and are determined by the color ID value.
     * It is used to trigger the color and scale animations simultaneously, as both animations should run together.
     * We choose color as the determinant because active and inactive state have exactly 1 color representation
     */
    val starState by remember { derivedStateOf { state.value.color } }

    /**
     * sizeState represent the star size state and it is determined by the size value. It is used to
     * trigger the star size animation
     */
    val sizeState by remember { derivedStateOf { state.value.size } }
    val contentDescription by remember { derivedStateOf { state.value.contentDescription } }
    val starDrawable = remember {
        ImageBitmap.imageResource(res = resources, id = R.drawable.ic_review_common_star_filled)
    }
    val starStateTransition = updateTransition(starState, label = "star state transition for $contentDescription")
    val sizeStateTransition = updateTransition(sizeState, label = "size state transition for $contentDescription")
    val color by starStateTransition.createColorAnimation(label = "color animation for $contentDescription")
    val scale by starStateTransition.createScaleAnimation(label = "scale animation for $contentDescription")
    val size by sizeStateTransition.createSizeAnimation(label = "size animation for $contentDescription")

    Image(
        painter = BitmapPainter(starDrawable),
        contentDescription = contentDescription,
        modifier = Modifier
            .size(size)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .drawWithCache {
                onDrawWithContent {
                    drawImage(
                        image = starDrawable,
                        colorFilter = ColorFilter.tint(color),
                        dstSize = IntSize(
                            size.value
                                .toInt()
                                .toPx(),
                            size.value
                                .toInt()
                                .toPx()
                        )
                    )
                }
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { state.value.onClick() }
    )
}

@Composable
fun WidgetReviewAnimatedRating(
    modifier: Modifier = Modifier,
    config: WidgetReviewAnimatedRatingConfig
) {
    with(config) {
        val starStates = remember { createInitialStarStates(config) }

        /**
         * Update star states if rating or star size is changed so that ReviewStar will recompose
         */
        LaunchedEffect(rating, starSize) {
            if (rating in Int.ZERO..STAR_COUNT) {
                updateStarStates(config = config, starStates = starStates)
            }
        }

        Row(modifier = modifier) {
            starStates.forEachIndexed { index, state ->
                ReviewStar(state)
                if (index < starStates.size.dec()) {
                    Spacer(modifier = Modifier.size(spaceInBetween))
                }
            }
        }
    }
}

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
private fun Transition<ReviewStarState.AnimatedState<Int>>.createScaleAnimation(label: String): State<Float> {
    return animateFloat(
        transitionSpec = {
            keyframes {
                durationMillis = STAR_SCALE_ANIM_DURATION
                delayMillis = targetState.delay
                STAR_SCALE_KEYFRAME_1_VALUE at STAR_SCALE_KEYFRAME_1_TIME
                STAR_SCALE_KEYFRAME_2_VALUE at STAR_SCALE_KEYFRAME_2_TIME
                STAR_SCALE_KEYFRAME_3_VALUE at STAR_SCALE_KEYFRAME_3_TIME
            }
        },
        label = label
    ) { _ -> STAR_SCALE }
}

@Composable
private fun Transition<ReviewStarState.AnimatedState<Dp>>.createSizeAnimation(label: String): State<Dp> {
    return animateDp(
        transitionSpec = {
            tween(
                durationMillis = STAR_SIZE_ANIM_DURATION,
                delayMillis = targetState.delay
            )
        },
        label = label
    ) { state -> state.value }
}

@Composable
private fun Transition<ReviewStarState.AnimatedState<Int>>.createColorAnimation(
    label: String
) = animateColor(
    transitionSpec = {
        tween(
            durationMillis = STAR_COLOR_ANIM_DURATION,
            delayMillis = targetState.delay
        )
    },
    label = label
) { state -> state.toColor(LocalContext.current) }

private fun createInitialStarStates(
    config: WidgetReviewAnimatedRatingConfig
): List<MutableState<ReviewStarState>> = List(STAR_COUNT) { starPos ->
    if (config.skipInitialAnimation) {
        if (config.rating <= starPos) {
            mutableStateOf(createInactiveWidgetReviewStarStateFrom(config, Int.ZERO, starPos.inc()))
        } else {
            mutableStateOf(createActiveWidgetReviewStarStateFrom(config, Int.ZERO, starPos.inc()))
        }
    } else {
        mutableStateOf(createInactiveWidgetReviewStarStateFrom(config, Int.ZERO, starPos.inc()))
    }
}

private fun updateStarStates(
    config: WidgetReviewAnimatedRatingConfig,
    starStates: List<MutableState<ReviewStarState>>
) = with(config) {
    recreateAffectedStarStates(config, starStates)
    updateNonAffectedStarStates(config, starStates)
}

private fun recreateAffectedStarStates(
    config: WidgetReviewAnimatedRatingConfig,
    starStates: List<MutableState<ReviewStarState>>
) = with(config) {
    val currentLastActiveStarPos = starStates.indexOfLast {
        it.value is ReviewStarState.Active
    }
    val expectedLastActiveStarPos = rating.dec()

    if (currentLastActiveStarPos > expectedLastActiveStarPos) {
        (currentLastActiveStarPos downTo expectedLastActiveStarPos.inc()).forEach { starPos ->
            starStates[starPos].value = createInactiveWidgetReviewStarStateFrom(
                config = this,
                delay = abs(currentLastActiveStarPos - starPos) * STAR_ANIM_DELAY,
                starRating = starPos.inc()
            )
        }
    } else if (currentLastActiveStarPos < expectedLastActiveStarPos) {
        (currentLastActiveStarPos.inc()..expectedLastActiveStarPos).forEach { starPos ->
            starStates[starPos].value = createActiveWidgetReviewStarStateFrom(
                config = this,
                delay = abs(currentLastActiveStarPos.inc() - starPos) * STAR_ANIM_DELAY,
                starRating = starPos.inc()
            )
        }
    }
}

fun updateNonAffectedStarStates(
    config: WidgetReviewAnimatedRatingConfig,
    starStates: List<MutableState<ReviewStarState>>
) {
    val currentLastActiveStarPos = starStates.indexOfLast {
        it.value is ReviewStarState.Active
    }
    val affectedStarRange = currentLastActiveStarPos..config.rating.dec()
    (Int.ZERO until STAR_COUNT).forEach { starPos ->
        if (starPos !in affectedStarRange || affectedStarRange.first == affectedStarRange.last) {
            starStates[starPos].value = starStates[starPos].value.update(config, starPos.inc())
        }
    }
}

private fun createInactiveWidgetReviewStarStateFrom(
    config: WidgetReviewAnimatedRatingConfig,
    delay: Int,
    starRating: Int
): ReviewStarState = with(config) {
    return ReviewStarState.Inactive(
        color = ReviewStarState.AnimatedState(
            delay = delay,
            value = ReviewStarState.Inactive.COLOR_RES_ID
        ),
        size = ReviewStarState.AnimatedState(
            delay = Int.ZERO,
            value = starSize
        ),
        contentDescription = "$starRating star rating"
    ) { onStarClicked(rating, starRating) }
}

private fun createActiveWidgetReviewStarStateFrom(
    config: WidgetReviewAnimatedRatingConfig,
    delay: Int,
    starRating: Int
): ReviewStarState = with(config) {
    return ReviewStarState.Active(
        color = ReviewStarState.AnimatedState(
            delay = delay,
            value = ReviewStarState.Active.COLOR_RES_ID
        ),
        size = ReviewStarState.AnimatedState(
            delay = Int.ZERO,
            value = starSize
        ),
        contentDescription = "$starRating star rating"
    ) { onStarClicked(rating, starRating) }
}

private fun ReviewStarState.update(
    config: WidgetReviewAnimatedRatingConfig,
    starRating: Int
): ReviewStarState {
    return when (this) {
        is ReviewStarState.Active -> copy(
            size = ReviewStarState.AnimatedState(
                delay = Int.ZERO,
                value = config.starSize
            ),
            onClick = { config.onStarClicked(config.rating, starRating) }
        )
        is ReviewStarState.Inactive -> copy(
            size = ReviewStarState.AnimatedState(
                delay = Int.ZERO,
                value = config.starSize
            ),
            onClick = { config.onStarClicked(config.rating, starRating) }
        )
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

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun WidgetReviewAnimatedRatingPreview() {
    val config by remember { previewConfig }

    NestTheme {
        Column {
            config.configs.forEach { config -> WidgetReviewAnimatedRating(config = config) }
            Button(onClick = ::onEnlargeWidgets) { Text(text = "Enlarge Widgets") }
            Button(onClick = ::onEnlargeSpaceInBetween) { Text(text = "Enlarge Space in Between") }
            Button(onClick = ::onIncreaseRating) { Text(text = "Increase Ratings") }
            Button(onClick = ::onDecreaseRating) { Text(text = "Decrease Ratings") }
            Button(onClick = ::onResetRating) { Text(text = "Reset Ratings") }
        }
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
            spaceInBetween = calculateSpaceInBetween(
                index,
                DEFAULT_PREVIEW_SPACE_IN_BETWEEN_MULTIPLIER
            ),
            skipInitialAnimation = true,
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
            sizeMultiplier = it.sizeMultiplier + 5,
            configs = it.configs.mapIndexed { index, config ->
                config.copy(
                    starSize = calculateStarSize(
                        index = index.inc(),
                        multiplier = it.sizeMultiplier + 5
                    )
                )
            }.toMutableList()
        )
    }
}

private fun onEnlargeSpaceInBetween() {
    previewConfig.value.let {
        previewConfig.value = it.copy(
            spaceInBetweenMultiplier = it.spaceInBetweenMultiplier + 5,
            configs = it.configs.mapIndexed { index, config ->
                config.copy(
                    spaceInBetween = calculateSpaceInBetween(
                        index = index.inc(),
                        multiplier = it.spaceInBetweenMultiplier + 5
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
