package com.tokopedia.targetedticker.domain

data class TargetedTickerParamModel(
    val page: String = "",
    val targets: List<Target> = listOf()
) {
    data class Target(
        val type: String = "",
        val value: List<String> = listOf()
    )
}
