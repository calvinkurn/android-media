package com.tokopedia.play.ui.productsheet.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.R
import com.tokopedia.play.ui.productsheet.viewholder.ProductPlaceholderViewHolder
import com.tokopedia.play.view.type.PlayProductUiModel
import com.tokopedia.play.view.type.ProductPlaceholderUiModel

/**
 * Created by jegul on 13/03/20
 */
class ProductPlaceholderAdapterDelegate : TypedAdapterDelegate<ProductPlaceholderUiModel, PlayProductUiModel, ProductPlaceholderViewHolder>(R.layout.item_play_product_placeholder) {

    override fun onBindViewHolder(item: ProductPlaceholderUiModel, holder: ProductPlaceholderViewHolder) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ProductPlaceholderViewHolder {
        return ProductPlaceholderViewHolder(basicView)
    }
}