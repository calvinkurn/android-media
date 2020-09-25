package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.graphics.Outline
import android.os.Build
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import com.tokopedia.abstraction.common.utils.image.DynamicSizeImageRequestListener
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.chat_common.data.ImageAnnouncementViewModel
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageAnnouncementListener
import com.tokopedia.topchat.R
import com.tokopedia.unifycomponents.toPx

class TopchatImageAnnouncementViewHolder(
        itemView: View?,
        private val listener: ImageAnnouncementListener
) : BaseChatViewHolder<ImageAnnouncementViewModel>(itemView) {

    private val attachment: ImageView? = itemView?.findViewById(R.id.image)
    private val container: LinearLayout? = itemView?.findViewById(R.id.ll_banner_container)
    private val btnCheckNow: Button? = itemView?.findViewById(R.id.btn_check)
    private val imageSizer = DynamicSizeImageRequestListener()

    override fun bind(viewModel: ImageAnnouncementViewModel) {
        super.bind(viewModel)
        bindBannerImage(viewModel)
        bindClick(viewModel)
    }

    override fun onViewRecycled() {
        super.onViewRecycled()
        attachment?.let {
            ImageHandler.clearImage(it)
        }
    }

    private fun bindBannerImage(viewModel: ImageAnnouncementViewModel) {
        ImageHandler.loadImageWithListener(attachment, viewModel.imageUrl, imageSizer)
        bindCornerAttachment()
    }

    private fun bindClick(viewModel: ImageAnnouncementViewModel) {
        val onClick = View.OnClickListener { listener.onImageAnnouncementClicked(viewModel) }
        container?.setOnClickListener(onClick)
        btnCheckNow?.setOnClickListener(onClick)
    }

    private fun bindCornerAttachment() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            attachment?.outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View, outline: Outline) {
                    outline.setRoundRect(
                            0, 0, view.width, view.height + 8.toPx(), 8.toPx().toFloat()
                    )
                }
            }
            attachment?.clipToOutline = true
        }
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_layout_image_announcement
    }
}