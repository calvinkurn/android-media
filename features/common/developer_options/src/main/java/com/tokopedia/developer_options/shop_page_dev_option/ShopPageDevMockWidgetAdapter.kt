package com.tokopedia.developer_options.shop_page_dev_option

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.developer_options.R
import com.tokopedia.unifyprinciples.Typography

class ShopPageDevMockWidgetAdapter(
    private val listenerShopPageMockWidgetViewHolder: ShopPageMockWidgetViewHolder.Listener? = null
) : RecyclerView.Adapter<ShopPageDevMockWidgetAdapter.ShopPageMockWidgetViewHolder>() {

    private var listShopPageMockWidget: List<ShopPageMockWidgetModel> = listOf()
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

    @SuppressLint("NotifyDataSetChanged")
    fun setListShopPageMockWidget(listShopPageMockWidget: List<ShopPageMockWidgetModel>) {
        this.listShopPageMockWidget = listShopPageMockWidget
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateIsFestivity(isFestivity: Boolean) {
        listShopPageMockWidget.forEach { it.updateIsFestivity(isFestivity) }
        notifyDataSetChanged()
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
                    optionText += " {isFestivity: true}"
                }
                text = optionText
                listener?.let {
                    setOnClickListener { listener.onMockWidgetItemClick(shopPageMockWidgetModel) }
                }
            }
        }
    }
}
