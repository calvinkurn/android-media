package com.tokopedia.product.detail.view.viewholder.social_proof.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.social_proof.SocialProofData
import com.tokopedia.product.detail.view.viewholder.social_proof.adapter.view_holder.SocialProofTypeViewHolder

class SocialProofAdapter(
    private val delegate: SocialProofAdapterDelegate
) : ListAdapter<SocialProofData, SocialProofTypeViewHolder>(DIFF_ITEM) {

    companion object {
        private val DIFF_ITEM = object : DiffUtil.ItemCallback<SocialProofData>() {
            override fun areItemsTheSame(
                oldItem: SocialProofData,
                newItem: SocialProofData
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: SocialProofData,
                newItem: SocialProofData
            ): Boolean = oldItem.socialProofType == newItem.socialProofType &&
                oldItem.title == newItem.title &&
                oldItem.subtitle == newItem.subtitle &&
                oldItem.icon == newItem.icon &&
                oldItem.appLink == newItem.appLink &&
                oldItem.socialProofId == newItem.socialProofId
        }
    }

    private var trackData: ComponentTrackDataModel? = null

    fun submitList(items: List<SocialProofData>, trackDataModel: ComponentTrackDataModel) {
        trackData = trackDataModel
        submitList(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SocialProofTypeViewHolder {
        return delegate.onCreateViewHolder(parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        val type = currentList.getOrNull(position)?.socialProofType.orEmpty()
        return delegate.getItemViewType(type)
    }

    override fun onBindViewHolder(holder: SocialProofTypeViewHolder, position: Int) {
        val data = currentList.getOrNull(position) ?: return
        holder.bind(data, trackData)
    }
}
