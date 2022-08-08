package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifyprinciples.Typography
import androidx.annotation.IdRes

/**
 * @author by astidhiyaa on 08/08/22
 */
class UpcomingDescriptionViewComponent(
    container: ViewGroup,
    @IdRes idRes: Int,
    private val listener: Listener
) : ViewComponent(container, idRes) {

    private val txt = (rootView as Typography)

    fun setupText(description: String) {
        txt.text = description
    }

    interface Listener {
        fun onTextClicked(isExpand: Boolean, view: UpcomingDescriptionViewComponent)
    }
}