package com.tokopedia.home_wishlist.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.home_wishlist.R
import com.tokopedia.home_wishlist.base.SmartAbstractViewHolder
import com.tokopedia.home_wishlist.base.SmartListener
import com.tokopedia.home_wishlist.model.datamodel.RecommendationTitleDataModel
import com.tokopedia.unifyprinciples.Typography

class RecommendationTitleViewHolder (
        private val view: View
) : SmartAbstractViewHolder<RecommendationTitleDataModel>(view){

    private val title: Typography by lazy { view.findViewById<Typography>(R.id.title) }
    private val seeMore: Typography by lazy { view.findViewById<Typography>(R.id.see_more) }

    override fun bind(element: RecommendationTitleDataModel, listener: SmartListener) {
        element.let { dataModel ->
            title.text = dataModel.title
            seeMore.visibility = if(element.seeMoreAppLink.isEmpty()) View.GONE else View.VISIBLE
            seeMore.setOnClickListener { RouteManager.route(seeMore.context, dataModel.seeMoreAppLink) }
        }
    }

    companion object{
        val LAYOUT = R.layout.layout_recommendation_title
    }
}