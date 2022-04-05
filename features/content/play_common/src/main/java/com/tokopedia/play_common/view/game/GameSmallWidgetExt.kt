package com.tokopedia.play_common.view.game

import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.play_common.R

/**
 * Created by kenny.hadisaputra on 05/04/22
 */
fun GameSmallWidgetView.setupGiveaway(
    desc: String,
    timerInfo: String
) {
    getIconUnifyDrawable(
        context = context,
        iconId = IconUnify.GIFT,
        assetColor = MethodChecker.getColor(context, R.color.play_bro_giveaway_icon_color)
    )?.let(::setIcon)

    setContentBackground(MethodChecker.getDrawable(context, R.drawable.bg_play_giveaway_header))
    setTimerInfo(timerInfo)
    setTimer(70000) {}
    description = desc
}

fun GameSmallWidgetView.setupQuiz(
    question: String,
    timerInfo: String
) {
    getIconUnifyDrawable(
        context = context,
        iconId = IconUnify.QUIZ,
        assetColor = MethodChecker.getColor(context, R.color.play_bro_quiz_icon_color)
    )?.let(::setIcon)

    setContentBackground(MethodChecker.getDrawable(context, R.drawable.bg_play_quiz_header))
    setTimerInfo(timerInfo)
    setTimer(70000) {}
    description = question
}