package com.tokopedia.report.view.fragment.unify_components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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
            CTypography(text = title, type = TextUnifyType.Heading3, weight = TextUnifyWeight.Bold)
        },
        modifier = modifier,
        navigationIcon = {
            IconButton(
                modifier = Modifier.width(48.dp).height(48.dp),
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