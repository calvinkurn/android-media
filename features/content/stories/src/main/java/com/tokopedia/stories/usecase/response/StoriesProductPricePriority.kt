package com.tokopedia.stories.usecase.response

enum class StoriesProductFormatPriority {
    Masked, Discount, Original;

    companion object {
        fun getFormatPriority(formatPriority: String): StoriesProductFormatPriority =
            when (formatPriority.lowercase()) {
                "masked" -> Masked
                "discount" -> Discount
                else -> Original
            }
    }
}
