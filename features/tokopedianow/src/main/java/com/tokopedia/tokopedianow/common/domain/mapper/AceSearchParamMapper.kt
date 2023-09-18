package com.tokopedia.tokopedianow.common.domain.mapper

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.network.authentication.AuthHelper
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AceSearchParamMapper @Inject constructor(
    private val userSession: UserSessionInterface,
    private val addressData: TokoNowLocalAddress,
) {

    companion object {
        private const val DEFAULT_PAGE = 1
    }

    var uniqueId: String = ""

    fun createRequestParams(
        page: Int? = DEFAULT_PAGE,
        rows: Int = SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS_PROFILE,
        srpPageId: String = "",
        source: String = "",
        sc: String = ""
    ): MutableMap<String?, Any?> {
        val addressData = addressData.getAddressData()
        val queryParams = mutableMapOf<String?, Any?>()

        if (addressData.city_id.isNotEmpty()) {
            queryParams[SearchApiConst.USER_CITY_ID] = addressData.city_id
        }
        if (addressData.address_id.isNotEmpty()) {
            queryParams[SearchApiConst.USER_ADDRESS_ID] = addressData.address_id
        }
        if (addressData.district_id.isNotEmpty()) {
            queryParams[SearchApiConst.USER_DISTRICT_ID] = addressData.district_id
        }
        if (addressData.lat.isNotEmpty()) {
            queryParams[SearchApiConst.USER_LAT] = addressData.lat
        }
        if (addressData.long.isNotEmpty()) {
            queryParams[SearchApiConst.USER_LONG] = addressData.long
        }
        if (addressData.postal_code.isNotEmpty()) {
            queryParams[SearchApiConst.USER_POST_CODE] = addressData.postal_code
        }
        if (addressData.warehouses.isNotEmpty()) {
            queryParams[SearchApiConst.WAREHOUSES] =
                AddressMapper.mapToWarehouses(addressData)
        }

        if(srpPageId.isNotEmpty()) {
            queryParams[SearchApiConst.SRP_PAGE_ID] = srpPageId
        }

        if(sc.isNotEmpty()) {
            queryParams[SearchApiConst.SC] = sc
        }

        if(source.isNotEmpty()) {
            queryParams[SearchApiConst.NAVSOURCE] = source
            queryParams[SearchApiConst.SOURCE] = source
        }

        if (page != null) {
            queryParams[SearchApiConst.PAGE] = page
            queryParams[SearchApiConst.USE_PAGE] = true
        }

        queryParams[SearchApiConst.ROWS] = rows
        queryParams[SearchApiConst.UNIQUE_ID] = getUniqueIdHash()
        queryParams[SearchApiConst.OB] = SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT
        queryParams[SearchApiConst.DEVICE] = SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE

        return queryParams
    }

    private fun getUniqueIdHash() =
        if (uniqueId.isNotEmpty()) {
            uniqueId
        } else if (userSession.isLoggedIn) {
            AuthHelper.getMD5Hash(userSession.userId)
        } else {
            AuthHelper.getMD5Hash(userSession.deviceId)
        }
}
