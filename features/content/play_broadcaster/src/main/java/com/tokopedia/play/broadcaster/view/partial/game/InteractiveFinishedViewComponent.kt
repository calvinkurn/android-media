package com.tokopedia.play.broadcaster.view.partial.game

import android.view.ViewGroup
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play_common.view.game.InteractiveFinishView
import com.tokopedia.play_common.view.game.setupGiveaway
import com.tokopedia.play_common.view.game.setupQuiz
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by kenny.hadisaputra on 20/04/22
 */
class InteractiveFinishedViewComponent(
    container: ViewGroup,
) : ViewComponent(container, R.id.view_interactive_finished) {

    private val view = rootView as InteractiveFinishView

    fun setupGiveaway() {
        view.setupGiveaway()
    }

    fun setupQuiz() {
        view.setupQuiz()
    }
}
