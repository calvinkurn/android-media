package com.tokopedia.loginregister.redefine_register_email.view.viewmodel

data class FormState (
    var emailError: Int = -1,
    var passwordError: Int = -1,
    var nameError: Int = -1,
    var isAllDataValid: Boolean = false
)