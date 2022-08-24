package com.tokopedia.play_common.view.game

import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.play_common.R
import com.tokopedia.play_common.databinding.ViewGameInteractiveBinding
import com.tokopedia.unifyprinciples.R as unifyR
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import java.util.*

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
    targetTime: Calendar,
    onDurationEnd: (GameSmallWidgetView) -> Unit,
) {
    setTimerVariant(TimerUnifySingle.VARIANT_GENERAL)
    description = desc
    setTimerInfo(context.getString(R.string.play_common_widget_interactive_start))
    setTargetTime(targetTime) { onDurationEnd(this) }
    setupGiveaway()
}

fun GameSmallWidgetView.setupOngoingGiveaway(
    desc: String,
    targetTime: Calendar,
    onDurationEnd: (GameSmallWidgetView) -> Unit,
) {
    setTimerVariant(TimerUnifySingle.VARIANT_MAIN)
    description = desc
    setTimerInfo(context.getString(R.string.play_common_widget_interactive_end))
    setTargetTime(targetTime) { onDurationEnd(this) }
    setupGiveaway()
}

fun GameSmallWidgetView.setupQuiz(
    question: String,
    targetTime: Calendar,
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
    setTimerInfo(context.getString(R.string.play_common_widget_interactive_end))
    setTargetTime(targetTime) { onDurationEnd(this) }
    setTimerVariant(TimerUnifySingle.VARIANT_MAIN)
    description = question
}

fun GameSmallWidgetView.setupPromo(
    title: String,
){
    description = title
    getIconUnifyDrawable(
        context = context,
        iconId = IconUnify.PROMO,
        assetColor = MethodChecker.getColor(context, unifyR.color.Unify_GN500)
    )?.let(::setIcon)
    setContentBackground(
        MethodChecker.getDrawable(context, R.drawable.bg_play_voucher_widget)
    )
}