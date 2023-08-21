package com.tokopedia.topads.common.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object GetTopadsDashboardDepositsV2 : GqlQueryInterface {

    private const val OPERATION_NAME = "topadsDashboardDepositsV2"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """query $OPERATION_NAME (${ '$' } shop_id : String !) {
            $OPERATION_NAME(shop_id: ${ '$' } shop_id) {
            data {
                amount
                amount_fmt
                amount_html
            }
        }
        }""".trimIndent()
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}
