package com.tokopedia.home_recom.view.viewholder

import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.analytics.RecommendationPageTracking
import com.tokopedia.home_recom.model.datamodel.TitleDataModel
import com.tokopedia.home_recom.model.datamodel.TitleListener
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener

/**
 * Created by lukas on 21/05/2019
 *
 * A class for holder view Title
 */
class TitleViewHolder(private val view: View, val listener: TitleListener) : AbstractViewHolder<TitleDataModel>(view){
    private val title: TextView by lazy { view.findViewById<TextView>(R.id.title) }
    private val seeMore: TextView by lazy { view.findViewById<TextView>(R.id.see_more) }

    override fun bind(element: TitleDataModel) {
        title.text = element.title
        seeMore.visibility = if(element.seeMoreAppLink.isEmpty()) View.GONE else View.VISIBLE
        seeMore.setOnClickListener {
            listener.onClickSeeMore(pageName = element.pageName, seeMoreAppLink = element.seeMoreAppLink)
        }
    }

}