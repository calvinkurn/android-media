package com.tokopedia.play.broadcaster.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.broadcaster.ui.model.ProductContentUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.ProductSelectableViewHolder

/**
 * Created by jegul on 11/06/20
 */
class PlayProductLiveAdapterDelegate : TypedAdapterDelegate<ProductContentUiModel, ProductContentUiModel, ProductSelectableViewHolder>(ProductSelectableViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: ProductContentUiModel, holder: ProductSelectableViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ProductSelectableViewHolder {
        return ProductSelectableViewHolder(basicView, showSelection = false)
    }
}