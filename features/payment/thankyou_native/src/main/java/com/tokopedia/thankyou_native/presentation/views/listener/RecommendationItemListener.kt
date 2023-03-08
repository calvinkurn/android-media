package com.tokopedia.thankyou_native.presentation.views.listener

import android.widget.LinearLayout
import com.tokopedia.thankyou_native.recommendation.presentation.view.IRecommendationView

interface RecommendationItemListener {

    var iRecommendationView: IRecommendationView?
    fun addRecommendation(containerView: LinearLayout?)
}
