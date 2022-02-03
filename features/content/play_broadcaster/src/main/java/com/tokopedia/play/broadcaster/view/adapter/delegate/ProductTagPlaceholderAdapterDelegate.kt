package com.tokopedia.play.broadcaster.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.broadcaster.ui.model.ProductLoadingUiModel
import com.tokopedia.play.broadcaster.ui.model.ProductUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.ProductTagPlaceholderViewHolder

/**
 * Created By : Jonathan Darwin on November 25, 2021
 */
class ProductTagPlaceholderAdapterDelegate: TypedAdapterDelegate<ProductLoadingUiModel, ProductUiModel, ProductTagPlaceholderViewHolder>(ProductTagPlaceholderViewHolder.LAYOUT) {
    override fun onBindViewHolder(
        item: ProductLoadingUiModel,
        holder: ProductTagPlaceholderViewHolder
    ) { }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): ProductTagPlaceholderViewHolder {
        return ProductTagPlaceholderViewHolder(basicView)
    }
}