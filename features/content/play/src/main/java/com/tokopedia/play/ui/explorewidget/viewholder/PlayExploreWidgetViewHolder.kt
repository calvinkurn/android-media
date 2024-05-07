package com.tokopedia.play.ui.explorewidget.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.databinding.ViewPlayGridBinding
import com.tokopedia.play.ui.explorewidget.PlayExploreWidgetCoordinator
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel

/**
 * @author by astidhiyaa on 02/12/22
 */
class PlayExploreWidgetViewHolder {
    internal class Widget(
        binding: ViewPlayGridBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        private val card = binding.root
        fun bind(item: PlayWidgetChannelUiModel) {
            card.setModel(item)
        }

        companion object {
            fun create(
                binding: ViewPlayGridBinding,
            ) = Widget(binding)
        }
    }
}
