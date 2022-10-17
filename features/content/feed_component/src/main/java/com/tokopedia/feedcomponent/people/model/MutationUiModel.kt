package com.tokopedia.feedcomponent.people.model

/**
 * Created By : Jonathan Darwin on June 29, 2022
 */
sealed interface MutationUiModel {
    data class Success(val message: String = "") : MutationUiModel
    data class Error(val message: String) : MutationUiModel
}
