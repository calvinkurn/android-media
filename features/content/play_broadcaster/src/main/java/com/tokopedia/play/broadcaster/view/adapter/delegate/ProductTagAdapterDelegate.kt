package com.tokopedia.play.broadcaster.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.broadcaster.ui.model.ProductContentUiModel
import com.tokopedia.play.broadcaster.ui.model.ProductUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.ProductTagViewHolder

/**
 * Created By : Jonathan Darwin on November 24, 2021
 */
class ProductTagAdapterDelegate: TypedAdapterDelegate<ProductContentUiModel, ProductUiModel, ProductTagViewHolder>(ProductTagViewHolder.LAYOUT) {
    override fun onBindViewHolder(item: ProductContentUiModel, holder: ProductTagViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ProductTagViewHolder {
        return ProductTagViewHolder(basicView)
    }
}