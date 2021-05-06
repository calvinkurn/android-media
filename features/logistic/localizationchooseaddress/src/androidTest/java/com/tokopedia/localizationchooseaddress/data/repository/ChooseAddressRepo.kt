package com.tokopedia.localizationchooseaddress.data.repository

import com.tokopedia.localizationchooseaddress.domain.response.GetChosenAddressListQglResponse
import com.tokopedia.localizationchooseaddress.domain.response.GetDefaultChosenAddressGqlResponse
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressQglResponse
import com.tokopedia.localizationchooseaddress.domain.response.SetStateChosenAddressQqlResponse

interface ChooseAddressRepo {
    suspend fun getChosenAddressList(): GetChosenAddressListQglResponse
    suspend fun setStateChosenAddress(status: Int, addressId: String, receiverName: String, addressName: String,
                                      latitude: String, longitude: String, districtId: String, postalCode: String): SetStateChosenAddressQqlResponse
    suspend fun getStateChosenAddress(source: String): GetStateChosenAddressQglResponse
    suspend fun getDefaultChosenAddress(latLong: String?, source: String): GetDefaultChosenAddressGqlResponse

}