package com.tokopedia.play.ui.explorewidget

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.databinding.ViewPlayExploreWidgetShimmeringBinding
import com.tokopedia.play.databinding.ViewPlayGridBinding
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel

/**
 * @author by astidhiyaa on 02/12/22
 */
class PlayExploreWidgetViewHolder {
    internal class Widget(
        private val binding: ViewPlayGridBinding,
        private val coordinator: PlayExploreWidgetCoordinator
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            coordinator.controlWidget(binding.root)
        }

        fun bind(item: PlayWidgetUiModel) {
            coordinator.connect(binding.root, item)
        }

        companion object {
            fun create(
                binding: ViewPlayGridBinding,
                coordinator: PlayExploreWidgetCoordinator
            ) = Widget(binding, coordinator)
        }
    }

    internal class Placeholder(
        private val binding: ViewPlayExploreWidgetShimmeringBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind() { //set Type
            binding.root.setData()
        }

        companion object {
            fun create(
                binding: ViewPlayExploreWidgetShimmeringBinding,
            ) = Placeholder(binding)
        }
    }
}
