package com.tokopedia.checkout.view.feature.addressoptions

import com.tokopedia.logisticdata.data.entity.address.Token
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel

/**
 * Created by fajarnuha on 2019-05-24.
 */
data class AddressListModel(
        var token: Token? = null,
        var hasNext: Boolean? = null,
        var listAddress: List<RecipientAddressModel> = emptyList())