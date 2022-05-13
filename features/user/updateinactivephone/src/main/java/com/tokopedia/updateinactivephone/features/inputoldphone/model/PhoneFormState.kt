package com.tokopedia.updateinactivephone.features.inputoldphone.model

data class PhoneFormState(
    var numberError: Int = DEFAULT_NUMBER_ERROR,
    var isDataValid: Boolean = false
) {
    companion object {
        const val DEFAULT_NUMBER_ERROR = 0
    }
}