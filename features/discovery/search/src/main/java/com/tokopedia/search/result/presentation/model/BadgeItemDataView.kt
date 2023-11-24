package com.tokopedia.search.result.presentation.model

import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.domain.model.SearchProductModel.OtherRelatedProductBadge
import com.tokopedia.search.result.domain.model.SearchProductV5

data class BadgeItemDataView(
        val imageUrl: String = "",
        val title: String = "",
        val isShown: Boolean = false,
) {

    companion object {

        fun create(otherRelatedProductBadge: OtherRelatedProductBadge) =
            BadgeItemDataView(
                otherRelatedProductBadge.imageUrl,
                otherRelatedProductBadge.title,
                otherRelatedProductBadge.isShown,
            )

        fun create(badge: SearchProductV5.Data.Badge) =
            BadgeItemDataView(badge.url, badge.title, true)

        fun create(badge: SearchProductModel.InspirationCarouselProductBadge) =
            BadgeItemDataView(
                imageUrl = badge.imageUrl,
                title = badge.title,
                isShown = badge.isShown,
            )
    }
}
