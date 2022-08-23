package com.tokopedia.chat_common.view.adapter.viewholder

import android.content.res.Resources
import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.common.utils.image.DynamicSizeImageRequestListener
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.chat_common.R
import com.tokopedia.chat_common.data.ImageAnnouncementUiModel
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageAnnouncementListener

/**
 * @author by nisie on 5/15/18.
 */
class ImageAnnouncementViewHolder(itemView: View, listener: ImageAnnouncementListener) :
    BaseChatViewHolder<ImageAnnouncementUiModel>(itemView) {

    private val listener: ImageAnnouncementListener = listener
    private val attachment: ImageView? = itemView.findViewById(R.id.image)
    private val container: LinearLayout = itemView.findViewById(R.id.card_group_chat_message)
    private val btnCheckNow: Button = itemView.findViewById(R.id.btn_check)

    override fun bind(uiModel: ImageAnnouncementUiModel) {
        super.bind(uiModel)
        uiModel.let {
            ImageHandler.loadImageWithListener(
                attachment,
                it.imageUrl,
                DynamicSizeImageRequestListener()
            )
            container.setOnClickListener { view: View? -> listener.onImageAnnouncementClicked(it) }
            btnCheckNow.setOnClickListener { view: View? ->
                listener.onImageAnnouncementClicked(
                    it
                )
            }
        }
        bindCornerAttachment()
    }

    private fun bindCornerAttachment() {
        attachment?.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(
                    0,
                    0,
                    view.width,
                    view.height + toDp(8),
                    toDp(8)
                        .toFloat()
                )
            }
        }
        attachment?.clipToOutline = true
    }

    override fun onViewRecycled() {
        super.onViewRecycled()
        if (attachment != null) {
            ImageHandler.clearImage(attachment)
        }
    }

    override val dateId: Int
        get() = R.id.tvDate

    private fun toDp(value: Int): Int {
        return (value * Resources.getSystem().displayMetrics.density).toInt()
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_image_announcement
    }

}