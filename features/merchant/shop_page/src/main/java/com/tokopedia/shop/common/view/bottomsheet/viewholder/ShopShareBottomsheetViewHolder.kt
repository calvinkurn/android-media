package com.tokopedia.shop.common.view.bottomsheet.viewholder

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.shop.R
import com.tokopedia.shop.common.view.bottomsheet.listener.ShopShareBottomsheetListener
import com.tokopedia.shop.common.view.model.ShopShareModel
import com.tokopedia.unifyprinciples.Typography

class ShopShareBottomsheetViewHolder(itemView: View, private val bottomsheetListener: ShopShareBottomsheetListener) : RecyclerView.ViewHolder(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_page_share_bottom_sheet
    }

    private val itemSeparator: View? = itemView.findViewById(R.id.item_separator)
    private val ivSocialMediaLogo: ImageView? = itemView.findViewById(R.id.iv_social_media_logo)
    private val tvSocialMediaName: Typography? = itemView.findViewById(R.id.tv_social_media_name)

    fun bind(shop: ShopShareModel) {
        if(shop is ShopShareModel.Others) {
            itemSeparator?.invisible()
        }
        ivSocialMediaLogo?.setImageDrawable(shop.socialMediaIcon)
        tvSocialMediaName?.text = shop.socialMediaName
        itemView.setOnClickListener {
            bottomsheetListener.onItemBottomsheetShareClicked(shop)
        }
    }

}