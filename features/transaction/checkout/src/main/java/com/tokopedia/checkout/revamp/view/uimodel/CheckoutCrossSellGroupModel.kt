package com.tokopedia.checkout.revamp.view.uimodel

import com.tokopedia.checkout.domain.model.cartshipmentform.Donation
import com.tokopedia.checkout.view.uimodel.CrossSellModel
import com.tokopedia.checkout.view.uimodel.EgoldAttributeModel

data class CheckoutCrossSellGroupModel(
    override val cartStringGroup: String = "",
    val crossSellList: List<CheckoutCrossSellItem> = emptyList(),
    var shouldTriggerScrollInteraction: Boolean = true
) : CheckoutItem

sealed interface CheckoutCrossSellItem

data class CheckoutCrossSellModel(
    var crossSellModel: CrossSellModel = CrossSellModel(),
    var isChecked: Boolean = false,
    var isEnabled: Boolean = true,
    var index: Int = -1
) : CheckoutCrossSellItem

data class CheckoutEgoldModel(
    val egoldAttributeModel: EgoldAttributeModel,
    val isChecked: Boolean = false,
    val buyEgoldValue: Long = 0
) : CheckoutCrossSellItem

data class CheckoutDonationModel(
    var donation: Donation = Donation(),
    var isChecked: Boolean = false,
    var isEnabled: Boolean = true
) : CheckoutCrossSellItem
