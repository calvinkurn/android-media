package com.tokopedia.home_account.ui.fundsAndInvestment.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.tokopedia.nest.components.loader.NestLoader
import com.tokopedia.nest.components.loader.NestLoaderType
import com.tokopedia.nest.components.loader.NestShimmerType
import com.tokopedia.nest.principles.ui.NestTheme

@Composable
fun ShimmerLayout() {
    Column {
        Spacer(modifier = Modifier.height(dp_16))
        SectionShimmer(3)
        Spacer(modifier = Modifier.height(dp_24))
        SectionShimmer(1)
    }
}

@Composable
fun SectionShimmer(listSize: Int) {
    Column {
        NestLoader(
            modifier = Modifier
                .padding(start = dp_16)
                .width(dp_100)
                .height(dp_24),
            variant = NestLoaderType.Shimmer(type = NestShimmerType.Line)
        )

        for (i in 1..listSize) {
            ItemShimmer()
        }
    }
}

@Composable
fun ItemShimmer() {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            NestLoader(
                modifier = Modifier
                    .padding(
                        start = dp_24,
                        top = dp_16,
                        end = dp_16,
                        bottom = dp_16
                    )
                    .size(dp_48),
                variant = NestLoaderType.Shimmer(type = NestShimmerType.Line)
            )

            ContentShimmer(
                modifier = Modifier.weight(1F)
            )

        }
    }
}


@Composable
private fun ContentShimmer(
    modifier: Modifier
) {
    Column(
        modifier = modifier
    ) {
        NestLoader(
            modifier = Modifier
                .width(dp_72),
            variant = NestLoaderType.Shimmer(type = NestShimmerType.Line)
        )

        Spacer(modifier = Modifier.height(dp_8))

        NestLoader(
            modifier = Modifier
                .width(dp_120),
            variant = NestLoaderType.Shimmer(type = NestShimmerType.Line)
        )
    }
}

@Preview(device = Devices.PIXEL_3A_XL, showBackground = true)
@Composable
fun ItemShimmerPreview() {
    NestTheme {
        ShimmerLayout()
    }
}
