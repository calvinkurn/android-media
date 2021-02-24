package com.tokopedia.home_component.viewholders.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.circular_view_pager.presentation.widgets.shimmeringImageView.ShimmeringImageView
import com.tokopedia.home_component.R
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.model.ImpressHolder

/**
 * A Pager Adapter that supports infinite loop.
 * This is achieved by adding a fake item at both beginning and the last,
 * And then silently changing to the same, real item, thus looks like infinite.
 */
@Suppress("unused")
@SuppressLint("SyntheticAccessor")
class BannerChannelAdapter(itemList: List<BannerItemModel>, private val bannerItemListener: BannerItemListener) : RecyclerView.Adapter<BannerChannelImageViewHolder>() {
    private var itemList: List<BannerItemModel> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerChannelImageViewHolder {
        val layout = if (itemCount > 1) R.layout.layout_banner_channel_item else R.layout.layout_banner_channel_item_full
        return BannerChannelImageViewHolder(LayoutInflater.from(parent.context).inflate(layout, parent, false), bannerItemListener)
    }

    val listCount: Int
        get() = itemList.size

    fun setItemList(newItemList: List<BannerItemModel>) {
        itemList = newItemList
        notifyDataSetChanged()
    }

    fun getItem(listPosition: Int): BannerItemModel? {
        return if (listPosition >= 0 && listPosition < itemList.size) {
            itemList[listPosition]
        } else {
            null
        }
    }

    override fun onBindViewHolder(holder: BannerChannelImageViewHolder, position: Int) {
        if(position != -1) {
            holder.bind(itemList[position])
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}

class BannerChannelImageViewHolder(itemView: View, val listener: BannerItemListener): RecyclerView.ViewHolder(itemView) {
    companion object{
        private const val FPM_HOMEPAGE_BANNER = "banner_component_channel"
    }
    fun bind(item: BannerItemModel) {
        itemView.findViewById<ShimmeringImageView>(R.id.image_banner_homepage).loadImage(item.url)
        itemView.findViewById<ShimmeringImageView>(R.id.image_banner_homepage).setOnClickListener { listener.onClick(adapterPosition) }
        itemView.addOnImpressionListener(item) {
            listener.onImpressed(adapterPosition)
        }
    }
}

interface BannerItemListener {
    fun onClick(position: Int)
    fun onImpressed(position: Int)
}

data class BannerItemModel(val id: Int, val url: String): ImpressHolder()
