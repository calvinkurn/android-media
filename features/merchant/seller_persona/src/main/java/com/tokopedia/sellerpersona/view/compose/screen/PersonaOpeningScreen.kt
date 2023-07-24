package com.tokopedia.sellerpersona.view.compose.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.nest.components.ButtonVariant
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource
import com.tokopedia.sellerpersona.R

/**
 * Created by @ilhamsuaib on 12/07/23.
 */

@Composable
fun PersonaOpeningScreen(
    onImpressed: () -> Unit, onNext: () -> Unit, onBack: () -> Unit
) {
    LaunchedEffect(key1 = Unit, block = {
        onImpressed()
    })
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Illustration(
            modifier = Modifier
                .requiredWidth(280.dp)
                .requiredHeight(210.dp)
        )
        NestTypography(
            modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.CenterHorizontally)
                .padding(start = 16.dp, end = 16.dp, top = 20.dp),
            text = stringResource(id = R.string.sp_opening_title),
            textStyle = NestTheme.typography.heading2.copy(
                color = NestTheme.colors.NN._950,
                textAlign = TextAlign.Center
            )
        )
        NestTypography(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp)
                .align(alignment = Alignment.CenterHorizontally),
            text = stringResource(id = R.string.sp_opening_sub_title),
            textStyle = NestTheme.typography.display3.copy(
                color = NestTheme.colors.NN._600,
                textAlign = TextAlign.Center
            )
        )
        NestTypography(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                .align(alignment = Alignment.CenterHorizontally),
            text = stringResource(id = R.string.sp_opening_footer),
            textStyle = NestTheme.typography.small.copy(
                color = NestTheme.colors.NN._600,
                textAlign = TextAlign.Center
            )
        )
        NestButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 24.dp),
            text = stringResource(id = R.string.sp_start_quiz),
            variant = ButtonVariant.FILLED,
            onClick = onNext
        )
        NestButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp),
            text = stringResource(id = R.string.sp_try_later),
            variant = ButtonVariant.TEXT_ONLY,
            onClick = onBack
        )
    }
}

@Composable
fun Illustration(modifier: Modifier) {
    val source = ImageSource.Remote(
        source = TokopediaImageUrl.IMG_PERSONA_INTRODUCTION,
        loaderType = ImageSource.Remote.LoaderType.NONE
    )
    NestImage(
        source = source,
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = modifier
    )
}

@Preview
@Composable
fun OpeningScreenPreview() {
    NestTheme {
        PersonaOpeningScreen(onImpressed = {}, onNext = {}, onBack = {})
    }
}
