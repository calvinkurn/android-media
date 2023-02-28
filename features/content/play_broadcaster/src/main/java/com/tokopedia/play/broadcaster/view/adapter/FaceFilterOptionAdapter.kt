package com.tokopedia.play.broadcaster.view.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.broadcaster.ui.model.FaceFilterUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.FaceFilterOptionViewHolder
import com.tokopedia.play.broadcaster.view.adapter.delegate.FaceFilterOptionAdapterDelegate

/**
 * Created By : Jonathan Darwin on February 28, 2023
 */
class FaceFilterOptionAdapter(
    listener: FaceFilterOptionViewHolder.Listener,
) : BaseDiffUtilAdapter<FaceFilterUiModel>() {

    init {
        delegatesManager
            .addDelegate(FaceFilterOptionAdapterDelegate(listener))
    }

    override fun areItemsTheSame(oldItem: FaceFilterUiModel, newItem: FaceFilterUiModel): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(
        oldItem: FaceFilterUiModel,
        newItem: FaceFilterUiModel
    ): Boolean {
        return oldItem == newItem
    }
}
