package com.tokopedia.home_wishlist.view.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_wishlist.R
import com.tokopedia.home_wishlist.model.datamodel.RecommendationCarouselDataModel
import com.tokopedia.home_wishlist.model.datamodel.RecommendationCarouselItemDataModel

class RecommendationCarouselViewHolder(view: View) : AbstractViewHolder<RecommendationCarouselDataModel>(view) {
    private val title: TextView by lazy { view.findViewById<TextView>(R.id.title) }
    private val seeMore: TextView by lazy { view.findViewById<TextView>(R.id.see_more) }
    private val recyclerView: RecyclerView by lazy { view.findViewById<RecyclerView>(R.id.list) }
    private val list = mutableListOf<RecommendationCarouselItemDataModel>()

    override fun bind(element: RecommendationCarouselDataModel?) {

    }

    companion object{
        val LAYOUT = R.layout.layout_recommendation_carousel
    }
}