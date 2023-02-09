package com.tokopedia.tokopedianow.search.presentation.listener

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel.TokoNowProductCardCarouselItemUiModel

interface BroadMatchListener {

    fun getRecyclerViewPool(): RecyclerView.RecycledViewPool

    fun onBroadMatchItemImpressed(
        broadMatchItemDataView: TokoNowProductCardCarouselItemUiModel,
        broadMatchIndex: Int
    )

    fun onBroadMatchItemClicked(
        broadMatchItemDataView: TokoNowProductCardCarouselItemUiModel,
        broadMatchIndex: Int
    )

    fun onBroadMatchItemATCNonVariant(
        broadMatchItemDataView: TokoNowProductCardCarouselItemUiModel,
        quantity: Int,
        broadMatchIndex: Int,
    )

    fun onBroadMatchSeeAllClicked(
        title: String,
        appLink: String
    )
}
