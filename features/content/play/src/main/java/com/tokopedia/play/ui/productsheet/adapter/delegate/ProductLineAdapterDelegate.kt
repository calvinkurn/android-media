package com.tokopedia.play.ui.productsheet.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.R
import com.tokopedia.play.ui.productsheet.viewholder.ProductLineViewHolder
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.ProductLineUiModel

/**
 * Created by jegul on 03/03/20
 */
class ProductLineAdapterDelegate(
        listener: ProductLineViewHolder.Listener
) : TypedAdapterDelegate<ProductLineUiModel, PlayProductUiModel, ProductLineViewHolder>(R.layout.item_play_product_line), ProductLineViewHolder.Listener by listener {

    override fun onBindViewHolder(item: ProductLineUiModel, holder: ProductLineViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ProductLineViewHolder {
        return ProductLineViewHolder(basicView, this)
    }
}