package com.tokopedia.centralizedpromo.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.centralizedpromo.R
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.components.NestLabel
import com.tokopedia.nest.components.NestLabelType.HIGHLIGHT_DARK_RED
import com.tokopedia.nest.components.card.NestCard
import com.tokopedia.nest.components.card.NestCardType
import com.tokopedia.nest.components.loader.NestLoader
import com.tokopedia.nest.components.loader.NestLoaderType.Shimmer
import com.tokopedia.nest.components.loader.NestShimmerType.Circle
import com.tokopedia.nest.components.loader.NestShimmerType.Rect
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource

@Composable
fun PromotionCard() {
    NestCard(
        modifier = Modifier.padding(4.dp).wrapContentHeight().fillMaxWidth(),
        type = NestCardType.Shadow
    ) {
        Column {
            Row(modifier = Modifier) {
                NestTypography(
                    text = "Broadcast Chat",
                    modifier = Modifier.padding(start = 12.dp, top = 10.dp).weight(1f),
                    overflow = TextOverflow.Ellipsis,
                    textStyle = NestTheme.typography.display3.copy(
                        color = NestTheme.colors.NN._950,
                        fontWeight = FontWeight.Bold
                    ),
                    maxLines = 2
                )
                ImageWithBackground(ImageSource.Painter(R.drawable.ic_voucher_waktu))
            }

            NestTypography(
                text = "Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 15.dp),
                textStyle = NestTheme.typography.small.copy(color = NestTheme.colors.NN._600),
                overflow = TextOverflow.Ellipsis,
                maxLines = 3
            )
        }
    }
}

@Composable
fun PromotionCardShimmer() {
    NestCard(
        modifier = Modifier.padding(4.dp).wrapContentHeight().fillMaxWidth(),
        type = NestCardType.Shadow
    ) {
        Column {
            Row(modifier = Modifier) {
                Column(
                    modifier = Modifier.padding(start = 12.dp, top = 10.dp, end = 17.dp).weight(1F)
                ) {
                    NestLoader(
                        variant = Shimmer(type = Rect(rounded = 8.dp)),
                        modifier = Modifier.height(12.dp).fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    NestLoader(
                        variant = Shimmer(type = Rect(rounded = 8.dp)),
                        modifier = Modifier.fillMaxWidth(0.3F).height(12.dp)
                    )
                }

                ImageWithBackgroundShimmer()
            }

            Column(
                modifier = Modifier.padding(
                    top = 12.dp,
                    start = 12.dp,
                    end = 12.dp,
                    bottom = 26.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                NestLoader(
                    variant = Shimmer(type = Rect(rounded = 8.dp)),
                    modifier = Modifier.height(12.dp).fillMaxWidth()
                )

                NestLoader(
                    variant = Shimmer(type = Rect(rounded = 8.dp)),
                    modifier = Modifier.height(12.dp).fillMaxWidth(0.6F)
                )
            }
        }
    }
}

@Composable
private fun ImageWithBackground(imageSource: ImageSource) {
    Box(
        modifier = Modifier.padding(bottom = 10.dp).wrapContentSize()
    ) {

        Box(
            modifier = Modifier.size(45.dp).clip(RoundedCornerShape(bottomStart = 8.dp))
                .background(NestTheme.colors.TN._50)
        )

        NestImage(
            source = imageSource,
            modifier = Modifier.size(22.dp).align(Alignment.Center),
            contentDescription = null
        )

        NestLabel(
            labelText = "Baru",
            labelType = HIGHLIGHT_DARK_RED,
            modifier = Modifier.align(Alignment.BottomCenter).offset(y = 10.dp)
        )
    }
}

@Composable
private fun ImageWithBackgroundShimmer() {
    Box(
        modifier = Modifier.padding(bottom = 10.dp).wrapContentSize()
    ) {

        Box(
            modifier = Modifier.size(45.dp).clip(RoundedCornerShape(bottomStart = 8.dp))
                .background(NestTheme.colors.NN._50)
        )

        NestLoader(
            variant = Shimmer(type = Circle),
            modifier = Modifier.size(22.dp).align(Alignment.Center)
        )
    }
}

@Composable
@Preview()
private fun PromotionCardPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        PromotionCard()
        PromotionCardShimmer()
    }
}
