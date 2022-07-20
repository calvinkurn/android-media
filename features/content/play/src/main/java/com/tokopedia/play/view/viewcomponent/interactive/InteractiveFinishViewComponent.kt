package com.tokopedia.play.view.viewcomponent.interactive

import android.view.ViewGroup
import com.tokopedia.play.R
import com.tokopedia.play_common.view.game.InteractiveFinishView
import com.tokopedia.play_common.view.game.setupGiveaway
import com.tokopedia.play_common.view.game.setupQuiz
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by kenny.hadisaputra on 11/04/22
 */
class InteractiveFinishViewComponent(
    container: ViewGroup,
) : ViewComponent(container, R.id.view_interactive_finish) {

    private val view = rootView as InteractiveFinishView

    fun setupGiveaway() {
        view.setupGiveaway()
    }

    fun setupQuiz() {
        view.setupQuiz()
    }
}