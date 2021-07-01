package com.tokopedia.play.view.viewcomponent.interactive

import android.view.ViewGroup
import com.tokopedia.play.R
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by jegul on 29/06/21
 */
class InteractiveFinishedViewComponent(
        container: ViewGroup
) : ViewComponent(container, R.id.view_interactive_finish) {

    private val tvInteractiveFinishInfo = findViewById<Typography>(R.id.tv_interactive_finish_info)

    fun setInfo(info: String) {
        tvInteractiveFinishInfo.text = info
    }
}