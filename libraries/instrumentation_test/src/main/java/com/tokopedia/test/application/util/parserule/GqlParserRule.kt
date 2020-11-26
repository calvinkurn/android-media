package com.tokopedia.test.application.util.parserule

interface GqlParserRule {
    fun getIgnoredKeywords() = arrayListOf<String>("operationName","null","query","n", "mutation")
    fun parse(rawQuery: String): String
}