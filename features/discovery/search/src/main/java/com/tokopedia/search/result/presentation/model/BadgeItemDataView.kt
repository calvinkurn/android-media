package com.tokopedia.search.result.presentation.model

import com.tokopedia.search.result.domain.model.SearchProductModel.OtherRelatedProductBadge

data class BadgeItemDataView(
        val imageUrl: String = "",
        val title: String = "",
        val isShown: Boolean = false,
) {

    companion object {

        fun create(otherRelatedProductBadge: OtherRelatedProductBadge) =
            BadgeItemDataView(
                otherRelatedProductBadge.imageUrl,
                "",
                otherRelatedProductBadge.isShown,
            )
    }
}
