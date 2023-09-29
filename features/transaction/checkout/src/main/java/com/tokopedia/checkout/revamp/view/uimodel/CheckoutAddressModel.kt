package com.tokopedia.checkout.revamp.view.uimodel

import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel

data class CheckoutAddressModel(
    override val cartStringGroup: String = "",
    val recipientAddressModel: RecipientAddressModel
) : CheckoutItem
