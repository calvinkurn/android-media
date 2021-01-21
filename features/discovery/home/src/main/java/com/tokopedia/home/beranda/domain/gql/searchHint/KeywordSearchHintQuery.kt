package com.tokopedia.home.beranda.domain.gql.searchHint

object KeywordSearchHintQuery {
    private const val firstInstall = "\$firstInstall"
    private const val uniqueId = "\$uniqueId"

    val query = """
        query universe_placeholder($firstInstall: Boolean, $uniqueId:String){
            universe_placeholder(navsource:"home", first_install:$firstInstall, unique_id:$uniqueId){
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