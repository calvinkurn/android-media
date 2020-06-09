package com.tokopedia.play_common.widget.playBannerCarousel.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play_common.widget.playBannerCarousel.event.PlayBannerCarouselViewEventListener

abstract class BasePlayBannerCarouselViewHolder<T>(view: View) : RecyclerView.ViewHolder(view){
    abstract fun bind(dataModel: T, listener: PlayBannerCarouselViewEventListener?)
}