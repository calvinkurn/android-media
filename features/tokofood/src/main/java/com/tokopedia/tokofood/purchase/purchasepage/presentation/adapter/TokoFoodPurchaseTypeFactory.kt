package com.tokopedia.tokofood.purchase.purchasepage.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.purchase.purchasepage.presentation.uimodel.*

interface TokoFoodPurchaseTypeFactory {

    fun type(uiModel: TokoFoodPurchaseAccordionUiModel): Int

    fun type(uiModel: TokoFoodPurchaseAddressUiModel): Int

    fun type(uiModel: TokoFoodPurchaseDividerUiModel): Int

    fun type(uiModel: TokoFoodPurchaseGeneralTickerUiModel): Int

    fun type(uiModel: TokoFoodPurchaseProductListHeaderUiModel): Int

    fun type(uiModel: TokoFoodPurchaseProductUiModel): Int

    fun type(uiModel: TokoFoodPurchaseProductUnavailableReasonUiModel): Int

    fun type(uiModel: TokoFoodPurchasePromoUiModel): Int

    fun type(uiModel: TokoFoodPurchaseShippingUiModel): Int

    fun type(uiModel: TokoFoodPurchaseSummaryTransactionUiModel): Int

    fun type(uiModel: TokoFoodPurchaseTickerErrorShopLevelUiModel): Int

    fun type(uiModel: TokoFoodPurchaseTotalAmountUiModel): Int

    fun createViewHolder(parent: View, viewType: Int): AbstractViewHolder<*>

}