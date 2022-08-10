package com.tokopedia.search.result.product.separator

interface VerticalSeparable {
    val verticalSeparator: VerticalSeparator

    fun addTopSeparator(): VerticalSeparable

    fun addBottomSeparator(): VerticalSeparable
}

sealed class VerticalSeparator {
    abstract val hasTopSeparator: Boolean
    abstract val hasBottomSeparator: Boolean

    object None : VerticalSeparator() {
        override val hasTopSeparator = false
        override val hasBottomSeparator = false
    }

    object Top : VerticalSeparator() {
        override val hasTopSeparator = true
        override val hasBottomSeparator = false
    }

    object Bottom : VerticalSeparator() {
        override val hasTopSeparator = false
        override val hasBottomSeparator = true
    }

    object Both : VerticalSeparator() {
        override val hasTopSeparator = true
        override val hasBottomSeparator = true
    }
}

