package com.tokopedia.content.product.preview.view.uimodel.review

/**
 * @author by astidhiyaa on 15/01/24
 */
sealed interface ReviewPaging {
    data class Success(val page: Int, val hasNextPage: Boolean) : ReviewPaging
    data class Error(val throwable: Throwable, val onRetry: () -> Unit = {}) : ReviewPaging
    object Load : ReviewPaging
    object Unknown : ReviewPaging
}
