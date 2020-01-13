package com.rahullohra.fakeresponse.data.parsers.rules

class SimpleParserRule : GqlParserRule {
    override fun parse(rawQuery: String): String {
        val regex = Regex("[\\w]+")
        val result = regex.findAll(rawQuery)
        if (result.count() > 1)
            return result.iterator().next().value.trim()
        return ""
    }

}