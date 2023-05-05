package com.tokopedia.thankyou_native.presentation.adapter.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationPage
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.presentation.adapter.factory.BottomContentFactory
import com.tokopedia.thankyou_native.presentation.views.widgettag.WidgetTag

data class DigitalRecommendationWidgetModel(
    val thanksPageData: ThanksPageData,
    val pgCategoryIds: List<Int> = listOf(),
    val pageType: DigitalRecommendationPage,
    val fragment: BaseDaggerFragment
) : Visitable<BottomContentFactory>, WidgetTag {

    override fun type(typeFactory: BottomContentFactory): Int {
        return typeFactory.type(this)
    }

    override val tag: String = TAG

    companion object {
        const val TAG = "dg"
    }
}
