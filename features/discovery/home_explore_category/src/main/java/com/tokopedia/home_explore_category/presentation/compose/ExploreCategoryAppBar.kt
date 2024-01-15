package com.tokopedia.home_explore_category.presentation.compose

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.home_explore_category.R
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestNN
import com.tokopedia.nest.principles.ui.NestTheme

@Composable
fun ExploreCategoryAppBar(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    title: String,
    navigationClick: () -> Unit = {}
) {
    TopAppBar(
        modifier = modifier,
        backgroundColor = backgroundColor,
        elevation = 0.dp
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                modifier = Modifier
                    .width(44.dp)
                    .height(44.dp),
                onClick = navigationClick
            ) {
                NestIcon(
                    IconUnify.ARROW_BACK,
                    modifier = Modifier.size(24.dp),
                    contentDescription = "exploreCategoryNavigationIcon"
                )
            }

            NestTypography(
                text = title,
                textStyle = NestTheme.typography.display1.copy(
                    fontWeight = FontWeight.Bold,
                    color = NestTheme.colors.NN._900
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, end = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExploreCategoryAppBarPreview() {
    ExploreCategoryAppBar(
        backgroundColor = NestNN.light._50,
        title = stringResource(id = R.string.title_home_browse_all_category)
    )
}
