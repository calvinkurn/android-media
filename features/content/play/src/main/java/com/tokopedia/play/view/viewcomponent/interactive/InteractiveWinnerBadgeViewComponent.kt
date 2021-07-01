package com.tokopedia.play.view.viewcomponent.interactive

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.play.R
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by jegul on 29/06/21
 */
class InteractiveWinnerBadgeViewComponent(
        container: ViewGroup,
        listener: Listener
) : ViewComponent(container, R.id.view_interactive_winner_badge) {

    private val coachMark: CoachMark2 = CoachMark2(container.context)

    private val coachMarkAnchorOffset = resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)

    init {
        coachMark.contentView.translationY = coachMarkAnchorOffset * -1f
        rootView.setOnClickListener {
            listener.onBadgeClicked(this)
        }
    }

    fun showCoachMark(title: String, subtitle: String) {
        coachMark.isDismissed = false
        coachMark.showCoachMark(
                arrayListOf(
                        CoachMark2Item(
                                rootView,
                                title,
                                subtitle
                        )
                )
        )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        coachMark.dismissCoachMark()
    }

    interface Listener {

        fun onBadgeClicked(view: InteractiveWinnerBadgeViewComponent)
    }
}