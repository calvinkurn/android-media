package com.tokopedia.product.detail.view.viewholder.gwp.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.product.detail.view.viewholder.gwp.model.GWPWidgetUiModel

/**
 * Created by yovi.putra on 27/07/23"
 * Project name: android-tokopedia-core
 **/

class GWPCardAdapter :
    ListAdapter<GWPWidgetUiModel.Card, GWPCardViewHolder<GWPWidgetUiModel.Card>>(DIFF_ITEMS) {

    companion object {
        private val DIFF_ITEMS = object : DiffUtil.ItemCallback<GWPWidgetUiModel.Card>() {
            override fun areItemsTheSame(
                oldItem: GWPWidgetUiModel.Card,
                newItem: GWPWidgetUiModel.Card
            ): Boolean = oldItem === newItem

            override fun areContentsTheSame(
                oldItem: GWPWidgetUiModel.Card,
                newItem: GWPWidgetUiModel.Card
            ): Boolean = oldItem == newItem
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GWPCardViewHolder<GWPWidgetUiModel.Card> {
        val viewHolder = when (viewType) {
            GWPCardOfProductViewHolder.ID -> GWPCardOfProductViewHolder.create(parent)
            GWPCardOfShowMoreViewHolder.ID -> GWPCardOfShowMoreViewHolder.create(parent)
            else -> {
                throw IllegalAccessException("viewType unresolved...")
            }
        }

        return viewHolder as GWPCardViewHolder<GWPWidgetUiModel.Card>
    }

    override fun getItemViewType(position: Int): Int {
        val product = currentList.getOrNull(position) ?: return -1
        return product.id
    }

    override fun onBindViewHolder(
        holder: GWPCardViewHolder<GWPWidgetUiModel.Card>,
        position: Int
    ) {
        val product = currentList.getOrNull(position) ?: return
        holder.bind(data = product)
    }
}
