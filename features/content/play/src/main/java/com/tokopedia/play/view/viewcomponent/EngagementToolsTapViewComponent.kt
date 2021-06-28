package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import android.widget.FrameLayout
import com.tokopedia.play.R
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by jegul on 28/06/21
 */
class EngagementToolsTapViewComponent(
        container: ViewGroup,
        listener: Listener
) : ViewComponent(container, R.id.view_engagement_tap) {

    private val flEngagementTap = findViewById<FrameLayout>(R.id.fl_engagement_tap)

    init {
        flEngagementTap.setOnClickListener {

        }
    }

    interface Listener {

        fun onTapClicked(view: EngagementToolsTapViewComponent)
    }
}