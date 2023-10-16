package com.tokopedia.content.product.picker.seller.model.result

/**
 * Created by jegul on 03/06/20
 */
sealed class PageResultState {
    data class Success(val hasNextPage: Boolean) : PageResultState()
    object Loading : PageResultState()
    data class Fail(val error: Throwable) : PageResultState()
}
