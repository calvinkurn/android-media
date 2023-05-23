package com.tokopedia.play.ui.explorewidget.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.play.databinding.ItemPlayExploreCategoryCardBinding
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel

/**
 * @author by astidhiyaa on 23/05/23
 */
internal class CategoryWidgetViewHolder {
    internal class Item(
        private val binding: ItemPlayExploreCategoryCardBinding,
        private val listener: Listener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PlayWidgetChannelUiModel) {
            with(binding) {
                root.setOnClickListener {
                    listener.onClicked(item)
                }

                ivCategoryView.loadImage(item.video.coverUrl)
                tvCategoryVideoTitle.text = item.title
                tvCategoryVideoCreator.text = item.partner.name //TODO() check partner image
                ivCategoryCreator.loadImage(item.video.coverUrl)
                tvCategoryView.text = item.totalView.totalViewFmt
            }
        }

        interface Listener {
            fun onClicked(item: PlayWidgetChannelUiModel)
        }
    }

    internal class Shimmer(private val binding: ItemPlayExploreCategoryCardBinding) :
        RecyclerView.ViewHolder(binding.root) {} //TODO() temp layout
}
