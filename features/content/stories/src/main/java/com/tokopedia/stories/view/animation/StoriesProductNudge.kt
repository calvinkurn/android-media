package com.tokopedia.stories.view.animation

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.stories.R
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * @author by astidhiyaa on 18/08/23
 */
@Composable
fun StoriesProductNudge(productCount: String, isShown: Boolean, onClick: () -> Unit) {
    if (isShown.not()) return
    NestTheme(darkTheme = true) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            StoriesProductBadge(productCount = productCount, onClick)
        }
    }
}

@Composable
private fun StoriesProductBadge(productCount: String, onClick: () -> Unit) {
    val ctx = LocalContext.current

    val infiniteTransition = rememberInfiniteTransition()
    val anim by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(tween(800), RepeatMode.Reverse),
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .wrapContentSize()
            .graphicsLayer {
                translationY = anim
                transformOrigin = TransformOrigin.Center
            }
            .background(
                color = colorResource(id = unifyprinciplesR.color.Unify_Static_Black),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(start = 8.dp, end = 12.dp, top = 4.dp, bottom = 4.dp)
            .clickable { onClick() }
    ) {
        val iconDrawable = getIconUnifyDrawable(
            ctx,
            IconUnify.CHEVRON_UP,
            MethodChecker.getColor(
                ctx,
                unifyprinciplesR.color.Unify_Static_White
            )
        )
        if (iconDrawable != null)
            Image(
                painter = rememberImagePainter(iconDrawable), contentDescription = "",
                modifier = Modifier
                    .size(24.dp)
            )
        NestTypography(
            text = ctx.getString(R.string.stories_product_notch, productCount),
            textStyle = NestTheme.typography.display2.copy(
                color = colorResource(
                    id = unifyprinciplesR.color.Unify_Static_White
                )
            )
        )
    }
}
