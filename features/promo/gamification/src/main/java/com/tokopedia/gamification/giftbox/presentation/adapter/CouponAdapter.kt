package com.tokopedia.gamification.giftbox.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.gamification.R

class CouponAdapter : RecyclerView.Adapter<VH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(VH.LAYOUT, parent, false)
        return VH(v)
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun onBindViewHolder(holder: VH, position: Int) {

    }
}

class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object{
        val LAYOUT = R.layout.list_item_coupons
    }
}