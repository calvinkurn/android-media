package com.tokopedia.tokofood.home.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokofood.home.domain.mapper.TokoFoodParamMapper

object TokoFoodHomeTickerQuery: GqlQueryInterface {

    private const val OPERATION_NAME = "getTicker"
    private val QUERY = """
        query $OPERATION_NAME(${'$'}page: String, ${'$'}location: String){
            ticker {
                tickers (page:${'$'}page, location:${'$'}location) {
                  id
                  title
                  message
                  color
                  layout
                }
            }
        }
    """.trimIndent()

    private const val PARAM_PAGE = "page"
    private const val PARAM_LOCATION = "location"
    private const val PARAM_TOKOFOOD = "tokofood"

    @JvmStatic
    fun createRequestParams(localCacheModel: LocalCacheModel?) =
        HashMap<String, Any>().apply {
            put(PARAM_PAGE, PARAM_TOKOFOOD)
            put(
                PARAM_LOCATION,
                TokoFoodParamMapper.mapLocation(localCacheModel)
            )
        }

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
    override fun getQuery(): String = QUERY
    override fun getTopOperationName(): String = OPERATION_NAME
}