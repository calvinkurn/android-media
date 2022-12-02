package com.tokopedia.product_ar.model.state

data class ArGlobalErrorState(
        val state: ArGlobalErrorMode = ArGlobalErrorMode.SUCCESS,
        val throwable: Throwable? = null
)

enum class ArGlobalErrorMode {
    ERROR,
    SUCCESS
}
