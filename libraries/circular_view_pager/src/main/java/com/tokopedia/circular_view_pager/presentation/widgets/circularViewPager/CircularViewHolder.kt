package com.tokopedia.home_page_banner.presentation.widgets.circularViewPager

import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class CircularViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    open fun bind(item: CircularModel, listener: CircularListener){
    }
}
interface CircularListener{
    fun onClick(position: Int)
}