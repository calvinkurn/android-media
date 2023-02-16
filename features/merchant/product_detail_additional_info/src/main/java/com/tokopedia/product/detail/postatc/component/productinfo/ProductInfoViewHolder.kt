package com.tokopedia.product.detail.postatc.component.productinfo

import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.product.detail.databinding.ItemProductInfoBinding
import com.tokopedia.product.detail.postatc.base.PostAtcListener
import com.tokopedia.product.detail.postatc.base.PostAtcViewHolder

class ProductInfoViewHolder(
    private val binding: ItemProductInfoBinding,
    private val listener: PostAtcListener
) : PostAtcViewHolder<ProductInfoUiModel>(binding.root) {
    override fun bind(element: ProductInfoUiModel) {
        binding.apply {
            postAtcProductInfoImage.setImageUrl(element.imageLink)
            postAtcProductInfoTitle.text = element.title
            postAtcProductInfoSubtitle.text = element.subtitle
            postAtcProductInfoButton.text = element.buttonText

            postAtcProductInfoButton.setOnClickListener { onClickLihatKeranjang(element) }

            root.addOnImpressionListener(element.impressHolder) {
                listener.impressComponent(getComponentTrackData(element))
            }
        }
    }

    private fun onClickLihatKeranjang(element: ProductInfoUiModel) {
        listener.onClickLihatKeranjang(
            element.cartId,
            getComponentTrackData(element)
        )
    }

}
