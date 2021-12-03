package com.tokopedia.test.application.graphql

import com.tokopedia.graphql.data.model.GraphqlRequest

object GqlQueryParser {

    fun parse(list: List<GraphqlRequest>): List<String> {
        return list.map {
            it.query
        }
    }
}