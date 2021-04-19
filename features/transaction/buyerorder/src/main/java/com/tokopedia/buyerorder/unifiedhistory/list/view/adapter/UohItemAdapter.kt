package com.tokopedia.buyerorder.unifiedhistory.list.view.adapter

import android.util.Log
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
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.TYPE_TICKER
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohListOrder
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohTypeData
import com.tokopedia.buyerorder.unifiedhistory.list.view.adapter.viewholder.*
import com.tokopedia.buyerorder.unifiedhistory.list.view.fragment.UohListFragment
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import timber.log.Timber

/**
 * Created by fwidjaja on 22/07/20.
 */
class UohItemAdapter : RecyclerView.Adapter<UohItemAdapter.BaseViewHolder<*>>() {
    var listTypeData = mutableListOf<UohTypeData>()
    private var actionListener: ActionListener? = null

    companion object {
        const val LAYOUT_LOADER = 0
        const val LAYOUT_TICKER = 1
        const val LAYOUT_ORDER_LIST = 2
        const val LAYOUT_EMPTY_STATE = 3
        const val LAYOUT_RECOMMENDATION_TITLE = 4
        const val LAYOUT_RECOMMENDATION_LIST = 5
    }

    interface ActionListener {
        fun onKebabMenuClicked(order: UohListOrder.Data.UohOrders.Order, orderIndex: Int)
        fun onListItemClicked(order: UohListOrder.Data.UohOrders.Order, index: Int)
        fun onActionButtonClicked(order: UohListOrder.Data.UohOrders.Order, index: Int)
        fun onTickerDetailInfoClicked(url: String)
        fun onEmptyResultResetBtnClicked()
        fun trackViewOrderCard(order: UohListOrder.Data.UohOrders.Order, index: Int)
        fun onMulaiBelanjaBtnClicked()
        fun trackProductViewRecommendation(recommendationItem: RecommendationItem, index: Int)
        fun trackProductClickRecommendation(recommendationItem: RecommendationItem, index: Int)
        fun atcRecommendationItem(recommendationItem: RecommendationItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            LAYOUT_LOADER -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.uoh_loader_item, parent, false)
                UohLoaderItemViewHolder(view)
            }
            LAYOUT_TICKER -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.uoh_ticker_item, parent, false)
                UohTickerItemViewHolder(view, actionListener)
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
                UohRecommendationItemViewHolder(view, actionListener)
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
            TYPE_TICKER -> LAYOUT_TICKER
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
                holder.bind(element, holder.adapterPosition)
            }
            is UohTickerItemViewHolder -> {
                holder.bind(element, holder.adapterPosition)
            }
            is UohEmptyStateViewHolder-> {
                holder.bind(element, holder.adapterPosition)
            }
            is UohRecommendationTitleViewHolder-> {
                holder.bind(element, holder.adapterPosition)
            }
            is UohRecommendationItemViewHolder-> {
                holder.bind(element, holder.adapterPosition)
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
        try {
            listTypeData[index] = UohTypeData("", TYPE_LOADER)
            notifyItemChanged(index)
        } catch (ex: Exception) {
            ServerLogger.log(Priority.P2, "ORDER_HISTORY", mapOf("type" to "error_show", "err" to Log.getStackTraceString(ex)))
        }
    }

    fun updateDataAtIndex(index: Int, order: UohListOrder.Data.UohOrders.Order) {
        try {
            listTypeData[index] = UohTypeData(order, TYPE_ORDER_LIST)
            notifyItemChanged(index)
        } catch (ex: Exception) {
            ServerLogger.log(Priority.P2, "ORDER_HISTORY", mapOf("type" to "error_update", "err" to Log.getStackTraceString(ex)))
        }
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

    fun getRecommendationItemAtIndex(index: Int): RecommendationItem {
        return listTypeData[index].dataObject as RecommendationItem
    }
}