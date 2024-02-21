package com.tokopedia.cart.view.compose

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.noRippleClickable

@Composable
fun CartBuyAgainFloatingButtonView(
    title: String?,
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val containerAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1.0f else 0f,
        label = "FloatingButtonContainerAlpha",
        animationSpec = tween(
            durationMillis = 300
        )
    )

    if (title.isNullOrBlank()) return

    Row(
        modifier = modifier
            .graphicsLayer(
                clip = true,
                shape = RoundedCornerShape(20.dp),
                alpha = containerAlpha
            )
            .background(NestTheme.colors.GN._500)
            .padding(
                top = 8.dp,
                start = 8.dp,
                end = 12.dp,
                bottom = 8.dp
            )
            .noRippleClickable {
                onClick.invoke()
            }
    ) {
        NestIcon(
            iconId = IconUnify.ARROW_DOWN,
            modifier = Modifier.size(16.dp),
            colorLightEnable = NestTheme.colors.NN._0,
            colorNightEnable = NestTheme.colors.NN._0
        )
        Spacer(modifier = Modifier.width(4.dp))
        NestTypography(
            text = title,
            textStyle = NestTheme.typography.display3.copy(
                color = NestTheme.colors.NN._0,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CartBuyAgainFloatingButtonViewPreview() {
    NestTheme {
        CartBuyAgainFloatingButtonView(
            title = "Waktunya beli lagi, nih!",
            isVisible = false,
            onClick = {}
        )
    }
}
