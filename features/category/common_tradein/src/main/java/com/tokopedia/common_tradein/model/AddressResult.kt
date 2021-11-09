package com.tokopedia.common_tradein.model

import com.tokopedia.logisticCommon.data.entity.address.Token

data class AddressResult(val defaultAddress: MoneyInKeroGetAddressResponse.ResponseData.KeroGetAddress.Data?,
                         val token: Token?)