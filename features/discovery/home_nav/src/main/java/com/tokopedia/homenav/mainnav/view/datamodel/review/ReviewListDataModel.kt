package com.tokopedia.homenav.mainnav.view.datamodel.review

import com.tokopedia.homenav.mainnav.domain.model.NavReviewModel
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.MainNavTypeFactory
import com.tokopedia.homenav.mainnav.view.datamodel.MainNavVisitable
import com.tokopedia.topads.sdk.domain.model.ImpressHolder

data class ReviewListDataModel(
    val showViewAll: Boolean = false,
    val reviewList: List<NavReviewModel>
): MainNavVisitable, ImpressHolder() {
    override fun id(): Any = "reviewList"

    override fun isContentTheSame(visitable: MainNavVisitable): Boolean =
        visitable is ReviewListDataModel &&
            visitable.reviewList == reviewList

    override fun type(factory: MainNavTypeFactory): Int {
        return factory.type(this)
    }
}
