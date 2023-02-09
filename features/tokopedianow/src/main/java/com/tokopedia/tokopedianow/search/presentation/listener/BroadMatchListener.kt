package com.tokopedia.tokopedianow.search.presentation.listener

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel

interface BroadMatchListener {

    fun getRecyclerViewPool(): RecyclerView.RecycledViewPool

    fun onBroadMatchItemImpressed(
        broadMatchItemDataView: ProductCardCompactCarouselItemUiModel,
        broadMatchIndex: Int
    )

    fun onBroadMatchItemClicked(
        broadMatchItemDataView: ProductCardCompactCarouselItemUiModel,
        broadMatchIndex: Int
    )

    fun onBroadMatchItemATCNonVariant(
        broadMatchItemDataView: ProductCardCompactCarouselItemUiModel,
        quantity: Int,
        broadMatchIndex: Int,
    )

    fun onBroadMatchSeeAllClicked(
        title: String,
        appLink: String
    )
}
