package com.tokopedia.addongifting.view.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.addongifting.R
import com.tokopedia.addongifting.databinding.ItemProductBinding
import com.tokopedia.addongifting.view.AddOnActionListener
import com.tokopedia.addongifting.view.uimodel.ProductUiModel
import timber.log.Timber

class ProductViewHolder(private val viewBinding: ItemProductBinding, private val listener: AddOnActionListener)
    : AbstractViewHolder<ProductUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_product
    }

    override fun bind(element: ProductUiModel) {
        Timber.d("Here")
    }

}