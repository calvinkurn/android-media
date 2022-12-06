package com.tokopedia.play.ui.explorewidget

import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play.databinding.ViewTabMenuBinding
import com.tokopedia.play.view.uimodel.ChipWidgetUiModel
import com.tokopedia.play.view.uimodel.TabMenuUiModel

/**
 * @author by astidhiyaa on 02/12/22
 */
class WidgetItemViewHolder {

    internal class Chip(
        binding: ViewTabMenuBinding,
        listener: Listener
    ) : BaseViewHolder(binding.root) {

        private val chipsListener = object : ChipsViewHolder.Listener {
            override fun onChipsClicked(item: ChipWidgetUiModel) {
                listener.onChipsClicked(item)
            }
        }

        private val chipsAdapter by lazy(LazyThreadSafetyMode.NONE) {
            ChipsWidgetAdapter(chipsListener)
        }

        init {
            binding.root.adapter = chipsAdapter
        }

        fun bind(item: TabMenuUiModel) {
            chipsAdapter.setItemsAndAnimateChanges(item.items)
        }

        interface Listener {
            fun onChipsClicked(item: ChipWidgetUiModel)
        }
    }
}
