package com.tokopedia.home_component.viewholders.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.listener.Lego4AutoBannerListener
import com.tokopedia.home_component.util.Lego4AutoTabletConfiguration
import com.tokopedia.home_component.viewholders.Lego4AutoComponentViewHolder
import com.tokopedia.home_component.visitable.Lego4AutoDataModel
import com.tokopedia.home_component.visitable.Lego4AutoItem

/**
 * @author by yoasfs on 28/07/20
 */
class Lego4AutoBannerAdapter(
        private val listener: Lego4AutoBannerListener?,
        private val positionInWidget: Int,
        private val isCacheData: Boolean
) : RecyclerView.Adapter<Lego4AutoComponentViewHolder>() {
    companion object {
        private const val LEGO_4_BANNER_SIZE = 4
    }

    private lateinit var dataModel: Lego4AutoDataModel
    private var itemList: MutableList<Lego4AutoItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Lego4AutoComponentViewHolder {
        val layout = Lego4AutoTabletConfiguration.getLayout(parent.context)
        return Lego4AutoComponentViewHolder(LayoutInflater.from(parent.context).inflate(layout, parent, false))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: Lego4AutoComponentViewHolder, position: Int) {
        holder.bind(itemList[position], positionInWidget, listener, dataModel.channelModel, isCacheData)
    }

    fun addData(dataModel: Lego4AutoDataModel) {
        this.dataModel = dataModel
        itemList.clear()
        dataModel.channelModel.channelGrids.forEachIndexed { index, data ->
            if (index < LEGO_4_BANNER_SIZE) {
                itemList.add(Lego4AutoItem(grid = data))
            }
        }
    }
}