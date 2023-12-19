package com.tokopedia.shareexperience.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.shareexperience.databinding.ShareexperienceChipItemBinding
import com.tokopedia.shareexperience.domain.model.property.ShareExChipModel
import com.tokopedia.shareexperience.ui.adapter.diffutil.ShareExChipsItemCallback
import com.tokopedia.shareexperience.ui.adapter.viewholder.chip.ShareExChipViewHolder

class ShareExChipsAdapter: ListAdapter<ShareExChipModel, ShareExChipViewHolder>(
    ShareExChipsItemCallback()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShareExChipViewHolder {
        val binding = ShareexperienceChipItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ShareExChipViewHolder(binding.root)
    }

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: ShareExChipViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    fun updateData(newList: List<ShareExChipModel>) {
        this.submitList(newList)
    }
}
