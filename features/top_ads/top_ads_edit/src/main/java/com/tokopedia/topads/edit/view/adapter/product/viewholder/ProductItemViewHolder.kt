package com.tokopedia.topads.edit.view.adapter.product.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.view.adapter.product.viewmodel.ProductItemViewModel
import kotlinx.android.synthetic.main.topads_edit_layout_product_list_item_product.view.*


class ProductItemViewHolder(val view: View, var actionChecked: (() -> Unit)?) : ProductViewHolder<ProductItemViewModel>(view) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_edit_layout_product_list_item_product
    }

    init {
        view.setOnClickListener {
            it.checkBox.isChecked = !it.checkBox.isChecked
            actionChecked?.invoke()
        }
    }

    override fun bind(item: ProductItemViewModel) {
        item.let {
            view.product_name.text = it.data.productName
            view.product_price.text = it.data.productPrice
            view.checkBox.setOnCheckedChangeListener(null)
            view.checkBox.isChecked = item.isChecked
            view.product_image.setImageUrl(it.data.productImage)
            view.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                item.isChecked = isChecked
                actionChecked?.invoke()
            }
        }
    }

}