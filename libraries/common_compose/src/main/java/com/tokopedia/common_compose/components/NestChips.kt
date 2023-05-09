package com.tokopedia.common_compose.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.R
import com.tokopedia.common_compose.principles.NestTypography
import com.tokopedia.common_compose.ui.NestTheme

@Composable
fun NestChips(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean,
    size: Size = Size.SMALL,
    showChevron: Boolean = false,
    onClick: () -> Unit = {}
) {
    val textColorSelected = NestTheme.colors.GN._500
    val textColorDefault = NestTheme.colors.NN._600

    val borderColorSelected = NestTheme.colors.GN._400
    val borderColorDefault = NestTheme.colors.NN._300

    val backgroundColorSelected = NestTheme.colors.GN._50
    val backgroundColorDefault = NestTheme.colors.NN._0

    val textColor = if (isSelected) textColorSelected else textColorDefault
    val borderColor = if (isSelected) borderColorSelected else borderColorDefault
    val backgroundColor = if (isSelected) backgroundColorSelected else backgroundColorDefault
    val chevronColor = if (isSelected) NestTheme.colors.GN._500 else NestTheme.colors.NN._900
    val paddingHorizontal = if (size == Size.LARGE) 12.dp else 8.dp // px tbd
    val maxLines = if (size == Size.LARGE) 2 else 1

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(size.cornerRad),
        border = BorderStroke(1.dp, borderColor),
        modifier = modifier
            .height(size.height)
    ) {
        Row(
            modifier = Modifier
                .clickable { onClick() }
                .padding(horizontal = paddingHorizontal),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NestTypography(
                text = text,
                textStyle = NestTheme.typography.display2.copy(color = textColor),
                maxLines = maxLines
            )
            if (showChevron) {
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_chevron_down),
                    contentDescription = "Dropdown Icon",
                    tint = chevronColor
                )
            }
        }
    }
}

enum class Size(val height: Dp, val cornerRad: Dp) {
    SMALL(32.dp, 10.dp),
    MEDIUM(40.dp, 12.dp),
    LARGE(48.dp, 8.dp)
}

@Preview("chip preview")
@Preview("chip preview (dark)", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewChip() {
    NestTheme {
        Surface {
            var active by remember { mutableStateOf(false) }
            var size by remember { mutableStateOf(Size.SMALL) }

            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = size == Size.SMALL, onCheckedChange = { size = Size.SMALL })
                    Text("S", fontWeight = FontWeight.Bold)
                    Checkbox(
                        checked = size == Size.MEDIUM,
                        onCheckedChange = { size = Size.MEDIUM }
                    )
                    Text("M", fontWeight = FontWeight.Bold)
                    Checkbox(checked = size == Size.LARGE, onCheckedChange = { size = Size.LARGE })
                    Text("L", fontWeight = FontWeight.Bold)
                }
                NestChips(text = "Normal", isSelected = active, size = size) { active = !active }
                NestChips(text = "Chevron", isSelected = false, size = size, showChevron = true)
            }
        }
    }
}
