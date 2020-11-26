package com.tokopedia.home.beranda.domain.gql.searchHint

object KeywordSearchHintQuery {
    private const val firstInstall = "\$firstInstall"

    val query = """
        query universe_placeholder($firstInstall: Boolean){
            universe_placeholder(navsource:"home", first_install:$firstInstall){
                data { 
                    placeholder 
                    keyword 
                    placeholder_list {
                        placeholder
                        keyword
                    }
                }
            }
        }
    """.trimIndent()
}