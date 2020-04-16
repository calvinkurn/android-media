package com.rahullohra.fakeresponse.data.parsers.rules

interface GqlParserRule {
    fun getIgnoredKeywords() = arrayListOf<String>("operationName","null","query","n")
    fun parse(rawQuery: String): String
}