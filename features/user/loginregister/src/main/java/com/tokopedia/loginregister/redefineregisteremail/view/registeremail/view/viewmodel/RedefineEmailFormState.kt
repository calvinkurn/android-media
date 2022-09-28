package com.tokopedia.loginregister.redefineregisteremail.view.registeremail.view.viewmodel

import com.tokopedia.loginregister.redefineregisteremail.common.RedefineRegisterEmailConstants.INITIAL_RESOURCE

data class RedefineEmailFormState (
    var emailError: Int = INITIAL_RESOURCE,
    var passwordError: Int = INITIAL_RESOURCE,
    var nameError: Int = INITIAL_RESOURCE,
    var isAllDataValid: Boolean = false
)