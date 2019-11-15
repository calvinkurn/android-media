package com.tokopedia.topads.view.adapter.product.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.design.image.ImageLoader
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.response.ResponseProductList
import com.tokopedia.topads.view.adapter.product.viewmodel.ProductItemViewModel
import kotlinx.android.synthetic.main.topads_create_layout_product_filter_list_item.view.*
import kotlinx.android.synthetic.main.topads_create_layout_product_list_item_product.view.*

/**
 * Author errysuprayogi on 11,November,2019
 */
class ProductItemViewHolder(val view: View, var actionChecked: (() -> Unit)?): ProductViewHolder<ProductItemViewModel>(view) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_create_layout_product_list_item_product
    }

    init {
        view?.setOnClickListener {
            it.checkBox.isChecked = !it.checkBox.isChecked
            actionChecked?.invoke()
        }
    }

    override fun bind(item: ProductItemViewModel) {
        item?.let {
            view.product_name.setText(it.data.productName)
            view.product_price.setText(it.data.productPrice)
            ImageLoader.LoadImage(view.product_image, it.data.productImage)
            view.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                item.isChecked = isChecked
            }
        }
    }

}