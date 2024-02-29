package com.tokopedia.home_account.explicitprofile.personalize.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.home_account.R
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource

@Composable
fun FailedScreen(
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        NestImage(
            source = ImageSource.Remote(source = LocalContext.current.getString(R.string.fund_and_investment_failed)),
            modifier = Modifier
                .size(width = 200.dp, height = 150.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        NestTypography(
            text = LocalContext.current.getString(R.string.funds_and_investment_failed_page_title),
            textStyle = NestTheme.typography.heading2
        )

        Spacer(modifier = Modifier.height(8.dp))

        NestTypography(
            text = LocalContext.current.getString(R.string.funds_and_investment_failed_page_subtitle),
            textStyle = NestTheme.typography.body2
        )

        Spacer(modifier = Modifier.height(16.dp))

        NestButton(
            text = LocalContext.current.getString(R.string.funds_and_investment_failed_page_button),
            onClick = { onClick() }
        )
    }
}

@Preview(device = Devices.PIXEL_3A_XL, showBackground = true)
@Composable
fun FailedLayoutPreview() {
    NestTheme {
        FailedScreen(onClick = {})
    }
}
