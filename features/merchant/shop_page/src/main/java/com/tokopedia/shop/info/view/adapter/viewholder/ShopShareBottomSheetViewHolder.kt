package com.tokopedia.shop.info.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.shop.R
import com.tokopedia.shop.info.view.model.SocialMediaShareModel
import kotlinx.android.synthetic.main.item_shop_page_share_bottom_sheet.view.*

class ShopShareBottomSheetViewHolder(itemView: View, private val listener: ShopShareListener) : RecyclerView.ViewHolder(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_page_share_bottom_sheet
    }

    fun bind(socialMedia: SocialMediaShareModel) {
        if(socialMedia is SocialMediaShareModel.Others) {
            itemView.item_separator.invisible()
        }
        itemView.iv_social_media_logo.setImageDrawable(socialMedia.socialMediaIcon)
        itemView.tv_social_media_name.text = socialMedia.socialMediaName
        itemView.setOnClickListener {
            listener.onShopShareClicked(socialMedia)
        }
    }

    interface ShopShareListener {
        fun onShopShareClicked(socialMedia: SocialMediaShareModel)
    }

}