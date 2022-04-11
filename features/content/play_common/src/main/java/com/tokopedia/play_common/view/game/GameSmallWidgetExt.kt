package com.tokopedia.play_common.view.game

import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.play_common.R
import com.tokopedia.unifycomponents.timer.TimerUnifySingle

/**
 * Created by kenny.hadisaputra on 05/04/22
 */
private fun GameSmallWidgetView.setupGiveaway() {
    getIconUnifyDrawable(
        context = context,
        iconId = IconUnify.GIFT,
        assetColor = MethodChecker.getColor(context, R.color.play_dms_giveaway_icon_color)
    )?.let(::setIcon)

    setContentBackground(
        MethodChecker.getDrawable(context, R.drawable.bg_play_giveaway_widget)
    )
}

fun GameSmallWidgetView.setupUpcomingGiveaway(
    desc: String,
    durationInMs: Long,
    onDurationEnd: (GameSmallWidgetView) -> Unit,
) {
    setTimerVariant(TimerUnifySingle.VARIANT_GENERAL)
    description = desc
    setTimerInfo(context.getString(R.string.play_common_widget_interactive_start))
    setTimer(durationInMs) { onDurationEnd(this) }
    setupGiveaway()
}

fun GameSmallWidgetView.setupOngoingGiveaway(
    desc: String,
    durationInMs: Long,
    onDurationEnd: (GameSmallWidgetView) -> Unit,
) {
    setTimerVariant(TimerUnifySingle.VARIANT_MAIN)
    description = desc
    setTimerInfo(context.getString(R.string.play_common_widget_interactive_end))
    setTimer(durationInMs) { onDurationEnd(this) }
    setupGiveaway()
}

fun GameSmallWidgetView.setupQuiz(
    question: String,
    timerInfo: String,
    durationInMs: Long,
    onDurationEnd: (GameSmallWidgetView) -> Unit,
) {
    getIconUnifyDrawable(
        context = context,
        iconId = IconUnify.QUIZ,
        assetColor = MethodChecker.getColor(context, R.color.play_dms_quiz_icon_color)
    )?.let(::setIcon)

    setContentBackground(
        MethodChecker.getDrawable(context, R.drawable.bg_play_quiz_widget)
    )
    setTimerInfo(timerInfo)
    setTimer(durationInMs) { onDurationEnd(this) }
    setTimerVariant(TimerUnifySingle.VARIANT_MAIN)
    description = question
}