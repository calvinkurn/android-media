package com.tokopedia.home_account.explicitprofile.personalize.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.home_account.R
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.tag

@Composable
fun HeaderSection(
    countItemSelected: Int,
    maxItemSelected: Int,
    minItemSelected: Int,
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        NestTypography(
            text = LocalContext.current.getString(R.string.explicit_personalize_title),
            textStyle = NestTheme.typography.heading3,
            modifier = Modifier.tag("title header section")
        )

        Spacer(modifier = Modifier.height(4.dp))

        NestTypography(
            text = LocalContext.current.getString(
                R.string.explicit_personalize_subtitle,
                minItemSelected.toString()
            ),
            textStyle = NestTheme.typography.paragraph2.copy(
                color = NestTheme.colors.NN._600
            ),
            modifier = Modifier.tag("subtitle header section")
        )

        Spacer(modifier = Modifier.height(4.dp))

        NestTypography(
            text = LocalContext.current.getString(
                R.string.explicit_personalize_counter,
                if (maxItemSelected == 0) {
                    countItemSelected.toString()
                } else {
                    "$countItemSelected/$maxItemSelected"
                }
            ),
            textStyle = NestTheme.typography.paragraph2.copy(
                color = if (countItemSelected == 0)
                    NestTheme.colors.NN._600
                else
                    NestTheme.colors.GN._500
            ),
            modifier = Modifier.align(Alignment.End)
                .tag("counter header section")
        )
    }
}

@Preview(device = Devices.PIXEL_3A_XL, showBackground = true)
@Composable
fun HeaderSectionSelectedPreview() {
    NestTheme {
        HeaderSection(countItemSelected = 2, maxItemSelected = 10, minItemSelected = 1)
    }
}

@Preview(device = Devices.PIXEL_3A_XL, showBackground = true)
@Composable
fun HeaderSectionUnSelectedPreview() {
    NestTheme {
        HeaderSection(countItemSelected = 0, maxItemSelected = 10, minItemSelected = 1)
    }
}

@Preview(device = Devices.PIXEL_3A_XL, showBackground = true)
@Composable
fun HeaderSectionNullMaxAnswerPreview() {
    NestTheme {
        HeaderSection(countItemSelected = 0, maxItemSelected = 0, minItemSelected = 0)
    }
}
