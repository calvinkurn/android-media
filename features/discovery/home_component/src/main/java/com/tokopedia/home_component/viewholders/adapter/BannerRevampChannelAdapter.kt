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
import com.tokopedia.home_component.util.loadImage
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.setImage

/**
 * A Pager Adapter that supports infinite loop.
 * This is achieved by adding a fake item at both beginning and the last,
 * And then silently changing to the same, real item, thus looks like infinite.
 */
@Suppress("unused")
@SuppressLint("SyntheticAccessor")
class BannerRevampChannelAdapter(
    private var itemList: List<BannerItemModel>,
    private val bannerItemListener: BannerItemListener,
    private val cardInteraction: Boolean = false,
    private val isUsingInfiniteScroll: Boolean = false
) : RecyclerView.Adapter<BannerRevampChannelImageViewHolder>() {
    private var imageRatio = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerRevampChannelImageViewHolder {
        val viewHolder = BannerRevampChannelImageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_banner_revamp_channel_item, parent, false), bannerItemListener)
        return viewHolder
    }

    fun setItemList(newItemList: List<BannerItemModel>) {
        itemList = newItemList
        notifyDataSetChanged()
    }

    fun setImageRatio(imageRatio: String) {
        this.imageRatio = imageRatio
    }

    override fun onBindViewHolder(holder: BannerRevampChannelImageViewHolder, position: Int) {
        if (position != -1) {
            holder.bind(itemList[position % itemList.size], imageRatio)
        }
    }

    override fun getItemCount(): Int {
        return if (isUsingInfiniteScroll && itemList.size > 1) Integer.MAX_VALUE else itemList.size
    }
}

class BannerRevampChannelImageViewHolder(itemView: View, val listener: BannerItemListener) : RecyclerView.ViewHolder(itemView) {
    companion object {
        private const val FPM_HOMEPAGE_BANNER = "banner_component_channel"
    }

    @SuppressLint("ClickableViewAccessibility")
    fun bind(item: BannerItemModel, imageRatio: String = "") {
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
        if (imageRatio.isNotEmpty()) setRatio(imageRatio)
    }

    private fun setRatio(imageRatio: String) {
        val view = itemView.findViewById<ImageUnify>(R.id.image_banner_revamp)
        val layoutParams = (view.layoutParams as ConstraintLayout.LayoutParams)
        layoutParams.dimensionRatio = imageRatio
        view.layoutParams = layoutParams
    }
}

//interface BannerItemListener {
//    fun onClick(position: Int)
//    fun onImpressed(position: Int)
//    fun onLongPress() { }
//    fun onRelease() { }
//}
//
//data class BannerItemModel(val id: Int, val url: String) : ImpressHolder()
