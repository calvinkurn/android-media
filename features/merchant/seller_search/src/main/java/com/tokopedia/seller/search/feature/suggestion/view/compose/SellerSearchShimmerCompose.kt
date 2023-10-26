package com.tokopedia.seller.search.feature.suggestion.view.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.tokopedia.nest.components.loader.NestLoader
import com.tokopedia.nest.components.loader.NestLoaderType
import com.tokopedia.nest.components.loader.NestShimmerType

const val TWELVE_SHIMMER = 12

@Composable
fun SellerSearchShimmerCompose() {
    LazyColumn(Modifier.fillMaxSize()) {
        items(TWELVE_SHIMMER) {
            SellerSearchItemShimmer()
        }
    }
}

@Composable
fun SellerSearchItemShimmer() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp, start = 16.dp, end = 16.dp)
    ) {
        val (loadImageView, loadTvTitle, loadTvDesc) = createRefs()

        NestLoader(
            variant = NestLoaderType.Shimmer(type = NestShimmerType.Rect(2.dp)),
            modifier = Modifier.constrainAs(loadImageView) {
                top.linkTo(parent.top, margin = 20.dp)
                start.linkTo(parent.start)
            }.size(48.dp)
        )

        NestLoader(
            variant = NestLoaderType.Shimmer(type = NestShimmerType.Rect(2.dp)),
            modifier = Modifier.constrainAs(loadTvTitle) {
                top.linkTo(parent.top, margin = 20.dp)
                start.linkTo(loadImageView.end, margin = 8.dp)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }.height(height = 16.dp)
        )

        NestLoader(
            variant = NestLoaderType.Shimmer(type = NestShimmerType.Rect(2.dp)),
            modifier = Modifier.constrainAs(loadTvDesc) {
                top.linkTo(loadTvTitle.bottom, margin = 8.dp)
                start.linkTo(loadImageView.end, margin = 8.dp)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }.height(height = 12.dp)
        )
    }
}

@Preview
@Composable
fun SellerSearchItemShimmerPreview() {
    SellerSearchShimmerCompose()
}
