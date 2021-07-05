package com.tokopedia.tradein.model

import com.tokopedia.logisticCommon.data.entity.address.Token

data class AddressResult(val defaultAddress: MoneyInKeroGetAddressResponse.ResponseData.KeroGetAddress.Data?,
                         val token: Token?)