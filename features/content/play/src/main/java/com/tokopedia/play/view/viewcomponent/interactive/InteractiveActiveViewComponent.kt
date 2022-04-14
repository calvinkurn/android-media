package com.tokopedia.play.view.viewcomponent.interactive

import android.view.ViewGroup
import com.tokopedia.play.R
import com.tokopedia.play_common.view.game.GameSmallWidgetView
import com.tokopedia.play_common.view.game.setupQuiz
import com.tokopedia.play_common.viewcomponent.ViewComponent
import java.util.*

/**
 * Created by kenny.hadisaputra on 05/04/22
 */
class InteractiveActiveViewComponent(
    container: ViewGroup,
    private val listener: Listener
) : ViewComponent(container, R.id.widget_game_small) {

    private val widget = rootView as GameSmallWidgetView

    init {
        rootView.setOnClickListener {
            listener.onInteractiveWidgetClicked(this@InteractiveActiveViewComponent)
        }
    }

    fun setOngoingGiveaway() {
        //TODO("Stitch with real code")
//        widget.setupOngoingGiveaway(
//            desc = "Kapan ulang tahun Rockbros Indonesia?",
//            durationInMs = 7000,
//            onDurationEnd = {},
//        )
    }

    fun setUpcomingGiveaway() {
        //TODO("Stitch with real code")
//        widget.setupUpcomingGiveaway(
//            desc = "Kapan ulang tahun Rockbros Indonesia?",
//            durationInMs = 7000,
//            onDurationEnd = {},
//        )
    }

    fun setQuiz(question: String,
                timerInfo: String,
                durationInMs: Long,
                onDurationEnd: (GameSmallWidgetView) -> Unit) {
        widget.setupQuiz(
            question = question,
            onDurationEnd = onDurationEnd,
            targetTime = Calendar.getInstance().apply {
                add(Calendar.MILLISECOND, 5000)
            }
        )
    }

    interface Listener {
        fun onInteractiveWidgetClicked(view: InteractiveActiveViewComponent)
    }
}