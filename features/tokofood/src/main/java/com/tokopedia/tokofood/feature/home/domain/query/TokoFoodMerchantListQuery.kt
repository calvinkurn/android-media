package com.tokopedia.tokofood.feature.home.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokofood.common.util.TokofoodExt.getLocalTimeZone
import com.tokopedia.tokofood.feature.home.domain.data.TokoFoodParamFilterMerchant
import com.tokopedia.tokofood.feature.home.domain.data.TokoFoodParamSort

object TokoFoodMerchantListQuery: GqlQueryInterface {

    private const val OPERATION_NAME = "getMerchantList"
    private val QUERY = """
        query $OPERATION_NAME(${'$'}latlong: String!, ${'$'}timezone: String, ${'$'}filter: tokofoodParamFilterMerchantList, ${'$'}sortBy: tokofoodParamSort, ${'$'}pageKey: String, ${'$'}limit: Int) {
          tokofoodGetMerchantList(latlong: ${'$'}latlong, timezone: ${'$'}timezone, filter: ${'$'}filter, sortBy: ${'$'}sortBy, pageKey: ${'$'}pageKey, limit: ${'$'}limit) {
            nextPageKey
            merchants {
              id
              applink
              brandID
              name
              imageURL
              priceLevel {
                icon
                fareCount
              }
              merchantCategories
              rating
              ratingFmt
              distance
              distanceFmt
              etaFmt
              promo
              hasBranch
              isClosed
              addressLocality
            }
          }
        }
    """.trimIndent()

    private const val LAT_LONG_KEY = "latlong"
    private const val TIMEZONE_KEY = "timezone"
    private const val FILTER_KEY = "filter"
    private const val SORT_BY_KEY = "sortBy"
    private const val PAGE_KEY = "pageKey"
    private const val LIMIT_KEY = "limit"

    private const val LIMIT = 10
    private val TIMEZONE = getLocalTimeZone()

    @JvmStatic
    fun createRequestParams(localCacheModel: LocalCacheModel?, option: Int = 0, brandUId: String = "",
                            sortBy: Int = 0, orderById: Int = 0, cuisine: String = "", pageKey: String = "") = HashMap<String, Any>().apply {
        put(LAT_LONG_KEY, mapLocation(localCacheModel))
        put(PAGE_KEY, pageKey)
        put(TIMEZONE_KEY, TIMEZONE)
        put(LIMIT_KEY, LIMIT)
        put(FILTER_KEY, mapFilter(option, brandUId, cuisine))
        put(SORT_BY_KEY, mapSort(sortBy, orderById))
    }

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
    override fun getQuery(): String = QUERY
    override fun getTopOperationName(): String = OPERATION_NAME

    private fun mapLocation(localCacheModel: LocalCacheModel?): String {
        val stringBuilder = StringBuilder()
        if (!localCacheModel?.lat.isNullOrEmpty() && !localCacheModel?.long.isNullOrEmpty()){
            stringBuilder.append(localCacheModel?.lat)
            stringBuilder.append(",")
            stringBuilder.append(localCacheModel?.long)
        }
        return stringBuilder.toString()
    }

    private fun mapFilter(option: Int, brandUId: String, cuisine: String): TokoFoodParamFilterMerchant {
        return TokoFoodParamFilterMerchant().apply {
            this.option = option
            this.brandUId = brandUId
            if (cuisine.isNotEmpty()) {
                this.cuisines = listOf(cuisine)
            }
        }
    }

    private fun mapSort(sortBy: Int, orderById: Int): TokoFoodParamSort {
        return TokoFoodParamSort().apply {
            this.sortBy = sortBy
            this.orderBy = orderById
        }
    }
}