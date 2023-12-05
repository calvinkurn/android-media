package com.tokopedia.appdownloadmanager_common.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.appdownloadmanager_common.R
import com.tokopedia.appdownloadmanager_common.presentation.model.DownloadingProgressUiModel
import com.tokopedia.nest.components.ButtonSize
import com.tokopedia.nest.components.ButtonVariant
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme

@Composable
fun DownloadManagerDownloadingScreen(
    downloadingProgressUiModel: DownloadingProgressUiModel,
    onCancelClick: () -> Unit = {},
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box {

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                AnimatedCircularProgressIndicator(
                    downloadingProgressUiModel = downloadingProgressUiModel,
                    progressBackgroundColor = NestTheme.colors.NN._100,
                    progressIndicatorColor = NestTheme.colors.GN._500,
                    modifier = modifier.padding(top = 8.dp)
                )

                NestTypography(
                    text = stringResource(id = R.string.is_downloading_title),
                    modifier = modifier.wrapContentSize(Alignment.Center).padding(top = 8.dp),
                    textStyle = NestTheme.typography.heading2.copy(
                        fontWeight = FontWeight.Bold, color = NestTheme.colors.NN._950,
                        textAlign = TextAlign.Center
                    ),
                )

                NestTypography(
                    text = stringResource(id = R.string.is_downloading_desc),
                    modifier = modifier
                        .wrapContentSize(Alignment.Center)
                        .padding(top = 8.dp),
                    textStyle = NestTheme.typography.paragraph2.copy(
                        fontWeight = FontWeight.Normal, color = NestTheme.colors.NN._600,
                        textAlign = TextAlign.Center
                    ),
                )
            }
        }

        NestButton(text = stringResource(id = R.string.is_downloading_cancel_text),
            variant = ButtonVariant.GHOST_ALTERNATE,
            size = ButtonSize.LARGE,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, top = 24.dp),
            onClick = {
                onCancelClick()
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DownloadManagerDownloadingScreenPreview() {
    DownloadManagerDownloadingScreen(
        downloadingProgressUiModel = DownloadingProgressUiModel(
            currentProgressInPercent = 25,
            currentDownloadedSize = "20 MB",
            totalResourceSize = "50 MB",
            isFinishedDownloading = true
        )
    )
}
