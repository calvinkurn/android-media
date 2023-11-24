package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation

import android.view.View
import android.widget.Button
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationEmpty
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.baselist.R as baselistR

class HomeRecommendationEmptyViewHolder(view: View) :
    AbstractViewHolder<HomeRecommendationEmpty>(view) {

    private val title =
        view.findViewById<TextView>(baselistR.id.text_view_empty_title_text)
    private val description =
        view.findViewById<TextView>(baselistR.id.text_view_empty_content_text)
    private val buttonRetry =
        view.findViewById<Button>(baselistR.id.button_add_promo)

    override fun bind(element: HomeRecommendationEmpty) {
        buttonRetry.gone()
        title.setText(R.string.home_recommendation_empty_title)
        description.setText(R.string.home_recommendation_empty_description)
    }
}
