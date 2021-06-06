package com.tokopedia.sessioncommon.domain.query

object LoginQueries {
    val registerCheckQuery = """
        mutation register_check (${'$'}id: String!){
            registerCheck(id: ${'$'}id) {
                isExist
                isPending
                registerOvoEnable
                status
                registerType
                userID
                view
                uh
                errors
            }
        }""".trimIndent()
}