package com.tokopedia.tokofood.feature.purchase.promopage.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.databinding.*
import com.tokopedia.tokofood.feature.purchase.promopage.presentation.TokoFoodPromoActionListener
import com.tokopedia.tokofood.feature.purchase.promopage.presentation.uimodel.TokoFoodPromoEligibilityHeaderUiModel
import com.tokopedia.tokofood.feature.purchase.promopage.presentation.uimodel.TokoFoodPromoHeaderUiModel
import com.tokopedia.tokofood.feature.purchase.promopage.presentation.uimodel.TokoFoodPromoItemUiModel
import com.tokopedia.tokofood.feature.purchase.promopage.presentation.uimodel.TokoFoodPromoTickerUiModel
import com.tokopedia.tokofood.feature.purchase.promopage.presentation.viewholder.*
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.viewholder.TokoFoodPurchaseLoadingViewHolder

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
            TokoFoodPromoTickerViewHolder.LAYOUT -> {
                val viewBinding = ItemTokofoodPromoTickerBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
                TokoFoodPromoTickerViewHolder(viewBinding, listener)
            }
            TokoFoodPurchaseLoadingViewHolder.LAYOUT -> {
                val viewBinding = ItemTokofoodPromoLoadingBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
                TokoFoodPromoLoadingViewHolder(viewBinding)
            }
            else -> super.createViewHolder(parent, viewType)
        }
    }
}