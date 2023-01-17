package com.tokopedia.play.view.viewcomponent.interactive

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.play.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @author by astidhiyaa on 18/04/22
 */
class InteractiveGameResultViewComponent (container: ViewGroup,
                                          listener: Listener,
                                          private val scope: CoroutineScope,
) : ViewComponent(container, R.id.view_game_interactive_result) {

    private val vAnchorBottom = findViewById<View>(com.tokopedia.play_common.R.id.v_anchor_bottom)
    private val coachMark: CoachMark2 = CoachMark2(container.context)

    init {
        rootView.setOnClickListener {
            listener.onGameResultClicked(this)
        }

        coachMark.onDismissListener = { vAnchorBottom.gone() }
    }

    fun showCoachMark(title: String, subtitle: String) {
        coachMark.isDismissed = false
        vAnchorBottom.visible()
        scope.launch {
            coachMark.showCoachMark(
                arrayListOf(
                    CoachMark2Item(
                        vAnchorBottom,
                        title,
                        subtitle
                    )
                )
            )
            delay(GIVEAWAY_LOSER_COACHMARK_DELAY)
            hideCoachMark()
        }
    }

    fun hideCoachMark() {
        coachMark.dismissCoachMark()
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        coachMark.dismissCoachMark()
    }


    interface Listener {
        fun onGameResultClicked(view: InteractiveGameResultViewComponent)
    }

    companion object {
        private const val GIVEAWAY_LOSER_COACHMARK_DELAY = 5000L
    }
}
