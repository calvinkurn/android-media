package com.tokopedia.play.ui.explorewidget

import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play.databinding.ViewChipShimmeringBinding
import com.tokopedia.play.databinding.ViewWidgetChipsBinding
import com.tokopedia.play.view.uimodel.ChipWidgetUiModel
import com.tokopedia.play_common.util.addImpressionListener
import com.tokopedia.unifycomponents.ChipsUnify

/**
 * @author by astidhiyaa on 04/12/22
 */
class ChipsViewHolder  {
    internal class Chips(private val binding: ViewWidgetChipsBinding, private val listener: Listener) : BaseViewHolder(binding.root){
        fun bind(item: ChipWidgetUiModel) {
            binding.root.chipText = item.text
            binding.root.chipType = if (item.isSelected) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL

            binding.root.setOnClickListener {
                listener.onChipsClicked(item)
            }
            binding.root.addImpressionListener(item.impressHolder) {
                listener.onChipsImpressed(item, adapterPosition)
            }
        }

        interface Listener {
            fun onChipsClicked(item: ChipWidgetUiModel)
            fun onChipsImpressed(item: ChipWidgetUiModel, position: Int)
        }
    }

    internal class Shimmering(binding: ViewChipShimmeringBinding) : BaseViewHolder(binding.root) {}
}
