package com.tokopedia.play.view.viewcomponent.interactive

import android.view.ViewGroup
import com.tokopedia.play.R
import com.tokopedia.play_common.view.game.GameSmallWidgetView
import com.tokopedia.play_common.view.game.setupOngoingGiveaway
import com.tokopedia.play_common.view.game.setupQuiz
import com.tokopedia.play_common.view.game.setupUpcomingGiveaway
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by kenny.hadisaputra on 05/04/22
 */
class InteractiveActiveViewComponent(
    container: ViewGroup,
    listener: Listener
) : ViewComponent(container, R.id.widget_game_small) {

    private val widget = rootView as GameSmallWidgetView

    init {
        widget.setListener(object : GameSmallWidgetView.Listener{
            override fun onWidgetClicked(view: GameSmallWidgetView) {
                listener.onInteractiveWidgetClicked(this@InteractiveActiveViewComponent)
            }
        })
    }

    fun setOngoingGiveaway() {
        //TODO("Stitch with real code")
        widget.setupOngoingGiveaway(
            desc = "Kapan ulang tahun Rockbros Indonesia?",
            durationInMs = 7000,
            onDurationEnd = {},
        )
    }

    fun setUpcomingGiveaway() {
        //TODO("Stitch with real code")
        widget.setupUpcomingGiveaway(
            desc = "Kapan ulang tahun Rockbros Indonesia?",
            durationInMs = 7000,
            onDurationEnd = {},
        )
    }

    fun setQuiz() {
        //TODO("Stitch with real code")
        widget.setupQuiz(
            question = "Kapan ulang tahun Rockbros Indonesia?",
            timerInfo = "Berakhir",
            durationInMs = 7000,
            onDurationEnd = {},
        )
    }

    interface Listener{
        fun onInteractiveWidgetClicked(viewComponent: InteractiveActiveViewComponent)
    }
}