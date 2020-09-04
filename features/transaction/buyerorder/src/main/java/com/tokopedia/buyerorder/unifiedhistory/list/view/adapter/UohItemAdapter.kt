package com.tokopedia.buyerorder.unifiedhistory.list.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.TYPE_EMPTY
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.TYPE_LOADER
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.TYPE_ORDER_LIST
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.TYPE_RECOMMENDATION_TITLE
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.TYPE_RECOMMENDATION_ITEM
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohListOrder
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohTypeData
import com.tokopedia.buyerorder.unifiedhistory.list.view.adapter.viewholder.*
import com.tokopedia.buyerorder.unifiedhistory.list.view.fragment.UohListFragment

/**
 * Created by fwidjaja on 22/07/20.
 */
class UohItemAdapter : RecyclerView.Adapter<UohItemAdapter.BaseViewHolder<*>>() {
    var listTypeData = mutableListOf<UohTypeData>()
    private var actionListener: ActionListener? = null

    companion object {
        const val LAYOUT_LOADER = 0
        const val LAYOUT_ORDER_LIST = 1
        const val LAYOUT_EMPTY_STATE = 2
        const val LAYOUT_RECOMMENDATION_TITLE = 3
        const val LAYOUT_RECOMMENDATION_LIST = 4
    }

    interface ActionListener {
        fun onKebabMenuClicked(order: UohListOrder.Data.UohOrders.Order)
        fun onListItemClicked(detailUrl: UohListOrder.Data.UohOrders.Order.Metadata.DetailUrl)
        fun onActionButtonClicked(button: UohListOrder.Data.UohOrders.Order.Metadata.Button,
                                  index: Int,
                                  orderUUID: String,
                                  verticalId: String,
                                  listProducts: String)
        fun onEmptyResultResetBtnClicked()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            LAYOUT_LOADER -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.uoh_loader_item, parent, false)
                UohLoaderItemViewHolder(view)
            }
            LAYOUT_ORDER_LIST -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.uoh_list_item, parent, false)
                UohOrderListViewHolder(view, actionListener)
            }
            LAYOUT_EMPTY_STATE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.uoh_empty_state, parent, false)
                UohEmptyStateViewHolder(view, actionListener)
            }
            LAYOUT_RECOMMENDATION_TITLE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.uoh_recommendation_title, parent, false)
                UohRecommendationTitleViewHolder(view)
            }
            LAYOUT_RECOMMENDATION_LIST -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.uoh_recommendation_item, parent, false)
                UohRecommendationItemViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return listTypeData.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (listTypeData[position].typeLayout) {
            TYPE_LOADER -> LAYOUT_LOADER
            TYPE_ORDER_LIST -> LAYOUT_ORDER_LIST
            TYPE_EMPTY -> LAYOUT_EMPTY_STATE
            TYPE_RECOMMENDATION_TITLE -> LAYOUT_RECOMMENDATION_TITLE
            TYPE_RECOMMENDATION_ITEM -> LAYOUT_RECOMMENDATION_LIST
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T, position: Int)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = listTypeData[position]
        when (holder) {
            is UohOrderListViewHolder-> {
                holder.bind(element, position)
            }
            is UohEmptyStateViewHolder-> {
                holder.bind(element, position)
            }
            is UohRecommendationTitleViewHolder-> {
                holder.bind(element, position)
            }
            is UohRecommendationItemViewHolder-> {
                holder.bind(element, position)
            }
        }
    }

    fun showLoader() {
        listTypeData.clear()
        for (x in 0 until 5) {
            listTypeData.add(UohTypeData("", TYPE_LOADER))
        }
        notifyDataSetChanged()
    }

    fun getDataAtIndex(index: Int): UohListOrder.Data.UohOrders.Order {
        return listTypeData[index].dataObject as UohListOrder.Data.UohOrders.Order
    }

    fun showLoaderAtIndex(index: Int) {
        listTypeData.removeAt(index)
        listTypeData.add(index, UohTypeData("", TYPE_LOADER))
        notifyDataSetChanged()
    }

    fun updateDataAtIndex(index: Int, order: UohListOrder.Data.UohOrders.Order) {
        listTypeData.removeAt(index)
        listTypeData.add(index, UohTypeData(order, TYPE_ORDER_LIST))
        notifyDataSetChanged()
    }

    fun addList(list: List<UohTypeData>) {
        listTypeData.clear()
        listTypeData.addAll(list)
        notifyDataSetChanged()
    }

    fun appendList(list: List<UohTypeData>) {
        listTypeData.addAll(list)
        notifyDataSetChanged()
    }

    fun setActionListener(fragment: UohListFragment) {
        this.actionListener = fragment
    }
}