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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.principles.nest_text.NestText
import com.tokopedia.common_compose.principles.nest_text.NestTextType
import com.tokopedia.common_compose.principles.nest_text.NestTextWeight

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
            NestText(text = title, type = NestTextType.Heading3, weight = NestTextWeight.Bold)
        },
        modifier = modifier,
        backgroundColor = colorResource(id = com.tokopedia.unifyprinciples.R.color.Unify_NN0),
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