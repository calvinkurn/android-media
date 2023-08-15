package com.tokopedia.centralizedpromo.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.centralizedpromo.R
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.components.NestLabel
import com.tokopedia.nest.components.NestLabelType.HIGHLIGHT_DARK_RED
import com.tokopedia.nest.components.card.NestCard
import com.tokopedia.nest.components.card.NestCardType
import com.tokopedia.nest.components.loader.NestLoader
import com.tokopedia.nest.components.loader.NestLoaderType.Shimmer
import com.tokopedia.nest.components.loader.NestShimmerType
import com.tokopedia.nest.components.loader.NestShimmerType.Rect
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource

@Composable
fun PromotionCard(
    title: String,
    labelNew: String,
    description: String,
    notAvailableText: String,
    imageUrl: String,
    modifier: Modifier = Modifier,
    onPromoClicked: () -> Unit
) {
    NestCard(
        modifier = modifier.heightIn(min = 130.dp).fillMaxWidth(),
        type = NestCardType.Shadow,
        onClick = onPromoClicked
    ) {
        Column {
            Row(modifier = Modifier) {
                NestTypography(
                    text = title,
                    modifier = Modifier.padding(start = 12.dp, top = 8.dp, end = 4.dp).weight(1f),
                    overflow = TextOverflow.Ellipsis,
                    textStyle = NestTheme.typography.display3.copy(
                        color = NestTheme.colors.NN._950,
                        fontWeight = FontWeight.Bold
                    ),
                    maxLines = 2
                )
                ImageWithBackground(ImageSource.Remote(imageUrl), labelNew)
            }

            val context = LocalContext.current
            val desc = if (notAvailableText.isEmpty()) {
                description
            } else {
                context.getString(
                    R.string.centralized_promo_description,
                    notAvailableText,
                    description
                ).parseAsHtml()
            }

            NestTypography(
                text = desc,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 15.dp),
                textStyle = NestTheme.typography.small.copy(color = NestTheme.colors.NN._600),
                overflow = TextOverflow.Ellipsis,
                maxLines = 3
            )
        }
    }
}

@Composable
private fun ImageWithBackground(imageSource: ImageSource, labelNew: String) {
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

        if (labelNew == "Baru") {
            NestLabel(
                labelText = labelNew,
                labelType = HIGHLIGHT_DARK_RED,
                modifier = Modifier.align(Alignment.BottomCenter).offset(y = 10.dp)
            )
        }
    }
}

fun LazyGridScope.PromotionCardShimmerGrid(modifier: Modifier = Modifier) = items(6) {
    PromotionCardShimmer(modifier = modifier.padding(top = 8.dp))
}

@Composable
private fun PromotionCardShimmer(modifier: Modifier = Modifier) {
    NestCard(
        modifier = modifier.heightIn(min = 130.dp).fillMaxWidth(),
        type = NestCardType.Shadow
    ) {
        Column {
            Row {
                Column(
                    modifier = Modifier.weight(1F).padding(start = 12.dp, top = 10.dp, end = 16.dp)
                ) {
                    NestLoader(
                        variant = Shimmer(type = Rect(rounded = 8.dp)),
                        modifier = Modifier.fillMaxWidth().height(12.dp)
                    )

                    Spacer(modifier = Modifier.padding(4.dp))

                    NestLoader(
                        variant = Shimmer(type = Rect(rounded = 8.dp)),
                        modifier = Modifier.fillMaxWidth(0.3F).height(12.dp)
                    )
                }

                Box(
                    modifier = Modifier.size(45.dp).clip(RoundedCornerShape(bottomStart = 8.dp))
                        .background(NestTheme.colors.NN._50)
                ) {
                    Box(modifier = Modifier.align(Alignment.Center)) {
                        NestLoader(
                            variant = Shimmer(type = NestShimmerType.Circle),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 15.dp)) {
                NestLoader(
                    variant = Shimmer(type = Rect(rounded = 8.dp)),
                    modifier = Modifier.fillMaxWidth().height(12.dp)
                )

                Spacer(modifier = Modifier.padding(4.dp))

                NestLoader(
                    variant = Shimmer(type = Rect(rounded = 8.dp)),
                    modifier = Modifier.fillMaxWidth(0.6F).height(12.dp)
                )
            }
        }
    }
}

@Composable
@Preview(name = "NEXUS_7", device = Devices.NEXUS_5)
private fun PromotionCardPreview() {
    NestTheme {
        PromotionCard(
            title = "Flash Sale Tokopedia",
            description = "Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli",
            imageUrl = "",
            labelNew = "Baru",
            notAvailableText = ""
        ) {

        }
    }
}

@Composable
@Preview
private fun ImageWithBackgroundPreview() {
    ImageWithBackground(ImageSource.Painter(R.drawable.ic_voucher_waktu), "Baru")
}

@Composable
@Preview
private fun PromotionCardShimmerPreview() {
    PromotionCardShimmer()
}

@Composable
@Preview(name = "NEXUS_7", device = Devices.NEXUS_5)
private fun PromotionCardGridPreview() {
    NestTheme {
        Surface(
            modifier = Modifier.fillMaxHeight(),
            contentColor = NestTheme.colors.NN._0
        ) {
            LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                items(10) {
                    PromotionCard(
                        title = "Flash Sale Tokopedia",
                        description = "Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli Iklankan produkmu untuk menjangkau lebih banyak pembeli",
                        imageUrl = "",
                        labelNew = "Baru",
                        notAvailableText = ""
                    ) {

                    }
                }
            }
        }
    }
}
