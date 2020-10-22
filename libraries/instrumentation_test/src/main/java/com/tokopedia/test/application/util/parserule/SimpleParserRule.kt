package com.tokopedia.test.application.util.parserule

class SimpleParserRule : GqlParserRule {
    override fun parse(rawQuery: String): String {
        val regex = Regex("[\\w]+")
        val result = regex.findAll(rawQuery)
        if (result.count() > 1) {
            val ignoredKeywords = getIgnoredKeywords()
            for (s in result.iterator()) {
                if (!ignoredKeywords.contains(s.value) && s.value.length > 2) {
                    return s.value
                }
            }
        }
        return ""
    }
}