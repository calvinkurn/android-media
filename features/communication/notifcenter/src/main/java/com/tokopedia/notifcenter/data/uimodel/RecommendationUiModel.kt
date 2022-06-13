package com.tokopedia.notifcenter.data.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.notifcenter.presentation.adapter.typefactory.notification.NotificationTypeFactory
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

data class RecommendationUiModel constructor(
        val recommendationItem: RecommendationItem
) : Visitable<NotificationTypeFactory> {
    override fun type(typeFactory: NotificationTypeFactory): Int {
        return typeFactory.type(this)
    }
}