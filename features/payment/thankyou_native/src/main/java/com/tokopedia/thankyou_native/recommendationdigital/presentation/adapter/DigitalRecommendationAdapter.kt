package com.tokopedia.thankyou_native.recommendationdigital.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.thankyou_native.recommendationdigital.model.RecommendationItem
import com.tokopedia.thankyou_native.recommendationdigital.model.RecommendationsItem
import com.tokopedia.thankyou_native.recommendationdigital.presentation.adapter.listener.DigitalRecommendationViewListener
import com.tokopedia.thankyou_native.recommendationdigital.presentation.view.DigitalRecommendationWidget

class DigitalRecommendationAdapter(val items: List<RecommendationItem>,
                                   private val onItemBindListener: DigitalRecommendationViewListener)
    : RecyclerView.Adapter<DigitalRecommendationAdapter.DigitalRecommendationViewHolder>() {

    override fun onBindViewHolder(viewHolder: DigitalRecommendationViewHolder, position: Int) {
        val element = items[position]
        (viewHolder.itemView as DigitalRecommendationWidget).data = element
        viewHolder.itemView.setOnClickListener {
            onItemBindListener.onDigitalProductClick(element, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): DigitalRecommendationViewHolder {
        val view = DigitalRecommendationWidget(parent.context)
        return DigitalRecommendationViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onViewAttachedToWindow(holderDigital: DigitalRecommendationViewHolder) {
        super.onViewAttachedToWindow(holderDigital)
        onItemBindListener.onDigitalProductImpression(items.get(holderDigital.adapterPosition),
                position = holderDigital.adapterPosition)
    }


    class DigitalRecommendationViewHolder(itemView: DigitalRecommendationWidget) : RecyclerView.ViewHolder(itemView)
}
