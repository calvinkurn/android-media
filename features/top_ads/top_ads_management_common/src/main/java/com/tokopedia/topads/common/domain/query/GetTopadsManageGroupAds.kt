package com.tokopedia.topads.common.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object GetTopadsManageGroupAds : GqlQueryInterface{

    private const val OPERATION_NAME = "topadsManageGroupAds"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """mutation $OPERATION_NAME (${ '$' } input : TopadsManageGroupAdsInput !){
            $OPERATION_NAME(input:${ '$' } input){
            groupResponse {
                data {
                    id
                    resourceUrl
                }
                errors {
                    code
                    detail
                    title
                }
            }
        }
        }"""
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}
