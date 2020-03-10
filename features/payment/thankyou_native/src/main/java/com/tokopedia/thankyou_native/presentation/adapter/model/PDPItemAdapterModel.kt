package com.tokopedia.thankyou_native.presentation.adapter.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.thankyou_native.presentation.adapter.PDPThankYouPageFactory

class PDPItemAdapterModel(
        val recommendationItem: RecommendationItem
) : Visitable<PDPThankYouPageFactory> {

    override fun type(typeFactory: PDPThankYouPageFactory?): Int {
        return typeFactory?.type(this)!!
    }
}