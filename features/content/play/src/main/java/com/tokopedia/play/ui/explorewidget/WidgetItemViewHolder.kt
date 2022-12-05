package com.tokopedia.play.ui.explorewidget

import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play.databinding.ViewTabMenuBinding
import com.tokopedia.play.databinding.ViewWidgetHolderBinding
import com.tokopedia.play.view.uimodel.TabMenuUiModel
import com.tokopedia.play.widget.sample.coordinator.PlayWidgetCoordinator
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel

/**
 * @author by astidhiyaa on 02/12/22
 */
class WidgetItemViewHolder {
    internal class Medium(
        private val widgetCoordinator: PlayWidgetCoordinator,
        binding: ViewWidgetHolderBinding
    ) : BaseViewHolder(binding.root) {
        val view = binding.widgetExplore

        init {
            widgetCoordinator.controlWidget(view)
        }

        fun bind(item: PlayWidgetUiModel) {
            widgetCoordinator.connect(view, item)
        }
    }

    internal class Chip(
        binding: ViewTabMenuBinding
    ) : BaseViewHolder(binding.root) {

        private val chipsAdapter by lazy(LazyThreadSafetyMode.NONE) {
            ChipsWidgetAdapter()
        }

        init {
            binding.root.adapter = chipsAdapter
        }

        fun bind(item: TabMenuUiModel) {
            chipsAdapter.setItemsAndAnimateChanges(item.items)
        }
    }
}
