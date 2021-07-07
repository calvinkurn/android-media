package com.tokopedia.play.broadcaster.view.partial

import android.view.View
import android.view.ViewGroup
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.view.custom.interactive.InteractiveFinishView
import com.tokopedia.play.broadcaster.view.custom.interactive.InteractiveLiveView
import com.tokopedia.play.broadcaster.view.custom.interactive.InteractiveLoadingView
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by jegul on 07/07/21
 */
class BroadcastInteractiveViewComponent(
        container: ViewGroup,
) : ViewComponent(container, R.id.view_play_interactive) {

    private val parent = rootView as ViewGroup

    fun setSchedule(
            title: String,
            timeToStartInMs: Long,
            onFinished: () -> Unit
    ) = setChildView { InteractiveLiveView(parent.context) }.apply {
        setMode(
                InteractiveLiveView.Mode.Scheduled(
                        title,
                        timeToStartInMs,
                        onFinished
                )
        )
    }

    fun setLive(
            remainingTimeInMs: Long,
            onFinished: () -> Unit
    ) = setChildView { InteractiveLiveView(parent.context) }.apply {
        setMode(
                InteractiveLiveView.Mode.Live(
                        remainingTimeInMs,
                        onFinished
                )
        )
    }

    fun setLoading() = setChildView { InteractiveLoadingView(parent.context) }

    fun setFinish() = setChildView { InteractiveFinishView(parent.context) }

    private fun removeListener() {
        val firstChild = parent.getChildAt(0) ?: return
        when (firstChild) {
            else -> {}
        }
    }

    private inline fun <reified V: View> setChildView(viewCreator: () -> V): V {
        val firstChild = parent.getChildAt(0)
        return if (firstChild !is V) {
            removeListener()
            parent.removeAllViews()
            val view = viewCreator()
            parent.addView(view)
            view
        } else firstChild
    }
}