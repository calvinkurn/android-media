package com.tokopedia.play.broadcaster.view.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.broadcaster.ui.model.CarouselCoverUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.PlayCoverCameraViewHolder
import com.tokopedia.play.broadcaster.ui.viewholder.PlayCoverProductViewHolder
import com.tokopedia.play.broadcaster.view.adapter.delegate.PlayCoverCameraAdapterDelegate
import com.tokopedia.play.broadcaster.view.adapter.delegate.PlayCoverProductAdapterDelegate

/**
 * @author by furqan on 07/06/2020
 */
class PlayCoverProductAdapter(
        coverProductListener: PlayCoverProductViewHolder.Listener,
        coverCameraListener: PlayCoverCameraViewHolder.Listener
) : BaseDiffUtilAdapter<CarouselCoverUiModel>() {

    init {
        delegatesManager
                .addDelegate(PlayCoverProductAdapterDelegate(coverProductListener))
                .addDelegate(PlayCoverCameraAdapterDelegate(coverCameraListener))
    }

    override fun areItemsTheSame(oldItem: CarouselCoverUiModel, newItem: CarouselCoverUiModel): Boolean {
        return if (oldItem is CarouselCoverUiModel.Product && newItem is CarouselCoverUiModel.Product) oldItem.model.id == newItem.model.id
        else oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: CarouselCoverUiModel, newItem: CarouselCoverUiModel): Boolean {
        return oldItem == newItem
    }
}