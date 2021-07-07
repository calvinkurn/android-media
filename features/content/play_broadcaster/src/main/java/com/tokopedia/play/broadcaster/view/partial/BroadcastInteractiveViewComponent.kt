package com.tokopedia.play.broadcaster.view.partial

import android.view.View
import android.view.ViewGroup
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.view.custom.interactive.InteractiveFinishView
import com.tokopedia.play.broadcaster.view.custom.interactive.InteractiveInitView
import com.tokopedia.play.broadcaster.view.custom.interactive.InteractiveLiveView
import com.tokopedia.play.broadcaster.view.custom.interactive.InteractiveLoadingView
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by jegul on 07/07/21
 */
class BroadcastInteractiveViewComponent(
        container: ViewGroup,
        listener: Listener
) : ViewComponent(container, R.id.view_play_interactive) {

    private val parent = rootView as ViewGroup

    private val initView = object : InteractiveInitView.Listener {
        override fun onCreateNewGameClicked(view: InteractiveInitView) {
            listener.onNewGameClicked(this@BroadcastInteractiveViewComponent)
        }
    }

    private val loadingListener = object : InteractiveLoadingView.Listener {
        override fun onCreateNewGameClicked(view: InteractiveLoadingView) {
            listener.onNewGameClicked(this@BroadcastInteractiveViewComponent)
        }
    }

    private val finishListener = object : InteractiveFinishView.Listener {
        override fun onCreateNewGameClicked(view: InteractiveFinishView) {
            listener.onNewGameClicked(this@BroadcastInteractiveViewComponent)
        }
    }

    fun setInit() = setChildView { InteractiveInitView(parent.context) }.apply {
        setListener(initView)
    }

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

    fun setLoading() = setChildView { InteractiveLoadingView(parent.context) }.apply {
        setListener(loadingListener)
    }

    fun setFinish() = setChildView { InteractiveFinishView(parent.context) }.apply {
        setListener(finishListener)
    }

    private fun removeListener() {
        val firstChild = parent.getChildAt(0) ?: return
        when (firstChild) {
            is InteractiveInitView -> firstChild.setListener(null)
            is InteractiveLoadingView -> firstChild.setListener(null)
            is InteractiveFinishView -> firstChild.setListener(null)
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

    interface Listener {

        fun onNewGameClicked(view: BroadcastInteractiveViewComponent)
    }
}