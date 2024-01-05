package com.tokopedia.play.widget.ui.carousel

import android.content.Context
import com.tokopedia.content.common.ui.layoutmanager.FocusedCarouselLayoutManager
import com.tokopedia.kotlin.extensions.view.dpToPx

/**
 * Created by kenny.hadisaputra on 04/05/23
 */
class PlayWidgetCarouselLayoutManager(
    context: Context
) : FocusedCarouselLayoutManager(context) {

    override val maxWidth: Int = 168.dpToPx(context.resources.displayMetrics)
    override val itemWidthToScreenWidthPercentage: Double = 0.52
    override val heightRatio: Double = 1 / 0.566
}
