package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.listener.DualAnnouncementListener
import com.tokopedia.topchat.chatroom.view.viewmodel.ImageDualAnnouncementUiModel

/**
 * Created by Hendri on 22/06/18.
 */
class ImageDualAnnouncementViewHolder(itemView: View, private val viewListener: DualAnnouncementListener)
    : BaseChatViewHolder<ImageDualAnnouncementUiModel>(itemView) {

    private val top: ImageView?
    private val bottom: ImageView?


    init {
        top = itemView.findViewById(R.id.dual_image_top)
        bottom = itemView.findViewById(R.id.dual_image_bottom)
    }

    override fun bind(viewModel: ImageDualAnnouncementUiModel) {
        super.bind(viewModel)

        top?.loadImage(viewModel.imageUrlTop) {
            adaptiveImageSizeRequest(true)
            fitCenter()
        }
        top!!.setOnClickListener { v: View ->
            viewListener.onDualAnnouncementClicked(viewModel.redirectUrlTop, viewModel
                    .attachmentId, viewModel.broadcastBlastId)
        }

        bottom?.loadImage(viewModel.imageUrlBottom) {
            adaptiveImageSizeRequest(true)
            fitCenter()
        }
        bottom!!.setOnClickListener { v: View ->
            viewListener.onDualAnnouncementClicked(viewModel.redirectUrlBottom, viewModel
                    .attachmentId, viewModel.broadcastBlastId)
        }
    }

    override fun onViewRecycled() {
        super.onViewRecycled()
        if (top != null) {
            Glide.with(itemView.context).clear(top)
        }

        if (bottom != null) {
            Glide.with(itemView.context).clear(bottom)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_announcement_dual_image
    }
}
