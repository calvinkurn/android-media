package com.tokopedia.entertainment.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @author by errysuprayogi on 3/29/17.
 */
abstract class HomeViewHolder<in T : HomeItem<*>>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(element: T)

}