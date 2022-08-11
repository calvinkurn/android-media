package com.tokopedia.telemetry.network.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

class TelemetryQuery : GqlQueryInterface {
    override fun getOperationNameList(): List<String> {
        return listOf("subDvcTl")
    }

    override fun getTopOperationName(): String {
        return "subDvcTl"
    }

    override fun getQuery(): String {
        return GQL_QUERY
    }

    companion object {
        const val GQL_QUERY =
            "mutation subDvcTl(\$input: SubDvcTlRequest!){ subDvcTl(input: \$input) { is_error data { error_message } } }"
    }
}
