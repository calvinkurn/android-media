package com.tokopedia.salam.umrah.search.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.salam.umrah.R
import kotlinx.android.synthetic.main.partial_umrah_search_empty.view.*

class UmrahSearchEmptyViewHolder(itemView: View, val listener:OnClickListener) : AbstractViewHolder<EmptyModel>(itemView) {
    override fun bind(element: EmptyModel) {
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