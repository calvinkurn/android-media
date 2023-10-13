package com.tokopedia.stories.creation.model

import com.tokopedia.content.product.picker.seller.model.sort.SortUiModel

/**
 * Created By : Jonathan Darwin on October 13, 2023
 */
data class GetStoryProductEtalaseRequest(
    val authorId: String,
    val authorType: Int,
    val cursor: String,
    val limit: Int,
    val keyword: String,
    val sort: SortUiModel,
    val etalaseId: String,
) {
    fun buildRequestParam(): Map<String, Any> {
        return mapOf(
            "req" to mapOf(
                "author" to mapOf(
                    "ID" to authorId,
                    "type" to authorType
                ),
                "filter" to mapOf(
                    "keyword" to keyword,
                    "storefrontIDs" to if (etalaseId.isEmpty()) emptyList<String>() else listOf(etalaseId),
                ),
                "sort" to mapOf(
                    "ID" to sort.key,
                    "value" to sort.direction.value,
                ),
                "pagerCursor" to mapOf(
                    "cursor" to cursor,
                    "limit" to limit,
                )
            )
        )
    }
}
