package com.tokopedia.tokofood.purchase.purchasepage.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.purchase.purchasepage.presentation.uimodel.*

interface TokoFoodPurchaseTypeFactory {

    fun type(uiModel: TokoFoodPurchaseAccordionTokoFoodPurchaseUiModel): Int

    fun type(uiModel: TokoFoodPurchaseAddressTokoFoodPurchaseUiModel): Int

    fun type(uiModel: TokoFoodPurchaseDividerTokoFoodPurchaseUiModel): Int

    fun type(uiModel: TokoFoodPurchaseGeneralTickerTokoFoodPurchaseUiModel): Int

    fun type(uiModel: TokoFoodPurchaseProductListHeaderTokoFoodPurchaseUiModel): Int

    fun type(uiModel: TokoFoodPurchaseProductTokoFoodPurchaseUiModel): Int

    fun type(uiModel: TokoFoodPurchaseProductUnavailableReasonTokoFoodPurchaseUiModel): Int

    fun type(uiModel: TokoFoodPurchasePromoTokoFoodPurchaseUiModel): Int

    fun type(uiModel: TokoFoodPurchaseShippingTokoFoodPurchaseUiModel): Int

    fun type(uiModel: TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel): Int

    fun type(uiModel: TokoFoodPurchaseTickerErrorShopLevelTokoFoodPurchaseUiModel): Int

    fun type(uiModel: TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel): Int

    fun createViewHolder(parent: View, viewType: Int): AbstractViewHolder<*>

}