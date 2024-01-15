package com.tokopedia.content.product.preview.view.uimodel

/**
 * @author by astidhiyaa on 15/01/24
 */
sealed interface PageState {
    data class Success(val page: Int, val hasNextPage: Boolean) : PageState
    data class Error(val throwable: Throwable) : PageState
    object Load : PageState
    object Unknown : PageState
}

