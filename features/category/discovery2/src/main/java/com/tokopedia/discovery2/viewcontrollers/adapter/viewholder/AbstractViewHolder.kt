package com.tokopedia.discovery2.viewcontrollers.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.viewcontrollers.adapter.BaseDataModel

abstract class AbstractViewHolder<in T : BaseDataModel>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bindView(item: T)
}