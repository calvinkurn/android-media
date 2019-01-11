package com.tokopedia.topchat.revamp.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.tkpd.library.utils.ImageHandler
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.viewmodel.imageannouncement.ImageDualAnnouncementViewModel
import com.tokopedia.topchat.common.util.ChatGlideImageRequestListener
import com.tokopedia.topchat.revamp.view.listener.DualAnnouncementListener

/**
 * Created by Hendri on 22/06/18.
 */
class ImageDualAnnouncementViewHolder(itemView: View, private val viewListener: DualAnnouncementListener)
    : BaseChatViewHolder<ImageDualAnnouncementViewModel>(itemView) {

    private val top: ImageView?
    private val bottom: ImageView?


    init {
        top = itemView.findViewById(R.id.dual_image_top)
        bottom = itemView.findViewById(R.id.dual_image_bottom)
    }

    override fun bind(viewModel: ImageDualAnnouncementViewModel) {
        super.bind(viewModel)

        ImageHandler.loadImageChat(top, viewModel.imageUrlTop, ChatGlideImageRequestListener())
        top!!.setOnClickListener { v: View ->
            viewListener.onDualAnnouncementClicked(viewModel.redirectUrlTop, viewModel
                    .attachmentId, viewModel.blastId)
        }

        ImageHandler.loadImageChat(bottom, viewModel.imageUrlBottom, ChatGlideImageRequestListener())
        bottom!!.setOnClickListener { v: View ->
            viewListener.onDualAnnouncementClicked(viewModel.redirectUrlBottom, viewModel
                    .attachmentId, viewModel.blastId)
        }
    }

    override fun onViewRecycled() {
        super.onViewRecycled()
        if (top != null) {
            Glide.clear(top)
        }

        if (bottom != null) {
            Glide.clear(bottom)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_announcement_dual_image
    }
}
