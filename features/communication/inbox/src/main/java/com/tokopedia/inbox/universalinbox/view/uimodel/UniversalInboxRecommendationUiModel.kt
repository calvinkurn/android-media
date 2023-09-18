package com.tokopedia.inbox.universalinbox.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.inbox.universalinbox.view.adapter.typefactory.UniversalInboxTypeFactory
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

data class UniversalInboxRecommendationUiModel constructor(
    val recommendationItem: RecommendationItem
) : Visitable<UniversalInboxTypeFactory> {

    override fun type(typeFactory: UniversalInboxTypeFactory): Int {
        return typeFactory.type(this)
    }
}
