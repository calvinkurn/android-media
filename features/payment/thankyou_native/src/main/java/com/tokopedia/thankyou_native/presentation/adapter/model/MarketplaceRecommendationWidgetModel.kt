package com.tokopedia.thankyou_native.presentation.adapter.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.presentation.adapter.factory.BottomContentFactory

data class MarketplaceRecommendationWidgetModel(
    val thanksPageData: ThanksPageData,
    val fragment: BaseDaggerFragment
) : Visitable<BottomContentFactory>, WidgetTag(TAG) {

    override fun type(typeFactory: BottomContentFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        const val TAG = "pg"
    }
}
