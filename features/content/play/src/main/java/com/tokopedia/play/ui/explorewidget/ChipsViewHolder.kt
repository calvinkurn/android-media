package com.tokopedia.play.ui.explorewidget

import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play.databinding.ViewWidgetChipsBinding
import com.tokopedia.play.view.uimodel.ChipWidgetUiModel
import com.tokopedia.unifycomponents.ChipsUnify

/**
 * @author by astidhiyaa on 04/12/22
 */
class ChipsViewHolder(private val binding: ViewWidgetChipsBinding) : BaseViewHolder(binding.root) {
    fun bind(item: ChipWidgetUiModel) {
        binding.root.chipText = item.text
        binding.root.chipType = if (item.isSelected) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL
    }
}
