package com.tokopedia.tokopedianow.common.domain.mapper

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.network.authentication.AuthHelper
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AceSearchParamMapper @Inject constructor(
    private val userSession: UserSessionInterface,
    private val addressData: TokoNowLocalAddress
) {

    companion object {
        private const val DEFAULT_PAGE = 1
    }

    fun createRequestParams(
        page: Int = DEFAULT_PAGE,
        rows: Int = SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS_PROFILE,
        srpPageId: String = String.EMPTY,
        source: String = String.EMPTY
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

        if(source.isNotEmpty()) {
            queryParams[SearchApiConst.NAVSOURCE] = source
            queryParams[SearchApiConst.SOURCE] = source
        }

        queryParams[SearchApiConst.ROWS] = rows
        queryParams[SearchApiConst.PAGE] = page
        queryParams[SearchApiConst.USE_PAGE] = true
        queryParams[SearchApiConst.UNIQUE_ID] = getUniqueId()
        queryParams[SearchApiConst.OB] = SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT
        queryParams[SearchApiConst.DEVICE] = SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE

        return queryParams
    }

    private fun getUniqueId() =
        if (userSession.isLoggedIn) {
            AuthHelper.getMD5Hash(userSession.userId)
        } else {
            AuthHelper.getMD5Hash(userSession.deviceId)
        }
}
