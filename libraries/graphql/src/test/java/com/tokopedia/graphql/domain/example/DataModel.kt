package com.tokopedia.graphql.domain.example

import com.tokopedia.graphql.data.GqlParam

data class FooModel(
    val id: Int,
    val msg: String
) : GqlParam

data class NestedFooModel(
    val id: Int,
    val msg: String?,
    val foo: FooModel
) : GqlParam

data class NumberFooModel(
    val id: Number
) : GqlParam
