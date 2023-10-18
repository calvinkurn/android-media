package com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
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
