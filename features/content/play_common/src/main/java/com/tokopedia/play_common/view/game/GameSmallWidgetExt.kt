package com.tokopedia.play_common.view.game

import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.play_common.R
import com.tokopedia.unifyprinciples.R as unifyR
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import java.util.*

/**
 * Created by kenny.hadisaputra on 05/04/22
 */
private fun GameSmallWidgetView.setupGiveaway(id: String = "") {
    getIconUnifyDrawable(
        context = context,
        iconId = IconUnify.GIFT,
        assetColor = MethodChecker.getColor(context, R.color.play_dms_giveaway_icon_color)
    )?.let(::setIcon)

    this.id = id

    setContentBackground(
        MethodChecker.getDrawable(context, R.drawable.bg_play_giveaway_widget)
    )
}

fun GameSmallWidgetView.setupUpcomingGiveaway(
    title: String,
    id: String = "",
    targetTime: Calendar,
    onDurationEnd: (GameSmallWidgetView) -> Unit,
    onTick: (Long) -> Unit = {},
) {
    setTimerVariant(TimerUnifySingle.VARIANT_GENERAL)
    this.title = title
    this.description = context.getString(R.string.play_common_widget_interactive_start)
    this.id = id
    setTargetTime(targetTime, onFinished = { onDurationEnd(this) }, onTicked = { onTick(it) })
    setupGiveaway(id)
}

fun GameSmallWidgetView.setupOngoingGiveaway(
    title: String,
    id: String = "",
    targetTime: Calendar,
    onDurationEnd: (GameSmallWidgetView) -> Unit,
    onTick: (Long) -> Unit = {},
) {
    setTimerVariant(TimerUnifySingle.VARIANT_MAIN)
    this.title = title
    this.description = context.getString(R.string.play_common_widget_interactive_end)
    this.id = id
    setTargetTime(targetTime, onFinished = { onDurationEnd(this) }, onTicked = { onTick(it) })
    setupGiveaway(id)
}

fun GameSmallWidgetView.setupQuiz(
    question: String,
    id: String = "",
    targetTime: Calendar,
    onDurationEnd: (GameSmallWidgetView) -> Unit,
    onTick: (Long) -> Unit = {},
) {
    getIconUnifyDrawable(
        context = context,
        iconId = IconUnify.QUIZ,
        assetColor = MethodChecker.getColor(context, R.color.play_dms_quiz_icon_color)
    )?.let(::setIcon)

    setContentBackground(
        MethodChecker.getDrawable(context, R.drawable.bg_play_quiz_widget)
    )
    description = context.getString(R.string.play_common_widget_interactive_end)
    setTargetTime(targetTime, onFinished = { onDurationEnd(this) }, onTicked = { onTick(it) })
    setTimerVariant(TimerUnifySingle.VARIANT_MAIN)
    title = question
    this.id = id
}

fun GameSmallWidgetView.setupPromo(
    title: String,
    description: String,
    id: String = "",
) {
    this.title = title
    this.description = description
    this.id = id

    getIconUnifyDrawable(
        context = context,
        iconId = IconUnify.DISCOUNT,
        assetColor = MethodChecker.getColor(context, unifyR.color.Unify_GN500)
    )?.let(::setIcon)
    setContentBackground(
        MethodChecker.getDrawable(context, R.drawable.bg_play_voucher_widget)
    )
}