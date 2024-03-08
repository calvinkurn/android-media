package com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

@Composable
internal fun CampaignStockPercentage(
    modifier: Modifier = Modifier,
    value: Int = 0
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            ProgressBarUnify(context).apply {
                val color = ContextCompat.getColor(
                    context,
                    unifyprinciplesR.color.Unify_Static_White
                )
                progressBarColor = intArrayOf(color, color)
                progressBarHeight = ProgressBarUnify.SIZE_MEDIUM
                val trackColor = ContextCompat.getColor(
                    context,
                    unifyprinciplesR.color.Unify_Overlay_Lvl2
                )
                trackDrawable.setColor(trackColor)
            }
        },
        update = { it.setValue(value, true) }
    )
}
