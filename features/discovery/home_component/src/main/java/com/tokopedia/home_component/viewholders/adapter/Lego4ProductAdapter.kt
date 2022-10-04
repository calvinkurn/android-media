package com.tokopedia.home_component.viewholders.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.R
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselProductCardDataModel
import com.tokopedia.home_component.viewholders.DeclutteredProductCardViewHolder
import com.tokopedia.home_component.visitable.DeclutteredProductCardDataModel
import com.tokopedia.home_component.visitable.VpsDataModel

/**
 * Created by frenzel
 */
class Lego4ProductAdapter(
    private val channels: ChannelModel,
    private val itemList: List<DeclutteredProductCardDataModel>
) : RecyclerView.Adapter<DeclutteredProductCardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeclutteredProductCardViewHolder {
        return DeclutteredProductCardViewHolder(
            LayoutInflater.from(parent.context).inflate(DeclutteredProductCardViewHolder.LAYOUT, parent, false),
            channels
        )
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: DeclutteredProductCardViewHolder, position: Int) {
        holder.bind(itemList[position])
    }
}
