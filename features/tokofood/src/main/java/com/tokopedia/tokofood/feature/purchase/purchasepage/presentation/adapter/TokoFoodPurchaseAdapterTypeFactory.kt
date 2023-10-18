package com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.databinding.ItemPurchaseAccordionBinding
import com.tokopedia.tokofood.databinding.ItemPurchaseAddressBinding
import com.tokopedia.tokofood.databinding.ItemPurchaseDividerBinding
import com.tokopedia.tokofood.databinding.ItemPurchaseGeneralTickerBinding
import com.tokopedia.tokofood.databinding.ItemPurchaseLoadingBinding
import com.tokopedia.tokofood.databinding.ItemPurchaseProductBinding
import com.tokopedia.tokofood.databinding.ItemPurchaseProductListHeaderBinding
import com.tokopedia.tokofood.databinding.ItemPurchaseProductUnavailableReasonBinding
import com.tokopedia.tokofood.databinding.ItemPurchasePromoBinding
import com.tokopedia.tokofood.databinding.ItemPurchaseShippingBinding
import com.tokopedia.tokofood.databinding.ItemPurchaseSummaryTransactionBinding
import com.tokopedia.tokofood.databinding.ItemPurchaseTickerErrorShopLevelBinding
import com.tokopedia.tokofood.databinding.ItemPurchaseTotalAmountBinding
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.TokoFoodPurchaseActionListener
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseAccordionTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseAddressTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseDividerTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseGeneralTickerTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseProductListHeaderTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseProductTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseProductUnavailableReasonTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchasePromoTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseShippingTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseTickerErrorShopLevelTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.viewholder.TokoFoodPurchaseAccordionViewHolder
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.viewholder.TokoFoodPurchaseAddressViewHolder
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.viewholder.TokoFoodPurchaseDividerViewHolder
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.viewholder.TokoFoodPurchaseGeneralTickerViewHolder
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.viewholder.TokoFoodPurchaseLoadingViewHolder
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.viewholder.TokoFoodPurchaseProductListHeaderViewHolder
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.viewholder.TokoFoodPurchaseProductUnavailableReasonViewHolder
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.viewholder.TokoFoodPurchaseProductViewHolder
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.viewholder.TokoFoodPurchasePromoViewHolder
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.viewholder.TokoFoodPurchaseShippingViewHolder
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.viewholder.TokoFoodPurchaseSummaryTransactionViewHolder
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.viewholder.TokoFoodPurchaseTickerErrorShopLevelViewHolder
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.viewholder.TokoFoodPurchaseTotalAmountViewHolder

class TokoFoodPurchaseAdapterTypeFactory(private val listener: TokoFoodPurchaseActionListener)
    : BaseAdapterTypeFactory(), TokoFoodPurchaseTypeFactory {

    override fun type(uiModel: TokoFoodPurchaseAccordionTokoFoodPurchaseUiModel): Int {
        return TokoFoodPurchaseAccordionViewHolder.LAYOUT
    }

    override fun type(uiModel: TokoFoodPurchaseAddressTokoFoodPurchaseUiModel): Int {
        return TokoFoodPurchaseAddressViewHolder.LAYOUT
    }

    override fun type(uiModel: TokoFoodPurchaseDividerTokoFoodPurchaseUiModel): Int {
        return TokoFoodPurchaseDividerViewHolder.LAYOUT
    }

    override fun type(uiModel: TokoFoodPurchaseGeneralTickerTokoFoodPurchaseUiModel): Int {
        return TokoFoodPurchaseGeneralTickerViewHolder.LAYOUT
    }

    override fun type(uiModel: TokoFoodPurchaseProductListHeaderTokoFoodPurchaseUiModel): Int {
        return TokoFoodPurchaseProductListHeaderViewHolder.LAYOUT
    }

    override fun type(uiModel: TokoFoodPurchaseProductTokoFoodPurchaseUiModel): Int {
        return TokoFoodPurchaseProductViewHolder.LAYOUT
    }

    override fun type(uiModel: TokoFoodPurchaseProductUnavailableReasonTokoFoodPurchaseUiModel): Int {
        return TokoFoodPurchaseProductUnavailableReasonViewHolder.LAYOUT
    }

    override fun type(uiModel: TokoFoodPurchasePromoTokoFoodPurchaseUiModel): Int {
        return TokoFoodPurchasePromoViewHolder.LAYOUT
    }

    override fun type(uiModel: TokoFoodPurchaseShippingTokoFoodPurchaseUiModel): Int {
        return TokoFoodPurchaseShippingViewHolder.LAYOUT
    }

    override fun type(uiModel: TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel): Int {
        return TokoFoodPurchaseSummaryTransactionViewHolder.LAYOUT
    }

    override fun type(uiModel: TokoFoodPurchaseTickerErrorShopLevelTokoFoodPurchaseUiModel): Int {
        return TokoFoodPurchaseTickerErrorShopLevelViewHolder.LAYOUT
    }

    override fun type(uiModel: TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel): Int {
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
                TokoFoodPurchaseDividerViewHolder(viewBinding)
            }
            TokoFoodPurchaseGeneralTickerViewHolder.LAYOUT -> {
                val viewBinding = ItemPurchaseGeneralTickerBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
                TokoFoodPurchaseGeneralTickerViewHolder(viewBinding)
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
                TokoFoodPurchaseProductUnavailableReasonViewHolder(viewBinding)
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
