package com.tokopedia.unifyorderhistory.view.widget.review_rating

import android.content.res.Resources
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import com.tokopedia.applink.UriUtil
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.nest.components.card.NestCard
import com.tokopedia.nest.components.card.NestCardType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.toAnnotatedString
import com.tokopedia.reviewcommon.feature.reviewer.presentation.widget.review_animated_rating.WidgetReviewAnimatedRating
import com.tokopedia.reviewcommon.feature.reviewer.presentation.widget.review_animated_rating.WidgetReviewAnimatedRatingConfig
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifyorderhistory.R
import com.tokopedia.unifyorderhistory.data.model.UohListOrder

private const val LABEL_WEIGHT = 1f
private const val LABEL_PADDING = 8
private const val REVIEW_RATING_WIDGET_PADDING = 8
private const val REVIEW_RATING_WIDGET_STAR_SIZE = 18
private const val REVIEW_RATING_WIDGET_SPACE_IN_BETWEEN = 8

@Composable
fun UohReviewRatingWidget(
    modifier: Modifier = Modifier,
    config: UohReviewRatingWidgetConfig
) {
    var rating by remember(config.componentData) { mutableStateOf(Int.ZERO) }
    val reviewRatingWidgetConfig by remember(config) {
        derivedStateOf {
            WidgetReviewAnimatedRatingConfig(
                rating = rating,
                starSize = REVIEW_RATING_WIDGET_STAR_SIZE.dp,
                spaceInBetween = REVIEW_RATING_WIDGET_SPACE_IN_BETWEEN.dp,
                skipInitialAnimation = true,
                onStarClicked = { previousRating: Int, currentRating: Int ->
                    if (previousRating != currentRating) {
                        rating = currentRating
                        config.onRatingChanged(composeAppLink(rating, config.componentData.action.appUrl))
                    }
                }
            )
        }
    }
    AnimatedVisibility(
        visible = config.show,
        enter = EnterTransition.None,
        exit = shrinkVertically()
    ) {
        val resources = LocalContext.current.resources

        NestCard(
            modifier = modifier,
            type = NestCardType.Border
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind { setupBackground(config.componentData.type, resources) },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                NestTypography(
                    modifier = Modifier
                        .weight(weight = LABEL_WEIGHT, fill = false)
                        .padding(LABEL_PADDING.dp),
                    text = formatLabel(config.componentData.label),
                    textStyle = NestTheme.typography.body3.copy(color = NestTheme.colors.NN._600)
                )
                if (shouldShowRatingWidget(config.componentData.type)) {
                    WidgetReviewAnimatedRating(
                        modifier = Modifier.padding(REVIEW_RATING_WIDGET_PADDING.dp),
                        config = reviewRatingWidgetConfig
                    )
                }
            }
        }
    }
}

@Composable
private fun formatLabel(label: String): CharSequence {
    return HtmlLinkHelper(LocalContext.current, label).spannedString?.toAnnotatedString() ?: ""
}

private fun composeAppLink(rating: Int, appLink: String): String {
    return UriUtil.buildUriAppendParam(
        uri = appLink,
        queryParameters = mapOf("rating" to rating.toString())
    )
}

private fun DrawScope.setupBackground(type: String, resources: Resources) {
    if (shouldDrawBackground(type)) {
        drawImage(ImageBitmap.imageResource(res = resources, id = R.drawable.bg_uoh_review_rating))
    }
}

private fun shouldDrawBackground(type: String): Boolean {
    return type == UohListOrder.UohOrders.Order.Metadata.ExtraComponent.TYPE_REVIEW_GOPAY_COINS_WITH_STARS
}

private fun shouldShowRatingWidget(type: String): Boolean {
    return type == UohListOrder.UohOrders.Order.Metadata.ExtraComponent.TYPE_REVIEW_GOPAY_COINS_WITH_STARS ||
        type == UohListOrder.UohOrders.Order.Metadata.ExtraComponent.TYPE_REVIEW_INTERACTIVE_STARS
}
