package com.tokopedia.home_account.explicitprofile.personalize.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.tokopedia.home_account.R
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.tag

@Composable
fun SuccessScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LottieAnimationWidget(
            url = LocalContext.current.getString(R.string.explicit_personalize_success_top)
        )

        Column(
            modifier = Modifier.weight(1f).padding(16.dp).fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NestTypography(
                text = LocalContext.current.getString(R.string.explicit_personalize_success_title),
                textStyle = NestTheme.typography.heading2,
                modifier = Modifier.tag("title success page")
            )
            NestTypography(
                text = LocalContext.current.getString(R.string.explicit_personalize_success_subtitle),
                textStyle = NestTheme.typography.paragraph2,
                modifier = Modifier.tag("subtitle success page")
            )
        }

        LottieAnimationWidget(
            url = LocalContext.current.getString(R.string.explicit_personalize_success_bottom)
        )
    }
}

@Composable
fun LottieAnimationWidget(
    url: String
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Url(url))
    LottieAnimation(
        composition = composition,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(37f / 23f)
    )
}

@Preview(device = Devices.PIXEL_3A_XL, showBackground = true)
@Composable
fun SuccessScreenPreview() {
    NestTheme {
        SuccessScreen()
    }
}
