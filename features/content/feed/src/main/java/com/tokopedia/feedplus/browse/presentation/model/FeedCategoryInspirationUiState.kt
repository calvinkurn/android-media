package com.tokopedia.feedplus.browse.presentation.model

/**
 * Created by kenny.hadisaputra on 22/09/23
 */
internal data class FeedCategoryInspirationUiState(
    val itemList: List<FeedCategoryInspirationModel>,
) {
    companion object {
        val Default: FeedCategoryInspirationUiState
            get() = FeedCategoryInspirationUiState(
                itemList = emptyList()
            )
    }
}
