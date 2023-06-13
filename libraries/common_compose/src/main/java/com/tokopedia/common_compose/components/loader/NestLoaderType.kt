package com.tokopedia.common_compose.components.loader

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Created by yovi.putra on 07/05/23"
 * Project name: compose_skeleton
 **/

sealed interface NestLoaderType {
    data class Circular(
        val size: NestLoaderSize = NestLoaderSize.Undefine,
        val isWhite: Boolean = false
    ) : NestLoaderType

    data class Decorative(
        val size: NestLoaderSize = NestLoaderSize.Undefine,
        val isWhite: Boolean = false
    ) : NestLoaderType

    data class Shimmer(
        val type: NestShimmerType
    ) : NestLoaderType
}

sealed class NestLoaderSize(val width: Dp, val height: Dp) {
    object Small : NestLoaderSize(32.dp, 32.dp)
    object Large : NestLoaderSize(50.dp, 50.dp)
    object Undefine : NestLoaderSize(0.dp, 0.dp)
}

sealed interface NestShimmerType {
    data class Rect(
        val rounded: Dp = 0.dp
    ) : NestShimmerType
    object Line : NestShimmerType
    object Circle : NestShimmerType
}
