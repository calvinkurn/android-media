package com.tokopedia.play.ui.explorewidget

import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play.databinding.ViewWidgetHolderBinding
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
        val view = binding.root

        init {
            widgetCoordinator.controlWidget(view)
        }

        fun bind(item: PlayWidgetUiModel) {
            widgetCoordinator.connect(view, item)
        }
    }
}
