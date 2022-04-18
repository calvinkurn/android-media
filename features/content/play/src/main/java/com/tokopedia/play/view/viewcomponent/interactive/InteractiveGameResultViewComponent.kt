package com.tokopedia.play.view.viewcomponent.interactive

import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.play.R

/**
 * @author by astidhiyaa on 18/04/22
 */
class InteractiveGameResultViewComponent (container: ViewGroup,
                                          listener: Listener,
) : ViewComponent(container, R.id.view_game_result) {

    private val gameResult = rootView as ConstraintLayout

    init {
        gameResult.setOnClickListener {
            listener.onGameResultClicked(this)
        }
    }

    interface Listener {
        fun onGameResultClicked(view: InteractiveGameResultViewComponent)
    }
}