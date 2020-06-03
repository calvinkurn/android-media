package com.tokopedia.play_common.widget.playBannerCarousel.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BasePlayBannerCarouselViewHolder<T>(view: View) : RecyclerView.ViewHolder(view){
    abstract fun bind(dataModel: T)
}