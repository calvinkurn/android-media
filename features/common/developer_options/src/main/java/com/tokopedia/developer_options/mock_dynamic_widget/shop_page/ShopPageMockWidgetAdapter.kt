package com.tokopedia.developer_options.mock_dynamic_widget.shop_page

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.developer_options.R
import com.tokopedia.unifyprinciples.Typography

class ShopPageMockWidgetAdapter(
    private val listenerShopPageMockWidgetViewHolder: ShopPageMockWidgetViewHolder.Listener? = null
) : RecyclerView.Adapter<ShopPageMockWidgetAdapter.ShopPageMockWidgetViewHolder>() {

    private var listShopPageMockWidget: MutableList<ShopPageMockWidgetModel> = mutableListOf()
    private var originalListShopPageMockWidget: List<ShopPageMockWidgetModel> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopPageMockWidgetViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_shop_page_mock_widget_item, parent, false)
        return ShopPageMockWidgetViewHolder(v, listenerShopPageMockWidgetViewHolder)
    }

    override fun onBindViewHolder(holder: ShopPageMockWidgetViewHolder, position: Int) {
        listShopPageMockWidget.getOrNull(position)?.let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int {
        return listShopPageMockWidget.size
    }

    fun setListShopPageMockWidget(listShopPageMockWidget: List<ShopPageMockWidgetModel>) {
        val newItems = listShopPageMockWidget
        this.originalListShopPageMockWidget = listShopPageMockWidget.toList()
        submitList(newItems)
    }

    fun updateIsFestivity(isFestivity: Boolean) {
        val newItems = mutableListOf<ShopPageMockWidgetModel>().apply {
            addAll(
                listShopPageMockWidget.map {
                    it.copy().apply {
                        updateIsFestivity(isFestivity)
                    }
                }
            )
        }
        submitList(newItems)
    }

    fun filterWidgetByName(widgetName: String) {
        val newData = if (widgetName.isEmpty()) {
            originalListShopPageMockWidget
        } else {
            originalListShopPageMockWidget.filter { it.getWidgetName().contains(widgetName, ignoreCase = true) }
        }
        submitList(newData)
    }

    private fun submitList(items: List<ShopPageMockWidgetModel>) {
        val diffUtilCallback = RvDiffUtilCallback(listShopPageMockWidget, items)
        val result = DiffUtil.calculateDiff(diffUtilCallback)
        listShopPageMockWidget.clear()
        listShopPageMockWidget.addAll(items)
        result.dispatchUpdatesTo(this)
    }

    fun addMockWidgetModel(shopPageMockWidgetModel: ShopPageMockWidgetModel) {
        submitList(listShopPageMockWidget.toMutableList().apply {
            add(shopPageMockWidgetModel)
        })
    }

    fun clear() {
        submitList(listShopPageMockWidget.toMutableList().apply {
            clear()
        })
    }

    fun getData(): List<ShopPageMockWidgetModel> {
        return listShopPageMockWidget.toList()
    }

    class RvDiffUtilCallback(
        private val oldItems: List<ShopPageMockWidgetModel>,
        private val newItems: List<ShopPageMockWidgetModel>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldItems.size
        }

        override fun getNewListSize(): Int {
            return newItems.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems.getOrNull(oldItemPosition)
            val newItem = newItems.getOrNull(newItemPosition)
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems.getOrNull(oldItemPosition)
            val newItem = newItems.getOrNull(newItemPosition)
            return oldItem == newItem
        }
    }

    class ShopPageMockWidgetViewHolder(itemView: View, private val listener: Listener? = null) : RecyclerView.ViewHolder(itemView) {
        interface Listener {
            fun onMockWidgetItemClick(shopPageMockWidgetModel: ShopPageMockWidgetModel)
        }

        fun bind(shopPageMockWidgetModel: ShopPageMockWidgetModel) {
            configShopWidgetName(shopPageMockWidgetModel)
        }

        private fun configShopWidgetName(shopPageMockWidgetModel: ShopPageMockWidgetModel) {
            itemView.findViewById<Typography>(R.id.text_shop_widget_name).apply {
                var optionText = shopPageMockWidgetModel.getWidgetName()
                if (shopPageMockWidgetModel.isFestivity()) {
                    optionText += " (festivity)"
                }
                text = optionText
                listener?.let {
                    setOnClickListener { listener.onMockWidgetItemClick(shopPageMockWidgetModel) }
                }
            }
        }
    }
}
