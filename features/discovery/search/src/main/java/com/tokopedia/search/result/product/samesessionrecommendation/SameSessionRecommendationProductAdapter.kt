package com.tokopedia.search.result.product.samesessionrecommendation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselListener

class SameSessionRecommendationProductAdapter(
    private val listener: InspirationCarouselListener,
) : RecyclerView.Adapter<SameSessionRecommendationProductViewHolder>() {
    private val list = mutableListOf<InspirationCarouselDataView.Option.Product>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SameSessionRecommendationProductViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context)
            .inflate(SameSessionRecommendationProductViewHolder.LAYOUT, parent, false)

        return SameSessionRecommendationProductViewHolder(
            view,
            listener
        )
    }

    override fun onBindViewHolder(
        holder: SameSessionRecommendationProductViewHolder,
        position: Int
    ) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addAll(list: List<InspirationCarouselDataView.Option.Product>) {
        this.list.addAll(list)

        notifyDataSetChanged()
    }

    fun clearData() {
        val size = list.size
        list.clear()
        notifyItemRangeRemoved(0, size)
    }
}
