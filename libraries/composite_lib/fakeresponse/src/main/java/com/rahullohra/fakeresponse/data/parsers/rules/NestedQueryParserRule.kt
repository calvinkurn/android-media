package com.rahullohra.fakeresponse.data.parsers.rules

class NestedQueryParserRule : GqlParserRule {
    override fun parse(rawQuery: String): String {
        val regex = Regex("[\\w]+")
        val result = regex.findAll(rawQuery)
        if (result.count() > 1) {
            var count = 0
            for (item in result.iterator()) {
                count = +1
                if(count > 1){
                    return item.value.trim()
                }
            }
        }
        return ""
    }

}