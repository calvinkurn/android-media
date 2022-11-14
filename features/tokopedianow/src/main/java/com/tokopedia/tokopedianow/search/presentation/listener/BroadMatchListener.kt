package com.tokopedia.tokopedianow.search.presentation.listener

import android.os.Parcelable
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardCarouselItemUiModel

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

    fun onBroadMatchItemATCNonVariantAnimationFinished(
        broadMatchItemDataView: TokoNowProductCardCarouselItemUiModel,
        quantity: Int,
        broadMatchIndex: Int,
    )

    fun onSaveCarouselScrollState(
        adapterPosition: Int,
        state: Parcelable?
    )

    fun onGetCarouselScrollState(
        adapterPosition: Int
    ): Parcelable?

    fun onBroadMatchSeeAllClicked(
        title: String,
        appLink: String
    )
}
