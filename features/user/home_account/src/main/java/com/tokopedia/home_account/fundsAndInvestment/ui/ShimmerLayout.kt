package com.tokopedia.home_account.fundsAndInvestment.ui

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
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.components.loader.NestLoader
import com.tokopedia.nest.components.loader.NestLoaderType
import com.tokopedia.nest.components.loader.NestShimmerType
import com.tokopedia.nest.principles.ui.NestTheme

@Composable
fun ShimmerLayout() {
    Column {
        Spacer(modifier = Modifier.height(8.dp))
        SectionShimmer(3)
        Spacer(modifier = Modifier.height(16.dp))
        SectionShimmer(1)
    }
}

@Composable
fun SectionShimmer(listSize: Int) {
    Column {
        NestLoader(
            modifier = Modifier
                .padding(start = 12.dp)
                .width(100.dp)
                .height(24.dp),
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
                    .padding(12.dp)
                    .size(48.dp),
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
                .width(72.dp),
            variant = NestLoaderType.Shimmer(type = NestShimmerType.Line)
        )

        Spacer(modifier = Modifier.height(8.dp))

        NestLoader(
            modifier = Modifier
                .width(120.dp),
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
