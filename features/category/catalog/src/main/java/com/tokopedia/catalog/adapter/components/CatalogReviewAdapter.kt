package com.tokopedia.catalog.adapter.components

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.raw.CatalogProductReviewResponse
import com.tokopedia.catalog.viewholder.components.CatalogReviewViewHolder

class CatalogReviewAdapter (private var list : List<CatalogProductReviewResponse.CatalogGetProductReview.ReviewData.Review?>, private val catalogDetailListener: CatalogDetailListener?)
    : RecyclerView.Adapter<CatalogReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogReviewViewHolder {
        return CatalogReviewViewHolder(View.inflate(parent.context, CatalogReviewViewHolder.LAYOUT, null))
    }

    override fun getItemCount(): Int  = list.size

    override fun onBindViewHolder(holder: CatalogReviewViewHolder, position: Int) {
        list[position]?.let {
            holder.bind(it,catalogDetailListener)
        }
    }

    fun submitList(data: List<CatalogProductReviewResponse.CatalogGetProductReview.ReviewData.Review?>){
        this.list = data
        notifyItemRangeInserted(0,data.size)
    }
}