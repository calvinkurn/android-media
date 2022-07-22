package com.tokopedia.search.result.product.separator

interface VerticalSeparable {
    val hasTopSeparator: Boolean
    val hasBottomSeparator: Boolean

    object None: VerticalSeparable {
        override val hasTopSeparator = false
        override val hasBottomSeparator = false
    }

    object Top : VerticalSeparable {
        override val hasTopSeparator = true
        override val hasBottomSeparator = false
    }

    object Bottom : VerticalSeparable {
        override val hasTopSeparator = false
        override val hasBottomSeparator = true
    }

    object Both : VerticalSeparable {
        override val hasTopSeparator = true
        override val hasBottomSeparator = true
    }
}

