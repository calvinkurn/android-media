package com.tokopedia.homenav.mainnav.view.datamodel.review

import com.tokopedia.homenav.MePage
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.MainNavTypeFactory
import com.tokopedia.homenav.mainnav.view.datamodel.MainNavVisitable
import com.tokopedia.topads.sdk.domain.model.ImpressHolder

@MePage(MePage.Widget.REVIEW)
data class ErrorStateReviewDataModel(
        val sectionId: Int? = null
): MainNavVisitable, ImpressHolder() {
    override fun id(): Any = "Error state review"

    override fun isContentTheSame(visitable: MainNavVisitable): Boolean = id() == visitable.id()

    override fun type(factory: MainNavTypeFactory): Int {
        return factory.type(this)
    }
}
