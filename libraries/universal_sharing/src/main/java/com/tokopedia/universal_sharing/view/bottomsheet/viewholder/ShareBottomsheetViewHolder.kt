package com.tokopedia.universal_sharing.view.bottomsheet.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.universal_sharing.R
import com.tokopedia.universal_sharing.view.model.ShareModel
class ShareBottomsheetViewHolder(itemView: View, private val bottomsheetListener: (shareModel: ShareModel) -> Unit) : RecyclerView.ViewHolder(itemView) {

    private var ivSocialMediaLogo : ImageView
    private var tvSocialMediaName : TextView
    init {
        ivSocialMediaLogo = itemView.findViewById(R.id.iv_social_media_logo)
        tvSocialMediaName = itemView.findViewById(R.id.tv_social_media_name)
    }
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.channel_item_universal_share_bottom_sheet
    }

    fun bind(shareModel: ShareModel) {
        ivSocialMediaLogo.setImageDrawable(shareModel.socialMediaIcon)
        ivSocialMediaLogo.setBackgroundResource(R.drawable.universal_sharing_ic_ellipse_49)
        tvSocialMediaName.text = shareModel.socialMediaName
        itemView.setOnClickListener {
            bottomsheetListener(shareModel)
        }
    }

}
