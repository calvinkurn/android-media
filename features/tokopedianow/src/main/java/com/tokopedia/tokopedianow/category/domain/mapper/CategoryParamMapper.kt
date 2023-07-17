package com.tokopedia.tokopedianow.category.domain.mapper

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.network.authentication.AuthHelper
import com.tokopedia.tokopedianow.common.domain.mapper.AddressMapper
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class CategoryParamMapper @Inject constructor(
    private val userSession: UserSessionInterface,
    private val addressData: TokoNowLocalAddress
) {

    fun createRequestParams(): MutableMap<String?, Any?> {
        val addressData = addressData.getAddressData()
        val queryParam = mutableMapOf<String?, Any?>()

        if (addressData.city_id.isNotEmpty()) {
            queryParam[SearchApiConst.USER_CITY_ID] = addressData.city_id
        }
        if (addressData.address_id.isNotEmpty()) {
            queryParam[SearchApiConst.USER_ADDRESS_ID] = addressData.address_id
        }
        if (addressData.district_id.isNotEmpty()) {
            queryParam[SearchApiConst.USER_DISTRICT_ID] = addressData.district_id
        }
        if (addressData.lat.isNotEmpty()) {
            queryParam[SearchApiConst.USER_LAT] = addressData.lat
        }
        if (addressData.long.isNotEmpty()) {
            queryParam[SearchApiConst.USER_LONG] = addressData.long
        }
        if (addressData.postal_code.isNotEmpty()) {
            queryParam[SearchApiConst.USER_POST_CODE] = addressData.postal_code
        }
        if (addressData.warehouses.isNotEmpty()) {
            queryParam[SearchApiConst.WAREHOUSES] =
                AddressMapper.mapToWarehouses(addressData)
        }

        queryParam[SearchApiConst.DEVICE] = SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE
        queryParam[SearchApiConst.UNIQUE_ID] = getUniqueId()

        return queryParam
    }

    private fun getUniqueId() =
        if (userSession.isLoggedIn) {
            AuthHelper.getMD5Hash(userSession.userId)
        } else {
            AuthHelper.getMD5Hash(userSession.deviceId)
        }
}
