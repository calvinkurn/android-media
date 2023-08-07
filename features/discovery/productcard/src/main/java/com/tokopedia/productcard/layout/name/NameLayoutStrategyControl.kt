package com.tokopedia.productcard.layout.name

internal class NameLayoutStrategyControl: NameLayoutStrategy {

    override fun isSingleLine(willShowVariant: Boolean): Boolean = willShowVariant
}
