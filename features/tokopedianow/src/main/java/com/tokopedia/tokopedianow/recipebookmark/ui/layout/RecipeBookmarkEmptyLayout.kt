package com.tokopedia.tokopedianow.recipebookmark.ui.layout

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.nest.components.ButtonVariant
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource
import com.tokopedia.tokopedianow.R

@Composable
fun RecipeBookmarkEmptyLayout() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NestTheme.colors.NN._0),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NestImage(
            modifier = Modifier
                .width(160.dp)
                .height(120.dp)
                .padding(top = 16.dp),
            source = ImageSource.Remote(TokopediaImageUrl.TOKO_NOW_NO_DATA_IMAGE)
        )

        NestTypography(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    top = 19.dp,
                    end = 16.dp
                ),
            text = stringResource(
                R.string.tokopedianow_recipe_bookmark_empty_state_title
            ),
            textStyle = NestTheme.typography.heading2.copy(
                textAlign = TextAlign.Center,
                color = NestTheme.colors.NN._1000
            )
        )

        NestTypography(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    top = 12.dp,
                    end = 16.dp
                ),
            text = stringResource(
                R.string.tokopedianow_recipe_bookmark_empty_state_description
            ),
            textStyle = NestTheme.typography.body2.copy(
                textAlign = TextAlign.Center,
                color = NestTheme.colors.NN._600
            )
        )

        NestButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 60.dp,
                    top = 24.dp,
                    end = 60.dp
                ),
            text = stringResource(
                R.string.tokopedianow_recipe_bookmark_empty_state_button
            ),
            onClick = {
                val intent = RouteManager.getIntent(context, ApplinkConstInternalTokopediaNow.RECIPE_HOME)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                context.startActivity(intent)
            },
            variant = ButtonVariant.GHOST
        )
    }
}

@Preview
@Composable
private fun RecipeBookmarkEmptyLayoutPreview() {
    RecipeBookmarkEmptyLayout()
}
