package com.tokopedia.universal_sharing.view.bottomsheet.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.universal_sharing.R
import com.tokopedia.universal_sharing.databinding.UniversalSharingPostPurchaseShopTitleItemBinding
import com.tokopedia.universal_sharing.view.model.UniversalSharingPostPurchaseShopTitleUiModel
import com.tokopedia.utils.view.binding.viewBinding

class UniversalSharingPostPurchaseShopTitleViewHolder(
    itemView: View
) : AbstractViewHolder<UniversalSharingPostPurchaseShopTitleUiModel>(itemView) {

    private val binding: UniversalSharingPostPurchaseShopTitleItemBinding? by viewBinding()

    override fun bind(element: UniversalSharingPostPurchaseShopTitleUiModel) {
        bindTitle(element)
        bindIcon(element)
    }

    private fun bindIcon(element: UniversalSharingPostPurchaseShopTitleUiModel) {
        binding?.universalSharingIconShop?.loadImage(element.iconUrl)
    }

    private fun bindTitle(element: UniversalSharingPostPurchaseShopTitleUiModel) {
        binding?.universalSharingTvShop?.text = element.name
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.universal_sharing_post_purchase_shop_title_item
    }
}
