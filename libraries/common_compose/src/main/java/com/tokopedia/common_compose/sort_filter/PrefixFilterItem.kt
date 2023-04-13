package com.tokopedia.common_compose.sort_filter

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.components.Color
import com.tokopedia.common_compose.components.NestNotification
import com.tokopedia.common_compose.ui.LocalNestColor
import com.tokopedia.common_compose.ui.NestTheme
import com.tokopedia.iconunify.R

@Composable
internal fun PrefixFilterItem(
    modifier: Modifier = Modifier,
    size: Size = Size.DEFAULT,
    selectedSize: Int = 0,
    painterId: Int = R.drawable.iconunify_sort_filter,
    text: String? = null,
    textWidth: Dp? = null,
    textWidthChange: (Int) -> Unit = {},
    onClick: () -> Unit = {}
) {
    // Implementation are specifically to cater SELECTED and NORMAL type chips only
    val backgroundColor = NestTheme.colors.NN._0
    val borderColor = NestTheme.colors.NN._200
    val height = when (size) {
        Size.DEFAULT -> 32.dp
        Size.LARGE -> 48.dp
    }
    Surface(color = backgroundColor,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, borderColor),
        modifier = modifier
            .height(height)
            .clickable { onClick() }) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (selectedSize > 0) {
                NestNotification(text = selectedSize.toString(), colorType = Color.SECONDARY)
            } else {
                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(id = painterId),
                    contentDescription = "Clear Filter Icon",
                    tint = LocalNestColor.current.NN._500
                )
            }
            if (text != null) {
                val padding = if (textWidth == 0.dp) 0.dp else 4.dp
                Text(
                    modifier = Modifier
                        .padding(start = padding)
                        .run { if (textWidth != null) width(textWidth) else this }
                        .onGloballyPositioned {
                            textWidthChange(it.size.width)
                        },
                    style = NestTheme.typography.display2,
                    text = text,
                    maxLines = 1
                )
            }
        }
    }
}

@Preview(name = "Prefix Sortfilter")
@Preview(name = "Prefix Sortfilter (Dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PrefixSortFilterPreview() {
    NestTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            PrefixFilterItem(text = "Filter")
            PrefixFilterItem(text = "Filter", selectedSize = 3)
            PrefixFilterItem(painterId = R.drawable.iconunify_close)
        }
    }
}
