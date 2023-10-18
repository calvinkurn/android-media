package com.tokopedia.people.views.uimodel.content

import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel

/**
 * Created By : Jonathan Darwin on February 10, 2023
 */
data class UserPlayVideoUiModel(
    val items: List<PlayWidgetChannelUiModel>,
    val nextCursor: String,
    val status: Status,
) {

    val isLoading: Boolean
        get() = status == Status.Loading

    enum class Status {
        Loading, Success, Error, Unknown
    }

    companion object {
        val Empty: UserPlayVideoUiModel
            get() = UserPlayVideoUiModel(
                items = emptyList(),
                nextCursor = "",
                status = Status.Unknown,
            )
    }
}
