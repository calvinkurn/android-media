package com.tokopedia.play.widget.ui.carousel

import android.content.Context
import com.tokopedia.content.common.ui.itemdecoration.FocusedCarouselItemDecoration
import com.tokopedia.play.widget.R as playwidgetR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by kenny.hadisaputra on 04/05/23
 */
class PlayWidgetCarouselItemDecoration(
    context: Context
) : FocusedCarouselItemDecoration(context) {

    override val roundedOffset: Int = context.resources.getDimensionPixelOffset(playwidgetR.dimen.play_widget_carousel_corner_radius)
    override val horizontalOffset: Int = context.resources.getDimensionPixelOffset(unifyprinciplesR.dimen.spacing_lvl1)
}
