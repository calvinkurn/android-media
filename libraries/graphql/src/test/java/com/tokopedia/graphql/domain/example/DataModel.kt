package com.tokopedia.graphql.domain.example

data class FooModel(val id: Int, val msg: String)

data class FooInput(val id: Int, val isSorted: Boolean) {
    fun toMap(): Map<String, Any> = mapOf(
        "id" to id,
        "isSorted" to isSorted
    )
}