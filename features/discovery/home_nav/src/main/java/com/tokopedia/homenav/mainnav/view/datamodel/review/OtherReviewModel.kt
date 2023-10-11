package com.tokopedia.homenav.mainnav.view.datamodel.review

import com.tokopedia.homenav.MePage
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.ReviewTypeFactory
import com.tokopedia.topads.sdk.domain.model.ImpressHolder

/**
 * Created by Frenzel on 18/04/22.
 */

@MePage(MePage.Widget.REVIEW)
data class OtherReviewModel(
        val otherReviewCount: Int = 0
): ReviewNavVisitable, ImpressHolder() {
    override fun type(factory: ReviewTypeFactory): Int {
        return factory.type(this)
    }
}
