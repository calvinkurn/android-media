package com.tokopedia.common_compose.principles

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tokopedia.common_compose.ui.NestTheme

@Composable
fun NestLabel(
    modifier: Modifier = Modifier,
    labelText: CharSequence,
    nestLabelType: NestLabelType
) {
    val backgroundColor = when (nestLabelType) {
        NestLabelType.HIGHLIGHT_LIGHT_GREEN -> NestTheme.colors.GN100
        NestLabelType.HIGHLIGHT_LIGHT_ORANGE -> NestTheme.colors.YN100
        NestLabelType.HIGHLIGHT_LIGHT_GREY -> NestTheme.colors.NN100
        NestLabelType.HIGHLIGHT_LIGHT_RED -> NestTheme.colors.RN100
    }

    val textColor = when (nestLabelType) {
        NestLabelType.HIGHLIGHT_LIGHT_GREEN -> NestTheme.colors.GN500
        NestLabelType.HIGHLIGHT_LIGHT_ORANGE -> NestTheme.colors.YN500
        NestLabelType.HIGHLIGHT_LIGHT_GREY -> NestTheme.colors.NN600
        NestLabelType.HIGHLIGHT_LIGHT_RED -> NestTheme.colors.RN500
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(4.dp),
        color = backgroundColor
    ) {
        Text(
            text = labelText.toString(),
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 3.dp),
            color = textColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }

}

@Preview(name = "Label (Green)")
@Composable
fun NestLabelPreview() {
    NestLabel(
        Modifier,
        labelText = "Berlangsung",
        nestLabelType = NestLabelType.HIGHLIGHT_LIGHT_GREEN
    )
}


@Preview(name = "Label (Red)")
@Composable
fun NestLabelRedPreview() {
    NestLabel(
        Modifier,
        labelText = "Dibatalkan",
        nestLabelType = NestLabelType.HIGHLIGHT_LIGHT_RED
    )
}
