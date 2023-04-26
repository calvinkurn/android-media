package com.tokopedia.common_compose.principles

import android.util.Log
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.header.HeaderSingleLineType
import com.tokopedia.common_compose.header.NestHeaderType
import com.tokopedia.common_compose.header.NestHeaderVariant
import com.tokopedia.common_compose.header.getHeaderBackgroundColor
import com.tokopedia.common_compose.header.getHeaderContentColor
import com.tokopedia.common_compose.header.getHeaderContentSecondaryColor
import com.tokopedia.common_compose.header.getHeaderIconColor

@Composable
fun NestHeader(
    modifier: Modifier = Modifier,
    variant: NestHeaderVariant = NestHeaderVariant.Default,
    type: NestHeaderType = NestHeaderType.SingleLine()
) {
    val headerBackground = getHeaderBackgroundColor(variant = variant)
    val contentColor = getHeaderContentColor(variant = variant)
    val contentSecondaryColor = getHeaderContentSecondaryColor(variant = variant)
    val iconColor = getHeaderIconColor(variant = variant)

    Surface(
        color = headerBackground,
        elevation = 1.dp,
        modifier = modifier,
        contentColor = contentColor
    ) {
        when (type) {
            is NestHeaderType.SingleLine -> HeaderSingleLineType(
                properties = type, iconColor = iconColor
            )
            else -> {

            }
        }
    }
}

@Preview
@Composable
fun NestHeaderPreview() {
    NestHeader(
        type = NestHeaderType.SingleLine().copy(
            onBackClicked = {
                Log.d("preview","click")
            }
        )
    )
}
