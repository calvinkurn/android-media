package com.tokopedia.common_compose.principles

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.ui.NestTheme

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
        title = {
            NestTypography(
                text = title,
                textStyle = NestTheme.typography.heading4.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        },
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.background,
        navigationIcon = {
            IconButton(
                modifier = Modifier
                    .width(48.dp)
                    .height(48.dp),
                onClick = navigationClick
            ) {
                Icon(Icons.Outlined.ArrowBack, contentDescription = "navigationIcon")
            }
        }
    )
}

@Preview
@Composable
fun AppBarPreview() {
    AppBar(title = "Title")
}
