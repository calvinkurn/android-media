package com.tokopedia.play.view.viewcomponent.interactive

import android.view.View
import android.view.ViewGroup
import com.tokopedia.play.R
import com.tokopedia.play.view.custom.interactive.InteractiveFinishedView
import com.tokopedia.play.view.custom.interactive.InteractivePreStartView
import com.tokopedia.play.view.custom.interactive.InteractiveTapView
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by jegul on 05/07/21
 */
class InteractiveViewComponent(
        container: ViewGroup,
        listener: Listener,
) : ViewComponent(container, R.id.view_interactive) {

    private val parent = rootView as ViewGroup

    private val preStartListener = object : InteractivePreStartView.Listener {
        override fun onFollowButtonClicked(view: InteractivePreStartView) {
            listener.onFollowButtonClicked(this@InteractiveViewComponent)
        }
    }

    private val tapTapListener = object : InteractiveTapView.Listener {
        override fun onTapClicked(view: InteractiveTapView) {
            listener.onTapTapClicked(this@InteractiveViewComponent)
        }

        override fun onFollowClicked(view: InteractiveTapView) {
            listener.onFollowButtonClicked(this@InteractiveViewComponent)
        }
    }

    fun showPreStart(
            title: String,
            timeToStartInMs: Long,
            onFinished: () -> Unit
    ) = setChildView { InteractivePreStartView(parent.context) }.apply {
        setTitle(title)
        setTimer(timeToStartInMs, onFinished)

        setListener(preStartListener)

        show()
    }

    fun showTapTap(
            durationInMs: Long,
            onFinished: () -> Unit
    ) = setChildView { InteractiveTapView(parent.context) }.apply {
        setTimer(durationInMs, onFinished)

        setListener(tapTapListener)

        show()
    }

    fun showFinish(
            info: String
    ) = setChildView { InteractiveFinishedView(parent.context) }.apply {
        setInfo(info)

        show()
    }

    fun showFollowMode(shouldShow: Boolean) {
        val firstChild = parent.getChildAt(0) ?: return
        when (firstChild) {
            is InteractivePreStartView -> firstChild.showFollowButton(shouldShow)
            is InteractiveTapView -> firstChild.showFollowMode(shouldShow)
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

    private fun removeListener() {
        val firstChild = parent.getChildAt(0) ?: return
        when (firstChild) {
            is InteractivePreStartView -> firstChild.setListener(null)
            is InteractiveTapView -> firstChild.setListener(null)
        }
    }

    interface Listener {

        fun onFollowButtonClicked(view: InteractiveViewComponent)
        fun onTapTapClicked(view: InteractiveViewComponent)
    }
}