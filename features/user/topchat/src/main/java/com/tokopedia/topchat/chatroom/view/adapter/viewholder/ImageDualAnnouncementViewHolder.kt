package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.tokopedia.abstraction.common.utils.image.DynamicSizeImageRequestListener
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.listener.DualAnnouncementListener
import com.tokopedia.topchat.chatroom.view.viewmodel.ImageDualAnnouncementViewModel

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

        ImageHandler.loadImageWithListener(top, viewModel.imageUrlTop, DynamicSizeImageRequestListener())
        top!!.setOnClickListener { v: View ->
            viewListener.onDualAnnouncementClicked(viewModel.redirectUrlTop, viewModel
                    .attachmentId, viewModel.blastId)
        }

        ImageHandler.loadImageWithListener(bottom, viewModel.imageUrlBottom, DynamicSizeImageRequestListener())
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
