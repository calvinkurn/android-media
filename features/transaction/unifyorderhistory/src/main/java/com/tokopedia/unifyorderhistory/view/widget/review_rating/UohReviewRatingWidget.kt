@file:OptIn(ExperimentalCoilApi::class)

package com.tokopedia.unifyorderhistory.view.widget.review_rating

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.SubcomposeMeasureScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.merchant.DeeplinkMapperMerchant
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.nest.components.card.NestCard
import com.tokopedia.nest.components.card.NestCardType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.shimmerBackground
import com.tokopedia.nest.principles.utils.toAnnotatedString
import com.tokopedia.reviewcommon.feature.reviewer.presentation.widget.review_animated_rating.WidgetReviewAnimatedRating
import com.tokopedia.reviewcommon.feature.reviewer.presentation.widget.review_animated_rating.WidgetReviewAnimatedRatingConfig
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifyorderhistory.R
import com.tokopedia.unifyorderhistory.data.model.UohListOrder
import kotlinx.coroutines.delay

private const val LABEL_WEIGHT = 1f
private const val REVIEW_RATING_WIDGET_STAR_SIZE = 24
private const val REVIEW_RATING_WIDGET_SPACE_IN_BETWEEN = 2
private const val SPACER_WIDTH = 16
private const val SPACER_HEIGHT = 0
private const val CONTENT_PADDING = 8
private const val DELAY_RATING_CHANGED_REDIRECTION = 500L
private const val BACKGROUND_URL = "https://images.tokopedia.net/img/android/buyer_order_management/widget_background_gopay_coin.png"

private enum class Slots { Content, Background }

@Composable
fun UohReviewRatingWidget(config: UohReviewRatingWidgetConfig) {
    var rating by rememberRating(config.componentData)
    SetupRatingChangedListener(rating, config)
    DrawContent(
        modifier = Modifier.fillMaxWidth(),
        config = config,
        reviewRatingWidgetConfig = rememberReviewRatingWidgetConfig(
            config = config,
            rating = rating,
            onRatingChanged = { newRating -> rating = newRating }
        )
    )
}

@Composable
private fun SetupRatingChangedListener(rating: Int, config: UohReviewRatingWidgetConfig) {
    LaunchedEffect(rating) {
        if (rating != Int.ZERO) {
            delay(DELAY_RATING_CHANGED_REDIRECTION)
            config.onRatingChanged(composeAppLink(rating, config.componentData.action.appUrl))
        }
    }
}

@Composable
private fun rememberReviewRatingWidgetConfig(
    config: UohReviewRatingWidgetConfig,
    rating: Int,
    onRatingChanged: (rating: Int) -> Unit
) = remember(config, rating) {
    derivedStateOf {
        WidgetReviewAnimatedRatingConfig(
            rating = rating,
            starSize = REVIEW_RATING_WIDGET_STAR_SIZE.dp,
            spaceInBetween = REVIEW_RATING_WIDGET_SPACE_IN_BETWEEN.dp,
            skipInitialAnimation = true,
            onStarClicked = { previousRating: Int, currentRating: Int ->
                // only process when previously clicked star is not the same as currently clicked star
                // and if user haven't clicked any star previously (only process rating change once)
                if (previousRating != currentRating && previousRating == Int.ZERO) {
                    onRatingChanged(currentRating)
                }
            }
        )
    }
}

@Composable
private fun rememberRating(
    componentData: UohListOrder.UohOrders.Order.Metadata.ExtraComponent
) = remember(componentData) { mutableStateOf(Int.ZERO) }

