package com.tokopedia.tokofood.purchase.promopage.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.databinding.*
import com.tokopedia.tokofood.purchase.promopage.presentation.TokoFoodPromoActionListener
import com.tokopedia.tokofood.purchase.promopage.presentation.uimodel.*
import com.tokopedia.tokofood.purchase.promopage.presentation.viewholder.*
import com.tokopedia.tokofood.purchase.purchasepage.presentation.viewholder.TokoFoodPurchaseLoadingViewHolder

class TokoFoodPromoAdapterTypeFactory(private val listener: TokoFoodPromoActionListener)
    : BaseAdapterTypeFactory(), TokoFoodPromoTypeFactory {

    override fun type(uiModel: TokoFoodPromoEligibilityHeaderUiModel): Int {
        return TokoFoodPromoEligibilityHeaderViewHolder.LAYOUT
    }

    override fun type(uiModel: TokoFoodPromoHeaderUiModel): Int {
        return TokoFoodPromoHeaderViewHolder.LAYOUT
    }

    override fun type(uiModel: TokoFoodPromoItemUiModel): Int {
        return TokoFoodPromoItemViewHolder.LAYOUT
    }

    override fun type(uiModel: TokoFoodPromoTabUiModel): Int {
        return TokoFoodPromoTabViewHolder.LAYOUT
    }

    override fun type(uiModel: TokoFoodPromoTickerUiModel): Int {
        return TokoFoodPromoTickerViewHolder.LAYOUT
    }

    override fun type(uiModel: LoadingModel): Int {
        return TokoFoodPurchaseLoadingViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, viewType: Int): AbstractViewHolder<out Visitable<*>> {
        return when (viewType) {
            TokoFoodPromoEligibilityHeaderViewHolder.LAYOUT -> {
                val viewBinding = ItemTokofoodPromoListAvailabilityHeaderBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
                TokoFoodPromoEligibilityHeaderViewHolder(viewBinding, listener)
            }
            TokoFoodPromoHeaderViewHolder.LAYOUT -> {
                val viewBinding = ItemTokofoodPromoListHeaderBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
                TokoFoodPromoHeaderViewHolder(viewBinding, listener)
            }
            TokoFoodPromoItemViewHolder.LAYOUT -> {
                val viewBinding = ItemTokofoodPromoCardBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
                TokoFoodPromoItemViewHolder(viewBinding, listener)
            }
            TokoFoodPromoTabViewHolder.LAYOUT -> {
                val viewBinding = ItemTokofoodPromoTabBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
                TokoFoodPromoTabViewHolder(viewBinding, listener)
            }
            TokoFoodPromoTickerViewHolder.LAYOUT -> {
                val viewBinding = ItemTokofoodPromoTickerBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
                TokoFoodPromoTickerViewHolder(viewBinding, listener)
            }
            TokoFoodPurchaseLoadingViewHolder.LAYOUT -> {
                val viewBinding = ItemTokofoodPromoLoadingBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
                TokoFoodPurchaseLoadingViewHolder(viewBinding)
            }
            else -> super.createViewHolder(parent, viewType)
        }
    }
}