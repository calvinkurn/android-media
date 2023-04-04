package com.tokopedia.play.view.viewcomponent

import android.annotation.SuppressLint
import android.view.ViewGroup
import com.tokopedia.content.common.ui.custom.VerticalSwipeOnboardingView
import com.tokopedia.play.R
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by kenny.hadisaputra on 22/02/23
 */
@SuppressLint("ClickableViewAccessibility")
class SwipeCoachMarkViewComponent(container: ViewGroup) : ViewComponent(
    container, R.id.view_vertical_swipe_coachmark,
) {

    private val onboarding = rootView as VerticalSwipeOnboardingView

    init {
        onboarding.setText(
            getString(R.string.play_check_next_video)
        )
    }

    fun showAnimated() {
        onboarding.showAnimated()
    }

    fun hideAnimated() {
        onboarding.hideAnimated()
    }
}
