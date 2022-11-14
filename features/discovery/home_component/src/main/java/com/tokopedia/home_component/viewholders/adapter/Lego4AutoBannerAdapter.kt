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
        private val isCacheData: Boolean,
        private val dataModel: Lego4AutoDataModel
) : RecyclerView.Adapter<Lego4AutoComponentViewHolder>() {
    companion object {
        private const val LEGO_4_BANNER_SIZE = 4
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Lego4AutoComponentViewHolder {
        val layout = Lego4AutoTabletConfiguration.getLayout(parent.context, dataModel.channelModel.channelConfig.borderStyle)
        return Lego4AutoComponentViewHolder(LayoutInflater.from(parent.context).inflate(layout, parent, false))
    }

    override fun getItemCount(): Int {
        return LEGO_4_BANNER_SIZE
    }

    override fun onBindViewHolder(holder: Lego4AutoComponentViewHolder, position: Int) {
        val grid = dataModel.channelModel.channelGrids[position]
        holder.bind(Lego4AutoItem(grid = grid),
            positionInWidget, listener, dataModel.channelModel, isCacheData)
    }
}
