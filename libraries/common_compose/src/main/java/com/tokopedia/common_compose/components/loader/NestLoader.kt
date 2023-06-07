package com.tokopedia.common_compose.components.loader

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.ui.NestTheme

/**
 * Created by yovi.putra on 07/05/23"
 * Project name: compose_skeleton
 **/

@Composable
fun NestLoader(
    variant: NestLoaderType,
    modifier: Modifier = Modifier
) {
    when (variant) {
        is NestLoaderType.Circular -> {
            NestCircularLoader(
                modifier = modifier.getTypeSize(size = variant.size),
                isWhite = variant.isWhite
            )
        }

        is NestLoaderType.Decorative -> {
            NestDecorativeLoader(
                modifier = modifier.getTypeSize(size = variant.size),
                isWhite = variant.isWhite
            )
        }

        is NestLoaderType.Shimmer -> {
            NestShimmer(modifier = modifier, type = variant.type)
        }
    }
}

private fun Modifier.getTypeSize(size: NestLoaderSize) = this.then(
    if (size != NestLoaderSize.Undefine) {
        this.size(width = size.width, height = size.height)
    } else {
        this
    }
)

@Preview
@Composable
private fun NestLoaderPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NestTheme.colors.NN._1000)
            .verticalScroll(state = rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NestLoader(variant = NestLoaderType.Circular(size = NestLoaderSize.Large))

        NestLoader(variant = NestLoaderType.Circular(size = NestLoaderSize.Small, isWhite = true))

        NestLoader(variant = NestLoaderType.Decorative(size = NestLoaderSize.Large))

        NestLoader(variant = NestLoaderType.Decorative(size = NestLoaderSize.Small, isWhite = true))

        NestLoader(
            variant = NestLoaderType.Shimmer(type = NestShimmerType.Rect()),
            modifier = Modifier
                .size(100.dp, 50.dp)
        )

        NestLoader(
            variant = NestLoaderType.Shimmer(type = NestShimmerType.Rect(rounded = 8.dp)),
            modifier = Modifier
                .size(100.dp, 50.dp)
        )

        NestLoader(
            variant = NestLoaderType.Shimmer(type = NestShimmerType.Line),
            modifier = Modifier
                .size(100.dp, 50.dp)
        )

        NestLoader(
            variant = NestLoaderType.Shimmer(type = NestShimmerType.Line),
            modifier = Modifier
                .size(100.dp, 16.dp)
        )

        NestLoader(
            variant = NestLoaderType.Shimmer(type = NestShimmerType.Circle),
            modifier = Modifier
                .size(100.dp)
        )
    }
}
