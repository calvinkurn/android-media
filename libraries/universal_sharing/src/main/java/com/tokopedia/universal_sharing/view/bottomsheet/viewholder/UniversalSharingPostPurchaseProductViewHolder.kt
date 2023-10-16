package com.tokopedia.universal_sharing.view.bottomsheet.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.universal_sharing.R
import com.tokopedia.universal_sharing.databinding.UniversalSharingPostPurchaseProductItemBinding
import com.tokopedia.universal_sharing.view.bottomsheet.listener.postpurchase.UniversalSharingPostPurchaseProductListener
import com.tokopedia.universal_sharing.view.model.UniversalSharingPostPurchaseProductUiModel
import com.tokopedia.utils.view.binding.viewBinding

class UniversalSharingPostPurchaseProductViewHolder(
    itemView: View,
    private val listener: UniversalSharingPostPurchaseProductListener
) : AbstractViewHolder<UniversalSharingPostPurchaseProductUiModel>(itemView) {

    private val binding: UniversalSharingPostPurchaseProductItemBinding? by viewBinding()

    init {
        binding?.universalSharingLayoutBtnShare?.setOnClickListener {
            val uiModel = itemView.tag as? UniversalSharingPostPurchaseProductUiModel
            uiModel?.let {
                setupListener(it)
            }
        }
    }

    override fun bind(element: UniversalSharingPostPurchaseProductUiModel) {
        bindImage(element)
        bindProduct(element)
        itemView.tag = element
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

    private fun setupListener(element: UniversalSharingPostPurchaseProductUiModel) {
        listener.onClickShare(
            orderId = element.orderId,
            shopName = element.shopName,
            productId = element.productId
        )
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.universal_sharing_post_purchase_product_item
    }
}
