package com.tokopedia.topads.common.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object GetTopadsManagePromoGroupProduct : GqlQueryInterface {

    private const val OPERATION_NAME = "topadsManagePromoGroupProduct"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """mutation $OPERATION_NAME (${ '$' } input : TopadsManagePromoGroupProductInput !){
            $OPERATION_NAME(input: ${ '$' } input){
            groupResponse {
                data {
                    id
                }
                errors {
                    code
                    detail
                    title
                }
            }
            keywordResponse {
                data {
                    id
                }
                errors {
                    code
                    detail
                    title
                }
            }
        }
        }""".trimIndent()
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}
