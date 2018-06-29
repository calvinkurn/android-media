package com.tokopedia.topads.keyword.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.tokopedia.topads.R
import com.tokopedia.topads.common.util.inflateLayout
import com.tokopedia.topads.keyword.domain.model.keywordadd.AddKeywordDomainModelDatum
import com.tokopedia.topads.keyword.view.adapter.viewholder.TopAdsKeywordAddViewHolder
import kotlinx.android.synthetic.main.item_top_ads_keyword_add_new.view.*

class TopAdsKeywordAddAdapter: RecyclerView.Adapter<TopAdsKeywordAddViewHolder>() {
    val localKeywords: MutableList<AddKeywordDomainModelDatum> = mutableListOf()
    var listener: OnKeywordRemovedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): TopAdsKeywordAddViewHolder {
        return TopAdsKeywordAddViewHolder(parent.inflateLayout(R.layout.item_top_ads_keyword_add_new))
    }

    override fun getItemCount(): Int {
        return localKeywords.size
    }

    override fun onBindViewHolder(holder: TopAdsKeywordAddViewHolder, pos: Int) {
        holder.bind(localKeywords[pos])
        holder.itemView.delete.setOnClickListener {
            remove(pos)
            listener?.onRemoved(pos)
        }
    }

    private fun remove(position: Int){
        localKeywords.removeAt(position)
        notifyItemRemoved(position)
    }

    fun addBulk(keywords: List<AddKeywordDomainModelDatum>){
        localKeywords.addAll(keywords)
        notifyItemRangeInserted(localKeywords.size, keywords.size)
    }

    fun clear() {
        localKeywords.clear()
        notifyDataSetChanged()
    }

    interface OnKeywordRemovedListener {
        fun onRemoved(position: Int)
    }
}