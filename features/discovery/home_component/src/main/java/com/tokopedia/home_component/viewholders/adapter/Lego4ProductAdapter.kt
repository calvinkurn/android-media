package com.tokopedia.home_component.viewholders.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.recordCrashlytics
import com.tokopedia.home_component.viewholders.LegoProductCardViewHolder
import com.tokopedia.home_component.visitable.LegoProductCardDataModel

/**
 * Created by frenzel
 */
class Lego4ProductAdapter(
    private val channels: ChannelModel,
    private val itemList: List<LegoProductCardDataModel>
) : RecyclerView.Adapter<LegoProductCardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LegoProductCardViewHolder {
        return LegoProductCardViewHolder(
            LayoutInflater.from(parent.context).inflate(LegoProductCardViewHolder.LAYOUT, parent, false),
            channels
        )
    }

    override fun onViewAttachedToWindow(holder: LegoProductCardViewHolder) {
        holder.onViewAttachedToWindow()
    }

    override fun onViewDetachedFromWindow(holder: LegoProductCardViewHolder) {
        holder.onViewDetachedFromWindow(holder.visiblePercentage)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: LegoProductCardViewHolder, position: Int) {
        try {
            holder.bind(itemList[position])
        } catch (e: Exception) {
            e.recordCrashlytics()
        }
    }
}
