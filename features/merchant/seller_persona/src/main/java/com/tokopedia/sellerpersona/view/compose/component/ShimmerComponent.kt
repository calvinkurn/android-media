package com.tokopedia.sellerpersona.view.compose.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.components.loader.NestLoader
import com.tokopedia.nest.components.loader.NestLoaderType
import com.tokopedia.nest.components.loader.NestShimmerType

/**
 * Created by @ilhamsuaib on 26/07/23.
 */

@Composable
internal fun RectShimmer(rounded: Dp = 8.dp, modifier: Modifier) {
    NestLoader(
        variant = NestLoaderType.Shimmer(NestShimmerType.Rect(rounded = rounded)),
        modifier = modifier
    )
}