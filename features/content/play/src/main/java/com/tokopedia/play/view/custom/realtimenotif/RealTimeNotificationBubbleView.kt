package com.tokopedia.play.view.custom.realtimenotif

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import com.tokopedia.play.R
import com.tokopedia.play.view.uimodel.RealTimeNotificationUiModel
import com.tokopedia.play_common.view.RoundedConstraintLayout
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by jegul on 03/08/21
 */
class RealTimeNotificationBubbleView : RoundedConstraintLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val imageNotif: ImageUnify
    private val tvNotifCopy: Typography

    init {
        val view = View.inflate(context, R.layout.view_real_time_notification_bubble, this)

        imageNotif = view.findViewById(R.id.image_notif)
        tvNotifCopy = view.findViewById(R.id.tv_notif_copy)

        setupView(view)
    }

    fun setNotification(notification: RealTimeNotificationUiModel) {
        imageNotif.setImageUrl(notification.icon)
        tvNotifCopy.text = notification.text

        val bgColor = try {
            Color.parseColor(notification.bgColor)
        } catch (e: IllegalArgumentException) {
            Color.parseColor("#AD009F92")
        }
        setBackgroundColor(bgColor)
    }

    private fun setupView(view: View) {
        setCornerRadius(
                resources.getDimensionPixelSize(R.dimen.play_real_time_notif_radius).toFloat()
        )
    }
}