package com.tokopedia.product.info.view.viewholder.productdetail

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.databinding.ProductDetailCardViewHolderBinding
import com.tokopedia.product.info.view.ProductDetailInfoListener
import com.tokopedia.product.info.view.models.ProductDetailInfoCardDataModel

class ProductDetailInfoCardViewHolder(
    view: View,
    private val listener: ProductDetailInfoListener
) : AbstractViewHolder<ProductDetailInfoCardDataModel>(view) {

    companion object {

        val LAYOUT = R.layout.product_detail_card_view_holder
    }

    private val binding = ProductDetailCardViewHolderBinding.bind(view)

    override fun bind(element: ProductDetailInfoCardDataModel) {
        binding.imgIcon.loadImage(element.image)
        binding.txtTitleCard.text = element.title

        binding.root.setOnClickListener {
            listener.onCustomInfoClicked(element.applink)
        }
    }
}
