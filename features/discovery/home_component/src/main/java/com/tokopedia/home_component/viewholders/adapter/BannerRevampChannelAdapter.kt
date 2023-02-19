package com.tokopedia.home_component.viewholders.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.R
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.unifycomponents.ImageUnify

class BannerRevampChannelAdapter(
    private var itemList: List<BannerItemModel>,
    private val bannerItemListener: BannerItemListener,
    private val isUsingInfiniteScroll: Boolean = false
) : RecyclerView.Adapter<BannerRevampChannelImageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerRevampChannelImageViewHolder {
        return BannerRevampChannelImageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_banner_revamp_channel_item, parent, false), bannerItemListener)
    }

    override fun onBindViewHolder(holder: BannerRevampChannelImageViewHolder, position: Int) {
        val index = position % itemList.size
        if (position != -1 && index > 0) {
            holder.bind(itemList[index])
        }
    }

    override fun getItemCount(): Int {
        return if (isUsingInfiniteScroll && itemList.size > 1) Integer.MAX_VALUE else itemList.size
    }
}

class BannerRevampChannelImageViewHolder(itemView: View, val listener: BannerItemListener) : RecyclerView.ViewHolder(itemView) {
    @SuppressLint("ClickableViewAccessibility")
    fun bind(item: BannerItemModel) {
        itemView.findViewById<ImageUnify>(R.id.image_banner_revamp).setImageUrl(item.url)
        itemView.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    listener.onLongPress()
                }
                MotionEvent.ACTION_UP -> {
                    listener.onRelease()
                }
            }
            return@setOnTouchListener true
        }
        itemView.setOnClickListener { listener.onClick(layoutPosition) }
        itemView.addOnImpressionListener(item) {
            listener.onImpressed(layoutPosition)
        }
    }
}
