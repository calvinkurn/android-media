package com.tokopedia.dilayanitokopedia.home.presentation.viewholder.foryou

import android.view.View
import android.widget.Button
import android.widget.TextView
import com.tokopedia.dilayanitokopedia.R
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationEmpty
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.smart_recycler_helper.SmartAbstractViewHolder
import com.tokopedia.smart_recycler_helper.SmartListener

class HomeRecommendationEmptyViewHolder(view: View) : SmartAbstractViewHolder<HomeRecommendationEmpty>(view) {
    private val title = view.findViewById<TextView>(R.id.text_view_empty_title_text)
    private val description = view.findViewById<TextView>(R.id.text_view_empty_content_text)
    private val buttonRetry = view.findViewById<Button>(R.id.button_add_promo)
    override fun bind(element: HomeRecommendationEmpty, listener: SmartListener) {
        buttonRetry.gone()
        title.setText(R.string.home_recommendation_empty_title)
        description.setText(R.string.home_recommendation_empty_description)
    }
}
