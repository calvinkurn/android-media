package com.tokopedia.stories.creation.model

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.content.product.picker.seller.model.sort.SortUiModel
import com.tokopedia.graphql.data.GqlParam

/**
 * Created By : Jonathan Darwin on October 13, 2023
 */
data class GetStoryProductEtalaseRequest(
    @SerializedName("req")
    val req: Data,
) : GqlParam {

    data class Data(
        @SerializedName("author")
        val author: Author,

        @SerializedName("filter")
        val filter: Filter,

        @SerializedName("sort")
        val sort: Sort,

        @SerializedName("pagerCursor")
        val pagerCursor: PagerCursor,
    )

    data class Author(
        @SerializedName("ID")
        val authorId: String,

        @SerializedName("type")
        val authorType: Int,
    )

    data class Filter(
        @SerializedName("keyword")
        val keyword: String,

        @SuppressLint("Invalid Data Type")
        @SerializedName("storefrontIDs")
        val storefrontIDs: List<String>,
    )

    data class Sort(
        @SerializedName("ID")
        val sortId: String,

        @SerializedName("value")
        val sortValue: String,
    )

    data class PagerCursor(
        @SerializedName("cursor")
        val cursor: String,

        @SerializedName("limit")
        val limit: Int,
    )

    companion object {
        fun create(
            authorId: String,
            authorType: Int,
            cursor: String,
            limit: Int,
            keyword: String,
            sort: SortUiModel,
            etalaseId: String,
        ) = GetStoryProductEtalaseRequest(
            req = Data(
                author = Author(
                    authorId = authorId,
                    authorType = authorType,
                ),
                filter = Filter(
                    keyword = keyword,
                    storefrontIDs = if (etalaseId.isEmpty()) emptyList() else listOf(etalaseId),
                ),
                sort = Sort(
                    sortId = sort.key,
                    sortValue = sort.direction.value,
                ),
                pagerCursor = PagerCursor(
                    cursor = cursor,
                    limit = limit,
                )
            )
        )
    }
}
