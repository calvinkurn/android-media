package com.tokopedia.feed.component.product

import com.tokopedia.content.common.types.ResultState
import com.tokopedia.content.common.view.ContentTaggedProductUiModel

/**
 * @author by astidhiyaa on 4/22/24
 */
data class FeedProductPaging(
    val state: ResultState,
    val products: List<ContentTaggedProductUiModel>,
    val cursor: String,
) {
    val hasNextPage: Boolean get() = this.cursor.isNotEmpty()

    companion object {
        val Empty: FeedProductPaging
            get() = FeedProductPaging(ResultState.Loading, emptyList(), "")
    }
}
