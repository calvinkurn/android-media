package com.tokopedia.graphql.domain.example

import java.util.*

data class FooModel(val id: Int, val msg: String)

data class FooInput(val id: Int, val isSorted: Boolean) {
    fun toMap(): Map<String, Any> = mapOf(
        "id" to id,
        "isSorted" to isSorted
    )
}

data class NestedFooModel(
    val id: Int,
    val msg: String?,
    val foo: FooModel)

data class NumberFooModel(
    val id: Number
)
