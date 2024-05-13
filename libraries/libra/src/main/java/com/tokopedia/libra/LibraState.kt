package com.tokopedia.libra

sealed class LibraState {

    object Control : LibraState()
    data class Variant(val name: String) : LibraState()
}
