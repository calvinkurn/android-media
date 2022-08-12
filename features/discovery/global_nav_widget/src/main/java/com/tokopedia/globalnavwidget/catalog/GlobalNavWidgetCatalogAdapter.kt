package com.tokopedia.globalnavwidget.catalog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.globalnavwidget.GlobalNavWidgetListener
import com.tokopedia.globalnavwidget.GlobalNavWidgetModel

internal class GlobalNavWidgetCatalogAdapter(
    private val globalNavWidgetModel: GlobalNavWidgetModel,
    private val globalNavWidgetListener: GlobalNavWidgetListener
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val itemList = mutableListOf<GlobalNavWidgetModel.Item>()

    fun setItemList(itemList: List<GlobalNavWidgetModel.Item>) {
        this.itemList.clear()
        this.itemList.addAll(itemList)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)

        return viewType.doWhenViewType({
            GlobalNavWidgetCatalogViewHolder(view)
        }, {
            GlobalNavWidgetCatalogViewAllCardViewHolder(
                view,
                globalNavWidgetListener,
                globalNavWidgetModel
            )
        })
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemViewType.doWhenViewType({
            if (holder is GlobalNavWidgetCatalogViewHolder)
                holder.bind(itemList[position])
        }, {
            if (holder is GlobalNavWidgetCatalogViewAllCardViewHolder)
                holder.bind()
        })
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLastPosition(position))
            GlobalNavWidgetCatalogViewAllCardViewHolder.LAYOUT
        else
            GlobalNavWidgetCatalogViewHolder.LAYOUT
    }

    private fun isLastPosition(position: Int): Boolean = position == itemCount - 1

    override fun getItemCount(): Int = itemList.size + 1

    private fun <T> Int.doWhenViewType(catalogViewHolder: () -> T, viewAllCardViewHolder: () -> T): T {
        return when (this) {
            GlobalNavWidgetCatalogViewAllCardViewHolder.LAYOUT -> viewAllCardViewHolder()
            else -> catalogViewHolder()
        }
    }
}