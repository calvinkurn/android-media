package com.tokopedia.play.broadcaster.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.broadcaster.ui.model.ProductContentUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.PlayCoverProductViewHolder

class PlayCoverProductAdapterDelegate(
        private val listener: PlayCoverProductViewHolder.Listener
) : TypedAdapterDelegate<ProductContentUiModel, ProductContentUiModel, PlayCoverProductViewHolder>(PlayCoverProductViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: ProductContentUiModel, holder: PlayCoverProductViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): PlayCoverProductViewHolder {
        return PlayCoverProductViewHolder(basicView, listener)
    }
}