package com.tokopedia.home_component.viewholders.shorten.viewholder.item

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.viewholders.shorten.ContainerMultiTwoSquareListener
import com.tokopedia.home_component.viewholders.shorten.internal.ShortenVisitable
import com.tokopedia.home_component.viewholders.shorten.internal.ShortenStaticSquaresDiffUtil
import com.tokopedia.home_component.visitable.shorten.MultiTwoSquareWidgetUiModel.Type as ItemTwoSquareType
import com.tokopedia.home_component.visitable.shorten.ItemThumbnailWidgetUiModel
import com.tokopedia.home_component.visitable.shorten.ItemMissionWidgetUiModel
import com.tokopedia.home_component.visitable.shorten.ItemProductWidgetUiModel

class ItemContentCardAdapter(
    private val type: ItemTwoSquareType,
    private val listener: ContainerMultiTwoSquareListener
) : ListAdapter<ShortenVisitable, RecyclerView.ViewHolder>(
    ShortenStaticSquaresDiffUtil()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            ItemTwoSquareType.Mission.value -> ItemMissionWidgetViewHolder.create(parent, listener)
            ItemTwoSquareType.Thumbnail.value -> ItemThumbnailWidgetViewHolder.create(parent, listener)
            ItemTwoSquareType.Product.value -> ItemProductWidgetViewHolder.create(parent, listener)
            else -> super.createViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        try {
            when (holder) {
                is ItemMissionWidgetViewHolder -> holder.bind(item as ItemMissionWidgetUiModel)
                is ItemThumbnailWidgetViewHolder -> holder.bind(item as ItemThumbnailWidgetUiModel)
                is ItemProductWidgetViewHolder -> holder.bind(item as ItemProductWidgetUiModel)
            }
        } catch (_: Throwable) {}
    }

    override fun getItemViewType(position: Int): Int {
        return type.value
    }
}
