package com.tokopedia.tokopedianow.home.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

internal object GetKeywordSearch: GqlQueryInterface {

    private const val OPERATION_NAME = "universe_placeholder"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """
        query $OPERATION_NAME(${'$'}firstInstall: Boolean, ${'$'}uniqueId:String){
            $OPERATION_NAME(navsource:"tokonow", first_install:${'$'}firstInstall, unique_id:${'$'}uniqueId){
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

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}