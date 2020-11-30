package com.tokopedia.play.broadcaster.view.partial

import android.view.ViewGroup
import androidx.annotation.IdRes
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by jegul on 30/11/20
 */
class BroadcastScheduleViewComponent(
        container: ViewGroup,
        @IdRes idRes: Int
) : ViewComponent(container, idRes) {

    init {

    }

    interface Listener {

        fun onAddEditBroadcastScheduleClicked(view: BroadcastScheduleViewComponent)
    }
}