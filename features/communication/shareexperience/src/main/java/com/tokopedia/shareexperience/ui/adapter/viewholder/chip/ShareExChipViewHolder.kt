package com.tokopedia.shareexperience.ui.adapter.viewholder.chip

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shareexperience.databinding.ShareexperienceChipItemBinding
import com.tokopedia.shareexperience.domain.model.property.ShareExChipModel
import com.tokopedia.utils.view.binding.viewBinding

class ShareExChipViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val binding: ShareexperienceChipItemBinding? by viewBinding()
    fun bind(element: ShareExChipModel) {
        binding?.shareexChip?.chip_text?.text = element.title
        //TODO: another configuration
    }
}
