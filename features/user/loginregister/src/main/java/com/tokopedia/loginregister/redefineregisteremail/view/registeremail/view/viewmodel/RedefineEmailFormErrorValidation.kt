package com.tokopedia.loginregister.redefineregisteremail.view.registeremail.view.viewmodel

data class RedefineEmailFormErrorValidation (
    var emailError: Int = -1,
    var passwordError: Int = -1,
    var nameError: Int = -1,
    var isAllDataValid: Boolean = false
)