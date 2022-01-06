package com.tokopedia.tokopedianow.common.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

object SetUserPreferenceQuery : GqlQueryInterface {
    override fun getOperationNameList() = listOf(
        "TokonowSetUserPreference"
    )

    override fun getQuery(): String {
        return "mutation TokonowSetUserPreference(${'$'}shop_id:String,${'$'}warehouse_id:String,${'$'}service_type:String,${'$'}warehouses:[TokonowWarehouse]){TokonowSetUserPreference(shop_id:${'$'}shop_id,warehouse_id:${'$'}warehouse_id,service_type:${'$'}service_type,warehouses:${'$'}warehouses){header{process_time messages reason error_code}data{shop_id warehouse_id service_type warehouses{service_type warehouse_id}}}}"
    }

    override fun getTopOperationName() = "TokonowSetUserPreference"
}