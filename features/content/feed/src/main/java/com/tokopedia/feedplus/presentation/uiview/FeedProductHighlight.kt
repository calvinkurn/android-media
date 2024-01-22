package com.tokopedia.feedplus.presentation.uiview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.nest.components.card.NestCard
import com.tokopedia.nest.principles.ui.NestTheme

/**
 * @author by astidhiyaa on 22/01/24
 */
@Composable
fun FeedProductHighlight() {
    NestTheme {
        AnimatedVisibility(
            visible = true,
            enter = slideInVertically(),
            exit = slideOutVertically()
        ) {

            NestCard {

            }
            NestIcon(iconId = IconUnify.CLOSE)
        }
    }
}
@Preview(showBackground = true)
@Composable
internal fun ProductHighlightPreview() {
    FeedProductHighlight()
}
