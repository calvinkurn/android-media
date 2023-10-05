package com.tokopedia.home_component.viewholders.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.circular_view_pager.presentation.widgets.shimmeringImageView.ShimmeringImageView
import com.tokopedia.home_component.R
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.unifycomponents.CardUnify2

/**
 * A Pager Adapter that supports infinite loop.
 * This is achieved by adding a fake item at both beginning and the last,
 * And then silently changing to the same, real item, thus looks like infinite.
 */
@Suppress("unused")
@SuppressLint("SyntheticAccessor")
class BannerChannelAdapter(
    private var itemList: List<BannerItemModel>,
    private val bannerItemListener: BannerItemListener,
    private val cardInteraction: Boolean = false,
    private val isUsingInfiniteScroll: Boolean = false
) : RecyclerView.Adapter<BannerChannelImageViewHolder>() {
    private var imageRatio = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerChannelImageViewHolder {
        val viewHolder = BannerChannelImageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_banner_channel_item, parent, false), bannerItemListener)
        viewHolder.cardUnify.animateOnPress = if (cardInteraction) CardUnify2.ANIMATE_OVERLAY_BOUNCE else CardUnify2.ANIMATE_OVERLAY
        return viewHolder
    }

    fun setItemList(newItemList: List<BannerItemModel>) {
        itemList = newItemList
        notifyDataSetChanged()
    }

    fun setImageRatio(imageRatio: String) {
        this.imageRatio = imageRatio
    }

    override fun onBindViewHolder(holder: BannerChannelImageViewHolder, position: Int) {
        if (position != -1) {
            holder.bind(itemList[position % itemList.size], imageRatio)
        }
    }

    override fun getItemCount(): Int {
        return if (isUsingInfiniteScroll && itemList.size > 1) Integer.MAX_VALUE else itemList.size
    }
}

class BannerChannelImageViewHolder(itemView: View, val listener: BannerItemListener) : RecyclerView.ViewHolder(itemView) {
    companion object {
        private const val FPM_HOMEPAGE_BANNER = "banner_component_channel"
    }

    val cardUnify: CardUnify2 = itemView.findViewById(R.id.banner_card)

    @SuppressLint("ClickableViewAccessibility")
    fun bind(item: BannerItemModel, imageRatio: String = "") {
        itemView.findViewById<ShimmeringImageView>(R.id.image_banner_homepage).loadImage(item.url)
        itemView.setOnTouchListener { _, motionEvent ->
            cardUnify.onTouchEvent(motionEvent)
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
        if (imageRatio.isNotEmpty()) setRatio(imageRatio)
    }

    private fun setRatio(imageRatio: String) {
        val view = itemView.findViewById<ShimmeringImageView>(R.id.image_banner_homepage)
        val layoutParams = (view.layoutParams as ConstraintLayout.LayoutParams)
        layoutParams.dimensionRatio = imageRatio
        view.layoutParams = layoutParams
    }
}

interface BannerItemListener {
    fun onClick(position: Int)
    fun onImpressed(position: Int)
    fun onLongPress() { }
    fun onRelease() { }
    fun isDrag(): Boolean = false
    fun onTouchEvent(motionEvent: MotionEvent) { }
}

data class BannerItemModel(val id: Int, val url: String, val position: Int = 0) : ImpressHolder()
