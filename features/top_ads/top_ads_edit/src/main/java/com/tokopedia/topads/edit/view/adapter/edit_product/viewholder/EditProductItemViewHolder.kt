package com.tokopedia.topads.edit.view.adapter.edit_product.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.design.image.ImageLoader
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.view.adapter.edit_product.viewmodel.EditProductItemViewModel
import kotlinx.android.synthetic.main.topads_edit_layout_product_list_item_edit_product.view.*

/**
 * Created by Pika on 8/4/20.
 */
class EditProductItemViewHolder(val view: View, var actionChecked: ((pos: Int) -> Unit)?) : EditProductViewHolder<EditProductItemViewModel>(view) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_edit_layout_product_list_item_edit_product
    }

    override fun bind(item: EditProductItemViewModel) {
        item.let {
            ImageLoader.LoadImage(view.product_image, it.data.adDetailProduct.productImageURI)
            view.product_name.text = it.data.adDetailProduct.productName
            view.product_price.text = it.data.adPriceBidFmt
            view.delete.setOnClickListener {
                actionChecked?.invoke(adapterPosition)
            }
        }
    }

}