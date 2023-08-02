package com.tokopedia.centralizedpromo.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme


fun LazyGridScope.TitleSection(
    title: String,
    modifier: Modifier = Modifier
) = item(span = { GridItemSpan(2) }) {
    PromoSectionTitle(
        text = title,
        modifier = modifier.padding(vertical = 16.dp)
    )
}

@Composable
fun CreatePromotionTitleHeader(titleText: String, descriptionText: String) {
    Column(modifier = Modifier.padding(16.dp).wrapContentHeight().fillMaxWidth()) {
        PromoSectionTitle(
            text = titleText,
            modifier = Modifier.fillMaxWidth()
        )

        NestTypography(
            text = descriptionText,
            modifier = Modifier.padding(top = 4.dp).fillMaxWidth(),
            textStyle = NestTheme.typography.paragraph3.copy(
                color = NestTheme.colors.NN._600
            ),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}

@Composable
fun PromoSectionTitle(text: String, modifier: Modifier = Modifier) {
    NestTypography(
        text = text,
        modifier = modifier,
        textStyle = NestTheme.typography.display1.copy(
            color = NestTheme.colors.NN._950, fontWeight = FontWeight.Bold
        ),
        overflow = TextOverflow.Ellipsis,
        maxLines = 1
    )
}

@Composable
@Preview
private fun CreatePromotionTitleHeaderPreview() {
    NestTheme {
        Surface {
            Column {
                PromoSectionTitle(
                    text = "Firut promosi aktifmu",
                    modifier = Modifier.padding(16.dp)
                )
                CreatePromotionTitleHeader(
                    "Buat Promosi",
                    "Pasang promosi untuk tingkatkan performa tokomu."
                )
            }
        }
    }
}