package com.tokopedia.thankyou_native.presentation.adapter.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.thankyou_native.analytics.GyroRecommendationAnalytics
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.presentation.adapter.factory.BottomContentFactory

data class GyroRecommendationWidgetModel(
    val gyroRecommendation: GyroRecommendation,
    val thanksPageData: ThanksPageData,
    val gyroRecommendationAnalytics: GyroRecommendationAnalytics,
): Visitable<BottomContentFactory> {

    override fun type(typeFactory: BottomContentFactory): Int {
        return typeFactory.type(this)
    }
}
