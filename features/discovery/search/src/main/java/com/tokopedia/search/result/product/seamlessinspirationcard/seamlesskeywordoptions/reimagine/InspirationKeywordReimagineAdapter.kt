package com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.reimagine

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.InspirationKeywordCardView
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.InspirationKeywordListener

class InspirationKeywordReimagineAdapter(
    private val inspirationKeywords: InspirationKeywordCardView,
    private val inspirationCarouselListener: InspirationKeywordListener,
    ): RecyclerView.Adapter<InspirationKeywordReimagineItemViewHolder>() {

    private val optionList = inspirationKeywords.optionsItems

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InspirationKeywordReimagineItemViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(InspirationKeywordReimagineItemViewHolder.LAYOUT, parent, false)

        return InspirationKeywordReimagineItemViewHolder(
            view,
            inspirationCarouselListener,
            inspirationKeywords.layoutType,
            inspirationKeywords.searchTerm,
        )
    }

    override fun getItemCount() = optionList.size

    override fun onBindViewHolder(holder: InspirationKeywordReimagineItemViewHolder, position: Int) {
        holder.bind(optionList[position])
    }
}
