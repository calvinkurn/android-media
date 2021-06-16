package com.tokopedia.tokomart.home.domain.query

internal object GetKeywordSearch {

    val QUERY = """
        query universe_placeholder(${'$'}firstInstall: Boolean, ${'$'}uniqueId:String){
            universe_placeholder(navsource:"tokonow", first_install:${'$'}firstInstall, unique_id:${'$'}uniqueId){
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