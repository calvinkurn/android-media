package com.tokopedia.gamification.giftbox.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.gamification.giftbox.presentation.entities.RewardSummaryItem
import com.tokopedia.gamification.giftbox.presentation.viewholder.RandomRewardViewHolder

class RewardSummaryAdapter(val dataList: ArrayList<RewardSummaryItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_SPECIAL = 0
    private val TYPE_NORMAL = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_NORMAL -> RandomRewardViewHolder(layoutInflater.inflate(RandomRewardViewHolder.LAYOUT, parent, false))
            else -> CouponListVH(LayoutInflater.from(parent.context).inflate(CouponListVH.LAYOUT, parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = dataList[position]
        if (item.couponDetail != null) {
            return TYPE_SPECIAL
        }
        return TYPE_NORMAL
    }

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CouponListVH -> holder.setData(dataList[position].couponDetail!!)
            is RandomRewardViewHolder -> holder.bind(dataList[position].simpleReward!!)
        }

    }
}