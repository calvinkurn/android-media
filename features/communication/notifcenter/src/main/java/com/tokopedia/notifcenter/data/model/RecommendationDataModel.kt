package com.tokopedia.notifcenter.data.model

import com.tokopedia.abstraction.base.view.adapter.Visitable

data class RecommendationDataModel(
        val item: List<Visitable<*>>,
        val hasNext: Boolean
) {
}