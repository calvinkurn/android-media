package com.tokopedia.test.application.graphql

import com.tokopedia.graphql.data.model.GraphqlRequest
import java.util.*

object GqlQueryParser {

    val QUERY_PATTERN = Regex("""(\w+)\s*(\(.+\))""")

    /**
     * A utils method to parse query from graphql request(s). Support multiple request and
     * returns list of String accordingly
     *
     * Example in Fake GraphqlRepository class (single request):
     * return when(GqlQueryParser.parse(requests).first()) {
     *      "queryA" -> GqlMockUtil.createSuccessResponse(mockJson)
     *      "queryB" -> ....
     *      ...
     * }
     *
     * Example in Fake GraphqlRepository class (multiple requests):
     * return when(GqlQueryParser.parse(requests).joinToString(",")) {
     *      "queryA,queryB" -> mockedGraphqlResponse
     *      ...
     * }
     * */
    fun parse(list: List<GraphqlRequest>): List<String> {
        val result = mutableListOf<String>()
        list.forEach {
            result += parse(it.query)
        }
        return result
    }

    fun parse(query: String): List<String> {
        val result = mutableMapOf<String, Int>()
        val bracketStack = Stack<Int>()

        for ((i, char) in query.withIndex()) {
            when (char) {
                '{' -> {
                    val open = if (bracketStack.isEmpty()) 1 else bracketStack.peek()
                    val str = query.substring(open, i)
                    result.put(str, bracketStack.size)
                    bracketStack.push(i + 1)
                }
                '}' -> {
                    bracketStack.pop()
                    if (bracketStack.size > 0) {
                        bracketStack.pop()
                        bracketStack.push(i + 1)
                    }
                }
            }
        }
        return result.filterValues { it == 1 }
            .map {
                val s = it.key.trim()
                QUERY_PATTERN.find(s)!!.groupValues[1]
            }
    }
}