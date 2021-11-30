package com.tokopedia.promocheckoutmarketplace.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.promocheckoutmarketplace.presentation.listener.PromoCheckoutActionListener
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.*
import com.tokopedia.promocheckoutmarketplace.presentation.viewholder.*

class PromoCheckoutAdapterTypeFactory(private val listener: PromoCheckoutActionListener)
    : BaseAdapterTypeFactory(), PromoCheckoutTypeFactory {

    override fun type(uiModel: PromoRecommendationUiModel): Int {
        return PromoRecommendationViewHolder.LAYOUT
    }

    override fun type(uiModel: PromoInputUiModel): Int {
        return PromoInputViewHolder.LAYOUT
    }

    override fun type(uiModel: PromoEligibilityHeaderUiModel): Int {
        return PromoEligibilityHeaderViewHolder.LAYOUT
    }

    override fun type(uiModel: PromoListHeaderUiModel): Int {
        if (uiModel.uiState.isEnabled) {
            return PromoListHeaderEnabledViewHolder.LAYOUT
        } else {
            return PromoListHeaderDisabledViewHolder.LAYOUT
        }
    }

    override fun type(uiModel: PromoListItemUiModel): Int {
        return PromoListItemViewHolder.LAYOUT
    }

    override fun type(uiModel: PromoEmptyStateUiModel): Int {
        return PromoEmptyStateViewHolder.LAYOUT
    }

    override fun type(uiModel: LoadingModel): Int {
        return PromoLoadingViewHolder.LAYOUT
    }

    override fun type(uiModel: PromoErrorStateUiModel): Int {
        return PromoErrorViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<out Visitable<*>> {
        return when (viewType) {
            PromoRecommendationViewHolder.LAYOUT -> PromoRecommendationViewHolder(view, listener)
            PromoInputViewHolder.LAYOUT -> PromoInputViewHolder(view, listener)
            PromoEligibilityHeaderViewHolder.LAYOUT -> PromoEligibilityHeaderViewHolder(view, listener)
            PromoListHeaderEnabledViewHolder.LAYOUT -> PromoListHeaderEnabledViewHolder(view, listener)
            PromoListHeaderDisabledViewHolder.LAYOUT -> PromoListHeaderDisabledViewHolder(view, listener)
            PromoListItemViewHolder.LAYOUT -> PromoListItemViewHolder(view, listener)
            PromoEmptyStateViewHolder.LAYOUT -> PromoEmptyStateViewHolder(view, listener)
            PromoLoadingViewHolder.LAYOUT -> PromoLoadingViewHolder(view)
            PromoErrorViewHolder.LAYOUT -> PromoErrorViewHolder(view, listener)
            else -> super.createViewHolder(view, viewType)
        }

    }
}