package com.tokopedia.product.detail.view.viewholder.bmgm.model

/**
 * Created by yovi.putra on 28/07/23"
 * Project name: android-tokopedia-core
 **/

sealed interface BMGMUiState {

    object Loading : BMGMUiState

    object Hide : BMGMUiState

    data class Show(val uiModel: BMGMUiModel) : BMGMUiState
}
