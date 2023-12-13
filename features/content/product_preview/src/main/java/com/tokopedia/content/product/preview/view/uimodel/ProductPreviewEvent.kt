package com.tokopedia.content.product.preview.view.uimodel

import com.tokopedia.content.common.report_content.model.ContentMenuItem

/**
 * @author by astidhiyaa on 12/12/23
 */
sealed interface ProductPreviewEvent {
    data class LoginEvent<T>(val data: T) : ProductPreviewEvent
    data class ShowSuccessToaster(
        val message: String? = null,
        val type: Type,
    ) : ProductPreviewEvent {
        enum class Type {
            ATC,
            Report,
            Unknown;
        }
    }

    data class ShowErrorToaster(
        val message: Throwable,
        val onClick: () -> Unit
    ) : ProductPreviewEvent

    data class NavigateEvent(
        val appLink: String
    ) : ProductPreviewEvent

    data class ShowMenuSheet(
        val menus: List<ContentMenuItem>,
    ) : ProductPreviewEvent

    data class LikeUpdate(
        val state: LikeUiState
    ) : ProductPreviewEvent
}


