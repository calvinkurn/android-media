package com.tokopedia.shop.common.view.bottomsheet.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.shop.R
import com.tokopedia.shop.common.view.bottomsheet.listener.ShopShareBottomsheetListener
import com.tokopedia.shop.common.view.model.ShopShareModel
import kotlinx.android.synthetic.main.item_shop_page_share_bottom_sheet.view.*

class ShopShareBottomsheetViewHolder(itemView: View, private val bottomsheetListener: ShopShareBottomsheetListener) : RecyclerView.ViewHolder(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_page_share_bottom_sheet
    }

    fun bind(shop: ShopShareModel) {
        if(shop is ShopShareModel.Others) {
            itemView.item_separator?.invisible()
        }
        itemView.iv_social_media_logo?.setImageDrawable(shop.socialMediaIcon)
        itemView.tv_social_media_name?.text = shop.socialMediaName
        itemView.setOnClickListener {
            bottomsheetListener.onItemBottomsheetShareClicked(shop)
        }
    }

}