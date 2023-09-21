package com.tokopedia.universal_sharing.view.bottomsheet.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.universal_sharing.R
import com.tokopedia.universal_sharing.databinding.UniversalSharingPostPurchaseProductItemBinding
import com.tokopedia.universal_sharing.view.bottomsheet.listener.UniversalSharingPostPurchaseBottomSheetListener
import com.tokopedia.universal_sharing.view.model.UniversalSharingPostPurchaseProductUiModel
import com.tokopedia.utils.view.binding.viewBinding

class UniversalSharingPostPurchaseProductViewHolder(
    itemView: View,
    private val listener: UniversalSharingPostPurchaseBottomSheetListener
) : AbstractViewHolder<UniversalSharingPostPurchaseProductUiModel>(itemView) {

    private val binding: UniversalSharingPostPurchaseProductItemBinding? by viewBinding()

    override fun bind(element: UniversalSharingPostPurchaseProductUiModel) {
        bindImage(element)
        bindProduct(element)
        bindListener(element)
    }

    private fun bindImage(element: UniversalSharingPostPurchaseProductUiModel) {
        binding?.universalSharingIvProduct?.loadImage(element.imageUrl) {
            this.setRoundedRadius(10.toFloat())
        }
    }

    private fun bindProduct(element: UniversalSharingPostPurchaseProductUiModel) {
        binding?.universalSharingTvProductName?.text = element.name
        val finalPriceText = "Rp. ${element.price}"
        binding?.universalSharingTvProductPrice?.text = finalPriceText
    }

    private fun bindListener(element: UniversalSharingPostPurchaseProductUiModel) {
        binding?.universalSharingLayoutBtnShare?.setOnClickListener {
            listener.onClickShare(element.productId)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.universal_sharing_post_purchase_product_item
    }
}
