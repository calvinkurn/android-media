package com.tokopedia.play.view.viewcomponent.interactive

import android.view.View
import android.view.ViewGroup
import com.tokopedia.play.R
import com.tokopedia.play_common.view.game.GameSmallWidgetView
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
) : ViewComponent(container, R.id.view_interactive_active) {

    private val parent = rootView as ViewGroup

    init {
        rootView.setOnClickListener {
            listener.onInteractiveWidgetClicked(this@InteractiveActiveViewComponent)
        }
        rootView.setOnClickListener {
            listener.onWidgetClicked(this)
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