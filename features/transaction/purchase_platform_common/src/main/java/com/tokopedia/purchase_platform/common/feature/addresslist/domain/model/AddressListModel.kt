package com.tokopedia.purchase_platform.common.feature.addresslist.domain.model

import com.tokopedia.logisticdata.data.entity.address.Token
import com.tokopedia.logisticdata.data.entity.address.RecipientAddressModel

/**
 * Created by fajarnuha on 2019-05-24.
 */
data class AddressListModel(
        var token: Token? = null,
        var hasNext: Boolean? = null,
        var listAddress: List<RecipientAddressModel> = emptyList())