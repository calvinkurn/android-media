package com.tokopedia.test.application.util.parserule

class ParserRuleProvider {

    private val parsers = arrayListOf(
            SimpleParserRule(),
            NestedQueryParserRule(),
            MapQueryParserRule())

    fun parse(rawQuery: String): String {
        var operationName = ""
        parsers.forEach {
            if (operationName.isEmpty()) {
                operationName = it.parse(rawQuery)
            } else {
                return@forEach
            }
        }
        return operationName
    }
}