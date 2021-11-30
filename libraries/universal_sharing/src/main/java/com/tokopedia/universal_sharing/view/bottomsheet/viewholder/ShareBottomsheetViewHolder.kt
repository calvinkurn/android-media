package com.tokopedia.universal_sharing.view.bottomsheet.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.universal_sharing.R
import com.tokopedia.universal_sharing.view.model.ShareModel
import kotlinx.android.synthetic.main.channel_item_universal_share_bottom_sheet.view.*

class ShareBottomsheetViewHolder(itemView: View, private val bottomsheetListener: (shareModel: ShareModel) -> Unit) : RecyclerView.ViewHolder(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.channel_item_universal_share_bottom_sheet
    }

    fun bind(shareModel: ShareModel) {
        itemView.iv_social_media_logo?.setImageDrawable(shareModel.socialMediaIcon)
        itemView.iv_social_media_logo?.setBackgroundResource(R.drawable.universal_sharing_ic_ellipse_49)
        itemView.tv_social_media_name?.text = shareModel.socialMediaName
        itemView.setOnClickListener {
            bottomsheetListener(shareModel)
        }
    }

}