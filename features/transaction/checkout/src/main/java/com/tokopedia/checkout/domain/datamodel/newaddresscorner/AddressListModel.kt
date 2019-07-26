package com.tokopedia.checkout.domain.datamodel.newaddresscorner

import com.tokopedia.logisticdata.data.entity.address.Token
import com.tokopedia.logisticcart.domain.shipping.RecipientAddressModel

/**
 * Created by fajarnuha on 2019-05-24.
 */
data class AddressListModel(
        var token: Token? = null,
        var hasNext: Boolean? = null,
        var listAddress: List<RecipientAddressModel> = emptyList())