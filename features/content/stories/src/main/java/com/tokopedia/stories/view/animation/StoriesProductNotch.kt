package com.tokopedia.stories.view.animation

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * @author by astidhiyaa on 18/08/23
 */
@Composable
fun StoriesProductNotch(productCount: Int, onClick: () -> Unit) {
    NestTheme {
        val ctx = LocalContext.current

        val infiniteTransition = rememberInfiniteTransition()

        val y by infiniteTransition.animateFloat(
            initialValue = 30f,
            targetValue = 10f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 300),
                repeatMode = RepeatMode.Reverse
            )
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .wrapContentSize()
                .offset(y = y.dp)
                .background(color = Color(0x80000000), shape = RoundedCornerShape(20.dp))
                .padding(4.dp)
                .clickable { onClick() }
        ) {
            val iconDrawable = getIconUnifyDrawable(
                ctx,
                IconUnify.ARROW_UP,
                MethodChecker.getColor(
                    ctx,
                    unifyR.color.Unify_Static_White
                )
            )
            if (iconDrawable != null)
                Image(
                    painter = rememberImagePainter(iconDrawable), contentDescription = "",
                    modifier = Modifier
                        .size(24.dp)
                )
            NestTypography(
                text = "Lihat $productCount Produk", textStyle = NestTheme.typography.display2.copy(
                    color = colorResource(
                        id = unifyR.color.Unify_Static_White
                    )
                )
            )
        }
    }
}
