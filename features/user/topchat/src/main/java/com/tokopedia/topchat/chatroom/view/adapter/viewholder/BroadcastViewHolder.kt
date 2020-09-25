package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_IMAGE_ANNOUNCEMENT
import com.tokopedia.chat_common.data.ImageAnnouncementViewModel
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder.ImageAnnouncementViewHolderBinder
import com.tokopedia.topchat.chatroom.view.uimodel.BroadCastUiModel

class BroadcastViewHolder(itemView: View?) : AbstractViewHolder<BroadCastUiModel>(itemView) {

    private val bannerView: ImageView? = itemView?.findViewById(R.id.iv_banner)

    override fun bind(element: BroadCastUiModel) {
        bindBanner(element)
    }

    private fun bindBanner(element: BroadCastUiModel) {
        val banner = element.items[TYPE_IMAGE_ANNOUNCEMENT] as? ImageAnnouncementViewModel ?: return
        ImageAnnouncementViewHolderBinder.bindBannerImage(banner, bannerView)
    }

    companion object {
        val LAYOUT = R.layout.item_broadcast_message_bubble
    }
}