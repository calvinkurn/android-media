package com.tokopedia.topads.edit.view.adapter.product.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.view.adapter.product.viewmodel.ProductItemViewModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography

class ProductItemViewHolder(val view: View, var actionChecked: (() -> Unit)?) :
    ProductViewHolder<ProductItemViewModel>(view) {

    private var productImage: ImageUnify = view.findViewById(R.id.product_image)
    private var productName: Typography = view.findViewById(R.id.product_name)
    private var productPrice: Typography = view.findViewById(R.id.product_price)
    private var checkBox: CheckboxUnify = view.findViewById(R.id.checkBox)

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_edit_layout_product_list_item_product
    }

    init {
        view.setOnClickListener {
            checkBox.isChecked = !checkBox.isChecked
            actionChecked?.invoke()
        }
    }

    override fun bind(item: ProductItemViewModel) {
        item.let {
            productName.text = it.data.productName
            productPrice.text = it.data.productPrice
            checkBox.setOnCheckedChangeListener(null)
            checkBox.isChecked = item.isChecked
            productImage.setImageUrl(it.data.productImage)
            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                item.isChecked = isChecked
                actionChecked?.invoke()
            }
        }
    }

}