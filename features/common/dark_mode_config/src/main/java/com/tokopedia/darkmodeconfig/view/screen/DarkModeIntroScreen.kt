package com.tokopedia.darkmodeconfig.view.screen

import android.widget.ImageView
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.nest.components.ButtonVariant
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.darkmodeconfig.R as darkmodeconfigR

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
        val gifImageUrl = rememberSaveable { TokopediaImageUrl.DARK_MODE_INTRO_ANIMATION }
        GifImageView(
            source = gifImageUrl,
            modifier = Modifier
                .fillMaxWidth()
                .height(256.dp)
                .background(
                    color = Color.Transparent,
                    shape = RoundedCornerShape(32.dp)
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

@Composable
private fun GifImageView(
    source: String,
    modifier: Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = {
            ImageUnify(it).apply {
                cornerRadius = 20
                scaleType = ImageView.ScaleType.CENTER_CROP
            }
        },
        update = { imageUnify ->
            Glide.with(imageUnify.context)
                .asGif()
                .load(source)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(imageUnify)
        }
    )
}

@Preview(showBackground = true)
@Composable
internal fun DarkModeInfoScreenPreview() {
    DarkModeIntroScreen({}, {})
}