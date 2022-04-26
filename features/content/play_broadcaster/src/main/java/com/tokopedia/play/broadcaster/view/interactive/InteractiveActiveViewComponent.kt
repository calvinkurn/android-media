package com.tokopedia.play.broadcaster.view.interactive

import android.view.View
import android.view.ViewGroup
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play_common.view.game.GameSmallWidgetView
import com.tokopedia.play_common.view.game.setupOngoingGiveaway
import com.tokopedia.play_common.view.game.setupQuiz
import com.tokopedia.play_common.view.game.setupUpcomingGiveaway
import com.tokopedia.play_common.viewcomponent.ViewComponent
import java.util.*

/**
 * Created by kenny.hadisaputra on 05/04/22
 */
class InteractiveActiveViewComponent(
    container: ViewGroup,
    listener: Listener,
) : ViewComponent(container, R.id.view_game_ongoing) {

    private val parent = rootView as ViewGroup


    init {
        rootView.setOnClickListener {
            listener.onWidgetClicked(this@InteractiveActiveViewComponent)
        }
    }

    fun setOngoingGiveaway(
        desc: String,
        targetTime: Calendar,
        onDurationEnd: () -> Unit,
    ) {
        setChildView { GameSmallWidgetView(parent.context) }.apply {
            setupOngoingGiveaway(
                desc = desc,
                targetTime = targetTime,
                onDurationEnd = { onDurationEnd() }
            )
        }
    }

    fun setUpcomingGiveaway(
        desc: String,
        targetTime: Calendar,
        onDurationEnd: () -> Unit,
    ) {
        setChildView { GameSmallWidgetView(parent.context) }.apply {
            setupUpcomingGiveaway(
                desc = desc,
                targetTime = targetTime,
                onDurationEnd = { onDurationEnd() }
            )
        }
    }


    fun setQuiz(
        question: String,
        targetTime: Calendar,
        onDurationEnd: () -> Unit,
    ) {
        setChildView { GameSmallWidgetView(parent.context) }.apply {
            setupQuiz(
                question = question,
                targetTime = targetTime,
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

    interface Listener {

        fun onWidgetClicked(view: InteractiveActiveViewComponent)
    }
}