@Composable
private fun DrawContent(
    modifier: Modifier = Modifier,
    config: UohReviewRatingWidgetConfig,
    reviewRatingWidgetConfig: State<WidgetReviewAnimatedRatingConfig>
) {
    var isLoadingBackground by remember { mutableStateOf(true) }
    val backgroundPainter = rememberImagePainter(
        data = BACKGROUND_URL,
        builder = {
            listener(
                onStart = { isLoadingBackground = true },
                onCancel = { isLoadingBackground = false },
                onError = { _, _ -> isLoadingBackground = false },
                onSuccess = { _, _ -> isLoadingBackground = false }
            )
        }
    )
    val cardBorderColor = Color(LocalContext.current.resources.getColor(R.color.dms_uoh_order_list_divider_color))

    NestCard(
        modifier = modifier.border(1.dp, cardBorderColor, RoundedCornerShape(8.dp)),
        type = NestCardType.NoBorder
    ) {
        SubcomposeLayout(modifier = Modifier.fillMaxWidth()) { constraints ->
            val contentPlaceable = createContentSubCompose(
                config = config,
                reviewRatingWidgetConfig = reviewRatingWidgetConfig
            ).map { it.measure(constraints) }
            val contentWidth = runCatching {
                contentPlaceable.maxOf { it.width }
            }.getOrElse { constraints.maxWidth }
            val contentHeight = runCatching {
                contentPlaceable.maxOf { it.height }
            }.getOrElse { constraints.minHeight }
            val backgroundConstraints = constraints.copy(
                minWidth = contentWidth,
                maxWidth = contentWidth,
                minHeight = contentHeight,
                maxHeight = contentHeight
            )
            val backgroundPlaceable = createBackgroundSubCompose(
                config = config,
                backgroundPainter = backgroundPainter,
                isLoadingBackground = isLoadingBackground
            ).map { it.measure(backgroundConstraints) }
            layout(contentWidth, contentHeight) {
                contentPlaceable.forEach { it.placeRelative(0, 0) }
                backgroundPlaceable.forEach { it.placeRelative(0, 0) }
            }
        }
    }
}

private fun SubcomposeMeasureScope.createContentSubCompose(
    config: UohReviewRatingWidgetConfig,
    reviewRatingWidgetConfig: State<WidgetReviewAnimatedRatingConfig>
): List<Measurable> {
    return subcompose(Slots.Content) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(CONTENT_PADDING.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            NestTypography(
                modifier = Modifier.weight(weight = LABEL_WEIGHT, fill = false),
                text = formatLabel(config.componentData.label),
                textStyle = NestTheme.typography.body3.copy(color = NestTheme.colors.NN._600)
            )
            if (shouldShowRatingWidget(config.componentData.type)) {
                Spacer(modifier = Modifier.size(width = SPACER_WIDTH.dp, height = SPACER_HEIGHT.dp))
                WidgetReviewAnimatedRating(
                    modifier = Modifier, config = reviewRatingWidgetConfig.value
                )
            }
        }
    }
}

private fun SubcomposeMeasureScope.createBackgroundSubCompose(
    config: UohReviewRatingWidgetConfig,
    backgroundPainter: ImagePainter,
    isLoadingBackground: Boolean
): List<Measurable> {
    return subcompose(Slots.Background) {
        if (shouldDrawBackground(config.componentData.type)) {
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .shimmerIf(isLoadingBackground),
                painter = backgroundPainter,
                contentDescription = null,
                alignment = Alignment.TopStart,
                contentScale = ContentScale.FillHeight
            )
        }
    }
}

@Composable
private fun formatLabel(label: String): CharSequence {
    return HtmlLinkHelper(LocalContext.current, label).spannedString?.toAnnotatedString() ?: ""
}

private fun composeAppLink(rating: Int, appLink: String): String {
    return UriUtil.appendDiffDeeplinkWithQuery(
        deeplink = appLink,
        query = "${ApplinkConstInternalMarketplace.CREATE_REVIEW_APP_LINK_PARAM_RATING}=$rating&${DeeplinkMapperMerchant.PARAM_UTM_SOURCE}=uoh_orders"
    )
}

private fun shouldDrawBackground(type: String): Boolean {
    return type == UohListOrder.UohOrders.Order.Metadata.ExtraComponent.TYPE_REVIEW_GOPAY_COINS_WITH_STARS ||
        type == UohListOrder.UohOrders.Order.Metadata.ExtraComponent.TYPE_REVIEW_GOPAY_COINS
}

private fun shouldShowRatingWidget(type: String): Boolean {
    return type == UohListOrder.UohOrders.Order.Metadata.ExtraComponent.TYPE_REVIEW_GOPAY_COINS_WITH_STARS ||
        type == UohListOrder.UohOrders.Order.Metadata.ExtraComponent.TYPE_REVIEW_INTERACTIVE_STARS
}

private fun Modifier.shimmerIf(condition: Boolean): Modifier = this.then(
    if (condition) shimmerBackground() else this
)
