package com.tokopedia.centralizedpromo.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.nest.components.NestDivider
import com.tokopedia.nest.components.NestDividerSize
import com.tokopedia.nest.components.card.NestCard
import com.tokopedia.nest.components.card.NestCardType.Shadow
import com.tokopedia.nest.components.loader.NestLoader
import com.tokopedia.nest.components.loader.NestLoaderType.Shimmer
import com.tokopedia.nest.components.loader.NestShimmerType.Circle
import com.tokopedia.nest.components.loader.NestShimmerType.Rect
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme

@Composable
fun OnGoingCard() {
    OnGoingCardContainer(modifier = Modifier,
        topContent = {
            Row(modifier = Modifier.padding(8.dp)) {
                NestTypography(
                    text = "Tokopedia Play",
                    textStyle = NestTheme.typography.display3.copy(
                        color = NestTheme.colors.NN._950,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                NestIcon(
                    modifier = Modifier.size(18.dp),
                    iconId = IconUnify.CHEVRON_RIGHT,
                    colorLightEnable = NestTheme.colors.NN._500
                )
            }
        },
        bottomContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                NestTypography(
                    text = "1",
                    textStyle = NestTheme.typography.heading4,
                    modifier = Modifier.padding(8.dp)
                )

                NestTypography(
                    text = "On Going",
                    textStyle = NestTheme.typography.paragraph3
                        .copy(color = NestTheme.colors.NN._600),
                    modifier = Modifier.fillMaxWidth(),
                    overflow = TextOverflow.Ellipsis,
                )
            }
        })
}

@Composable
fun OnGoingCardShimmer() {
    OnGoingCardContainer(
        modifier = Modifier,
        topContent = {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                NestLoader(
                    variant = Shimmer(type = Circle),
                    modifier = Modifier
                        .size(16.dp)
                )

                NestLoader(
                    variant = Shimmer(type = Rect(rounded = 8.dp)),
                    modifier = Modifier.fillMaxWidth().height(12.dp).padding(start = 6.dp)
                )
            }
        },
        bottomContent = {
            Box(modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp)) {
                NestLoader(
                    variant = Shimmer(type = Rect(rounded = 8.dp)),
                    modifier = Modifier.width(60.dp).height(12.dp)
                )
            }
        }
    )
}

@Composable
private fun OnGoingCardContainer(
    modifier: Modifier = Modifier,
    topContent: @Composable () -> Unit,
    bottomContent: @Composable () -> Unit
) {
    NestCard(
        modifier = modifier.wrapContentHeight()
            .width(160.dp),
        type = Shadow,
    ) {
        Column(verticalArrangement = Arrangement.Center) {
            topContent()

            NestDivider(size = NestDividerSize.Small, modifier = Modifier.fillMaxWidth())

            bottomContent()
        }
    }
}


@Composable
@Preview
private fun OnGoingCardPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        OnGoingCard()
        OnGoingCardShimmer()
    }
}