package com.tokopedia.appdownloadmanager_common.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.appdownloadmanager_common.R
import com.tokopedia.appdownloadmanager_common.presentation.model.DownloadManagerUpdateModel
import com.tokopedia.nest.components.ButtonSize
import com.tokopedia.nest.components.ButtonVariant
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource
import com.tokopedia.appdownloadmanager_common.R as appdownloadmanager_commonR

const val APP_DOWNLOAD_INSUFFICIENT_SPACE_URL =
    "https://images.tokopedia.net/img/android/appdownloadmanager/app_download_insufficient_space.png"

@Composable
fun AppDownloadInsufficientSpaceScreen(
    onTryAgainClicked: () -> Unit = {},
    onGoToStorageClicked: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NestImage(
            source = ImageSource.Remote(APP_DOWNLOAD_INSUFFICIENT_SPACE_URL),
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp),
            contentScale = ContentScale.Crop
        )

        NestTypography(
            text = stringResource(id = appdownloadmanager_commonR.string.app_download_error_insufficient_space_title),
            modifier = Modifier
                .wrapContentSize(Alignment.Center)
                .padding(top = 16.dp),
            textStyle = NestTheme.typography.heading2.copy(
                fontWeight = FontWeight.Bold,
                color = NestTheme.colors.NN._950,
                textAlign = TextAlign.Center
            )
        )

        NestTypography(
            text = stringResource(id = appdownloadmanager_commonR.string.app_download_error_insufficient_space_desc),
            modifier = Modifier
                .wrapContentSize(Alignment.Center)
                .padding(top = 8.dp),
            textStyle = NestTheme.typography.paragraph2.copy(
                fontWeight = FontWeight.Normal,
                color = NestTheme.colors.NN._600,
                textAlign = TextAlign.Center
            )
        )

        NestButton(
            text = stringResource(id = appdownloadmanager_commonR.string.app_download_error_insufficient_button_primary),
            variant = ButtonVariant.FILLED,
            size = ButtonSize.LARGE,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            onClick = { onGoToStorageClicked() }
        )

        NestButton(
            text = stringResource(id = appdownloadmanager_commonR.string.app_download_try_again),
            variant = ButtonVariant.GHOST,
            size = ButtonSize.LARGE,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, top = 8.dp),
            onClick = { onTryAgainClicked() }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AppDownloadInsufficientSpaceScreenPreview() {
    AppDownloadInsufficientSpaceScreen()
}
