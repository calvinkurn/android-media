package com.tokopedia.fakeresponse.data.parsers.rules

class NestedQueryParserRule : GqlParserRule {
    override fun parse(rawQuery: String): String {
        val regex = Regex("[\\w]+")
        val result = regex.findAll(rawQuery)
        if (result.count() > 1) {
            return result.iterator().next().value.trim()
        }
        return ""
    }
}