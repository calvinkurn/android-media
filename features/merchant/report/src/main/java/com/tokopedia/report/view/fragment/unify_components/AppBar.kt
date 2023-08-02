package com.tokopedia.report.view.fragment.unify_components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme

/**
 * Created by yovi.putra on 05/10/22"
 * Project name: android-tokopedia-core
 **/

@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    title: String,
    navigationClick: () -> Unit = {}
) {
    TopAppBar(
        modifier = modifier,
        backgroundColor = NestTheme.colors.NN._0
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                modifier = Modifier
                    .width(48.dp)
                    .height(48.dp),
                onClick = navigationClick
            ) {
                Icon(
                    Icons.Outlined.ArrowBack,
                    contentDescription = "navigationIcon",
                    tint = NestTheme.colors.NN._900
                )
            }

            NestTypography(
                text = title,
                textStyle = NestTheme.typography.heading4.copy(
                    fontWeight = FontWeight.Bold,
                    color = NestTheme.colors.NN._900
                )
            )
        }
    }
}

@Preview
@Composable
fun AppBarPreview() {
    AppBar(title = "Title")
}
