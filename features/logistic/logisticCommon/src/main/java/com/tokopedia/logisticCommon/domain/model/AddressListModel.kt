package com.tokopedia.logisticCommon.domain.model

import com.tokopedia.logisticCommon.data.entity.address.PageInfoDataModel
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.address.Token

/**
 * Created by fajarnuha on 2019-05-24.
 */
data class AddressListModel(
        var token: Token? = null,
        var hasNext: Boolean? = null,
        var listAddress: List<RecipientAddressModel> = emptyList(),
        var pageInfo: PageInfoDataModel? = null)