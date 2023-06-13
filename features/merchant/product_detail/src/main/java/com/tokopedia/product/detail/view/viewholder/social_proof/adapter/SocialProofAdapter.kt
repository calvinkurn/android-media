package com.tokopedia.product.detail.view.viewholder.social_proof.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.social_proof.SocialProofUiModel
import com.tokopedia.product.detail.view.viewholder.social_proof.adapter.view_holder.SocialProofTypeViewHolder

class SocialProofAdapter(
    private val factory: SocialProofAdapterFactory
) : ListAdapter<SocialProofUiModel, SocialProofTypeViewHolder>(DIFF_ITEM) {

    companion object {
        private val DIFF_ITEM = object : DiffUtil.ItemCallback<SocialProofUiModel>() {
            override fun areItemsTheSame(
                oldItem: SocialProofUiModel,
                newItem: SocialProofUiModel
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: SocialProofUiModel,
                newItem: SocialProofUiModel
            ): Boolean = oldItem.type == newItem.type &&
                oldItem.title == newItem.title &&
                oldItem.subtitle == newItem.subtitle &&
                oldItem.icon == newItem.icon &&
                oldItem.appLink == newItem.appLink &&
                oldItem.identifier == newItem.identifier
        }
    }

    private var trackData: ComponentTrackDataModel? = null

    fun submitList(items: List<SocialProofUiModel>, trackDataModel: ComponentTrackDataModel) {
        trackData = trackDataModel
        submitList(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SocialProofTypeViewHolder {
        return factory.onCreateViewHolder(parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        val type = currentList.getOrNull(position)?.type ?: SocialProofUiModel.Type.Text
        return factory.getItemViewType(type)
    }

    override fun onBindViewHolder(holder: SocialProofTypeViewHolder, position: Int) {
        val data = currentList.getOrNull(position) ?: return
        holder.bind(data, trackData)
    }
}
