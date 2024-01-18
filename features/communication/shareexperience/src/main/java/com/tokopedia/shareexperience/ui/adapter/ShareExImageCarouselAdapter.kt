package com.tokopedia.shareexperience.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.shareexperience.databinding.ShareexperienceImageItemBinding
import com.tokopedia.shareexperience.ui.adapter.diffutil.ShareExImageCarouselItemCallback
import com.tokopedia.shareexperience.ui.adapter.viewholder.image.ShareExImageViewHolder
import com.tokopedia.shareexperience.ui.listener.ShareExCarouselImageListener
import com.tokopedia.shareexperience.ui.model.image.ShareExImageUiModel

class ShareExImageCarouselAdapter(
    private val listener: ShareExCarouselImageListener
): ListAdapter<ShareExImageUiModel, ShareExImageViewHolder>(
    ShareExImageCarouselItemCallback()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShareExImageViewHolder {
        val binding = ShareexperienceImageItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ShareExImageViewHolder(binding.root, listener)
    }

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: ShareExImageViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    fun updateData(newList: List<ShareExImageUiModel>) {
        this.submitList(newList)
    }
}
