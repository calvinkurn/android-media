package com.tokopedia.play.ui.productsheet.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.R
import com.tokopedia.play.ui.productsheet.viewholder.ProductLineViewHolder
import com.tokopedia.play.view.type.ProductSheetProduct
import com.tokopedia.play.view.type.ProductSheetContent

/**
 * Created by jegul on 03/03/20
 */
class ProductLineAdapterDelegate : TypedAdapterDelegate<ProductSheetProduct, ProductSheetContent, ProductLineViewHolder>(R.layout.item_play_product_line) {

    override fun onBindViewHolder(item: ProductSheetProduct, holder: ProductLineViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ProductLineViewHolder {
        return ProductLineViewHolder(basicView)
    }
}