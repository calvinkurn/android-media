package com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.reimagine

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.InspirationKeywordDataView
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.InspirationKeywordListener

class InspirationKeywordReimagineAdapter(
    inspirationKeywords: List<InspirationKeywordDataView>,
    private val inspirationCarouselListener: InspirationKeywordListener,
): RecyclerView.Adapter<InspirationKeywordReimagineItemViewHolder>() {

    private val optionList = inspirationKeywords

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InspirationKeywordReimagineItemViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(InspirationKeywordReimagineItemViewHolder.LAYOUT, parent, false)

        return InspirationKeywordReimagineItemViewHolder(view, inspirationCarouselListener)
    }

    override fun getItemCount() = optionList.size

    override fun onBindViewHolder(holder: InspirationKeywordReimagineItemViewHolder, position: Int) {
        holder.bind(optionList[position])
    }
}
