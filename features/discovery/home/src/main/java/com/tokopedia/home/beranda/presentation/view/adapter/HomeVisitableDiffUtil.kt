package com.tokopedia.home.beranda.presentation.view.adapter

import androidx.recyclerview.widget.DiffUtil

class HomeVisitableDiffUtil : DiffUtil.ItemCallback<HomeVisitable>() {
    override fun areItemsTheSame(oldItem: HomeVisitable, newItem: HomeVisitable): Boolean {
        return oldItem.visitableId() == newItem.visitableId()
    }

    override fun areContentsTheSame(oldItem: HomeVisitable, newItem: HomeVisitable): Boolean {
        return oldItem.equalsWith(newItem)
    }

    override fun getChangePayload(oldItem: HomeVisitable, newItem: HomeVisitable): Any? {
        return oldItem.getChangePayloadFrom(newItem)
    }
}
