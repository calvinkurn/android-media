package com.tokopedia.productcard.layout.name

internal interface NameLayoutStrategy {

    fun isSingleLine(willShowVariant: Boolean): Boolean
}
