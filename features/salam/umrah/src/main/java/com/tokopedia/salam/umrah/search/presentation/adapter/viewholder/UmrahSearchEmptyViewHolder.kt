package com.tokopedia.salam.umrah.search.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.search.data.UmrahSearchEmpty
import kotlinx.android.synthetic.main.partial_umrah_search_empty.view.*

class UmrahSearchEmptyViewHolder(itemView: View, val listener:OnClickListener) : AbstractViewHolder<UmrahSearchEmpty>(itemView) {
    override fun bind(element: UmrahSearchEmpty) {
        itemView.btn_umrah_search_empty_open_filter.setOnClickListener {
            listener.umrahSearchEmptyOnClickListener()
        }
    }

    interface OnClickListener{
        fun umrahSearchEmptyOnClickListener()
    }
    companion object {
        val LAYOUT = R.layout.partial_umrah_search_empty
    }
}