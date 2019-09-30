package com.tokopedia.tradein.model

import com.tokopedia.logisticaddaddress.domain.model.Token

data class AddressResult(val defaultAddress: MoneyInKeroGetAddressResponse.ResponseData.KeroGetAddress.Data?,
                         val token: Token?)