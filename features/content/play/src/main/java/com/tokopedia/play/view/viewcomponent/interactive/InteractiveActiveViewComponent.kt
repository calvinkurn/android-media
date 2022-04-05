package com.tokopedia.play.view.viewcomponent.interactive

import android.view.ViewGroup
import com.tokopedia.play.R
import com.tokopedia.play_common.view.game.GameSmallWidgetView
import com.tokopedia.play_common.view.game.setupGiveaway
import com.tokopedia.play_common.view.game.setupQuiz
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by kenny.hadisaputra on 05/04/22
 */
class InteractiveActiveViewComponent(
    container: ViewGroup,
) : ViewComponent(container, R.id.widget_game_small) {

    private val widget = rootView as GameSmallWidgetView

    fun setGiveaway() {
        widget.setupGiveaway(
            desc = "Kapan ulang tahun Rockbros Indonesia?",
            timerInfo = "Berakhir",
        )
    }

    fun setQuiz() {
        widget.setupQuiz(
            question = "Kapan ulang tahun Rockbros Indonesia?",
            timerInfo = "Berakhir",
        )
    }
}