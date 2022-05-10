package com.tokopedia.tokofood.home.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object TokoFoodHomeDynamicIconsQuery: GqlQueryInterface {

    private const val OPERATION_NAME = "homeIcon"
    private val QUERY = """
        query $OPERATION_NAME(${'$'}param: String,${'$'}location: String) { 
            dynamicHomeIcon { 
                dynamicIcon(param:${'$'}param, location:${'$'}location) { 
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
    private const val LOCATION_KEY = "location"

    @JvmStatic
    fun createRequestParams() =
        HashMap<String, Any>().apply {
            val dummyParam = ""
            val dummyLocation = ""
            put(PARAM_KEY, dummyParam)
            put(LOCATION_KEY, dummyLocation)
        }

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

    override fun getQuery(): String = QUERY

    override fun getTopOperationName(): String = OPERATION_NAME
}