package com.tokopedia.tokopedianow.repurchase.domain.param

import com.tokopedia.tokopedianow.common.domain.model.WarehouseData

data class GetRepurchaseProductListParam(
    val warehouses: List<WarehouseData>,
    val sort: Int,
    val dateStart: String? = null,
    val dateEnd: String? = null,
    val totalScan: Int,
    val catIds: List<String>? = null,
    val page: Int
) {

    companion object {
        private const val PARAM_SORT = "sort"
        private const val PARAM_DATE_START = "dateStart"
        private const val PARAM_DATE_END = "dateEnd"
        private const val PARAM_TOTAL_SCAN = "totalScan"
        private const val PARAM_PAGE = "page"
        private const val PARAM_CAT_IDS = "catIDs"
        private const val PARAM_DELIMITER = "&"
    }

    fun generateQuery(): String {
        val queryParam = StringBuilder()
        val paramMap = mutableMapOf<String, Any>()

        paramMap[PARAM_SORT] = sort

        dateStart?.let {
            paramMap[PARAM_DATE_START] = it
        }

        dateEnd?.let {
            paramMap[PARAM_DATE_END] = it
        }

        paramMap[PARAM_TOTAL_SCAN] = totalScan
        paramMap[PARAM_PAGE] = page

        catIds?.let {
            val catIdsParam = "'${it.joinToString(",")}'"
            paramMap[PARAM_CAT_IDS] = catIdsParam
        }

        val paramEntries = paramMap.entries.iterator()
        queryParam.append(paramEntries.next())

        while (paramEntries.hasNext()) {
            queryParam.append(PARAM_DELIMITER)
            queryParam.append(paramEntries.next())
        }

        return queryParam.toString()
    }
}
