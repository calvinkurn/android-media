package com.tokopedia.home_component.viewholders.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.R
import com.tokopedia.home_component.listener.VpsWidgetListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.util.recordCrashlytics
import com.tokopedia.home_component.viewholders.VpsWidgetComponentViewHolder
import com.tokopedia.home_component.visitable.VpsDataModel

/**
 * Created by frenzel
 */
class VpsWidgetAdapter(
        private val listener: VpsWidgetListener?,
        private val positionInWidget: Int,
        private val isCacheData: Boolean
) : RecyclerView.Adapter<VpsWidgetComponentViewHolder>() {
    companion object {
        private const val VPS_WIDGET_SIZE = 4
    }

    private lateinit var dataModel: VpsDataModel
    private var itemList: MutableList<ChannelGrid> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VpsWidgetComponentViewHolder {
        val layout = R.layout.layout_vps_widget_item
        return VpsWidgetComponentViewHolder(LayoutInflater.from(parent.context).inflate(layout, parent, false))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: VpsWidgetComponentViewHolder, position: Int) {
        try {
            holder.bind(itemList[position], positionInWidget, listener, dataModel.channelModel, isCacheData)
        } catch (e: Exception) {
            e.recordCrashlytics()
        }
    }

    fun addData(dataModel: VpsDataModel) {
        this.dataModel = dataModel
        itemList.clear()
        dataModel.channelModel.channelGrids.forEachIndexed { index, data ->
            if (index < VPS_WIDGET_SIZE) {
                itemList.add(data)
            }
        }
    }
}
