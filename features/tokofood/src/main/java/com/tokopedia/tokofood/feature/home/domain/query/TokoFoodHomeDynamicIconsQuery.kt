package com.tokopedia.tokofood.feature.home.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object TokoFoodHomeDynamicIconsQuery: GqlQueryInterface {

    private const val OPERATION_NAME = "homeIcon"
    private val QUERY = """
        query $OPERATION_NAME(${'$'}param: String) { 
            dynamicHomeIcon { 
                dynamicIcon(param:${'$'}param) { 
                    id 
                    galaxy_attribution 
                    persona 
                    brand_id 
                    category_persona 
                    name 
                    url 
                    imageUrl 
                    applinks 
                    bu_identifier 
                    campaignCode 
                    withBackground 
                    } 
                } 
           }
    """.trimIndent()

    private const val PARAM_KEY = "param"

    @JvmStatic
    fun createRequestParams(param: String) =
        HashMap<String, Any>().apply {
            put(PARAM_KEY, param)
        }

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

    override fun getQuery(): String = QUERY

    override fun getTopOperationName(): String = OPERATION_NAME
}