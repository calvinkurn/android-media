package com.tokopedia.tokomart.common.base.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.searchcategory.presentation.customview.ShimmeringImageView
import com.tokopedia.tokomart.searchcategory.presentation.model.BannerDataView

/**
 * A Pager Adapter that supports infinite loop.
 * This is achieved by adding a fake item at both beginning and the last,
 * And then silently changing to the same, real item, thus looks like infinite.
 */
@Suppress("unused")
@SuppressLint("SyntheticAccessor")
class BannerComponentAdapter(itemList: List<BannerDataView>, private val bannerItemListener: BannerItemListener) : RecyclerView.Adapter<BannerChannelImageViewHolder>() {
    private var itemList: List<BannerDataView> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerChannelImageViewHolder {
        val layout = if (itemCount > 1) R.layout.layout_banner_component_item else R.layout.layout_banner_component_item_full
        return BannerChannelImageViewHolder(LayoutInflater.from(parent.context).inflate(layout, parent, false), bannerItemListener)
    }

    val listCount: Int
        get() = itemList.size

    fun setItemList(newItemList: List<BannerDataView>) {
        itemList = newItemList
        notifyDataSetChanged()
    }

    fun getItem(listPosition: Int): BannerDataView? {
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
    fun bind(item: BannerDataView) {
        itemView.findViewById<ShimmeringImageView>(R.id.image_banner_homepage).loadImage(item.imgUrl)
        itemView.findViewById<ShimmeringImageView>(R.id.image_banner_homepage).setOnClickListener { listener.onClick(adapterPosition) }
//        itemView.addOnImpressionListener(item) {
//            listener.onImpressed(adapterPosition)
//        }
    }
}

interface BannerItemListener {
    fun onClick(position: Int)
    fun onImpressed(position: Int)
}

data class BannerItemModel(val id: Int, val url: String): ImpressHolder()
