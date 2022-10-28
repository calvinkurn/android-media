package com.tokopedia.interceptors.forcelogout

import com.tokopedia.gql_query_annotation.GqlQueryInterface

class ForceLogoutQuery: GqlQueryInterface {

    override fun getOperationNameList(): List<String> = emptyList()

    override fun getQuery(): String =  """
        query force_logout(${'$'}user_id: Int!){
            force_logout_info(user_id: ${'$'}user_id) {
                is_force_logout
                title
                description
                url
            }
        }
    """.trimIndent()

    override fun getTopOperationName(): String = ""
}