package com.tokopedia.common_digital.product.presentation.model

enum class ClientNumberType(val value: String) {
    TYPE_INPUT_TEL("tel"),
    TYPE_INPUT_NUMERIC("numeric"),
    TYPE_INPUT_ALPHANUMERIC("alphanumeric"),
    DEFAULT_TYPE_CONTRACT("client_number")
}