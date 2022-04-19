package com.tokopedia.topads.common.view.adapter.etalase.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Author errysuprayogi on 11,November,2019
 */

abstract class EtalaseViewHolder<in T>(view: View): RecyclerView.ViewHolder(view) {
     open fun bind(item: T){}
}
