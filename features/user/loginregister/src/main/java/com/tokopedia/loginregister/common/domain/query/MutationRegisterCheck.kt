package com.tokopedia.loginregister.common.domain.query

/**
 * Created by Ade Fulki on 2019-12-27.
 * ade.hadian@tokopedia.com
 */

object MutationRegisterCheck {

    private const val id = "\$id"

    fun getQuery(): String = """
        mutation register_check ($id: String!){
            registerCheck(id: $id) {
                isExist
                isPending
                status
                registerType
                userID
                view
                errors
                uh
                registerOvoEnable
            }
        }
    """.trimIndent()
}