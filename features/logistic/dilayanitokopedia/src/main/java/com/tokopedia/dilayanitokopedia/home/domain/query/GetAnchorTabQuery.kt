package com.tokopedia.dilayanitokopedia.home.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

/**
 * Created by irpan on 10/01/23.
 */
internal object GetAnchorTabQuery : GqlQueryInterface {

    private const val OPERATION_NAME = "getHomeIconV2"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """
            query $OPERATION_NAME(${'$'}param: String, ${'$'}location: String) {
              getHomeIconV2(param: ${'$'}param, location: ${'$'}location) {
                icons {
                  id
                  url
                  name
                  page
                  persona
                  brandID
                  applinks
                  imageUrl
                  buIdentifier
                  campaignCode
                  withBackground
                  categoryPersona
                  galaxyAttribution
                  feParam
                }
              }
            }

        """.trimIndent()
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}
