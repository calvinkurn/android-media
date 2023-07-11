package com.tokopedia.checkout.revamp.view.uimodel

import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel

data class CheckoutAddressModel(
    val recipientAddressModel: RecipientAddressModel,
    override val cartStringGroup: String = ""
) : CheckoutItem
