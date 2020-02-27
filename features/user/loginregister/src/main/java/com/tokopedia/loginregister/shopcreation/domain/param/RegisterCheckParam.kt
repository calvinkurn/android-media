package com.tokopedia.loginregister.shopcreation.domain.param

/**
 * Created by Ade Fulki on 2019-12-27.
 * ade.hadian@tokopedia.com
 */

data class RegisterCheckParam @JvmOverloads constructor(
        val id: String = ""
) {

    fun toMap(): Map<String, Any> = mapOf(
            PARAM_ID to id
    )

    companion object {
        private const val PARAM_ID = "id"
    }
}