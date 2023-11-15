package com.tokopedia.loginregister.registerinitial.di

object RegisterInitialQueryConstant {
    const val MUTATION_REGISTER_CHECK = "register_check"
    const val MUTATION_REGISTER_REQUEST = "register_request"
    const val MUTATION_REGISTER_REQUEST_SCP = "register_request_scp"
    const val MUTATION_ACTIVATE_USER = "activate_user"

    const val PARAM_ID = "id"
    const val PARAM_EMAIL = "email"
    const val PARAM_VALIDATE_TOKEN = "validate_token"
    const val PARAM_GOTO_VERIFICATION_TOKEN = "goto_verification_token"
    const val PARAM_REG_TYPE = "reg_type"
    const val PARAM_PHONE = "phone"
    const val PARAM_FULLNAME = "fullname"
    const val PARAM_PASSWORD = "password"
    const val PARAM_OS_TYPE = "os_type"
    const val PARAM_HASH = "h"

    fun getRegisterRequest(isScpToken: Boolean): String {
        return if (isScpToken) {
            MUTATION_REGISTER_REQUEST_SCP
        } else {
            MUTATION_REGISTER_REQUEST
        }
    }
}
