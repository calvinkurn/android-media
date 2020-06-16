package com.tokopedia.thankyou_native.recommendationdigital.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.thankyou_native.recommendationdigital.model.RecommendationsItem
import com.tokopedia.thankyou_native.recommendationdigital.presentation.adapter.listener.DigitalRecommendationViewListener
import com.tokopedia.thankyou_native.recommendationdigital.presentation.view.DigitalRecommendationWidget

class DigitalRecommendationAdapter(val items: List<RecommendationsItem>, val onItemBindListener: DigitalRecommendationViewListener)
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

//    private fun mapRecommendationData(data: RecommendationsItem): RecommendationsItem {
//        return RecommendationsItem(
//                id = data.productId,
//                name = data.categoryName,
//                imageUrl = data.iconUrl,
//                url = data.webLink,
//                applink = data.applink,
//                title1st = data.title,
//                desc1st = data.clientNumber,
//                tagName = data.tag,
//                tagType = data.tagType,
//                price = data.productPrice.toString()
//        )
//    }

    class DigitalRecommendationViewHolder(itemView: DigitalRecommendationWidget) : RecyclerView.ViewHolder(itemView)
}
