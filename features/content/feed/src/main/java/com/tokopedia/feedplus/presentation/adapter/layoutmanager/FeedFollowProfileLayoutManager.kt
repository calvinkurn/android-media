package com.tokopedia.feedplus.presentation.adapter.layoutmanager

import android.content.Context
import com.tokopedia.play.widget.ui.carousel.PlayWidgetCarouselLayoutManager

/**
 * Created By : Jonathan Darwin on July 04, 2023
 */
class FeedFollowProfileLayoutManager(
    context: Context,
) : PlayWidgetCarouselLayoutManager(context) {

    override val heightPercentage: Double
        get() = 1 / 0.5
}
