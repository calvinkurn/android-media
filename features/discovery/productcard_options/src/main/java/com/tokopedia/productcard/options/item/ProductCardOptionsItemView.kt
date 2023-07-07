package com.tokopedia.productcard.options.item

import android.content.Context
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.productcard.options.ProductCardOptionsListener
import com.tokopedia.productcard.options.R
import com.tokopedia.productcard.options.databinding.ProductCardOptionsItemLayoutBinding
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

internal class ProductCardOptionsItemView(context: Context) : BaseCustomView(context) {

    constructor(context: Context, optionItem: ProductCardOptionsItemModel): this(context) {
        View.inflate(context, R.layout.product_card_options_item_layout, this)
        setOption(optionItem)
    }

    private val productCardOptionsItemTitle: Typography? by lazy(LazyThreadSafetyMode.NONE) {
        findViewById(R.id.productCardOptionsItemTitle)
    }

    private fun setOption(optionItem: ProductCardOptionsItemModel) {
        productCardOptionsItemTitle?.text = optionItem.title

        productCardOptionsItemTitle?.setOnClickListener {
            optionItem.onClick()
        }
    }
}

internal class ProductCardOptionsItemViewHolder(
    view: View,
    private val listener: ProductCardOptionsListener,
): AbstractViewHolder<ProductCardOptionsItemModel>(view) {

    companion object {
        val LAYOUT = R.layout.product_card_options_item_layout
    }
    private var binding: ProductCardOptionsItemLayoutBinding? by viewBinding()

    override fun bind(element: ProductCardOptionsItemModel) {
        val productCardOptionsItemTitle = binding?.productCardOptionsItemTitle ?: return
        productCardOptionsItemTitle.text = element.title
        productCardOptionsItemTitle.setOnClickListener {
            element.onClick()
        }
        listener.onProductCardOptionsItemImpressed(element, bindingAdapterPosition)
    }
}
