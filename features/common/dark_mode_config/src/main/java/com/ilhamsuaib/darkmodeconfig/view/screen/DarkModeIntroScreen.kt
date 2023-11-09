package com.ilhamsuaib.darkmodeconfig.view.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilhamsuaib.darkmodeconfig.view.component.LottieAnimationComponent
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.nest.components.ButtonVariant
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.ilhamsuaib.darkmodeconfig.R as darkmodeconfigR

/**
 * Created by @ilhamsuaib on 07/11/23.
 */

@Composable
internal fun DarkModeIntroScreen(
    onPrimaryClicked: () -> Unit,
    onSecondaryClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val lottieRes = rememberSaveable { TokopediaImageUrl.DARK_MODE_INTRO_LOTTIE }
        LottieAnimationComponent(
            source = lottieRes,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(
                    color = NestTheme.colors.NN._50,
                    shape = RoundedCornerShape(16.dp)
                )
        )
        NestTypography(
            text = stringResource(id = darkmodeconfigR.string.dmc_intro_title),
            textStyle = NestTheme.typography.heading2.copy(
                fontWeight = FontWeight.Bold,
                color = NestTheme.colors.NN._950,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
        )
        NestTypography(
            text = stringResource(id = darkmodeconfigR.string.dmc_intro_description),
            textStyle = NestTheme.typography.display2.copy(
                color = NestTheme.colors.NN._600,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
        )
        NestButton(
            text = stringResource(id = darkmodeconfigR.string.dmc_intro_primary_button),
            onClick = onPrimaryClicked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        )
        NestButton(
            text = stringResource(id = darkmodeconfigR.string.dmc_intro_secondary_button),
            onClick = onSecondaryClicked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 16.dp),
            variant = ButtonVariant.GHOST_ALTERNATE
        )
    }
}

@Preview(showBackground = true)
@Composable
internal fun DarkModeInfoScreenPreview() {
    DarkModeIntroScreen({}, {})
}