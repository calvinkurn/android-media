package com.tokopedia.content.analytic.helper

import com.tokopedia.content.analytic.model.ContentAnalyticAuthor

/**
 * Created By : Jonathan Darwin on January 24, 2024
 */
object ContentAnalyticHelper {

    fun concatLabelsWithAuthor(author: ContentAnalyticAuthor, vararg labels: String): String {
        return concatLabels(author.id, author.type, *labels)
    }

    fun concatLabels(
        vararg labels: String
    ): String {
        return buildString {
            for (i in labels.indices) {
                append(labels[i])

                if (i != labels.size-1) {
                    append(" - ")
                }
            }
        }
    }
}
