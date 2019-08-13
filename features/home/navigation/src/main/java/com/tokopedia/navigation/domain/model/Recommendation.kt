package com.tokopedia.navigation.domain.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.navigation.presentation.adapter.InboxTypeFactory
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

/**
 * Created by Lukas on 2019-07-31
 */
class Recommendation(val recommendationItem: RecommendationItem) : Visitable<InboxTypeFactory> {

    override fun type(typeFactory: InboxTypeFactory): Int {
        return typeFactory.type(this)
    }
}
