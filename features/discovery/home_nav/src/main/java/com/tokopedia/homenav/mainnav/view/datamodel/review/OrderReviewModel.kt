package com.tokopedia.homenav.mainnav.view.datamodel.review

import com.tokopedia.homenav.mainnav.domain.model.NavReviewModel
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.OrderListTypeFactory
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.ReviewTypeFactory
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OrderNavVisitable
import com.tokopedia.kotlin.model.ImpressHolder

/**
 * Created by Frenzel
 */
data class OrderReviewModel(
        val navReviewModel: NavReviewModel
): ReviewNavVisitable, ImpressHolder() {
    override fun type(factory: ReviewTypeFactory): Int {
        return factory.type(this)
    }
}
