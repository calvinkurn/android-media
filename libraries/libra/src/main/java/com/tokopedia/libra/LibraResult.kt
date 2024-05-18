package com.tokopedia.libra

data class LibraResult(
    val experiment: String,
    val variant: String
) {

    companion object {
        fun empty() = LibraResult("", "")
    }
}
