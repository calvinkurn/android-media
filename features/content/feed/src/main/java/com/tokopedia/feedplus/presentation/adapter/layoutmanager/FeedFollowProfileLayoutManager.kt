package com.tokopedia.feedplus.presentation.adapter.layoutmanager

import android.content.Context
import com.tokopedia.content.common.ui.layoutmanager.FocusedCarouselLayoutManager

/**
 * Created By : Jonathan Darwin on July 04, 2023
 */
class FeedFollowProfileLayoutManager(
    context: Context,
) : FocusedCarouselLayoutManager(context) {

    override val heightPercentage: Double
        get() = 2.0
}
