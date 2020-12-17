package com.tokopedia.review.feature.ovoincentive.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.review.R
import com.tokopedia.review.feature.ovoincentive.presentation.IncentiveOvoListener
import com.tokopedia.review.feature.ovoincentive.presentation.adapter.viewholder.IncentiveOvoViewHolder

class IncentiveOvoAdapter(private val list: List<String>, private val incentiveOvoListener: IncentiveOvoListener)
    : RecyclerView.Adapter<IncentiveOvoViewHolder>() {

    override fun onBindViewHolder(holder: IncentiveOvoViewHolder, position: Int) {
        holder.bindHero(list[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncentiveOvoViewHolder {
        return IncentiveOvoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_incentive_ovo, parent, false), incentiveOvoListener)
    }

    override fun getItemCount(): Int = list.size


}