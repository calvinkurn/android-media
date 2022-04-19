package com.tokopedia.digital.home.old.presentation.adapter.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.common_digital.common.presentation.model.RecommendationItemEntity
import com.tokopedia.digital.home.old.model.DigitalQuickBuyItem
import com.tokopedia.digital.home.old.presentation.customview.DigitalQuickBuyWidget
import com.tokopedia.digital.home.old.presentation.listener.OnItemBindListener

class DigitalItemRecommendationAdapter(val items: List<RecommendationItemEntity>, val onItemBindListener: OnItemBindListener)
    : RecyclerView.Adapter<DigitalItemRecommendationAdapter.DigitalItemRecommendationViewHolder>() {

    override fun onBindViewHolder(viewHolder: DigitalItemRecommendationViewHolder, position: Int) {
        val element = items[position]
        (viewHolder.itemView as DigitalQuickBuyWidget).data = mapRecommendationData(element)
        viewHolder.itemView.setOnClickListener {
            onItemBindListener.onRecommendationClicked(element, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): DigitalItemRecommendationViewHolder {
        val view = DigitalQuickBuyWidget(parent.context)
        return DigitalItemRecommendationViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun mapRecommendationData(data: RecommendationItemEntity): DigitalQuickBuyItem {
        return DigitalQuickBuyItem(
                id = data.productId,
                name = data.categoryName,
                imageUrl = data.iconUrl,
                url = data.webLink,
                applink = data.applink,
                title1st = data.title,
                desc1st = data.clientNumber,
                tagName = data.tag,
                tagType = data.tagType,
                price = data.productPrice.toString()
        )
    }

    class DigitalItemRecommendationViewHolder(itemView: DigitalQuickBuyWidget) : RecyclerView.ViewHolder(itemView)
}
