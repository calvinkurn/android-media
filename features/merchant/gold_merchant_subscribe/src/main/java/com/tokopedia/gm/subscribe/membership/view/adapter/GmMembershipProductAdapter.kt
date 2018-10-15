package com.tokopedia.gm.subscribe.membership.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import com.tokopedia.gm.subscribe.R
import com.tokopedia.gm.subscribe.view.recyclerview.GmProductAdapter
import com.tokopedia.gm.subscribe.view.recyclerview.GmProductAdapterCallback

class GmMembershipProductAdapter(listener: GmProductAdapterCallback) : GmProductAdapter(listener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            GmProductAdapter.GM_PRODUCT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gm_subscribe_membership_product, parent, false)
                GmMembershipProductViewHolder(view)
            }
            else -> super.onCreateViewHolder(parent, viewType)
        }
    }
}
