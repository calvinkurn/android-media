package com.tokopedia.play.ui.explorewidget.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.play.databinding.ItemPlayExploreCategoryCardBinding
import com.tokopedia.play.databinding.ItemPlayExploreCategoryShimmerBinding
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType

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

                ivCategoryVideo.loadImage(item.video.coverUrl)
                tvCategoryVideoTitle.text = item.title
                tvCategoryVideoCreator.text = item.partner.name
                ivCategoryCreator.loadImage(item.partner.thumbnail)
                tvCategoryView.text = item.totalView.totalViewFmt
                layoutLiveBadge.root.showWithCondition(item.channelType == PlayWidgetChannelType.Live)
                val currentPosition = absoluteAdapterPosition + 1
                tvPlayCategoryRank.text = "$currentPosition."
            }
        }

        interface Listener {
            fun onClicked(item: PlayWidgetChannelUiModel)
        }
    }

    internal class Shimmer(private val binding: ItemPlayExploreCategoryShimmerBinding) :
        RecyclerView.ViewHolder(binding.root) {}
}
