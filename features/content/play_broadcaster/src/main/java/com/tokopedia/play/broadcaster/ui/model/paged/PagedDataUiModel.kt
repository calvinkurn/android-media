package com.tokopedia.play.broadcaster.ui.model.paged

/**
 * Created by kenny.hadisaputra on 07/02/22
 */
data class PagedDataUiModel<T: Any>(
    val dataList: List<T>,
    val hasNextPage: Boolean,
)