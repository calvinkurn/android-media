package com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.NoMinimumTouchArea

/**
 * Created by yovi.putra on 20/12/23"
 * Project name: android-unify
 **/

@Composable
fun RemindMeButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val buttonColors = ButtonDefaults.buttonColors(
        backgroundColor = Color.Transparent,
        contentColor = Color.White
    )
    val border = BorderStroke(
        width = 1.dp,
        color = Color.White
    )
    NoMinimumTouchArea {
        Button(
            modifier = modifier,
            onClick = onClick,
            shape = RoundedCornerShape(8.dp),
            border = border,
            colors = buttonColors,
            elevation = null,
            contentPadding = PaddingValues(horizontal = 8.dp),
            interactionSource = interactionSource
        ) {
            NestTypography(
                text = text,
                textStyle = NestTheme.typography.heading6.copy(
                    color = Color.White
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview
@Composable
fun RemindMeButtonPreview() {
    NestTheme {
        RemindMeButton(
            "Ingatkan Saya",
            onClick = {}
        )
    }
}
