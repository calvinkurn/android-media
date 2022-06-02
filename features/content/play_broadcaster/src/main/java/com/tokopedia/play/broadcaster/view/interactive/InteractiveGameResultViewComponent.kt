package com.tokopedia.play.broadcaster.view.interactive

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play_common.viewcomponent.ViewComponent
import kotlinx.coroutines.*

/**
 * @author by astidhiyaa on 18/04/22
 */
class InteractiveGameResultViewComponent (container: ViewGroup,
                                          listener: Listener,
) : ViewComponent(container, R.id.view_game_result) {

    private val job = SupervisorJob()
    private val vAnchorBottom = findViewById<View>(com.tokopedia.play_common.R.id.v_anchor_bottom)
    private val coachMark: CoachMark2 = CoachMark2(container.context)
    private val scope = CoroutineScope(Dispatchers.Main + job)

    init {
        rootView.setOnClickListener {
            listener.onGameResultClicked(this)
        }

        coachMark.onDismissListener = { vAnchorBottom.gone() }
    }

    fun showCoachMark() {
        scope.launch {
            showCoachMark(
                title = "",
                subtitle = "Lihat pemenang, hasil, dan\npeserta game di sini ya"
            )
        }
    }

    fun showCoachMark(title: String, subtitle: String) {
        coachMark.isDismissed = false
        vAnchorBottom.visible()

        coachMark.showCoachMark(
            arrayListOf(
                CoachMark2Item(
                    vAnchorBottom,
                    title,
                    subtitle
                )
            )
        )
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
}