package com.tokopedia.checkout.revamp.view.uimodel

import com.tokopedia.checkout.domain.model.cartshipmentform.Donation
import com.tokopedia.checkout.view.uimodel.CrossSellModel
import com.tokopedia.checkout.view.uimodel.EgoldAttributeModel

private const val E_GOLD_CATEGORY_NAME = "egold"
private const val DONATION_CATEGORY_NAME = "donation"

data class CheckoutCrossSellGroupModel(
    override val cartStringGroup: String = "",
    val crossSellList: List<CheckoutCrossSellItem> = emptyList(),
    var shouldTriggerScrollInteraction: Boolean = true
) : CheckoutItem

sealed class CheckoutCrossSellItem {
    var hasSentImpressionAnalytics: Boolean = false
    var productId: String = "0"

    abstract fun getCategoryName(): String

    open fun getCrossSellProductId(): String {
        return productId
    }
}

data class CheckoutCrossSellModel(
    var crossSellModel: CrossSellModel = CrossSellModel(),
    var isChecked: Boolean = false,
    var isEnabled: Boolean = true,
    var index: Int = -1
) : CheckoutCrossSellItem() {

    override fun getCategoryName(): String {
        return crossSellModel.orderSummary.title
    }

    override fun getCrossSellProductId(): String {
        return crossSellModel.id
    }
}

data class CheckoutEgoldModel(
    val egoldAttributeModel: EgoldAttributeModel,
    val isChecked: Boolean = false,
    val buyEgoldValue: Long = 0
) : CheckoutCrossSellItem() {

    override fun getCategoryName(): String {
        return E_GOLD_CATEGORY_NAME
    }
}

data class CheckoutDonationModel(
    var donation: Donation = Donation(),
    var isChecked: Boolean = false,
    var isEnabled: Boolean = true
) : CheckoutCrossSellItem() {

    override fun getCategoryName(): String {
        return DONATION_CATEGORY_NAME
    }
}
