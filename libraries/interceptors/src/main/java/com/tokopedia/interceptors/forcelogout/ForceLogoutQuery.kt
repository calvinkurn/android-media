package com.tokopedia.interceptors.forcelogout

import com.tokopedia.gql_query_annotation.GqlQueryInterface

class ForceLogoutQuery: GqlQueryInterface {

    override fun getOperationNameList(): List<String> = emptyList()

    override fun getQuery(): String =  """
        mutation force_logout(${'$'}userid: String!){
            force_logout_info(userid: ${'$'}userid) {
                is_force_logout
                title
                description
                url
            }
        }
    """.trimIndent()

    override fun getTopOperationName(): String = ""
}