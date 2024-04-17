package com.tokopedia.home_component.viewholders.shorten.viewholder.item

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.viewholders.shorten.ContainerMultiTwoSquareListener
import com.tokopedia.home_component.viewholders.shorten.internal.ShortenVisitable
import com.tokopedia.home_component.viewholders.shorten.internal.ShortenStaticSquaresDiffUtil
import com.tokopedia.home_component.visitable.shorten.DealsAndMissionWidgetUiModel
import com.tokopedia.home_component.visitable.shorten.ItemDealsWidgetUiModel
import com.tokopedia.home_component.visitable.shorten.ItemMissionWidgetUiModel

class PartialItemWidgetAdapter(
    private val type: DealsAndMissionWidgetUiModel.Type,
    private val listener: ContainerMultiTwoSquareListener
) : ListAdapter<ShortenVisitable, RecyclerView.ViewHolder>(
    ShortenStaticSquaresDiffUtil()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            DealsAndMissionWidgetUiModel.Type.Deals.value -> ItemDealsWidgetViewHolder.create(parent, listener)
            DealsAndMissionWidgetUiModel.Type.Mission.value -> ItemMissionWidgetViewHolder.create(parent, listener)
            else -> super.createViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        try {
            if (holder is ItemDealsWidgetViewHolder) {
                holder.bind(getItem(position) as ItemDealsWidgetUiModel)
            } else if (holder is ItemMissionWidgetViewHolder) {
                holder.bind(getItem(position) as ItemMissionWidgetUiModel)
            }
        } catch (_: Throwable) {}
    }

    override fun getItemViewType(position: Int): Int {
        return type.value
    }
}
