package com.tokopedia.review.feature.ovoincentive.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.review.feature.ovoincentive.presentation.adapter.viewholder.IncentiveOvoTnCViewHolder
import com.tokopedia.review.inbox.R

class IncentiveOvoTnCAdapter(private var list: List<String>, private val incentiveOvoTnCListener: IncentiveOvoTnCViewHolder.Listener)
    : RecyclerView.Adapter<IncentiveOvoTnCViewHolder>() {

    override fun onBindViewHolder(holderTnC: IncentiveOvoTnCViewHolder, position: Int) {
        holderTnC.bindHero(list[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncentiveOvoTnCViewHolder {
        return IncentiveOvoTnCViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_incentive_ovo_tnc, parent, false), incentiveOvoTnCListener)
    }

    override fun getItemCount(): Int = list.size

    fun setElements(list: List<String>) {
        this.list = list
        notifyDataSetChanged()
    }
}