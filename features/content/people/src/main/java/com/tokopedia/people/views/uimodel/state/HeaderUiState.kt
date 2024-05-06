package com.tokopedia.people.views.uimodel.state

/**
 * Created by Jonathan Darwin on 28 February 2024
 */
sealed interface HeaderUiState {

    data class ShowUserInfo(
        val name: String,
        val username: String,
    ) : HeaderUiState

    object HideUserInfo : HeaderUiState
}
