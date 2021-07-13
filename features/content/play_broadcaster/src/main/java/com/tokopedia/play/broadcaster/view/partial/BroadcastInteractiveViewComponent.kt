package com.tokopedia.play.broadcaster.view.partial

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.view.custom.interactive.InteractiveFinishView
import com.tokopedia.play.broadcaster.view.custom.interactive.InteractiveInitView
import com.tokopedia.play.broadcaster.view.custom.interactive.InteractiveLiveView
import com.tokopedia.play.broadcaster.view.custom.interactive.InteractiveLoadingView
import com.tokopedia.play_common.viewcomponent.ViewComponent
import kotlinx.coroutines.*

/**
 * Created by jegul on 07/07/21
 */
class BroadcastInteractiveViewComponent(
        container: ViewGroup,
        listener: Listener
) : ViewComponent(container, R.id.view_play_interactive) {

    private val parent = rootView as ViewGroup

    private val coachMark: CoachMark2 = CoachMark2(container.context)

    private val coachMarkAnchorOffset = resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    private val initView = object : InteractiveInitView.Listener {
        override fun onCreateNewGameClicked(view: InteractiveInitView) {
            cancelCoachMark()
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

        override fun onSeeWinnerClicked(view: InteractiveFinishView) {
            hideCoachMark()
            listener.onSeeWinnerClicked(this@BroadcastInteractiveViewComponent)
        }
    }

    init {
        coachMark.contentView.translationY = coachMarkAnchorOffset * -1f
    }

    fun setInit(showOnBoarding: Boolean) = setChildView { InteractiveInitView(parent.context) }.apply {
        setListener(initView)
        if (showOnBoarding) showOnBoardingCoachMark()
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

    fun setFinish(coachMarkTitle: String, coachMarkSubtitle: String) = setChildView { InteractiveFinishView(parent.context) }.apply {
        setListener(finishListener)
        showCoachMark(
                view = getBadgeView(),
                title = coachMarkTitle,
                subtitle = coachMarkSubtitle
        )
    }

    private fun showOnBoardingCoachMark() {
        scope.launch {
            delay(ON_BOARDING_COACH_MARK_DELAY)
            showCoachMark(
                view = rootView,
                title = getString(R.string.play_interactive_broadcast_onboarding_title),
                subtitle = getString(R.string.play_interactive_broadcast_onboarding_subtitle)
            )
        }
    }

    private fun cancelCoachMark() {
        job.cancelChildren()
        hideCoachMark()
    }

    private fun hideCoachMark() {
        coachMark.dismissCoachMark()
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
            hideCoachMark()
            removeListener()
            parent.removeAllViews()
            val view = viewCreator()
            parent.addView(view)
            view
        } else firstChild
    }

    private fun showCoachMark(view: View, title: String, subtitle: String) {
        coachMark.isDismissed = false
        coachMark.showCoachMark(
                arrayListOf(
                        CoachMark2Item(
                                view,
                                title,
                                subtitle
                        )
                )
        )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        cancelCoachMark()
    }

    interface Listener {

        fun onNewGameClicked(view: BroadcastInteractiveViewComponent)
        fun onSeeWinnerClicked(view: BroadcastInteractiveViewComponent)
    }

    companion object {
        private const val ON_BOARDING_COACH_MARK_DELAY = 3000L
    }
}