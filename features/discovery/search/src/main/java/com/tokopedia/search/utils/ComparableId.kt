package com.tokopedia.search.utils

interface ComparableId {

    val id: String

    fun compare(toCompare: ComparableId): Boolean = this.id == toCompare.id
}
