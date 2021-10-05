package com.tokopedia.unifyorderhistory.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifyorderhistory.util.UohConsts.TYPE_EMPTY
import com.tokopedia.unifyorderhistory.util.UohConsts.TYPE_LOADER
import com.tokopedia.unifyorderhistory.util.UohConsts.TYPE_ORDER_LIST
import com.tokopedia.unifyorderhistory.util.UohConsts.TYPE_RECOMMENDATION_TITLE
import com.tokopedia.unifyorderhistory.util.UohConsts.TYPE_RECOMMENDATION_ITEM
import com.tokopedia.unifyorderhistory.util.UohConsts.TYPE_TICKER
import com.tokopedia.unifyorderhistory.data.model.UohListOrder
import com.tokopedia.unifyorderhistory.data.model.UohTypeData
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.unifyorderhistory.R
import com.tokopedia.unifyorderhistory.databinding.*
import com.tokopedia.unifyorderhistory.view.adapter.viewholder.*
import com.tokopedia.unifyorderhistory.view.fragment.UohListFragment

/**
 * Created by fwidjaja on 22/07/20.
 */
class UohItemAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LAYOUT_LOADER -> {
                val binding = UohLoaderItemBinding.inflate(LayoutInflater.from(parent.context), null, false)
                UohLoaderItemViewHolder(binding)
            }
            LAYOUT_TICKER -> {
                val binding = UohTickerItemBinding.inflate(LayoutInflater.from(parent.context), null, false)
                UohTickerItemViewHolder(binding, actionListener)
            }
            LAYOUT_ORDER_LIST -> {
                val binding = UohListItemBinding.inflate(LayoutInflater.from(parent.context), null, false)
                UohOrderListViewHolder(binding, actionListener)
            }
            LAYOUT_EMPTY_STATE -> {
                val binding = UohEmptyStateBinding.inflate(LayoutInflater.from(parent.context), null, false)
                UohEmptyStateViewHolder(binding, actionListener)
            }
            LAYOUT_RECOMMENDATION_TITLE -> {
                val binding = UohRecommendationTitleBinding.inflate(LayoutInflater.from(parent.context), null, false)
                UohRecommendationTitleViewHolder(binding)
            }
            LAYOUT_RECOMMENDATION_LIST -> {
                val binding = UohRecommendationItemBinding.inflate(LayoutInflater.from(parent.context), null, false)
                UohRecommendationItemViewHolder(binding, actionListener)
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



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val element = listTypeData[position]
        when (holder) {
            is UohLoaderItemViewHolder -> {
                holder.bind(element)
            }
            is UohOrderListViewHolder -> {
                holder.bind(element, holder.adapterPosition)
            }
            is UohTickerItemViewHolder -> {
                holder.bind(element)
            }
            is UohEmptyStateViewHolder -> {
                holder.bind(element)
            }
            is UohRecommendationTitleViewHolder -> {
                holder.bind(element)
            }
            is UohRecommendationItemViewHolder -> {
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