package com.tokopedia.play.broadcaster.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.broadcaster.ui.viewholder.ProductSelectableViewHolder
import com.tokopedia.play.broadcaster.view.uimodel.ProductUiModel

/**
 * Created by jegul on 27/05/20
 */
class ProductSelectableAdapterDelegate(
        private val listener: ProductSelectableViewHolder.Listener
) : TypedAdapterDelegate<ProductUiModel, Any, ProductSelectableViewHolder>(ProductSelectableViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: ProductUiModel, holder: ProductSelectableViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ProductSelectableViewHolder {
        return ProductSelectableViewHolder(basicView, listener)
    }
}