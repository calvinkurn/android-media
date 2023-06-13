package com.tokopedia.shop.common.view.bottomsheet.viewholder

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.shop.R
import com.tokopedia.shop.common.view.bottomsheet.listener.ShopShareBottomsheetListener
import com.tokopedia.shop.common.view.model.ShopShareModel
import com.tokopedia.shop.databinding.ItemShopPageShareBottomSheetBinding
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class ShopShareBottomsheetViewHolder(itemView: View, private val bottomsheetListener: ShopShareBottomsheetListener) : RecyclerView.ViewHolder(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_page_share_bottom_sheet
    }

    private val viewBinding: ItemShopPageShareBottomSheetBinding? by viewBinding()
    private val itemSeparator: View? = viewBinding?.itemSeparator
    private val ivSocialMediaLogo: ImageView? = viewBinding?.ivSocialMediaLogo
    private val tvSocialMediaName: Typography? = viewBinding?.tvSocialMediaName

    fun bind(shop: ShopShareModel) {
        if (shop is ShopShareModel.Others) {
            itemSeparator?.invisible()
        }
        ivSocialMediaLogo?.setImageDrawable(shop.socialMediaIcon)
        tvSocialMediaName?.text = shop.socialMediaName
        itemView.setOnClickListener {
            bottomsheetListener.onItemBottomsheetShareClicked(shop)
        }
    }
}
