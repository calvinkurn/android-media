package com.tokopedia.fakeresponse.data.parsers

import com.tokopedia.fakeresponse.data.parsers.rules.MapQueryParserRule
import com.tokopedia.fakeresponse.data.parsers.rules.NestedQueryParserRule
import com.tokopedia.fakeresponse.data.parsers.rules.SimpleParserRule

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