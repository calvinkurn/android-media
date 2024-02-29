package com.tokopedia.home_account.explicitprofile.personalize.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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

@Composable
fun HeaderSection(
    countItemSelected: Int,
    maxItemSelected: Int
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        NestTypography(
            text = LocalContext.current.getString(R.string.explicit_personalize_title),
            textStyle = NestTheme.typography.heading3
        )

        Spacer(modifier = Modifier.height(4.dp))

        NestTypography(
            text = LocalContext.current.getString(R.string.explicit_personalize_subtitle),
            textStyle = NestTheme.typography.paragraph2.copy(
                color = NestTheme.colors.NN._600
            )
        )

        Spacer(modifier = Modifier.height(4.dp))

        NestTypography(
            text = LocalContext.current.getString(
                R.string.explicit_personalize_counter,
                countItemSelected.toString(),
                maxItemSelected.toString()
            ),
            textStyle = NestTheme.typography.paragraph2.copy(
                color = if (countItemSelected == 0)
                    NestTheme.colors.NN._600
                else
                    NestTheme.colors.GN._500
            ),
            modifier = Modifier.align(Alignment.End)
        )
    }
}

@Preview(device = Devices.PIXEL_3A_XL, showBackground = true)
@Composable
fun HeaderSectionSelectedPreview() {
    NestTheme {
        HeaderSection(countItemSelected = 2, maxItemSelected = 10)
    }
}

@Preview(device = Devices.PIXEL_3A_XL, showBackground = true)
@Composable
fun HeaderSectionUnSelectedPreview() {
    NestTheme {
        HeaderSection(countItemSelected = 0, maxItemSelected = 10)
    }
}
