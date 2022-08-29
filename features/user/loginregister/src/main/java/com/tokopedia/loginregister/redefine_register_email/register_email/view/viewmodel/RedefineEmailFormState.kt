package com.tokopedia.loginregister.redefine_register_email.register_email.view.viewmodel

data class RedefineEmailFormState (
    var emailError: Int = -1,
    var passwordError: Int = -1,
    var nameError: Int = -1,
    var isAllDataValid: Boolean = false
)