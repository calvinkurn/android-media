package com.tokopedia.thankyou_native.recommendation.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.recommendation.model.MarketPlaceRecommendationResult
import com.tokopedia.thankyou_native.recommendation.presentation.viewmodel.MarketPlaceRecommendationViewModel
import com.tokopedia.thankyou_native.recommendationdigital.model.DigitalRecommendationList
import com.tokopedia.thankyou_native.recommendationdigital.model.RecommendationsItem
import com.tokopedia.thankyou_native.recommendationdigital.presentation.adapter.listener.DigitalRecommendationViewListener
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.thank_item_recommendation.view.*
import kotlinx.coroutines.withContext
import java.io.IOException

class DigitalRecommendationViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        val LAYOUT_ID = R.layout.thank_digital_recommendation_item
    }

    private lateinit var recommendationsItem: RecommendationsItem

    fun bind(recommendationsItem: RecommendationsItem,
             listener: DigitalRecommendationViewListener?) {
        this.recommendationsItem = recommendationsItem


        }
    }

