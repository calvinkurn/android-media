package com.tokopedia.tokopoints.view.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokopoints.view.model.addpointsection.SectionsItem

class AddPointAdapterVH(val view: View, val listenerItemClick: ListenerItemClick) : RecyclerView.ViewHolder(view) {
    fun bind(data: SectionsItem) {
        view.setOnClickListener { listenerItemClick.onClickItem() }

    }

    interface ListenerItemClick {
        fun onClickItem()
    }
}
