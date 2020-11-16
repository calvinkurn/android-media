package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.uimodel.EmptyNotificationUiModel

class EmptyNotificationViewHolder(
        itemView: View?
) : AbstractViewHolder<EmptyNotificationUiModel>(itemView) {

    private val image: ImageView? = itemView?.findViewById(R.id.iv_icon)

    override fun bind(element: EmptyNotificationUiModel) {
        bindImage(element)
    }

    private fun bindImage(element: EmptyNotificationUiModel) {
        ImageHandler.LoadImage(image, emptyImageUrl)
    }

    companion object {
        val LAYOUT = R.layout.item_notification_empty
        const val emptyImageUrl = "https://ecs7.tokopedia.net/android/user/item_empty_notification.png"
    }
}