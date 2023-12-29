package com.tokopedia.play.widget.ui.carousel

import android.content.Context
import com.tokopedia.content.common.ui.itemdecoration.FocusedCarouselItemDecoration
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by kenny.hadisaputra on 04/05/23
 */
class PlayWidgetCarouselItemDecoration(
    context: Context
) : FocusedCarouselItemDecoration(context) {

    override val horizontalOffset: Int = context.resources.getDimensionPixelOffset(unifyprinciplesR.dimen.spacing_lvl1)
}
