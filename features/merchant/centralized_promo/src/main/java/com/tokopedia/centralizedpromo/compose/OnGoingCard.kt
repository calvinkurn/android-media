package com.tokopedia.centralizedpromo.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme

@Composable
fun OnGoingCard(
    title: String,
    counter: Int,
    counterTitle: String,
    onTitleClicked: () -> Unit = {},
    onFooterClicked: () -> Unit = {}
) {
    NestCard(
        modifier = Modifier.wrapContentHeight().width(160.dp).padding(start = 1.dp), type = Shadow
    ) {
        Column(verticalArrangement = Arrangement.Center) {
            Row(modifier = Modifier.padding(8.dp)) {
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
            }

            NestDivider(size = NestDividerSize.Small, modifier = Modifier.fillMaxWidth())

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                NestTypography(text = counter.toString(),
                    textStyle = NestTheme.typography.heading4.copy(color = NestTheme.colors.NN._950),
                    modifier = Modifier.padding(8.dp).clickable {
                        onFooterClicked.invoke()
                    })

                NestTypography(
                    text = counterTitle,
                    textStyle = NestTheme.typography.paragraph3.copy(color = NestTheme.colors.NN._600),
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth().clickable {
                        onFooterClicked.invoke()
                    }
                )
            }
        }
    }
}


@Composable
@Preview
fun OnGoingCardPreview() {
    OnGoingCard(
        title = "Tokopedia Play", counter = 2, counterTitle = "Mendatang"
    )
}