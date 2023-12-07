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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.appdownloadmanager_common.presentation.model.DownloadManagerUpdateModel
import com.tokopedia.nest.components.ButtonSize
import com.tokopedia.nest.components.ButtonVariant
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource

const val DOWNLOAD_MANAGER_ONBOARDING_URL =
    "https://images.tokopedia.net/img/android/appdownloadmanager/app_download_manager_onboarding.png"

@Composable
fun DownloadManagerOnboardingScreen(
    downloadManagerUpdateModel: DownloadManagerUpdateModel?,
    onDownloadClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NestImage(
            source = ImageSource.Remote(DOWNLOAD_MANAGER_ONBOARDING_URL),
            modifier = Modifier.fillMaxWidth().height(240.dp),
            contentScale = ContentScale.Crop
        )

        NestTypography(
            text = downloadManagerUpdateModel?.dialogTitle.orEmpty(),
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
            text = downloadManagerUpdateModel?.dialogText.orEmpty(),
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
            text = downloadManagerUpdateModel?.dialogButtonPositive.orEmpty(),
            variant = ButtonVariant.FILLED,
            size = ButtonSize.LARGE,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, top = 24.dp),
            onClick = { onDownloadClick() }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DownloadManagerOnboardingPreview() {
    DownloadManagerOnboardingScreen(
        DownloadManagerUpdateModel(
            dialogTitle = "Cobain fitur terbaru duluan, yuk!",
            dialogText = "Sekarang kamu bisa cobain fitur baru lebih awal dengan Tokopedia BETA, khusus buat Nakama. Ditunggu juga masukannya, ya~",
            dialogButtonPositive = "Download Tokopedia BETA"
        )
    )
}
