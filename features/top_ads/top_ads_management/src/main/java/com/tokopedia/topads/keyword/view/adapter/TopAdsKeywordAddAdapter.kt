package com.tokopedia.topads.keyword.view.adapter

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.ViewGroup
import com.tokopedia.topads.R
import com.tokopedia.topads.common.extension.inflateLayout
import com.tokopedia.topads.keyword.domain.model.keywordadd.AddKeywordDomainModelDatum
import com.tokopedia.topads.keyword.view.adapter.viewholder.TopAdsKeywordAddViewHolder
import kotlinx.android.synthetic.main.item_top_ads_keyword_add_new.view.*

class TopAdsKeywordAddAdapter: RecyclerView.Adapter<TopAdsKeywordAddViewHolder>() {
    val localKeywords: MutableList<AddKeywordDomainModelDatum> = mutableListOf()
    var listener: OnKeywordRemovedListener? = null
    private val errorList: MutableList<String> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): TopAdsKeywordAddViewHolder {
        return TopAdsKeywordAddViewHolder(parent.inflateLayout(R.layout.item_top_ads_keyword_add_new))
    }

    override fun getItemCount(): Int {
        return localKeywords.size
    }

    override fun onBindViewHolder(holder: TopAdsKeywordAddViewHolder, pos: Int) {
        val localKeyword = localKeywords[pos]
        holder.bind(localKeyword)
        holder.itemView.delete.setOnClickListener {
            remove(pos)
            listener?.onRemoved(pos)
        }
        val titleColor = if (localKeyword.keywordTag.toLowerCase() in errorList){
            R.color.background_error
        } else {
            R.color.font_black_primary_70
        }
        holder.itemView.title.setTextColor(ContextCompat
                .getColor(holder.itemView.context, titleColor))
    }

    private fun remove(position: Int){
        Log.e("tes remove", "$position")
        if(isWithinDataset(position)) {
            localKeywords.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)
        }
    }

    private fun isWithinDataset(position: Int): Boolean {
        return position >= 0 && position <= localKeywords.size - 1
    }

    fun addBulk(keywords: List<AddKeywordDomainModelDatum>){
        localKeywords.addAll(keywords)
        notifyItemRangeInserted(localKeywords.size, keywords.size)
    }

    fun clear() {
        localKeywords.clear()
        notifyDataSetChanged()
    }

    fun settErrorList(errorList: List<String>) {
        this.errorList.clear()
        this.errorList.addAll(errorList)
        notifyDataSetChanged()
    }

    interface OnKeywordRemovedListener {
        fun onRemoved(position: Int)
    }
}