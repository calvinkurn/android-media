package com.tokopedia.content.product.preview.view.uimodel

import androidx.annotation.StringRes

/**
 * @author by astidhiyaa on 12/12/23
 */
sealed interface ProductPreviewEvent {
    data class LoginEvent<T>(val data: T): ProductPreviewEvent
    data class ShowErrorEvent(val message: Throwable) : ProductPreviewEvent
    data class ShowInfoEvent(@StringRes val message: Int) : ProductPreviewEvent
}


