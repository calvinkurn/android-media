package com.tokopedia.topads.common.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object GetTopAdsGetShopWhitelistedFeature : GqlQueryInterface {

    private const val OPERATION_NAME = "topAdsGetShopWhitelistedFeature"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """query $OPERATION_NAME (${ '$' } shopID : String !){
            $OPERATION_NAME(shopID: ${ '$' } shopID){
            data {
                featureID
                featureName
            }
            errors {
                code
                detail
                title
            }
        }
        }""".trimIndent()
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}
