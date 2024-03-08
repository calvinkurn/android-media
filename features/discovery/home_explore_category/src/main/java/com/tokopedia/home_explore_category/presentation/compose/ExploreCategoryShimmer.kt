package com.tokopedia.home_explore_category.presentation.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.components.loader.NestLoader
import com.tokopedia.nest.components.loader.NestLoaderType
import com.tokopedia.nest.components.loader.NestShimmerType

const val TWELVE_SHIMMER = 21

@Composable
fun ExploreCategoryShimmer(modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        modifier = modifier
    ) {
        items(TWELVE_SHIMMER) {
            ExploreCategoryItemShimmer()
        }
    }
}

@Composable
fun ExploreCategoryItemShimmer() {
    NestLoader(
        variant = NestLoaderType.Shimmer(type = NestShimmerType.Rect(2.dp)),
        modifier = Modifier
            .height(147.dp)
            .width(104.dp)
    )
}

@Preview
@Composable
fun ExploreCategoryShimmerPreview() {
    ExploreCategoryShimmer()
}
