package com.tokopedia.tokofood.purchase.purchasepage.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.databinding.*
import com.tokopedia.tokofood.purchase.purchasepage.presentation.TokoFoodPurchaseActionListener
import com.tokopedia.tokofood.purchase.purchasepage.presentation.uimodel.*
import com.tokopedia.tokofood.purchase.purchasepage.presentation.viewholder.*

class TokoFoodPurchaseAdapterTypeFactory(private val listener: TokoFoodPurchaseActionListener)
    : BaseAdapterTypeFactory(), TokoFoodPurchaseTypeFactory {

    override fun type(uiModel: TokoFoodPurchaseAccordionUiModel): Int {
        return TokoFoodPurchaseAccordionViewHolder.LAYOUT
    }

    override fun type(uiModel: TokoFoodPurchaseAddressUiModel): Int {
        return TokoFoodPurchaseAddressViewHolder.LAYOUT
    }

    override fun type(uiModel: TokoFoodPurchaseDividerUiModel): Int {
        return TokoFoodPurchaseDividerViewHolder.LAYOUT
    }

    override fun type(uiModel: TokoFoodPurchaseGeneralTickerUiModel): Int {
        return TokoFoodPurchaseGeneralTickerViewHolder.LAYOUT
    }

    override fun type(uiModel: TokoFoodPurchaseProductListHeaderUiModel): Int {
        return TokoFoodPurchaseProductListHeaderViewHolder.LAYOUT
    }

    override fun type(uiModel: TokoFoodPurchaseProductUiModel): Int {
        return TokoFoodPurchaseProductViewHolder.LAYOUT
    }

    override fun type(uiModel: TokoFoodPurchaseProductUnavailableReasonUiModel): Int {
        return TokoFoodPurchaseProductUnavailableReasonViewHolder.LAYOUT
    }

    override fun type(uiModel: TokoFoodPurchasePromoUiModel): Int {
        return TokoFoodPurchasePromoViewHolder.LAYOUT
    }

    override fun type(uiModel: TokoFoodPurchaseShippingUiModel): Int {
        return TokoFoodPurchaseShippingViewHolder.LAYOUT
    }

    override fun type(uiModel: TokoFoodPurchaseSummaryTransactionUiModel): Int {
        return TokoFoodPurchaseSummaryTransactionViewHolder.LAYOUT
    }

    override fun type(uiModel: TokoFoodPurchaseTickerErrorShopLevelUiModel): Int {
        return TokoFoodPurchaseTickerErrorShopLevelViewHolder.LAYOUT
    }

    override fun type(uiModel: TokoFoodPurchaseTotalAmountUiModel): Int {
        return TokoFoodPurchaseTotalAmountViewHolder.LAYOUT
    }

    override fun type(uiModel: LoadingModel): Int {
        return TokoFoodPurchaseLoadingViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, viewType: Int): AbstractViewHolder<out Visitable<*>> {
        return when (viewType) {
            TokoFoodPurchaseAccordionViewHolder.LAYOUT -> {
                val viewBinding = ItemPurchaseAccordionBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
                TokoFoodPurchaseAccordionViewHolder(viewBinding, listener)
            }
            TokoFoodPurchaseAddressViewHolder.LAYOUT -> {
                val viewBinding = ItemPurchaseAddressBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
                TokoFoodPurchaseAddressViewHolder(viewBinding, listener)
            }
            TokoFoodPurchaseDividerViewHolder.LAYOUT -> {
                val viewBinding = ItemPurchaseDividerBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
                TokoFoodPurchaseDividerViewHolder(viewBinding, listener)
            }
            TokoFoodPurchaseGeneralTickerViewHolder.LAYOUT -> {
                val viewBinding = ItemPurchaseGeneralTickerBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
                TokoFoodPurchaseGeneralTickerViewHolder(viewBinding, listener)
            }
            TokoFoodPurchaseProductListHeaderViewHolder.LAYOUT -> {
                val viewBinding = ItemPurchaseProductListHeaderBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
                TokoFoodPurchaseProductListHeaderViewHolder(viewBinding, listener)
            }
            TokoFoodPurchaseProductViewHolder.LAYOUT -> {
                val viewBinding = ItemPurchaseProductBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
                TokoFoodPurchaseProductViewHolder(viewBinding, listener)
            }
            TokoFoodPurchaseProductUnavailableReasonViewHolder.LAYOUT -> {
                val viewBinding = ItemPurchaseProductUnavailableReasonBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
                TokoFoodPurchaseProductUnavailableReasonViewHolder(viewBinding, listener)
            }
            TokoFoodPurchasePromoViewHolder.LAYOUT -> {
                val viewBinding = ItemPurchasePromoBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
                TokoFoodPurchasePromoViewHolder(viewBinding, listener)
            }
            TokoFoodPurchaseShippingViewHolder.LAYOUT -> {
                val viewBinding = ItemPurchaseShippingBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
                TokoFoodPurchaseShippingViewHolder(viewBinding, listener)
            }
            TokoFoodPurchaseSummaryTransactionViewHolder.LAYOUT -> {
                val viewBinding = ItemPurchaseSummaryTransactionBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
                TokoFoodPurchaseSummaryTransactionViewHolder(viewBinding, listener)
            }
            TokoFoodPurchaseTickerErrorShopLevelViewHolder.LAYOUT -> {
                val viewBinding = ItemPurchaseTickerErrorShopLevelBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
                TokoFoodPurchaseTickerErrorShopLevelViewHolder(viewBinding, listener)
            }
            TokoFoodPurchaseTotalAmountViewHolder.LAYOUT -> {
                val viewBinding = ItemPurchaseTotalAmountBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
                TokoFoodPurchaseTotalAmountViewHolder(viewBinding, listener)
            }
            TokoFoodPurchaseLoadingViewHolder.LAYOUT -> {
                val viewBinding = ItemPurchaseLoadingBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
                TokoFoodPurchaseLoadingViewHolder(viewBinding)
            }
            else -> super.createViewHolder(parent, viewType)
        }
    }
}