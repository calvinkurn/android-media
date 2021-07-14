package com.tokopedia.play.view.viewcomponent.interactive

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.R
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by jegul on 29/06/21
 */
class InteractiveWinnerBadgeViewComponent(
        container: ViewGroup,
        listener: Listener
) : ViewComponent(container, R.id.view_interactive_winner_badge) {

    private val vAnchorTop = findViewById<View>(R.id.v_anchor_top)
    private val coachMark: CoachMark2 = CoachMark2(container.context)

    init {
        coachMark.onDismissListener = { vAnchorTop.gone() }
        rootView.setOnClickListener {
            listener.onBadgeClicked(this)
        }
    }

    fun showCoachMark(title: String, subtitle: String) {
        coachMark.isDismissed = false
        vAnchorTop.visible()

        coachMark.showCoachMark(
                arrayListOf(
                        CoachMark2Item(
                                vAnchorTop,
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

        fun onBadgeClicked(view: InteractiveWinnerBadgeViewComponent)
    }
}