package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3

import android.content.res.ColorStateList
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.ImageViewCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.inboxcommon.time.TimeHelper
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.notifcenter.listener.v3.NotificationItemListener
import com.tokopedia.unifyprinciples.Typography

abstract class BaseNotificationViewHolder constructor(
        itemView: View?,
        private val listener: NotificationItemListener?
) : AbstractViewHolder<NotificationUiModel>(itemView) {

    protected val container: ConstraintLayout? = itemView?.findViewById(R.id.notification_container)
    protected val icon: ImageView? = itemView?.findViewById(R.id.iv_icon)
    protected val type: Typography? = itemView?.findViewById(R.id.txt_notification_type)
    protected val time: Typography? = itemView?.findViewById(R.id.txt_notification_time)
    protected val title: Typography? = itemView?.findViewById(R.id.txt_notification_title)
    protected val desc: Typography? = itemView?.findViewById(R.id.txt_notification_desc)

    protected val clickedColor = MethodChecker.getColor(
            itemView?.context, com.tokopedia.unifycomponents.R.color.Green_G100
    )
    protected val clickedColorIcon = MethodChecker.getColor(
            itemView?.context, com.tokopedia.unifycomponents.R.color.Green_G500
    )

    override fun bind(element: NotificationUiModel) {
        bindContainer(element)
        bindTitle(element)
        bindDesc(element)
        bindNotificationType(element)
        bindIcon(element)
        bindTime(element)
        bindClick(element)
    }

    private fun bindContainer(element: NotificationUiModel) {
        if (!element.isRead()) {
            container?.setBackgroundColor(clickedColor)
        } else {
            container?.background = null
        }
    }

    private fun bindTitle(element: NotificationUiModel) {
        title?.text = element.title
    }

    private fun bindDesc(element: NotificationUiModel) {
        desc?.text = element.shortDescription
    }

    private fun bindNotificationType(element: NotificationUiModel) {
        type?.text = element.sectionKey
    }

    private fun bindIcon(element: NotificationUiModel) {
        icon?.let {
            ImageHandler.LoadImage(icon, element.sectionIcon)
            if (!element.isRead()) {
                ImageViewCompat.setImageTintList(icon, ColorStateList.valueOf(clickedColorIcon))
            } else {
                ImageViewCompat.setImageTintList(icon, null)
            }
        }
    }

    private fun bindTime(element: NotificationUiModel) {
        time?.text = TimeHelper.getRelativeTimeFromNow(element.createTimeUnix)
    }

    private fun bindClick(element: NotificationUiModel) {
        container?.setOnClickListener {
            if (element.isLongerContent) {
                listener?.showLongerContent(element)
            } else {
                RouteManager.route(itemView.context, element.dataNotification.appLink)
            }
        }
    }

}