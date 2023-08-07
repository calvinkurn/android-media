package com.tokopedia.play.ui.explorewidget.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.model.ImpressHolder
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

        private val impressHolder = ImpressHolder()

        fun bind(item: PlayWidgetChannelUiModel) {
            with(binding) {
                root.setOnClickListener {
                    listener.onClicked(item, absoluteAdapterPosition)
                }

                root.addOnImpressionListener(impressHolder) {
                    listener.onImpressed(item, absoluteAdapterPosition)
                }

                ivPlayCategoryVideo.loadImage(item.video.coverUrl)
                tvCategoryVideoTitle.text = item.title
                tvCategoryVideoCreator.text = item.partner.name
                ivCategoryCreator.loadImage(item.partner.avatarUrl)
                tvCategoryView.text = item.totalView.totalViewFmt
                layoutLiveBadge.root.showWithCondition(item.channelType == PlayWidgetChannelType.Live)
                val currentPosition = absoluteAdapterPosition + 1
                tvPlayCategoryRank.text = buildString {
                    append(currentPosition)
                    append(".")
                }
            }
        }

        interface Listener {
            fun onClicked(item: PlayWidgetChannelUiModel, position: Int)
            fun onImpressed(item: PlayWidgetChannelUiModel, position: Int)
        }
    }

    internal class Shimmer(binding: ItemPlayExploreCategoryShimmerBinding) :
        RecyclerView.ViewHolder(binding.root) {}
}
