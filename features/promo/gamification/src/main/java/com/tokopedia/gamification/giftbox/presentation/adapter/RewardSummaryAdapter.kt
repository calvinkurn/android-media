package com.tokopedia.gamification.giftbox.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.gamification.giftbox.presentation.viewholder.CouponViewHolder
import com.tokopedia.gamification.giftbox.presentation.viewholder.RandomRewardViewHolder

class RewardSummaryAdapter(val dataList: ArrayList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_SPECIAL = 0
    private val TYPE_NORMAL = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_NORMAL -> RandomRewardViewHolder(layoutInflater.inflate(RandomRewardViewHolder.LAYOUT, parent, false))
            else -> CouponViewHolder(layoutInflater.inflate(CouponViewHolder.LAYOUT, parent, false))
        }
    }

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CouponViewHolder -> holder.bind()
            is RandomRewardViewHolder -> holder.bind()
        }

    }
}