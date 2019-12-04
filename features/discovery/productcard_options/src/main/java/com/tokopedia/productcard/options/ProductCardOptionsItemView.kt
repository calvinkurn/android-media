package com.tokopedia.productcard.options

import android.content.Context
import android.view.View
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.product_card_options_item_layout.view.*

internal class ProductCardOptionsItemView(context: Context) : BaseCustomView(context) {

    init {
        View.inflate(context, R.layout.product_card_options_item_layout, this)
    }

    fun setOption(optionItem: ProductCardOptionsItemModel, onClick: (optionItem: ProductCardOptionsItemModel, view: View) -> Unit) {
        productCardOptionsItemTitle?.text = optionItem.title

        productCardOptionsItemTitle?.setOnClickListener {
            onClick(optionItem, it)
        }
    }
}