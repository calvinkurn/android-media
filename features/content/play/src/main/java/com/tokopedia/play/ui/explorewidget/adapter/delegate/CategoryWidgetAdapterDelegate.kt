package com.tokopedia.play.ui.explorewidget.adapter.delegate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.R
import com.tokopedia.play.databinding.ItemPlayExploreCategoryCardBinding
import com.tokopedia.play.databinding.ItemPlayExploreCategoryShimmerBinding
import com.tokopedia.play.ui.explorewidget.viewholder.CategoryWidgetViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetItemUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetShimmerUiModel

/**
 * @author by astidhiyaa on 23/05/23
 */
class CategoryWidgetAdapterDelegate private constructor() {
    internal class Card(private val listener: CategoryWidgetViewHolder.Item.Listener) :
        TypedAdapterDelegate<PlayWidgetChannelUiModel, PlayWidgetItemUiModel, CategoryWidgetViewHolder.Item>(
            R.layout.item_play_explore_category_card
        ) {
        override fun onBindViewHolder(
            item: PlayWidgetChannelUiModel,
            holder: CategoryWidgetViewHolder.Item
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): CategoryWidgetViewHolder.Item {
            val binding = ItemPlayExploreCategoryCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return CategoryWidgetViewHolder.Item(binding, listener)
        }
    }

    internal object Shimmer :
        TypedAdapterDelegate<PlayWidgetShimmerUiModel, PlayWidgetItemUiModel, CategoryWidgetViewHolder.Shimmer>(
            R.layout.item_play_explore_category_shimmer
        ) {
        override fun onBindViewHolder(
            item: PlayWidgetShimmerUiModel,
            holder: CategoryWidgetViewHolder.Shimmer
        ) {
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): CategoryWidgetViewHolder.Shimmer {
            val binding = ItemPlayExploreCategoryShimmerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return CategoryWidgetViewHolder.Shimmer(binding)
        }
    }
}
