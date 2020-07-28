package com.tokopedia.home_component.viewholders.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.R
import com.tokopedia.home_component.listener.Lego4AutoBannerListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.loadImage
import com.tokopedia.home_component.util.setGradientBackground
import com.tokopedia.home_component.visitable.Lego4AutoDataModel
import com.tokopedia.home_component.visitable.Lego4AutoItem
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.unifyprinciples.Typography

/**
 * @author by yoasfs on 28/07/20
 */
class Lego4AutoBannerAdapter(
        val listener: Lego4AutoBannerListener?,
        val positionInWidget: Int
) : RecyclerView.Adapter<Lego4AutoBannerAdapter.Holder>() {

    private lateinit var dataModel: Lego4AutoDataModel
    private var itemList: MutableList<Lego4AutoItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.global_component_lego_banner_auto, parent, false))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(itemList[position], positionInWidget, listener, dataModel.channelModel)
    }

    fun addData(dataModel: Lego4AutoDataModel) {
        this.dataModel = dataModel
        itemList.clear()
        dataModel.channelModel.channelGrids.forEach {
            itemList.add(Lego4AutoItem(grid = it))
        }
    }

    class Holder(v: View): RecyclerView.ViewHolder(v) {
        private val itemLayout: ConstraintLayout = v.findViewById(R.id.item_lego_auto)
        private val itemImage: ImageView = v.findViewById(R.id.item_image)
        private val itemName: Typography = v.findViewById(R.id.item_name)
        private val itemDesc: Typography = v.findViewById(R.id.item_desc)
        private val itemPrice: Typography = v.findViewById(R.id.item_price)


        fun bind(item: Lego4AutoItem, parentPosition: Int, listener: Lego4AutoBannerListener?, channelModel: ChannelModel) {
            itemName.text = item.grid.name
            itemImage.loadImage(item.grid.imageUrl)
            itemDesc.text = item.grid.benefit.type
            itemPrice.text = item.grid.benefit.value
            itemLayout.setGradientBackground(arrayListOf(item.grid.backColor))

            itemLayout.addOnImpressionListener(item.impressHolder){
                listener?.onLegoItemImpressed(channelModel, item.grid, adapterPosition, parentPosition)
            }
            itemLayout.setOnClickListener {
                listener?.onLegoItemClicked(channelModel, item.grid, adapterPosition, parentPosition)
            }
        }
    }
}