package com.tokopedia.home_component.widget.atf_banner

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.util.loadImage
import com.tokopedia.home_component.viewholders.adapter.BannerItemListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.home_component.R as home_componentR

/**
 * Created by frenzel
 */
class BannerRevampItemViewHolder(itemView: View, val listener: BannerItemListener) :
    AbstractViewHolder<BannerRevampItemModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = home_componentR.layout.layout_banner_revamp_channel_item
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun bind(item: BannerRevampItemModel) {
        val imageBanner = itemView.findViewById<AppCompatImageView>(R.id.image_banner_revamp)
        imageBanner.loadImage(item.url)
        imageBanner.setOnTouchListener { _, motionEvent ->
            listener.onTouchEvent(motionEvent)
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
