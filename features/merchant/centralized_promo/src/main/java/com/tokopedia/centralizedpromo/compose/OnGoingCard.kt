package com.tokopedia.centralizedpromo.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
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
import com.tokopedia.nest.components.loader.NestLoaderType
import com.tokopedia.nest.components.loader.NestShimmerType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.tag


@Composable
fun OnGoingCard(
    title: String,
    counter: Int,
    counterTitle: String,
    modifier: Modifier = Modifier,
    onTitleClicked: () -> Unit = {},
    onFooterClicked: () -> Unit = {}
) {
    OnGoingCardContainer(
        modifier = modifier.tag("tvOnGoingPromoTitle"),
        headerContent = {
            NestTypography(
                text = title.uppercase(),
                textStyle = NestTheme.typography.display3.copy(
                    color = NestTheme.colors.NN._950,
                    fontWeight = FontWeight.Bold
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f).clickable {
                    onTitleClicked.invoke()
                }
            )

            NestIcon(
                modifier = Modifier.size(18.dp).clickable {
                    onTitleClicked.invoke()
                },
                iconId = IconUnify.CHEVRON_RIGHT,
                colorLightEnable = NestTheme.colors.NN._500
            )
        },
        footerContent = {
            NestTypography(text = counter.toString(),
                textStyle = NestTheme.typography.heading4.copy(color = NestTheme.colors.NN._950),
                modifier = Modifier
                    .tag("tvOnGoingPromoCount")
                    .clickable {
                    onFooterClicked.invoke()
                })

            NestTypography(
                text = counterTitle,
                textStyle = NestTheme.typography.paragraph3.copy(color = NestTheme.colors.NN._600),
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .tag("tvOnGoingPromoStatus")
                    .fillMaxWidth()
                    .padding(start = 8.dp)
                    .clickable {
                    onFooterClicked.invoke()
                }
            )
        })
}

@Composable
private fun OnGoingCardContainer(
    headerContent: @Composable RowScope.() -> Unit,
    footerContent: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier
) {
    NestCard(
        modifier = modifier.wrapContentHeight().width(160.dp).padding(start = 1.dp), type = Shadow
    ) {
        Column(verticalArrangement = Arrangement.Center) {
            Row(modifier = Modifier.padding(8.dp)) {
                headerContent.invoke(this)
            }

            NestDivider(size = NestDividerSize.Small, modifier = Modifier.fillMaxWidth())

            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                footerContent.invoke(this)
            }
        }
    }
}

fun LazyGridScope.OnGoingCardShimmerRow() = item(
    span = { GridItemSpan(2) }
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(3) {
            OnGoingCardShimmer()
        }
    }
}

@Composable
private fun OnGoingCardShimmer() {
    OnGoingCardContainer(
        headerContent = {
            NestLoader(
                variant = NestLoaderType.Shimmer(type = NestShimmerType.Circle),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            NestLoader(
                variant = NestLoaderType.Shimmer(type = NestShimmerType.Rect(rounded = 8.dp)),
                modifier = Modifier.fillMaxWidth(0.8F).height(12.dp)
                    .align(alignment = Alignment.CenterVertically)
            )
        },
        footerContent = {
            Box(modifier = Modifier.padding(vertical = 4.dp)) {
                NestLoader(
                    variant = NestLoaderType.Shimmer(type = NestShimmerType.Rect(rounded = 8.dp)),
                    modifier = Modifier.fillMaxWidth(0.4F).height(12.dp)
                )
            }
        }
    )
}


@Composable
@Preview
fun OnGoingCardPreview() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OnGoingCardShimmer()
        OnGoingCard(
            title = "Tokopedia Play", counter = 2, counterTitle = "Mendatang"
        )
    }

}