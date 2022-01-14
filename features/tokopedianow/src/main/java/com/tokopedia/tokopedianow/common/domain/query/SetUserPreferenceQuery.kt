package com.tokopedia.tokopedianow.common.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object SetUserPreferenceQuery : GqlQueryInterface {
    override fun getOperationNameList() = listOf(
        "TokonowSetUserPreference"
    )

    override fun getQuery(): String {
        return "query TokonowSetUserPreference(${'$'}shopID:String!,${'$'}warehouseID:String,${'$'}serviceType:String!,${'$'}warehouses:[WarehouseUserPreference!]!){TokonowSetUserPreference(shopID:${'$'}shopID,warehouseID:${'$'}warehouseID,serviceType:${'$'}serviceType,warehouses:${'$'}warehouses){header{process_time messages reason error_code}data{shopID warehouseID serviceType warehouses{serviceType warehouseID}}}}"
    }

    override fun getTopOperationName() = "TokonowSetUserPreference"
}