package com.tokopedia.content.product.preview.view.uimodel

import com.tokopedia.content.product.preview.R

/**
 * @author by astidhiyaa on 12/12/23
 */
sealed interface ProductPreviewEvent {
    data class LoginEvent<T>(val data: T) : ProductPreviewEvent
    data class ShowSuccessToaster(
        val message: Int? = null,
        val type: Type,
    ) : ProductPreviewEvent {
        enum class Type(val textRes: Int) {
            ATC(R.string.bottom_atc_success_toaster), Remind(R.string.bottom_wishlist_toaster), Unknown(0);
        }
    }

    data class ShowErrorToaster(
        val message: Throwable,
        val onClick: () -> Unit
    ) : ProductPreviewEvent

    data class NavigateEvent(
        val appLink: String
    ) : ProductPreviewEvent
}


