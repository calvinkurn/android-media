package com.tokopedia.home.beranda.domain.gql.searchHint

object KeywordSearchHintQuery {
    val query = """
        {
            universe_placeholder(navsource:"home"){
                data { placeholder keyword }
            }
        }
    """.trimIndent()
}