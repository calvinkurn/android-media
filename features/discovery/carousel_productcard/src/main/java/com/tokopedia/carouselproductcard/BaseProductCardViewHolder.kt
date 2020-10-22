package com.tokopedia.carouselproductcard

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseProductCardViewHolder<T>(itemView: View): RecyclerView.ViewHolder(itemView){
    abstract fun bind(model: T)
    abstract fun recycle()
}