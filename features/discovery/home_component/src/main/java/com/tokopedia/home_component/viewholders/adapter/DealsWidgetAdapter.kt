package com.tokopedia.home_component.viewholders.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.R
import com.tokopedia.home_component.listener.VpsWidgetListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.util.recordCrashlytics
import com.tokopedia.home_component.viewholders.DealsWidgetItemViewHolder
import com.tokopedia.home_component.visitable.DealsDataModel

/**
 * Created by frenzel
 */
class DealsWidgetAdapter(
        private val listener: VpsWidgetListener?,
        private val positionInWidget: Int,
        private val isCacheData: Boolean
) : RecyclerView.Adapter<DealsWidgetItemViewHolder>() {
    companion object {
        private const val DEALS_WIDGET_SIZE = 4
    }

    private var dataModel: DealsDataModel? = null
    private var itemList: MutableList<ChannelGrid> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealsWidgetItemViewHolder {
        return DealsWidgetItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_deals_widget_item, parent, false))
    }

    override fun getItemCount(): Int {
        return DEALS_WIDGET_SIZE
    }

    override fun onBindViewHolder(holder: DealsWidgetItemViewHolder, position: Int) {
        try{
            dataModel?.let {
                holder.bind(itemList[position], positionInWidget, listener, it.channelModel, isCacheData)
            }
        } catch (e: Exception) {
            e.recordCrashlytics()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addData(dataModel: DealsDataModel) {
        this.dataModel = dataModel
        itemList.clear()
        itemList.addAll(dataModel.channelModel.channelGrids.take(DEALS_WIDGET_SIZE))
        notifyDataSetChanged()
    }
}
