package com.tokopedia.unifyorderhistory.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.unifyorderhistory.data.model.PmsNotification
import com.tokopedia.unifyorderhistory.data.model.UohEmptyState
import com.tokopedia.unifyorderhistory.data.model.UohListOrder
import com.tokopedia.unifyorderhistory.data.model.UohTypeData
import com.tokopedia.unifyorderhistory.databinding.UohEmptyStateBinding
import com.tokopedia.unifyorderhistory.databinding.UohListItemBinding
import com.tokopedia.unifyorderhistory.databinding.UohLoaderItemBinding
import com.tokopedia.unifyorderhistory.databinding.UohLoaderPmsButtonItemBinding
import com.tokopedia.unifyorderhistory.databinding.UohPmsButtonItemBinding
import com.tokopedia.unifyorderhistory.databinding.UohRecommendationItemBinding
import com.tokopedia.unifyorderhistory.databinding.UohRecommendationTitleBinding
import com.tokopedia.unifyorderhistory.databinding.UohTdnBannerLayoutBinding
import com.tokopedia.unifyorderhistory.databinding.UohTickerItemBinding
import com.tokopedia.unifyorderhistory.util.UohConsts.TDN_BANNER
import com.tokopedia.unifyorderhistory.util.UohConsts.TYPE_EMPTY
import com.tokopedia.unifyorderhistory.util.UohConsts.TYPE_LOADER
import com.tokopedia.unifyorderhistory.util.UohConsts.TYPE_LOADER_PMS_BUTTON
import com.tokopedia.unifyorderhistory.util.UohConsts.TYPE_ORDER_LIST
import com.tokopedia.unifyorderhistory.util.UohConsts.TYPE_PMS_BUTTON
import com.tokopedia.unifyorderhistory.util.UohConsts.TYPE_RECOMMENDATION_ITEM
import com.tokopedia.unifyorderhistory.util.UohConsts.TYPE_RECOMMENDATION_TITLE
import com.tokopedia.unifyorderhistory.util.UohConsts.TYPE_TICKER
import com.tokopedia.unifyorderhistory.view.adapter.viewholder.UohEmptyStateViewHolder
import com.tokopedia.unifyorderhistory.view.adapter.viewholder.UohLoaderItemViewHolder
import com.tokopedia.unifyorderhistory.view.adapter.viewholder.UohLoaderPmsButtonItemViewHolder
import com.tokopedia.unifyorderhistory.view.adapter.viewholder.UohOrderListViewHolder
import com.tokopedia.unifyorderhistory.view.adapter.viewholder.UohPmsButtonViewHolder
import com.tokopedia.unifyorderhistory.view.adapter.viewholder.UohRecommendationItemViewHolder
import com.tokopedia.unifyorderhistory.view.adapter.viewholder.UohRecommendationTitleViewHolder
import com.tokopedia.unifyorderhistory.view.adapter.viewholder.UohTdnBannerViewHolder
import com.tokopedia.unifyorderhistory.view.adapter.viewholder.UohTickerItemViewHolder
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
        const val LAYOUT_BANNER = 6
        const val LAYOUT_PMS_BUTTON = 7
        const val LAYOUT_LOADER_PMS_BUTTON = 8
    }

    interface ActionListener {
        fun onKebabMenuClicked(order: UohListOrder.UohOrders.Order, orderIndex: Int)
        fun onListItemClicked(order: UohListOrder.UohOrders.Order, index: Int)
        fun onActionButtonClicked(order: UohListOrder.UohOrders.Order, index: Int, buttonIndex: Int)
        fun onTickerDetailInfoClicked(url: String)
        fun onEmptyResultResetBtnClicked()
        fun trackViewOrderCard(order: UohListOrder.UohOrders.Order, index: Int)
        fun onMulaiBelanjaBtnClicked()
        fun trackProductViewRecommendation(recommendationItem: RecommendationItem, index: Int)
        fun trackProductClickRecommendation(recommendationItem: RecommendationItem, index: Int)
        fun atcRecommendationItem(recommendationItem: RecommendationItem)
        fun onImpressionPmsButton()
        fun onPmsButtonClicked()
        fun onReviewRatingClicked(index: Int, order: UohListOrder.UohOrders.Order, appLink: String)
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
            LAYOUT_BANNER -> {
                val binding = UohTdnBannerLayoutBinding.inflate(LayoutInflater.from(parent.context), null, false)
                UohTdnBannerViewHolder(binding)
            }
            LAYOUT_PMS_BUTTON -> {
                val binding = UohPmsButtonItemBinding.inflate(LayoutInflater.from(parent.context), null, false)
                UohPmsButtonViewHolder(binding, actionListener)
            }
            LAYOUT_LOADER_PMS_BUTTON -> {
                val binding = UohLoaderPmsButtonItemBinding.inflate(LayoutInflater.from(parent.context), null, false)
                UohLoaderPmsButtonItemViewHolder(binding)
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
            TDN_BANNER -> LAYOUT_BANNER
            TYPE_PMS_BUTTON -> LAYOUT_PMS_BUTTON
            TYPE_LOADER_PMS_BUTTON -> LAYOUT_LOADER_PMS_BUTTON
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
            is UohTdnBannerViewHolder -> {
                holder.bind(element)
            }
            is UohPmsButtonViewHolder -> {
                holder.bind(element)
            }
            is UohLoaderPmsButtonItemViewHolder -> {
                holder.bind(element)
            }
        }
    }

    fun showLoader() {
        listTypeData.clear()
        listTypeData.add(UohTypeData("", TYPE_LOADER_PMS_BUTTON))
        for (x in 0 until 5) {
            listTypeData.add(UohTypeData("", TYPE_LOADER))
        }
        notifyDataSetChanged()
    }

    fun showLoaderAtIndex(index: Int) {
        try {
            listTypeData[index] = UohTypeData("", TYPE_LOADER)
            notifyItemChanged(index)
        } catch (ex: Exception) {
            ServerLogger.log(Priority.P2, "ORDER_HISTORY", mapOf("type" to "error_show", "err" to Log.getStackTraceString(ex)))
        }
    }

    fun updateDataAtIndex(index: Int, order: UohListOrder.UohOrders.Order) {
        try {
            listTypeData[index] = UohTypeData(order, TYPE_ORDER_LIST)
            notifyItemChanged(index)
        } catch (ex: Exception) {
            ServerLogger.log(Priority.P2, "ORDER_HISTORY", mapOf("type" to "error_update", "err" to Log.getStackTraceString(ex)))
        }
    }

    fun resetReviewRatingWidgetAtIndex(orderIdNeedUpdated: String) {
        try {
            val index = listTypeData.indexOfFirst {
                it.dataObject is UohListOrder.UohOrders.Order && it.dataObject.orderUUID == orderIdNeedUpdated
            }
            if (index != RecyclerView.NO_POSITION) notifyItemChanged(index)
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

    fun appendPmsButton(pmsButtonData: UohTypeData, onItemInserted: (position: Int) -> Unit) {
        var targetIndex = RecyclerView.NO_POSITION
        var targetData: UohTypeData? = null
        loop@ for ((index, data) in listTypeData.withIndex()) {
            if (data.dataObject is PmsNotification) {
                targetData = data
                targetIndex = index
                break@loop
            } else if (data.dataObject is UohListOrder.UohOrders.Order || data.dataObject is UohEmptyState) {
                targetIndex = index
                break@loop
            }
        }

        // Only add or update pms button item when uoh list already loaded
        if (targetIndex != RecyclerView.NO_POSITION) {
            if (targetData != null) {
                // Update existing pms button item
                listTypeData[targetIndex] = pmsButtonData
                notifyItemChanged(targetIndex)
            } else {
                // insert pms button item
                listTypeData.add(targetIndex, pmsButtonData)
                notifyItemInserted(targetIndex)
                onItemInserted.invoke(targetIndex)
            }
        }
    }

    fun removePmsButton() {
        var targetIndex = RecyclerView.NO_POSITION
        loop@ for ((index, data) in listTypeData.withIndex()) {
            if (data.dataObject is PmsNotification) {
                targetIndex = index
                break@loop
            } else if (data.dataObject is UohListOrder.UohOrders.Order || data.dataObject is UohEmptyState) {
                break@loop
            }
        }

        if (targetIndex != RecyclerView.NO_POSITION) {
            listTypeData.removeAt(targetIndex)
            notifyItemRemoved(targetIndex)
        }
    }
}
