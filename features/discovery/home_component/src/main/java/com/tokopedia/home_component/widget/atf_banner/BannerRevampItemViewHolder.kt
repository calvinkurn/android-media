package com.tokopedia.home_component.widget.atf_banner

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.viewholders.adapter.BannerItemListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.unifycomponents.ImageUnify

/**
 * Created by frenzel
 */
class BannerRevampItemViewHolder(itemView: View, val listener: BannerItemListener) :
    AbstractViewHolder<BannerRevampItemModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = com.tokopedia.home_component.R.layout.layout_banner_revamp_channel_item
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun bind(item: BannerRevampItemModel) {
        val imageBanner = itemView.findViewById<ImageUnify>(R.id.image_banner_revamp)
        imageBanner.setImageUrl(item.url)
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
