package com.tokopedia.promocheckoutmarketplace.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.promocheckoutmarketplace.databinding.*
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

    override fun type(uiModel: PromoTabUiModel): Int {
        return PromoTabViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<out Visitable<*>> {
        return when (viewType) {
            PromoRecommendationViewHolder.LAYOUT -> {
                val viewBinding = PromoCheckoutMarketplaceModuleItemPromoRecommendationBinding.inflate(LayoutInflater.from(view.context), view as ViewGroup, false)
                PromoRecommendationViewHolder(viewBinding, listener)
            }
            PromoInputViewHolder.LAYOUT -> {
                val viewBinding = PromoCheckoutMarketplaceModuleItemPromoInputBinding.inflate(LayoutInflater.from(view.context), view as ViewGroup, false)
                PromoInputViewHolder(viewBinding, listener)
            }
            PromoEligibilityHeaderViewHolder.LAYOUT -> {
                val viewBinding = PromoCheckoutMarketplaceModuleItemPromoEligibilityHeaderBinding.inflate(LayoutInflater.from(view.context), view as ViewGroup, false)
                PromoEligibilityHeaderViewHolder(viewBinding)
            }
            PromoListHeaderEnabledViewHolder.LAYOUT -> {
                val viewBinding = PromoCheckoutMarketplaceModuleItemPromoListHeaderEnabledBinding.inflate(LayoutInflater.from(view.context), view as ViewGroup, false)
                PromoListHeaderEnabledViewHolder(viewBinding, listener)
            }
            PromoListHeaderDisabledViewHolder.LAYOUT -> {
                val viewBinding = PromoCheckoutMarketplaceModuleItemPromoListHeaderDisabledBinding.inflate(LayoutInflater.from(view.context), view as ViewGroup, false)
                PromoListHeaderDisabledViewHolder(viewBinding, listener)
            }
            PromoListItemViewHolder.LAYOUT -> {
                val viewBinding = PromoCheckoutMarketplaceModuleItemPromoCardBinding.inflate(LayoutInflater.from(view.context), view as ViewGroup, false)
                PromoListItemViewHolder(viewBinding, listener)
            }
            PromoEmptyStateViewHolder.LAYOUT -> {
                val viewBinding = PromoCheckoutMarketplaceModuleItemPromoEmptyBinding.inflate(LayoutInflater.from(view.context), view as ViewGroup, false)
                PromoEmptyStateViewHolder(viewBinding, listener)
            }
            PromoLoadingViewHolder.LAYOUT -> {
                val viewBinding = PromoCheckoutMarketplaceModuleItemLoadingPromoPageBinding.inflate(LayoutInflater.from(view.context), view as ViewGroup, false)
                PromoLoadingViewHolder(viewBinding)
            }
            PromoErrorViewHolder.LAYOUT -> {
                val viewBinding = PromoCheckoutMarketplaceModuleItemErrorPromoPageBinding.inflate(LayoutInflater.from(view.context), view as ViewGroup, false)
                PromoErrorViewHolder(viewBinding, listener)
            }
            PromoTabViewHolder.LAYOUT -> {
                val viewBinding = PromoCheckoutMarketplaceModuleItemPromoTabBinding.inflate(LayoutInflater.from(view.context), view as ViewGroup, false)
                PromoTabViewHolder(viewBinding, listener)
            }
            else -> super.createViewHolder(view, viewType)
        }

    }
}