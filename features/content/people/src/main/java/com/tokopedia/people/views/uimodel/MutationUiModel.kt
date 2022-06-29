package com.tokopedia.people.views.uimodel

/**
 * Created By : Jonathan Darwin on June 29, 2022
 */
sealed interface MutationUiModel {

    object Success: MutationUiModel
    data class Error(val message: String): MutationUiModel
}