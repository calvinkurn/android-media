package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.inboxcommon.time.TimeHelper
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.unifyprinciples.Typography

class NormalNotificationViewHolder(
        itemView: View?
) : AbstractViewHolder<NotificationUiModel>(itemView) {

    private val icon: ImageView? = itemView?.findViewById(R.id.iv_icon)
    private val type: Typography? = itemView?.findViewById(R.id.txt_notification_type)
    private val time: Typography? = itemView?.findViewById(R.id.txt_notification_time)
    private val title: Typography? = itemView?.findViewById(R.id.txt_notification_title)
    private val desc: Typography? = itemView?.findViewById(R.id.txt_notification_desc)

    override fun bind(element: NotificationUiModel) {
        bindTitle(element)
        bindDesc(element)
        bindNotificationType(element)
        bindIcon(element)
        bindTime(element)
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
        ImageHandler.LoadImage(icon, element.sectionIcon)
    }

    private fun bindTime(element: NotificationUiModel) {
        time?.text = TimeHelper.getRelativeTimeFromNow(element.createTimeUnix)
    }

    companion object {
        val LAYOUT = R.layout.item_notifcenter_normal_notification
    }
}