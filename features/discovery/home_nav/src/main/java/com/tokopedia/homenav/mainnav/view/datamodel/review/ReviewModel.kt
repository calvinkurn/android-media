package com.tokopedia.homenav.mainnav.view.datamodel.review

import com.tokopedia.homenav.MePage
import com.tokopedia.homenav.mainnav.domain.model.NavReviewModel
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.ReviewTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder

/**
 * Created by Frenzel
 */

@MePage(MePage.Widget.REVIEW)
data class ReviewModel(
        val navReviewModel: NavReviewModel,
        val position: Int
): ReviewNavVisitable, ImpressHolder() {
    override fun type(factory: ReviewTypeFactory): Int {
        return factory.type(this)
    }
}
