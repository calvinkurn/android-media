package com.tokopedia.play.broadcaster.view.partial.game

import android.view.ViewGroup
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created By : Jonathan Darwin on March 29, 2022
 */
class SelectGameViewComponent(
    container: ViewGroup,
    private val listener: Listener
) : ViewComponent(container, R.id.ic_game){



    interface Listener {

    }
}