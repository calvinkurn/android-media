package com.tokopedia.fakeresponse.data.parsers.rules

interface GqlParserRule {
    fun getIgnoredKeywords() = arrayListOf<String>("operationName","null","query","n", "mutation")
    fun parse(rawQuery: String): String
}