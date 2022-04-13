package com.tokopedia.play.view.viewcomponent.interactive

import android.view.View
import android.view.ViewGroup
import com.tokopedia.play.R
import com.tokopedia.play.view.custom.interactive.InteractivePreStartView
import com.tokopedia.play.view.custom.interactive.InteractiveTapView
import com.tokopedia.play_common.view.game.GameSmallWidgetView
import com.tokopedia.play_common.view.game.giveaway.GiveawayWidgetView
import com.tokopedia.play_common.view.game.setupOngoingGiveaway
import com.tokopedia.play_common.view.game.setupQuiz
import com.tokopedia.play_common.view.game.setupUpcomingGiveaway
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by kenny.hadisaputra on 05/04/22
 */
class InteractiveActiveViewComponent(
    container: ViewGroup,
) : ViewComponent(container, R.id.view_interactive_active) {

    private val parent = rootView as ViewGroup

    fun setOngoingGiveaway(
        desc: String,
        durationInMs: Long,
        onDurationEnd: () -> Unit,
    ) {
        setChildView { GameSmallWidgetView(parent.context) }.apply {
            setupOngoingGiveaway(
                desc = desc,
                durationInMs = durationInMs,
                onDurationEnd = { onDurationEnd() }
            )
        }
    }

    fun setUpcomingGiveaway(
        desc: String,
        durationInMs: Long,
        onDurationEnd: () -> Unit,
    ) {
        setChildView { GameSmallWidgetView(parent.context) }.apply {
            setupUpcomingGiveaway(
                desc = desc,
                durationInMs = durationInMs,
                onDurationEnd = { onDurationEnd() }
            )
        }
    }

    fun setQuiz(
        question: String,
        durationInMs: Long,
        onDurationEnd: () -> Unit,
    ) {
        setChildView { GameSmallWidgetView(parent.context) }.apply {
            setupQuiz(
                question = question,
                durationInMs = durationInMs,
                onDurationEnd = { onDurationEnd() }
            )
        }
    }

    private inline fun <reified V: View> setChildView(viewCreator: () -> V): V {
        val firstChild = parent.getChildAt(0)
        return if (firstChild !is V) {
            parent.removeAllViews()
            val view = viewCreator()
            parent.addView(view)
            view
        } else firstChild
    }
}