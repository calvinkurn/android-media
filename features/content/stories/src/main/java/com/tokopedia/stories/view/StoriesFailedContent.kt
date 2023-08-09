package com.tokopedia.stories.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.nest.components.ButtonVariant
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.stories.R
import com.tokopedia.unifyprinciples.R as unifyR


/**
 * @author by astidhiyaa on 09/08/23
 */

@Composable
fun StoriesFailedLoad(
    onRetryClicked: (Unit) -> Unit
) {
    val ctx = LocalContext.current
    NestTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(id = unifyR.color.Unify_Static_Black)),
        ) {
            val iconDrawable = getIconUnifyDrawable(
                ctx,
                IconUnify.RELOAD,
                MethodChecker.getColor(
                    ctx,
                    unifyR.color.Unify_Static_White
                )
            )
            Image(
                painter = rememberImagePainter(iconDrawable), contentDescription = "",
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.CenterHorizontally),
            )
            NestTypography(
                text = ctx.getString(R.string.stories_retry_description),
                textStyle = NestTheme.typography.heading3,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.CenterHorizontally)
            )
            NestButton(
                text = ctx.getString(R.string.stories_retry),
                variant = ButtonVariant.GHOST,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.CenterHorizontally),
                onClick = { onRetryClicked(Unit) })
        }
    }
}

@Preview
@Composable
private fun StoriesFailedLoadPage() {
    StoriesFailedLoad(onRetryClicked = {})
}
