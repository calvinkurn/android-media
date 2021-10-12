package com.tokopedia.graphql.domain.example

import java.util.*
import com.tokopedia.graphql.data.GqlParam

data class FooModel(
    val id: Int,
    val msg: String
) : GqlParam

data class FooInput(val id: Int, val isSorted: Boolean) {
    fun toMap(): Map<String, Any> = mapOf(
        "id" to id,
        "isSorted" to isSorted
    )
}

data class NestedFooModel(
    val id: Int,
    val msg: String?,
    val foo: FooModel
)

data class NumberFooModel(
    val id: Number
)
