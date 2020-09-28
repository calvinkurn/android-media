package com.tokopedia.play.broadcaster.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.broadcaster.ui.model.CarouselCoverUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.PlayCoverProductViewHolder

class PlayCoverProductAdapterDelegate(
        private val listener: PlayCoverProductViewHolder.Listener
) : TypedAdapterDelegate<CarouselCoverUiModel.Product, CarouselCoverUiModel, PlayCoverProductViewHolder>(PlayCoverProductViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: CarouselCoverUiModel.Product, holder: PlayCoverProductViewHolder) {
        holder.bind(item.model)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): PlayCoverProductViewHolder {
        return PlayCoverProductViewHolder(basicView, listener)
    }
}