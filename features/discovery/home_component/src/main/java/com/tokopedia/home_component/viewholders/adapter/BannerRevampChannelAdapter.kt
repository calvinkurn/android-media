package com.tokopedia.home_component.viewholders.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.R
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.unifycomponents.ImageUnify

class BannerRevampChannelAdapter(
    private var itemList: List<BannerItemModel>,
    private val bannerItemListener: BannerItemListener
) : RecyclerView.Adapter<BannerRevampChannelImageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerRevampChannelImageViewHolder {
        return BannerRevampChannelImageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_banner_revamp_channel_item, parent, false), bannerItemListener)
    }

    override fun onBindViewHolder(holder: BannerRevampChannelImageViewHolder, position: Int) {
        val index = position % itemList.size
        if (index != RecyclerView.NO_POSITION && itemList.size > index) {
            holder.bind(itemList[index])
        }
    }

    override fun getItemCount(): Int {
        return if (itemList.size > Int.ONE) Integer.MAX_VALUE else itemList.size
    }
}

class BannerRevampChannelImageViewHolder(itemView: View, val listener: BannerItemListener) : RecyclerView.ViewHolder(itemView) {
    @SuppressLint("ClickableViewAccessibility")
    fun bind(item: BannerItemModel) {
        val imageBanner = itemView.findViewById<ImageUnify>(R.id.image_banner_revamp)
        imageBanner.setImageUrl(item.url)
        imageBanner.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                    listener.onLongPress()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    if (!listener.isDrag()) {
                        listener.onRelease()
                    }
                }
            }
            return@setOnTouchListener false
        }
        imageBanner.setOnClickListener {
            listener.onClick(item.position)
        }
        imageBanner.addOnImpressionListener(item) {
            listener.onImpressed(item.position)
        }
    }
}
