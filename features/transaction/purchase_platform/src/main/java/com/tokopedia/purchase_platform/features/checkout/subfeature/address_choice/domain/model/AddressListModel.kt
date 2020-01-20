package com.tokopedia.purchase_platform.features.checkout.subfeature.address_choice.domain.model

import com.tokopedia.logisticdata.data.entity.address.Token
import com.tokopedia.logisticcart.shipping.model.RecipientAddressModel

/**
 * Created by fajarnuha on 2019-05-24.
 */
data class AddressListModel(
        var token: Token? = null,
        var hasNext: Boolean? = null,
        var listAddress: List<RecipientAddressModel> = emptyList())