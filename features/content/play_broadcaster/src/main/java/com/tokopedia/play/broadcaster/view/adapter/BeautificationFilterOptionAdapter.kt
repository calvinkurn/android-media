package com.tokopedia.play.broadcaster.view.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.broadcaster.ui.model.beautification.FaceFilterUiModel
import com.tokopedia.play.broadcaster.ui.model.beautification.PresetFilterUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.BeautificationFilterOptionViewHolder
import com.tokopedia.play.broadcaster.view.adapter.delegate.BeautificationFilterOptionAdapterDelegate

/**
 * Created By : Jonathan Darwin on February 28, 2023
 */
class BeautificationFilterOptionAdapter(
    faceFilterListener: BeautificationFilterOptionViewHolder.FaceFilter.Listener,
    presetListener: BeautificationFilterOptionViewHolder.Preset.Listener,
) : BaseDiffUtilAdapter<BeautificationFilterOptionAdapter.Model>() {

    init {
        delegatesManager
            .addDelegate(BeautificationFilterOptionAdapterDelegate.FaceFilter(faceFilterListener))
            .addDelegate(BeautificationFilterOptionAdapterDelegate.Preset(presetListener))
    }

    override fun areItemsTheSame(oldItem: Model, newItem: Model): Boolean {
        return if(oldItem is Model.FaceFilter && newItem is Model.FaceFilter)
            oldItem.data.id == newItem.data.id
        else if(oldItem is Model.Preset && newItem is Model.Preset)
            oldItem.data.id == newItem.data.id
        else oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Model, newItem: Model): Boolean {
        return oldItem == newItem
    }

    sealed interface Model {
        data class FaceFilter(val data: FaceFilterUiModel) : Model
        data class Preset(val data: PresetFilterUiModel) : Model
    }
}
