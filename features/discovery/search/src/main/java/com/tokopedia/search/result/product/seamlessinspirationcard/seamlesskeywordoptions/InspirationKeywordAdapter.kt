package com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class InspirationKeywordAdapter(
    private val inspirationKeywordDataView: List<InspirationKeywordDataView>,
    private val inspirationKeywordListener: InspirationKeywordListener
) : RecyclerView.Adapter<InspirationKeywordItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InspirationKeywordItemViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(InspirationKeywordItemViewHolder.LAYOUT, parent, false)

        return InspirationKeywordItemViewHolder(view, inspirationKeywordListener)
    }

    override fun getItemCount(): Int {
        return inspirationKeywordDataView.size
    }

    override fun onBindViewHolder(holder: InspirationKeywordItemViewHolder, position: Int) {
        holder.bind(inspirationKeywordDataView[position])
    }
}
