package com.tokopedia.product.detail.postatc.component.productinfo

import android.view.View
import com.tokopedia.product.detail.databinding.ItemProductInfoBinding
import com.tokopedia.product.detail.postatc.base.PostAtcViewHolder

class ProductInfoViewHolder(view: View) : PostAtcViewHolder<ProductInfoUiModel>(view) {

    private val binding = ItemProductInfoBinding.bind(view)

    override fun bind(element: ProductInfoUiModel) {
        binding.apply {
            postAtcProductInfoImage.setImageUrl(element.imageLink)
            postAtcProductInfoTitle.text = element.title
            postAtcProductInfoSubtitle.text = element.subtitle
            postAtcProductInfoButton.text = element.buttonText
        }
    }
}
