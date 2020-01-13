package com.rahullohra.fakeresponse.data.parsers

import com.rahullohra.fakeresponse.data.parsers.rules.GqlParserRule
import com.rahullohra.fakeresponse.data.parsers.rules.MapQueryParserRule
import com.rahullohra.fakeresponse.data.parsers.rules.NestedQueryParserRule
import com.rahullohra.fakeresponse.data.parsers.rules.SimpleParserRule

class ParserFactory {

    fun getSimpleParser(): GqlParserRule {
        return SimpleParserRule()
    }

    fun getNestedQueryParser(): GqlParserRule {
        return NestedQueryParserRule()
    }

    fun getMappedParser(): GqlParserRule {
        return MapQueryParserRule()
    }

}