package com.rahullohra.fakeresponse.data.parsers.rules

interface GqlParserRule {
    fun parse(rawQuery: String): String
}