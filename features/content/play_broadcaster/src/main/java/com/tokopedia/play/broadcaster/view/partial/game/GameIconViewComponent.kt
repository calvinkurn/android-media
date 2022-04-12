package com.tokopedia.play.broadcaster.view.partial.game

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.view.partial.BroadcastInteractiveViewComponent
import com.tokopedia.play_common.viewcomponent.ViewComponent
import kotlinx.coroutines.*

/**
 * Created By : Jonathan Darwin on March 29, 2022
 */
class GameIconViewComponent(
    container: ViewGroup,
    private val listener: Listener
) : ViewComponent(container, R.id.ic_game){

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    private val coachMark: CoachMark2 = CoachMark2(container.context)

    init {
        rootView.setOnClickListener {
            listener.onIconClick()
        }
    }

    fun showWithCondition(isShow: Boolean) {
        if(isShow) show()
        else hide()
    }

    fun showCoachmark() {
        scope.launch {
            delay(ON_BOARDING_COACH_MARK_DELAY)
            showCoachMark(
                view = rootView,
                subtitle = getString(R.string.play_interactive_broadcast_onboarding_subtitle)
            )
        }
    }

    private fun showCoachMark(view: View, subtitle: String) {
        coachMark.isDismissed = false
        coachMark.showCoachMark(
            arrayListOf(
                CoachMark2Item(
                    view,
                    "",
                    subtitle
                )
            )
        )
    }

    fun cancelCoachMark() {
        job.cancelChildren()
        coachMark.dismissCoachMark()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        cancelCoachMark()
        scope.cancel()
    }

    interface Listener {
        fun onIconClick()
    }

    companion object {
        private const val ON_BOARDING_COACH_MARK_DELAY = 3000L
    }
}