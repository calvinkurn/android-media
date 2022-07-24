package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.tokopedia.chat_common.data.ImageAnnouncementUiModel
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageAnnouncementListener
import com.tokopedia.media.loader.clearImage
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder.ImageAnnouncementViewHolderBinder

class TopchatImageAnnouncementViewHolder(
        itemView: View?,
        private val listener: ImageAnnouncementListener
) : BaseChatViewHolder<ImageAnnouncementUiModel>(itemView) {

    private val attachment: ImageView? = itemView?.findViewById(R.id.image)
    private val container: LinearLayout? = itemView?.findViewById(R.id.ll_banner_container)

    override fun bind(uiModel: ImageAnnouncementUiModel) {
        super.bind(uiModel)
        ImageAnnouncementViewHolderBinder.bindBannerImage(uiModel, attachment)
        ImageAnnouncementViewHolderBinder.bindBannerClick(uiModel, container, listener)
    }

    override fun onViewRecycled() {
        super.onViewRecycled()
        attachment?.let {
            it.clearImage()
        }
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_layout_image_announcement
    }
}