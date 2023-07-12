package com.tokopedia.centralizedpromo.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme

@Composable
fun CreatePromotionTittleHeader() {
    Column(modifier = Modifier.padding(16.dp).wrapContentHeight().fillMaxWidth()) {
        PromoSectionTittle(
            text = "Buat Promosi",
            modifier = Modifier.fillMaxWidth()
        )

        NestTypography(
            text = "Pasang promosi untuk tingkatkan performa tokomu.",
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
private fun PromoSectionTittle(text: String, modifier: Modifier = Modifier) {
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
private fun CreatePromotionTittleHeaderPreview() {
    NestTheme {
        Surface {
            Column {
                PromoSectionTittle(
                    text = "Firut promosi aktifmu",
                    modifier = Modifier.padding(16.dp)
                )
                CreatePromotionTittleHeader()
            }
        }
    }
}