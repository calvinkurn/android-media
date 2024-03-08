package com.tokopedia.home_account.ui.mediaquality

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.home_account.R
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme

@Composable
fun MediaQualitySettingScreen(
    qualities: List<MediaQualityUIModel>,
    modifier: Modifier = Modifier,
    selectedIndex: Int = 0,
    onSelected: (Int) -> Unit = {}
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        NestTypography(
            stringResource(R.string.image_quality_setting_title),
            textStyle = NestTheme.typography.heading4
        )
        NestTypography(
            stringResource(R.string.image_quality_setting_subtitle),
            modifier = Modifier.padding(top = 4.dp),
            textStyle = NestTheme.typography.display2.copy(
                color = NestTheme.colors.NN._950.copy(0.68f)
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        qualities.forEachIndexed { i, v ->
            Row(modifier = Modifier
                .clickable { onSelected(i) }
                .padding(vertical = 8.dp)
            ) {
                RadioButton(
                    selected = selectedIndex == i,
                    modifier = Modifier.size(48.dp),
                    colors = RadioButtonDefaults.colors(MaterialTheme.colors.primary),
                    onClick = null
                )
                Column {
                    NestTypography(
                        text = stringResource(v.title),
                        textStyle = NestTheme.typography.body1
                    )
                    NestTypography(
                        text = stringResource(v.subtitle),
                        textStyle = NestTheme.typography.body2.copy(
                            color = NestTheme.colors.NN._950.copy(0.68f)
                        )
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun MediaQualitySettingPreview() {
    NestTheme {
        MediaQualitySettingScreen(MediaQualityUIModel.settingsMenu())
    }
}
