package com.tokopedia.tokopedianow.common.domain.param

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokopedianow.common.domain.mapper.AddressMapper

data class GetProductAdsParam(
    val query: String = "",
    val categoryId: String = "",
    val src: String = "",
    val page: Int = 1,
    val userId: String = "",
    val addressData: LocalCacheModel? = LocalCacheModel()
) {

    companion object {
        const val SRC_DIRECTORY_TOKONOW = "directory_tokonow"
        const val SRC_SEARCH_TOKONOW = "search_tokonow"
        const val DEVICE_ANDROID = "android"
        const val EP_PRODUCT = "product"

        private const val PARAM_QUERY = "q"
        private const val PARAM_SRC = "src"
        private const val PARAM_ITEM = "item"
        private const val PARAM_PAGE = "page"
        private const val PARAM_SC = "sc"
        private const val PARAM_USER_WAREHOUSE_ID = "user_warehouseId"
        private const val PARAM_DEVICE = "device"
        private const val PARAM_USER_ID = "userId"
        private const val PARAM_EP = "ep"

        private const val PARAM_USER_LAT = "user_lat"
        private const val PARAM_USER_LONG = "user_long"
        private const val PARAM_USER_CITY_ID = "user_cityId"
        private const val PARAM_USER_DISTRICT_ID = "user_districtId"
        private const val PARAM_USER_POSTAL_CODE = "user_postCode"
        private const val PARAM_USER_ADDRESS_ID = "user_addressId"

        private const val PER_PAGE_ITEM = 20
    }

    fun generateQueryParams(): MutableMap<String?, Any> {
        val warehouseIds = AddressMapper.mapToWarehouseIds(addressData)

        return mutableMapOf<String?, Any>().apply {
            if (query.isNotBlank()) {
                put(PARAM_QUERY, query)
            }
            if (categoryId.isNotBlank()) {
                put(PARAM_SC, categoryId)
            }
            put(PARAM_SRC, src)
            put(PARAM_PAGE, page)
            put(PARAM_USER_WAREHOUSE_ID, warehouseIds)
            put(PARAM_ITEM, PER_PAGE_ITEM)
            put(PARAM_DEVICE, DEVICE_ANDROID)
            put(PARAM_USER_ID, userId)
            put(PARAM_EP, EP_PRODUCT)

            addressData?.let {
                put(PARAM_USER_LAT, it.lat)
                put(PARAM_USER_LONG, it.long)
                put(PARAM_USER_CITY_ID, it.city_id)
                put(PARAM_USER_DISTRICT_ID, it.district_id)
                put(PARAM_USER_POSTAL_CODE, it.postal_code)
                put(PARAM_USER_ADDRESS_ID, it.address_id)
            }
        }
    }
}
