package com.tokopedia.addongifting.addonunavailablebottomsheet

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.addongifting.R
import com.tokopedia.addongifting.databinding.ItemProductUnavailableBinding

class AddOnUnavailableProductViewHolder constructor(private val viewBinding: ItemProductUnavailableBinding) : RecyclerView.ViewHolder(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_product_unavailable
    }

    fun bind(addOnUnavailableProductUiModel: AddOnUnavailableProductUiModel) {
        with(viewBinding) {
            imageUnavailableProduct.setImageUrl(addOnUnavailableProductUiModel.productImageUrl)
            labelUnavailableProductName.text = addOnUnavailableProductUiModel.productName
        }
    }

}