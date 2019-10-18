package com.tokopedia.officialstore.official.presentation.dynamic_channel

import android.support.annotation.LayoutRes
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.officialstore.R

class DynamicChannelLegoViewHolder(
        private val view: View?
) : AbstractViewHolder<DynamicChannelViewModel>(view) {

    override fun bind(element: DynamicChannelViewModel?) {
        val recyclerView: RecyclerView = itemView.findViewById(R.id.dc_lego_rv)

        recyclerView.layoutManager = GridLayoutManager(
                itemView.context,
                3,
                GridLayoutManager.VERTICAL,
                false
        )

        recyclerView.adapter = LegoListAdapter(view?.context)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.dynamic_channel_lego_main
    }
}
