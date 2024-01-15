package com.tokopedia.shareexperience.ui.adapter.viewholder.chip

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shareexperience.databinding.ShareexperienceChipItemBinding
import com.tokopedia.shareexperience.ui.listener.ShareExChipsListener
import com.tokopedia.shareexperience.ui.model.chip.ShareExChipUiModel
import com.tokopedia.unifycomponents.ChipsUnify.Companion.TYPE_NORMAL
import com.tokopedia.unifycomponents.ChipsUnify.Companion.TYPE_SELECTED
import com.tokopedia.utils.view.binding.viewBinding

class ShareExChipViewHolder(
    itemView: View,
    private val chipsListener: ShareExChipsListener
) : RecyclerView.ViewHolder(itemView) {

    private val binding: ShareexperienceChipItemBinding? by viewBinding()
    private var element: ShareExChipUiModel? = null

    init {
        binding?.shareexChip?.setOnClickListener {
            chipsListener.onChipClicked(bindingAdapterPosition, element?.title?: "")
        }
    }

    fun bind(element: ShareExChipUiModel) {
        this.element = element
        binding?.shareexChip?.chip_text?.text = element.title
        val chipType = if (element.isSelected) {
            TYPE_SELECTED
        } else {
            TYPE_NORMAL
        }
        binding?.shareexChip?.chipType = chipType
    }
}
