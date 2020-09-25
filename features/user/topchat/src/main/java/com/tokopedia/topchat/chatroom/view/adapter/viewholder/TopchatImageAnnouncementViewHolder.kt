package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.chat_common.data.ImageAnnouncementViewModel
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageAnnouncementListener
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder.ImageAnnouncementViewHolderBinder

class TopchatImageAnnouncementViewHolder(
        itemView: View?,
        private val listener: ImageAnnouncementListener
) : BaseChatViewHolder<ImageAnnouncementViewModel>(itemView) {

    private val attachment: ImageView? = itemView?.findViewById(R.id.image)
    private val container: LinearLayout? = itemView?.findViewById(R.id.ll_banner_container)
    private val btnCheckNow: Button? = itemView?.findViewById(R.id.btn_check)

    override fun bind(viewModel: ImageAnnouncementViewModel) {
        super.bind(viewModel)
        ImageAnnouncementViewHolderBinder.bindBannerImage(viewModel, attachment)
        bindClick(viewModel)
    }

    override fun onViewRecycled() {
        super.onViewRecycled()
        attachment?.let {
            ImageHandler.clearImage(it)
        }
    }

    private fun bindClick(viewModel: ImageAnnouncementViewModel) {
        val onClick = View.OnClickListener { listener.onImageAnnouncementClicked(viewModel) }
        container?.setOnClickListener(onClick)
        btnCheckNow?.setOnClickListener(onClick)
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_layout_image_announcement
    }
}