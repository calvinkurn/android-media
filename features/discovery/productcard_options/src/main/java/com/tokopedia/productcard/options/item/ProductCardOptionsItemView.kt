package com.tokopedia.productcard.options.item

import android.content.Context
import android.view.View
import com.tokopedia.productcard.options.R
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.product_card_options_item_layout.view.*

internal class ProductCardOptionsItemView(context: Context) : BaseCustomView(context) {

    constructor(context: Context, optionItem: ProductCardOptionsItemModel): this(context) {
        View.inflate(context, R.layout.product_card_options_item_layout, this)
        setOption(optionItem)
    }

    private fun setOption(optionItem: ProductCardOptionsItemModel) {
        productCardOptionsItemTitle?.text = optionItem.title

        productCardOptionsItemTitle?.setOnClickListener {
            optionItem.onClick()
        }
    }
}
