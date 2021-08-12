package com.tokopedia.play.view.viewcomponent.realtimenotif

import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import androidx.transition.Slide
import androidx.transition.TransitionManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.play.R
import com.tokopedia.play.view.custom.realtimenotif.RealTimeNotificationBubbleView
import com.tokopedia.play.view.uimodel.RealTimeNotificationUiModel
import com.tokopedia.play_common.util.extension.awaitPreDraw
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by jegul on 12/08/21
 */
class RealTimeNotificationViewComponent(
        container: ViewGroup
) : ViewComponent(container, R.id.view_real_time_notification) {

    private val rtnBubbleView: RealTimeNotificationBubbleView = findViewById(
            R.id.view_real_time_notification
    )

    fun setNotification(notification: RealTimeNotificationUiModel) {
        rtnBubbleView.setText(notification.text)
        rtnBubbleView.setIconUrl(notification.icon)

        val bgColor = try {
            Color.parseColor(notification.bgColor)
        } catch (e: IllegalArgumentException) {
            MethodChecker.getColor(
                    rootView.context,
                    R.color.play_dms_default_real_time_notif_bg
            )
        }
        rtnBubbleView.setBackgroundColor(bgColor)
    }

    fun showAnimated() {
        TransitionManager.beginDelayedTransition(rootView as ViewGroup, Slide(Gravity.START))
        show()
    }

    fun hideAnimated() {
        TransitionManager.beginDelayedTransition(rootView as ViewGroup, Slide(Gravity.START))
        hide()
    }
}