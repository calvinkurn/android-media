package com.tokopedia.play_common.view.game

import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.play_common.R

/**
 * Created by kenny.hadisaputra on 11/04/22
 */
fun InteractiveFinishView.setupGiveaway() {
    background = MethodChecker.getDrawable(context, R.drawable.bg_play_giveaway_widget)
    setDescription(context.getString(R.string.play_interactive_giveaway_finish))
}

fun InteractiveFinishView.setupQuiz() {
    background = MethodChecker.getDrawable(context, R.drawable.bg_play_quiz_widget)
    setDescription(context.getString(R.string.play_interactive_quiz_finish))
}