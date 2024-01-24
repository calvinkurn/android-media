package com.tokopedia.content.analytic.model

/**
 * Created By : Jonathan Darwin on January 24, 2024
 */
data class ContentAnalyticAuthor(
    val id: String,
    val type: String,
) {
    companion object {
        val Empty: ContentAnalyticAuthor
            get() = ContentAnalyticAuthor(
                id = "",
                type = "",
            )
    }
}
