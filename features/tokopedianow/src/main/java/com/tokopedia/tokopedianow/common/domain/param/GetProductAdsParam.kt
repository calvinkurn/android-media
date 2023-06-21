package com.tokopedia.tokopedianow.common.domain.param

data class GetProductAdsParam(
    val query: String = "",
    val warehouseIds: String,
    val src: String,
    val page: Int = 1,
    val userId: String
) {

    companion object {
        const val SRC_DIRECTORY_TOKONOW = "directory_tokonow"
        const val SRC_SEARCH_TOKONOW = "search_tokonow"
        const val DEVICE_ANDROID = "android"

        private const val PARAM_QUERY = "q"
        private const val PARAM_SRC = "src"
        private const val PARAM_ITEM = "item"
        private const val PARAM_PAGE = "page"
        private const val PARAM_USER_WAREHOUSE_ID = "user_warehouseId"
        private const val PARAM_DEVICE = "device"
        private const val PARAM_USER_ID = "userId"

        private const val PER_PAGE_ITEM = 20
    }

    fun generateQueryParams(): String {
        val stringBuilder = StringBuilder()
        val params = mutableMapOf<String, Any>().apply {
            if (query.isNotBlank()) {
                put(PARAM_QUERY, query)
            }
            put(PARAM_SRC, src)
            put(PARAM_PAGE, page)
            put(PARAM_USER_WAREHOUSE_ID, warehouseIds)
            put(PARAM_ITEM, PER_PAGE_ITEM)
            put(PARAM_DEVICE, DEVICE_ANDROID)
            put(PARAM_USER_ID, userId)
        }

        for ((key, value) in params) {
            if (stringBuilder.isNotBlank()) {
                stringBuilder.append("&")
            }
            stringBuilder.append("$key=$value")
        }

        return stringBuilder.toString()
    }
}
