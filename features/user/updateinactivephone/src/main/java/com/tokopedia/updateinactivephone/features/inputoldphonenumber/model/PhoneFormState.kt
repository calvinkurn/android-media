package com.tokopedia.updateinactivephone.features.inputoldphonenumber.model

data class PhoneFormState(
    var numberError: Int = DEFAULT_NUMBER_ERROR,
    var isDataValid: Boolean = false
) {
    companion object {
        const val DEFAULT_NUMBER_ERROR = 0
    }
}